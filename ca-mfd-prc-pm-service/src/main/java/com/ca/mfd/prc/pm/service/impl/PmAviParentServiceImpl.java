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
import com.ca.mfd.prc.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.mapper.IPmAviMapper;
import com.ca.mfd.prc.pm.service.IPmAviParentService;
import com.google.common.collect.Maps;
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
 * @Description: AVI站点(定义父类 为了解决项目中循环依赖的问题 ， 项目规范中不能使用循环依赖))
 * 所有在pmService里要用的方法都在这里面定义
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service("pmAviParentService")
public class PmAviParentServiceImpl extends AbstractPmCrudServiceImpl<IPmAviMapper, PmAviEntity> implements IPmAviParentService {

    //用于接收excel表格和映射布尔类型字段:value是实体中布尔类型字段名称，key是接收excel表格的字段名称
    private static final Map<String, String> BOOLEAN_FIELDS_MAPPING = new HashMap<>(3);
    private static final String FIELD_NAME_AVI_TYPE = "aviType";

    private static final String FIELD_NAME_AVI_FUNCTIONS = "aviFunctions";
    private static final Object lockObj = new Object();

    static {
        BOOLEAN_FIELDS_MAPPING.put("mainFlag", "isMain");
        BOOLEAN_FIELDS_MAPPING.put("deleteFlag", "isDelete");
        BOOLEAN_FIELDS_MAPPING.put("enableFlag", "isEnable");
    }

    @Autowired
    private IPmAviMapper pmAviDao;
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PM_AVI";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmAviEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmAviEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmAviEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmAviEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PmAviEntity> getAllDatas() {
        List<PmAviEntity> datas = localCache.getObject(cacheName);
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
    public Map<String, String> getExcelColumnNames() {
        Map<String, String> maps = Maps.newHashMapWithExpectedSize(21);
        maps.put("ShopName", "车间名称");
        maps.put("ShopCode", "车间编码");
        maps.put("AreaName", "线体名称");
        maps.put("AreaCode", "线体编码");
        maps.put("Name", "AVI名称");
        maps.put("Code", "AVI编码");
        maps.put("Type", "类型");
        maps.put("Model", "PLC Model");
        maps.put("IpAddress", "IP");
        maps.put("MacAddress", "MAC");
        maps.put("OpcConnector", "OPC连接");
        maps.put("PointDb", "站点DB");
        maps.put("Db3", "DB3");
        maps.put("Db4", "Db4");
        maps.put("IsEnable", "是否启用");
        maps.put("Remark", "备注");
        maps.put("CreateDt", "创建时间");
        maps.put("UpdateDt", "更新时间");
        maps.put("CreateUserName", "创建用户");
        maps.put("UpdateUserName", "更新用户");
        return maps;
    }

    @Override
    public List<PmAviEntity> getByShopId(Long shopId) {
        return pmAviDao.selectList(Wrappers.lambdaQuery(PmAviEntity.class)
                .eq(PmAviEntity::getPrcPmWorkshopId, shopId)
                .eq(PmAviEntity::getVersion, 0)
                .eq(PmAviEntity::getIsDelete, false)
                .orderByAsc(PmAviEntity::getAviDisplayNo));
    }

    @Override
    public Map<String, String> getExcelHead() {
        Map<String, String> excelHeadMap = new HashMap<>(15);
        excelHeadMap.put("workshopCode", "区域代码");
        excelHeadMap.put("lineCode", "工作中心代码");
        excelHeadMap.put("aviCode", "编码");
        excelHeadMap.put("aviName", "名称");
        excelHeadMap.put("aviType", "站点类型");
        excelHeadMap.put("enableFlag", "是否启用");
        excelHeadMap.put("AviAttribute", "特性");
        excelHeadMap.put("mainFlag", "关键点");
        excelHeadMap.put("IpAddress", "AVI站点地址");
        excelHeadMap.put("OpcConnector", "PLC链接");
        excelHeadMap.put("PointDb", "站点DB");
        excelHeadMap.put("AviFunctions", "AVI功能");
        excelHeadMap.put("DefaultPage", "AVI默认页面");
        excelHeadMap.put("Remark", "备注");
        excelHeadMap.put("deleteFlag", "是否删除");
        return excelHeadMap;
    }

