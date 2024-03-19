package com.ca.mfd.prc.pqs.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.InkelinkExcelUtils;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.MaintainDataParaInfo;
import com.ca.mfd.prc.pqs.dto.WpAnomalyWorkStationListInfo;
import com.ca.mfd.prc.pqs.dto.WpAnomalyWorkstationParaInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyEntity;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyWpEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsDefectAnomalyWpMapper;
import com.ca.mfd.prc.pqs.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pqs.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyService;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyWpService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 常用缺陷服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsDefectAnomalyWpServiceImpl extends AbstractCrudServiceImpl<IPqsDefectAnomalyWpMapper, PqsDefectAnomalyWpEntity> implements IPqsDefectAnomalyWpService {

    private final Map<String, Map<String, String>> defectAnomalyWp = new HashMap<>();
    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private IPqsDefectAnomalyService pqsDefectAnomalyService;
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_DEFECT_ANOMALY_WP";

    {
        Map<String, String> mpStamping = new LinkedHashMap<>();
        mpStamping.put(MpSqlUtils.getColumnName(PqsDefectAnomalyWpEntity::getWorkstationCode), "工位代码");
        mpStamping.put(MpSqlUtils.getColumnName(PqsDefectAnomalyWpEntity::getWorkstationName), "工位名称");
        mpStamping.put(MpSqlUtils.getColumnName(PqsDefectAnomalyWpEntity::getDefectAnomalyCode), "组合代码");
        mpStamping.put(MpSqlUtils.getColumnName(PqsDefectAnomalyWpEntity::getDefectAnomalyDescription), "ICC缺陷");
        defectAnomalyWp.put("常用缺陷", mpStamping);

    }

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsDefectAnomalyWpEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsDefectAnomalyWpEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsDefectAnomalyWpEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsDefectAnomalyWpEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 维护数据
     *
     * @param info
     */
    @Override
    public void maintainData(MaintainDataParaInfo info) {

        ResultVO<List<PmWorkStationEntity>> pse = new ResultVO<List<PmWorkStationEntity>>()
                .ok(pmVersionProvider.getObjectedPm().getStations());
        if (pse == null || !pse.getSuccess()) {
            throw new InkelinkException("在建模中获取工位信息失败。" + (pse == null ? "" : pse.getMessage()));
        }
        PmWorkStationEntity workstationInfo = pse.getData().stream()
                .filter(p -> p.getWorkstationCode().equals(info.getWorkstationCode())).findFirst().orElse(null);
        if (workstationInfo == null) {
            throw new InkelinkException("工位" + info.getWorkstationCode() + "在建模中未找到");
        }

        // 删除旧数据
        List<ConditionDto> conditionInfos = Lists.newArrayList();
        conditionInfos.add(
                new ConditionDto("workstationCode", info.getWorkstationCode(), ConditionOper.Equal));
        delete(conditionInfos);

        List<PqsDefectAnomalyWpEntity> insertList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(info.getDefectAnomalyList())) {
            // 所有缺陷
            List<PqsDefectAnomalyEntity> allDefect = pqsDefectAnomalyService.getAllDatas();

            info.getDefectAnomalyList().forEach(i -> {
                PqsDefectAnomalyEntity defectInfo = allDefect.stream()
                        .filter(a -> a.getDefectAnomalyCode().equals(i.getDefectAnomalyCode())).findFirst().orElse(null);
                PqsDefectAnomalyWpEntity paw = new PqsDefectAnomalyWpEntity();
                paw.setWorkstationCode(info.getWorkstationCode());
                paw.setWorkstationName(workstationInfo.getWorkstationName());
                paw.setDefectAnomalyCode(i.getDefectAnomalyCode());
                paw.setDefectAnomalyDescription(defectInfo == null ? StringUtils.EMPTY : defectInfo.getDefectAnomalyDescription());
                paw.setShortCode(defectInfo == null ? StringUtils.EMPTY : defectInfo.getShortCode());
                insertList.add(paw);
            });
        } else {
            // 处理长安提的保存空缺陷
            PqsDefectAnomalyWpEntity paw = new PqsDefectAnomalyWpEntity();
            paw.setWorkstationCode(info.getWorkstationCode());
            paw.setWorkstationName(workstationInfo.getWorkstationName());
            insertList.add(paw);
        }
        insertBatch(insertList, insertList.size());
    }

    /**
     * 获取工位信息
     *
     * @param info
     * @return
     */
    @Override
    public PageData<WpAnomalyWorkStationListInfo> getWorkStationList(WpAnomalyWorkstationParaInfo info) {

        List<WpAnomalyWorkStationListInfo> list = Lists.newArrayList();
        PageData<WpAnomalyWorkStationListInfo> pageInfo = new PageData<>(new ArrayList<>(), 0);
        // 是否显示所有
        if (info.isShowAllWorkstation()) {
            ResultVO<List<PmWorkStationEntity>> pse = new ResultVO<List<PmWorkStationEntity>>()
                    .ok(pmVersionProvider.getObjectedPm().getStations());
            if (pse == null || !pse.getSuccess()) {
                throw new InkelinkException("PM模块主信息失败。" + (pse == null ? "" : pse.getMessage()));
            }

            List<PmWorkStationEntity> pageData;
            if (StringUtils.isNotEmpty(info.getWorkstationName())) {
                pageData = pse.getData().stream()
                        .filter(p -> p.getWorkstationName().contains(info.getWorkstationName()))
                        .collect(Collectors.toList());
            } else {
                pageData = pse.getData();
            }
            for (PmWorkStationEntity pm : pageData) {
                WpAnomalyWorkStationListInfo wpAnomalyWorkStationListInfo = new WpAnomalyWorkStationListInfo();
                wpAnomalyWorkStationListInfo.setWorkstationCode(pm.getWorkstationCode());
                wpAnomalyWorkStationListInfo.setWorkstationName(pm.getWorkstationName());
                list.add(wpAnomalyWorkStationListInfo);
            }
            pageInfo.setPageIndex(info.getPageIndex());
            pageInfo.setPageSize(info.getPageSize());
            pageInfo.setDatas(list);
            pageInfo.setTotal(pageData.size());
        } else {
            List<PqsDefectAnomalyWpEntity> pageData;
            if (StringUtils.isNotEmpty(info.getWorkstationName())) {
                pageData = getAllDatas().stream()
                        .filter(p -> p.getWorkstationName().contains(info.getWorkstationName()))
                        .collect(Collectors.toList());
            } else {
                pageData = getAllDatas();
            }

            for (PqsDefectAnomalyWpEntity record : pageData) {
                WpAnomalyWorkStationListInfo wpAnomalyWorkStationListInfo = new WpAnomalyWorkStationListInfo();
                wpAnomalyWorkStationListInfo.setWorkstationCode(record.getWorkstationCode());
                wpAnomalyWorkStationListInfo.setWorkstationName(record.getWorkstationName());
                list.add(wpAnomalyWorkStationListInfo);
            }
            list = list.stream().distinct().collect(Collectors.toList());
            pageInfo.setPageIndex(info.getPageIndex());
            pageInfo.setPageSize(info.getPageSize());
            pageInfo.setDatas(list);
            pageInfo.setTotal(list.size());
        }

        return pageInfo;
    }

    @Override
    public List<PqsDefectAnomalyWpEntity> getAllDatas() {
        Function<Object, ? extends List<PqsDefectAnomalyWpEntity>> getDataFunc = (obj) -> {
            List<PqsDefectAnomalyWpEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsDefectAnomalyWpEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 获取模板
     *
     * @param fileName
     * @param response
     * @throws IOException
     */
    @Override
    public void getImportTemplate(String fileName, HttpServletResponse response) throws IOException {
        super.setExcelColumnNames(defectAnomalyWp.get("常用缺陷"));
        super.getImportTemplate(fileName, response);
    }

    /**
     * 导出数据
     *
     * @param conditions
     * @param sorts
     * @param fileName
     * @param response
     * @throws IOException
     */
    @Override
    public void export(List<ConditionDto> conditions, List<SortDto> sorts, String fileName, HttpServletResponse response) throws IOException {
        super.setExcelColumnNames(defectAnomalyWp.get("常用缺陷"));
        Map<String, String> fieldParam = getExcelColumnNames();
        List<PqsDefectAnomalyWpEntity> dataLists = getData(null);
        List<Map<String, Object>> mapList = InkelinkExcelUtils.getListMap(dataLists);
        dealExcelDatas(mapList);
        InkelinkExcelUtils.exportByDc(fieldParam, mapList, fileName, response);
    }

    /**
     * 导入数据
     *
     * @param is
     * @throws Exception
     */
    @Override
    public void importExcel(InputStream is) throws Exception {
        Map<String, List<Map<String, String>>> datas = InkelinkExcelUtils.importExcel(is,
                defectAnomalyWp.keySet().toArray(new String[defectAnomalyWp.keySet().size()]));

        List<PqsDefectAnomalyWpEntity> entityList = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, String>>> item : datas.entrySet()) {
            if (item.getValue() == null || item.getValue().size() <= 0) {
                continue;
            }
            // 验证excel的数据
            validImportDatas(item.getValue(), defectAnomalyWp.get(item.getKey()));
            List<PqsDefectAnomalyWpEntity> pushData = convertExcelDataToEntity(item.getValue());

            List<String> importWorkstations = pushData.stream().map(PqsDefectAnomalyWpEntity::getWorkstationCode)
                    .distinct().collect(Collectors.toList());
            List<PmWorkStationEntity> pmWorkstations = pmVersionProvider.getObjectedPm().getStations()
                    .stream().filter(p -> importWorkstations.contains(p.getWorkstationCode())).collect(Collectors.toList());

            List<PqsDefectAnomalyEntity> alllDefect = pqsDefectAnomalyService.getAllDatas();

            for (PmWorkStationEntity pw : pmWorkstations) {
                for (PqsDefectAnomalyWpEntity data : pushData) {
                    if (pw.getWorkstationCode().equals(data.getWorkstationCode())) {
                        PqsDefectAnomalyEntity anomalyEntity = alllDefect.stream()
                                .filter(a -> StringUtils.equals(a.getDefectAnomalyCode(), data.getDefectAnomalyCode()))
                                .findFirst().orElse(new PqsDefectAnomalyEntity());
                        data.setDefectAnomalyCode(anomalyEntity.getDefectAnomalyCode());
                        data.setDefectAnomalyDescription(anomalyEntity.getDefectAnomalyDescription());
                        data.setShortCode(anomalyEntity.getShortCode());
                        data.setWorkstationCode(pw.getWorkstationCode());
                        data.setWorkstationName(pw.getWorkstationName());
                        entityList.add(data);

                        // 导入前删除重复的
                        UpdateWrapper<PqsDefectAnomalyWpEntity> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.lambda().eq(PqsDefectAnomalyWpEntity::getWorkstationCode, pw.getWorkstationCode());
                        delete(updateWrapper);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(entityList)) {
            super.saveExcelData(entityList);
        }
    }
}