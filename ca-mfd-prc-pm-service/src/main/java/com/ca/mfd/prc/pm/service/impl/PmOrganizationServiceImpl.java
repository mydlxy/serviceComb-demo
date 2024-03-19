package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.pm.communication.entity.MidPmCountryEntity;
import com.ca.mfd.prc.pm.communication.service.IMidPmCountryService;
import com.ca.mfd.prc.pm.entity.PmOrganizationEntity;
import com.ca.mfd.prc.pm.mapper.IPmOrganizationMapper;
import com.ca.mfd.prc.pm.service.IPmOrganizationService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 工厂服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class PmOrganizationServiceImpl extends AbstractCrudServiceImpl<IPmOrganizationMapper, PmOrganizationEntity> implements IPmOrganizationService {

    private static final Object lockObj = new Object();
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PM_ORGANIZATION";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmOrganizationEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmOrganizationEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmOrganizationEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmOrganizationEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PmOrganizationEntity> getAllDatas() {
        List<PmOrganizationEntity> datas = localCache.getObject(cacheName);
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

    @Resource
    IPmOrganizationMapper pmOrganizationMapper;

    @Autowired
    IMidPmCountryService countryService;

    @Override
    public PmOrganizationEntity getPmOrganization(Long id, Boolean aFalse) {
        QueryWrapper<PmOrganizationEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmOrganizationEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmOrganizationEntity::getId, id);
        lambdaQueryWrapper.eq(PmOrganizationEntity::getIsDelete, aFalse);
        return pmOrganizationMapper.selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public String getCurrentOrgCode() {
        QueryWrapper<PmOrganizationEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmOrganizationEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmOrganizationEntity::getIsDelete, false);
        List<PmOrganizationEntity> orgList = pmOrganizationMapper.selectList(queryWrapper);
        if (orgList.isEmpty()) {
            return "";
        }
        return orgList.get(0).getOrganizationCode();
    }

    @Override
    public void receiveData(List<MidPmCountryEntity> dtos) {
        List<MidPmCountryEntity> insertDto = dtos.stream().filter(c -> c.getOpCode() == 1 && c.getExeStatus() == 0).collect(Collectors.toList());
        List<MidPmCountryEntity> updateDto = dtos.stream().filter(c -> c.getOpCode() == 2 && c.getExeStatus() == 0).collect(Collectors.toList());
        List<MidPmCountryEntity> deleteDto = dtos.stream().filter(c -> c.getOpCode() == 3 && c.getExeStatus() == 0).collect(Collectors.toList());
        List<MidPmCountryEntity> insertOrUpdateDto = dtos.stream().filter(c -> c.getOpCode() == 4 && c.getExeStatus() == 0).collect(Collectors.toList());


        insertData(insertDto);
        insertOrUpdateData(insertOrUpdateDto);
        updateData(updateDto);
        deleteData(deleteDto);
    }


    private void deleteData(List<MidPmCountryEntity> deleteDto) {
        if(CollectionUtils.isNotEmpty(deleteDto)){
            List<PmOrganizationEntity> allDatas = this.getAllDatas();
            List<Long> deleteData = new ArrayList<>();
            //删除
            deleteDto.forEach(c->{
                PmOrganizationEntity check = allDatas.stream().filter(x -> x.getOrganizationCode().equals(c.getNationCode())).findFirst().orElse(null);
                String anomalyMsg="国家代码不存在";
                if(check!=null){
                    c.setExeStatus(1);
                    c.setExeTime(new Date());
                    deleteData.add(check.getId());
                }else {
                    c.setCheckResult(1);
                    c.setExeStatus(3);
                    c.setExeMsg(anomalyMsg);
                    c.setExeTime(new Date());
                    c.setCheckResultDesc(anomalyMsg);
                }
            });
            if(CollectionUtils.isNotEmpty(deleteData)){
                this.delete(deleteData.toArray(new Serializable[deleteData.size()]));
                this.saveChange();
            }
            countryService.updateBatchById(deleteDto);
            countryService.saveChange();
        }
    }

    private void updateData(List<MidPmCountryEntity> updateDto) {
        if(CollectionUtils.isNotEmpty(updateDto)){
            List<PmOrganizationEntity> allDatas = this.getAllDatas();
            List<PmOrganizationEntity> updateData = new ArrayList<>();
            //更新
            updateDto.forEach(c->{
                PmOrganizationEntity check = allDatas.stream().filter(x -> x.getOrganizationCode().equals(c.getNationCode())).findFirst().orElse(null);
                String anomalyMsg="国家代码不存在";
                if(check!=null){
                    PmOrganizationEntity organizationEntity = new PmOrganizationEntity();
                    organizationEntity.setId(check.getId());
                    organizationEntity.setOrganizationCode(c.getNationCode());
                    organizationEntity.setOrganizationName(c.getNationName());
                    organizationEntity.setRemark(c.getNationRemark());
                    c.setExeStatus(1);
                    c.setExeTime(new Date());
                    updateData.add(organizationEntity);
                }else {
                    c.setCheckResult(1);
                    c.setExeStatus(3);
                    c.setExeMsg(anomalyMsg);
                    c.setExeTime(new Date());
                    c.setCheckResultDesc(anomalyMsg);
                }
            });
            if(CollectionUtils.isNotEmpty(updateData)){
                this.updateBatchById(updateData);
                this.saveChange();
            }
            countryService.updateBatchById(updateDto);
            countryService.saveChange();
        }
    }

    private void insertOrUpdateData(List<MidPmCountryEntity> insertOrUpdateDto) {
        if(CollectionUtils.isNotEmpty(insertOrUpdateDto)){
            List<PmOrganizationEntity> insert4Data = new ArrayList<>();
            List<PmOrganizationEntity> update4Data = new ArrayList<>();
            //新增or更新
            List<PmOrganizationEntity> allDatas = this.getAllDatas();
            insertOrUpdateDto.forEach(c->{
                PmOrganizationEntity check = allDatas.stream().filter(x -> x.getOrganizationCode().equals(c.getNationCode())).findFirst().orElse(null);
                PmOrganizationEntity organizationEntity = new PmOrganizationEntity();
                organizationEntity.setId(check.getId());
                organizationEntity.setOrganizationCode(c.getNationCode());
                organizationEntity.setOrganizationName(c.getNationName());
                organizationEntity.setRemark(c.getNationRemark());
                c.setExeStatus(1);
                c.setExeTime(new Date());
                if(check!=null){
                    organizationEntity.setId(check.getId());
                    update4Data.add(organizationEntity);
                }else {
                    organizationEntity.setId(IdGenerator.getId());
                    insert4Data.add(organizationEntity);
                }
            });
            if(CollectionUtils.isNotEmpty(insert4Data)){
                this.insertBatch(insert4Data);
            }
            if(CollectionUtils.isNotEmpty(update4Data)){
                this.updateBatchById(update4Data);
            }
            this.saveChange();
            countryService.updateBatchById(insertOrUpdateDto);
            countryService.saveChange();
        }
    }

    private void insertData(List<MidPmCountryEntity> insertDto) {
        if(CollectionUtils.isNotEmpty(insertDto)){
            List<PmOrganizationEntity> insertData = new ArrayList<>();
            List<PmOrganizationEntity> allDatas = this.getAllDatas();
            insertDto.forEach(c->{
                PmOrganizationEntity check = allDatas.stream().filter(x -> x.getOrganizationCode().equals(c.getNationCode())).findFirst().orElse(null);
                String anomalyMsg="国家代码重复";
                if(check!=null){
                    c.setCheckResult(1);
                    c.setExeStatus(3);
                    c.setExeMsg(anomalyMsg);
                    c.setExeTime(new Date());
                    c.setCheckResultDesc(anomalyMsg);
                }else {
                    PmOrganizationEntity organizationEntity = new PmOrganizationEntity();
                    organizationEntity.setId(IdGenerator.getId());
                    organizationEntity.setOrganizationCode(c.getNationCode());
                    organizationEntity.setOrganizationName(c.getNationName());
                    organizationEntity.setRemark(c.getNationRemark());
                    c.setExeStatus(1);
                    c.setExeTime(new Date());
                    insertData.add(organizationEntity);
                }
            });
            if(CollectionUtils.isNotEmpty(insertData)){
                this.insertBatch(insertData);
                this.saveChange();
            }
            countryService.updateBatchById(insertDto);
            countryService.saveChange();
        }
    }
}