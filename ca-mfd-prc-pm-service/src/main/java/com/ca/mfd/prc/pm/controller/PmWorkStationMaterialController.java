package com.ca.mfd.prc.pm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ClassUtil;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.ManyStationMaterialRelaDTO;
import com.ca.mfd.prc.pm.entity.*;
import com.ca.mfd.prc.pm.mapper.IPmLineMapper;
import com.ca.mfd.prc.pm.service.*;
import com.google.common.util.concurrent.AtomicDouble;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author inkelink
 * @Description: 工位物料清单Controller
 * @date 2023年09月26日
 * @变更说明 BY inkelink At 2023年09月26日
 */
@RestController
@RequestMapping("pmworkstationmaterial")
@Tag(name = "工位物料清单服务", description = "工位物料清单")
public class PmWorkStationMaterialController extends PmBaseController<PmWorkstationMaterialEntity> {

    private final static Logger logger = LoggerFactory.getLogger(PmWorkStationMaterialController.class);
    private final IPmWorkstationMaterialService pmWorkstationMaterialService;
    private final IPmBopBomService pmBopBomService;
    private final IPmWorkShopService pmWorkShopService;
    private final IPmWorkStationService pmWorkStationService;
    private final IPmVersionService pmVersionService;
    private final IPmLineMapper pmLineMapper;

    protected final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    public PmWorkStationMaterialController(IPmWorkstationMaterialService pmWorkstationMaterialService,
                                           IPmBopBomService pmBopBomService,
                                           IPmWorkShopService pmWorkShopService,
                                           IPmWorkStationService pmWorkStationService,
                                           IPmVersionService pmVersionService,
                                           IPmLineMapper pmLineMapper,
                                           ThreadPoolTaskExecutor pmThreadPoolTaskExecutor) {
        this.crudService = pmWorkstationMaterialService;
        this.pmWorkstationMaterialService = pmWorkstationMaterialService;
        this.pmBopBomService = pmBopBomService;
        this.pmWorkShopService = pmWorkShopService;
        this.pmWorkStationService = pmWorkStationService;
        this.pmVersionService = pmVersionService;
        this.pmLineMapper = pmLineMapper;
        this.threadPoolTaskExecutor = pmThreadPoolTaskExecutor;
    }

    @GetMapping("/provider/getbyworkstationcode")
    @Operation(summary = "根据工位Code 查询工位物料清单")
    public ResultVO<List<PmWorkstationMaterialEntity>> getByWorkstationCode(@NotNull(message = "工位编码不能为空") String workstationCode) {

        // 找关联工位
        List<PmWorkStationEntity> pmWorkStationEntities = pmVersionService.getRelevanceQgWorkplaceByStation(workstationCode);
        QueryWrapper<PmWorkstationMaterialEntity> queryWrapper = new QueryWrapper<>();
        if (CollectionUtils.isNotEmpty(pmWorkStationEntities)) {
            // 绑定关联工位
            List<String> collect = pmWorkStationEntities.stream()
                    .map(PmWorkStationEntity::getWorkstationCode).collect(Collectors.toList());
            queryWrapper.lambda().in(PmWorkstationMaterialEntity::getWorkstationCode, collect);
        } else {
            queryWrapper.lambda().eq(PmWorkstationMaterialEntity::getWorkstationCode, workstationCode);
        }
        List<PmWorkstationMaterialEntity> resultData = pmWorkstationMaterialService.getData(queryWrapper, false);

        return new ResultVO<List<PmWorkstationMaterialEntity>>().ok(resultData, "获取数据成功");
    }


