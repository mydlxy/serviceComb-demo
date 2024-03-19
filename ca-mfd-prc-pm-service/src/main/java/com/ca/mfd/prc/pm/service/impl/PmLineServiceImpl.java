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
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.excel.PmAllModel;
import com.ca.mfd.prc.pm.mapper.IPmLineMapper;
import com.ca.mfd.prc.pm.service.IPmAviService;
import com.ca.mfd.prc.pm.service.IPmLineService;
import com.ca.mfd.prc.pm.service.IPmWorkStationService;
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

import static com.ca.mfd.prc.common.constant.Constant.TRUE_BOOL;
import static com.ca.mfd.prc.common.constant.Constant.TRUE_CHINESE;
import static com.ca.mfd.prc.common.constant.Constant.TRUE_NUM;

/**
 * @author inkelink
 * @Description: 生产线服务
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmLineServiceImpl extends AbstractPmCrudServiceImpl<IPmLineMapper, PmLineEntity> implements IPmLineService {

    private static final String LINE_TYPE = "lineType";

    //用于接收excel表格和映射布尔类型字段:value是实体中布尔类型字段名称，key是接收excel表格的字段名称
    private static final Map<String, String> BOOLEAN_FIELDS_MAPPING = new HashMap<>(2);
    private static final Object lockObj = new Object();

    static {
        BOOLEAN_FIELDS_MAPPING.put("lineDeleteFlag", "isDelete");
        BOOLEAN_FIELDS_MAPPING.put("lineEnableFlag", "isEnable");
    }

    @Autowired
    private IPmLineMapper pmLineDao;
    @Autowired
    private IPmWorkStationService workStationService;
    @Autowired
    private IPmAviService aviService;

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PM_LINE";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmLineEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmLineEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmLineEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmLineEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PmLineEntity> getAllDatas() {
        List<PmLineEntity> datas = localCache.getObject(cacheName);
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
    public void update(PmLineEntity pmLineEntity) {
        beforeUpdate(pmLineEntity);
        LambdaUpdateWrapper<PmLineEntity> luw = new LambdaUpdateWrapper<>();
        luw.set(PmLineEntity::getLineCode, pmLineEntity.getLineCode());
        luw.set(PmLineEntity::getLineName, pmLineEntity.getLineName());
        luw.set(PmLineEntity::getLineRemark, pmLineEntity.getLineRemark());
        luw.set(PmLineEntity::getProductTime, pmLineEntity.getProductTime());
        luw.set(PmLineEntity::getLineDisplayNo, pmLineEntity.getLineDisplayNo());
        luw.set(PmLineEntity::getStationLength, pmLineEntity.getStationLength());
        luw.set(PmLineEntity::getBeginDistance, pmLineEntity.getBeginDistance());
        luw.set(PmLineEntity::getEndDistance, pmLineEntity.getEndDistance());
        luw.set(PmLineEntity::getLineType, pmLineEntity.getLineType());
        luw.set(PmLineEntity::getQueueDb, pmLineEntity.getQueueDb());
        luw.set(PmLineEntity::getPlcDb3, pmLineEntity.getPlcDb3());
        luw.set(PmLineEntity::getAndonDb, pmLineEntity.getAndonDb());
        luw.set(PmLineEntity::getOpcConnect, pmLineEntity.getOpcConnect());
        luw.set(PmLineEntity::getFrontBufferUpLimit, pmLineEntity.getFrontBufferUpLimit());
        luw.set(PmLineEntity::getFrontBufferLowLimit, pmLineEntity.getFrontBufferLowLimit());
        luw.set(PmLineEntity::getReceiveWorkCenterCode, pmLineEntity.getReceiveWorkCenterCode());
        luw.set(PmLineEntity::getRearBufferUpLimit, pmLineEntity.getRearBufferUpLimit());
        luw.set(PmLineEntity::getRearBufferLowLimit, pmLineEntity.getRearBufferLowLimit());
        luw.set(PmLineEntity::getLineType, pmLineEntity.getLineType());
        luw.set(PmLineEntity::getIsEnable, pmLineEntity.getIsEnable());
        luw.set(PmLineEntity::getLineDesignJph, pmLineEntity.getLineDesignJph());
        luw.set(PmLineEntity::getReceiveWorkCenterCode, pmLineEntity.getReceiveWorkCenterCode());
        luw.set(PmLineEntity::getStationDb, pmLineEntity.getStationDb());
        luw.set(PmLineEntity::getAndonOpcConnect, pmLineEntity.getAndonOpcConnect());
        luw.set(PmLineEntity::getEntryDb, pmLineEntity.getEntryDb());
        luw.set(PmLineEntity::getEntryOpcConnect, pmLineEntity.getEntryOpcConnect());
        luw.set(PmLineEntity::getStationCount, pmLineEntity.getStationCount());
        luw.set(PmLineEntity::getRunType, pmLineEntity.getRunType());
        luw.set(PmLineEntity::getMergeLine, pmLineEntity.getMergeLine());
        luw.set(PmLineEntity::getTpOnlinePoint, pmLineEntity.getTpOnlinePoint());
        luw.set(PmLineEntity::getTpOfflinePoint, pmLineEntity.getTpOfflinePoint());

        luw.set(PmLineEntity::getAttribute1, getAttribVal(pmLineEntity.getAttribute1()));
        luw.set(PmLineEntity::getAttribute2, getAttribVal(pmLineEntity.getAttribute2()));
        luw.set(PmLineEntity::getAttribute3, getAttribVal(pmLineEntity.getAttribute3()));
        luw.set(PmLineEntity::getAttribute4, getAttribVal(pmLineEntity.getAttribute4()));
        luw.set(PmLineEntity::getAttribute5, getAttribVal(pmLineEntity.getAttribute5()));

        luw.set(PmLineEntity::getAttribute6, getAttribVal(pmLineEntity.getAttribute6()));
        luw.set(PmLineEntity::getAttribute7, getAttribVal(pmLineEntity.getAttribute7()));
        luw.set(PmLineEntity::getAttribute8, getAttribVal(pmLineEntity.getAttribute8()));
        luw.set(PmLineEntity::getAttribute9, getAttribVal(pmLineEntity.getAttribute9()));
        luw.set(PmLineEntity::getAttribute10, getAttribVal(pmLineEntity.getAttribute10()));

        luw.eq(PmLineEntity::getId, pmLineEntity.getId());
        luw.eq(PmLineEntity::getVersion, pmLineEntity.getVersion());
        luw.eq(PmLineEntity::getPrcPmWorkshopId, pmLineEntity.getPrcPmWorkshopId());
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
        aviService.canDeleteLine(ids);
        workStationService.canDeleteLine(ids);
        super.delete(ids);
    }

    @Override
    public List<PmLineEntity> getByShopId(Long shopId) {
        return pmLineDao.selectList(Wrappers.lambdaQuery(PmLineEntity.class)
                .eq(PmLineEntity::getPrcPmWorkshopId, shopId)
                .eq(PmLineEntity::getVersion, 0)
                .eq(PmLineEntity::getIsDelete, false)
                .orderByAsc(PmLineEntity::getLineDisplayNo)
                .orderByAsc(PmLineEntity::getLineCode));
    }

    @Override
    public Map<String, String> getExcelHead() {
        Map<String, String> pmLineMap = new HashMap<>(11);
        pmLineMap.put("workshopCode", "区域代码");
        pmLineMap.put("lineCode", "线体代码");
        pmLineMap.put("lineName", "线体名称");
        pmLineMap.put("stationCount", "线体工位数");
        pmLineMap.put("stationLength", "线体工位长度(cm)");
        pmLineMap.put("lineType", "线体类型");
        pmLineMap.put("runType", "线体运行模式");
        pmLineMap.put("lineLength", "线体长度(cm)");
        pmLineMap.put("lineDesignJph", "线体JPH");
        pmLineMap.put("lineProductTime", "线体生产L/T(分钟)");
        pmLineMap.put("lineEnableFlag", "线体是否启用");
        pmLineMap.put("lineDeleteFlag", "线体是否删除");
        pmLineMap.put("opcConnect", "线体链接");
        pmLineMap.put("queueDb", "队列DB");
        pmLineMap.put("stationDb", "岗位DB");
        pmLineMap.put("andonOpcConnect", "安灯链接");
        pmLineMap.put("andonDb", "安灯DB");
        return pmLineMap;
    }

    @Override
    public List<PmLineEntity> getListByParentId(Long parentId) {
        QueryWrapper<PmLineEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmLineEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmLineEntity::getPrcPmWorkshopId, parentId);
        lambdaQueryWrapper.eq(PmLineEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmLineEntity::getVersion, 0);
        lambdaQueryWrapper.orderByAsc(PmLineEntity::getLineDisplayNo);
        return this.pmLineDao.selectList(queryWrapper);
    }

    @Override
    public List<PmLineEntity> getPmAreaEntityByVersion(Long shopId, int version, Boolean flags) {
        QueryWrapper<PmLineEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmLineEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmLineEntity::getPrcPmWorkshopId, shopId);
        lambdaQueryWrapper.eq(PmLineEntity::getVersion, version);
        lambdaQueryWrapper.eq(PmLineEntity::getIsEnable, true);
        lambdaQueryWrapper.eq(PmLineEntity::getIsDelete, flags);
        return pmLineDao.selectList(queryWrapper);
    }

    @Override
    public List<PmAllModel> getAllExcelDatas(String shopId) {
        return pmLineDao.getAllExcelDatas(shopId);
    }

    @Override
    public void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) throws Exception {
        List<PmLineEntity> listOfPmLine = this.convertExcelDataToEntity(listFromExcelSheet,
                mapSysConfigByCategory, sheetName);
        //设置外键
        setForeignKey(listOfPmLine, currentUnDeployData);
        //验证并保存
        verifyAndSaveEntity(listOfPmLine, currentUnDeployData);
    }

    @Override
    public void canDeleteWorkShop(Serializable[] shopIds) {
        QueryWrapper<PmLineEntity> qry = new QueryWrapper<>();
        qry.lambda().in(PmLineEntity::getPrcPmWorkshopId, shopIds);
        List<PmLineEntity> line = getData(qry, false);
        if (CollectionUtils.isNotEmpty(line)) {
            PmLineEntity pmLineEntity = line.stream().findFirst().orElse(null);
            if(Objects.nonNull(pmLineEntity)){
                throw new InkelinkException("车间下有线体，不允许删除!");
            }
        }
    }

    @Override
    public List<PmAviEntity> getAvis(Long pmshopId) {
        QueryWrapper<PmAviEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<PmAviEntity> lwq = qw.lambda();
        lwq.eq(PmAviEntity::getPrcPmWorkshopId,pmshopId);
        return this.aviService.getData(qw,false);
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


    private void setForeignKey(List<PmLineEntity> listOfPmLine, PmAllDTO pmAllDTO) {
        if (listOfPmLine.isEmpty()) {
            return;
        }
        for (PmLineEntity pmLineEntity : listOfPmLine) {
            setForeignKey(pmLineEntity, pmAllDTO);
        }
    }

    private void setForeignKey(PmLineEntity pmLineEntity, PmAllDTO pmAllDTO) {
        List<PmWorkShopEntity> shops = pmAllDTO.getShops();
        PmWorkShopEntity shop = shops.stream().filter(item -> Objects.equals(pmLineEntity.getWorkshopCode(), item.getWorkshopCode()))
                .findFirst().orElse(null);
        if (shop == null) {
            throw new InkelinkException("线体编码[" + pmLineEntity.getLineCode() + "]对应的车间编码[" + pmLineEntity.getWorkshopCode() + "]没有对应任何车间，请检查是否有配置对应编码车间");
        }
        pmLineEntity.setPrcPmWorkshopId(shop.getId());
    }

    private void verifyAndSaveEntity(List<PmLineEntity> listEntity,
                                     PmAllDTO currentUnDeployData) {
        if (listEntity == null || listEntity.isEmpty()) {
            return;
        }
        List<PmLineEntity> insertList = new ArrayList<>(listEntity.size());
        Map<Long, Set<String>> mapOfLineCodeByShopId = new HashMap(16);
        Map<Long, Set<String>> mapOfLineNameByShopId = new HashMap(16);
        for (PmLineEntity line : listEntity) {
            if(line.getIsDelete()){
                LambdaUpdateWrapper<PmLineEntity> luw = new LambdaUpdateWrapper();
                luw.set(PmLineEntity::getIsDelete, true);
                luw.eq(PmLineEntity::getLineCode, line.getLineCode());
                luw.eq(PmLineEntity::getVersion, 0);
                this.update(luw,false);
                continue;
            }
            ClassUtil.validNullByNullAnnotation(line);
            //验证线体编码
            verifyLineCode(line, mapOfLineCodeByShopId);
            //验证线体名称
            verifyLineName(line, mapOfLineNameByShopId);

            PmLineEntity existLine = currentUnDeployData.getLines().stream().filter(
                    item -> Objects.equals(item.getLineCode(), line.getLineCode())).findFirst().orElse(null);
            if (existLine != null) {
                PmLineEntity existSameCodeLine = currentUnDeployData.getLines().stream().filter(
                        item -> Objects.equals(item.getLineCode(), line.getLineCode())
                                && !Objects.equals(item.getId(), existLine.getId())).findFirst().orElse(null);
                if (existSameCodeLine != null) {
                    throw new InkelinkException("线体[" + line.getLineCode() + "]代码已经存在,线体编码全厂唯一");
                }
                LambdaUpdateWrapper<PmLineEntity> luw = new LambdaUpdateWrapper();
                luw.set(PmLineEntity::getLineName, line.getLineName());
                luw.set(PmLineEntity::getStationCount, line.getStationCount());
                luw.set(PmLineEntity::getStationLength, line.getStationLength());
                luw.set(PmLineEntity::getLineType, line.getLineType());
                luw.set(PmLineEntity::getRunType, line.getRunType());
                luw.set(PmLineEntity::getBeginDistance, line.getBeginDistance());
                luw.set(PmLineEntity::getEndDistance, line.getEndDistance());
                luw.set(PmLineEntity::getLineDesignJph, line.getLineDesignJph());
                luw.set(PmLineEntity::getProductTime, line.getProductTime());
                luw.set(PmLineEntity::getIsDelete, line.getIsDelete());
                luw.set(PmLineEntity::getIsEnable, line.getIsEnable());
                luw.set(PmLineEntity::getOpcConnect, line.getOpcConnect());
                luw.set(PmLineEntity::getQueueDb, line.getQueueDb());
                luw.set(PmLineEntity::getStationDb, line.getStationDb());
                luw.set(PmLineEntity::getAndonDb, line.getAndonDb());
                luw.set(PmLineEntity::getAndonOpcConnect, line.getAndonOpcConnect());

                luw.eq(PmLineEntity::getId, existLine.getId());
                luw.eq(PmLineEntity::getVersion, 0);
                this.update(luw,false);
            } else{
                line.setVersion(0);
                insertList.add(line);
                //this.insert(line);
            }
        }
        if(!insertList.isEmpty()){
            this.insertBatch(insertList,200,false,1);
        }

    }

    private void verifyLineCode(PmLineEntity line, Map<Long, Set<String>> mapOfLineCodeByShopId) {
        Set<String> setOfLineCode = mapOfLineCodeByShopId.computeIfAbsent(line.getPrcPmWorkshopId(), v -> new HashSet<>());
        if (setOfLineCode.contains(line.getLineCode())) {
            throw new InkelinkException("线体[" + line.getLineCode() + "]代码重复");
        }
        setOfLineCode.add(line.getLineCode());
    }

    private void verifyLineName(PmLineEntity line, Map<Long, Set<String>> mapOfLineNameByShopId) {
        Set<String> setOfLineName = mapOfLineNameByShopId.computeIfAbsent(line.getPrcPmWorkshopId(), v -> new HashSet<>());
        if (setOfLineName.contains(line.getLineName())) {
            throw new InkelinkException("线体[" + line.getLineCode() + "]名称[" + line.getLineName() + "]重复");
        }
        setOfLineName.add(line.getLineName());
    }

//    private void verifyLineDisplayNo(PmLineEntity line, Map<Long,Set<Integer>> mapOfLineDisplayNoByShopId){
//        Set<Integer> setOfLineDisplayNo = mapOfLineDisplayNoByShopId.computeIfAbsent(line.getPrcPmWorkshopId(),v -> new HashSet<>());
//        if(setOfLineDisplayNo.contains(line.getLineName())){
//            throw new InkelinkException("车间[" + line.getWorkshopCode() + "]>线体[" + line.getLineCode() + "]顺序号[" + line.getLineCode() + "]重复");
//        }
//        setOfLineDisplayNo.add(line.getLineDisplayNo());
//    }

    @Override
    protected boolean canBeNullOrEmpty(Map.Entry<String, String> eachColumnData) {
        //线体如果未设置 则设置为主线
        if (LINE_TYPE.equalsIgnoreCase(eachColumnData.getKey())
                && StringUtils.isBlank(eachColumnData.getValue())) {
            eachColumnData.setValue("主线");
        }
        return true;
    }

    @Override
    public PmLineEntity get(Serializable id) {
        QueryWrapper<PmLineEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmLineEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmLineEntity::getId, id);
        lambdaQueryWrapper.eq(PmLineEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmLineEntity::getVersion, 0);
        List<PmLineEntity> pmLineEntityList = pmLineDao.selectList(queryWrapper);
        return pmLineEntityList.stream().findFirst().orElse(null);
    }

    @Override
    public void beforeUpdate(PmLineEntity entity) {
        valid(entity);
    }

    @Override
    public void beforeInsert(PmLineEntity entity) {
        valid(entity);
    }

    private void valid(PmLineEntity entity) {
        QueryWrapper<PmLineEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmLineEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmLineEntity::getLineCode, entity.getLineCode());
        lambdaQueryWrapper.eq(PmLineEntity::getVersion, entity.getVersion());
        lambdaQueryWrapper.ne(PmLineEntity::getId, entity.getId());
        if (selectCount(queryWrapper) > 0) {
            throw new InkelinkException("代码" + entity.getLineCode() + "已存在,线体代码全厂唯一");
        }
    }


}