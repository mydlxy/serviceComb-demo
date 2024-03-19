package com.ca.mfd.prc.pm.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.*;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.TreeNode;
import com.ca.mfd.prc.pm.dto.CmcPmAreaVo;
import com.ca.mfd.prc.pm.dto.CmcPmBopVo;
import com.ca.mfd.prc.pm.dto.CmcPmEquipmentPowerVo;
import com.ca.mfd.prc.pm.dto.CmcPmEquipmentVo;
import com.ca.mfd.prc.pm.dto.CmcPmOrganizationVo;
import com.ca.mfd.prc.pm.dto.CmcPmWorkCenterVo;
import com.ca.mfd.prc.pm.dto.CmcPmWorkUnitVo;
import com.ca.mfd.prc.pm.dto.OtDto;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.dto.PmAllVo;
import com.ca.mfd.prc.pm.dto.PmComboDto;
import com.ca.mfd.prc.pm.dto.PmInfo;
import com.ca.mfd.prc.pm.dto.PmVersionDTO;
import com.ca.mfd.prc.pm.dto.PmViewVo;
import com.ca.mfd.prc.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pm.entity.PmBopEntity;
import com.ca.mfd.prc.pm.entity.PmEquipmentEntity;
import com.ca.mfd.prc.pm.entity.PmEquipmentPowerEntity;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmOrganizationEntity;
import com.ca.mfd.prc.pm.entity.PmOtEntity;
import com.ca.mfd.prc.pm.entity.PmPullCordEntity;
import com.ca.mfd.prc.pm.entity.PmSyncEntity;
import com.ca.mfd.prc.pm.entity.PmToolEntity;
import com.ca.mfd.prc.pm.entity.PmToolJobEntity;
import com.ca.mfd.prc.pm.entity.PmVersionEntity;
import com.ca.mfd.prc.pm.entity.PmWoEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationMaterialEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationOperBookEntity;
import com.ca.mfd.prc.pm.mapper.IPmVersionMapper;
import com.ca.mfd.prc.pm.remote.app.cmc.provider.CmcPmCompanyProvider;
import com.ca.mfd.prc.pm.remote.app.cmc.provider.CmcPmFactoryProvider;
import com.ca.mfd.prc.pm.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pm.remote.app.core.provider.SysSequenceNumberProvider;
import com.ca.mfd.prc.pm.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pm.remote.app.core.sys.entity.SysSequenceNumberEntity;
import com.ca.mfd.prc.pm.service.IPmAviParentService;
import com.ca.mfd.prc.pm.service.IPmEquipmentPowerService;
import com.ca.mfd.prc.pm.service.IPmEquipmentService;
import com.ca.mfd.prc.pm.service.IPmLineService;
import com.ca.mfd.prc.pm.service.IPmOtService;
import com.ca.mfd.prc.pm.service.IPmPullCordService;
import com.ca.mfd.prc.pm.service.IPmService;
import com.ca.mfd.prc.pm.service.IPmSyncService;
import com.ca.mfd.prc.pm.service.IPmToolJobService;
import com.ca.mfd.prc.pm.service.IPmToolService;
import com.ca.mfd.prc.pm.service.IPmVersionService;
import com.ca.mfd.prc.pm.service.IPmWoService;
import com.ca.mfd.prc.pm.service.IPmWorkShopService;
import com.ca.mfd.prc.pm.service.IPmWorkStationService;
import com.ca.mfd.prc.pm.service.IPmWorkstationMaterialService;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.naming.LinkException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author inkelink ${email}
 * @Description: 建模版本控制
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmVersionServiceImpl extends AbstractCrudServiceImpl<IPmVersionMapper, PmVersionEntity> implements IPmVersionService {

    private static final Logger logger = LoggerFactory.getLogger(PmVersionServiceImpl.class);
    private static final Random publishVersionRandom = new Random();

    /**
     * 缓存KEY
     */
    private static final String PM_ALL_CACHE_NAME = "ALL_PRC_PM_VERSION";
    private static final String PM_ALL_CACHE_NAME_VO = "ALL_PRC_PM_VERSION_VO";
    private static final String CACHE_NAME = "PRC_PM_VERSION";

    /**
     * 锁
     */
    private static final Object LOCK_OBJ = new Object();

    @Autowired
    private IPmService pmService;
    @Autowired
    private LocalCache localCache;
    @Autowired
    private IPmVersionMapper versionMapper;
    @Autowired
    private IdentityHelper identityHelper;
    @Autowired
    private SysSequenceNumberProvider sysSequenceNumberService;
    @Autowired
    private SysConfigurationProvider sysConfigurationService;
    @Autowired
    private IPmWorkShopService workShopService;
    @Autowired
    private IPmLineService lineService;
    @Autowired
    private IPmWorkStationService workStationService;
    @Autowired
    private IPmAviParentService pmAviParentService;
    @Autowired
    private IPmOtService otService;
    @Autowired
    private IPmWoService woService;
    @Autowired
    private IPmToolService toolService;
    @Autowired
    private IPmToolJobService toolJobService;
    @Autowired
    private IPmPullCordService pullCordService;
    @Autowired
    private IPmEquipmentService equipmentService;
    @Autowired
    private IPmEquipmentPowerService pmEquipmentPowerService;
    @Autowired
    private IPmSyncService pmSyncService;
    @Autowired
    private IPmWorkstationMaterialService pmWorkstationMaterialService;
    @Autowired
    private CmcPmFactoryProvider cmcPmFactoryService;
    @Autowired
    private CmcPmCompanyProvider cmcPmCompanyService;
    @Autowired
    private RedisUtils redisUtils;

    @Qualifier("pmThreadPoolTaskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Value("${inkelink.canSyncCmc:true}")
    private boolean canSyncCmc = true;


    @Override
    public PageData<PmVersionEntity> page(PageDataDto model) {
        List<String> columns = new ArrayList<>();
        columns.add("PRC_PM_VERSION_ID AS ID");
        columns.add("WORKSHOP_CODE");
        columns.add("WORKSHOP_NAME");
        columns.add("VERSION");
        columns.add("BEGIN_DT");
        columns.add("BEGIN_USER_NAME");
        columns.add("BEGIN_DT");
        columns.add("END_DT");
        columns.add("END_USER_NAME");
        columns.add("IS_ENABLED");
        columns.add("REMARK");
        columns.add("IS_DOWN_OT");
        columns.add("IS_DOWN_ANDON");
        return page(model, columns);
    }

    @Override
    public PmAllDTO getObjectedPm() {
        PmAllDTO data = localCache.getObject(PM_ALL_CACHE_NAME);
        if (data != null) {
            return data;
        }
        synchronized (LOCK_OBJ) {
            data = localCache.getObject(PM_ALL_CACHE_NAME);
            if (data != null) {
                return data;
            }
            List<PmVersionEntity> cvs = this.getCurrentVersions();
            String version = String.join(".", cvs.stream().map(item -> item.getVersion().toString()).collect(Collectors.toList()));
            data = new PmAllDTO();
            data.setVersion(version);
            for (PmVersionEntity item : cvs) {
                String key = String.format("Pm:%s-%s", item.getWorkshopCode(), item.getVersion());
                Map<String, Object> mapOfPmInfo = (Map) redisUtils.get(key);
                PmInfo info;
                if (mapOfPmInfo == null || mapOfPmInfo.isEmpty()) {
                    PmVersionEntity pmVersionEntity = this.versionMapper.selectById(item.getId());
                    String content = pmVersionEntity.getContent();
                    item.setContent(content);
                    if (StringUtils.isBlank(content)) {
                        throw new InkelinkException(String.format("车间[%s]版本号[%s]版本内容为空", item.getWorkshopCode(), item.getVersion()));
                    } else {
                        info = resetWorkshopAndVersionRedisCache(item);
                    }
                } else {
                    info = new PmInfo();
                    info = JsonUtils.parseObject(JsonUtils.toJsonString(mapOfPmInfo), PmInfo.class);
                    //BeanUtil.fillBeanWithMap(mapOfPmInfo,info,false);
                }
                data.addPmInfo(info);
            }
            //data.setOrganization(pmOrganizationService.get(data.getShops().get(0).getPrcPmOrganizationId()));
            localCache.addObject(PM_ALL_CACHE_NAME, data, -1);
        }
        return data;
    }


    private PmInfo convertJsonStringToPmInfo(PmVersionEntity pmVersionEntity) {
        String content = pmVersionEntity.getContent();
        if (StringUtils.isBlank(content)) {
            throw new InkelinkException(String.format("车间[%s]版本号[%s]版本内容为空", pmVersionEntity.getWorkshopCode(), pmVersionEntity.getVersion()));
        }
        PmInfo pmInfo;
        try {
            pmInfo = JsonUtils.parseObject(pmVersionEntity.getContent(), PmInfo.class);
        } catch (Exception e) {
            throw new InkelinkException(String.format("车间[%s]版本号[%s]版本内容转成java对象失败", pmVersionEntity.getWorkshopCode(), pmVersionEntity.getVersion()));
        }
        return pmInfo;
    }

    @Override
    public List<PmVersionEntity> getCurrentVersions() {
        QueryWrapper<PmVersionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(PmVersionEntity::getId, PmVersionEntity::getVersion, PmVersionEntity::getWorkshopCode);
        queryWrapper.eq("IS_ENABLED", true);
        return this.selectList(queryWrapper);
    }

    /**
     * 根据工位编号获取关联的QG岗位信息
     */
    @Override
    public List<String> getRelevanceQgWorkplaceByStationId(String stationId) {
        PmAllDTO pmAll = getObjectedPm();
        List<PmWorkStationEntity> qgWorkStationInfos = pmAll.getStations().stream().filter(c -> c.getWorkstationType() == 2 && c.getStations().contains(stationId.toLowerCase())).collect(Collectors.toList());
        if (qgWorkStationInfos.isEmpty()) {
            return Arrays.asList(Constant.EMPTY_ID, "");
        } else {
            return Arrays.asList(Constant.EMPTY_ID, String.join(",", qgWorkStationInfos.stream().map(PmWorkStationEntity::getWorkstationName).collect(Collectors.toList())));
        }

    }

    @Override
    public Document getCurretPm() {
        PmAllDTO pmAllDTO = getObjectedPm();
        if (pmAllDTO == null) {
            throw new InkelinkException("未获取到基础信息");
        }
        List<PmWorkShopEntity> shoplist = pmAllDTO.getShops();
        if (shoplist == null || shoplist.size() == 0) {
            throw new InkelinkException("未获取到车间基础信息");
        }
        List<PmWorkShopEntity> shops = shoplist.stream().sorted(Comparator.comparing(PmWorkShopEntity::getDisplayNo))
                .collect(Collectors.toList());
        //创建一个 xml 对象
        Document result = DocumentHelper.createDocument();
        Element root = DocumentHelper.createElement("PmInfo");
        List<String> version = new ArrayList<>();
        List<String> code = new ArrayList<>();
        for (PmWorkShopEntity item : shops) {
            try {
                Document xdoc = getCurretShopPm(item.getWorkshopCode());
                Element pm = xdoc.getRootElement();
                Attribute attributeVersion = pm.attribute("PMVersion");
                String shopVersion = attributeVersion.getValue();
                Element shop = pm.element("PmShopInfo");
                Attribute attributeShop = shop.attribute("Code");
                String shopCode = attributeShop.getValue();
                version.add(shopVersion);
                code.add(shopCode);
                root.add((Element) shop.clone());
            } catch (Exception exception) {

            }
        }
        root.addAttribute("PMVersion", String.join(".", version));
        root.addAttribute("PMCode", String.join(".", code));
        result.add(root);
        return result;
    }

    @Override
    public ResultVO publishShopPm(PmVersionDTO model) {
        JSONObject jsonContent = pmService.generationJson(model.getShopId());
        //查询最大版本，包含删除的，不然会报主键唯一性冲突
        LambdaQueryWrapper<PmVersionEntity> pm = new LambdaQueryWrapper<>();
        pm.eq(PmVersionEntity::getWorkshopCode, model.getShopCode());
        PmVersionEntity maxVersionData = versionMapper.selectList(pm).stream()
                .sorted(Comparator.comparing(PmVersionEntity::getVersion).reversed()).findFirst().orElse(null);

        int version = 1;
        if (ObjectUtil.isNotEmpty(maxVersionData)) {
            version = maxVersionData.getVersion() + 1 + publishVersionRandom.nextInt(10);
        }
        //核对一下 setAttributeValue为前方法
        jsonContent.put("version", String.valueOf(version));
        JSONArray pmWorkShopArray = jsonContent.getJSONArray("workShops");
        JSONObject pmWorkShopEntity = pmWorkShopArray.getJSONObject(0);

        String code = ObjectUtil.isNotEmpty(pmWorkShopEntity) && StringUtils.isNotEmpty(pmWorkShopEntity.getString("workshopCode")) ? pmWorkShopEntity.getString("workshopCode") : "";
        String name = ObjectUtil.isNotEmpty(pmWorkShopEntity) && StringUtils.isNotEmpty(pmWorkShopEntity.getString("workshopName")) ? pmWorkShopEntity.getString("workshopName") : "";

        PmVersionEntity data = new PmVersionEntity();
        data.setWorkshopCode(code);
        data.setWorkshopName(name);
        data.setContent(JsonUtils.toJsonString(jsonContent));
        data.setVersion(version);
        data.setRemark(model.getRemark());
        data.setIsEnabled(false);
        this.insert(data);
        return new ResultVO().ok(null, "发布成功");
    }

    @Override
    public boolean insert(PmVersionEntity entity) {
        List<ConditionDto> dtos = new ArrayList<>();
        dtos.add(new ConditionDto("workshopCode", entity.getWorkshopCode(), ConditionOper.Equal));
        dtos.add(new ConditionDto("version", "0", ConditionOper.Equal));
        PmWorkShopEntity pmWorkShopEntity = this.workShopService.getData(dtos).stream().findFirst().orElse(null);
        if (pmWorkShopEntity != null) {
            pmWorkShopEntity.setIsDelete(true);
            pmWorkShopEntity.setVersion(entity.getVersion());
            this.workShopService.insert(pmWorkShopEntity, false);
        }
        this.lineService.publishByWorkShopId(pmWorkShopEntity.getId(), entity.getVersion());
        this.workStationService.publishByWorkShopId(pmWorkShopEntity.getId(), entity.getVersion());
        this.pmAviParentService.publishByWorkShopId(pmWorkShopEntity.getId(), entity.getVersion());
        this.otService.publishByWorkShopId(pmWorkShopEntity.getId(), entity.getVersion());
        this.woService.publishByWorkShopId(pmWorkShopEntity.getId(), entity.getVersion());
        this.toolService.publishByWorkShopId(pmWorkShopEntity.getId(), entity.getVersion());
        this.toolJobService.publishByWorkShopId(pmWorkShopEntity.getId(), entity.getVersion());
        this.pullCordService.publishByWorkShopId(pmWorkShopEntity.getId(), entity.getVersion());
        this.pmWorkstationMaterialService.publishByWorkShopId(pmWorkShopEntity.getId(), entity.getVersion());
        this.equipmentService.publishByWorkShopId(pmWorkShopEntity.getId(), entity.getVersion());
        return super.insert(entity);
    }

    @Override
    public ResultVO enableShopVersion(Long shopId) throws LinkException {
        PmVersionEntity data = versionMapper.selectById(shopId);
        if (ObjectUtil.isEmpty(data)) {
            throw new InkelinkException("启用的工厂建模在数据库中不存在");
        }
        enableShopVersion(data);
        //工厂建模启动以后，需要进行一些其他的处理
        afterEnableShopVersion(data);
        //重置缓存
        resetWorkshopAndVersionRedisCache(data);
        localCache.removeObject(CACHE_NAME + data.getWorkshopCode());
        localCache.removeObject(PM_ALL_CACHE_NAME);
        localCache.removeObject(PM_ALL_CACHE_NAME_VO + data.getWorkshopCode());
        clearRedisCacheByShopCodeAndVersion(data);
        //先avechange 就算通用同步失败也不影响mom系统生产
        this.saveChange();
        //同步通用工厂建模
        doSyncCommFactory(data);
        return new ResultVO().ok(null, "启用成功");

    }

    /**
     * 同步通用工厂建模
     *
     * @param data
     */
    private void doSyncCommFactory(PmVersionEntity data) {
        log.warn("开始执行cmc同步");
        if (!canSyncCmc) {
            log.warn("但'canSyncCmc'设置为'不同步',同步停止");
            return;
        }
        threadPoolTaskExecutor.submit(() -> {
            try {
                syncCommFactory(data,false);
                log.warn("执行cmc同步结束");
            } catch (Exception e) {
                log.error("执行cmc同步错误:" + e.getMessage(), e);
            }
        });

    }

    @Override
    public ResultVO<Object> syncCommFactory(Long versionId,int syncEquipment) {
        PmVersionEntity data = versionMapper.selectById(versionId);
        syncCommFactory(data,syncEquipment == 1);
        return new ResultVO().ok(null, "同步成功");
    }

    @Override
    public List<PmWorkStationEntity> getRelevanceQgWorkplaceByStation(String workstationCode) {
        PmAllDTO pmAll = getObjectedPm();
        List<PmWorkStationEntity> collect = pmAll.getStations().stream().filter(c -> c.getStations().contains(workstationCode)).collect(Collectors.toList());
        return collect;
    }

    private void syncCommFactory(PmVersionEntity data,boolean syncEquipment) {
        if (Objects.isNull(data)) {
            throw new InkelinkException("启用的工厂建模在数据库中不存在");
        }
        PmAllVo pmAllVo;
        try {
            //mom建模的数据
            pmAllVo = JSON.parseObject(data.getContent(), PmAllVo.class);
        } catch (Exception e) {
            throw new InkelinkException("启用的工厂建模json转换失败");
        }
        //工厂
        /**
         *  1、查询通用工厂
         *  2、查询的出的数据拿id去缓存表里查询是否存在，不存在新增，存在是修改，差集是删除
         *  3、新增完的后更新通用建模id到缓存表
         */
        //工厂增删改逻辑
        processPlant(pmAllVo);
        processShop(pmAllVo);
        processLine(pmAllVo);
        processStation(pmAllVo);
        if(syncEquipment){
            processEquipment(pmAllVo);
            processEquipmentPower(pmAllVo);
        }
        //processBop(pmAllVo);
    }

    private void processBop(PmAllVo pmAllVo) {
        QueryWrapper<PmSyncEntity> queryW = new QueryWrapper<>();
        queryW.lambda().eq(PmSyncEntity::getPrcId, pmAllVo.getWorkShops().get(0).getId());
        PmSyncEntity query = pmSyncService.getData(queryW, false).stream().findFirst().orElse(null);
        DataDto dataDto = new DataDto();
        List<ConditionDto> conditionDtos = new ArrayList<>(2);
        if (query != null) {
            conditionDtos.add(new ConditionDto("cmcPmWorkshopId", String.valueOf(query.getCmcId()), ConditionOper.Equal));
        } else {
            conditionDtos.add(new ConditionDto("cmcPmWorkshopId", "0", ConditionOper.Equal));
        }
        conditionDtos.add(new ConditionDto("isDelete", "0", ConditionOper.Equal));
        dataDto.setConditions(conditionDtos);
        Object resData = cmcPmFactoryService.getBopPage(dataDto);
        if (resData == null) {
            resData = new ArrayList<>();
        }
        //mom线体
        List<PmBopEntity> bops = pmAllVo.getBops();
        List<Long> prcIds = new ArrayList<>(bops.size());
        List<CmcPmBopVo> bopVos = bops.stream().map(bop -> {
            CmcPmBopVo bopVo = new CmcPmBopVo();
            BeanUtils.copyProperties(bop, bopVo);
            bopVo.setCmcPmLineCode(bop.getLineCode());
            bopVo.setCmcPmLineName(bop.getLineName());
            bopVo.setCmcPmWorkshopCode(bop.getWorkshopCode());
            bopVo.setCmcPmWorkstationCode(bop.getWorkstationCode());
            bopVo.setCmcPmWorkstationName(bop.getWorkstationName());
            bopVo.setCmcPmLineId(bop.getPrcPmLineId());
            bopVo.setCmcPmWorkshopId(bop.getPrcPmWorkshopId());
            bopVo.setCmcPmWorkstationId(bop.getPrcPmWorkstationId());
            bopVo.setSourceId(bop.getId());
            prcIds.add(bop.getId());
            return bopVo;
        }).collect(Collectors.toList());
        //查询缓存表
        List<CmcPmBopVo> cmcPmBopVos = JSON.parseArray(JSON.toJSONString(resData), CmcPmBopVo.class);
        List<String> cmcIds = cmcPmBopVos.stream().map(e -> String.valueOf(e.getCmcPmBopId())).collect(Collectors.toList());
        List<CmcPmBopVo> existData = Collections.emptyList();
        List<Long> exitsIds = new ArrayList<>(cmcIds.size());
        if (CollectionUtils.isNotEmpty(cmcIds)) {
            List<PmSyncEntity> existDataTemp = pmSyncService.getPmSyncsByCmcIds(cmcIds);
            existData = existDataTemp.stream().map(s -> {
                CmcPmBopVo cmcPmBopVo = new CmcPmBopVo();
                cmcPmBopVo.setSourceId(s.getPrcId());
                cmcPmBopVo.setCmcPmBopId(s.getCmcId());
                exitsIds.add(s.getPrcId());
                return cmcPmBopVo;
            }).collect(Collectors.toList());
        }
        //处理要操作的数据
        List<CmcPmBopVo> updateCollect = bopVos.stream().filter(c -> exitsIds.contains(c.getSourceId())).collect(Collectors.toList());
        List<CmcPmBopVo> insertCollect = bopVos.stream().filter(c -> !exitsIds.contains(c.getSourceId())).collect(Collectors.toList());
        List<CmcPmBopVo> delCollect = existData.stream().filter(c -> !prcIds.contains(c.getSourceId())).collect(Collectors.toList());
        //加载全部映射
        Map<Long, Long> prcIdAndCmcIdMapping = pmSyncService.getPrcIdAndCmcIdMapping();
        //修改数据
        if (CollectionUtils.isNotEmpty(updateCollect)) {
            updateCollect.forEach(m -> setForeignKey(m, prcIdAndCmcIdMapping));
            cmcPmFactoryService.bopBatchSave(updateCollect);
        }
        //保存数据
        if (CollectionUtils.isNotEmpty(insertCollect)) {
            insertCollect.forEach(m -> setForeignKey(m, prcIdAndCmcIdMapping));
            saveCmcBop(insertCollect);
        }
        //删除数据
        List<Long> commitDelIds = delCollect.stream().map(CmcPmBopVo::getCmcPmBopId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(commitDelIds)) {
            delCmcBop(commitDelIds);
        }

    }

    private void setForeignKey(CmcPmBopVo cmcPmBopVo, Map<Long, Long> prcIdAndCmcIdMapping) {
        Long cmcLineId = prcIdAndCmcIdMapping.get(cmcPmBopVo.getCmcPmLineId());
        cmcPmBopVo.setCmcPmLineId(cmcLineId == null ? Constant.DEFAULT_ID : cmcLineId);
        Long cmcWorkshopId = prcIdAndCmcIdMapping.get(cmcPmBopVo.getCmcPmWorkshopId());
        cmcPmBopVo.setCmcPmWorkshopId(cmcWorkshopId == null ? Constant.DEFAULT_ID : cmcWorkshopId);
        Long cmcWorkstationId = prcIdAndCmcIdMapping.get(cmcPmBopVo.getCmcPmWorkstationId());
        cmcPmBopVo.setCmcPmWorkstationId(cmcWorkstationId == null ? Constant.DEFAULT_ID : cmcWorkstationId);
        Long cmcBopId = prcIdAndCmcIdMapping.get(cmcPmBopVo.getSourceId());
        cmcPmBopVo.setCmcPmBopId(cmcBopId == null ? Constant.DEFAULT_ID : cmcBopId);
    }

    private void setForeignKey(CmcPmEquipmentVo cmcPmEquipmentVo, Map<Long, Long> prcIdAndCmcIdMapping) {
        Long cmcLineId = prcIdAndCmcIdMapping.get(cmcPmEquipmentVo.getCmcPmWorkCenterId());
        cmcPmEquipmentVo.setCmcPmWorkCenterId(cmcLineId == null ? Constant.DEFAULT_ID : cmcLineId);
        Long cmcWorkshopId = prcIdAndCmcIdMapping.get(cmcPmEquipmentVo.getCmcPmAreaId());
        cmcPmEquipmentVo.setCmcPmAreaId(cmcWorkshopId == null ? Constant.DEFAULT_ID : cmcWorkshopId);
        Long cmcWorkstationId = prcIdAndCmcIdMapping.get(cmcPmEquipmentVo.getCmcPmWorkUnitId());
        cmcPmEquipmentVo.setCmcPmWorkUnitId(cmcWorkstationId == null ? Constant.DEFAULT_ID : cmcWorkstationId);
        Long cmcEquipmentId = prcIdAndCmcIdMapping.get(cmcPmEquipmentVo.getSourceId());
        cmcPmEquipmentVo.setCmcPmEquipmentId(cmcEquipmentId == null ? Constant.DEFAULT_ID : cmcEquipmentId);
    }

    private void setForeignKey(CmcPmEquipmentPowerVo cmcPmEquipmentPowerVo, Map<Long, Long> prcIdAndCmcIdMapping) {
        Long cmcLineId = prcIdAndCmcIdMapping.get(cmcPmEquipmentPowerVo.getCmcPmWorkCenterId());
        cmcPmEquipmentPowerVo.setCmcPmWorkCenterId(cmcLineId == null ? Constant.DEFAULT_ID : cmcLineId);
        Long cmcWorkshopId = prcIdAndCmcIdMapping.get(cmcPmEquipmentPowerVo.getCmcPmAreaId());
        cmcPmEquipmentPowerVo.setCmcPmAreaId(cmcWorkshopId == null ? Constant.DEFAULT_ID : cmcWorkshopId);
        Long cmcWorkstationId = prcIdAndCmcIdMapping.get(cmcPmEquipmentPowerVo.getCmcPmWorkUnitId());
        cmcPmEquipmentPowerVo.setCmcPmWorkUnitId(cmcWorkstationId == null ? Constant.DEFAULT_ID : cmcWorkstationId);
        Long cmcEquipmentId = prcIdAndCmcIdMapping.get(cmcPmEquipmentPowerVo.getCmcPmEquipmentId());
        cmcPmEquipmentPowerVo.setCmcPmEquipmentId(cmcEquipmentId == null ? Constant.DEFAULT_ID : cmcEquipmentId);
        Long cmcEquipmentPowerId = prcIdAndCmcIdMapping.get(cmcPmEquipmentPowerVo.getSourceId());
        cmcPmEquipmentPowerVo.setCmcPmEquipmentPowerId(cmcEquipmentPowerId == null ? Constant.DEFAULT_ID : cmcEquipmentPowerId);
    }

    private void saveCmcBop(List<CmcPmBopVo> commitInsert) {
        Object data = cmcPmFactoryService.bopBatchSave(commitInsert);
        if (data == null) {
            data = new ArrayList<>();
        }
        List<CmcPmBopVo> saveVos = JsonUtils.parseArray(JsonUtils.toJsonString(data), CmcPmBopVo.class);
        List<PmSyncEntity> list = new ArrayList<>();
        saveVos.stream().forEach(saveVo -> {
            PmSyncEntity saveEntity = new PmSyncEntity();
            saveEntity.setCmcId(saveVo.getCmcPmBopId());
            saveEntity.setPrcId(saveVo.getSourceId());
            saveEntity.setDataType("bop");
            list.add(saveEntity);
        });
        pmSyncService.insertBatch(list);
        pmSyncService.saveChange();
    }

    private void delCmcBop(List<Long> commitDelIds) {
        cmcPmFactoryService.batchDelete("bop", commitDelIds);
        List<String> selfCmcIds = commitDelIds.stream().map(s -> String.valueOf(s)).collect(Collectors.toList());
        pmSyncService.delete(Arrays.asList(new ConditionDto("cmcId", String.join("|", selfCmcIds), ConditionOper.In)));
        pmSyncService.saveChange();
    }

    private void processStation(PmAllVo pmAllVo) {
        QueryWrapper<PmSyncEntity> queryW = new QueryWrapper<>();
        queryW.lambda().eq(PmSyncEntity::getPrcId, pmAllVo.getWorkShops().get(0).getId());
        PmSyncEntity query = pmSyncService.getData(queryW, false).stream().findFirst().orElse(null);
        DataDto dataDto = new DataDto();
        List<ConditionDto> conditionDtos = new ArrayList<>(2);
        if (query != null) {
            conditionDtos.add(new ConditionDto("cmcPmAreaId", String.valueOf(query.getCmcId()), ConditionOper.Equal));
        } else {
            conditionDtos.add(new ConditionDto("cmcPmAreaId", "0", ConditionOper.Equal));
        }
        conditionDtos.add(new ConditionDto("isDelete", "0", ConditionOper.Equal));
        dataDto.setConditions(conditionDtos);
        Object resData = cmcPmCompanyService.getWorkUnitPage(dataDto);
        if (resData == null) {
            resData = new ArrayList<>();
        }
        //mom工位
        List<PmWorkStationEntity> workstationsInMom = getCanSyncWorkStation(pmAllVo);
        List<PmAviEntity> avisInMom = getCanSyncAvis(pmAllVo);
        //avi
        List<Long> prcIdsInMom = new ArrayList<>(workstationsInMom.size() + avisInMom.size());
        List<CmcPmWorkUnitVo> finalAvis = avisInMom.stream().filter(PmAviEntity :: getIsSync).map(a -> {
            CmcPmWorkUnitVo workUnitVo = new CmcPmWorkUnitVo();
            workUnitVo.setDisplayNo(a.getAviDisplayNo());
            workUnitVo.setIsEnable(a.getIsEnable());
            workUnitVo.setWorkUnitCode(a.getAviCode());
            workUnitVo.setWorkUnitName(a.getAviName());
            workUnitVo.setWorkUnitType(2);
            workUnitVo.setAviType(a.getAviType());
            workUnitVo.setRemark(a.getRemark());
            workUnitVo.setSourceId(a.getId());
            workUnitVo.setCmcPmAreaId(a.getPrcPmWorkshopId());
            workUnitVo.setCmcPmWorkCenterId(a.getPrcPmLineId());
            prcIdsInMom.add(a.getId());
            return workUnitVo;
        }).collect(Collectors.toList());

        //工位
        List<CmcPmWorkUnitVo> finalWorkstations = workstationsInMom.stream().filter(PmWorkStationEntity :: getIsSync).map(workstation -> {
            CmcPmWorkUnitVo workUnitVo = new CmcPmWorkUnitVo();
            workUnitVo.setAlarmDistance(workstation.getAlarmDistance());
            workUnitVo.setBeginDistance(workstation.getBeginDistance());
            workUnitVo.setDirection(workstation.getDirection());
            workUnitVo.setDisplayNo(workstation.getWorkstationDisplayNo());
            workUnitVo.setEndDistance(workstation.getEndDistance());
            workUnitVo.setIsEnable(workstation.getIsEnable());
            workUnitVo.setProductTime(workstation.getProductTime());
            workUnitVo.setTeamNo(workstation.getTeamNo());
            workUnitVo.setWorkStationNo(workstation.getWorkstationNo());
            workUnitVo.setWorkUnitCode(workstation.getWorkstationCode());
            workUnitVo.setWorkUnitName(workstation.getWorkstationName());
            workUnitVo.setWorkUnitType(1);
            workUnitVo.setRemark(workstation.getRemark());
            workUnitVo.setWorkStationType(workstation.getWorkstationType());
            workUnitVo.setSourceId(workstation.getId());
            workUnitVo.setCmcPmAreaId(workstation.getPrcPmWorkshopId());
            workUnitVo.setCmcPmWorkCenterId(workstation.getPrcPmLineId());
            prcIdsInMom.add(workstation.getId());
            return workUnitVo;
        }).collect(Collectors.toList());
        finalWorkstations.addAll(finalAvis);

        List<CmcPmWorkUnitVo> existDataInMom = new ArrayList<>();
        //查询缓存表
        List<CmcPmWorkUnitVo> workUnitsInCmc = JSON.parseArray(JSON.toJSONString(resData), CmcPmWorkUnitVo.class);
        List<String> workUnitIdsInCmc = workUnitsInCmc.stream().map(e -> String.valueOf(e.getCmcPmWorkUnitId())).collect(Collectors.toList());
        Set<Long> exitsIdsInMom = new HashSet<>(workUnitIdsInCmc.size());
        if (CollectionUtils.isNotEmpty(workUnitIdsInCmc)) {
            List<PmSyncEntity> existDataTemp = pmSyncService.getPmSyncsByCmcIds(workUnitIdsInCmc);
            existDataInMom = existDataTemp.stream().map(s -> {
                CmcPmWorkUnitVo vo = new CmcPmWorkUnitVo();
                vo.setSourceId(s.getPrcId());
                vo.setCmcPmWorkUnitId(s.getCmcId());
                exitsIdsInMom.add(s.getPrcId());
                return vo;
            }).collect(Collectors.toList());
        }
        //处理要操作的数据
        List<CmcPmWorkUnitVo> updateCollect = finalWorkstations.stream().filter(c -> exitsIdsInMom.contains(c.getSourceId())).collect(Collectors.toList());
        List<CmcPmWorkUnitVo> insertCollect = finalWorkstations.stream().filter(c -> !exitsIdsInMom.contains(c.getSourceId())).collect(Collectors.toList());
        List<CmcPmWorkUnitVo> delCollect = existDataInMom.stream().filter(c -> !prcIdsInMom.contains(c.getSourceId())).collect(Collectors.toList());
        //修改数据
        Map<Long, Long> prcIdAndCmcIdMapping = pmSyncService.getPrcIdAndCmcIdMapping();
        if (CollectionUtils.isNotEmpty(updateCollect)) {
            updateCollect.forEach(e -> setForeignKey(e, prcIdAndCmcIdMapping));
            cmcPmFactoryService.workUnitBatchSave(updateCollect);
        }
        //保存数据
        if (CollectionUtils.isNotEmpty(insertCollect)) {
            insertCollect.forEach(e -> setForeignKey(e, prcIdAndCmcIdMapping));
            saveCmcStation(insertCollect);
        }
        //删除数据
        List<Long> commitDelIds = delCollect.stream().map(CmcPmWorkUnitVo::getCmcPmWorkUnitId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(commitDelIds)) {
            delCmcStation(commitDelIds);
        }
    }

    private void processEquipment(PmAllVo pmAllVo) {
        QueryWrapper<PmSyncEntity> queryW = new QueryWrapper<>();
        queryW.lambda().eq(PmSyncEntity::getPrcId, pmAllVo.getWorkShops().get(0).getId());
        PmSyncEntity query = pmSyncService.getData(queryW, false).stream().findFirst().orElse(null);
        DataDto dataDto = new DataDto();
        List<ConditionDto> conditionDtos = new ArrayList<>(2);
        if (query != null) {
            conditionDtos.add(new ConditionDto("cmcPmAreaId", String.valueOf(query.getCmcId()), ConditionOper.Equal));
        } else {
            conditionDtos.add(new ConditionDto("cmcPmAreaId", "0", ConditionOper.Equal));
        }
        conditionDtos.add(new ConditionDto("isDelete", "0", ConditionOper.Equal));
        dataDto.setConditions(conditionDtos);
        Object resData = cmcPmFactoryService.getEquipmentPage(dataDto);
        if (resData == null) {
            resData = new ArrayList<>();
        }
        List<PmEquipmentEntity> equipmenies = pmAllVo.getEquipments();
        List<CmcPmEquipmentVo> finalEquipments = equipmenies.stream().map(a -> {
            CmcPmEquipmentVo cmcPmEquipmentVo = new CmcPmEquipmentVo();
            cmcPmEquipmentVo.setEquipmentCode(a.getEquipmentCode());
            cmcPmEquipmentVo.setEquipmentName(a.getEquipmentName());
            cmcPmEquipmentVo.setAddress(a.getAddress());
            cmcPmEquipmentVo.setSupplierCode(a.getSupplierCode());
            cmcPmEquipmentVo.setSupplierName(a.getSupplierName());
            cmcPmEquipmentVo.setRemark(a.getRemark());
            cmcPmEquipmentVo.setCmcPmAreaId(a.getPrcPmWorkshopId());
            cmcPmEquipmentVo.setCmcPmWorkCenterId(a.getPrcPmLineId());
            cmcPmEquipmentVo.setCmcPmWorkUnitId(a.getPrcPmWorkstationId());
            cmcPmEquipmentVo.setSourceId(a.getId());
            return cmcPmEquipmentVo;
        }).collect(Collectors.toList());

        List<CmcPmEquipmentVo> existEquipmentInMom = new ArrayList<>();
        //查询缓存表
        List<CmcPmEquipmentVo> equipmentsInCmc = JSON.parseArray(JSON.toJSONString(resData), CmcPmEquipmentVo.class);
        List<String> equipmentIdsInCmc = equipmentsInCmc.stream().map(e -> String.valueOf(e.getCmcPmEquipmentId())).collect(Collectors.toList());
        Set<Long> exitsIdsInMom = new HashSet<>(equipmentIdsInCmc.size());
        if (CollectionUtils.isNotEmpty(equipmentIdsInCmc)) {
            List<PmSyncEntity> existDataTemp = pmSyncService.getPmSyncsByCmcIds(equipmentIdsInCmc);
            existEquipmentInMom = existDataTemp.stream().map(s -> {
                CmcPmEquipmentVo vo = new CmcPmEquipmentVo();
                vo.setSourceId(s.getPrcId());
                vo.setCmcPmWorkUnitId(s.getCmcId());
                exitsIdsInMom.add(s.getPrcId());
                return vo;
            }).collect(Collectors.toList());
        }
        //处理要操作的数据
        List<CmcPmEquipmentVo> updateCollect = finalEquipments.stream().filter(c -> exitsIdsInMom.contains(c.getSourceId())).collect(Collectors.toList());
        List<CmcPmEquipmentVo> insertCollect = finalEquipments.stream().filter(c -> !exitsIdsInMom.contains(c.getSourceId())).collect(Collectors.toList());
        //设备不支持通过mom删除
        //List<CmcPmWorkUnitVo> delCollect = existDataInMom.stream().filter(c -> !prcIdsInMom.contains(c.getSourceId())).collect(Collectors.toList());
        //修改数据
        Map<Long, Long> prcIdAndCmcIdMapping = pmSyncService.getPrcIdAndCmcIdMapping();
        if (CollectionUtils.isNotEmpty(updateCollect)) {
            updateCollect.forEach(e -> setForeignKey(e, prcIdAndCmcIdMapping));
            cmcPmFactoryService.equipmentBatchSave(updateCollect);
        }
        //保存数据
        if (CollectionUtils.isNotEmpty(insertCollect)) {
            insertCollect.forEach(e -> setForeignKey(e, prcIdAndCmcIdMapping));
            saveCmcEquipment(insertCollect);
        }
        //删除数据
//        List<Long> commitDelIds = delCollect.stream().map(CmcPmWorkUnitVo::getCmcPmWorkUnitId).collect(Collectors.toList());
//        if (CollectionUtils.isNotEmpty(commitDelIds)) {
//            delCmcStation(commitDelIds);
//        }
    }

    private void processEquipmentPower(PmAllVo pmAllVo) {
        QueryWrapper<PmSyncEntity> queryW = new QueryWrapper<>();
        Long shopId = pmAllVo.getWorkShops().get(0).getId();
        queryW.lambda().eq(PmSyncEntity::getPrcId, shopId);
        PmSyncEntity query = pmSyncService.getData(queryW, false).stream().findFirst().orElse(null);
        DataDto dataDto = new DataDto();
        List<ConditionDto> conditionDtos = new ArrayList<>(2);
        if (query != null) {
            conditionDtos.add(new ConditionDto("cmcPmAreaId", String.valueOf(query.getCmcId()), ConditionOper.Equal));
        } else {
            conditionDtos.add(new ConditionDto("cmcPmAreaId", "0", ConditionOper.Equal));
        }
        conditionDtos.add(new ConditionDto("isDelete", "0", ConditionOper.Equal));
        dataDto.setConditions(conditionDtos);
        Object resData = cmcPmFactoryService.getEquipmentPowerPage(dataDto);
        if (resData == null) {
            resData = new ArrayList<>();
        }
        //mom工位
        List<PmEquipmentPowerEntity> equipmeniePowers = getEquipmentPowersByShopId(shopId);
        //avi
        List<Long> prcIdsInMom = new ArrayList<>(equipmeniePowers.size());
        List<CmcPmEquipmentPowerVo> finalEquipmentPowers = equipmeniePowers.stream().map(a -> {
            CmcPmEquipmentPowerVo cmcPmEquipmentPowerVo = new CmcPmEquipmentPowerVo();
            cmcPmEquipmentPowerVo.setCmcPmAreaId(a.getPrcPmWorkshopId());
            cmcPmEquipmentPowerVo.setCmcPmWorkCenterId(a.getPrcPmLineId());
            cmcPmEquipmentPowerVo.setCmcPmWorkUnitId(a.getPrcPmWorkstationId());
            cmcPmEquipmentPowerVo.setCmcPmEquipmentId(a.getPrcPmEquipmentId());
            cmcPmEquipmentPowerVo.setPowerType(a.getPowerType());
            cmcPmEquipmentPowerVo.setAttribute1(a.getAttribute1());
            cmcPmEquipmentPowerVo.setMaxValue(a.getMaxValue());
            cmcPmEquipmentPowerVo.setMinValue(a.getMinValue());
            cmcPmEquipmentPowerVo.setStandardValue(a.getStandardValue());
            cmcPmEquipmentPowerVo.setUnit(a.getUnit());
            cmcPmEquipmentPowerVo.setRemark(a.getRemark());
            cmcPmEquipmentPowerVo.setSourceId(a.getId());
            prcIdsInMom.add(a.getId());
            return cmcPmEquipmentPowerVo;
        }).collect(Collectors.toList());

        List<CmcPmEquipmentVo> existEquipmentInMom = new ArrayList<>();
        //查询缓存表
        List<CmcPmEquipmentPowerVo> equipmentsInCmc = JSON.parseArray(JSON.toJSONString(resData), CmcPmEquipmentPowerVo.class);
        List<String> equipmentPowerIdsInCmc = equipmentsInCmc.stream().map(e -> String.valueOf(e.getCmcPmEquipmentPowerId())).collect(Collectors.toList());
        Set<Long> exitsIdsInMom = new HashSet<>(equipmentPowerIdsInCmc.size());
        if (CollectionUtils.isNotEmpty(equipmentPowerIdsInCmc)) {
            List<PmSyncEntity> existDataTemp = pmSyncService.getPmSyncsByCmcIds(equipmentPowerIdsInCmc);
            existEquipmentInMom = existDataTemp.stream().map(s -> {
                CmcPmEquipmentVo vo = new CmcPmEquipmentVo();
                vo.setSourceId(s.getPrcId());
                vo.setCmcPmWorkUnitId(s.getCmcId());
                exitsIdsInMom.add(s.getPrcId());
                return vo;
            }).collect(Collectors.toList());
        }
        //处理要操作的数据
        List<CmcPmEquipmentPowerVo> updateCollect = finalEquipmentPowers.stream().filter(c -> exitsIdsInMom.contains(c.getSourceId())).collect(Collectors.toList());
        List<CmcPmEquipmentPowerVo> insertCollect = finalEquipmentPowers.stream().filter(c -> !exitsIdsInMom.contains(c.getSourceId())).collect(Collectors.toList());
        //设备不支持通过mom删除
        //List<CmcPmWorkUnitVo> delCollect = existDataInMom.stream().filter(c -> !prcIdsInMom.contains(c.getSourceId())).collect(Collectors.toList());
        //修改数据
        Map<Long, Long> prcIdAndCmcIdMapping = pmSyncService.getPrcIdAndCmcIdMapping();
        if (CollectionUtils.isNotEmpty(updateCollect)) {
            updateCollect.forEach(e -> setForeignKey(e, prcIdAndCmcIdMapping));
            cmcPmFactoryService.equipmentPowerBatchSave(updateCollect);
        }
        //保存数据
        if (CollectionUtils.isNotEmpty(insertCollect)) {
            insertCollect.forEach(e -> setForeignKey(e, prcIdAndCmcIdMapping));
            saveCmcEquipmentPower(insertCollect);
        }
        //删除数据
//        List<Long> commitDelIds = delCollect.stream().map(CmcPmWorkUnitVo::getCmcPmWorkUnitId).collect(Collectors.toList());
//        if (CollectionUtils.isNotEmpty(commitDelIds)) {
//            delCmcStation(commitDelIds);
//        }
    }

    private List<PmEquipmentPowerEntity> getEquipmentPowersByShopId(Long shopId){
        QueryWrapper<PmEquipmentPowerEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<PmEquipmentPowerEntity> lqw = qw.lambda();
        lqw.eq(PmEquipmentPowerEntity :: getPrcPmWorkshopId,shopId);
        return pmEquipmentPowerService.getData(qw,false);
    }

    public List<PmAviEntity> getCanSyncAvis(PmAllVo pmAllVo){
        List<PmLineEntity> lines = pmAllVo.getLines();
        List<Long> lineIds = lines.stream().filter(PmLineEntity :: getIsSync).map(PmLineEntity :: getId)
                .collect(Collectors.toList());
        List<PmAviEntity> avis = pmAllVo.getAvis();
        return avis.stream().filter(item -> lineIds.contains(item.getPrcPmLineId())
                && item.getIsSync()).collect(Collectors.toList());
    }

    public List<PmWorkStationEntity> getCanSyncWorkStation(PmAllVo pmAllVo){
        List<PmLineEntity> lines = pmAllVo.getLines();
        List<Long> lineIds = lines.stream().filter(PmLineEntity :: getIsSync).map(PmLineEntity :: getId)
                .collect(Collectors.toList());
        List<PmWorkStationEntity> stations = pmAllVo.getWorkStations();
        return stations.stream().filter(item -> lineIds.contains(item.getPrcPmLineId())
                && item.getIsSync()).collect(Collectors.toList());
    }


    @Override
    public void syncMonToCmc(int type) {
        if (type < 1 || type > 3) {
            throw new InkelinkException("type参数只能传递1到3的数字");
        }
        //删除通用工厂重复数据
        if (type == 1 || type == 3) {
            delRepeatStation();
        }
        if (type == 2 || type == 3) {
            //同步sync表
            addLackRelaForStation();
        }
    }

    /**
     * 补充关联表：补充相同工位编码在mom里有，通用工厂里也有，但是关联表里没有的关联数据
     */
    public void addLackRelaForStation() {
        List<PmAviEntity> prcAvis = pmAviParentService.getData(null);
        Map<String, Long> prcAviIdMapping = new HashMap<>(prcAvis.size());
        if(!prcAvis.isEmpty()){
            prcAvis.stream().forEach(item ->
                    prcAviIdMapping.put(item.getAviCode(), item.getId())
            );
        }
        List<PmWorkStationEntity> prcStations = workStationService.getData(null);
        Map<String, Long> prcStationNoAndStationIdMapping = new HashMap<>(prcStations.size());
        prcStations.stream().forEach(item ->
                prcStationNoAndStationIdMapping.put(item.getWorkstationCode(), item.getId())
        );
        List<CmcPmWorkUnitVo> workUnitsInCmc = getCmcPmWorkUnit();
        if (workUnitsInCmc.isEmpty()) {
            throw new InkelinkException("未查询到通用工厂工位信息");
        }
        Map<String, Long> cmcStationNoAndStationIdMapping = new HashMap<>(workUnitsInCmc.size());
        workUnitsInCmc.stream().forEach(item ->
                cmcStationNoAndStationIdMapping.put(item.getWorkUnitCode(), item.getCmcPmWorkUnitId())
        );
        List<PmSyncEntity> syncs = getStationsInSync();
        Map<Long, Long> sycCmcIdMapping = new HashMap<>(syncs.size());
        syncs.stream().forEach(item ->
                sycCmcIdMapping.put(item.getCmcId(), item.getPrcId())
        );
        Map<String, Long> notExistsStationDataInSync = getNotExistsDataInSync(cmcStationNoAndStationIdMapping, prcStationNoAndStationIdMapping, sycCmcIdMapping);
        List<PmSyncEntity> list = new ArrayList<>(prcStationNoAndStationIdMapping.size() + prcAviIdMapping.size());
        if(!notExistsStationDataInSync.isEmpty()){
            batchSaveSync(notExistsStationDataInSync, prcStationNoAndStationIdMapping,list);
        }
        Map<String, Long> notExistsAviDataInSync = getNotExistsDataInSync(cmcStationNoAndStationIdMapping, prcAviIdMapping, sycCmcIdMapping);
        if(!notExistsAviDataInSync.isEmpty()){
            batchSaveSync(notExistsAviDataInSync, prcAviIdMapping,list);
        }
        if(!list.isEmpty()){
            pmSyncService.insertBatch(list);
        }
        Map<Long, Long> updateStationData = getUpdateDataInSync(cmcStationNoAndStationIdMapping, prcStationNoAndStationIdMapping, sycCmcIdMapping);
        batchUpdate(updateStationData);
        Map<Long, Long> updateAviData = getUpdateDataInSync(cmcStationNoAndStationIdMapping, prcAviIdMapping, sycCmcIdMapping);
        batchUpdate(updateAviData);
        pmSyncService.saveChange();
    }

    private Map<String, Long> getNotExistsDataInSync(Map<String, Long> cmcStationNoAndStationIdMapping,
                                                     Map<String, Long> prcStationNoAndStationIdMapping,
                                                     Map<Long, Long> sycCmcIdMapping) {
        Map<String, Long> notExistsData = new HashMap<>();
        for (Map.Entry<String, Long> entry : cmcStationNoAndStationIdMapping.entrySet()) {
            if (!sycCmcIdMapping.containsKey(entry.getValue()) &&
                    prcStationNoAndStationIdMapping.containsKey(entry.getKey())) {
                notExistsData.put(entry.getKey(), entry.getValue());
            }
        }
        return notExistsData;
    }

    /**
     * cmc和prc都存在 但是prcid已经变化
     * @param cmcStationNoAndStationIdMapping
     * @param prcStationNoAndStationIdMapping
     * @param sycCmcIdMapping
     * @return
     */
    private Map<Long, Long> getUpdateDataInSync(Map<String, Long> cmcStationNoAndStationIdMapping,
                                                     Map<String, Long> prcStationNoAndStationIdMapping,
                                                     Map<Long, Long> sycCmcIdMapping) {
        Map<Long, Long> updateData = new HashMap<>();
        for (Map.Entry<String, Long> entry : cmcStationNoAndStationIdMapping.entrySet()) {
            Long prcId = sycCmcIdMapping.get(entry.getValue());
            Long actualPrcId = prcStationNoAndStationIdMapping.get(entry.getKey());
            if (prcId != null
                    && actualPrcId != null
                    && prcId.compareTo(actualPrcId) != 0) {
                updateData.put(entry.getValue(), actualPrcId);
            }
        }
        return updateData;
    }


    private void batchSaveSync(Map<String, Long> notExistsCmcDataInSync,
                               Map<String, Long> prcStationNoAndStationIdMapping,
                               List<PmSyncEntity> list) {
        for (Map.Entry<String, Long> item : notExistsCmcDataInSync.entrySet()) {
            PmSyncEntity saveEntity = new PmSyncEntity();
            saveEntity.setCmcId(item.getValue());
            saveEntity.setPrcId(prcStationNoAndStationIdMapping.get(item.getKey()));
            saveEntity.setDataType("workunit");
            list.add(saveEntity);
        }
    }

    private void batchUpdate(Map<Long, Long> datas) {
        if(!datas.isEmpty()){
            for (Map.Entry<Long, Long> item : datas.entrySet()) {
                UpdateWrapper<PmSyncEntity> qw = new UpdateWrapper<>();
                LambdaUpdateWrapper<PmSyncEntity> lqw = qw.lambda();
                lqw.set(PmSyncEntity :: getPrcId,item.getValue());
                lqw.eq(PmSyncEntity :: getCmcId,item.getKey());
                lqw.eq(PmSyncEntity :: getIsDelete,false);
                this.pmSyncService.update(qw);
            }
        }
    }


    private List<PmSyncEntity> getStationsInSync() {
        QueryWrapper<PmSyncEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<PmSyncEntity> lqw = qw.lambda();
        lqw.eq(PmSyncEntity::getDataType, "workunit");
        return pmSyncService.getData(qw, false);
    }

    /**
     * 删除通用工厂重复工位
     */
    public void delRepeatStation() {
        List<CmcPmWorkUnitVo> workUnitsInCmc = getCmcPmWorkUnit();
        List<Long> prepareDeleteIds = getRepeatWorkUnitIds(workUnitsInCmc);
        if (CollectionUtils.isNotEmpty(prepareDeleteIds)) {
            delCmcStation(prepareDeleteIds);
        }
    }

    private List<CmcPmWorkUnitVo> getCmcPmWorkUnit() {
        List<ConditionDto> conditionDtos = new ArrayList<>(2);
        conditionDtos.add(new ConditionDto("isDelete", "0", ConditionOper.Equal));
        DataDto dataDto = new DataDto();
        dataDto.setConditions(conditionDtos);
        Object resData = cmcPmCompanyService.getWorkUnitPage(dataDto);
        if (resData == null) {
            resData = new ArrayList<>();
        }
        return JSON.parseArray(JSON.toJSONString(resData), CmcPmWorkUnitVo.class);
    }

    private List<Long> getRepeatWorkUnitIds(List<CmcPmWorkUnitVo> units) {
        List<Long> prepareDeleteIds = new ArrayList<>();
        if (units.isEmpty()) {
            return prepareDeleteIds;
        }
        Map<String, List<Long>> unitIdsByUnitCode = new HashMap<>();
        for (CmcPmWorkUnitVo cmcPmWorkUnitVo : units) {
            unitIdsByUnitCode.computeIfAbsent(cmcPmWorkUnitVo.getWorkUnitCode(),
                    v -> new ArrayList<>()).add(cmcPmWorkUnitVo.getCmcPmWorkUnitId());
        }
        for (Map.Entry<String, List<Long>> item : unitIdsByUnitCode.entrySet()) {
            if (item.getValue().size() > 1) {
                Long minId = getMinId(item.getValue());
                getPrepareDeleteIds(item.getValue(), minId, prepareDeleteIds);
            }
        }
        return prepareDeleteIds;
    }

    /**
     * 获取最小的id
     *
     * @param ids
     * @return
     */
    private Long getMinId(List<Long> ids) {
        Long minId = ids.get(0);
        for (int i = 1; i < ids.size(); i++) {
            if (minId > ids.get(i)) {
                minId = ids.get(i);
            }
        }
        return minId;
    }

    private void getPrepareDeleteIds(List<Long> ids, Long minId, List<Long> prepareDeleteIds) {
        prepareDeleteIds.addAll(ids.stream().filter(v -> v > minId).collect(Collectors.toList()));
    }

    private void setForeignKey(CmcPmWorkUnitVo cmcPmWorkUnitVo, Map<Long, Long> prcIdAndCmcIdMapping) {
        Long cmcLineId = prcIdAndCmcIdMapping.get(cmcPmWorkUnitVo.getCmcPmWorkCenterId());
        cmcPmWorkUnitVo.setCmcPmWorkCenterId(cmcLineId == null ? Constant.DEFAULT_ID : cmcLineId);
        Long cmcWorkshopId = prcIdAndCmcIdMapping.get(cmcPmWorkUnitVo.getCmcPmAreaId());
        cmcPmWorkUnitVo.setCmcPmAreaId(cmcWorkshopId == null ? Constant.DEFAULT_ID : cmcWorkshopId);
        Long cmcWorkstationId = prcIdAndCmcIdMapping.get(cmcPmWorkUnitVo.getSourceId());
        cmcPmWorkUnitVo.setCmcPmWorkUnitId(cmcWorkstationId == null ? Constant.DEFAULT_ID : cmcWorkstationId);
    }

    private void saveCmcStation(List<CmcPmWorkUnitVo> commitInsert) {
        Object resData = cmcPmFactoryService.workUnitBatchSave(commitInsert);
        if (resData == null) {
            resData = new ArrayList<>();
        }
        List<CmcPmWorkUnitVo> saveVos = JsonUtils.parseArray(JsonUtils.toJsonString(resData), CmcPmWorkUnitVo.class);
        List<PmSyncEntity> list = new ArrayList<>(saveVos.size());
        saveVos.stream().forEach(saveVo -> {
            PmSyncEntity saveEntity = new PmSyncEntity();
            saveEntity.setCmcId(saveVo.getCmcPmWorkUnitId());
            saveEntity.setPrcId(saveVo.getSourceId());
            saveEntity.setDataType("workunit");
            list.add(saveEntity);
        });
        pmSyncService.insertBatch(list);
        pmSyncService.saveChange();
    }

    private void saveCmcEquipment(List<CmcPmEquipmentVo> commitInsert) {
        Object resData = cmcPmFactoryService.equipmentBatchSave(commitInsert);
        if (resData == null) {
            resData = new ArrayList<>();
        }
        List<CmcPmEquipmentVo> saveVos = JsonUtils.parseArray(JsonUtils.toJsonString(resData), CmcPmEquipmentVo.class);
        List<PmSyncEntity> list = new ArrayList<>(saveVos.size());
        saveVos.stream().forEach(saveVo -> {
            PmSyncEntity saveEntity = new PmSyncEntity();
            saveEntity.setCmcId(saveVo.getCmcPmEquipmentId());
            saveEntity.setPrcId(saveVo.getSourceId());
            saveEntity.setDataType("equipment");
            list.add(saveEntity);
        });
        pmSyncService.insertBatch(list);
        pmSyncService.saveChange();
    }

    private void saveCmcEquipmentPower(List<CmcPmEquipmentPowerVo> commitInsert) {
        Object resData = cmcPmFactoryService.equipmentPowerBatchSave(commitInsert);
        if (resData == null) {
            resData = new ArrayList<>();
        }
        List<CmcPmEquipmentPowerVo> saveVos = JsonUtils.parseArray(JsonUtils.toJsonString(resData), CmcPmEquipmentPowerVo.class);
        List<PmSyncEntity> list = new ArrayList<>(saveVos.size());
        saveVos.stream().forEach(saveVo -> {
            PmSyncEntity saveEntity = new PmSyncEntity();
            saveEntity.setCmcId(saveVo.getCmcPmEquipmentPowerId());
            saveEntity.setPrcId(saveVo.getSourceId());
            saveEntity.setDataType("equipmentPower");
            list.add(saveEntity);
        });
        pmSyncService.insertBatch(list);
        pmSyncService.saveChange();
    }

    private void delCmcStation(List<Long> commitDelIds) {
        cmcPmFactoryService.batchDelete("workunit", commitDelIds);
        List<String> selfCmcIds = commitDelIds.stream().map(s -> String.valueOf(s)).collect(Collectors.toList());
        pmSyncService.delete(Arrays.asList(new ConditionDto("cmcId", String.join("|", selfCmcIds), ConditionOper.In)));
        pmSyncService.saveChange();
    }

    private void processLine(PmAllVo pmAllVo) {
        QueryWrapper<PmSyncEntity> queryW = new QueryWrapper<>();
        queryW.lambda().eq(PmSyncEntity::getPrcId, pmAllVo.getWorkShops().get(0).getId());
        PmSyncEntity query = pmSyncService.getData(queryW, false).stream().findFirst().orElse(null);
        DataDto dataDto = new DataDto();
        List<ConditionDto> conditionDtos = new ArrayList<>(2);
        if (query != null) {
            conditionDtos.add(new ConditionDto("cmcPmAreaId", String.valueOf(query.getCmcId()), ConditionOper.Equal));
        } else {
            conditionDtos.add(new ConditionDto("cmcPmAreaId", "0", ConditionOper.Equal));
        }
        conditionDtos.add(new ConditionDto("isDelete", "0", ConditionOper.Equal));
        dataDto.setConditions(conditionDtos);
        Object resData = cmcPmCompanyService.getWorkCenterPage(dataDto);
        if (resData == null) {
            resData = new ArrayList<>();
        }
        List<PmLineEntity> lines = pmAllVo.getLines();
        List<Long> prcIds = new ArrayList<>(lines.size());
        List<CmcPmWorkCenterVo> workCenterList = lines.stream().filter(PmLineEntity::getIsSync).map(line -> {
            CmcPmWorkCenterVo workCenterVo = new CmcPmWorkCenterVo();
            workCenterVo.setBeginDistance(line.getBeginDistance());
            workCenterVo.setDisplayNo(line.getLineDisplayNo());
            workCenterVo.setEndDistance(line.getEndDistance());
            workCenterVo.setIsEnable(line.getIsEnable());
            workCenterVo.setLineType(line.getLineType());
            workCenterVo.setRunType(line.getRunType());
            workCenterVo.setStationCount(line.getStationCount());
            workCenterVo.setStationLength(line.getStationLength());
            workCenterVo.setWorkCenterCode(line.getLineCode());
            workCenterVo.setWorkCenterName(line.getLineName());
            workCenterVo.setWorkCenterDesignJph(line.getLineDesignJph());
            workCenterVo.setRemark(line.getLineRemark());
            workCenterVo.setCmcPmAreaId(line.getPrcPmWorkshopId());
            workCenterVo.setSourceId(line.getId());
            workCenterVo.setMergeLine(line.getMergeLine());
            workCenterVo.setTpOnlinePoint(line.getTpOnlinePoint());
            workCenterVo.setTpOfflinePoint(line.getTpOfflinePoint());
            prcIds.add(line.getId());
            return workCenterVo;
        }).collect(Collectors.toList());
        //查询缓存表
        List<CmcPmWorkCenterVo> cmcPmWorkCenterVos = JSON.parseArray(JSON.toJSONString(resData), CmcPmWorkCenterVo.class);
        List<String> ids = cmcPmWorkCenterVos.stream().map(e -> String.valueOf(e.getCmcPmWorkCenterId())).collect(Collectors.toList());
        List<CmcPmWorkCenterVo> existData = new ArrayList<>(ids.size());
        Set<Long> exitsIds = new HashSet<>(ids.size());
        if (CollectionUtils.isNotEmpty(ids)) {
            List<PmSyncEntity> existDataTemp = pmSyncService.getPmSyncsByCmcIds(ids);
            existData = existDataTemp.stream().map(s -> {
                CmcPmWorkCenterVo cmcPmWorkCenterVo = new CmcPmWorkCenterVo();
                cmcPmWorkCenterVo.setSourceId(s.getPrcId());
                cmcPmWorkCenterVo.setCmcPmWorkCenterId(s.getCmcId());
                exitsIds.add(s.getPrcId());
                return cmcPmWorkCenterVo;
            }).collect(Collectors.toList());
        }
        //处理要操作的数据
        List<CmcPmWorkCenterVo> updateCollect = workCenterList.stream().filter(c -> exitsIds.contains(c.getSourceId())).collect(Collectors.toList());
        List<CmcPmWorkCenterVo> insertCollect = workCenterList.stream().filter(c -> !exitsIds.contains(c.getSourceId())).collect(Collectors.toList());
        List<CmcPmWorkCenterVo> delCollect = existData.stream().filter(c -> !prcIds.contains(c.getSourceId())).collect(Collectors.toList());
        //修改数据
        Map<Long, Long> prcIdAndCmcIdMapping = pmSyncService.getPrcIdAndCmcIdMapping();
        if (CollectionUtils.isNotEmpty(updateCollect)) {
            updateCollect.forEach(e -> setForeignKey(e, prcIdAndCmcIdMapping));
            cmcPmFactoryService.workCenterBatchSave(updateCollect);
        }
        //保存数据
        if (CollectionUtils.isNotEmpty(insertCollect)) {
            insertCollect.forEach(e -> setForeignKey(e, prcIdAndCmcIdMapping));
            saveCmcLine(insertCollect);
        }
        //删除数据
        List<Long> commitDelIds = delCollect.stream().map(CmcPmWorkCenterVo::getCmcPmWorkCenterId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(commitDelIds)) {
            delCmcLine(commitDelIds);
        }

    }

    private void setForeignKey(CmcPmWorkCenterVo cmcPmWorkCenterVo, Map<Long, Long> prcIdAndCmcIdMapping) {
        Long cmcWorkshopId = prcIdAndCmcIdMapping.get(cmcPmWorkCenterVo.getCmcPmAreaId());
        cmcPmWorkCenterVo.setCmcPmAreaId(cmcWorkshopId == null ? Constant.DEFAULT_ID : cmcWorkshopId);
        Long cmcLineId = prcIdAndCmcIdMapping.get(cmcPmWorkCenterVo.getSourceId());
        cmcPmWorkCenterVo.setCmcPmWorkCenterId(cmcLineId == null ? Constant.DEFAULT_ID : cmcLineId);
    }

    private void saveCmcLine(List<CmcPmWorkCenterVo> commitInsert) {
        Object resData = cmcPmFactoryService.workCenterBatchSave(commitInsert);
        if (resData == null) {
            resData = new ArrayList<>();
        }
        List<CmcPmWorkCenterVo> saveVos = JsonUtils.parseArray(JsonUtils.toJsonString(resData), CmcPmWorkCenterVo.class);
        List<PmSyncEntity> list = new ArrayList<>();
        saveVos.stream().forEach(saveVo -> {
            PmSyncEntity saveEntity = new PmSyncEntity();
            saveEntity.setCmcId(saveVo.getCmcPmWorkCenterId());
            saveEntity.setPrcId(saveVo.getSourceId());
            saveEntity.setDataType("workcenter");
            list.add(saveEntity);
        });
        pmSyncService.insertBatch(list);
        pmSyncService.saveChange();

    }

    private void delCmcLine(List<Long> commitDelIds) {
        cmcPmFactoryService.batchDelete("workcenter", commitDelIds);
        List<String> selfCmcIds = commitDelIds.stream().map(s -> String.valueOf(s)).collect(Collectors.toList());
        pmSyncService.delete(Arrays.asList(new ConditionDto("cmcId", String.join("|", selfCmcIds), ConditionOper.In)));
        pmSyncService.saveChange();
    }

    private void processShop(PmAllVo pmAllVo) {
        QueryWrapper<PmSyncEntity> queryW = new QueryWrapper<>();
        queryW.lambda().eq(PmSyncEntity::getPrcId, pmAllVo.getWorkShops().get(0).getId());
        PmSyncEntity query = pmSyncService.getData(queryW, false).stream().findFirst().orElse(null);
        DataDto dataDto = new DataDto();
        List<ConditionDto> conditionDtos = new ArrayList<>(2);
        if (query != null) {
            conditionDtos.add(new ConditionDto("cmcPmAreaId", String.valueOf(query.getCmcId()), ConditionOper.Equal));
        } else {
            conditionDtos.add(new ConditionDto("cmcPmAreaId", "0", ConditionOper.Equal));
        }
        conditionDtos.add(new ConditionDto("isDelete", "0", ConditionOper.Equal));
        dataDto.setConditions(conditionDtos);
        Object resData = cmcPmCompanyService.getAreaPage(dataDto);
        if (resData == null) {
            resData = new ArrayList<>();
        }
        List<CmcPmAreaVo> areaVos = new ArrayList<>();
        List<Long> prcIds = new ArrayList<>(areaVos.size());
        PmWorkShopEntity pmWorkShopEntity = pmAllVo.getWorkShops().get(0);
        CmcPmAreaVo areaVo = new CmcPmAreaVo();
        areaVo.setAreaCode(pmWorkShopEntity.getWorkshopCode());
        areaVo.setAreaName(pmWorkShopEntity.getWorkshopName());
        areaVo.setDisplayNo(pmWorkShopEntity.getDisplayNo());
        areaVo.setIsEnable(true);
        areaVo.setPmAreaDesignJph(pmWorkShopEntity.getWorkshopDesignJph());
        areaVo.setProductTime(pmWorkShopEntity.getProductTime());
        areaVo.setRemark(pmWorkShopEntity.getRemark());
        areaVo.setSourceId(pmWorkShopEntity.getId());
        areaVo.setCmcPmOrganizationId(pmWorkShopEntity.getPrcPmOrganizationId());
        prcIds.add(pmWorkShopEntity.getId());
        areaVos.add(areaVo);
        //查询缓存表
        List<CmcPmAreaVo> cmcLineVos = JsonUtils.parseArray(JsonUtils.toJsonString(resData), CmcPmAreaVo.class);
        List<String> ids = cmcLineVos.stream().map(e -> String.valueOf(e.getCmcPmAreaId())).collect(Collectors.toList());
        List<CmcPmAreaVo> existData = new ArrayList<>(ids.size());
        Set<Long> exitsIds = new HashSet<>(ids.size());
        if (CollectionUtils.isNotEmpty(ids)) {
            List<PmSyncEntity> existDataTemp = pmSyncService.getPmSyncsByCmcIds(ids);
            existData = existDataTemp.stream().map(s -> {
                CmcPmAreaVo cmcPmAreaVo = new CmcPmAreaVo();
                cmcPmAreaVo.setSourceId(s.getPrcId());
                cmcPmAreaVo.setCmcPmAreaId(s.getCmcId());
                exitsIds.add(s.getPrcId());
                return cmcPmAreaVo;
            }).collect(Collectors.toList());
        }

        //处理要操作的数据
        List<CmcPmAreaVo> updateCollect = areaVos.stream().filter(c -> exitsIds.contains(c.getSourceId())).collect(Collectors.toList());
        List<CmcPmAreaVo> insertCollect = areaVos.stream().filter(c -> !exitsIds.contains(c.getSourceId())).collect(Collectors.toList());
        List<CmcPmAreaVo> delCollect = existData.stream().filter(c -> !prcIds.contains(c.getSourceId())).collect(Collectors.toList());
        //修改数据
        List<CmcPmAreaVo> commitUpdate = updateCollect.stream().map(e -> {
            QueryWrapper<PmSyncEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PmSyncEntity::getPrcId, e.getSourceId());
            PmSyncEntity pmSyncEntity = pmSyncService.getData(queryWrapper, false).stream().findFirst().orElse(null);

            QueryWrapper<PmSyncEntity> shopId = new QueryWrapper<>();
            shopId.lambda().eq(PmSyncEntity::getPrcId, e.getCmcPmOrganizationId());
            PmSyncEntity shopIdEntity = pmSyncService.getData(shopId, false).stream().findFirst().orElse(null);

            if (pmSyncEntity != null && shopId != null) {
                e.setCmcPmAreaId(pmSyncEntity.getCmcId());
                e.setCmcPmOrganizationId(shopIdEntity.getCmcId());
            }
            return e;
        }).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(commitUpdate)) {
            cmcPmFactoryService.areaBatchSave(commitUpdate);
        }
        //保存数据
        List<CmcPmAreaVo> commitInsert = insertCollect.stream().map(m -> {
            QueryWrapper<PmSyncEntity> shopId = new QueryWrapper<>();
            shopId.lambda().eq(PmSyncEntity::getPrcId, m.getCmcPmOrganizationId());
            PmSyncEntity shopIdEntity = pmSyncService.getData(shopId, false).stream().findFirst().orElse(null);
            if (shopIdEntity != null) {
                m.setCmcPmOrganizationId(shopIdEntity.getCmcId());
            }
            return m;
        }).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(commitInsert)) {
            saveCmcShop(commitInsert);
        }
        //删除数据
        List<Long> commitDelIds = delCollect.stream().map(CmcPmAreaVo::getCmcPmAreaId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(commitDelIds)) {
            delCmShop(commitDelIds);
        }

    }

    private void saveCmcShop(List<CmcPmAreaVo> commitInsert) {
        Object res = cmcPmFactoryService.areaBatchSave(commitInsert);
        if (res == null) {
            res = new ArrayList<>();
        }
        List<CmcPmAreaVo> saveVos = JsonUtils.parseArray(JsonUtils.toJsonString(res), CmcPmAreaVo.class);
        List<PmSyncEntity> list = new ArrayList<>();
        saveVos.stream().forEach(saveVo -> {
            PmSyncEntity saveEntity = new PmSyncEntity();
            saveEntity.setCmcId(saveVo.getCmcPmAreaId());
            saveEntity.setPrcId(saveVo.getSourceId());
            saveEntity.setDataType("area");
            list.add(saveEntity);
        });
        pmSyncService.insertBatch(list);
        pmSyncService.saveChange();
    }

    private void delCmShop(List<Long> commitDelIds) {
        cmcPmFactoryService.batchDelete("area", commitDelIds);
        List<String> selfCmcIds = commitDelIds.stream().map(s -> String.valueOf(s)).collect(Collectors.toList());
        pmSyncService.delete(Arrays.asList(new ConditionDto("cmcId", String.join("|", selfCmcIds), ConditionOper.In)));
        pmSyncService.saveChange();
    }

    private void processPlant(PmAllVo pmAllVo) {
        QueryWrapper<PmSyncEntity> queryW = new QueryWrapper<>();
        queryW.lambda().eq(PmSyncEntity::getPrcId, pmAllVo.getOrganization().getId());
        PmSyncEntity query = pmSyncService.getData(queryW, false).stream().findFirst().orElse(null);
        DataDto dataDto = new DataDto();
        List<ConditionDto> conditionDtos = new ArrayList<>(2);
        if (query != null) {
            conditionDtos.add(new ConditionDto("cmcPmOrganizationId", String.valueOf(query.getCmcId()), ConditionOper.Equal));
        } else {
            conditionDtos.add(new ConditionDto("cmcPmOrganizationId", "0", ConditionOper.Equal));
        }
        conditionDtos.add(new ConditionDto("isDelete", "0", ConditionOper.Equal));
        dataDto.setConditions(conditionDtos);
        Object resObject = cmcPmCompanyService.getPlantPage(dataDto);
        if (resObject == null) {
            resObject = new ArrayList<>();
        }
        //mom工厂
        PmOrganizationEntity organization = pmAllVo.getOrganization();
        CmcPmOrganizationVo vo = new CmcPmOrganizationVo();
        vo.setOrganizationCode(organization.getOrganizationCode());
        vo.setOrganizationName(organization.getOrganizationName());
        vo.setAddress(organization.getAddress());
        vo.setIsEnable(true);
        vo.setRemark(organization.getRemark());
        vo.setSourceId(organization.getId());
        List<CmcPmOrganizationVo> pmVos = new ArrayList<>(1);
        List<Long> prcIds = new ArrayList<>(pmVos.size());
        prcIds.add(organization.getId());
        pmVos.add(vo);
        //查询缓存表
        List<CmcPmOrganizationVo> cmcPmOrganizationVos = JsonUtils.parseArray(JsonUtils.toJsonString(resObject), CmcPmOrganizationVo.class);
        List<String> ids = cmcPmOrganizationVos.stream().map(e -> String.valueOf(e.getCmcPmOrganizationId())).collect(Collectors.toList());
        List<CmcPmOrganizationVo> existData = new ArrayList<>(ids.size());
        List<Long> exitsIds = new ArrayList<>(ids.size());
        if (CollectionUtils.isNotEmpty(ids)) {
            List<PmSyncEntity> existDataTemp = pmSyncService.getPmSyncsByCmcIds(ids);
            existData = existDataTemp.stream().map(s -> {
                CmcPmOrganizationVo cmcPmOrganizationVo = new CmcPmOrganizationVo();
                cmcPmOrganizationVo.setSourceId(s.getPrcId());
                cmcPmOrganizationVo.setCmcPmOrganizationId(s.getCmcId());
                exitsIds.add(s.getPrcId());
                return cmcPmOrganizationVo;
            }).collect(Collectors.toList());
        }
        //处理要操作的数据
        List<CmcPmOrganizationVo> updateCollect = pmVos.stream().filter(c -> exitsIds.contains(c.getSourceId())).collect(Collectors.toList());
        List<CmcPmOrganizationVo> insertCollect = pmVos.stream().filter(c -> !exitsIds.contains(c.getSourceId())).collect(Collectors.toList());
        List<CmcPmOrganizationVo> delCollect = existData.stream().filter(c -> !prcIds.contains(c.getSourceId())).collect(Collectors.toList());
        //修改数据
        List<CmcPmOrganizationVo> commitUpdate = updateCollect.stream().map(e -> {
            QueryWrapper<PmSyncEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PmSyncEntity::getPrcId, e.getSourceId());
            PmSyncEntity pmSyncEntity = pmSyncService.getData(queryWrapper, false).stream().findFirst().orElse(null);
            if (pmSyncEntity != null) {
                e.setCmcPmOrganizationId(pmSyncEntity.getCmcId());
            }
            return e;
        }).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(commitUpdate)) {
            commitUpdate.forEach(c -> {
                cmcPmCompanyService.edit(c);
            });
        }
        //保存数据
        insertCollect.forEach(m -> m.setCmcPmOrganizationId(Constant.DEFAULT_ID));
        if (CollectionUtils.isNotEmpty(insertCollect)) {
            insertCollect.stream().forEach(c -> {
                saveCmcPlant(c);
            });
            pmSyncService.saveChange();
        }
        //删除数据
        List<Long> commitDelIds = delCollect.stream().map(CmcPmOrganizationVo::getCmcPmOrganizationId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(commitDelIds)) {
            delCmPlant(commitDelIds);
        }

    }

    private void saveCmcPlant(CmcPmOrganizationVo cmcPmOrganizationVo) {
        Object resData = cmcPmCompanyService.edit(cmcPmOrganizationVo);
        if (resData == null) {
            resData = new ArrayList<>();
        }
        CmcPmOrganizationVo saveVo = JsonUtils.parseObject(JsonUtils.toJsonString(resData), CmcPmOrganizationVo.class);
        PmSyncEntity saveEntity = new PmSyncEntity();
        saveEntity.setCmcId(saveVo.getCmcPmOrganizationId());
        saveEntity.setPrcId(cmcPmOrganizationVo.getSourceId());
        saveEntity.setDataType("organization");
        pmSyncService.save(saveEntity);
    }

    private void delCmPlant(List<Long> commitDelIds) {
        cmcPmCompanyService.del(commitDelIds);
        List<String> selfCmcIds = commitDelIds.stream().map(s -> String.valueOf(s)).collect(Collectors.toList());
        pmSyncService.delete(Arrays.asList(new ConditionDto("cmcId", String.join("|", selfCmcIds), ConditionOper.In)));
        pmSyncService.saveChange();
    }

    /**
     * <summary>
     * 工厂建模启动以后，需要进行一些其他的处理，比如，创建工单的顺序号生成配置
     * </summary>
     * <param name="pmVersionInfo"></param>
     */
    private void afterEnableShopVersion(PmVersionEntity pmVersionInfo) {
        String numSeq = "PpsEntry_" + pmVersionInfo.getWorkshopCode() + "_Seq";
        String numCode = "PpsEntry_" + pmVersionInfo.getWorkshopCode() + "_Code";
        if (sysSequenceNumberService.getSequenceTypeCount(numSeq) == 0) {
            SysSequenceNumberEntity entity = new SysSequenceNumberEntity();
            entity.setSequenceType(numSeq);
            entity.setSequencenumberLen(9);
            entity.setResetType("scope");
            entity.setCurMaxDate(new Date());
            entity.setMinValue(1);
            entity.setMaxValue(999999999);
            sysSequenceNumberService.insert(entity);
        }
        if (sysSequenceNumberService.getSequenceTypeCount(numCode) == 0) {
            SysSequenceNumberEntity entity = new SysSequenceNumberEntity();
            entity.setSequenceType(numCode);
            entity.setSequencenumberLen(4);
            entity.setResetType("day");
            entity.setPartDayFmt("yyyy");
            entity.setPartMonthFmt("mm");
            entity.setPartDayFmt("dd");
            entity.setCurMaxDate(new Date());
            entity.setMinValue(1);
            entity.setMaxValue(9999);
            sysSequenceNumberService.insert(entity);
        }
    }

    /**
     * 启用特定版本的工厂模型
     *
     * <param name="data"></param>
     **/
    public void enableShopVersion(PmVersionEntity data) throws LinkException {
        Boolean isEnabled = !data.getIsEnabled();
        UpdateWrapper<PmVersionEntity> wr = new UpdateWrapper<>();
        wr.lambda().set(PmVersionEntity::getIsEnabled, isEnabled)
                .set(PmVersionEntity::getBeginDt, new Date())
                .set(PmVersionEntity::getBeginUserId, identityHelper.getUserId())
                .set(PmVersionEntity::getBeginUserName, identityHelper.getUserName())
                .set(PmVersionEntity::getIsDownAndon, false)
                .eq(PmVersionEntity::getId, data.getId())
                .eq(PmVersionEntity::getIsEnabled, false)
                .eq(PmVersionEntity::getWorkshopCode, data.getWorkshopCode())
                .eq(PmVersionEntity::getIsDelete, false);
        this.update(wr);

        UpdateWrapper<PmVersionEntity> wrOther = new UpdateWrapper<>();
        wrOther.lambda().set(PmVersionEntity::getIsEnabled, false)
                .set(PmVersionEntity::getEndDt, new Date())
                .set(PmVersionEntity::getEndUserId, identityHelper.getUserId())
                .set(PmVersionEntity::getEndUserName, identityHelper.getUserName())
                .ne(PmVersionEntity::getId, data.getId())
                .eq(PmVersionEntity::getIsEnabled, true)
                .eq(PmVersionEntity::getWorkshopCode, data.getWorkshopCode())
                .eq(PmVersionEntity::getIsDelete, false);
        this.update(wrOther);
    }

    private PmInfo resetWorkshopAndVersionRedisCache(PmVersionEntity entity) {
        String key = String.format("Pm:%s-%s", entity.getWorkshopCode(), entity.getVersion());
        PmInfo pmInfo = convertJsonStringToPmInfo(entity);
        redisUtils.set(key, pmInfo, -1);
        return pmInfo;
    }

    private void clearRedisCacheByShopCodeAndVersion(PmVersionEntity entity) {
        List<PmVersionEntity> lastVersions = getLastVersions(entity);
        if (!lastVersions.isEmpty()) {
            List<String> keys = new ArrayList<>(lastVersions.size());
            for (PmVersionEntity item : lastVersions) {
                keys.add(String.format("Pm:%s-%s", entity.getWorkshopCode(), item.getVersion()));
            }
            redisUtils.delete(keys);
        }

    }

    private List<PmVersionEntity> getLastVersions(PmVersionEntity entity) {
        QueryWrapper<PmVersionEntity> qw = new QueryWrapper();
        qw.select("VERSION");
        qw.eq("WORKSHOP_CODE", entity.getWorkshopCode());
        qw.ne("VERSION", entity.getVersion());
        return this.getData(qw, false);
    }


    @Override
    public ResultVO restDownConfig(String shopCode) {
        try {
            LambdaQueryWrapper<PmVersionEntity> lw = new LambdaQueryWrapper<>();
            lw.eq(PmVersionEntity::getWorkshopCode, shopCode);
            PmVersionEntity entity = versionMapper.selectOne(lw);
            if (ObjectUtil.isEmpty(entity)) {
                throw new InkelinkException("未找到当前车间启用版本");
            }
            entity.setIsDownOt(false);
            entity.setIsDownAndon(false);
            versionMapper.updateById(entity);
            return new ResultVO<>().ok(null, "重置成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultVO().error("重置失败");
        }
    }

    @Override
    public ResultVO getPmDifference(String ids) {
        JSONArray array = new JSONArray();
        if (StringUtils.isNotBlank(ids)) {
            String[] idsTemp = ids.split(",");
            PmVersionEntity pmVersionEntity = versionMapper.selectById(ConvertUtils.stringToLong(idsTemp[0]));
            PmVersionEntity pmVersionEntity1 = versionMapper.selectById(ConvertUtils.stringToLong(idsTemp[1]));
            if (!Objects.isNull(pmVersionEntity) && !Objects.isNull(pmVersionEntity1)) {
                if (!Objects.equals(pmVersionEntity.getWorkshopCode(), pmVersionEntity1.getWorkshopCode())) {
                    throw new InkelinkException("不同车间不能对比");
                }
                array.add(treeJson(pmVersionEntity.getContent()));
                array.add(treeJson(pmVersionEntity1.getContent()));
            }
        }
        return new ResultVO().ok(array);
    }

    private JSONObject treeJson(String json) {
        PmAllVo pmAllVo = JsonUtils.parseObject(json, PmAllVo.class);
        List<PmToolJobEntity> toolJobs = pmAllVo.getToolJobs();
        List<PmToolEntity> tools = pmAllVo.getTools();
        List<PmOtEntity> ots = pmAllVo.getOts();
        List<PmPullCordEntity> pullCords = pmAllVo.getPullCords();
        List<PmWoEntity> wos = pmAllVo.getWos();
        List<PmBopEntity> bops = pmAllVo.getBops();
        List<PmWorkStationEntity> workstations = pmAllVo.getWorkStations();
        List<PmAviEntity> avis = pmAllVo.getAvis();
        List<PmLineEntity> lines = pmAllVo.getLines();

        PmViewVo viewVo = new PmViewVo();
        //线体
        List<PmLineEntity> finalLines = lines.stream().map(line -> {
            //avi
            List<PmAviEntity> finalAvis = avis.stream().filter(avi -> avi.getPrcPmLineId().equals(line.getId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(finalAvis)) {
                line.setPmAviEntity(finalAvis);
            }
            //工位
            List<PmWorkStationEntity> finalWorkStations = workstations.stream().filter(workStation -> workStation.getPrcPmLineId().equals(line.getId())).map(workstation -> {
                //ot
                List<PmOtEntity> finalOts = ots.stream().filter(ot -> ot.getPrcPmWorkstationId().equals(workstation.getId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(finalOts)) {
                    workstation.setPmOtEntity(finalOts);
                }
                //wo
                List<PmWoEntity> finalWos = wos.stream().filter(wo -> wo.getPrcPmWorkstationId().equals(workstation.getId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(finalWos)) {
                    workstation.setPmWoEntity(finalWos);
                }
                //tool
                List<PmToolEntity> finalTools = tools.stream().filter(tool -> tool.getPrcPmWorkstationId().equals(workstation.getId())).map(tool -> {
                    //tooljob
                    List<PmToolJobEntity> collect = toolJobs.stream().filter(t -> t.getPrcPmToolId().equals(tool.getId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(collect)) {
                        tool.setPmToolJobEntity(collect);
                    }
                    return tool;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(finalTools)) {
                    workstation.setPmToolEntity(finalTools);
                }
                //pullcord
                List<PmPullCordEntity> finalPullCords = pullCords.stream().filter(pullCord -> pullCord.getPrcPmWorkstationId().equals(workstation.getId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(finalPullCords)) {
                    workstation.setPmPullCordEntity(finalPullCords);
                }
                //bop
                List<PmBopEntity> finalBops = bops.stream().filter(bop -> bop.getPrcPmWorkstationId().equals(workstation.getId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(finalBops)) {
                    workstation.setPmBopEntity(finalBops);
                }
                return workstation;
            }).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(finalWorkStations)) {
                line.setPmWorkStationEntity(finalWorkStations);
            }
            return line;
        }).collect(Collectors.toList());

        PmWorkShopEntity pmWorkShopEntity = pmAllVo.getWorkShops().get(0);
        pmWorkShopEntity.setPmLineEntity(finalLines);
        viewVo.setPmWorkShopEntity(pmWorkShopEntity);

        String viewString = JsonUtils.toJsonString(viewVo);
        JSONObject jsonObject = JSONObject.parseObject(viewString);

        jsonObject.put("PMVersion", pmAllVo.getVersion());
        return jsonObject;
    }

    /**
     * 获取当前的工厂建模对象缓存
     *
     * @param nodeName
     * @param parentId
     * @return
     */
//    @Override
//    public ResultVO getPmCombo(String nodeName, String parentId) {
//        List<PmWorkShopEntity> shops = getObjectedPm().getShops();
//        List<String> shopCodes = shops.stream().map(PmWorkShopEntity :: getWorkshopCode).collect(Collectors.toList());
//        List<PmAllVo> pmAllVos =  getCurretShopPms(shopCodes);
//        List<PmComboDto> allResult = new ArrayList<>();
//        pmAllVos.forEach(item ->{
//            allResult.addAll(getCombo(item, nodeName, parentId));
//        });
//        return new ResultVO().ok(allResult);
//    }
    @Override
    public ResultVO getPmCombo(String nodeName, String parentId) {
        List<PmWorkShopEntity> shops = getObjectedPm().getShops();
        List<PmComboDto> allResult = new ArrayList<>();
        for (PmWorkShopEntity item : shops) {
            try {
                PmAllVo pmAllVo = getCurretShopPms(item.getWorkshopCode());
                allResult.addAll(getCombo(pmAllVo, nodeName, parentId));
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return new ResultVO().ok(allResult);
    }

    @Override
    public ResultVO getPmShopCombo(String shopCode, String nodeName, String parentId) {
        PmAllVo pmAllVo = getCurretShopPms(shopCode);
        List<PmComboDto> maps = getCombo(pmAllVo, nodeName, parentId);
        return new ResultVO().ok(maps, "获取数据成功");
    }

    @Override
    public ResultVO getShopCombo() {
        List<PmWorkShopEntity> shops = getObjectedPm().getShops();
        List<ComboInfoDTO> list = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(shops)) {
            shops.forEach(t -> {
                ComboInfoDTO dto = new ComboInfoDTO();
                dto.setText(t.getWorkshopName());
                dto.setValue(t.getWorkshopCode());
                list.add(dto);
            });
        }
        return new ResultVO().ok(list, "获取数据成功");
    }


    @Override
    public ResultVO getAllShopCombo() {
        PmAllDTO pmAllVo = getObjectedPm();
        List<PmWorkShopEntity> shopList = pmAllVo.getShops();
        if (shopList == null || shopList.isEmpty()) {
            throw new InkelinkException("没有获取到车间信息");
        }
        List<ComboInfoDTO> comBolist = new ArrayList<>(shopList.size());
        shopList.forEach(eachShop -> {
            ComboInfoDTO dto = new ComboInfoDTO();
            dto.setText(eachShop.getWorkshopCode());
            dto.setValue(String.valueOf(eachShop.getId()));
            comBolist.add(dto);
        });
        return new ResultVO().ok(comBolist, "获取数据成功");
    }

    @Override
    public ResultVO getAreaPosition(String shopCode) {
        PmAllVo pmAllVo = getCurretShopPms(shopCode);
        List<PmLineEntity> lineList = pmAllVo.getLines();
        if (lineList == null || lineList.isEmpty()) {
            throw new InkelinkException("没有获取到线体信息");
        }
        List<ComboInfoDTO> comBolist = new ArrayList<>(lineList.size());
        lineList.forEach(eachLine -> {
            ComboInfoDTO dto = new ComboInfoDTO();
            dto.setText(eachLine.getLineName());
            dto.setValue(String.valueOf(eachLine.getId()));
            comBolist.add(dto);
        });
        return new ResultVO().ok(comBolist, "获取数据成功");
    }

    @Override
    public ResultVO getWorkStationCombo(String shopCode, Integer stationType) {
        PmAllVo pmAllVo = getCurretShopPms(shopCode);
        List<PmWorkStationEntity> workstationList = pmAllVo.getWorkStations();
        if (workstationList == null || workstationList.isEmpty()) {
            throw new InkelinkException("没有获取到任何工位");
        }
        workstationList = workstationList.stream().filter(t ->
                stationType == null || stationType.equals(t.getWorkstationType())
        ).collect(Collectors.toList());

        List<ComboInfoDTO> list = new ArrayList<>(workstationList.size());
        workstationList.forEach(workstation -> {
            ComboInfoDTO dto = new ComboInfoDTO();
            dto.setText(workstation.getWorkstationName());
            dto.setValue(String.valueOf(workstation.getId()));
            list.add(dto);
        });
        return new ResultVO().ok(list, "获取数据成功");
    }

    @Override
    public ResultVO geWorkStationAllCombo(Integer stationType) {
        List<PmWorkStationEntity> workstationInfos = getObjectedPm().getStations();
        if (ObjectUtil.isNotEmpty(workstationInfos)) {
            workstationInfos = workstationInfos.stream().filter(t ->
                    stationType == null || t.getWorkstationType().equals(stationType)
            ).collect(Collectors.toList());
        }
        List<ComboSelectDTO> list = new ArrayList<>();
        workstationInfos.forEach(t -> {
            ComboSelectDTO dto = new ComboSelectDTO();
            dto.setName(t.getWorkstationName());
            dto.setId(String.valueOf(t.getId()));
            dto.setCode(t.getWorkstationCode());
            dto.setLabel(t.getWorkstationName() + "[" + t.getWorkstationCode() + "]");
            list.add(dto);
        });
        return new ResultVO().ok(list, "获取数据成功");
    }

    @Override
    public ResultVO getWorkStationComboByLineId(Long lineId, Integer workStationType) {
        PmAllDTO pmObj = getObjectedPm();
        if (pmObj == null) {
            return new ResultVO().error("没有发布工厂数据");
        }

        if (lineId == null) {
            return new ResultVO().error("没有获取到数据");
        }
        List<PmWorkStationEntity> stations = pmObj.getStations().stream()
                .filter(t -> lineId.equals(t.getPrcPmLineId())
                        && (workStationType == null
                        || workStationType.equals(t.getWorkstationType())))
                .sorted(Comparator.comparing(s -> s.getWorkstationCode()))
                .collect(Collectors.toList());
        if (stations.isEmpty()) {
            return new ResultVO().error("没有获取到数据");
        }
        List<ComboInfoDTO> list = new ArrayList<>();
        for (PmWorkStationEntity t : stations) {
            ComboInfoDTO dto = new ComboInfoDTO();
            dto.setText(t.getWorkstationName());
            dto.setValue(String.valueOf(t.getId()));
            list.add(dto);
        }
        return new ResultVO().ok(list);
    }

    @Override
    public ResultVO getWorkStationByLineCode(String lineCode, Integer workStationType) {
        PmAllDTO pmObj = getObjectedPm();

        if (pmObj == null) {
            return new ResultVO().error("没有发布工厂数据");
        }
        List<PmWorkStationEntity> stations = new ArrayList<>();
        if (StringUtils.isBlank(lineCode)) {
            stations = pmObj.getStations();
        } else {
            PmLineEntity line = pmObj.getLines().stream().filter(c -> StringUtils.equals(c.getLineCode(), lineCode)).findFirst().orElse(null);
            if (line == null) {
                return new ResultVO().ok(stations);
            }
            stations = pmObj.getStations().stream()
                    .filter(t -> line.getId().equals(t.getPrcPmLineId())
                            && (workStationType == null
                            || workStationType.equals(t.getWorkstationType())))
                    .sorted(Comparator.comparing(s -> s.getWorkstationCode()))
                    .collect(Collectors.toList());
        }
        return new ResultVO().ok(stations);
    }

    @Override
    public ResultVO getAllObjectPm() {
        PmAllDTO pmObj = getObjectedPm();
        if (pmObj == null) {
            return new ResultVO().ok(null, "没有发布工厂数据");
        }
        return new ResultVO().ok(pmObj, "获取数据成功");
    }

    @Override
    public ResultVO getTreeData(Long id, String shopCode, String nodeTypes) {
        List<TreeNode> nodes = new ArrayList<>();
        List<String> nodeTypeList = Arrays.asList((nodeTypes == null ? "" : nodeTypes).split("\\|"));

        Document xDoc = null;
        if (id != null) {
            PmVersionEntity data = versionMapper.selectById(id);
            if (ObjectUtil.isNotEmpty(data)) {
                xDoc = getXdoc(data.getContent());
            }
        } else {
            xDoc = getCurretShopPm(shopCode);
        }
        if (xDoc != null) {
            Element shop = xDoc.getRootElement().element("PmShopInfo");

            TreeNode shopNode = new TreeNode();
            shopNode.setId(shop.attributeValue("Id"));
            shopNode.setCode(shop.attributeValue("Code"));
            shopNode.setText(shop.attributeValue("Name"));
            shopNode.setIconCls(getIcon("PmShop"));

            Map<String, Object> map = Maps.newHashMapWithExpectedSize(7);
            map.put("Type", "PmShop");
            map.put("Data", shop);
            map.put("NewModel", shop);
            map.put("OldModel", "");
            map.put("DiffFields", new ArrayList<String>());
            map.put("Oper", "none");

            shopNode.setExtendData(map);
            nodes.add(shopNode);

            getDataDifference(xDoc.getRootElement().element("PmShopInfo"), null, shopNode, nodeTypeList);
        }
        return new ResultVO().ok(nodes);
    }

    private void getChildNodesByNodeName(Element parent, String nodeName, List<Element> targetChildNodeList) {
        List<Element> childNodeList = parent.elements(nodeName);
        if (!childNodeList.isEmpty()) {
            targetChildNodeList.addAll(childNodeList);
        }
        childNodeList = parent.elements();
        for (Element childNode : childNodeList) {
            getChildNodesByNodeName(childNode, nodeName, targetChildNodeList);
        }
    }

    /**
     * @param pmAllVo
     * @param nodeName
     * @param parentId 已廢棄
     * @return
     */
    private List<PmComboDto> getCombo(PmAllVo pmAllVo, String nodeName, String parentId) {
        List<PmComboDto> targetList = new ArrayList<>();
        Long pid = ConvertUtils.stringToLong(parentId);
        //判断权限
        switch (nodeName) {
            case "PmShopInfo":
                pmAllVo.getWorkShops().stream().forEach(t -> {
                    PmComboDto pmComboDto = new PmComboDto();
                    pmComboDto.setCode(t.getWorkshopCode());
                    pmComboDto.setValue(String.valueOf(t.getId()));
                    pmComboDto.setText(t.getWorkshopName());
                    pmComboDto.setPmShopId(String.valueOf(t.getId()));
                    pmComboDto.setPmShopCode(t.getWorkshopCode());
                    targetList.add(pmComboDto);
                });
                break;
            case "PmAreaInfo":
                List<PmLineEntity> linesStream = pmAllVo.getLines().stream().filter(s -> s.getIsEnable()).collect(Collectors.toList());
                if (!StringUtils.isBlank(parentId)) {
                    if (pid == 0) {
                        PmWorkShopEntity shop = pmAllVo.getWorkShops().stream().filter(c -> Objects.equals(c.getWorkshopCode(), parentId)).findFirst().orElse(null);
                        if (shop != null) {
                            pid = shop.getId();
                        }
                    }
                    Long finalPid3 = pid;
                    linesStream = linesStream.stream().filter(c -> Objects.equals(c.getPrcPmWorkshopId(), finalPid3))
                            .collect(Collectors.toList());
                }
                linesStream.forEach(t -> {
                    PmComboDto pmComboDto = new PmComboDto();
                    pmComboDto.setCode(t.getLineCode());
                    pmComboDto.setValue(String.valueOf(t.getId()));
                    pmComboDto.setText(t.getLineName());
                    pmComboDto.setPmShopId(String.valueOf(t.getPrcPmWorkshopId()));
                    pmComboDto.setPmShopCode(getPmShopCode(pmAllVo, t.getPrcPmWorkshopId()));
                    targetList.add(pmComboDto);
                });
                break;

            case "TeamNo":
                pmAllVo.getWorkStations().stream().filter(c -> !StringUtils.isBlank(c.getTeamNo()) && c.getIsEnable()).forEach(t -> {
                    PmComboDto pmComboDto = new PmComboDto();
                    pmComboDto.setCode(t.getTeamNo());
                    pmComboDto.setValue(t.getTeamNo());
                    pmComboDto.setText(t.getTeamNo());
                    pmComboDto.setPmShopId(String.valueOf(t.getPrcPmWorkshopId()));
                    pmComboDto.setPmShopCode(getPmShopCode(pmAllVo, t.getPrcPmWorkshopId()));
                    pmComboDto.setPmAreaId(String.valueOf(t.getPrcPmLineId()));
                    targetList.add(pmComboDto);
                });
                break;
            case "PmAviInfo":
                List<PmAviEntity> aviStream = pmAllVo.getAvis().stream().filter(s -> s.getIsEnable()).collect(Collectors.toList());
                if (!StringUtils.isBlank(parentId)) {
                    if (pid == 0) {
                        PmLineEntity line = pmAllVo.getLines().stream().filter(c -> Objects.equals(c.getLineCode(), parentId)).findFirst().orElse(null);
                        if (line != null) {
                            pid = line.getId();
                        }
                    }
                    Long finalPid2 = pid;
                    aviStream = aviStream.stream().filter(c -> Objects.equals(c.getPrcPmLineId(), finalPid2))
                            .collect(Collectors.toList());
                }
                aviStream.forEach(t -> {
                    PmComboDto pmComboDto = new PmComboDto();
                    pmComboDto.setCode(t.getAviCode());
                    pmComboDto.setValue(String.valueOf(t.getId()));
                    pmComboDto.setText(t.getAviName());
                    pmComboDto.setPmShopId(String.valueOf(t.getPrcPmWorkshopId()));
                    pmComboDto.setPmShopCode(getPmShopCode(pmAllVo, t.getPrcPmWorkshopId()));
                    pmComboDto.setPmAreaId(String.valueOf(t.getPrcPmLineId()));
                    targetList.add(pmComboDto);
                });
                break;
            case "PmStationInfo":
                List<PmWorkStationEntity> stationsStream = pmAllVo.getWorkStations().stream().filter(s -> s.getIsEnable()).collect(Collectors.toList());
                if (!StringUtils.isBlank(parentId)) {
                    if (pid == 0) {
                        PmLineEntity line = pmAllVo.getLines().stream().filter(c -> Objects.equals(c.getLineCode(), parentId)).findFirst().orElse(null);
                        if (line != null) {
                            pid = line.getId();
                        }
                    }
                    Long finalPid1 = pid;
                    stationsStream = stationsStream.stream().filter(c -> Objects.equals(c.getPrcPmLineId(), finalPid1))
                            .collect(Collectors.toList());
                }
                stationsStream.forEach(t -> {
                    PmComboDto pmComboDto = new PmComboDto();
                    pmComboDto.setCode(t.getWorkstationCode());
                    pmComboDto.setValue(String.valueOf(t.getId()));
                    pmComboDto.setText(t.getWorkstationName());
                    pmComboDto.setPmShopId(String.valueOf(t.getPrcPmWorkshopId()));
                    pmComboDto.setPmShopCode(getPmShopCode(pmAllVo, t.getPrcPmWorkshopId()));
                    pmComboDto.setPmAreaId(String.valueOf(t.getPrcPmLineId()));
                    targetList.add(pmComboDto);
                });
                break;
            case "PmWoInfo":
                List<PmWoEntity> pmWoStream = pmAllVo.getWos().stream().filter(s -> s.getIsEnable()).collect(Collectors.toList());
                if (!StringUtils.isBlank(parentId)) {
                    if (pid == 0) {
                        PmWorkStationEntity station = pmAllVo.getWorkStations().stream().filter(c -> Objects.equals(c.getWorkstationCode(), parentId)).findFirst().orElse(null);
                        if (station != null) {
                            pid = station.getId();
                        }
                    }
                    Long finalPid = pid;
                    pmWoStream = pmWoStream.stream().filter(c -> Objects.equals(c.getPrcPmWorkstationId(), finalPid))
                            .collect(Collectors.toList());
                }
                pmWoStream.forEach(t -> {
                    PmComboDto pmComboDto = new PmComboDto();
                    pmComboDto.setCode(t.getWoCode());
                    pmComboDto.setValue(String.valueOf(t.getId()));
                    pmComboDto.setText(t.getWoDescription());
                    pmComboDto.setPmShopId(String.valueOf(t.getPrcPmWorkshopId()));
                    pmComboDto.setPmShopCode(getPmShopCode(pmAllVo, t.getPrcPmWorkshopId()));
                    pmComboDto.setPmAreaId(String.valueOf(t.getPrcPmLineId()));
                    pmComboDto.setPmStationId(String.valueOf(t.getPrcPmWorkstationId()));
                    targetList.add(pmComboDto);
                });
                break;
            default:
                break;
        }
        return targetList;
    }

    private String getPmShopCode(PmAllVo pmAllVo, Long shopId) {
        PmWorkShopEntity shop = pmAllVo.getWorkShops().stream().filter(c -> Objects.equals(c.getId(), shopId)).findFirst().orElse(null);
        return shop == null ? "" : shop.getWorkshopCode();
    }

    private void getDataDifference(Element newData, Element oldData, TreeNode parentNode, List<String> nodeTypeList) {
        List children = parentNode.getChildren();
        if (!newData.equals(oldData))//有更改
        {
            List<Element> newChildren = newData == null ? new ArrayList<>() : newData.elements();
            List<Element> oldChildren = oldData == null ? new ArrayList<Element>() : oldData.elements();
            //筛选新版本中未出现的过id 也就是新增数据

            List<Element> addChildren = new ArrayList<>();
            List<Element> updateChildren = new ArrayList<>();
            for (Element newChild : newChildren) {
                for (Element oldChild : oldChildren) {
                    if (!oldChild.attribute("Id").getValue().equals(newChild.attribute("Id").getValue())) {
                        addChildren.add(newChild);
                    } else {
                        updateChildren.add(oldChild);
                    }
                }
            }
            for (Element child : addChildren) {
                String s = nodeTypeList.stream().filter(o -> (o + "Info").equals(child.getName())).findFirst().orElse(StringUtils.EMPTY);
                if (ObjectUtil.isNotEmpty(nodeTypeList) && StringUtils.isEmpty(s)) {
                    continue;
                }

                TreeNode node = getDifferenceTreeNode(child, null, "add", child.getName(), null);
                getDataDifference(child, null, node, nodeTypeList);
                parentNode.getChildren().add(node);
            }

            //更新


            for (Element child : updateChildren) {
                String s = nodeTypeList.stream().filter(o -> (o + "info").equals(child.getName())).findFirst().orElse(StringUtils.EMPTY);
                if (ObjectUtil.isNotEmpty(nodeTypeList) && StringUtils.isEmpty(s)) {
                    continue;
                }
                Element oldChild = oldChildren.stream().filter(o -> o.attributeValue("Id").equals(child.attributeValue("Id"))).findFirst().orElse(null);

                if (oldChild == null) {
                    continue;
                }

                if (!oldChild.toString().equals(child.toString())) {
                    List<String> exceptFields = Arrays.asList("Id", "IsDelete", "CreateUserId", "CreateDt", "UpdateUserId", "lastUpdatedDate");
                    //获取变更的数据：
                    Map<String, String> diffFields = Maps.newHashMapWithExpectedSize(16);
                    if (!child.toString().substring(0, child.toString().indexOf('>')).equals(oldChild.toString().substring(0, oldChild.toString().indexOf('>'))))//该节点数据有变化
                    {
                        List<Attribute> attributes = child.attributes();
                        for (Attribute attr : attributes) {
                            if (exceptFields.contains(attr.getName())) {
                                continue;
                            }
                            if (!child.attribute(attr.getName()).getValue().equals(oldChild.attribute(attr.getName()).getValue())) {
                                String value = oldChild.attribute(attr.getName()).getValue();
                                if (StringUtils.isEmpty(value)) {
                                    value = "空";
                                }
                                diffFields.put(attr.getName(), value);
                            }
                        }

                    }
                    TreeNode node = null;
                    if (diffFields.size() == 0) {
                        node = getDifferenceTreeNode(child, oldChild, "none", child.getName(), diffFields);
                    } else {
                        node = getDifferenceTreeNode(child, oldChild, "update", child.getName(), diffFields);
                    }
                    getDataDifference(child, oldChild, node, nodeTypeList);
                    parentNode.getChildren().add(node);
                }
            }
            //删除
            List<Element> delChildren = new ArrayList<>();
            for (Element oldChild : oldChildren) {
                for (Element newChild : newChildren) {
                    if (!oldChild.attribute("Id").getValue().equals(newChild.attribute("Id").getValue())) {
                        delChildren.add(oldChild);
                    }
                }
            }

            for (Element child : delChildren) {
                String s = nodeTypeList.stream().filter(o -> (o + "info").equals(child.getName())).findFirst().orElse(null);
                if (ObjectUtil.isNotEmpty(nodeTypeList) && StringUtils.isEmpty(s)) {
                    continue;
                }

                TreeNode node = getDifferenceTreeNode(null, child, "del", child.getName(), null);
                getDataDifference(null, child, node, nodeTypeList);
                parentNode.getChildren().add(node);
            }
        }

        parentNode.setLeaf(parentNode.getChildren().size() <= 0);
    }

    private TreeNode getDifferenceTreeNode(Element newData, Element oldData, String oper, String modelType, Map<String, String> diffFields) {
        modelType = modelType.substring(0, modelType.length() - 4);//去掉后面的Info
        Element ele = null;
        if (StringUtils.equals(oper, "del")) {
            ele = oldData;
        } else {
            ele = newData;
        }
        String code = "";
        String text = "";

        String id = "";
        String icon = "";

        Map<String, Object> model = new HashMap<>();
        //数据模型
        switch (modelType) {
            case "PmShop":
                Object pmShop = ele.getData();
                String s = JSONObject.toJSONString(pmShop);
                PmWorkShopEntity entity = JSONObject.parseObject(s, PmWorkShopEntity.class);
                mapData(model, entity.getId(), entity.getWorkshopCode(), entity.getWorkshopName());
                break;
            case "PmArea":
                Object pmArea = ele.getData();
                String pmAreaStr = JSONObject.toJSONString(pmArea);
                PmLineEntity pmAreaEntity = JSONObject.parseObject(pmAreaStr, PmLineEntity.class);
                mapData(model, pmAreaEntity.getId(), pmAreaEntity.getLineCode(), pmAreaEntity.getLineName());
                break;
            case "PmAvi":
                Object avi = ele.getData();
                String aviStr = JSONObject.toJSONString(avi);
                PmAviEntity aviEntity = JSONObject.parseObject(aviStr, PmAviEntity.class);
                mapData(model, aviEntity.getId(), aviEntity.getAviCode(), aviEntity.getAviName());
                break;
            case "PmStation":
                Object station = ele.getData();
                String stationStr = JSONObject.toJSONString(station);
                PmWorkStationEntity stationEntity = JSONObject.parseObject(stationStr, PmWorkStationEntity.class);
                mapData(model, stationEntity.getId(), stationEntity.getWorkstationCode(), stationEntity.getWorkstationName());
                break;
            case "PmWo":
                Object wo = ele.getData();
                String woStr = JSONObject.toJSONString(wo);
                PmWoEntity woEntity = JSONObject.parseObject(woStr, PmWoEntity.class);
                mapData(model, woEntity.getId(), woEntity.getWoCode(), woEntity.getWoGroupName());
                break;
            case "PmOt":
                Object ot = ele.getData();
                String otStr = JSONObject.toJSONString(ot);
                PmOtEntity otEntity = JSONObject.parseObject(otStr, PmOtEntity.class);
                mapData(model, otEntity.getId(), "", otEntity.getOtName());
                break;
            case "PmTool":
                Object tool = ele.getData();
                String toolStr = JSONObject.toJSONString(tool);
                PmToolEntity toolEntity = JSONObject.parseObject(toolStr, PmToolEntity.class);
                mapData(model, toolEntity.getId(), toolEntity.getToolCode(), toolEntity.getToolName());
                break;
            case "PmToolJob":
                Object job = ele.getData();
                String jobStr = JSONObject.toJSONString(job);
                PmToolJobEntity jobEntity = JSONObject.parseObject(jobStr, PmToolJobEntity.class);
                mapData(model, jobEntity.getId(), jobEntity.getJobNo(), jobEntity.getJobNo());
                break;
            case "PmPullcord":
                Object cord = ele.getData();
                String cordStr = JSONObject.toJSONString(cord);
                PmPullCordEntity cordEntity = JSONObject.parseObject(cordStr, PmPullCordEntity.class);
                mapData(model, cordEntity.getId(), "", cordEntity.getPullcordName());
                break;
            default:
                break;
        }
        id = model.get("id").toString();
        //菜单节点的ID和显示值
        switch (modelType) {
            case "PmShop":
                break;
            case "PmArea":
                code = model.get("code").toString();
                text = model.get("name").toString();
                break;
            case "PmAvi":
                code = model.get("code").toString();
                text = model.get("name").toString();
                break;
            case "PmStation":
                code = model.get("code").toString();
                text = model.get("name").toString();
                break;
            case "PmWorkplace":
                code = model.get("code").toString();
                text = model.get("name").toString();
                break;
            case "PmTool":
                code = model.get("code").toString();
                text = model.get("code").toString();
                break;
            case "PmOt":
                code = model.get("name").toString();
                text = model.get("name").toString();
                break;
            case "PmPullcord":
                code = model.get("name").toString();
                text = model.get("name").toString();
                break;
            case "PmWo":
                code = model.get("code").toString();
                text = model.get("code").toString();
                break;
            case "PmToolJob":
                code = model.get("code").toString();
                text = model.get("name").toString();
                break;
            default:
                break;
        }
        //按钮样式
        switch (oper) {
            case "add":
                icon = getIcon(modelType) + " new";
                break;
            case "update":
                icon = getIcon(modelType) + " different";
                break;
            case "del":
                icon = getIcon(modelType) + " del";
                break;
            case "none":
                icon = getIcon(modelType) + " ok";
                break;
            default:
                break;
        }
        TreeNode node = new TreeNode();
        node.setId(id);
        node.setText(text);
        node.setCode(code);
        node.setIconCls(icon);
        Map<String, Object> extendData = new HashMap<>();
        extendData.put("Type", modelType);
        extendData.put("Data", model);
        extendData.put("Oper", oper);
        extendData.put("DiffFields", diffFields);
        return node;
    }

    public Map<String, Object> mapData(Map<String, Object> map, Long id, String code, String text) {
        map.put("id", id);
        map.put("code", code);
        map.put("text", text);
        return map;
    }

    /**
     * <summary>
     * 获取icon
     * </summary>
     * <param name="name"></param>
     * <returns></returns>
     **/
    private String getIcon(String name) {
        //自带缓存
        SysConfigurationEntity item = sysConfigurationService.getAllDatas().stream().filter(t -> "PMIcon".equals(t.getCategory())
                && t.getText().equals(name)).collect(Collectors.toList()).stream().findFirst().get();
        if (ObjectUtil.isNotEmpty(item)) {
            return StringUtils.isEmpty(item.getValue()) ? "" : item.getValue();
        }
        return "";
    }

    @Override
    public void setDownAndonCommplate(String code) {
        UpdateWrapper<PmVersionEntity> set = new UpdateWrapper<>();
        set.lambda().set(PmVersionEntity::getIsDownAndon, true)
                .eq(PmVersionEntity::getIsDownAndon, false)
                .eq(PmVersionEntity::getWorkshopCode, code);
        this.update(set);
    }

    @Override
    public boolean isExeistsAndonPmVersion(String code) {
        boolean blVersion = false;
        QueryWrapper<PmVersionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PmVersionEntity::getIsEnabled, true).eq(PmVersionEntity::getIsDownAndon, false).eq(PmVersionEntity::getWorkshopCode, code);
        Long count = this.selectCount(queryWrapper);
        if (count > 0) {
            blVersion = true;
        }
        return blVersion;
    }

    @Override
    public ResultVO getPreview(Long id) {
        JSONObject jsonObject = new JSONObject();
        if (id != null) {
            PmVersionEntity data = versionMapper.selectById(id);
            if (ObjectUtil.isNotEmpty(data)) {
                PmAllVo pmAllVo = JsonUtils.parseObject(data.getContent(), PmAllVo.class);
                List<PmToolJobEntity> toolJobs = pmAllVo.getToolJobs();
                List<PmToolEntity> tools = pmAllVo.getTools();
                List<PmOtEntity> ots = pmAllVo.getOts();
                List<PmPullCordEntity> pullCords = pmAllVo.getPullCords();
                List<PmWoEntity> wos = pmAllVo.getWos();
                List<PmBopEntity> bops = pmAllVo.getBops();
                List<PmWorkstationMaterialEntity> workstationMaterials = pmAllVo.getWorkstationMaterials();
                List<PmWorkStationEntity> workstations = pmAllVo.getWorkStations();
                List<PmAviEntity> avis = pmAllVo.getAvis();
                List<PmLineEntity> lines = pmAllVo.getLines();
                List<PmWorkstationOperBookEntity> workstationBooks = pmAllVo.getWorkstationOperBooks();
                List<PmEquipmentEntity> equipments = pmAllVo.getEquipments();

                PmViewVo viewVo = new PmViewVo();
                //线体
                List<PmLineEntity> finalLines = lines.stream().map(line -> {
                    //avi
                    List<PmAviEntity> finalAvis = avis.stream().filter(avi -> avi.getPrcPmLineId().equals(line.getId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(finalAvis)) {
                        line.setPmAviEntity(finalAvis);
                    }
                    //工位
                    List<PmWorkStationEntity> finalWorkstations = workstations.stream().filter(workStation -> workStation.getPrcPmLineId().equals(line.getId())).map(workstation -> {
                        //ot
                        List<PmOtEntity> finalOts = ots.stream().filter(ot -> ot.getPrcPmWorkstationId().equals(workstation.getId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(finalOts)) {
                            workstation.setPmOtEntity(finalOts);
                        }
                        //wo
                        List<PmWoEntity> finalWos = wos.stream().filter(wo -> wo.getPrcPmWorkstationId().equals(workstation.getId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(finalWos)) {
                            workstation.setPmWoEntity(finalWos);
                        }
                        //tool
                        List<PmToolEntity> finalTools = tools.stream().filter(tool -> tool.getPrcPmWorkstationId().equals(workstation.getId())).map(tool -> {
                            //tooljob
                            List<PmToolJobEntity> collect = toolJobs.stream().filter(t -> t.getPrcPmToolId().equals(tool.getId())).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(collect)) {
                                tool.setPmToolJobEntity(collect);
                            }
                            return tool;
                        }).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(finalTools)) {
                            workstation.setPmToolEntity(finalTools);
                        }
                        //pullcord
                        List<PmPullCordEntity> finalPullCords = pullCords.stream().filter(pullCord -> pullCord.getPrcPmWorkstationId().equals(workstation.getId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(finalPullCords)) {
                            workstation.setPmPullCordEntity(finalPullCords);
                        }
                        //bop
                        List<PmBopEntity> finalBops = bops.stream().filter(bop -> bop.getPrcPmWorkstationId().equals(workstation.getId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(finalBops)) {
                            workstation.setPmBopEntity(finalBops);
                        }
                        //workstationMaterial
                        List<PmWorkstationMaterialEntity> finalWorkstationMaterials = workstationMaterials.stream().filter(workstationMaterial -> workstationMaterial.getPrcPmWorkstationId().equals(workstation.getId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(finalWorkstationMaterials)) {
                            workstation.setPmWorkstationMaterialEntity(finalWorkstationMaterials);
                        }
                        //workstationOperBook
                        List<PmWorkstationOperBookEntity> finaWorkstationOperBooks = workstationBooks.stream().filter(workstationBook -> workstationBook.getPrcPmWorkstationId().equals(workstation.getId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(finaWorkstationOperBooks)) {
                            workstation.setPmWorkstationOperBookEntity(finaWorkstationOperBooks);
                        }
                        //equipment
                        List<PmEquipmentEntity> finalEquipments = equipments.stream().filter(equipment -> equipment.getPrcPmWorkstationId().equals(workstation.getId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(finalEquipments)) {
                            workstation.setPmEquipmentEntity(finalEquipments);
                        }
                        return workstation;
                    }).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(finalWorkstations)) {
                        line.setPmWorkStationEntity(finalWorkstations);
                    }
                    return line;
                }).collect(Collectors.toList());
                PmWorkShopEntity pmWorkShopEntity = pmAllVo.getWorkShops().get(0);
                pmWorkShopEntity.setPmLineEntity(finalLines);
                viewVo.setPmWorkShopEntity(pmWorkShopEntity);


//                SimplePropertyPreFilter filterField = filterField();
                String viewString = JsonUtils.toJsonString(viewVo);
                jsonObject = JSONObject.parseObject(viewString);
                jsonObject.put("PMVersion", pmAllVo.getVersion());
            }
        }
        return new ResultVO().ok(jsonObject);
    }

    private SimplePropertyPreFilter filterField() {
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("flag");
        filter.getExcludes().add("isDelete");
        filter.getExcludes().add("createdBy");
        filter.getExcludes().add("creationDate");
        filter.getExcludes().add("createdUser");
        filter.getExcludes().add("lastUpdatedUser");
        filter.getExcludes().add("lastUpdatedBy");
        filter.getExcludes().add("lastUpdateDate");
        filter.getExcludes().add("lastUpdatedTraceid");
        filter.getExcludes().add("attribute1");
        filter.getExcludes().add("attribute2");
        filter.getExcludes().add("attribute3");
        filter.getExcludes().add("attribute4");
        filter.getExcludes().add("attribute5");
        filter.getExcludes().add("attribute6");
        filter.getExcludes().add("attribute7");
        filter.getExcludes().add("attribute8");
        filter.getExcludes().add("attribute9");
        filter.getExcludes().add("attribute10");
        return filter;
    }

    /**
     * xml转json
     *
     * @param element
     * @param json
     */
    private void dom4j2Json(Element element, JSONObject json) {
        // 如果是属性
        for (Object o : element.attributes()) {
            Attribute attr = (Attribute) o;
            if (StringUtils.isNotBlank(attr.getValue())) {
                json.put(attr.getName(), attr.getValue());
            }
        }
        List<Element> chdEl = element.elements();
        if (chdEl.isEmpty() && StringUtils.isNotBlank(element.getText())) {// 如果没有子元素,只有一个值
            json.put(element.getName(), element.getText());
        }
        for (Element e : chdEl) {// 有子元素
            if (!e.elements().isEmpty()) {// 子元素也有子元素
                JSONObject chdjson = new JSONObject();
                dom4j2Json(e, chdjson);
                Object o = json.get(e.getName());
                if (o != null) {
                    JSONArray jsona = null;
                    if (o instanceof JSONObject) {// 如果此元素已存在,则转为jsonArray
                        JSONObject jsono = (JSONObject) o;
                        json.remove(e.getName());
                        jsona = new JSONArray();
                        jsona.add(jsono);
                        jsona.add(chdjson);
                    }
                    if (o instanceof JSONArray) {
                        jsona = (JSONArray) o;
                        jsona.add(chdjson);
                    }
                    json.put(e.getName(), jsona);
                } else {
                    if (!chdjson.isEmpty()) {
                        json.put(e.getName(), chdjson);
                    }
                }
            } else {// 子元素没有子元素
                for (Object o : element.attributes()) {
                    Attribute attr = (Attribute) o;
                    if (StringUtils.isNotBlank(attr.getValue())) {
                        json.put(attr.getName(), attr.getValue());
                    }
                }
                if (!e.getText().isEmpty()) {
                    json.put(e.getName(), e.getText());
                }
            }
        }
    }

    @Override
    public <T> List<T> getAllElements(Class<T> className) {
        Document xdoc = getCurretPm();
        Element root = xdoc.getRootElement();
        String name = className.getName();
        name = name.replace("Entity", "Info");
        name = name.substring(name.lastIndexOf('.') + 1);
        List<Element> elementList = new ArrayList<>();
        List<Node> nodeList = root.selectNodes("//" + name);
        for (Node node : nodeList) {
            Element element = (Element) node;
            elementList.add((Element) element.clone());
        }
        List<T> list = new ArrayList<>();
        Field[] declaredFields = className.getDeclaredFields();
        for (Element element : elementList) {
            try {
                Map<String, Object> attributeMap = getAttributes(element);
                T newInstance = className.newInstance();
                Map<Object, Object> map = new HashMap<>();
                for (Field field : declaredFields) {
                    String fileName = field.getName();
                    String caseName = fileName.toLowerCase();
                    if (attributeMap.containsKey(caseName)) {
                        map.put(fileName, attributeMap.get(caseName));
                    }
                }
                newInstance = (T) ConvertUtils.mapToObject(map, newInstance.getClass());
                list.add(newInstance);
            } catch (Exception exception) {

            }
        }
        return list;
    }

    private Map<String, Object> getAttributes(Element element) {
        Map<String, Object> maps = new HashMap<>();
        List<Attribute> attributes = element.attributes();
        for (Attribute item : attributes) {
            String name = item.getName().toLowerCase();
            String value = item.getValue();
            maps.put(name, value);
        }
        return maps;
    }

    public Document getCurretShopPm(String shopCode) {
        List<ConditionDto> conditionDtoList = new ArrayList<>();
        conditionDtoList.add(new ConditionDto("IS_ENABLED", "1", ConditionOper.Equal));
        if (StringUtils.isNotBlank(shopCode)) {
            conditionDtoList.add(new ConditionDto("WORKSHOP_CODE", shopCode, ConditionOper.Equal));
        }
        PmVersionEntity pmVersionEntity = getData(conditionDtoList).stream().findFirst().orElse(null);
        Document data;
        if (pmVersionEntity != null) {
            data = getXdoc(pmVersionEntity.getContent());
        } else {
            throw new InkelinkException("");
        }
        return data;
    }

    private PmAllVo getCurretShopPmsMapper(String shopCode) {
        List<ConditionDto> conditionDtoList = new ArrayList<>();
        conditionDtoList.add(new ConditionDto("IS_ENABLED", "1", ConditionOper.Equal));
        if (StringUtils.isNotBlank(shopCode)) {
            conditionDtoList.add(new ConditionDto("WORKSHOP_CODE", shopCode, ConditionOper.Equal));
        }
        PmVersionEntity pmVersionEntity = getData(conditionDtoList).stream().findFirst().orElse(null);
        PmAllVo data;
        if (pmVersionEntity != null) {
            data = JsonUtils.parseObject(pmVersionEntity.getContent(), PmAllVo.class);
        } else {
            throw new InkelinkException("");
        }
        return data;
    }

    public PmAllVo getCurretShopPms(String shopCode) {
        String cacheKey = PM_ALL_CACHE_NAME_VO + shopCode;
        PmAllVo data = localCache.getObject(cacheKey);
        if (data != null) {
            return data;
        }
        data = this.getCurretShopPmsMapper(shopCode);
        localCache.addObject(cacheKey, data, -1);
        return data;
    }

    public List<PmAllVo> getCurretShopPms(List<String> shopCodes) {
        List<PmAllVo> pmAllVos = new ArrayList<>(shopCodes.size());
        for (String shopCode : shopCodes) {
            pmAllVos.add(this.getCurretShopPms(shopCode));
        }

        return pmAllVos;
    }

    private Document getXdoc(String str) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(str);
        } catch (Exception exception) {
            throw new InkelinkException("未获取到xml节点");
        }
        Element root = document.getRootElement();
        List<Node> nodes = root.selectNodes("//*[@IsEnable='false']");
        //List<Node> nodes = document.selectNodes("//*[@IsEnable='false']");
        for (Node item : nodes) {
            boolean remove = item.getParent().remove(item);
            //document.remove(item);
        }
        return document;
    }

    @Override
    public void test() {
        PmVersionEntity info = this.get("3a04e018-67f7-3f59-4f5d-12a79bc6698f");
        String str = info.getContent();
        Document document = getXdoc(str);

        Element root = document.getRootElement();

        List elements = root.elements();
        System.out.println("elements的大小==>" + elements.size());
        Iterator<Element> it = root.elementIterator();
        while (it.hasNext()) {
            Element e = it.next();
            Attribute id = e.attribute("Code");
            System.out.println(id.getName() + " = ====>" + id.getStringValue());
        }

    }

    @Override
    public ResultVO<OtDto> getOtDatas(String workshopCode, String workstationType) {
        PmAllDTO pmAllDTO = getObjectedPm();
        List<PmWorkShopEntity> pmWorkShopEntityList = pmAllDTO.getShops();
        if (StringUtils.isNotBlank(workshopCode)) {
            pmWorkShopEntityList = pmWorkShopEntityList.stream()
                    .filter(item -> Objects.equals(item.getWorkshopCode(), workshopCode))
                    .sorted(Comparator.comparing(PmWorkShopEntity::getDisplayNo))
                    .collect(Collectors.toList());
        }
        List<Integer> workstationTypeList = null;
        if (StringUtils.isNotBlank(workstationType)) {
            workstationTypeList = com.ca.mfd.prc.common.utils.StringUtils.split(workstationType, new char[]{',', '|', ';'})
                    .stream().map(Integer::valueOf).collect(Collectors.toList());
        }
        List<Long> shopIdList = pmWorkShopEntityList.stream().map(PmWorkShopEntity::getId).collect(Collectors.toList());
        List<PmLineEntity> lineList = pmAllDTO.getLines().stream().filter(item -> shopIdList.contains(item.getPrcPmWorkshopId()))
                .sorted(Comparator.comparing(PmLineEntity::getLineDisplayNo)).collect(Collectors.toList());
        List<Long> lineIdList = lineList.stream().map(PmLineEntity::getId).collect(Collectors.toList());
        List<PmWorkStationEntity> stationEntityList = pmAllDTO.getStations().stream()
                .filter(item -> lineIdList.contains(item.getPrcPmLineId())).collect(Collectors.toList());
        if (workstationTypeList != null && !workstationTypeList.isEmpty()) {
            List<Integer> finalWorkstationTypeList = workstationTypeList;
            stationEntityList = stationEntityList.stream().filter(item -> finalWorkstationTypeList.contains(item.getWorkstationType()))
                    .sorted(Comparator.comparing(PmWorkStationEntity::getWorkstationCode)).collect(Collectors.toList());
        }
        List<Long> stationIdList = stationEntityList.stream().map(PmWorkStationEntity::getId).collect(Collectors.toList());
        List<PmOtEntity> otEntityList = pmAllDTO.getOts().stream().filter(item ->
                stationIdList.contains(item.getPrcPmWorkstationId())
                        && StringUtils.isNotBlank(item.getTemplate())
        ).collect(Collectors.toList());

        if (workstationTypeList != null && !workstationTypeList.isEmpty()) {
            //匹配线体
            List<Long> matchLineIds = stationEntityList.stream().map(PmWorkStationEntity::getPrcPmLineId)
                    .distinct().collect(Collectors.toList());
            lineList = lineList.stream().filter(item -> matchLineIds.contains(item.getId())).collect(Collectors.toList());

            //过滤车间
            List<Long> matchShopIds = lineList.stream().map(PmLineEntity::getPrcPmWorkshopId).distinct()
                    .collect(Collectors.toList());
            pmWorkShopEntityList = pmWorkShopEntityList.stream().filter(item -> matchShopIds.contains(item.getId())).collect(Collectors.toList());
        }

        for (PmOtEntity pmOtEntity : otEntityList) {
            pmOtEntity.setRemark(String.valueOf(pmOtEntity.getPrcPmWorkstationId()));
        }
        stationEntityList.sort(Comparator.comparing(PmWorkStationEntity::getPrcPmLineId)
                .thenComparing(PmWorkStationEntity::getWorkstationNo)
                .thenComparing(PmWorkStationEntity::getDirection));
        return new ResultVO<OtDto>().ok(new OtDto(pmWorkShopEntityList, lineList, stationEntityList, otEntityList));

    }
}