    @PostMapping(value = "batchedit", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "批量更新")
    public ResultVO<?> batchedit(@RequestBody List<PmWorkstationMaterialEntity> dtos) {
        if (!dtos.isEmpty()) {
            Map<String, Object> res = getBomMaterialNumByShopCodeAndMaterialCode(dtos);
            Double bomMaterialNum = (Double) res.get("materialNum");
            String materialName = (String) res.get("materialName");
            String materialNo = (String) res.get("materialNo");
            String usageValue = (String) res.get("usageValue");
            String rowNum = (String) res.get("rowNum");
            String rdSupplyStatus = (String) res.get("rdSupplyStatus");
            String rowNumAndMaterialNo = (String) res.get("rowNumAndMaterialNo");
            batchDel(rowNumAndMaterialNo);
//            AtomicDouble workStationMaterialNum = new AtomicDouble();
//            dtos.stream().forEach(item -> {
//                workStationMaterialNum.addAndGet(item.getMaterialNum() == null ? 0D : item.getMaterialNum());
//            });
//            if (bomMaterialNum != workStationMaterialNum.get()) {
//                throw new InkelinkException(String.format("零件在当前车间的使用总量[%s]和MBOM物料数量[%s]不一致", workStationMaterialNum.get(), bomMaterialNum));
//            }
            for (PmWorkstationMaterialEntity pmWorkstationMaterialEntity : dtos) {
                ClassUtil.defaultValue(pmWorkstationMaterialEntity);
                QueryWrapper<PmWorkStationEntity> qw = new QueryWrapper<>();
                LambdaQueryWrapper<PmWorkStationEntity> lqw = qw.lambda();
                lqw.eq(PmWorkStationEntity::getWorkstationCode, pmWorkstationMaterialEntity.getWorkstationCode());
                PmWorkStationEntity pmWorkStationEntity = pmWorkStationService.getData(qw, false).stream().findFirst().orElse(null);
                if(pmWorkStationEntity == null){
                    throw new InkelinkException(String.format("工位[%s]不存在或已被删除", pmWorkstationMaterialEntity.getWorkstationCode()));
                }
                Long id = crudService.currentModelGetKey(pmWorkstationMaterialEntity);
                if (id == null || id <= 0) {
                    pmWorkstationMaterialEntity.setPrcPmWorkstationId(pmWorkStationEntity.getId());
                    pmWorkstationMaterialEntity.setWorkstationCode(pmWorkStationEntity.getWorkstationCode());
                    pmWorkstationMaterialEntity.setMasterChinese(materialName);
                    pmWorkstationMaterialEntity.setMaterialNo(materialNo);
                    pmWorkstationMaterialEntity.setFeatureCode(usageValue);
                    pmWorkstationMaterialEntity.setRowNumAndMaterialNo(rowNumAndMaterialNo);
                    //BOP行号 attribute1
                    //供货状态  attribute2
                    //用量  attribute3
                    pmWorkstationMaterialEntity.setAttribute1(rowNum);
                    pmWorkstationMaterialEntity.setAttribute2(rdSupplyStatus);
                    pmWorkstationMaterialEntity.setAttribute3(bomMaterialNum.toString());
                    pmWorkstationMaterialEntity.setIsDelete(false);
                    crudService.save(pmWorkstationMaterialEntity);
                } else {
                    QueryWrapper<PmLineEntity> qwline = new QueryWrapper<>();
                    LambdaQueryWrapper<PmLineEntity> lqwline = qwline.lambda();
                    lqwline.eq(PmLineEntity :: getId,pmWorkStationEntity.getPrcPmLineId());
                    lqwline.eq(PmLineEntity :: getVersion,0);
                    PmLineEntity line = pmLineMapper.selectOne(lqwline);
                    UpdateWrapper<PmWorkstationMaterialEntity> qwws = new UpdateWrapper<>();
                    LambdaUpdateWrapper<PmWorkstationMaterialEntity> lqwws = qwws.lambda();
                    lqwws.set(PmWorkstationMaterialEntity :: getPrcPmWorkstationId,pmWorkStationEntity.getId());
                    lqwws.set(PmWorkstationMaterialEntity :: getWorkstationCode,pmWorkStationEntity.getWorkstationCode());
                    lqwws.set(PmWorkstationMaterialEntity :: getPrcPmLineId,pmWorkStationEntity.getPrcPmLineId());
                    lqwws.set(PmWorkstationMaterialEntity :: getLineCode,line.getLineCode());
                    lqwws.set(PmWorkstationMaterialEntity :: getMasterChinese,materialName);
                    lqwws.set(PmWorkstationMaterialEntity :: getMaterialNum,pmWorkstationMaterialEntity.getMaterialNum());
                    lqwws.set(PmWorkstationMaterialEntity :: getMaterialNo,materialNo);
                    lqwws.set(PmWorkstationMaterialEntity :: getFeatureCode,usageValue);
                    lqwws.set(PmWorkstationMaterialEntity :: getRowNumAndMaterialNo,rowNumAndMaterialNo);
                    lqwws.set(PmWorkstationMaterialEntity :: getAttribute1,rowNum);
                    lqwws.set(PmWorkstationMaterialEntity :: getAttribute2,rdSupplyStatus);
                    lqwws.set(PmWorkstationMaterialEntity :: getAttribute3,bomMaterialNum.toString());
                    lqwws.set(PmWorkstationMaterialEntity :: getIsDelete,false);
                    lqwws.eq(PmWorkstationMaterialEntity :: getId,pmWorkstationMaterialEntity.getId());
                    lqwws.eq(PmWorkstationMaterialEntity :: getVersion,pmWorkstationMaterialEntity.getVersion());
                    crudService.update(lqwws);
                }
            }
            crudService.saveChange();
            asyncUpdateUseType(Arrays.asList(rowNumAndMaterialNo));
        }
        return new ResultVO<>().ok(dtos, "保存成功");
    }

