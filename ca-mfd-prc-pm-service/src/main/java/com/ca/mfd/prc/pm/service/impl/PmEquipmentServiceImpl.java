package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ClassUtil;
import com.ca.mfd.prc.common.utils.StringUtils;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmEquipmentEntity;
import com.ca.mfd.prc.pm.entity.PmEquipmentPowerEntity;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmPullCordEntity;
import com.ca.mfd.prc.pm.entity.PmWoEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationMaterialEntity;
import com.ca.mfd.prc.pm.mapper.IPmEquipmentMapper;
import com.ca.mfd.prc.pm.mapper.IPmEquipmentPowerMapper;
import com.ca.mfd.prc.pm.service.IPmEquipmentService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * @author inkelink
 * @Description: 设备服务实现
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class PmEquipmentServiceImpl extends AbstractPmCrudServiceImpl<IPmEquipmentMapper, PmEquipmentEntity> implements IPmEquipmentService {

    @Resource
    private IPmEquipmentMapper equipmentMapper;
    private static final Object lockObj = new Object();

    @Autowired
    private LocalCache localCache;
    private static final String cacheName = "PRC_PM_EQUIPMENT";

    private static final String UNDER_LINE = "_";

    @Autowired
    private IPmEquipmentPowerMapper powerMapper;


    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmEquipmentEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmEquipmentEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmEquipmentEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmEquipmentEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeUpdate(PmEquipmentEntity entity) {
        ClassUtil.validNullByNullAnnotation(entity);
    }

    @Override
    public void beforeInsert(PmEquipmentEntity entity) {
        ClassUtil.validNullByNullAnnotation(entity);
    }

    @Override
    public List<PmEquipmentEntity> getListByParentId(Long parentId) {
        QueryWrapper<PmEquipmentEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmEquipmentEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmEquipmentEntity::getPrcPmWorkstationId, parentId);
        lambdaQueryWrapper.eq(PmEquipmentEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmEquipmentEntity::getVersion, 0);
        return this.equipmentMapper.selectList(queryWrapper);
    }

    @Override
    public List<PmEquipmentEntity> getAllDatas() {
        List<PmEquipmentEntity> datas = localCache.getObject(cacheName);
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
    public List<PmEquipmentEntity> getByShopId(Long shopId) {
        return equipmentMapper.selectList(Wrappers.lambdaQuery(PmEquipmentEntity.class)
                .eq(PmEquipmentEntity::getPrcPmWorkshopId, shopId)
                .eq(PmEquipmentEntity::getVersion, 0)
                .eq(PmEquipmentEntity::getIsDelete, false));
    }

    @Override
    public Map<String, String> getExcelHead() {
        Map<String, String> pmMap = new HashMap<>(6);
        pmMap.put("id", "设备ID");
        pmMap.put("workstationCode", "工位编码");
        pmMap.put("equipmentCode", "设备编码");
        pmMap.put("equipmentName", "设备名称");
        return pmMap;
    }

    @Override
    public PmEquipmentEntity get(Serializable id, Integer version) {
        QueryWrapper<PmEquipmentEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmEquipmentEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmEquipmentEntity::getId, id);
        lambdaQueryWrapper.eq(PmEquipmentEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmEquipmentEntity::getVersion, version);
        List<PmEquipmentEntity> pmEquipmentEntityList = equipmentMapper.selectList(queryWrapper);
        return pmEquipmentEntityList.stream().findFirst().orElse(null);
    }

    @Override
    public void delete(Serializable[] ids) {
        QueryWrapper<PmEquipmentPowerEntity> qw = new QueryWrapper<>();
        qw.lambda().in(PmEquipmentPowerEntity::getPrcPmEquipmentId, ids).eq(PmEquipmentPowerEntity::getIsDelete, false);
        List<PmEquipmentPowerEntity> powers = powerMapper.selectList(qw);
        if (CollectionUtils.isNotEmpty(powers)) {
            throw new InkelinkException("设备下有能力不能删除");
        }
        super.delete(ids);
    }

    @Override
    public void importExcel(List<Map<String, String>> listFromExcelSheet,
                            Map<String, Map<String, String>> mapSysConfigByCategory,
                            String sheetName,
                            PmAllDTO currentUnDeployData) throws Exception {

        Map<String,List<Map<String,String>>> insertAndUpdateDataList = splitData(listFromExcelSheet);
        List<PmEquipmentEntity> insertEquipmentList = convertExcelDataToEntity(insertAndUpdateDataList.get("insert"),mapSysConfigByCategory, sheetName);
        List<PmEquipmentEntity> updateEquipmentList = convertExcelDataToEntity(insertAndUpdateDataList.get("updata"),mapSysConfigByCategory, sheetName);
        //设置外键
        setForeignKey(insertEquipmentList, currentUnDeployData);
        setForeignKey(updateEquipmentList, currentUnDeployData);
        //验证并保存
        //verifyAndSaveEntity(listOfPm, currentUnDeployData);
        saveEquipmentData(insertEquipmentList,updateEquipmentList);
    }

    @Override
    public List<PmEquipmentEntity> getPmEquipmentEntityByVersion(Long shopId, int version) {
        QueryWrapper<PmEquipmentEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmEquipmentEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmEquipmentEntity::getPrcPmWorkshopId, shopId);
        lambdaQueryWrapper.eq(PmEquipmentEntity::getVersion, version);
        lambdaQueryWrapper.eq(PmEquipmentEntity::getIsDelete, false);
        return equipmentMapper.selectList(queryWrapper);
    }

    @Override
    public PmEquipmentEntity getPmEquipmentEntityByCode(String equipmentCode) {
        QueryWrapper<PmEquipmentEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmEquipmentEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmEquipmentEntity::getEquipmentCode, equipmentCode);
        lambdaQueryWrapper.eq(PmEquipmentEntity::getIsDelete, false);
        return this.selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    private void setForeignKey(List<PmEquipmentEntity> listOfPmEquipment, PmAllDTO pmAllDTO) {
        if (listOfPmEquipment.isEmpty()) {
            return;
        }
        Set<String> notExistsequipmentCodeList = new HashSet<>(listOfPmEquipment.size());
        for (PmEquipmentEntity pmEquipmentEntity : listOfPmEquipment) {
            try{
                setForeignKey(pmEquipmentEntity, pmAllDTO);
            }catch (InkelinkException e){
                notExistsequipmentCodeList.add(pmEquipmentEntity.getWorkstationCode());
            }
        }
        if(!notExistsequipmentCodeList.isEmpty()){
            throw new InkelinkException("以下工位不存在["+String.join(",",notExistsequipmentCodeList)+"]");
        }
    }

    private void setForeignKey(PmEquipmentEntity pmEquipmentEntity, PmAllDTO pmAllDTO) {
        List<PmWorkStationEntity> stations = pmAllDTO.getStations();
        //设置工位id
        PmWorkStationEntity workStation = stations.stream().filter(item -> Objects.equals(pmEquipmentEntity.getWorkstationCode(), item.getWorkstationCode()))
                .findFirst().orElse(null);
        if (workStation == null) {
            throw new InkelinkException("设备名称[" + pmEquipmentEntity.getEquipmentName() + "]对应的线体编码[" + pmEquipmentEntity.getLineCode() + "]对应的工位编码[" + pmEquipmentEntity.getWorkstationCode() + "]没有对应任何工位，请检查是否有配置对应编码工位");
        }
        pmEquipmentEntity.setPrcPmWorkshopId(workStation.getPrcPmWorkshopId());
        pmEquipmentEntity.setPrcPmLineId(workStation.getPrcPmLineId());
        pmEquipmentEntity.setPrcPmWorkstationId(workStation.getId());
    }

    private void verifyAndSaveEntity(List<PmEquipmentEntity> listEntity,
                                     PmAllDTO currentUnDeployData) {
        if (listEntity == null || listEntity.isEmpty()) {
            return;
        }
        for (PmEquipmentEntity pmEquipmentEntity : listEntity) {
            ClassUtil.validNullByNullAnnotation(pmEquipmentEntity);
            PmEquipmentEntity existEquipment = currentUnDeployData.getEquipments().stream().filter(
                            item ->  Objects.equals(item.getEquipmentCode(), pmEquipmentEntity.getEquipmentCode()))
                    .findFirst().orElse(null);
            if (existEquipment != null) {
                LambdaUpdateWrapper<PmEquipmentEntity> luw = new LambdaUpdateWrapper();
                luw.set(PmEquipmentEntity::getEquipmentCode, pmEquipmentEntity.getEquipmentCode());
                luw.set(PmEquipmentEntity::getEquipmentName, pmEquipmentEntity.getEquipmentName());
                luw.set(PmEquipmentEntity::getWorkstationCode, pmEquipmentEntity.getWorkstationCode());
                luw.set(PmEquipmentEntity::getIsDelete, pmEquipmentEntity.getIsDelete());
                luw.eq(PmEquipmentEntity::getId, existEquipment.getId());
                luw.eq(PmEquipmentEntity::getVersion, 0);
                this.update(luw);
            } else if (!pmEquipmentEntity.getIsDelete()) {
                pmEquipmentEntity.setVersion(0);
                this.insert(pmEquipmentEntity);
            }
        }
    }

    private void saveEquipmentData(List<PmEquipmentEntity> insertList, List<PmEquipmentEntity> updataList){
        if(!insertList.isEmpty()){
            insertBatch(insertList, 200, false, 1);
        }
        if(!updataList.isEmpty()){
            for(PmEquipmentEntity dto : updataList){
                LambdaUpdateWrapper<PmEquipmentEntity> equipmentWrap = new LambdaUpdateWrapper();
                equipmentWrap.set(PmEquipmentEntity::getEquipmentCode, dto.getEquipmentCode())
                        .set(PmEquipmentEntity::getEquipmentName, dto.getEquipmentName())
                        .set(PmEquipmentEntity::getWorkstationCode, dto.getWorkstationCode())
                        .set(PmEquipmentEntity::getPrcPmLineId, dto.getPrcPmLineId())
                        .set(PmEquipmentEntity::getPrcPmWorkshopId, dto.getPrcPmWorkshopId())
                        .set(PmEquipmentEntity::getPrcPmWorkstationId, dto.getPrcPmWorkstationId())
                        .set(PmEquipmentEntity::getIsDelete, dto.getIsDelete())
                        .eq(PmEquipmentEntity::getId, dto.getId())
                        .eq(PmEquipmentEntity::getVersion, 0);
                update(equipmentWrap,false);
            }
        }
    }

}