package com.ca.mfd.prc.pqs.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.pqs.dto.BatchAddCodeInfo;
import com.ca.mfd.prc.pqs.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.pqs.dto.DefectShowInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectCodeEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsDefectCodeMapper;
import com.ca.mfd.prc.pqs.service.IPqsDefectCodeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 缺陷代码服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsDefectCodeServiceImpl extends AbstractCrudServiceImpl<IPqsDefectCodeMapper, PqsDefectCodeEntity> implements IPqsDefectCodeService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_DEFECT_CODE";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsDefectCodeEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsDefectCodeEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsDefectCodeEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsDefectCodeEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsDefectCodeEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("DEFECT_CODE_CODE", entity.getDefectCodeCode(), ConditionOper.Equal));
            PqsDefectCodeEntity codeEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (codeEntity != null) {
                throw new InkelinkException("编码" + codeEntity.getDefectCodeCode() + "已存在,请不要重复添加");
            }
        }
        validData(entity);
        super.beforeInsert(entity);
    }

    @Override
    public void beforeUpdate(PqsDefectCodeEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("DEFECT_CODE_CODE", entity.getDefectCodeCode(), ConditionOper.Equal));
            conditionInfos.add(
                    new ConditionDto("id", entity.getId().toString(), ConditionOper.Unequal));
            PqsDefectCodeEntity codeEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (codeEntity != null) {
                throw new InkelinkException("编码" + codeEntity.getDefectCodeCode() + "已存在,请不要重复添加");
            }
        }
        validData(entity);
        super.beforeUpdate(entity);
    }

    private void validData(PqsDefectCodeEntity entity) {

        if (StringUtils.isEmpty(entity.getShortCode())) {
            entity.setShortCode(entity.getDefectCodeDescription());
        }
    }

    /**
     * 获取所有的缺陷信息
     *
     * @return
     */
    @Override
    public List<PqsDefectCodeEntity> getAllDatas() {
        Function<Object, ? extends List<PqsDefectCodeEntity>> getDataFunc = (obj) -> {
            List<PqsDefectCodeEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsDefectCodeEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 批量添加缺陷分类
     *
     * @param list
     */
    @Override
    public void batchAddCode(List<BatchAddCodeInfo> list) {
        List<String> codes = list.stream().map(t -> t.getCode()).collect(Collectors.toList());
        /**
         * 批量查询数据库是否存在缺陷位置
         */
        List<PqsDefectCodeEntity> selectList = selectList(new QueryWrapper<PqsDefectCodeEntity>().
                in("CODE", codes));
        for (BatchAddCodeInfo m : list) {
            PqsDefectCodeEntity mode = selectList.stream().filter(c -> c.getDefectCodeCode().equals(m.getCode())).findFirst().orElse(null);
            if (mode != null) {
                /**
                 * 更新
                 */
                mode.setGroupName(m.getGroupName());
                mode.setSubGroupName(m.getSubGroupName());
                update(mode);
            } else {
                /**
                 * 添加
                 */
                PqsDefectCodeEntity entity = new PqsDefectCodeEntity();
                entity.setId(IdGenerator.getId());
                entity.setGroupName(m.getGroupName());
                entity.setSubGroupName(m.getSubGroupName());
                entity.setDefectCodeCode(m.getCode());
                entity.setDefectCodeDescription(m.getDescription());
                save(entity);
            }
        }
    }

    @Override
    public void batchDelByCodes(List<String> codeList) {
        List<Long> ids = selectList(new QueryWrapper<PqsDefectCodeEntity>().
                in("defectCodeCode", codeList)).stream().map(c -> c.getId()).collect(Collectors.toList());
        delete(ids.toArray(new String[ids.size()]));
    }

    /**
     * 获取缺陷分类数据展示
     *
     * @param info
     * @return
     */
    @Override
    public List<DefectShowInfo> getCodeShowList(DefectFilterlParaInfo info) {
        List<ConditionDto> conditions = new ArrayList<ConditionDto>();
        ConditionDto conditionDto1 = new ConditionDto();
        conditionDto1.setColumnName("defectCodeCode");
        conditionDto1.setOperator(ConditionOper.AllLike);
        conditionDto1.setValue(info.getKey());
        conditionDto1.setRelation(ConditionRelation.Or);
        conditionDto1.setGroup("v");
        conditionDto1.setGroupRelation(ConditionRelation.Or);
        conditions.add(conditionDto1);

        ConditionDto conditionDto2 = new ConditionDto();
        conditionDto2.setColumnName("defectCodeDescription");
        conditionDto2.setOperator(ConditionOper.AllLike);
        conditionDto2.setValue(info.getKey());
        conditionDto2.setRelation(ConditionRelation.Or);
        conditionDto2.setGroup("v");
        conditionDto2.setGroupRelation(ConditionRelation.Or);
        conditions.add(conditionDto2);

        List<SortDto> sortInfos = new ArrayList<SortDto>();
        SortDto sortDto = new SortDto();
        sortDto.setColumnName("defectCodeCode");
        sortDto.setDirection(ConditionDirection.ASC);
        sortInfos.add(sortDto);
        PageDataDto pageDataDto = new PageDataDto();
        pageDataDto.setPageIndex(info.getPageIndex());
        pageDataDto.setPageSize(info.getPageSize());
        pageDataDto.setConditions(conditions);
        pageDataDto.setSorts(sortInfos);
        PageData<PqsDefectCodeEntity> pageInfo = page(pageDataDto);
        List<DefectShowInfo> exsitList = new ArrayList<DefectShowInfo>();
        if (!StringUtils.isEmpty(info.getExsitCodes())) {
            String[] exsitCodes = info.getExsitCodes().split(",");
            if (exsitCodes.length > 0) {
                exsitList = selectList(new QueryWrapper<PqsDefectCodeEntity>().
                        in("DEFECT_CODE_CODE", exsitCodes)
                        .orderByAsc("DEFECT_CODE_CODE")).stream().map(t -> {
                    DefectShowInfo defectShowInfo = new DefectShowInfo();
                    defectShowInfo.setId(t.getId());
                    defectShowInfo.setCode(t.getDefectCodeCode());
                    defectShowInfo.setDescription(t.getDefectCodeDescription());
                    return defectShowInfo;
                }).collect(Collectors.toList());
            }
        }
        return (List<DefectShowInfo>) CollectionUtils.union(exsitList,
                pageInfo.getDatas().stream().map(t -> {
                    DefectShowInfo defectShowInfo = new DefectShowInfo();
                    defectShowInfo.setId(t.getId());
                    defectShowInfo.setCode(t.getDefectCodeCode());
                    defectShowInfo.setDescription(t.getDefectCodeDescription());
                    return defectShowInfo;
                }).collect(Collectors.toList()));
    }

    /*@Override
    public Map<String, String> getExcelColumnNames() {
        Map<String, String> columnNames = new HashMap<String, String>(5);
        columnNames.put("groupName", "缺陷组件组");
        columnNames.put("subGroupName", "缺陷子组件组");
        columnNames.put("defectCodeCode", "组件代码");
        columnNames.put("defectCodeDescription", "缺陷组件名称");
        columnNames.put("shortCode", "拼音简码");
        return columnNames;
    }*/

    /**
     * 导入前验证
     *
     * @param datas
     * @param fieldParam
     */
    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        validExcelDataUnique(datas, "defectCodeCode", "Excel中代码重复");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("groupName"), i + 1, data.get("groupName"), "");
            validExcelDataRequire(excelColumnNames.get("subGroupName"), i + 1, data.get("subGroupName"), "");
            validExcelDataRequire(excelColumnNames.get("defectCodeCode"), i + 1, data.get("defectCodeCode"), "");
            validExcelDataRequire(excelColumnNames.get("defectCodeDescription"), i + 1, data.get("defectCodeDescription"), "");
        }
    }

    /**
     * excel导入保存数据
     *
     * @param entities
     */
    @Override
    public void saveExcelData(List<PqsDefectCodeEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsDefectCodeEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsDefectCodeEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getDefectCodeCode().equals(a.getDefectCodeCode())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsDefectCodeEntity> insertList = entities.stream()
                .filter(e -> !updateList.contains(e)).collect(Collectors.toList());

        if (insertList.size() > 0) {
            super.insertBatch(insertList);
        }
        if (updateList.size() > 0) {
            super.updateBatchById(updateList);
        }
        removeCache();
        saveChange();
    }

}