    private void batchDel(String rowNumAndMaterialNo){
        UpdateWrapper<PmWorkstationMaterialEntity> uw = new UpdateWrapper<>();
        LambdaUpdateWrapper<PmWorkstationMaterialEntity> luw = uw.lambda();
        luw.set(PmWorkstationMaterialEntity::getIsDelete,true);
        luw.eq(PmWorkstationMaterialEntity::getRowNumAndMaterialNo,rowNumAndMaterialNo);
        luw.eq(PmWorkstationMaterialEntity::getIsDelete,false);
        this.pmWorkstationMaterialService.update(uw);
    }


    @GetMapping(value = "syncusetype", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "批量刷新使用类型")
    public ResultVO<?> syncUseType(@RequestParam(value = "rownum",defaultValue = "") String rownum) {
        List<String> rowNums = null;
        if(StringUtils.isNotBlank(rownum)){
            rowNums = Arrays.asList(rownum);
        }
        doSyncUseType(rowNums);
        return new ResultVO<>().ok(true,"同步执行完毕");
    }

    @GetMapping(value = "asyncAllUseType", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "批量刷新使用类型")
    public void asyncAllUseType() {
        threadPoolTaskExecutor.submit(() -> {
            try{
                doSyncUseType(null);
            }catch (Exception e){
                logger.error("刷新工位物料类型失败:{}",e.getMessage());
            }
        });
    }

    private void doSyncUseType(List<String> rowNums){
        List<PmBopBomEntity> boms = getBopBoms(rowNums);
        if(boms.isEmpty()){
            throw new InkelinkException("未查询到有效的mbom");
        }
        List<String> rowNumAndMaterialNos = boms.stream().map(PmBopBomEntity :: getRowNumAndMaterialNo)
                .collect(Collectors.toList());
        try{
            updateUseType(rowNumAndMaterialNos,boms);
        }catch (Exception e){
            logger.error("同步工位物料'useType'字段失败,失败原因:{}",e.getMessage(),e);
            throw new InkelinkException("同步工位物料'useType'字段失败,失败原因:" + e.getMessage());
        }
    }


    private void asyncUpdateUseType(List<String> rowNumAndMaterialNos){
        threadPoolTaskExecutor.submit(() -> {
            try{
                updateUseType(rowNumAndMaterialNos,null);
            }catch (Exception e){
                logger.error("同步工位物料'useType'字段失败,失败原因:{}",e.getMessage(),e);
            }

        });
    }

    private void updateUseType(List<String> rowNumAndMaterialNos,List<PmBopBomEntity> bopBoms){
        Map<String,Double> quantityInBomByRowNumAndMaterialNo = getQuantityInBom(rowNumAndMaterialNos,bopBoms);
        if(quantityInBomByRowNumAndMaterialNo.isEmpty()){
            logger.info("通过bom行号未查询到mbom数据");
            return;
        }
        Map<String,Integer> workStationCountInStationMaterial = new HashMap<>(rowNumAndMaterialNos.size());
        Map<String,Double> quantityInWorkStationByRowNumAndMaterialNo = getQuantityInStationMaterial(rowNumAndMaterialNos,
                workStationCountInStationMaterial);
        if(quantityInWorkStationByRowNumAndMaterialNo.isEmpty()){
            logger.info("通过bom行号未查询到工位物料数据");
            return;
        }
        Map<String,List<String>> updateUseTypeList = compareQuantity(quantityInBomByRowNumAndMaterialNo,
                quantityInWorkStationByRowNumAndMaterialNo,
                workStationCountInStationMaterial);
        execDb(updateUseTypeList);
    }

