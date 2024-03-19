package com.ca.mfd.prc.pqs.service.impl;

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
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.BatchAddComponentInfo;
import com.ca.mfd.prc.pqs.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.pqs.dto.DefectShowInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectComponentEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsDefectComponentMapper;
import com.ca.mfd.prc.pqs.remote.app.core.provider.SysSequenceNumberProvider;
import com.ca.mfd.prc.pqs.service.IPqsDefectComponentService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
 * @Description: 组件代码服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsDefectComponentServiceImpl extends AbstractCrudServiceImpl<IPqsDefectComponentMapper, PqsDefectComponentEntity> implements IPqsDefectComponentService {
    @Autowired
    private SysSequenceNumberProvider sysSequenceNumberProvider;
    private final String cacheName = "PRC_PQS_DEFECT_COMPONENT";
    @Autowired
    private LocalCache localCache;

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsDefectComponentEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsDefectComponentEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsDefectComponentEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsDefectComponentEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsDefectComponentEntity entity) {
        if (StringUtils.isEmpty(entity.getDefectComponentCode())) {
            ResultVO<String> rsp = new ResultVO<String>().ok(sysSequenceNumberProvider.getSeqNumWithTransaction("SN_ComponentCode"));
            if (rsp == null || !rsp.getSuccess()) {
                throw new InkelinkException("获取流水号失败。" + (rsp == null ? "" : rsp.getMessage()));
            }
            entity.setDefectComponentCode(rsp.getData());
        }
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("DEFECT_COMPONENT_CODE", entity.getDefectComponentCode(), ConditionOper.Equal));
            PqsDefectComponentEntity componentEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (componentEntity != null) {
                throw new InkelinkException("编码" + componentEntity.getDefectComponentCode() + "已存在,请不要重复添加");
            }
        }

        validData(entity);
    }

    @Override
    public void beforeUpdate(PqsDefectComponentEntity entity) {
        if (entity.isDataCheck()) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("DEFECT_COMPONENT_CODE", entity.getDefectComponentCode(), ConditionOper.Equal));
            conditionInfos.add(
                    new ConditionDto("id", entity.getId().toString(), ConditionOper.Unequal));
            PqsDefectComponentEntity componentEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (componentEntity != null) {
                throw new InkelinkException("编码" + componentEntity.getDefectComponentCode() + "已存在,请不要重复添加");
            }
        }
        validData(entity);
        super.beforeUpdate(entity);
    }

    private void validData(PqsDefectComponentEntity entity) {

        if (StringUtils.isBlank(entity.getShortCode())) {
            entity.setShortCode(entity.getDefectComponentDescription());
        }
    }

    /**
     * 获取所有的组件信息
     *
     * @return
     */
    @Override
    public List<PqsDefectComponentEntity> getAllDatas() {
        Function<Object, ? extends List<PqsDefectComponentEntity>> getDataFunc = (obj) -> {
            List<PqsDefectComponentEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsDefectComponentEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
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
    public void batchAddComponent(List<BatchAddComponentInfo> list) {
        List<String> codes = list.stream().map(t -> t.getCode()).collect(Collectors.toList());
        /**
         * 批量查询数据库是否存在缺陷位置
         */
        List<PqsDefectComponentEntity> selectList = selectList(new QueryWrapper<PqsDefectComponentEntity>().
                in("DEFECT_COMPONENT_CODE", codes));
        for (BatchAddComponentInfo m : list) {
            PqsDefectComponentEntity mode = selectList.stream().filter(c -> c.getDefectComponentCode().equals(m.getCode())).findFirst().orElse(null);
            if (mode != null) {
                /**
                 * 更新
                 */
                mode.setGroupName(m.getGroupName());
                mode.setSubGroupName(m.getSubGroupName());
                //TODO 位置字段没有了
                //mode.setPosition(m.getPosition());
                mode.setSource(1);
                update(mode);
            } else {
                /**
                 * 添加
                 */
                PqsDefectComponentEntity entity = new PqsDefectComponentEntity();
                entity.setId(IdGenerator.getId());
                entity.setGroupName(m.getGroupName());
                entity.setSubGroupName(m.getSubGroupName());
                entity.setDefectComponentCode(m.getCode());
                entity.setDefectComponentDescription(m.getDescription());
                //TODO 位置字段没有了
                //entity.setPosition(m.getPosition());
                save(entity);
            }
        }
    }

    /**
     * 批量传入需要删除的组件代码
     *
     * @param codeList
     */
    @Override
    public void batchDelByCodes(List<String> codeList) {
        List<Long> ids = selectList(new QueryWrapper<PqsDefectComponentEntity>().
                in("DEFECT_COMPONENT_CODE", codeList)).stream().map(c -> c.getId()).collect(Collectors.toList());
        delete(ids.toArray(new String[ids.size()]));
    }


    @Override
    public List<DefectShowInfo> getComponentShowList(DefectFilterlParaInfo info) {
        List<ConditionDto> conditions = new ArrayList<ConditionDto>();
        ConditionDto conditionDto1 = new ConditionDto();
        conditionDto1.setColumnName("defectComponentCode");
        conditionDto1.setOperator(ConditionOper.AllLike);
        conditionDto1.setValue(info.getKey());
        conditionDto1.setRelation(ConditionRelation.Or);
        conditionDto1.setGroup("v");
        conditionDto1.setGroupRelation(ConditionRelation.Or);
        conditions.add(conditionDto1);

        ConditionDto conditionDto2 = new ConditionDto();
        conditionDto2.setColumnName("defectComponentDescription");
        conditionDto2.setOperator(ConditionOper.AllLike);
        conditionDto2.setValue(info.getKey());
        conditionDto2.setRelation(ConditionRelation.Or);
        conditionDto2.setGroup("v");
        conditionDto2.setGroupRelation(ConditionRelation.Or);
        conditions.add(conditionDto2);

        List<SortDto> sortInfos = new ArrayList<SortDto>();
        SortDto sortDto = new SortDto();
        sortDto.setColumnName("defectComponentCode");
        sortDto.setDirection(ConditionDirection.ASC);
        sortInfos.add(sortDto);
        PageDataDto pageDataDto = new PageDataDto();
        pageDataDto.setPageIndex(info.getPageIndex());
        pageDataDto.setPageSize(info.getPageSize());
        pageDataDto.setConditions(conditions);
        pageDataDto.setSorts(sortInfos);
        PageData<PqsDefectComponentEntity> pageInfo = page(pageDataDto);
        List<DefectShowInfo> exsitList = new ArrayList<DefectShowInfo>();
        if (!com.alibaba.druid.util.StringUtils.isEmpty(info.getExsitCodes())) {
            String[] exsitCodes = info.getExsitCodes().split(",");
            if (exsitCodes.length > 0) {
                exsitList = selectList(new QueryWrapper<PqsDefectComponentEntity>().
                        in("DEFECT_COMPONENT_CODE", exsitCodes)
                        .orderByAsc("DEFECT_COMPONENT_CODE")).stream().map(t -> {
                    DefectShowInfo defectShowInfo = new DefectShowInfo();
                    defectShowInfo.setId(t.getId());
                    defectShowInfo.setCode(t.getDefectComponentCode());
                    defectShowInfo.setDescription(t.getDefectComponentDescription());
                    return defectShowInfo;
                }).collect(Collectors.toList());
            }
        }
        return (List<DefectShowInfo>) CollectionUtils.union(exsitList,
                pageInfo.getDatas().stream().map(t -> {
                    DefectShowInfo defectShowInfo = new DefectShowInfo();
                    defectShowInfo.setId(t.getId());
                    defectShowInfo.setCode(t.getDefectComponentCode());
                    defectShowInfo.setDescription(t.getDefectComponentDescription());
                    return defectShowInfo;
                }).collect(Collectors.toList()));
    }

    /*@Override
    public Map<String, String> getExcelColumnNames() {
        Map<String, String> columnNames = new HashMap<String, String>(5);
        columnNames.put("groupName", "缺陷组件组");
        columnNames.put("subGroupName", "缺陷子组件组");
        columnNames.put("defectComponentCode", "组件代码");
        columnNames.put("defectComponentDescription", "缺陷组件名称");
        columnNames.put("shortCode", "拼音简码");
        return columnNames;
    }*/

    /**
     * ss
     * 导入前验证
     *
     * @param datas
     * @param fieldParam
     */
    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        validExcelDataUnique(datas, "defectComponentCode", "Excel中组件代码重复");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("groupName"), i + 1, data.get("groupName"), "");
            validExcelDataRequire(excelColumnNames.get("subGroupName"), i + 1, data.get("subGroupName"), "");
            validExcelDataRequire(excelColumnNames.get("defectComponentCode"), i + 1, data.get("defectComponentCode"), "");
            validExcelDataRequire(excelColumnNames.get("defectComponentDescription"), i + 1, data.get("defectComponentDescription"), "");
        }
    }

    @Override
    public void saveExcelData(List<PqsDefectComponentEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsDefectComponentEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsDefectComponentEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getDefectComponentCode().equals(a.getDefectComponentCode())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsDefectComponentEntity> insertList = entities.stream()
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