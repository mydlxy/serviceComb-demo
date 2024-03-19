package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.pqs.entity.PqsInspectPolicyEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsInspectPolicyMapper;
import com.ca.mfd.prc.pqs.service.IPqsInspectPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @Description: 检验策略服务实现
 * @author inkelink
 * @date 2023年11月02日
 * @变更说明 BY inkelink At 2023年11月02日
 */
@Service
public class PqsInspectPolicyServiceImpl extends AbstractCrudServiceImpl<IPqsInspectPolicyMapper, PqsInspectPolicyEntity> implements IPqsInspectPolicyService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_INSPECT_POLICY";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsInspectPolicyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsInspectPolicyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsInspectPolicyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsInspectPolicyEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsInspectPolicyEntity entity) {
        if (entity.isDataCheck()) {
            QueryWrapper<PqsInspectPolicyEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsInspectPolicyEntity::getTriggerPolicyDesc, entity.getTriggerPolicyDesc());
            PqsInspectPolicyEntity inspectPolicyEntity = getData(queryWrapper, false).stream().findFirst().orElse(null);
            if (inspectPolicyEntity != null) {
                throw new InkelinkException("抽检策略已存在,请不要重复添加");
            }
        }
        removeCache();
    }

    @Override
    public void beforeUpdate(PqsInspectPolicyEntity entity) {
        if (entity.isDataCheck()) {
            QueryWrapper<PqsInspectPolicyEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsInspectPolicyEntity::getTriggerPolicyDesc, entity.getTriggerPolicyDesc())
                    .ne(PqsInspectPolicyEntity::getId, entity.getId());
            PqsInspectPolicyEntity inspectPolicyEntity = getData(queryWrapper, false).stream().findFirst().orElse(null);
            if (inspectPolicyEntity != null) {
                throw new InkelinkException("抽检策略已存在,请不要重复添加");
            }
        }
        removeCache();
    }

    /**
     * 从缓存中获取检验模板信息
     *
     * @return 检验模板列表
     */
    @Override
    public List<PqsInspectPolicyEntity> getAllDatas() {
        Function<Object, ? extends List<PqsInspectPolicyEntity>> getDataFunc = (obj) -> {
            List<PqsInspectPolicyEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsInspectPolicyEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 数据导入前验证
     *
     * @param datas
     * @param fieldParam
     */
    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        List<String> asList = Arrays.asList("triggerPolicyDesc".split(","));
        validExcelDataUnique(datas, asList, "Excel中抽检策略重复");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("triggerPolicyDesc"), i + 1, data.get("triggerPolicyDesc"), "");

            if (data.containsKey("triggerType") && data.getOrDefault("triggerType", null) != null) {
                switch (String.valueOf(data.get("triggerType"))) {
                    case "普通/定时":
                        data.put("triggerType", "1");
                        break;
                    case "设备触发":
                        data.put("triggerType", "2");
                        break;
                    case "模具更换":
                        data.put("triggerType", "3");
                        break;
                    default:
                        break;
                }
            }
            if (data.containsKey("periodType") && data.getOrDefault("periodType", null) != null) {
                switch (String.valueOf(data.get("periodType"))) {
                    case "分钟":
                        data.put("periodType", "0");
                        break;
                    case "小时":
                        data.put("periodType", "1");
                        break;
                    case "天":
                        data.put("periodType", "2");
                        break;
                    case "周":
                        data.put("periodType", "3");
                        break;
                    case "月":
                        data.put("periodType", "4");
                        break;
                    case "季度":
                        data.put("periodType", "5");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * excel数据导入入库
     *
     * @param entities
     */
    @Override
    public void saveExcelData(List<PqsInspectPolicyEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsInspectPolicyEntity> allDataLists = getAllDatas();
        // 需要更新的数据 "clxh", "cpggh", "ggpc"
        List<PqsInspectPolicyEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getTriggerPolicyDesc().equals(a.getTriggerPolicyDesc())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsInspectPolicyEntity> insertList = entities.stream()
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

    /**
     * 导出数据转换
     *
     * @param datas
     */
    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            if (data.containsKey("triggerType") && data.getOrDefault("triggerType", null) != null) {
                switch (String.valueOf(data.get("triggerType"))) {
                    case "1":
                        data.put("triggerType", "普通/定时");
                        break;
                    case "2":
                        data.put("triggerType", "设备触发");
                        break;
                    case "3":
                        data.put("triggerType", "模具更换");
                        break;
                    default:
                        break;
                }
            }
            if (data.containsKey("periodType") && data.getOrDefault("periodType", null) != null) {
                switch (String.valueOf(data.get("periodType"))) {
                    case "0":
                        data.put("periodType", "分钟");
                        break;
                    case "1":
                        data.put("periodType", "小时");
                        break;
                    case "2":
                        data.put("periodType", "天");
                        break;
                    case "3":
                        data.put("periodType", "周");
                        break;
                    case "4":
                        data.put("periodType", "月");
                        break;
                    case "5":
                        data.put("periodType", "季度");
                        break;
                    default:
                        break;
                }
            }
            // 导出时间处理
            if (data.containsKey("startDt") && data.getOrDefault("startDt", null) != null) {
                data.put("startDt", DateUtils.format((Date) data.get("startDt"), DateUtils.DATE_TIME_PATTERN));
            }
        }
    }
}