    private Map<String,Double> getQuantityInBom(List<String> rowNumAndMaterialNos,
                                                List<PmBopBomEntity> bopBoms){
        //捞出mbom使用数量
        if(bopBoms == null){
            bopBoms = getBopBoms(rowNumAndMaterialNos);
        }
        Map<String,Double> target = new HashMap<>(bopBoms.size());
        for(PmBopBomEntity bopBom : bopBoms ){
            if(StringUtils.isNotBlank(bopBom.getQuantity())){
                target.put(bopBom.getRowNumAndMaterialNo(),Double.valueOf(bopBom.getQuantity()));
            }else{
                target.put(bopBom.getRowNumAndMaterialNo(),Double.valueOf(0));
                logger.error("BOM行号[{}]数量为空",bopBom.getRowNumAndMaterialNo());
            }
        }
        return target;
    }

    private List<PmBopBomEntity> getBopBoms(List<String> rowNumAndMaterialNos){
        List<String> rowNums = null;
        if(rowNumAndMaterialNos != null && !rowNumAndMaterialNos.isEmpty()){
            rowNums = rowNumAndMaterialNos.stream().map(item -> item.split("\\|")[0]).collect(Collectors.toList());
        }
        QueryWrapper<PmBopBomEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<PmBopBomEntity> lqw = qw.lambda();
        if(rowNums != null){
            lqw.in(PmBopBomEntity :: getRowNum,rowNums);
        }
        return pmBopBomService.getData(qw,false);
    }
    private Map<String,Double> getQuantityInStationMaterial(List<String> rowNumAndMaterialNos,Map<String,Integer> workStationCountInStationMaterial){
        QueryWrapper<PmWorkstationMaterialEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkstationMaterialEntity> lqw = qw.lambda();
        lqw.in(PmWorkstationMaterialEntity :: getRowNumAndMaterialNo,rowNumAndMaterialNos);
        List<PmWorkstationMaterialEntity> workstationMaterials = pmWorkstationMaterialService.getData(qw,false);
        Map<String,Double> quantityInStationMaterial = new HashMap<>(workstationMaterials.size());
        if(workstationMaterials.isEmpty()){
            return quantityInStationMaterial;
        }

        for(PmWorkstationMaterialEntity pmWorkstationMaterialEntity : workstationMaterials){
            String rowNumAndMaterialNo = pmWorkstationMaterialEntity.getRowNumAndMaterialNo();
            Double val = quantityInStationMaterial.get(rowNumAndMaterialNo);
            if(val == null){
                val = (double) 0;
            }
            val = val + pmWorkstationMaterialEntity.getMaterialNum();
            quantityInStationMaterial.put(rowNumAndMaterialNo,val);
            Integer ind = workStationCountInStationMaterial.get(rowNumAndMaterialNo);
            if(ind == null){
                ind = 0;
            }
            workStationCountInStationMaterial.put(rowNumAndMaterialNo,++ind);
        }
        return quantityInStationMaterial;
    }

    private Map<String,List<String>> compareQuantity(Map<String,Double> quantityInBomByRowNumAndMaterialNo,
                                                     Map<String,Double> quantityInWorkstationMaterialByRowNumAndMaterialNo,
                                                     Map<String,Integer> workStationCountInStationMaterial){
        Map<String,List<String>> target = new HashMap<>(3);
        if(quantityInBomByRowNumAndMaterialNo.isEmpty() || quantityInWorkstationMaterialByRowNumAndMaterialNo.isEmpty()){
            return target;
        }
        for(Map.Entry<String,Double> item : quantityInBomByRowNumAndMaterialNo.entrySet()){
            Double quantityInWorkstation = quantityInWorkstationMaterialByRowNumAndMaterialNo.get(item.getKey());
            if(quantityInWorkstation == null){
                continue;
            }
            if(quantityInWorkstation.compareTo(item.getValue()) < 0){
                target.computeIfAbsent("2",v -> new ArrayList<>()).add(item.getKey());
            }else if(quantityInWorkstation.compareTo(item.getValue()) == 0){
                int count = workStationCountInStationMaterial.get(item.getKey());
                if(count == 1){
                    target.computeIfAbsent("1",v -> new ArrayList<>()).add(item.getKey());
                }else{
                    target.computeIfAbsent("2",v -> new ArrayList<>()).add(item.getKey());
                }
            }else{
                target.computeIfAbsent("0",v -> new ArrayList<>()).add(item.getKey());
            }
        }
        return target;
    }

