package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ClassUtil;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmEquipmentEntity;
import com.ca.mfd.prc.pm.entity.PmEquipmentPowerEntity;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.mapper.IPmEquipmentMapper;
import com.ca.mfd.prc.pm.mapper.IPmEquipmentPowerMapper;
import com.ca.mfd.prc.pm.service.IPmEquipmentPowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @Description: 设备能力服务实现
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class PmEquipmentPowerServiceImpl extends AbstractPmCrudServiceImpl<IPmEquipmentPowerMapper, PmEquipmentPowerEntity> implements IPmEquipmentPowerService {
    private static final Object lockObj = new Object();

    @Resource
    private IPmEquipmentPowerMapper pmEquipmentPowerMapper;

    @Resource
    private IPmEquipmentMapper pmEquipmentMapper;

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PM_EQUIPMENT_POWER";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmEquipmentPowerEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmEquipmentPowerEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmEquipmentPowerEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmEquipmentPowerEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeUpdate(PmEquipmentPowerEntity entity) {
        checkValue(entity);
    }

    @Override
    public void beforeInsert(PmEquipmentPowerEntity entity) {
        checkValue(entity);
    }

    private void checkValue(PmEquipmentPowerEntity entity) {
        if(entity.getMinValue().compareTo(entity.getMaxValue())>0){
            throw new InkelinkException("最小值不能大于最大值");
        }
        ClassUtil.validNullByNullAnnotation(entity);
    }

    @Override
    public List<PmEquipmentPowerEntity> getAllDatas() {
        List<PmEquipmentPowerEntity> datas = localCache.getObject(cacheName);
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
    public List<PmEquipmentPowerEntity> getByShopId(Long shopId) {
        return pmEquipmentPowerMapper.selectList(Wrappers.lambdaQuery(PmEquipmentPowerEntity.class)
                .eq(PmEquipmentPowerEntity::getPrcPmWorkshopId, shopId)
                .eq(PmEquipmentPowerEntity::getVersion, 0)
                .eq(PmEquipmentPowerEntity::getIsDelete, false));
    }

    @Override
    public Map<String, String> getExcelHead() {
        Map<String, String> pmMap = new HashMap<>(11);
        pmMap.put("id", "设备ID");
        pmMap.put("workstationCode", "工位编码");
        pmMap.put("equipmentCode", "设备编码");
        pmMap.put("equipmentName", "设备名称");
        pmMap.put("powerType", "设备能力");
        pmMap.put("standardValue", "标准值");
        pmMap.put("maxValue", "最大值");
        pmMap.put("minValue", "最小值");
        pmMap.put("attribute1", "平均值");
        pmMap.put("unit", "单位");
        return pmMap;
    }

    @Override
    public PmEquipmentPowerEntity get(Serializable id, Integer version) {
        QueryWrapper<PmEquipmentPowerEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmEquipmentPowerEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmEquipmentPowerEntity::getId, id);
        lambdaQueryWrapper.eq(PmEquipmentPowerEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmEquipmentPowerEntity::getVersion, version);
        List<PmEquipmentPowerEntity> pmEquipmentPowerEntityList = pmEquipmentPowerMapper.selectList(queryWrapper);
        return pmEquipmentPowerEntityList.stream().findFirst().orElse(null);
    }

    @Override
    public void importExcel(List<Map<String, String>> listFromExcelSheet,
                            Map<String, Map<String, String>> mapSysConfigByCategory,
                            String sheetName,
                            PmAllDTO currentUnDeployData) throws Exception {
        List<PmEquipmentPowerEntity> listOfPm = this.convertExcelDataToEntity(listFromExcelSheet,
                mapSysConfigByCategory, sheetName);
        //设置外键
        setForeignKey(listOfPm, currentUnDeployData);
        //验证并保存
        verifyAndSaveEntity(listOfPm, currentUnDeployData);
    }

    @Override
    public List<PmEquipmentPowerEntity> getEquipmentPowersByShopId(Long workshopId) {
        return pmEquipmentPowerMapper.getEquipmentPowersByShopId(workshopId);
    }

    private void setForeignKey(List<PmEquipmentPowerEntity> listOfPmTool, PmAllDTO pmAllDTO) {
        if (listOfPmTool.isEmpty()) {
            return;
        }
        for (PmEquipmentPowerEntity pmEquipmentPowerEntity : listOfPmTool) {
            setForeignKey(pmEquipmentPowerEntity, pmAllDTO);
        }
    }

    private void setForeignKey(PmEquipmentPowerEntity pmEquipmentPowerEntity, PmAllDTO pmAllDTO) {

        //设置设备id
        PmEquipmentEntity equipment = getCurrEquipment(pmEquipmentPowerEntity);
        if (equipment == null) {
            throw new InkelinkException("设备名称[" + pmEquipmentPowerEntity.getEquipmentName() + "]在线体编码[" + pmEquipmentPowerEntity.getLineCode() + "]工位编码[" + pmEquipmentPowerEntity.getWorkstationCode() + "]中不存在");
        }
        pmEquipmentPowerEntity.setPrcPmWorkshopId(equipment.getPrcPmWorkshopId());
        pmEquipmentPowerEntity.setPrcPmLineId(equipment.getPrcPmLineId());
        pmEquipmentPowerEntity.setWorkstationCode(equipment.getWorkstationCode());
        pmEquipmentPowerEntity.setPrcPmWorkstationId(equipment.getPrcPmWorkstationId());
        pmEquipmentPowerEntity.setPrcPmEquipmentId(equipment.getId());
    }

    private PmEquipmentEntity getCurrEquipment(PmEquipmentPowerEntity pmEquipmentPowerEntity){
        QueryWrapper<PmEquipmentEntity> qw = new QueryWrapper<>();
        qw.lambda().eq(PmEquipmentEntity :: getEquipmentCode,pmEquipmentPowerEntity.getEquipmentCode());
        qw.lambda().eq(PmEquipmentEntity :: getIsDelete,false);
        return pmEquipmentMapper.selectList(qw).stream().findFirst().orElse(null);
    }

    private void verifyAndSaveEntity(List<PmEquipmentPowerEntity> listEntity,
                                     PmAllDTO currentUnDeployData) {
        if (listEntity == null || listEntity.isEmpty()) {
            return;
        }
        Map<Long, Set<String>> mapOfPowerTypeByEquipmentId = new HashMap(16);
        for (PmEquipmentPowerEntity pmEquipmentPowerEntity : listEntity) {
            ClassUtil.validNullByNullAnnotation(pmEquipmentPowerEntity);
            verifyPowerType(pmEquipmentPowerEntity,mapOfPowerTypeByEquipmentId);
            PmEquipmentPowerEntity existEquipmentPower = currentUnDeployData.getEquipmentPowers().stream().filter(
                            item -> Objects.equals(item.getPrcPmEquipmentId(), pmEquipmentPowerEntity.getPrcPmEquipmentId())
                                    && Objects.equals(item.getPowerType(), pmEquipmentPowerEntity.getPowerType()))
                    .findFirst().orElse(null);
            if (existEquipmentPower != null) {
                LambdaUpdateWrapper<PmEquipmentPowerEntity> luw = new LambdaUpdateWrapper();
                luw.set(PmEquipmentPowerEntity::getPrcPmWorkshopId, pmEquipmentPowerEntity.getPrcPmWorkshopId());
                luw.set(PmEquipmentPowerEntity::getPrcPmLineId, pmEquipmentPowerEntity.getPrcPmLineId());
                luw.set(PmEquipmentPowerEntity::getPrcPmWorkstationId, pmEquipmentPowerEntity.getPrcPmWorkstationId());
                luw.set(PmEquipmentPowerEntity::getPrcPmEquipmentId, pmEquipmentPowerEntity.getPrcPmEquipmentId());
                luw.set(PmEquipmentPowerEntity::getPowerType, pmEquipmentPowerEntity.getPowerType());
                luw.set(PmEquipmentPowerEntity::getMaxValue, pmEquipmentPowerEntity.getMaxValue());
                luw.set(PmEquipmentPowerEntity::getMinValue, pmEquipmentPowerEntity.getMinValue());
                luw.set(PmEquipmentPowerEntity::getStandardValue, pmEquipmentPowerEntity.getStandardValue());
                luw.set(PmEquipmentPowerEntity::getIsDelete,pmEquipmentPowerEntity.getIsDelete());
                luw.set(PmEquipmentPowerEntity::getUnit, pmEquipmentPowerEntity.getUnit());
                luw.set(PmEquipmentPowerEntity::getAttribute1, pmEquipmentPowerEntity.getAttribute1());

                luw.eq(PmEquipmentPowerEntity::getId, existEquipmentPower.getId());
                luw.eq(PmEquipmentPowerEntity::getVersion, 0);
                this.update(luw);
            } else if (!pmEquipmentPowerEntity.getIsDelete()) {
                pmEquipmentPowerEntity.setVersion(0);
                this.insert(pmEquipmentPowerEntity);
            }
        }

    }

    private void verifyPowerType(PmEquipmentPowerEntity pmEquipmentPowerEntity, Map<Long, Set<String>> mapOfPowerTypeByEquipmentId) {
        Set<String> setOfPowerTypeByEquipmentId = mapOfPowerTypeByEquipmentId.computeIfAbsent(pmEquipmentPowerEntity.getPrcPmEquipmentId(), v -> new HashSet<>());
        if (setOfPowerTypeByEquipmentId.contains(pmEquipmentPowerEntity.getPowerType())) {
            throw new InkelinkException("车间[" + pmEquipmentPowerEntity.getWorkshopCode() + "]>线体[" + pmEquipmentPowerEntity.getLineCode() + "]>工位[" + pmEquipmentPowerEntity.getWorkstationCode() + "]>设备[" + pmEquipmentPowerEntity.getEquipmentName() + "]能力类型[" + pmEquipmentPowerEntity.getPowerType() + "]重复");
        }
        setOfPowerTypeByEquipmentId.add(pmEquipmentPowerEntity.getPowerType());
    }



}