    @Override
    public List<PmAviEntity> getListByLineId(Long lineId) {
        QueryWrapper<PmAviEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmAviEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmAviEntity::getPrcPmLineId, lineId);
        lambdaQueryWrapper.eq(PmAviEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmAviEntity::getVersion, 0);
        lambdaQueryWrapper.orderByAsc(PmAviEntity::getAviDisplayNo);
        return pmAviDao.selectList(queryWrapper);
    }

    @Override
    public List<PmAviEntity> getPmAviEntityByVersion(Long shopId, int version, Boolean flags) {
        QueryWrapper<PmAviEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmAviEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmAviEntity::getPrcPmWorkshopId, shopId);
        lambdaQueryWrapper.eq(PmAviEntity::getVersion, version);
        lambdaQueryWrapper.eq(PmAviEntity::getIsEnable, true);
        lambdaQueryWrapper.eq(PmAviEntity::getIsDelete, flags);
        return pmAviDao.selectList(queryWrapper);
    }

    @Override
    public PmAviEntity get(Serializable id) {
        QueryWrapper<PmAviEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmAviEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmAviEntity::getId, id);
        lambdaQueryWrapper.eq(PmAviEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmAviEntity::getVersion, 0);
        List<PmAviEntity> pmAviEntityList = pmAviDao.selectList(queryWrapper);
        return pmAviEntityList.stream().findFirst().orElse(null);
    }

    @Override
    public void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) throws Exception {
        List<PmAviEntity> listOfPmAvi = this.convertExcelDataToEntity(listFromExcelSheet,
                mapSysConfigByCategory, sheetName);
        //设置外键
        setForeignKey(listOfPmAvi, currentUnDeployData);
        //验证并保存
        verifyAndSaveEntity(listOfPmAvi, currentUnDeployData);
    }

    @Override
    protected String customSettings(Map.Entry<String, String> eachColumnData,
                                    Map<String, String> mapSysConfigByCategory) {
        if (mapSysConfigByCategory != null
                && FIELD_NAME_AVI_TYPE.equals(eachColumnData.getKey())) {
            return setMultiValue(eachColumnData, mapSysConfigByCategory, ',');

        } else if (mapSysConfigByCategory != null
                && FIELD_NAME_AVI_FUNCTIONS.equals(eachColumnData.getKey())) {
            return setMultiValue(eachColumnData, mapSysConfigByCategory, ',');

        }
        return null;
    }

    @Override
    protected boolean canBeNullOrEmpty(Map.Entry<String, String> eachColumnData) {
        if (FIELD_NAME_AVI_TYPE.equals(eachColumnData.getKey())) {
            return false;
        } else{
            return !FIELD_NAME_AVI_FUNCTIONS.equals(eachColumnData.getKey());
        }
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

    private void setForeignKey(List<PmAviEntity> listOfPmAvi, PmAllDTO pmAllDTO) {
        if (listOfPmAvi.isEmpty()) {
            return;
        }
        for (PmAviEntity pmAviEntity : listOfPmAvi) {
            setForeignKey(pmAviEntity, pmAllDTO);
        }
    }

    private void setForeignKey(PmAviEntity pmAviEntity, PmAllDTO pmAllDTO) {
        List<PmWorkShopEntity> shops = pmAllDTO.getShops();
        //设置车间id
        PmWorkShopEntity shop = shops.stream().filter(item -> Objects.equals(pmAviEntity.getWorkshopCode(), item.getWorkshopCode()))
                .findFirst().orElse(null);
        if (shop == null) {
            throw new InkelinkException("Avi编码[" + pmAviEntity.getAviCode() + "]对应的车间编码[" + pmAviEntity.getWorkshopCode() + "]没有对应任何车间，请检查是否有配置对应编码车间");
        }
        pmAviEntity.setPrcPmWorkshopId(shop.getId());
        List<PmLineEntity> lines = pmAllDTO.getLines();
        //设置线体id
        PmLineEntity line = lines.stream().filter(item -> Objects.equals(pmAviEntity.getLineCode(), item.getLineCode()))
                .findFirst().orElse(null);
        if (line == null) {
            throw new InkelinkException("Avi编码[" + pmAviEntity.getAviCode() + "]对应车间编码[" + pmAviEntity.getWorkshopCode() + "]线体编码[" + pmAviEntity.getLineCode() + "]没有对应任何线体，请检查是否有配置对应编码线体");
        }
        pmAviEntity.setPrcPmLineId(line.getId());
    }

    private void verifyAndSaveEntity(List<PmAviEntity> listEntity,
                                     PmAllDTO currentUnDeployData) {
        if (listEntity == null || listEntity.isEmpty()) {
            return;
        }
        List<PmAviEntity> insertList = new ArrayList<>(listEntity.size());
        Map<Long, Set<String>> mapOfAviCodeByShopId = new HashMap(16);
        Map<Long, Set<String>> mapOfAviNameByShopId = new HashMap(16);
        for (PmAviEntity avi : listEntity) {
            if(avi.getIsDelete()){
                LambdaUpdateWrapper<PmAviEntity> luw = new LambdaUpdateWrapper();
                luw.set(PmAviEntity::getIsDelete, true);
                luw.eq(PmAviEntity::getAviCode, avi.getAviCode());
                luw.eq(PmAviEntity::getVersion, 0);
                this.update(luw,false);
                continue;
            }
            ClassUtil.validNullByNullAnnotation(avi);
            //验证编码
            verifyAviCode(avi, mapOfAviCodeByShopId);
            //验证名称
            verifyAviName(avi, mapOfAviNameByShopId);

            PmAviEntity existAvi = currentUnDeployData.getAvis().stream().filter(
                    item -> Objects.equals(item.getAviCode(), avi.getAviCode())).findFirst().orElse(null);
            if (existAvi != null) {
                PmAviEntity existSameNameAvi = currentUnDeployData.getAvis().stream().filter(
                        item -> Objects.equals(item.getAviName(), avi.getAviName())
                                && !Objects.equals(item.getId(), existAvi.getId())).findFirst().orElse(null);
                if (existSameNameAvi != null) {
                    throw new InkelinkException("Avi名称[" + avi.getAviName() + "]已存在");
                }
                PmAviEntity existSameCodeAvi = currentUnDeployData.getAvis().stream().filter(
                        item -> Objects.equals(item.getAviCode(), avi.getAviCode())
                                && !Objects.equals(item.getId(), existAvi.getId())).findFirst().orElse(null);
                if (existSameCodeAvi != null) {
                    throw new InkelinkException("Avi编码[" + avi.getAviCode() + "]已经存在");
                }
                LambdaUpdateWrapper<PmAviEntity> luw = new LambdaUpdateWrapper();
                luw.set(PmAviEntity::getAviName, avi.getAviName());
                luw.set(PmAviEntity::getAviType, avi.getAviType());
                luw.set(PmAviEntity::getRemark, avi.getRemark());

                luw.set(PmAviEntity::getIsEnable, avi.getIsEnable());
                luw.set(PmAviEntity::getIsMain, avi.getIsMain());
                luw.set(PmAviEntity::getAviFunctions, avi.getAviFunctions());
                luw.set(PmAviEntity::getOpcConnector, avi.getOpcConnector());
                luw.set(PmAviEntity::getPointDb, avi.getPointDb());
                luw.set(PmAviEntity::getDefaultPage, avi.getDefaultPage());
                luw.set(PmAviEntity::getIsDelete, avi.getIsDelete());

                luw.eq(PmAviEntity::getId, existAvi.getId());
                luw.eq(PmAviEntity::getVersion, 0);
                this.update(luw);
            } else {
                PmAviEntity existSameStationNameAvi = currentUnDeployData.getAvis().stream().filter(
                                item -> Objects.equals(item.getAviName(), avi.getAviName()))
                        .findFirst().orElse(null);
                if (existSameStationNameAvi != null) {
                    throw new InkelinkException("Avi名称[" + avi.getAviName() + "]已存在");
                }
                avi.setVersion(0);
                insertList.add(avi);
                //this.insert(avi);
            }
        }
        if(!insertList.isEmpty()){
            this.insertBatch(insertList,200,false,1);
        }

    }

    private void verifyAviCode(PmAviEntity avi, Map<Long, Set<String>> mapOfAviCodeByShopId) {
        Set<String> setOfAviCode = mapOfAviCodeByShopId.computeIfAbsent(avi.getPrcPmWorkshopId(), v -> new HashSet<>());
        if (setOfAviCode.contains(avi.getAviCode())) {
            throw new InkelinkException("车间[" + avi.getWorkshopCode() + "]>线体[" + avi.getLineCode() + "]>Avi编码[" + avi.getAviCode() + "]重复");
        }
        setOfAviCode.add(avi.getAviCode());
    }

    private void verifyAviName(PmAviEntity avi, Map<Long, Set<String>> mapOfAviCodeByShopId) {
        Set<String> setOfAviName = mapOfAviCodeByShopId.computeIfAbsent(avi.getPrcPmWorkshopId(), v -> new HashSet<>());
        if (setOfAviName.contains(avi.getAviName())) {
            throw new InkelinkException("车间[" + avi.getWorkshopCode() + "]>线体[" + avi.getLineCode() + "]>Avi名称[" + avi.getAviName() + "]重复");
        }
        setOfAviName.add(avi.getAviName());
    }
}