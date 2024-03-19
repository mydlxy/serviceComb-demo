package com.ca.mfd.prc.audit.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.audit.dto.BatchAddCodeInfo;
import com.ca.mfd.prc.audit.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.audit.dto.DefectShowInfo;
import com.ca.mfd.prc.audit.entity.PqsAuditDefectCodeEntity;
import com.ca.mfd.prc.audit.mapper.IPqsAuditDefectCodeMapper;
import com.ca.mfd.prc.audit.service.IPqsAuditDefectCodeService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: AUDIT缺陷代码服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsAuditDefectCodeServiceImpl extends AbstractCrudServiceImpl<IPqsAuditDefectCodeMapper, PqsAuditDefectCodeEntity> implements IPqsAuditDefectCodeService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_AUDIT_DEFECT_CODE";

    /**
     * 获取所有的缺陷信息
     *
     * @return
     */
    @Override
    public List<PqsAuditDefectCodeEntity> getAllDatas() {
        Function<Object, ? extends List<PqsAuditDefectCodeEntity>> getDataFunc = (obj) -> {
            List<PqsAuditDefectCodeEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsAuditDefectCodeEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
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
        List<PqsAuditDefectCodeEntity> selectList = selectList(new QueryWrapper<PqsAuditDefectCodeEntity>().
                in("CODE", codes));
        for (BatchAddCodeInfo m : list) {
            PqsAuditDefectCodeEntity mode = selectList.stream().filter(c -> c.getDefectCodeCode().equals(m.getCode())).findFirst().orElse(null);
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
                PqsAuditDefectCodeEntity entity = new PqsAuditDefectCodeEntity();
                entity.setId(IdGenerator.getId());
                entity.setGroupName(m.getGroupName());
                entity.setSubGroupName(m.getSubGroupName());
                entity.setDefectCodeCode(m.getCode());
                entity.setDefectCodeDescription(m.getDescription());
                save(entity);
            }
        }
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
        conditionDto1.setColumnName("code");
        conditionDto1.setOperator(ConditionOper.AllLike);
        conditionDto1.setValue(info.getKey());
        conditionDto1.setRelation(ConditionRelation.Or);
        conditionDto1.setGroup("v");
        conditionDto1.setGroupRelation(ConditionRelation.Or);
        conditions.add(conditionDto1);

        ConditionDto conditionDto2 = new ConditionDto();
        conditionDto2.setColumnName("description");
        conditionDto2.setOperator(ConditionOper.AllLike);
        conditionDto2.setValue(info.getKey());
        conditionDto2.setRelation(ConditionRelation.Or);
        conditionDto2.setGroup("v");
        conditionDto2.setGroupRelation(ConditionRelation.Or);
        conditions.add(conditionDto2);

        List<SortDto> sortInfos = new ArrayList<SortDto>();
        SortDto sortDto = new SortDto();
        sortDto.setColumnName("code");
        sortDto.setDirection(ConditionDirection.ASC);
        sortInfos.add(sortDto);
        PageDataDto pageDataDto = new PageDataDto();
        pageDataDto.setPageIndex(info.getPageIndex());
        pageDataDto.setPageSize(info.getPageSize());
        pageDataDto.setConditions(conditions);
        pageDataDto.setSorts(sortInfos);
        PageData<PqsAuditDefectCodeEntity> pageInfo = page(pageDataDto);
        List<DefectShowInfo> exsitList = new ArrayList<DefectShowInfo>();
        if (!StringUtils.isEmpty(info.getExsitCodes())) {
            String[] exsitCodes = info.getExsitCodes().split(",");
            if (exsitCodes.length > 0) {
                exsitList = selectList(new QueryWrapper<PqsAuditDefectCodeEntity>().
                        in("CODE", exsitCodes)
                        .orderByAsc("CODE")).stream().map(t -> {
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
    public Map<String,String> getExcelColumnNames(){
        Map<String,String> columnNames=new HashMap<String,String>(4);
        columnNames.put("groupName","组");
        columnNames.put("subGroupName","子组");
        columnNames.put("code","代码");
        columnNames.put("description","描述");
        return columnNames;
    }*/

}