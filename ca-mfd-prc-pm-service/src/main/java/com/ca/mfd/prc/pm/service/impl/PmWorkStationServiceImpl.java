package com.ca.mfd.prc.pm.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ClassUtil;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.MoveStationDto;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.dto.VcurrentWorkStationInfo;
import com.ca.mfd.prc.pm.entity.PmBopEntity;
import com.ca.mfd.prc.pm.entity.PmEquipmentEntity;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmOtEntity;
import com.ca.mfd.prc.pm.entity.PmPullCordEntity;
import com.ca.mfd.prc.pm.entity.PmToolEntity;
import com.ca.mfd.prc.pm.entity.PmToolJobEntity;
import com.ca.mfd.prc.pm.entity.PmWoEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationMaterialEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationOperBookEntity;
import com.ca.mfd.prc.pm.mapper.IPmWorkShopMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkStationMapper;
import com.ca.mfd.prc.pm.service.IPmBopParentService;
import com.ca.mfd.prc.pm.service.IPmEquipmentPowerService;
import com.ca.mfd.prc.pm.service.IPmEquipmentService;
import com.ca.mfd.prc.pm.service.IPmOtService;
import com.ca.mfd.prc.pm.service.IPmPullCordService;
import com.ca.mfd.prc.pm.service.IPmToolJobService;
import com.ca.mfd.prc.pm.service.IPmToolService;
import com.ca.mfd.prc.pm.service.IPmWoService;
import com.ca.mfd.prc.pm.service.IPmWorkStationOperBookService;
import com.ca.mfd.prc.pm.service.IPmWorkStationService;
import com.ca.mfd.prc.pm.service.IPmWorkstationMaterialService;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ca.mfd.prc.common.constant.Constant.TRUE_BOOL;
import static com.ca.mfd.prc.common.constant.Constant.TRUE_CHINESE;
import static com.ca.mfd.prc.common.constant.Constant.TRUE_NUM;

