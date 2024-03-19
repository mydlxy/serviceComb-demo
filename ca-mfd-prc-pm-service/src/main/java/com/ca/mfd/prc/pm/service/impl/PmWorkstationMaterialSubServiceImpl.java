package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ArraysUtils;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.FeatureTool;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.SystemUtils;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQConstants;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.SysQueueNoteEntity;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.provider.RabbitMqSysQueueNoteProvider;
import com.ca.mfd.prc.pm.communication.dto.MidBomInfo;
import com.ca.mfd.prc.pm.communication.dto.MidLmsDatasVo;
import com.ca.mfd.prc.pm.communication.dto.MidLmsSigtrueVo;
import com.ca.mfd.prc.pm.communication.dto.WorkstationMaterialDto;
import com.ca.mfd.prc.pm.communication.entity.MidVehicleMasterEntity;
import com.ca.mfd.prc.pm.communication.mapper.IMidVehicleMasterMapper;
import com.ca.mfd.prc.pm.communication.service.IMidMaterialService;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.*;
import com.ca.mfd.prc.pm.service.IPmBopBomService;
import com.ca.mfd.prc.pm.service.IPmProductBomVersionsService;
import com.ca.mfd.prc.pm.service.IPmVersionService;
import com.ca.mfd.prc.pm.service.IPmWorkstationMaterialSubService;
import org.apache.commons.lang3.AnnotationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class PmWorkstationMaterialSubServiceImpl extends
        PmWorkstationMaterialServiceImpl implements IPmWorkstationMaterialSubService {

    @Autowired
    IPmVersionService pmVersionService;

    @Autowired
    private IMidMaterialService materialService;

    @Autowired
    IPmBopBomService pmBopBomService;
    @Autowired
    IPmProductBomVersionsService pmProductBomVersionsService;
    @Autowired
    IMidVehicleMasterMapper midVehicleMasterMapper;
    @Autowired
    private RabbitMqSysQueueNoteProvider sysQueueNoteService;

    @Autowired
    private LocalCache localCache;


    private static final Object lockObj = new Object();

    private static final Object lockObjLms = new Object();

    private final String cacheName = "PRC_PM_WORKSTATION_MATERIAL";
    private final String cacheNameVersions = "PRC_PM_WORKSTATION_MATERIAL_VESION";

    private final String cacheLmsName = "WORKSTATION_MATERIAL_LMS";

    /**
     * 通过缓存查询数据
     *
     * @param sigtrue 令牌
     * @return
     */
    @Override
    public MidLmsDatasVo getPmWorkstationMaterial(String sigtrue) {
        byte[] decodedBytes = Base64.getDecoder().decode(sigtrue);
        String strMessage = StringUtils.toEncodedString(decodedBytes, StandardCharsets.UTF_8);
        String[] sigtrues = strMessage.split("\\|");
        if (sigtrues.length != 6) {
            throw new InkelinkException("令牌格式错误");
        }
        String productCode = sigtrues[0];
        String version = sigtrues[1];
        String type = sigtrues[3];
        String cacheTimes = sigtrues[4];
        String specifyDate = sigtrues[5];
        int cacheNumber = ConvertUtils.tryParse(cacheTimes) == null ? 60 : ConvertUtils.tryParse(cacheTimes);

        String[] shops = sigtrues[2].split(",");
        String[] workShopCode = getShopCodes(shops);

        return getLocalCacheWorkstationMaterial(productCode, workShopCode,
                sigtrue, version, cacheNumber,type,specifyDate);

    }

    private String[] getShopCodes(String[] shops){
        String[] newShops = new String[shops.length];
        int i = 0;
        for (String shop : shops) {
            newShops[i] = shop.trim();
            i++;
        }
        shops = newShops;
        String[] workShopCode = null;
        if (shops.length > 0) {
            List<String> arrayList = new ArrayList<>();
            if (shops.length > 1) {
                for (String items : shops) {
                    if (StringUtils.isNotBlank(items)) {
                        arrayList.add(items);
                    }
                }
                if (!arrayList.isEmpty()) {
                    workShopCode = arrayList.toArray(new String[0]);
                }
            } else {
                String code = shops[0];
                if (!code.equals("all")) {
                    if (StringUtils.isNotBlank(code)) {
                        arrayList.add(code);
                    }
                    if (!arrayList.isEmpty()) {
                        workShopCode = arrayList.toArray(new String[0]);
                    }
                }
            }
        }
        return workShopCode;
    }

    /**
     * 根据整车物料号查询车系信息
     * @param productCode
     * @return
     */
    private String getModelByProductCode(String productCode){
        QueryWrapper<MidVehicleMasterEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<MidVehicleMasterEntity> lqw = qw.lambda();
        lqw.eq(MidVehicleMasterEntity :: getVehicleMaterialNumber,productCode);
        lqw.eq(MidVehicleMasterEntity :: getIsDelete,false);
        MidVehicleMasterEntity midVehicleMasterEntity = midVehicleMasterMapper.selectList(lqw).stream().findFirst()
                .orElse(null);
        if(midVehicleMasterEntity == null
            || StringUtils.isBlank(midVehicleMasterEntity.getBomRoom())){
            throw new InkelinkException("根据整车物料号未查询到车系信息");
        }
        return midVehicleMasterEntity.getBomRoom();
    }

    private MidLmsDatasVo getLocalCacheWorkstationMaterial(String productCode, String[] workShopCode,
                                                           String sigtrue, String version, int cacheNumber,
                                                           String type,String specifyDate) {
        String shopCodes = StringUtils.join(workShopCode, "_");
        String key = cacheLmsName +
                shopCodes +
                productCode +
                version +
                "_" +
                type +
                "_" +
                specifyDate;
        MidLmsDatasVo datas = localCache.getObject(key);
        if (datas == null) {
            synchronized (lockObjLms) {
                datas = localCache.getObject(key);
                if (datas == null) {
                    datas = getWorkstationMaterial(productCode, workShopCode, sigtrue, type, specifyDate);
                    localCache.addObject(key, datas, cacheNumber * 60);
                }
            }
        }
        return datas;
    }

    private MidLmsDatasVo getWorkstationMaterial(String productCode, String[] workShopCode,
                                                 String sigtrue, String type, String specifyDate) {
        String model;
        //电池
        if("2".equals(type)){
            model = productCode;
        }else{
            //获取车系
            model = getModelByProductCode(productCode);
        }
        MidLmsDatasVo midLmsDatasVo = new MidLmsDatasVo();
        List<PmWorkstationMaterialEntity> materialList = getPmMaterial(productCode, workShopCode, model, type, specifyDate);
        List<WorkstationMaterialDto> workstationMaterialDtos = new ArrayList<>();
        if (!materialList.isEmpty()) {
            PmAllDTO pmAllDTO = pmVersionService.getObjectedPm();
            Map<String,PmBopBomEntity> bopBomMap = getBopBoms();
            for (PmWorkstationMaterialEntity workItems : materialList) {
                WorkstationMaterialDto info = new WorkstationMaterialDto();
                PmWorkStationEntity workStationEntitie = pmAllDTO.getStations().stream()
                        .filter(s -> StringUtils.equals(s.getWorkstationCode(), workItems.getWorkstationCode())).findFirst().orElse(null);
                info.setWorkshopId(workItems.getPrcPmWorkshopId());
                info.setWorkshopCode(workItems.getWorkshopCode());
                info.setLineId(workItems.getPrcPmLineId());
                info.setLineCode(workItems.getLineCode());
                info.setWorkstationId(workItems.getPrcPmWorkstationId());
                info.setWorkstationCode(workItems.getWorkstationCode());
                info.setMaterialCode(workItems.getMaterialNo());
                info.setMaterialName(workItems.getMasterChinese());
                info.setWorkstationUseQuantity(workItems.getMaterialNum());
                info.setWorkstationDisplay(workStationEntitie.getDirection());
                info.setWorkstationSeq(workStationEntitie.getWorkstationNo());
                info.setUseType(workItems.getUseType());
                //MBOM行号和物料号
                String rowNumAndMaterialNo = workItems.getRowNumAndMaterialNo();
                if(bopBomMap.containsKey(rowNumAndMaterialNo)){
                    PmBopBomEntity pmBopBomEntity = bopBomMap.get(rowNumAndMaterialNo);
                    info.setManufacturingSupplyStatus(pmBopBomEntity.getManufacturingSupplyStatus());
                    info.setBomRowNum(pmBopBomEntity.getRowNum());
                    info.setCompositesNum(pmBopBomEntity.getCompositesNum());
                    info.setUseWorkshop(pmBopBomEntity.getUseWorkShop());
                    info.setManufacturingWorkshop(pmBopBomEntity.getManufacturingWorkshop());
                } else{
                    continue;
                }
                workstationMaterialDtos.add(info);
            }
        }
        midLmsDatasVo.setSigtrue(sigtrue);
        midLmsDatasVo.setProductCode(productCode);
        midLmsDatasVo.setData(workstationMaterialDtos);
        midLmsDatasVo.setUniqueCode(System.currentTimeMillis());
        return midLmsDatasVo;
    }

    private Map<String,PmBopBomEntity> getBopBoms() {
        List<PmBopBomEntity> bopBoms = pmBopBomService.getData(null);
        Map<String,PmBopBomEntity> bopBomMap = new HashMap<>(bopBoms.size());
        for(PmBopBomEntity pmBopBomEntity : bopBoms){
            bopBomMap.put(pmBopBomEntity.getRowNumAndMaterialNo(),pmBopBomEntity);
        }
        return bopBomMap;
    }

    /**
     * @param productMaterialNo
     * @param workShopCode
     * @param model
     * @return
     */
    @Override
    public List<PmWorkstationMaterialEntity> getPmMaterial(String productMaterialNo, String[] workShopCode,
                                                           String model,String type,String specifyDate) {
        String charsversion = null;
        if(type.equals("1")){
             charsversion = pmProductCharacteristicsVersionsService.getCharacteristicsVersions(productMaterialNo);
        }
        List<PmWorkstationMaterialEntity> allPmMaterial = getPmAllMaterialByproductMaterialNo(productMaterialNo, charsversion, model,type);
        allPmMaterial = matchSingleBom(productMaterialNo, specifyDate, allPmMaterial);
        PmAllDTO allinfo = pmVersionService.getObjectedPm();
        if (workShopCode != null && workShopCode.length > 0) {
            ///根据车间返回
            List<Long> workShopId = allinfo.getShops().stream().filter(x -> Arrays.asList(workShopCode).contains(x.getWorkshopCode()))
                    .map(PmWorkShopEntity::getId).collect(Collectors.toList());
            List<String> listWorkStation = allinfo.getStations().stream().filter(s -> workShopId
                    .contains(s.getPrcPmWorkshopId())).map(PmWorkStationEntity::getWorkstationCode).collect(Collectors.toList());
            return allPmMaterial.stream().filter(s -> listWorkStation.contains(s.getWorkstationCode())).collect(Collectors.toList());
        } else {
            ///返回所有
            List<PmWorkStationEntity> stationEntityList = allinfo.getStations();
            List<String> works = stationEntityList.stream().map(PmWorkStationEntity::getWorkstationCode).collect(Collectors.toList());
            List<String> listWorkStation = allinfo.getStations().stream().map(PmWorkStationEntity::getWorkstationCode).collect(Collectors.toList());
            return allPmMaterial.stream().filter(s -> listWorkStation.contains(s.getWorkstationCode())).collect(Collectors.toList());
        }
    }

    private List<PmWorkstationMaterialEntity> matchSingleBom(String productMaterialNo,String specifyDate,
                                                             List<PmWorkstationMaterialEntity> allPmMaterial){
        if(allPmMaterial.isEmpty()){
            log.error("根据物料号[" + productMaterialNo + "]未匹配到工位物料数据");
            return allPmMaterial;
        }
        List<MidBomInfo> singleBoms = materialService.getSingleBom("CQY",productMaterialNo,specifyDate);
        if(singleBoms.isEmpty()){
            log.error("根据物料号[" + productMaterialNo + "]时间[" + specifyDate + "]从bom里未拉取到物料数据");
            return Collections.emptyList();
        }
        Map<String,List<PmWorkstationMaterialEntity>> workstationMaterialMap = getPmWorkstationMaterialEntityByMaterialNo(allPmMaterial);
        List<PmWorkstationMaterialEntity> targetList = new ArrayList<>(singleBoms.size());
        for(MidBomInfo midBomInfo : singleBoms){
            if(workstationMaterialMap.containsKey(midBomInfo.getMaterialCode())){
                targetList.addAll(workstationMaterialMap.get(midBomInfo.getMaterialCode()));
            }
        }
        return targetList;
    }

    private Map<String,List<PmWorkstationMaterialEntity>> getPmWorkstationMaterialEntityByMaterialNo(List<PmWorkstationMaterialEntity> allPmMaterial){
        Map<String,List<PmWorkstationMaterialEntity>> targetMap = new HashMap<>(allPmMaterial.size());
        for(PmWorkstationMaterialEntity pmWorkstationMaterialEntity : allPmMaterial){
            targetMap.computeIfAbsent(pmWorkstationMaterialEntity.getMaterialNo(),v->new ArrayList<>()).add(pmWorkstationMaterialEntity);
        }
        return targetMap;
    }

    public List<PmWorkstationMaterialEntity> getPmAllMaterialByproductMaterialNo(String productMaterialNo, String version, String model,String type) {
        List<String> versions = pmVersionService.getCurrentVersions().stream().map(c -> c.getVersion().toString()).collect(Collectors.toList());
        String pmver = String.join(".", versions);
        String mcacheKey = cacheName + productMaterialNo + version + pmver;
        List<PmWorkstationMaterialEntity> datas = localCache.getObject(mcacheKey);
        if (datas == null || datas.isEmpty()) {
            synchronized (lockObj) {
                datas = localCache.getObject(mcacheKey);
                if (datas == null || datas.isEmpty()) {
                    //获取车型
                    if (StringUtils.isBlank(model)) {
                        if (ArraysUtils.splitNoEmpty(productMaterialNo, "[\\.\\;]").size() > 2) {
                            List<String> splitStr = ArraysUtils.splitNoEmpty(productMaterialNo, "[\\.\\;]");
                            model = splitStr.get(0);
                        } else {
                            throw new InkelinkException(productMaterialNo + "整车物料号无法长度不对无法获取车型");
                        }
                    }
                    List<PmWorkstationMaterialEntity> listModel = pmVersionService.getObjectedPm().getMaterials();
                    List<PmWorkstationMaterialEntity> book = new ArrayList<>();
                    if("2".equals(type)){
                        String bomVersion = this.pmProductBomVersionsService.getBomVersions(productMaterialNo);
                        List<PmProductBomEntity> pmProductBomEntities = this.pmProductBomVersionsService.getBomData(productMaterialNo,bomVersion);
                        if(!pmProductBomEntities.isEmpty() && !listModel.isEmpty()){
                            filterMaterial(book,pmProductBomEntities,listModel,model);
                        }

                    }else{
                        List<String> characteristicValues = pmProductCharacteristicsVersionsService.getCharacteristicsData(productMaterialNo, version).stream()
                                .map(PmProductCharacteristicsEntity::getProductCharacteristicsValue).collect(Collectors.toList());
                        for (PmWorkstationMaterialEntity item : listModel) {
                            if (FeatureTool.calExpression(item.getFeatureCode(), model, characteristicValues)) {
                                book.add(item);
                            }
                        }
                    }
                    datas = book;
                    localCache.addObject(mcacheKey, datas, -1);
                    List<String> gloagCache = localCache.getObject(cacheNameVersions);
                    if (gloagCache == null) {
                        gloagCache = new ArrayList<>();
                        gloagCache.add(mcacheKey);
                    } else {
                        gloagCache.add(mcacheKey);
                    }
                    localCache.addObject(cacheNameVersions, gloagCache, -1);
                }
            }
        }
        return datas;
    }

    private void filterMaterial(List<PmWorkstationMaterialEntity> books,
                                 List<PmProductBomEntity> pmProductBomEntities,
                                 List<PmWorkstationMaterialEntity> listModel,
                                 String model
                                 ){
       Set<String> productBomMaterialNos =  pmProductBomEntities.stream().map(PmProductBomEntity :: getMaterialNo).collect(Collectors.toSet());
        for(PmWorkstationMaterialEntity pmWorkstationMaterialEntity : listModel){
            String feature = pmWorkstationMaterialEntity.getFeatureCode();
           if(productBomMaterialNos.contains(pmWorkstationMaterialEntity.getMaterialNo())
             && StringUtils.isNotBlank(feature) && model.equals(feature.split(":")[0])){
               books.add(pmWorkstationMaterialEntity);
           }
        }
    }


    /**
     * 发送消息队列
     *
     * @param messages
     */
    @Override
    public void sendCreateLmsSigtrueMes(List<MidLmsSigtrueVo> messages) {
        SysQueueNoteEntity sysQueueNoteEntity = new SysQueueNoteEntity();
        sysQueueNoteEntity.setGroupName(RabbitMQConstants.GROUP_NAME_LMS_WORKSTATIONMATERIAL_SIGTRUE);
        sysQueueNoteEntity.setContent(JsonUtils.toJsonString(messages));
        sysQueueNoteService.addSimpleMessage(sysQueueNoteEntity);
    }
}
