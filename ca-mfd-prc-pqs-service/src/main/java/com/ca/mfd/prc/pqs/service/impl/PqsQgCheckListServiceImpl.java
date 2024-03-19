package com.ca.mfd.prc.pqs.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyEntity;
import com.ca.mfd.prc.pqs.entity.PqsQgCheckListEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsQgCheckListMapper;
import com.ca.mfd.prc.pqs.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pqs.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pqs.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyService;
import com.ca.mfd.prc.pqs.service.IPqsQgCheckListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: QG必检项目服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsQgCheckListServiceImpl extends AbstractCrudServiceImpl<IPqsQgCheckListMapper, PqsQgCheckListEntity> implements IPqsQgCheckListService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_QG_CHECK_LIST";
    @Autowired
    private IPqsDefectAnomalyService pqsDefectAnomalyService;

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    private PmVersionProvider pmVersionProvider;

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsQgCheckListEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsQgCheckListEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsQgCheckListEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsQgCheckListEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsQgCheckListEntity entity) {
        if (entity.isDataCheck()) {
            validData(entity);
        }
        removeCache();
    }

    @Override
    public void beforeUpdate(PqsQgCheckListEntity entity) {
        if (entity.isDataCheck()) {
            validData(entity);
        }
        removeCache();
    }

    private void validData(PqsQgCheckListEntity entity) {
        if (StringUtils.isEmpty(entity.getModel())) {
            throw new InkelinkException("车型不能为空");
        }
        if (StringUtils.isEmpty(entity.getContent())) {
            throw new InkelinkException("检测内容不能为空");
        }
        if (!StringUtils.isEmpty(entity.getDefectAnomalyCode())) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("DEFECT_ANOMALY_CODE", entity.getDefectAnomalyCode(), ConditionOper.Equal));
            PqsDefectAnomalyEntity anomalyEntity = pqsDefectAnomalyService.getData(conditionInfos).stream().findFirst().orElse(null);
            if (anomalyEntity == null) {
                throw new InkelinkException("没有找到缺陷信息");
            }
        }
        if (!StringUtils.isEmpty(entity.getWorkstationCode())) {
            ResultVO<List<PmWorkStationEntity>> pse = new ResultVO<List<PmWorkStationEntity>>()
                    .ok(pmVersionProvider.getObjectedPm().getStations());
            if (pse == null || !pse.getSuccess()) {
                throw new InkelinkException("获工位信息失败。" + (pse == null ? "" : pse.getMessage()));
            }
            List<PmWorkStationEntity> workstationNames = pse.getData();
            workstationNames.stream().filter(w -> StringUtils.equals(w.getWorkstationCode(), String.valueOf(entity.getWorkstationCode())))
                    .findFirst().ifPresent(workstationList -> entity.setWorkstationName(workstationList.getWorkstationName()));
            if (workstationNames == null) {
                throw new InkelinkException("工位" + entity.getWorkstationCode() + "在建模中未找到");
            }
        }
        if (!StringUtils.isEmpty(entity.getModel())) {
            ResultVO<List<ComboInfoDTO>> rsp = new ResultVO<List<ComboInfoDTO>>().ok(sysConfigurationProvider.getComboDatas("vehicleModel"));
            if (rsp == null || !rsp.getSuccess()) {
                throw new InkelinkException("获取型号失败。" + (rsp == null ? "" : rsp.getMessage()));
            }
        }
    }

    /**
     * 获取所有的组件信息
     *
     * @return
     */
    @Override
    public List<PqsQgCheckListEntity> getAllDatas() {
        Function<Object, ? extends List<PqsQgCheckListEntity>> getDataFunc = (obj) -> {
            List<PqsQgCheckListEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsQgCheckListEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 导入前验证
     *
     * @param datas
     * @param fieldParam
     */
    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        String str = "model,workstationCode,content";
        validExcelDataUnique(datas, Arrays.asList(str.split(",")), "Excel中存在【车型、工位代码、检查内容】的重复行");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("model"), i + 1, data.get("model"), "");
            validExcelDataRequire(excelColumnNames.get("modelDescription"), i + 1, data.get("modelDescription"), "");
            validExcelDataRequire(excelColumnNames.get("workstationCode"), i + 1, data.get("workstationCode"), "");
            validExcelDataRequire(excelColumnNames.get("workstationName"), i + 1, data.get("workstationName"), "");
            validExcelDataRequire(excelColumnNames.get("group"), i + 1, data.get("group"), "");
            validExcelDataRequire(excelColumnNames.get("content"), i + 1, data.get("content"), "");
        }
    }

    @Override
    public void saveExcelData(List<PqsQgCheckListEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsQgCheckListEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsQgCheckListEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getModel().equals(a.getModel())
                    && u.getWorkstationCode().equals(a.getWorkstationCode())
                    && u.getContent().equals(a.getContent())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsQgCheckListEntity> insertList = entities.stream()
                .filter(e -> !updateList.contains(e)).collect(Collectors.toList());

        if (insertList.size() > 0) {
            insertBatch(insertList, insertList.size());
        }
        if (updateList.size() > 0) {
            updateBatchById(updateList, updateList.size());
        }
        removeCache();
        saveChange();
    }
}