package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.ca.mfd.prc.pqs.dto.BatchAddAnomalyInfo;
import com.ca.mfd.prc.pqs.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.pqs.dto.DefectShowInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyEntity;
import com.ca.mfd.prc.pqs.entity.PqsDeptEntity;
import com.ca.mfd.prc.pqs.entity.PqsGradeEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsDefectAnomalyMapper;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyService;
import com.ca.mfd.prc.pqs.service.IPqsDefectCodeService;
import com.ca.mfd.prc.pqs.service.IPqsDefectComponentService;
import com.ca.mfd.prc.pqs.service.IPqsDefectPositionService;
import com.ca.mfd.prc.pqs.service.IPqsDeptService;
import com.ca.mfd.prc.pqs.service.IPqsGradeService;
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
 * @Description: 组合缺陷库服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsDefectAnomalyServiceImpl extends AbstractCrudServiceImpl<IPqsDefectAnomalyMapper, PqsDefectAnomalyEntity> implements IPqsDefectAnomalyService {
    @Autowired
    private LocalCache localCache;
    @Autowired
    private IPqsDefectComponentService pqsDefectComponentService;
    @Autowired
    private IPqsDefectPositionService pqsDefectPositionService;
    @Autowired
    private IPqsDefectCodeService pqsDefectCodeService;
    @Autowired
    private IPqsGradeService pqsGradeService;
    @Autowired
    private IPqsDeptService pqsDeptService;
    private final String cacheName = "PRC_PQS_DEFECT_ANOMALY";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsDefectAnomalyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsDefectAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsDefectAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsDefectAnomalyEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PqsDefectAnomalyEntity entity) {
        if (entity.isDataCheck()) {
            validData(entity);
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("DEFECT_ANOMALY_CODE", entity.getDefectAnomalyCode(), ConditionOper.Equal));
            PqsDefectAnomalyEntity anomalyEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (anomalyEntity != null) {
                throw new InkelinkException("编码" + anomalyEntity.getDefectAnomalyCode() + "已存在,请不要重复添加");
            }
        }
        removeCache();
    }

    @Override
    public void beforeUpdate(PqsDefectAnomalyEntity entity) {
        if (entity.isDataCheck()) {
            validData(entity);
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(
                    new ConditionDto("DEFECT_ANOMALY_CODE", entity.getDefectAnomalyCode(), ConditionOper.Equal));
            conditionInfos.add(
                    new ConditionDto("id", entity.getId().toString(), ConditionOper.Unequal));
            PqsDefectAnomalyEntity anomalyEntity = getData(conditionInfos).stream().findFirst().orElse(null);
            if (anomalyEntity != null) {
                throw new InkelinkException("编码" + anomalyEntity.getDefectAnomalyCode() + "已存在,请不要重复添加");
            }
        }
        removeCache();
    }

    private void validData(PqsDefectAnomalyEntity model) {
        List<ConditionDto> conditionInfos = new ArrayList<>();
        /*conditionInfos.add(
                new ConditionDto("DEFECT_COMPONENT_CODE", model.getComponentCode(), ConditionOper.Equal));
        PqsDefectComponentEntity component = pqsDefectComponentService.getData(conditionInfos).stream().findFirst().orElse(null);
        if (component == null) {
            throw new InkelinkException("没有找到组件数据");
        }
        conditionInfos.clear();
        conditionInfos.add(
                new ConditionDto("DEFECT_POSITION_CODE", model.getPositionCode(), ConditionOper.Equal));
        PqsDefectPositionEntity position = pqsDefectPositionService.getData(conditionInfos).stream().findFirst().orElse(null);
        if (position == null) {
            throw new InkelinkException("没有找到方位数据");
        }
        conditionInfos.clear();
        conditionInfos.add(
                new ConditionDto("DEFECT_CODE_CODE", model.getDefectCodeCode(), ConditionOper.Equal));
        PqsDefectCodeEntity code = pqsDefectCodeService.getData(conditionInfos).stream().findFirst().orElse(null);
        if (code == null) {
            throw new InkelinkException("没有找到缺陷代码数据");
        }
        conditionInfos.clear();*/
        if (StringUtils.isNotEmpty(model.getGradeCode())) {
            conditionInfos.add(
                    new ConditionDto("GRADE_CODE", model.getGradeCode(), ConditionOper.Equal));
            PqsGradeEntity gradeEntity = pqsGradeService.getData(conditionInfos).stream().findFirst().orElse(null);
            if (gradeEntity == null) {
                throw new InkelinkException("没有找到缺陷等级代码数据");
            }
            conditionInfos.clear();
            // 缺陷等级名称
            model.setGradeName(gradeEntity.getGradeName());
        }
        if (StringUtils.isNotEmpty(model.getResponsibleDeptCode())) {
            conditionInfos.add(
                    new ConditionDto("DEPT_CODE", model.getResponsibleDeptCode(), ConditionOper.Equal));
            PqsDeptEntity deptEntity = pqsDeptService.getData(conditionInfos).stream().findFirst().orElse(null);
            if (deptEntity == null) {
                throw new InkelinkException("没有找到责任部门代码数据");
            }
            // 责任部门名称
            model.setResponsibleDeptName(deptEntity.getDeptName());
        }
        /*// 组件名称
        model.setComponentDescription(component.getDefectComponentDescription());
        // 位置名称
        model.setPositionDescription(position.getDefectPositionDescription());
        // 缺陷名称
        model.setDefectCodeDescription(code.getDefectCodeDescription());
        // 组合代码
        model.setDefectAnomalyCode(component.getDefectComponentCode()
                + position.getDefectPositionCode() + code.getDefectCodeCode());
        // ICC缺陷
        model.setDefectAnomalyDescription(component.getDefectComponentDescription()
                + position.getDefectPositionDescription() + code.getDefectCodeDescription());
        // 拼音简码
        if (StringUtils.isEmpty(model.getShortCode())) {ss
            model.setShortCode(component.getShortCode()+"-"+position.getShortCode()+"-"+code.getShortCode());
        }*/
    }

    /**
     * 批量添加缺陷数据
     *
     * @param list
     */
    @Override
    public void batchAddAnomaly(List<BatchAddAnomalyInfo> list) {
        List<String> codes = list.stream().map(t -> t.getCode()).collect(Collectors.toList());
        /**
         * 批量查询数据库是否存在缺陷位置
         */
        List<PqsDefectAnomalyEntity> selectList = selectList(new QueryWrapper<PqsDefectAnomalyEntity>().
                in("CODE", codes));
        for (BatchAddAnomalyInfo m : list) {
            PqsDefectAnomalyEntity mode = selectList.stream().filter(c -> c.getComponentCode().equals(m.getCode())).findFirst().orElse(null);
            if (mode != null) {
                /**
                 * 更新
                 */
                mode.setDefectAnomalyDescription(m.getDescription());
                mode.setGradeCode(m.getLevel() + "");
                mode.setComponentCode(m.getComponentCode());
                mode.setDefectCodeCode(m.getDefectCode());
                mode.setPositionCode(m.getDefectPositionCode());
                mode.setResponsibleDeptCode(m.getDutyDepartment());
                update(mode);
            } else {
                /**
                 * 添加
                 */
                PqsDefectAnomalyEntity entity = new PqsDefectAnomalyEntity();
                entity.setId(IdGenerator.getId());
                entity.setDefectAnomalyCode(m.getCode());
                entity.setDefectAnomalyDescription(m.getDescription());
                entity.setGradeCode(m.getLevel() + "");
                entity.setComponentCode(m.getComponentCode());
                entity.setDefectCodeCode(m.getDefectCode());
                entity.setPositionCode(m.getDefectPositionCode());
                entity.setResponsibleDeptCode(m.getDutyDepartment());
                save(entity);
            }
        }
    }

    /**
     * 获取缺陷展示数据
     *
     * @param info
     * @return
     */
    @Override
    public List<DefectShowInfo> getAnomalyShowList(DefectFilterlParaInfo info) {
        List<ConditionDto> conditions = new ArrayList<>();
        ConditionDto conditionDto1 = new ConditionDto();
        conditionDto1.setColumnName("defectAnomalyCode");
        conditionDto1.setOperator(ConditionOper.AllLike);
        conditionDto1.setValue(info.getKey());
        conditionDto1.setRelation(ConditionRelation.Or);
        conditionDto1.setGroup("V");
        conditionDto1.setGroupRelation(ConditionRelation.Or);
        conditions.add(conditionDto1);

        ConditionDto conditionDto2 = new ConditionDto();
        conditionDto2.setColumnName("defectAnomalyDescription");
        conditionDto2.setOperator(ConditionOper.AllLike);
        conditionDto2.setValue(info.getKey());
        conditionDto2.setRelation(ConditionRelation.Or);
        conditionDto2.setGroup("V");
        conditionDto2.setGroupRelation(ConditionRelation.Or);
        conditions.add(conditionDto2);

        List<SortDto> sortInfos = new ArrayList<SortDto>();
        SortDto sortDto = new SortDto();
        sortDto.setColumnName("defectAnomalyDescription");
        sortDto.setDirection(ConditionDirection.ASC);
        sortInfos.add(sortDto);

        PageDataDto pageDataDto = new PageDataDto();
        pageDataDto.setPageIndex(info.getPageIndex());
        pageDataDto.setPageSize(info.getPageSize());
        pageDataDto.setConditions(conditions);
        pageDataDto.setSorts(sortInfos);
        PageData<PqsDefectAnomalyEntity> pageInfo = page(pageDataDto);
        List<DefectShowInfo> exsitList = new ArrayList<DefectShowInfo>();
        if (!com.alibaba.druid.util.StringUtils.isEmpty(info.getExsitCodes())) {
            String[] exsitCodes = info.getExsitCodes().split(",");
            if (exsitCodes.length > 0) {
                exsitList = selectList(new QueryWrapper<PqsDefectAnomalyEntity>().
                        in("DEFECT_ANOMALY_CODE", exsitCodes)
                        .orderByAsc("DEFECT_ANOMALY_CODE")).stream().map(t -> {
                    DefectShowInfo defectShowInfo = new DefectShowInfo();
                    defectShowInfo.setId(t.getId());
                    defectShowInfo.setCode(t.getDefectAnomalyCode());
                    defectShowInfo.setDescription(t.getDefectAnomalyDescription());
                    return defectShowInfo;
                }).collect(Collectors.toList());
            }
        }
        return (List<DefectShowInfo>) CollectionUtils.union(exsitList,
                pageInfo.getDatas().stream().map(t -> {
                    DefectShowInfo defectShowInfo = new DefectShowInfo();
                    defectShowInfo.setId(t.getId());
                    defectShowInfo.setCode(t.getDefectAnomalyCode());
                    defectShowInfo.setDescription(t.getDefectAnomalyDescription());
                    return defectShowInfo;
                }).collect(Collectors.toList()));
    }

    @Override
    public List<PqsDefectAnomalyEntity> getAllDatas() {
        Function<Object, ? extends List<PqsDefectAnomalyEntity>> getDataFunc = (obj) -> {
            List<PqsDefectAnomalyEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsDefectAnomalyEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 获取AUDIT缺陷展示数据
     *
     * @return
     */
    @Override
    public List<PqsDefectAnomalyEntity> getAuditAnomalyShowList() {
        List<ConditionDto> conditions = new ArrayList<>();
        ConditionDto conditionDto = new ConditionDto();
        conditionDto.setColumnName("pqsComponentCode");
        conditionDto.setOperator(ConditionOper.AllLike);
        conditionDto.setValue("audit");
        conditions.add(conditionDto);


        List<SortDto> sortInfos = new ArrayList<SortDto>();
        SortDto sortDto = new SortDto();
        sortDto.setColumnName("description");
        sortDto.setDirection(ConditionDirection.ASC);
        sortInfos.add(sortDto);

        PageDataDto pageDataDto = new PageDataDto();
        pageDataDto.setPageIndex(1);
        pageDataDto.setPageSize(50);
        pageDataDto.setConditions(conditions);
        pageDataDto.setSorts(sortInfos);
        PageData<PqsDefectAnomalyEntity> pageInfo = page(pageDataDto);
        return pageInfo.getDatas();
    }

    /*@Override
    public Map<String, String> getExcelColumnNames() {
        Map<String, String> columnNames = new HashMap<String, String>(13);
        columnNames.put("defectAnomalyCode", "组合代码");
        columnNames.put("defectAnomalyDescription", "ICC缺陷");
        columnNames.put("responsibleDeptCode", "责任部门代码");
        columnNames.put("responsibleDeptName", "责任部门");
        columnNames.put("gradeCode", "缺陷等级代码");
        columnNames.put("gradeName", "缺陷等级");
        columnNames.put("componentCode", "组件代码");
        columnNames.put("componentDescription", "组件描述");
        columnNames.put("defectCodeCode", "缺陷代码");
        columnNames.put("defectCodeDescription", "缺陷描述");
        columnNames.put("positionCode", "位置代码");
        columnNames.put("positionDescription", "位置描述");
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
        validExcelDataUnique(datas, "defectAnomalyCode", "Excel中组合代码重复");
        Map<String, String> excelColumnNames = getExcelColumnNames();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(excelColumnNames.get("defectAnomalyCode"), i + 1, data.get("defectAnomalyCode"), "");
            validExcelDataRequire(excelColumnNames.get("defectAnomalyDescription"), i + 1, data.get("defectAnomalyDescription"), "");
        }
    }

    @Override
    public void saveExcelData(List<PqsDefectAnomalyEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsDefectAnomalyEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsDefectAnomalyEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getDefectAnomalyCode().equals(a.getDefectAnomalyCode())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsDefectAnomalyEntity> insertList = entities.stream()
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
     * 根据缺陷名称或者缺陷编号获取缺陷列表
     *
     * @param cout           查询limit 数量
     * @param conditionInfos 条件
     * @return 缺陷数据列表
     */
    @Override
    public List<PqsDefectAnomalyEntity> getTopDatasByCondtion(Integer cout, List<ConditionDto> conditionInfos) {
        return this.getTopDatas(cout, conditionInfos, null, false);
    }

    /**
     * 根据缺陷编码查询
     *
     * @param code 缺陷编号
     * @return 实体
     */
    @Override
    public PqsDefectAnomalyEntity getEntityByCode(String code) {
        QueryWrapper<PqsDefectAnomalyEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PqsDefectAnomalyEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PqsDefectAnomalyEntity::getDefectAnomalyCode, code);
        return getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

}