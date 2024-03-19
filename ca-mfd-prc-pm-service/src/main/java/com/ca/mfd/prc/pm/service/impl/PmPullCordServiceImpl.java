package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.FieldNameAndGategory;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ClassUtil;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmPullCordEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.mapper.IPmPullCordMapper;
import com.ca.mfd.prc.pm.service.IPmPullCordService;
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
 * @author inkelink ${email}
 * @Description: 拉绳配置
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmPullCordServiceImpl extends AbstractPmCrudServiceImpl<IPmPullCordMapper, PmPullCordEntity> implements IPmPullCordService {

    private static final Map<String, String> BOOLEAN_FIELDS_MAPPING = new HashMap<>(2);
    private static final Object lockObj = new Object();

    static {
        BOOLEAN_FIELDS_MAPPING.put("deleteFlag", "isDelete");
        BOOLEAN_FIELDS_MAPPING.put("enableFlag", "isEnable");
    }

    @Autowired
    private IPmPullCordMapper pmPullCordDao;
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PM_PULLCORD";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmPullCordEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmPullCordEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmPullCordEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmPullCordEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeUpdate(PmPullCordEntity entity) {

    }

    @Override
    public void beforeInsert(PmPullCordEntity entity) {

    }

    @Override
    public List<PmPullCordEntity> getAllDatas() {
        List<PmPullCordEntity> datas = localCache.getObject(cacheName);
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
    public void update(PmPullCordEntity pmPullCordEntity) {
        LambdaUpdateWrapper<PmPullCordEntity> luw = new LambdaUpdateWrapper<>();
        luw.set(PmPullCordEntity::getPullcordName, pmPullCordEntity.getPullcordName());
        luw.set(PmPullCordEntity::getRemark, pmPullCordEntity.getRemark());
        luw.set(PmPullCordEntity::getIsEnable, pmPullCordEntity.getIsEnable());
        luw.set(PmPullCordEntity::getStopType, pmPullCordEntity.getStopType());
        luw.set(PmPullCordEntity::getTimeDelay, pmPullCordEntity.getTimeDelay());
        luw.set(PmPullCordEntity::getSequence, pmPullCordEntity.getSequence());
        luw.set(PmPullCordEntity::getType, pmPullCordEntity.getType());
        luw.eq(PmPullCordEntity::getId, pmPullCordEntity.getId());
        luw.eq(PmPullCordEntity::getVersion, pmPullCordEntity.getVersion());
        this.update(luw);
    }

    @Override
    public List<PmPullCordEntity> getByShopId(Long shopId) {
        return pmPullCordDao.selectList(Wrappers.lambdaQuery(PmPullCordEntity.class)
                .eq(PmPullCordEntity::getPrcPmWorkshopId, shopId)
                .eq(PmPullCordEntity::getVersion, 0)
                .eq(PmPullCordEntity::getIsDelete, false));
    }

    @Override
    public Map<String, String> getExcelHead() {
        Map<String, String> pmPullCordMap = new HashMap<>(8);
        pmPullCordMap.put("workshopCode", "车间代码");
        pmPullCordMap.put("lineCode", "线体代码");
        pmPullCordMap.put("workstationCode", "工位代码");
        pmPullCordMap.put("pullcordName", "名称");
        pmPullCordMap.put("type", "拉绳类型");
        pmPullCordMap.put("stopType", "停线类型");
        pmPullCordMap.put("timeDelay", "延时");
        pmPullCordMap.put("remark", "备注");
        pmPullCordMap.put("enableFlag", "是否启用");
        pmPullCordMap.put("deleteFlag", "是否删除");
        return pmPullCordMap;
    }

    @Override
    public List<PmPullCordEntity> getListByParentId(Long parentId) {
        QueryWrapper<PmPullCordEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmPullCordEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmPullCordEntity::getPrcPmWorkstationId, parentId);
        lambdaQueryWrapper.eq(PmPullCordEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmPullCordEntity::getVersion, 0);
        return this.pmPullCordDao.selectList(queryWrapper);
    }

    @Override
    public List<PmPullCordEntity> getPmPullCordEntityByVersion(Long shopId, int version, Boolean flags) {
        QueryWrapper<PmPullCordEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmPullCordEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmPullCordEntity::getPrcPmWorkshopId, shopId);
        lambdaQueryWrapper.eq(PmPullCordEntity::getVersion, version);
        lambdaQueryWrapper.eq(PmPullCordEntity::getIsEnable, true);
        lambdaQueryWrapper.eq(PmPullCordEntity::getIsDelete, flags);
        return pmPullCordDao.selectList(queryWrapper);
    }

    @Override
    public PmPullCordEntity get(Serializable id) {
        QueryWrapper<PmPullCordEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmPullCordEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmPullCordEntity::getId, id);
        lambdaQueryWrapper.eq(PmPullCordEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmPullCordEntity::getVersion, 0);
        List<PmPullCordEntity> pmPullCordEntityList = pmPullCordDao.selectList(queryWrapper);
        return pmPullCordEntityList.stream().findFirst().orElse(null);
    }

    @Override
    public void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) throws Exception {
        List<PmPullCordEntity> listOfPmPullCord = this.convertExcelDataToEntity(listFromExcelSheet,
                mapSysConfigByCategory, sheetName);
        //设置外键
        setForeignKey(listOfPmPullCord, currentUnDeployData);
        //验证并保存
        verifyAndSaveEntity(listOfPmPullCord, currentUnDeployData);
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

    private void setForeignKey(List<PmPullCordEntity> listOfPmPullCord, PmAllDTO pmAllDTO) {
        if (listOfPmPullCord.isEmpty()) {
            return;
        }
        for (PmPullCordEntity pmPullCordEntity : listOfPmPullCord) {
            setForeignKey(pmPullCordEntity, pmAllDTO);
        }
    }

    private void setForeignKey(PmPullCordEntity pmPullCordEntity, PmAllDTO pmAllDTO) {
        List<PmWorkShopEntity> shops = pmAllDTO.getShops();
        //设置车间id
        PmWorkShopEntity shop = shops.stream().filter(item -> Objects.equals(pmPullCordEntity.getWorkshopCode(), item.getWorkshopCode()))
                .findFirst().orElse(null);
        if (shop == null) {
            throw new InkelinkException("PullCord名称[" + pmPullCordEntity.getPullcordName() + "]对应的车间编码[" + pmPullCordEntity.getWorkshopCode() + "]没有对应任何车间，请检查是否有配置对应编码车间");
        }
        pmPullCordEntity.setPrcPmWorkshopId(shop.getId());
        List<PmLineEntity> lines = pmAllDTO.getLines();
        //设置线体id
        PmLineEntity line = lines.stream().filter(item -> Objects.equals(pmPullCordEntity.getLineCode(), item.getLineCode())
                        && Objects.equals(pmPullCordEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId()))
                .findFirst().orElse(null);
        if (line == null) {
            throw new InkelinkException("PullCord名称[" + pmPullCordEntity.getPullcordName() + "]对应的线体编码[" + pmPullCordEntity.getLineCode() + "]没有对应任何线体，请检查是否有配置对应编码线体");
        }
        pmPullCordEntity.setPrcPmLineId(line.getId());
        List<PmWorkStationEntity> stations = pmAllDTO.getStations();
        //设置工位id
        PmWorkStationEntity workStation = stations.stream().filter(item -> Objects.equals(pmPullCordEntity.getWorkstationCode(), item.getWorkstationCode())
                        && Objects.equals(pmPullCordEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId())
                        && Objects.equals(pmPullCordEntity.getPrcPmLineId(), item.getPrcPmLineId()))
                .findFirst().orElse(null);
        if (workStation == null) {
            throw new InkelinkException("PullCord名称[" + pmPullCordEntity.getPullcordName() + "]对应的线体编码[" + pmPullCordEntity.getLineCode() + "]对应的工位编码[" + pmPullCordEntity.getWorkstationCode() + "]没有对应任何工位，请检查是否有配置对应编码工位");
        }
        pmPullCordEntity.setPrcPmWorkstationId(workStation.getId());
    }

    private void verifyAndSaveEntity(List<PmPullCordEntity> listEntity,
                                     PmAllDTO currentUnDeployData) {
        if (listEntity == null || listEntity.isEmpty()) {
            return;
        }
        List<PmPullCordEntity> insertList = new ArrayList<>(listEntity.size());
        Map<Long, Set<String>> mapOfPullCordNameByStationId = new HashMap(16);
        for (PmPullCordEntity pullCord : listEntity) {
            if(pullCord.getIsDelete()){
                LambdaUpdateWrapper<PmPullCordEntity> luw = new LambdaUpdateWrapper();
                luw.set(PmPullCordEntity::getIsDelete, true);
                luw.eq(PmPullCordEntity::getPrcPmWorkstationId, pullCord.getPrcPmWorkstationId());
                luw.eq(PmPullCordEntity::getPullcordName, pullCord.getPullcordName());
                luw.eq(PmPullCordEntity::getVersion, 0);
                this.update(luw,false);
                continue;
            }
            ClassUtil.validNullByNullAnnotation(pullCord);
            //验证顺序
            //verifyPullCordSequence(pullCord,mapOfPullCordSequenceByStationId);
            //验证名称
            verifyPullCordName(pullCord, mapOfPullCordNameByStationId);

            PmPullCordEntity existPullCord = currentUnDeployData.getPullCords().stream().filter(
                            item -> Objects.equals(item.getPrcPmWorkstationId(), pullCord.getPrcPmWorkstationId())
                                    && Objects.equals(item.getPullcordName(), pullCord.getPullcordName()))
                    .findFirst().orElse(null);
            if (existPullCord != null) {
                LambdaUpdateWrapper<PmPullCordEntity> luw = new LambdaUpdateWrapper();
                luw.set(PmPullCordEntity::getPullcordName, pullCord.getPullcordName());
                luw.set(PmPullCordEntity::getType, pullCord.getType());
                luw.set(PmPullCordEntity::getStopType, pullCord.getStopType());
                luw.set(PmPullCordEntity::getTimeDelay, pullCord.getTimeDelay());
                luw.set(PmPullCordEntity::getRemark, pullCord.getRemark());
                luw.set(PmPullCordEntity::getIsDelete, pullCord.getIsDelete());
                luw.set(PmPullCordEntity::getIsEnable, pullCord.getIsEnable());

                luw.eq(PmPullCordEntity::getId, existPullCord.getId());
                luw.eq(PmPullCordEntity::getVersion, 0);
                this.update(luw,false);
            } else {
                pullCord.setVersion(0);
                insertList.add(pullCord);
                //this.insert(pullCord);
            }
        }
        if(!insertList.isEmpty()){
            this.insertBatch(insertList,200,false,1);
        }

    }

    private void verifyPullCordSequence(PmPullCordEntity pullCord, Map<Long, Set<Integer>> mapOfPullCordSequenceByStationId) {
        Set<Integer> setOfPullCordSequence = mapOfPullCordSequenceByStationId.computeIfAbsent(pullCord.getPrcPmWorkstationId(), v -> new HashSet<>());
        if (setOfPullCordSequence.contains(pullCord.getSequence())) {
            throw new InkelinkException("车间[" + pullCord.getWorkshopCode() + "]>线体[" + pullCord.getLineCode() + "]>工位[" + pullCord.getWorkstationCode() + "]>拉绳顺序号[" + pullCord.getSequence() + "]重复");
        }
        setOfPullCordSequence.add(pullCord.getSequence());
    }

    private void verifyPullCordName(PmPullCordEntity pullCord, Map<Long, Set<String>> mapOfPullCordNameByStationId) {
        Set<String> setOfPullCordName = mapOfPullCordNameByStationId.computeIfAbsent(pullCord.getPrcPmWorkstationId(), v -> new HashSet<>());
        if (setOfPullCordName.contains(pullCord.getPullcordName())) {
            throw new InkelinkException("车间[" + pullCord.getWorkshopCode() + "]>线体[" + pullCord.getLineCode() + "]>工位[" + pullCord.getWorkstationCode() + "]>拉绳名称[" + pullCord.getPullcordName() + "]重复");
        }
        setOfPullCordName.add(pullCord.getPullcordName());
    }

    protected String getSysConfig(String fieldName, Map<String, String> eachRowData) {
        if("type".equals(fieldName)){
            return eachRowData.get("workshopCode") + FieldNameAndGategory.getGategoryName(fieldName);
        }else{
            return FieldNameAndGategory.getGategoryName(fieldName);
        }
    }

}