    private void execDb(Map<String,List<String>> updateUseTypeList){
        List<String> updateTo0List = updateUseTypeList.get("0");
        List<String> updateTo1List = updateUseTypeList.get("1");
        List<String> updateTo2List = updateUseTypeList.get("2");
        if(updateTo0List != null && !updateTo0List.isEmpty()){
            execDb(updateTo0List,0);
        }
        if(updateTo1List != null && !updateTo1List.isEmpty()){
            execDb(updateTo1List,1);
        }
        if(updateTo2List != null && !updateTo2List.isEmpty()){
            execDb(updateTo2List,2);
        }
        crudService.saveChange();
    }

    private void execDb(List<String> rowNumAndMaterialNos,Integer useType){
        UpdateWrapper<PmWorkstationMaterialEntity> uw = new UpdateWrapper<>();
        LambdaUpdateWrapper<PmWorkstationMaterialEntity> luw = uw.lambda();
        luw.set(PmWorkstationMaterialEntity :: getUseType,useType);
        luw.in(PmWorkstationMaterialEntity :: getRowNumAndMaterialNo,rowNumAndMaterialNos);
        luw.eq(PmWorkstationMaterialEntity :: getIsDelete,false);
        pmWorkstationMaterialService.update(uw);
    }


    @PostMapping(value = "batchmanystationedit", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "关联多物料")
    public ResultVO<?> batchManyStationEdit(@RequestBody ManyStationMaterialRelaDTO dto) {
        if (dto.getMaterialNos() == null || dto.getMaterialNos().isEmpty()) {
            throw new InkelinkException("请至少选择一个物料");
        }
        if (StringUtils.isBlank(dto.getWorkstationCode())) {
            throw new InkelinkException("请选择工位");
        }
        PmWorkStationEntity pmWorkStationEntity = getStationByStationCode(dto.getWorkstationCode());
        Long workshopId = pmWorkStationEntity.getPrcPmWorkshopId();
        PmWorkShopEntity pmWorkShopEntity = getWorkShopById(workshopId);
        List<PmBopBomEntity> boms = getBomByShopCodeAndMaterialNos(pmWorkShopEntity.getWorkshopCode(), dto.getMaterialNos());
        if (boms.isEmpty()) {
            throw new InkelinkException("未查询到物料信息");
        }
        for (PmBopBomEntity pmBopBomEntity : boms) {
            PmWorkstationMaterialEntity pmWorkstationMaterialEntity = new PmWorkstationMaterialEntity();
            pmWorkstationMaterialEntity.setPrcPmWorkshopId(workshopId);
            pmWorkstationMaterialEntity.setPrcPmLineId(pmWorkStationEntity.getPrcPmLineId());
            pmWorkstationMaterialEntity.setPrcPmWorkstationId(pmWorkStationEntity.getId());
            pmWorkstationMaterialEntity.setMaterialNo(pmBopBomEntity.getMaterialCode());
            pmWorkstationMaterialEntity.setRowNumAndMaterialNo(pmBopBomEntity.getRowNumAndMaterialNo());
            pmWorkstationMaterialEntity.setMasterChinese(pmBopBomEntity.getMaterialName());
            pmWorkstationMaterialEntity.setFeatureCode(pmBopBomEntity.getUsageValue());
            pmWorkstationMaterialEntity.setMaterialNum(Double.valueOf(pmBopBomEntity.getQuantity()));

            //BOP行号 attribute1
            //供货状态  attribute2
            //用量  attribute3
            pmWorkstationMaterialEntity.setAttribute1(pmBopBomEntity.getRowNum());
            pmWorkstationMaterialEntity.setAttribute2(pmBopBomEntity.getRdSupplyStatus());
            pmWorkstationMaterialEntity.setAttribute3(pmBopBomEntity.getQuantity());
            pmWorkstationMaterialService.save(pmWorkstationMaterialEntity);
        }
        this.pmWorkstationMaterialService.saveChange();
        this.asyncUpdateUseType(dto.getMaterialNos());
        return new ResultVO<>().ok(null, "保存成功");
    }

