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
import com.ca.mfd.prc.pqs.dto.BatchAddPositionInfo;
import com.ca.mfd.prc.pqs.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.pqs.dto.DefectShowInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectPositionEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsDefectPositionMapper;
import com.ca.mfd.prc.pqs.service.IPqsDefectPositionService;
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
 * @Description: 缺陷位置代码服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsDefectPositionServiceImpl extends AbstractCrudServiceImpl<IPqsDefectPositionMapper, PqsDefectPositionEntity> implements IPqsDefectPositionService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_DEFECT_POSITION";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsDefectPositionEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsDefectPositionEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsDefectPositionEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsDefectPositionEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsDefectPositionEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("DEFECT_POSITION_CODE", entity.getDefectPositionCode(), ConditionOper.Equal));
            PqsDefectPositionEntity positionEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (positionEntity != null) {
                throw new InkelinkException("编码" + positionEntity.getDefectPositionCode() + "已存在,请不要重复添加");
            }
        }
        validData(entity);
        super.beforeInsert(entity);
    }

    @Override
    public void beforeUpdate(PqsDefectPositionEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("DEFECT_POSITION_CODE", entity.getDefectPositionCode(), ConditionOper.Equal));
            conditionInfos.add(
                    new ConditionDto("id", entity.getId().toString(), ConditionOper.Unequal));
            PqsDefectPositionEntity positionEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (positionEntity != null) {
                throw new InkelinkException("编码" + positionEntity.getDefectPositionCode() + "已存在,请不要重复添加");
            }
        }
        validData(entity);
        super.beforeUpdate(entity);
    }

    private void validData(PqsDefectPositionEntity entity) {

        if (StringUtils.isEmpty(entity.getShortCode())) {
            entity.setShortCode(entity.getDefectPositionDescription());
        }
    }

    /**
     * 获取所有的位置信息
     *
     * @return
     */
    @Override
    public List<PqsDefectPositionEntity> getAllDatas() {
        Function<Object, ? extends List<PqsDefectPositionEntity>> getDataFunc = (obj) -> {
            List<PqsDefectPositionEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsDefectPositionEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 批量添加缺陷位置
     *
     * @param list
     */
    @Override
    public void batchAddPosition(List<BatchAddPositionInfo> list) {
        List<String> codes = list.stream().map(t -> t.getCode()).collect(Collectors.toList());
        /**
         * 批量查询数据库是否存在缺陷位置
         */
        List<PqsDefectPositionEntity> selectList = selectList(new QueryWrapper<PqsDefectPositionEntity>().
                in("DEFECT_POSITION_CODE", codes));
        for (BatchAddPositionInfo m : list) {
            PqsDefectPositionEntity mode = selectList.stream().filter(c -> c.getDefectPositionCode().equals(m.getCode())).findFirst().orElse(null);
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
                PqsDefectPositionEntity entity = new PqsDefectPositionEntity();
                entity.setId(IdGenerator.getId());
                entity.setGroupName(m.getGroupName());
                entity.setSubGroupName(m.getSubGroupName());
                entity.setDefectPositionCode(m.getCode());
                entity.setDefectPositionDescription(m.getDescription());
                save(entity);
            }
        }
    }

    /**
     * 批量传入需要删除的缺陷位置代码
     *
     * @param codeList
     */
    @Override
    public void batchDelByCodes(List<String> codeList) {
        List<Long> ids = selectList(new QueryWrapper<PqsDefectPositionEntity>().
                in("defectPositionCode", codeList)).stream().map(c -> c.getId()).collect(Collectors.toList());
        delete(ids.toArray(new String[ids.size()]));
    }


    @Override
    public List<DefectShowInfo> getPositionShowList(DefectFilterlParaInfo info) {
        List<ConditionDto> conditions = new ArrayList<ConditionDto>();
        ConditionDto conditionDto1 = new ConditionDto();
        conditionDto1.setColumnName("defectPositionCode");
        conditionDto1.setOperator(ConditionOper.AllLike);
        conditionDto1.setValue(info.getKey());
        conditionDto1.setRelation(ConditionRelation.Or);
        conditionDto1.setGroup("v");
        conditionDto1.setGroupRelation(ConditionRelation.Or);
        conditions.add(conditionDto1);

        ConditionDto conditionDto2 = new ConditionDto();
        conditionDto2.setColumnName("defectPositionDescription");
        conditionDto2.setOperator(ConditionOper.AllLike);
        conditionDto2.setValue(info.getKey());
        conditionDto2.setRelation(ConditionRelation.Or);
        conditionDto2.setGroup("v");
        conditionDto2.setGroupRelation(ConditionRelation.Or);
        conditions.add(conditionDto2);

        List<SortDto> sortInfos = new ArrayList<SortDto>();
        SortDto sortDto = new SortDto();
        sortDto.setColumnName("defectPositionCode");
        sortDto.setDirection(ConditionDirection.ASC);
        sortInfos.add(sortDto);
        PageDataDto pageDataDto = new PageDataDto();
        pageDataDto.setPageIndex(info.getPageIndex());
        pageDataDto.setPageSize(info.getPageSize());
        pageDataDto.setConditions(conditions);
        pageDataDto.setSorts(sortInfos);
        PageData<PqsDefectPositionEntity> pageInfo = page(pageDataDto);
        List<DefectShowInfo> exsitList = new ArrayList<DefectShowInfo>();
        if (!StringUtils.isEmpty(info.getExsitCodes())) {
            String[] exsitCodes = info.getExsitCodes().split(",");
            if (exsitCodes.length > 0) {
                exsitList = selectList(new QueryWrapper<PqsDefectPositionEntity>().
                        in("DEFECT_POSITION_CODE", exsitCodes)
                        .orderByAsc("DEFECT_POSITION_CODE")).stream().map(t -> {
                    DefectShowInfo defectShowInfo = new DefectShowInfo();
                    defectShowInfo.setId(t.getId());
                    defectShowInfo.setCode(t.getDefectPositionCode());
                    defectShowInfo.setDescription(t.getDefectPositionDescription());
                    return defectShowInfo;
                }).collect(Collectors.toList());
            }
        }
        return (List<DefectShowInfo>) CollectionUtils.union(exsitList,
                pageInfo.getDatas().stream().map(t -> {
                    DefectShowInfo defectShowInfo = new DefectShowInfo();
                    defectShowInfo.setId(t.getId());
                    defectShowInfo.setCode(t.getDefectPositionCode());
                    defectShowInfo.setDescription(t.getDefectPositionDescription());
                    return defectShowInfo;
                }).collect(Collectors.toList()));
    }


    /*@Override
    public Map<String, String> getExcelColumnNames() {
        Map<String, String> columnNames = new HashMap<String, String>(5);
        columnNames.put("groupName", "缺陷组件组");
        columnNames.put("subGroupName", "缺陷子组件组");
        columnNames.put("defectPositionCode", "组件代码");
        columnNames.put("defectPositionDescription", "缺陷组件名称");
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
        validExcelDataUnique(datas, "defectPositionCode", "Excel中缺陷位置代码重复");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("groupName"), i + 1, data.get("groupName"), "");
            validExcelDataRequire(excelColumnNames.get("subGroupName"), i + 1, data.get("subGroupName"), "");
            validExcelDataRequire(excelColumnNames.get("defectPositionCode"), i + 1, data.get("defectPositionCode"), "");
            validExcelDataRequire(excelColumnNames.get("defectPositionDescription"), i + 1, data.get("defectPositionDescription"), "");
        }
    }

    /**
     * excel导入保存数据
     *
     * @param entities
     */
    @Override
    public void saveExcelData(List<PqsDefectPositionEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsDefectPositionEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsDefectPositionEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getDefectPositionCode().equals(a.getDefectPositionCode())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsDefectPositionEntity> insertList = entities.stream()
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