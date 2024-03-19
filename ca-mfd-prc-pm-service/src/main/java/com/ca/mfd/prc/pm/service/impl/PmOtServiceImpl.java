package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ClassUtil;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.*;
import com.ca.mfd.prc.pm.mapper.IPmOtMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkStationMapper;
import com.ca.mfd.prc.pm.service.IPmOtService;
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

import static com.ca.mfd.prc.common.constant.Constant.TRUE_BOOL;
import static com.ca.mfd.prc.common.constant.Constant.TRUE_CHINESE;
import static com.ca.mfd.prc.common.constant.Constant.TRUE_NUM;

/**
 * @author inkelink
 * @Description: 操作终端
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmOtServiceImpl extends AbstractPmCrudServiceImpl<IPmOtMapper, PmOtEntity> implements IPmOtService {

    private static final Map<String, String> BOOLEAN_FIELDS_MAPPING = new HashMap<>(2);
    private static final Object lockObj = new Object();

    static {
        BOOLEAN_FIELDS_MAPPING.put("deleteFlag", "isDelete");
        BOOLEAN_FIELDS_MAPPING.put("enableFlag", "isEnable");
    }

    @Autowired
    private IPmOtMapper pmOtDao;
    @Autowired
    private LocalCache localCache;
    @Autowired
    private IPmWorkStationMapper pmWorkStationMapper;
    private final String cacheName = "PRC_PM_OT";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmOtEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmOtEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmOtEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmOtEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeUpdate(PmOtEntity entity) {
        validData(entity);
    }

    @Override
    public void beforeInsert(PmOtEntity entity) {
        validData(entity);
    }

    private void validData(PmOtEntity model) {
        if(model.getPrcPmWorkshopId() == null || model.getPrcPmWorkshopId() == 0
                || model.getPrcPmLineId() == null || model.getPrcPmLineId() == 0){
            validWorkStation(model);
        }
        checkRepeatIp(model.getIpAddress(), model.getId());
    }
    private void validWorkStation(PmOtEntity entity) {
        //工位信息
        QueryWrapper<PmWorkStationEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmWorkStationEntity> lwq = qw.lambda();
        String msg = "编码";
        String res = entity.getWorkstationCode();
        if(entity.getPrcPmWorkstationId() != null && entity.getPrcPmWorkstationId() > 0){
            lwq.eq(PmWorkStationEntity::getId, entity.getPrcPmWorkstationId());
            msg = "ID";
            res = String.valueOf(entity.getPrcPmWorkstationId());
        }else{
            lwq.eq(PmWorkStationEntity::getWorkstationCode, entity.getWorkstationCode());
        }
        lwq.eq(PmWorkStationEntity::getVersion, 0);
        PmWorkStationEntity pmWorkStationEntity = this.pmWorkStationMapper.selectList(qw).stream().findFirst().orElse(null);
        if (pmWorkStationEntity == null) {
            throw new InkelinkException(String.format("OT[%s]对应的工位%s[%s]不存在",entity.getOtName(),msg,res));
        }
        entity.setPrcPmLineId(pmWorkStationEntity.getPrcPmLineId());
        entity.setPrcPmWorkshopId(pmWorkStationEntity.getPrcPmWorkshopId());
    }

    @Override
    public List<PmOtEntity> getAllDatas() {
        List<PmOtEntity> datas = localCache.getObject(cacheName);
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
    public void update(PmOtEntity pmOtEntity) {
        checkRepeatIp(pmOtEntity.getIpAddress(), pmOtEntity.getId());
        LambdaUpdateWrapper<PmOtEntity> luw = new LambdaUpdateWrapper<>();
        luw.set(PmOtEntity::getPrcPmWorkstationId, pmOtEntity.getPrcPmWorkstationId());
        luw.set(PmOtEntity::getOtName, pmOtEntity.getOtName());
        luw.set(PmOtEntity::getIpAddress, pmOtEntity.getIpAddress());
        luw.set(PmOtEntity::getOtDescription, pmOtEntity.getOtDescription());
        luw.set(PmOtEntity::getRemark, pmOtEntity.getRemark());
        luw.set(PmOtEntity::getIsEnable, pmOtEntity.getIsEnable());
        luw.set(PmOtEntity::getTemplate, pmOtEntity.getTemplate());
        luw.eq(PmOtEntity::getId, pmOtEntity.getId());
        luw.eq(PmOtEntity::getVersion, pmOtEntity.getVersion());
        luw.eq(PmOtEntity::getPrcPmWorkshopId, pmOtEntity.getPrcPmWorkshopId());
        luw.eq(PmOtEntity::getPrcPmLineId, pmOtEntity.getPrcPmLineId());
        luw.eq(PmOtEntity::getPrcPmWorkstationId, pmOtEntity.getPrcPmWorkstationId());
        this.update(luw);
    }

    private void checkRepeatIp(String ipAddress, Long id) {
        QueryWrapper<PmOtEntity> qrw = new QueryWrapper<>();
        qrw.lambda().eq(PmOtEntity::getIpAddress, ipAddress)
                .eq(PmOtEntity::getIsEnable, true)
                .ne(PmOtEntity :: getId, id == null ? 0 : id);
        List<PmOtEntity> repeatIpOt = getData(qrw, false);
        if (CollectionUtils.isNotEmpty(repeatIpOt)) {
            throw new InkelinkException("OT的IP地址不能重复");
        }
    }

    /**
     * 跟据ip 查询
     *
     * @param id id
     * @param ip ip
     * @return 返回实体
     */
    @Override
    public PmOtEntity getEntityByIp(String id, String ip) {
        QueryWrapper<PmOtEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmOtEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmOtEntity::getIpAddress, ip);
        lambdaQueryWrapper.ne(PmOtEntity::getId, id);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public List<PmOtEntity> getListByParentId(Long parentId) {
        QueryWrapper<PmOtEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmOtEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmOtEntity::getPrcPmWorkstationId, parentId);
        lambdaQueryWrapper.eq(PmOtEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmOtEntity::getVersion, 0);
        return this.pmOtDao.selectList(queryWrapper);

    }

    @Override
    public List<PmOtEntity> getByShopId(Long shopId) {
        return pmOtDao.selectList(Wrappers.lambdaQuery(PmOtEntity.class)
                .eq(PmOtEntity::getPrcPmWorkshopId, shopId)
                .eq(PmOtEntity::getVersion, 0)
                .eq(PmOtEntity::getIsDelete, false));
    }

    @Override
    public Map<String, String> getExcelHead() {
        Map<String, String> pmOtMap = new HashMap<>(10);
        pmOtMap.put("workshopCode", "车间代码");
        pmOtMap.put("lineCode", "线体代码");
        pmOtMap.put("workstationCode", "工位代码");
        pmOtMap.put("otName", "名称");
        pmOtMap.put("template", "模板");
        pmOtMap.put("ipAddress", "IP地址");
        pmOtMap.put("otDescription", "描述");
        pmOtMap.put("remark", "备注");
        pmOtMap.put("enableFlag", "是否启用");
        pmOtMap.put("deleteFlag", "是否删除");

        return pmOtMap;
    }

    @Override
    public List<PmOtEntity> getPmOtEntityByVersion(Long shopId, int version, Boolean flags) {
        QueryWrapper<PmOtEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmOtEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmOtEntity::getPrcPmWorkshopId, shopId);
        lambdaQueryWrapper.eq(PmOtEntity::getVersion, version);
        lambdaQueryWrapper.eq(PmOtEntity::getIsEnable, true);
        lambdaQueryWrapper.eq(PmOtEntity::getIsDelete, flags);
        return pmOtDao.selectList(queryWrapper);
    }

    @Override
    public void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) throws Exception {
        List<PmOtEntity> listOfPmOt = this.convertExcelDataToEntity(listFromExcelSheet,
                mapSysConfigByCategory, sheetName);
        //设置外键
        setForeignKey(listOfPmOt, currentUnDeployData);
        //验证并保存
        verifyAndSaveEntity(listOfPmOt, currentUnDeployData);
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

    private void setForeignKey(List<PmOtEntity> listOfPmOt, PmAllDTO pmAllDTO) {
        if (listOfPmOt.isEmpty()) {
            return;
        }
        for (PmOtEntity pmOtEntity : listOfPmOt) {
            setForeignKey(pmOtEntity, pmAllDTO);
        }
    }

    private void setForeignKey(PmOtEntity pmOtEntity, PmAllDTO pmAllDTO) {
        List<PmWorkShopEntity> shops = pmAllDTO.getShops();
        //设置车间id
        PmWorkShopEntity shop = shops.stream().filter(item -> Objects.equals(pmOtEntity.getWorkshopCode(), item.getWorkshopCode()))
                .findFirst().orElse(null);
        if (shop == null) {
            throw new InkelinkException("Ot名称[" + pmOtEntity.getOtName() + "]对应的车间编码(内部编码)[" + pmOtEntity.getWorkshopCode() + "]没有对应任何车间，请检查是否有配置对应编码车间");
        }
        pmOtEntity.setPrcPmWorkshopId(shop.getId());
        List<PmLineEntity> lines = pmAllDTO.getLines();
        //设置线体id
        PmLineEntity line = lines.stream().filter(item -> Objects.equals(pmOtEntity.getLineCode(), item.getLineCode())
                        && Objects.equals(pmOtEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId()))
                .findFirst().orElse(null);
        if (line == null) {
            throw new InkelinkException("Ot名称[" + pmOtEntity.getOtName() + "]对应的线体编码[" + pmOtEntity.getLineCode() + "]没有对应任何线体，请检查是否有配置对应编码线体");
        }
        pmOtEntity.setPrcPmLineId(line.getId());
        List<PmWorkStationEntity> stations = pmAllDTO.getStations();
        //设置工位id
        PmWorkStationEntity workStation = stations.stream().filter(item ->
                        Objects.equals(pmOtEntity.getWorkstationCode(), item.getWorkstationCode())
                                && Objects.equals(pmOtEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId())
                                && Objects.equals(pmOtEntity.getPrcPmLineId(), item.getPrcPmLineId()))
                .findFirst().orElse(null);
        if (workStation == null) {
            throw new InkelinkException("Ot名称[" + pmOtEntity.getOtName() + "]对应的线体编码[" + pmOtEntity.getLineCode() + "]对应的工位编码[" + pmOtEntity.getWorkstationCode() + "]没有对应任何工位，请检查是否有配置对应编码工位");
        }
        pmOtEntity.setPrcPmWorkstationId(workStation.getId());
    }

    private void verifyAndSaveEntity(List<PmOtEntity> listEntity,
                                     PmAllDTO currentUnDeployData) {
        if (listEntity == null || listEntity.isEmpty()) {
            return;
        }
        List<PmOtEntity> insertList = new ArrayList<>(listEntity.size());
        Map<Long, Set<String>> mapOfOtNameByStationId = new HashMap(16);
        for (PmOtEntity ot : listEntity) {
            if(ot.getIsDelete()){
                LambdaUpdateWrapper<PmOtEntity> luw = new LambdaUpdateWrapper();
                luw.set(PmOtEntity::getIsDelete, true);
                luw.eq(PmOtEntity::getPrcPmWorkstationId, ot.getPrcPmWorkstationId());
                luw.eq(PmOtEntity::getOtName, ot.getOtName());
                luw.eq(PmOtEntity::getVersion, 0);
                this.update(luw,false);
                continue;
            }
            ClassUtil.validNullByNullAnnotation(ot);
            //验证名称
            verifyOtName(ot, mapOfOtNameByStationId);

            PmOtEntity existOt = currentUnDeployData.getOts().stream().filter(
                            item -> Objects.equals(item.getPrcPmWorkstationId(), ot.getPrcPmWorkstationId())
                                    && Objects.equals(item.getOtName(), ot.getOtName()))
                    .findFirst().orElse(null);
            if (existOt != null) {
                LambdaUpdateWrapper<PmOtEntity> luw = new LambdaUpdateWrapper();
                luw.set(PmOtEntity::getOtName, ot.getOtName());
                luw.set(PmOtEntity::getTemplate, ot.getTemplate());
                luw.set(PmOtEntity::getIpAddress, ot.getIpAddress());
                luw.set(PmOtEntity::getOtDescription, ot.getOtDescription());
                luw.set(PmOtEntity::getRemark, ot.getRemark());
                luw.set(PmOtEntity::getIsDelete, ot.getIsDelete());
                luw.set(PmOtEntity::getIsEnable, ot.getIsEnable());

                luw.eq(PmOtEntity::getId, existOt.getId());
                luw.eq(PmOtEntity::getVersion, 0);
                this.update(luw,false);
            } else {
                ot.setVersion(0);
                insertList.add(ot);
                //this.insert(ot);
            }
        }
        if(!insertList.isEmpty()){
            this.insertBatch(insertList,200,false,1);
        }

    }


    private void verifyOtName(PmOtEntity ot, Map<Long, Set<String>> mapOfOtNameByStationId) {
        Set<String> setOfOtName = mapOfOtNameByStationId.computeIfAbsent(ot.getPrcPmWorkshopId(), v -> new HashSet<>());
        if (setOfOtName.contains(ot.getOtName())) {
            throw new InkelinkException("车间[" + ot.getWorkshopCode() + "]>线体[" + ot.getLineCode() + "]>工位[" + ot.getWorkstationCode() + "]>ot名称[" + ot.getOtName() + "]重复");
        }
        setOfOtName.add(ot.getOtName());
    }


}