/**
 * @author inkelink ${email}
 * @Description: 岗位
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmWorkStationServiceImpl extends AbstractPmCrudServiceImpl<IPmWorkStationMapper, PmWorkStationEntity> implements IPmWorkStationService {

    private static final Map<String, String> BOOLEAN_FIELDS_MAPPING = new HashMap<>(2);
    private static final Map<String, String> DIRECT_MAPPING = new HashMap<>(6);
    private static final String TEAM_NO = "teamNo";
    private static final Object lockObj = new Object();

    static {
        BOOLEAN_FIELDS_MAPPING.put("deleteFlag", "isDelete");
        BOOLEAN_FIELDS_MAPPING.put("enableFlag", "isEnable");

        DIRECT_MAPPING.put("1", "L");
        DIRECT_MAPPING.put("2", "R");
        DIRECT_MAPPING.put("3", "FL");
        DIRECT_MAPPING.put("4", "FR");
        DIRECT_MAPPING.put("5", "RL");
        DIRECT_MAPPING.put("6", "RR");
    }
    @Autowired
    private IPmWorkShopMapper pmShopDao;
    @Autowired
    private IPmWorkStationMapper pmStationDao;
    @Autowired
    private IPmToolService toolService;
    @Autowired
    private IPmWoService woService;
    @Autowired
    private IPmOtService otService;
    @Autowired
    private IPmToolJobService toolJobService;
    @Autowired
    private IPmPullCordService pullCordService;
    @Autowired
    private IPmBopParentService bopService;
    @Autowired
    private IPmWorkstationMaterialService pmWorkstationMaterialService;
    @Autowired
    private IPmWorkStationOperBookService workStationOperBookService;
    @Autowired
    private IPmEquipmentService pmEquipmentService;
    @Autowired
    private IPmEquipmentPowerService pmEquipmentPowerService;


    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PM_WORK_STATION";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmWorkStationEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmWorkStationEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmWorkStationEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmWorkStationEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PmWorkStationEntity> getAllDatas() {
        List<PmWorkStationEntity> datas = localCache.getObject(cacheName);
        if (datas == null || datas.isEmpty()) {
            synchronized (lockObj) {
                datas = localCache.getObject(cacheName);
                if (datas == null || datas.isEmpty()) {
                    datas = getData(null);
                    localCache.addObject(cacheName, datas);
                }
            }
        }
        return datas;
    }

    @Override
    public List<PmWorkStationEntity> getWorkStationCodeByLineId(Long lineId){
        QueryWrapper<PmWorkStationEntity> qw = new QueryWrapper();
        qw.select("WORKSTATION_CODE");
        qw.select("WORKSTATION_NAME");
        qw.eq("PRC_PM_LINE_ID",lineId);
        return this.getData(qw,false);
    }

    @Override
    public List<PmWorkStationEntity> getPmWorkStationList(Long shopId, String shopCode) {
        QueryWrapper<PmWorkStationEntity> qwStation = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkStationEntity> qlwStation = qwStation.lambda();
        if(shopId != null && shopId != 0){
            qlwStation.eq(PmWorkStationEntity :: getPrcPmWorkshopId,shopId);
        }else if(StringUtils.isNotBlank(shopCode)){
            QueryWrapper<PmWorkShopEntity> qwShop = new QueryWrapper<>();
            LambdaQueryWrapper<PmWorkShopEntity> qlwShop = qwShop.lambda();
            qlwShop.eq(PmWorkShopEntity :: getWorkshopCode,shopCode);
            qlwShop.eq(PmWorkShopEntity :: getVersion,0);
            PmWorkShopEntity pmWorkShopEntity = pmShopDao.selectList(qlwShop)
                    .stream().findFirst().orElse(null);
            if(pmWorkShopEntity == null){
                throw new InkelinkException(String.format("根据车间编码[%s]没有查询到车间信息",shopCode));
            }
            qlwStation.eq(PmWorkStationEntity :: getPrcPmWorkshopId,pmWorkShopEntity.getId());
        }
        qlwStation.eq(PmWorkStationEntity :: getVersion,0);
        qlwStation.orderByAsc(PmWorkStationEntity :: getWorkstationCode);
        return this.getData(qwStation,false);
    }

    /**
     * getCurrentWorkplaceList
     *
     * @param pageIndex
     * @param pageSize
     * @param conditions
     * @return
     */
    @Override
    public PageData<VcurrentWorkStationInfo> getCurrentWorkplaceList(int pageIndex, int pageSize, List<ConditionDto> conditions) {
        PageData<VcurrentWorkStationInfo> pageInfo = new PageData<>(new ArrayList<>(), 0);
        pageInfo.setPageIndex(pageIndex);
        pageInfo.setPageSize(pageSize);
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(2);
        List<ConditionDto> conditiondatas = MpSqlUtils.filtrationCondition(currentModelClass(), conditions, "a");
        map.put("wheresa", conditiondatas);
        Page<VcurrentWorkStationInfo> mpage = new Page<>(pageInfo.getPageIndex(), pageInfo.getPageSize());
        Page<VcurrentWorkStationInfo> pdata = pmStationDao.getCurrentWorkplaceList(mpage, map);
        pageInfo.setDatas(pdata.getRecords());
        pageInfo.setTotal((int) pdata.getTotal());
        return pageInfo;
    }

    @Override
    public ResultVO<List<ComboInfoDTO>> getPmWorkplace(String guid) {
        LambdaQueryWrapper<PmWorkStationEntity> wr = new LambdaQueryWrapper<>();
        wr.eq(PmWorkStationEntity::getPrcPmWorkshopId, guid);
        List<PmWorkStationEntity> list = pmStationDao.selectList(wr);
        List<ComboInfoDTO> arr = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(list)) {
            list.forEach(t -> {
                ComboInfoDTO dto = new ComboInfoDTO();
                dto.setText(t.getWorkstationName());
                dto.setValue(String.valueOf(t.getId()));
                arr.add(dto);
            });
        }
        return new ResultVO<List<ComboInfoDTO>>().ok(arr);
    }

    @Override
    public ResultVO<List<ComboInfoDTO>> getQualityPmWorkplaceList() {
        LambdaQueryWrapper<PmWorkStationEntity> wr = new LambdaQueryWrapper<>();
        wr.eq(PmWorkStationEntity::getWorkstationType, 2);
        wr.eq(PmWorkStationEntity::getIsDelete, Boolean.TRUE);
        List<PmWorkStationEntity> list = pmStationDao.selectList(wr);
        List<ComboInfoDTO> arr = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(list)) {
            list.forEach(t -> {
                ComboInfoDTO dto = new ComboInfoDTO();
                dto.setText(t.getWorkstationCode());
                dto.setValue(String.valueOf(t.getId()));
                arr.add(dto);
            });
        }
        return new ResultVO<List<ComboInfoDTO>>().ok(arr);
    }

    @Override
    public void update(PmWorkStationEntity pmWorkplaceEntity) {
        beforeUpdate(pmWorkplaceEntity);
        LambdaUpdateWrapper<PmWorkStationEntity> luw = new LambdaUpdateWrapper<>();
        luw.set(PmWorkStationEntity::getWorkstationCode, pmWorkplaceEntity.getWorkstationCode());
        luw.set(PmWorkStationEntity::getWorkstationName, pmWorkplaceEntity.getWorkstationName());
        luw.set(PmWorkStationEntity::getWorkstationNo, pmWorkplaceEntity.getWorkstationNo());
        luw.set(PmWorkStationEntity::getProductTime, pmWorkplaceEntity.getProductTime());
        luw.set(PmWorkStationEntity::getTeamNo, pmWorkplaceEntity.getTeamNo());
        luw.set(PmWorkStationEntity::getWorkstationType, pmWorkplaceEntity.getWorkstationType());
        luw.set(PmWorkStationEntity::getBeginDistance, pmWorkplaceEntity.getBeginDistance());
        luw.set(PmWorkStationEntity::getAlarmDistance, pmWorkplaceEntity.getAlarmDistance());
        luw.set(PmWorkStationEntity::getEndDistance, pmWorkplaceEntity.getEndDistance());
        luw.set(PmWorkStationEntity::getRoutePath, pmWorkplaceEntity.getRoutePath());
        luw.set(PmWorkStationEntity::getRouteCheck, pmWorkplaceEntity.getRouteCheck());
        luw.set(PmWorkStationEntity::getStations, pmWorkplaceEntity.getStations());
        luw.set(PmWorkStationEntity::getIsEnable, pmWorkplaceEntity.getIsEnable());
        luw.set(PmWorkStationEntity::getRemark, pmWorkplaceEntity.getRemark());
        luw.set(PmWorkStationEntity::getIsAvi, pmWorkplaceEntity.getIsAvi());
        luw.set(PmWorkStationEntity::getDirection, pmWorkplaceEntity.getDirection());

        luw.set(PmWorkStationEntity::getAttribute1, getAttribVal(pmWorkplaceEntity.getAttribute1()));
        luw.set(PmWorkStationEntity::getAttribute2, getAttribVal(pmWorkplaceEntity.getAttribute2()));
        luw.set(PmWorkStationEntity::getAttribute3, getAttribVal(pmWorkplaceEntity.getAttribute3()));
        luw.set(PmWorkStationEntity::getAttribute4, getAttribVal(pmWorkplaceEntity.getAttribute4()));
        luw.set(PmWorkStationEntity::getAttribute5, getAttribVal(pmWorkplaceEntity.getAttribute5()));

        luw.set(PmWorkStationEntity::getAttribute6, getAttribVal(pmWorkplaceEntity.getAttribute6()));
        luw.set(PmWorkStationEntity::getAttribute7, getAttribVal(pmWorkplaceEntity.getAttribute7()));
        luw.set(PmWorkStationEntity::getAttribute8, getAttribVal(pmWorkplaceEntity.getAttribute8()));
        luw.set(PmWorkStationEntity::getAttribute9, getAttribVal(pmWorkplaceEntity.getAttribute9()));
        luw.set(PmWorkStationEntity::getAttribute10, getAttribVal(pmWorkplaceEntity.getAttribute10()));

        luw.eq(PmWorkStationEntity::getId, pmWorkplaceEntity.getId());
        luw.eq(PmWorkStationEntity::getVersion, pmWorkplaceEntity.getVersion());
        luw.eq(PmWorkStationEntity::getPrcPmWorkshopId, pmWorkplaceEntity.getPrcPmWorkshopId());
        luw.eq(PmWorkStationEntity::getPrcPmLineId, pmWorkplaceEntity.getPrcPmLineId());
        this.update(luw);
    }

    private String getAttribVal(String val) {
        if (StringUtils.isBlank(val)) {
            return StringUtils.EMPTY;
        }
        return val;
    }

    @Override
    public void delete(Serializable[] ids) {

        canDeleteWorkStation(ids);

        super.delete(ids);
    }

    private void canDeleteWorkStation(Serializable[] ids) {
        QueryWrapper<PmToolEntity> qw = new QueryWrapper<>();
        qw.lambda().in(PmToolEntity::getPrcPmWorkstationId, ids);
        List<PmToolEntity> toos = this.toolService.getData(qw, false);
        if (CollectionUtils.isNotEmpty(toos)) {
            throw new InkelinkException("工位下有工具不能删除");
        }

        QueryWrapper<PmOtEntity> otw = new QueryWrapper<>();
        otw.lambda().in(PmOtEntity::getPrcPmWorkstationId, ids);
        List<PmOtEntity> otList = this.otService.getData(otw, false);
        if (CollectionUtils.isNotEmpty(otList)) {
            throw new InkelinkException("工位下有OT不能删除");
        }

        QueryWrapper<PmWoEntity> wow = new QueryWrapper<>();
        wow.lambda().in(PmWoEntity::getPrcPmWorkstationId, ids);
        List<PmWoEntity> wowList = this.woService.getData(wow, false);
        if (CollectionUtils.isNotEmpty(wowList)) {
            throw new InkelinkException("工位下有操作不能删除");
        }


        QueryWrapper<PmPullCordEntity> pcw = new QueryWrapper<>();
        pcw.lambda().in(PmPullCordEntity::getPrcPmWorkstationId, ids);
        List<PmPullCordEntity> pcwList = this.pullCordService.getData(pcw, false);
        if (CollectionUtils.isNotEmpty(pcwList)) {
            throw new InkelinkException("工位下有安灯不能删除");
        }

        QueryWrapper<PmWorkstationMaterialEntity> wmw = new QueryWrapper<>();
        wmw.lambda().in(PmWorkstationMaterialEntity::getPrcPmWorkstationId, ids);
        List<PmWorkstationMaterialEntity> wmwList = this.pmWorkstationMaterialService.getData(wmw, false);
        if (CollectionUtils.isNotEmpty(wmwList)) {
            throw new InkelinkException("工位下有物料清单不能删除");
        }


        QueryWrapper<PmBopEntity> bopw = new QueryWrapper<>();
        bopw.lambda().in(PmBopEntity::getPrcPmWorkstationId, ids);
        List<PmBopEntity> bopwList = this.bopService.getData(bopw, false);
        if (CollectionUtils.isNotEmpty(bopwList)) {
            throw new InkelinkException("工位下有操作指导不能删除");
        }

        QueryWrapper<PmWorkstationOperBookEntity> operw = new QueryWrapper<>();
        operw.lambda().in(PmWorkstationOperBookEntity::getPrcPmWorkstationId, ids);
        List<PmWorkstationOperBookEntity> operwList = this.workStationOperBookService.getData(operw, false);
        if (CollectionUtils.isNotEmpty(operwList)) {
            throw new InkelinkException("工位下有操作指导书不能删除");
        }

        QueryWrapper<PmEquipmentEntity> eqqw = new QueryWrapper<>();
        eqqw.lambda().in(PmEquipmentEntity::getPrcPmWorkstationId, ids);
        List<PmEquipmentEntity> equipments = this.pmEquipmentService.getData(eqqw, false);
        if (CollectionUtils.isNotEmpty(equipments)) {
            throw new InkelinkException("工位下有设备不能删除");
        }
    }

    @Override
    public List<PmWorkStationEntity> getByShopId(Long shopId) {
        return pmStationDao.selectList(Wrappers.lambdaQuery(PmWorkStationEntity.class)
                .eq(PmWorkStationEntity::getPrcPmWorkshopId, shopId)
                .eq(PmWorkStationEntity::getVersion, 0)
                .eq(PmWorkStationEntity::getIsDelete, false)
                .orderByAsc(PmWorkStationEntity::getWorkstationNo)
                .orderByAsc(PmWorkStationEntity::getDirection));
    }

    @Override
    public Map<String, String> getExcelHead() {
        Map<String, String> pmWorkStationMap = new HashMap<>(9);
        pmWorkStationMap.put("workshopCode", "区域代码");
        pmWorkStationMap.put("lineCode", "线体代码");
        pmWorkStationMap.put("workstationCode", "工位代码");
        pmWorkStationMap.put("workstationName", "工位名称");
        pmWorkStationMap.put("workstationNo", "工位号");
        pmWorkStationMap.put("direction", "方位");
        pmWorkStationMap.put("teamNo", "班组");
        pmWorkStationMap.put("beginDistance", "工位开始");
        pmWorkStationMap.put("endDistance", "工位结束");
        pmWorkStationMap.put("alarmDistance", "工位预警距离");
        pmWorkStationMap.put("productTime", "工位生产L/T(S)");
        pmWorkStationMap.put("workstationType", "工位类型");
        pmWorkStationMap.put("remark", "备注");
        pmWorkStationMap.put("enableFlag", "工位是否启用");
        pmWorkStationMap.put("deleteFlag", "工位是否删除");
        return pmWorkStationMap;
    }

    @Override
    public List<PmWorkStationEntity> getListByLineId(Long lineId) {
        QueryWrapper<PmWorkStationEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkStationEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWorkStationEntity::getPrcPmLineId, lineId);
        lambdaQueryWrapper.eq(PmWorkStationEntity::getVersion, 0);
        lambdaQueryWrapper.eq(PmWorkStationEntity::getIsDelete, false);
        lambdaQueryWrapper.orderByAsc(PmWorkStationEntity::getWorkstationNo);
        lambdaQueryWrapper.orderByAsc(PmWorkStationEntity::getDirection);
        return pmStationDao.selectList(queryWrapper);
    }

    @Override
    public List<PmWorkStationEntity> getPmStationEntityByVersion(Long shopId, int version, Boolean flags) {
        QueryWrapper<PmWorkStationEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkStationEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWorkStationEntity::getPrcPmWorkshopId, shopId);
        lambdaQueryWrapper.eq(PmWorkStationEntity::getVersion, version);
        lambdaQueryWrapper.eq(PmWorkStationEntity::getIsEnable, true);
        lambdaQueryWrapper.eq(PmWorkStationEntity::getIsDelete, flags);
        return pmStationDao.selectList(queryWrapper);
    }

    @Override
    public List<PmWorkStationEntity> getPmStationEntityByAll(Long shopId, int i, Boolean aFalse) {
        QueryWrapper<PmWorkStationEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkStationEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWorkStationEntity::getPrcPmWorkshopId, shopId);
        lambdaQueryWrapper.eq(PmWorkStationEntity::getVersion, i);
        lambdaQueryWrapper.eq(PmWorkStationEntity::getIsDelete, aFalse);
        return pmStationDao.selectList(queryWrapper);
    }

    @Override
    public ResultVO moveWorkStation(MoveStationDto dto) {
        QueryWrapper<PmWorkStationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PmWorkStationEntity::getPrcPmLineId, dto.getOldLineId()).eq(PmWorkStationEntity::getPrcPmWorkshopId, dto.getOldShopId()).eq(PmWorkStationEntity::getId, dto.getOldStationId());
        PmWorkStationEntity entity = this.getData(queryWrapper, false).stream().findFirst().orElse(null);
        if (entity == null) {
            throw new InkelinkException("当前工位不存在！");
        }
        QueryWrapper<PmWorkStationEntity> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().eq(PmWorkStationEntity::getPrcPmLineId, dto.getNewLineId()).eq(PmWorkStationEntity::getPrcPmWorkshopId, dto.getNewShopId()).eq(PmWorkStationEntity::getId, dto.getNewStationId());
        PmWorkStationEntity entity1 = this.getData(queryWrapper1, false).stream().findFirst().orElse(null);
        if (entity1 == null) {
            throw new InkelinkException("目标工位不存在！");
        }
        //更新工位移动数据
        updateStation(dto);
        return new ResultVO().ok("", "移动成功");
    }

    private void updateStation(MoveStationDto dto) {
        UpdateWrapper<PmOtEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(PmOtEntity::getPrcPmWorkshopId, dto.getNewShopId()).set(PmOtEntity::getPrcPmLineId, dto.getNewLineId()).set(PmOtEntity::getPrcPmWorkstationId, dto.getNewStationId())
                .eq(PmOtEntity::getPrcPmWorkshopId, dto.getOldShopId()).eq(PmOtEntity::getPrcPmLineId, dto.getOldLineId()).eq(PmOtEntity::getPrcPmWorkstationId, dto.getOldStationId())
                .eq(PmOtEntity::getIsDelete, false);
        otService.update(wrapper);
        UpdateWrapper<PmPullCordEntity> pullWrapper = new UpdateWrapper<>();
        pullWrapper.lambda().set(PmPullCordEntity::getPrcPmWorkshopId, dto.getNewShopId()).set(PmPullCordEntity::getPrcPmLineId, dto.getNewLineId()).set(PmPullCordEntity::getPrcPmWorkstationId, dto.getNewStationId())
                .eq(PmPullCordEntity::getPrcPmWorkshopId, dto.getOldShopId()).eq(PmPullCordEntity::getPrcPmLineId, dto.getOldLineId()).eq(PmPullCordEntity::getPrcPmWorkstationId, dto.getOldStationId())
                .eq(PmPullCordEntity::getIsDelete, false);
        pullCordService.update(pullWrapper);
        QueryWrapper<PmToolEntity> toolWrap = new QueryWrapper<>();
        toolWrap.lambda().eq(PmToolEntity::getPrcPmLineId, dto.getOldLineId()).eq(PmToolEntity::getPrcPmWorkshopId, dto.getOldShopId()).eq(PmToolEntity::getPrcPmWorkstationId, dto.getOldStationId());
        PmToolEntity toolEntity = toolService.getData(toolWrap, false).stream().findFirst().orElse(null);
        Long toolId = Constant.DEFAULT_ID;
        if (toolEntity != null) {
            toolId = toolEntity.getId();
        }
        Long woId = Constant.DEFAULT_ID;
        QueryWrapper<PmWoEntity> woWrap = new QueryWrapper<>();
        woWrap.lambda().eq(PmWoEntity::getPrcPmLineId, dto.getOldLineId()).eq(PmWoEntity::getPrcPmWorkshopId, dto.getOldShopId()).eq(PmWoEntity::getPrcPmWorkstationId, dto.getOldStationId());
        PmWoEntity woEntity = woService.getData(woWrap, false).stream().findFirst().orElse(null);
        if (woEntity != null) {
            woId = woEntity.getId();
        }
        UpdateWrapper<PmToolJobEntity> toolJobWrapper = new UpdateWrapper<>();
        toolJobWrapper.lambda().set(PmToolJobEntity::getPrcPmWorkshopId, dto.getNewShopId()).set(PmToolJobEntity::getPrcPmLineId, dto.getNewLineId())
                .eq(PmToolJobEntity::getPrcPmWorkshopId, dto.getOldShopId()).eq(PmToolJobEntity::getPrcPmLineId, dto.getOldLineId())
                .eq(PmToolJobEntity::getPrcPmToolId, toolId).eq(PmToolJobEntity::getPmWoId, woId)
                .eq(PmToolJobEntity::getIsDelete, false);
        toolJobService.update(toolJobWrapper);
        UpdateWrapper<PmWoEntity> woWrapper = new UpdateWrapper<>();
        woWrapper.lambda().set(PmWoEntity::getPrcPmWorkshopId, dto.getNewShopId()).set(PmWoEntity::getPrcPmLineId, dto.getNewLineId()).set(PmWoEntity::getPrcPmWorkstationId, dto.getNewStationId())
                .eq(PmWoEntity::getPrcPmWorkshopId, dto.getOldShopId()).eq(PmWoEntity::getPrcPmLineId, dto.getOldLineId()).eq(PmWoEntity::getPrcPmWorkstationId, dto.getOldStationId())
                .eq(PmWoEntity::getIsDelete, false);
        woService.update(woWrapper);
        UpdateWrapper<PmToolEntity> toolWrapper = new UpdateWrapper<>();
        toolWrapper.lambda().set(PmToolEntity::getPrcPmWorkshopId, dto.getNewShopId()).set(PmToolEntity::getPrcPmLineId, dto.getNewLineId()).set(PmToolEntity::getPrcPmWorkstationId, dto.getNewStationId())
                .eq(PmToolEntity::getPrcPmWorkshopId, dto.getOldShopId()).eq(PmToolEntity::getPrcPmLineId, dto.getOldLineId()).eq(PmToolEntity::getPrcPmWorkstationId, dto.getOldStationId())
                .eq(PmToolEntity::getIsDelete, false);
        toolService.update(toolWrapper);
        UpdateWrapper<PmBopEntity> bopWrapper = new UpdateWrapper<>();
        bopWrapper.lambda().set(PmBopEntity::getPrcPmWorkshopId, dto.getNewShopId()).set(PmBopEntity::getPrcPmLineId, dto.getNewLineId()).set(PmBopEntity::getPrcPmWorkstationId, dto.getNewStationId())
                .eq(PmBopEntity::getPrcPmWorkshopId, dto.getOldShopId()).eq(PmBopEntity::getPrcPmLineId, dto.getOldLineId()).eq(PmBopEntity::getPrcPmWorkstationId, dto.getOldStationId())
                .eq(PmBopEntity::getIsDelete, false);
        bopService.update(bopWrapper);
        UpdateWrapper<PmWorkstationMaterialEntity> wmWrapper = new UpdateWrapper<>();
        wmWrapper.lambda().set(PmWorkstationMaterialEntity::getPrcPmWorkshopId, dto.getNewShopId()).set(PmWorkstationMaterialEntity::getPrcPmLineId, dto.getNewLineId()).set(PmWorkstationMaterialEntity::getPrcPmWorkstationId, dto.getNewStationId())
                .eq(PmWorkstationMaterialEntity::getPrcPmWorkshopId, dto.getOldShopId()).eq(PmWorkstationMaterialEntity::getPrcPmLineId, dto.getOldLineId()).eq(PmWorkstationMaterialEntity::getPrcPmWorkstationId, dto.getOldStationId())
                .eq(PmWorkstationMaterialEntity::getIsDelete, false);
        pmWorkstationMaterialService.update(wmWrapper);
        UpdateWrapper<PmWorkstationOperBookEntity> bookWrapper = new UpdateWrapper<>();
        bookWrapper.lambda().set(PmWorkstationOperBookEntity::getPrcPmWorkshopId, dto.getNewShopId()).set(PmWorkstationOperBookEntity::getPrcPmLineId, dto.getNewLineId()).set(PmWorkstationOperBookEntity::getPrcPmWorkstationId, dto.getNewStationId())
                .eq(PmWorkstationOperBookEntity::getPrcPmWorkshopId, dto.getOldShopId()).eq(PmWorkstationOperBookEntity::getPrcPmLineId, dto.getOldLineId()).eq(PmWorkstationOperBookEntity::getPrcPmWorkstationId, dto.getOldStationId())
                .eq(PmWorkstationOperBookEntity::getIsDelete, false);
        workStationOperBookService.update(bookWrapper);
    }

    @Override
    public PmWorkStationEntity get(Serializable id) {
        QueryWrapper<PmWorkStationEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkStationEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWorkStationEntity::getId, id);
        lambdaQueryWrapper.eq(PmWorkStationEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmWorkStationEntity::getVersion, 0);
        List<PmWorkStationEntity> pmWorkStationEntityList = pmStationDao.selectList(queryWrapper);
        return pmWorkStationEntityList.stream().findFirst().orElse(null);
    }

    @Override
    public void beforeInsert(PmWorkStationEntity entity) {
        valid(entity);
    }

    @Override
    public void beforeUpdate(PmWorkStationEntity entity) {
        valid(entity);
    }

    private void valid(PmWorkStationEntity entity) {
        QueryWrapper<PmWorkStationEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkStationEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWorkStationEntity::getWorkstationCode, entity.getWorkstationCode());
        lambdaQueryWrapper.eq(PmWorkStationEntity::getVersion, entity.getVersion());
        //lambdaQueryWrapper.eq(PmWorkStationEntity::getPrcPmWorkshopId, entity.getPrcPmWorkshopId());
        lambdaQueryWrapper.ne(PmWorkStationEntity::getId, entity.getId());

        if (selectCount(queryWrapper) > 0) {
            throw new InkelinkException("代码" + entity.getWorkstationCode() + "已存在。工位代码全厂唯一");
        }
        lambdaQueryWrapper.clear();
        lambdaQueryWrapper.eq(PmWorkStationEntity::getPrcPmLineId, entity.getPrcPmLineId());
        lambdaQueryWrapper.eq(PmWorkStationEntity::getVersion, entity.getVersion());
        lambdaQueryWrapper.eq(PmWorkStationEntity::getWorkstationNo, entity.getWorkstationNo());
        lambdaQueryWrapper.eq(PmWorkStationEntity::getDirection, entity.getDirection());
        lambdaQueryWrapper.ne(PmWorkStationEntity::getId, entity.getId());
        if (selectCount(queryWrapper) > 0) {
            throw new InkelinkException(String.format("系统中已经存在，工位代码是'%s',工位号'%s'是方位是'%s'的数据", entity.getWorkstationCode(), entity.getWorkstationNo(), DIRECT_MAPPING.get(String.valueOf(entity.getDirection()))));
        }

    }

    @Override
    public void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) throws Exception {
        List<PmWorkStationEntity> listOfPmWorkStation = this.convertExcelDataToEntity(listFromExcelSheet,
                mapSysConfigByCategory, sheetName);
        //设置外键
        setForeignKey(listOfPmWorkStation, currentUnDeployData);
        //验证并保存
        verifyAndSaveEntity(listOfPmWorkStation, currentUnDeployData);
    }


    @Override
    public void canDeleteLine(Serializable[] lineIds) {
        QueryWrapper<PmWorkStationEntity> qry = new QueryWrapper<>();
        qry.lambda().in(PmWorkStationEntity::getPrcPmLineId, lineIds);
        List<PmWorkStationEntity> workstationList = getData(qry, false);
        if (CollectionUtils.isNotEmpty(workstationList)) {
            PmWorkStationEntity workStationEntity = workstationList.stream().findFirst().orElse(null);
            if (Objects.nonNull(workStationEntity)) {
                throw new InkelinkException("线体下有工位，不允许删除!");
            }
        }
    }

    @Override
    public ResultVO<Boolean> checkCanDelete(Long prcWorkstationId) {
        boolean canDelete = false;
        ResultVO<Boolean> ret = new ResultVO<>();
        QueryWrapper<PmToolEntity> qw = new QueryWrapper<>();
        qw.lambda().eq(PmToolEntity::getPrcPmWorkstationId, prcWorkstationId);
        List<PmToolEntity> toos = this.toolService.getData(qw, false);
        if (CollectionUtils.isNotEmpty(toos)) {
            return ret.ok(canDelete,"存在未删除的工具");
        }

        QueryWrapper<PmOtEntity> otw = new QueryWrapper<>();
        otw.lambda().eq(PmOtEntity::getPrcPmWorkstationId, prcWorkstationId);
        List<PmOtEntity> otList = this.otService.getData(otw, false);
        if (CollectionUtils.isNotEmpty(otList)) {
            return ret.ok(canDelete,"存在未删除的ot");
        }

        QueryWrapper<PmWoEntity> wow = new QueryWrapper<>();
        wow.lambda().eq(PmWoEntity::getPrcPmWorkstationId, prcWorkstationId);
        List<PmWoEntity> wowList = this.woService.getData(wow, false);
        if (CollectionUtils.isNotEmpty(wowList)) {
            return ret.ok(canDelete,"存在未删除的工艺");
        }


        QueryWrapper<PmPullCordEntity> pcw = new QueryWrapper<>();
        pcw.lambda().eq(PmPullCordEntity::getPrcPmWorkstationId, prcWorkstationId);
        List<PmPullCordEntity> pcwList = this.pullCordService.getData(pcw, false);
        if (CollectionUtils.isNotEmpty(pcwList)) {
            return ret.ok(canDelete,"存在未删除的安灯");
        }

        QueryWrapper<PmWorkstationMaterialEntity> wmw = new QueryWrapper<>();
        wmw.lambda().eq(PmWorkstationMaterialEntity::getPrcPmWorkstationId, prcWorkstationId);
        List<PmWorkstationMaterialEntity> wmwList = this.pmWorkstationMaterialService.getData(wmw, false);
        if (CollectionUtils.isNotEmpty(wmwList)) {
            return ret.ok(canDelete,"存在未删除的工位物料");
        }


        QueryWrapper<PmBopEntity> bopw = new QueryWrapper<>();
        bopw.lambda().eq(PmBopEntity::getPrcPmWorkstationId, prcWorkstationId);
        List<PmBopEntity> bopwList = this.bopService.getData(bopw, false);
        if (CollectionUtils.isNotEmpty(bopwList)) {
            return ret.ok(canDelete,"存在未删除的bop");
        }

        QueryWrapper<PmWorkstationOperBookEntity> operw = new QueryWrapper<>();
        operw.lambda().eq(PmWorkstationOperBookEntity::getPrcPmWorkstationId, prcWorkstationId);
        List<PmWorkstationOperBookEntity> operwList = this.workStationOperBookService.getData(operw, false);
        if (CollectionUtils.isNotEmpty(operwList)) {
            return ret.ok(canDelete,"存在未删除的指导书");
        }

        QueryWrapper<PmEquipmentEntity> eqqw = new QueryWrapper<>();
        eqqw.lambda().eq(PmEquipmentEntity::getPrcPmWorkstationId, prcWorkstationId);
        List<PmEquipmentEntity> equipments = this.pmEquipmentService.getData(eqqw, false);
        if (CollectionUtils.isNotEmpty(equipments)) {
            return ret.ok(canDelete,"存在未删除的设备");
        }
        return ret.ok(true);
    }

    @Override
    protected boolean canBeNullOrEmpty(Map.Entry<String, String> eachColumnData) {
        //班长配置非必填配置可以为空
        return !TEAM_NO.equalsIgnoreCase(eachColumnData.getKey())
                || !StringUtils.isBlank(eachColumnData.getValue());
    }

    @Override
    protected void setBooleanVal(Map<String, String> eachRowData) {
        Set<String> booleanKeySet = BOOLEAN_FIELDS_MAPPING.keySet();
        Set<String> allKeySet = eachRowData.keySet();
        Map<String, String> appendMap = new HashMap<>(3);
        for (String eachField : allKeySet) {
            if (booleanKeySet.contains(eachField)) {
                boolean flag = TRUE_BOOL.equalsIgnoreCase(eachRowData.get(eachField))
                        || TRUE_NUM.equals(eachRowData.get(eachField))
                        || TRUE_CHINESE.equals(eachRowData.get(eachField));
                appendMap.put(BOOLEAN_FIELDS_MAPPING.get(eachField), String.valueOf(flag));
            }
        }
        eachRowData.putAll(appendMap);
    }

    private void setForeignKey(List<PmWorkStationEntity> listOfPmWorkStation, PmAllDTO pmAllDTO) {
        if (listOfPmWorkStation.isEmpty()) {
            return;
        }
        for (PmWorkStationEntity pmWorkStationEntity : listOfPmWorkStation) {
            setForeignKey(pmWorkStationEntity, pmAllDTO);
        }
    }

    private void setForeignKey(PmWorkStationEntity pmWorkStationEntity, PmAllDTO pmAllDTO) {
        List<PmWorkShopEntity> shops = pmAllDTO.getShops();
        if (shops.isEmpty()) {
            throw new InkelinkException("没有任何车间请添加车间信息");
        }
        //设置车间id
        PmWorkShopEntity shop = shops.stream().filter(item -> Objects.equals(pmWorkStationEntity.getWorkshopCode(), item.getWorkshopCode()))
                .findFirst().orElse(null);
        if (shop == null) {
            throw new InkelinkException("工位编码[" + pmWorkStationEntity.getWorkstationCode() + "]对应的车间编码[" + pmWorkStationEntity.getWorkshopCode() + "]没有对应任何车间，请检查是否有配置对应编码车间");
        }
        pmWorkStationEntity.setPrcPmWorkshopId(shop.getId());
        List<PmLineEntity> lines = pmAllDTO.getLines();
        if (lines.isEmpty()) {
            throw new InkelinkException("没有任何线体请添加线体信息");
        }
        //设置线体id
        PmLineEntity line = lines.stream().filter(item -> Objects.equals(pmWorkStationEntity.getLineCode(), item.getLineCode()))
                .findFirst().orElse(null);
        if (line == null) {
            throw new InkelinkException("工位编码[" + pmWorkStationEntity.getWorkstationCode() + "]对应的线体编码[" + pmWorkStationEntity.getLineCode() + "]没有对应任何线体，请检查是否有配置对应编码线体");
        }
        pmWorkStationEntity.setPrcPmLineId(line.getId());
    }

    private Map<Long, PmWorkShopEntity> getWorkShopById(List<PmWorkShopEntity> listOfShop) {
        return listOfShop.stream().collect(Collectors.toMap(PmWorkShopEntity::getId, v -> v));
    }

    private Map<Long, PmLineEntity> getLineById(List<PmLineEntity> listOfLine) {
        return listOfLine.stream().collect(Collectors.toMap(PmLineEntity::getId, v -> v));
    }

    private void verifyAndSaveEntity(List<PmWorkStationEntity> listEntity,
                                     PmAllDTO currentUnDeployData) {
        if (listEntity == null || listEntity.isEmpty()) {
            return;
        }
        List<PmWorkStationEntity> insertList = new ArrayList<>(listEntity.size());
        Map<Long, Set<String>> mapOfWorkStationCodeByShopId = new HashMap(16);
        Map<String, Set<Integer>> mapOfStationDirectionByLineIdAndStationNo = new HashMap(16);
        for (PmWorkStationEntity workStation : listEntity) {
            if(workStation.getIsDelete()){
                LambdaUpdateWrapper<PmWorkStationEntity> luw = new LambdaUpdateWrapper();
                luw.set(PmWorkStationEntity::getIsDelete, true);
                luw.eq(PmWorkStationEntity::getWorkstationCode, workStation.getWorkstationCode());
                luw.eq(PmWorkStationEntity::getVersion, 0);
                this.update(luw,false);
                continue;
            }
            ClassUtil.validNullByNullAnnotation(workStation);
            //验证编码
            verifyWorkStationCode(workStation, mapOfWorkStationCodeByShopId);
            //验证工位号和方位
            verifyStationDirection(workStation, mapOfStationDirectionByLineIdAndStationNo);

            PmWorkStationEntity existWorkStation = currentUnDeployData.getStations().stream().filter(
                    item -> Objects.equals(item.getWorkstationCode(), workStation.getWorkstationCode())).findFirst().orElse(null);
            if (existWorkStation != null) {
                PmWorkStationEntity existSameStationCodeWorkStation = currentUnDeployData.getStations().stream().filter(
                        item -> Objects.equals(item.getWorkstationCode(), workStation.getWorkstationCode())
                                && !Objects.equals(item.getId(), existWorkStation.getId())).findFirst().orElse(null);
                if (existSameStationCodeWorkStation != null) {
                    throw new InkelinkException("工位[" + workStation.getWorkstationCode() + "]已经存在");
                }
                PmWorkStationEntity existSameStationNoDirection = currentUnDeployData.getStations().stream().filter(
                        item -> Objects.equals(item.getPrcPmLineId(), workStation.getPrcPmLineId())
                                && Objects.equals(item.getWorkstationNo(), workStation.getWorkstationNo())
                                && Objects.equals(item.getDirection(), workStation.getDirection())
                                && !Objects.equals(item.getId(), existWorkStation.getId())).findFirst().orElse(null);
                if (existSameStationNoDirection != null) {
                    throw new InkelinkException("车间[" + workStation.getWorkshopCode() + "]线体[" + workStation.getLineCode() + "]工位号[" + workStation.getWorkstationNo() + "]方位编号[" + DIRECT_MAPPING.get(String.valueOf(workStation.getDirection())) + "]已存在");
                }

                LambdaUpdateWrapper<PmWorkStationEntity> luw = new LambdaUpdateWrapper();

                luw.set(PmWorkStationEntity::getBeginDistance, workStation.getBeginDistance());
                luw.set(PmWorkStationEntity::getEndDistance, workStation.getEndDistance());
                luw.set(PmWorkStationEntity::getAlarmDistance, workStation.getAlarmDistance());
                luw.set(PmWorkStationEntity::getProductTime, workStation.getProductTime());
                luw.set(PmWorkStationEntity::getWorkstationType, workStation.getWorkstationType());
                luw.set(PmWorkStationEntity::getRemark, workStation.getRemark());

                luw.set(PmWorkStationEntity::getWorkstationNo, workStation.getWorkstationNo());
                luw.set(PmWorkStationEntity::getWorkstationName, workStation.getWorkstationName());
                luw.set(PmWorkStationEntity::getDirection, workStation.getDirection());
                luw.set(PmWorkStationEntity::getTeamNo, workStation.getTeamNo());
                luw.set(PmWorkStationEntity::getIsDelete, workStation.getIsDelete());
                luw.set(PmWorkStationEntity::getIsEnable, workStation.getIsEnable());

                luw.eq(PmWorkStationEntity::getId, existWorkStation.getId());
                luw.eq(PmWorkStationEntity::getVersion, 0);
                this.update(luw,false);
            } else {
                PmWorkStationEntity existSameStationNoDirection = currentUnDeployData.getStations().stream().filter(
                                item -> Objects.equals(item.getPrcPmLineId(), workStation.getPrcPmLineId())
                                        && Objects.equals(item.getWorkstationNo(), workStation.getWorkstationNo())
                                        && Objects.equals(item.getDirection(), workStation.getDirection()))
                        .findFirst().orElse(null);
                if (existSameStationNoDirection != null) {
                    throw new InkelinkException("车间[" + workStation.getWorkshopCode() + "]线体[" + workStation.getLineCode() + "]工位号[" + workStation.getWorkstationNo() + "]方位编号[" + DIRECT_MAPPING.get(String.valueOf(workStation.getDirection())) + "]已存在");
                }
                workStation.setVersion(0);
                insertList.add(workStation);
                //this.insert(workStation,false);
            }
        }
        if(!insertList.isEmpty()){
            this.insertBatch(insertList,200,false,1);
        }

    }

    private void verifyWorkStationCode(PmWorkStationEntity workStation, Map<Long, Set<String>> mapOfWorkStationCodeByShopId) {
        Set<String> setOfWorkStationCode = mapOfWorkStationCodeByShopId.computeIfAbsent(workStation.getPrcPmWorkshopId(), v -> new HashSet<>());
        if (setOfWorkStationCode.contains(workStation.getWorkstationCode())) {
            throw new InkelinkException("车间[" + workStation.getWorkshopCode() + "]线体[" + workStation.getLineCode() + "]工位代码[" + workStation.getWorkstationCode() + "]重复");
        }
        setOfWorkStationCode.add(workStation.getWorkstationCode());
    }

    private void verifyWorkStationName(PmWorkStationEntity workStation, Map<Long, Set<String>> mapOfWorkStationCodeByShopId) {
        Set<String> setOfWorkStationName = mapOfWorkStationCodeByShopId.computeIfAbsent(workStation.getPrcPmWorkshopId(), v -> new HashSet<>());
        if (setOfWorkStationName.contains(workStation.getWorkstationName())) {
            throw new InkelinkException("车间[" + workStation.getWorkshopCode() + "]线体[" + workStation.getLineCode() + "]工位[" + workStation.getWorkstationCode() + "]对应名称重复");
        }
        setOfWorkStationName.add(workStation.getWorkstationName());
    }

    /**
     * 验证方位每个工位号下最多只能6个岗位，因为只有6个方位且不能重复
     *
     * @param workStation
     * @param mapOfStationDirectionByLineIdAndStationNo
     */
    private void verifyStationDirection(PmWorkStationEntity workStation, Map<String, Set<Integer>> mapOfStationDirectionByLineIdAndStationNo) {
        Set<Integer> setOfStationDirection = mapOfStationDirectionByLineIdAndStationNo.computeIfAbsent(workStation.getPrcPmLineId() + "-" + workStation.getWorkstationNo(), v -> new HashSet<>());
        if (!workStation.getIsDelete() && setOfStationDirection.size() >= 6) {
            throw new InkelinkException("车间[" + workStation.getWorkshopCode() + "]线体[" + workStation.getLineCode() + "]工位号[" + workStation.getWorkstationNo() + "]方位已达到6个，不能再添加");
        }
        if (setOfStationDirection.contains(workStation.getDirection())) {
            throw new InkelinkException("车间[" + workStation.getWorkshopCode() + "]线体[" + workStation.getLineCode() + "]工位号[" + workStation.getWorkstationNo() + "]对应方位重复");
        }
        if (!workStation.getIsDelete()) {
            setOfStationDirection.add(workStation.getDirection());
        }
    }
}