    private PmWorkStationEntity getStationByStationCode(String workstationCode) {
        QueryWrapper<PmWorkStationEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkStationEntity> lqw = qw.lambda();
        lqw.eq(PmWorkStationEntity::getWorkstationCode, workstationCode);
        return pmWorkStationService.getData(qw, false).stream().findFirst().orElse(null);
    }

    private PmWorkShopEntity getWorkShopById(Long shopId) {
        return pmWorkShopService.get(shopId, 0);
    }

    private List<PmBopBomEntity> getBomByShopCodeAndMaterialNos(String shopCode, List<String> rowNumAndMaterialNos) {
        List<PmBopBomEntity> boms = new ArrayList<>();
        for(String rowNumAndMaterialNo : rowNumAndMaterialNos){
            String[] rowNumMaterialNos = rowNumAndMaterialNo.split("\\|");
            if(rowNumMaterialNos.length != 2){
                throw new InkelinkException("请传递'materialNos'参数且参数内容须用|分割");
            }
            QueryWrapper<PmBopBomEntity> bbqw = new QueryWrapper<>();
            LambdaQueryWrapper<PmBopBomEntity> blqw = bbqw.lambda();
            blqw.eq(PmBopBomEntity::getUseWorkShop, shopCode);
            blqw.eq(PmBopBomEntity::getRowNum, rowNumMaterialNos[0]);
            blqw.eq(PmBopBomEntity::getMaterialCode, rowNumMaterialNos[1]);
            boms.addAll(pmBopBomService.getData(bbqw, false));
        }
        return boms;
    }


    private Map<String, Object> getBomMaterialNumByShopCodeAndMaterialCode(List<PmWorkstationMaterialEntity> dtos) {
        PmWorkstationMaterialEntity pmWorkstationMaterialEntity = dtos.get(0);
        Long workshopId = pmWorkstationMaterialEntity.getPrcPmWorkshopId();
        if (workshopId == 0) {
            PmWorkStationEntity pmWorkStationEntity = getStationByStationCode(pmWorkstationMaterialEntity.getWorkstationCode());
            workshopId = pmWorkStationEntity.getPrcPmWorkshopId();
        }
        PmWorkShopEntity pmWorkShopEntity = getWorkShopById(workshopId);
        QueryWrapper<PmBopBomEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<PmBopBomEntity> lqw = qw.lambda();
        lqw.eq(PmBopBomEntity::getUseWorkShop, pmWorkShopEntity.getWorkshopCode());
        String[] rowNumAndMaterialNo = pmWorkstationMaterialEntity.getRowNumAndMaterialNo().split("\\|");
        if(rowNumAndMaterialNo.length != 2){
            throw new InkelinkException("请传递'rowNumAndMaterialNo'参数且参数内容须用|分割");
        }
        lqw.eq(PmBopBomEntity::getRowNum, rowNumAndMaterialNo[0]);
        lqw.eq(PmBopBomEntity::getMaterialCode, rowNumAndMaterialNo[1]);
        PmBopBomEntity pmBopBomEntity = pmBopBomService.getData(qw, false).stream().findFirst().orElse(null);
        Map<String, Object> res = new HashMap<>(2);
        res.put("materialNum", Double.valueOf(StringUtils.isBlank(pmBopBomEntity.getQuantity()) ? "0D" : pmBopBomEntity.getQuantity()));
        res.put("materialName", pmBopBomEntity.getMaterialName());
        res.put("materialNo", pmBopBomEntity.getMaterialCode());
        res.put("usageValue", pmBopBomEntity.getUsageValue());
        res.put("rowNum", pmBopBomEntity.getRowNum());
        res.put("rdSupplyStatus", pmBopBomEntity.getRdSupplyStatus());
        res.put("rowNumAndMaterialNo", pmBopBomEntity.getRowNumAndMaterialNo());
        return res;
    }


    @Operation(summary = "根据车间物料编码获取物料信息")
    @GetMapping("/getWorkstationMaterialsByMaterialNoAndShopCode")
    public ResultVO<?> getWorkstationMaterialsByMaterialNoAndShopCode(String materialNo, String workstationCode, String shopCode) {
        return new ResultVO<>().ok(pmWorkstationMaterialService.getWorkstationMaterialsByMaterialNoAndShopCode(materialNo, workstationCode, shopCode), "获取成功");
    }

}