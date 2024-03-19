package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.audit.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.audit.dto.DefectShowInfo;
import com.ca.mfd.prc.audit.entity.PqsAuditDefectPositionEntity;
import com.ca.mfd.prc.audit.mapper.IPqsAuditDefectPositionMapper;
import com.ca.mfd.prc.audit.service.IPqsAuditDefectPositionService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: AUDIT缺陷位置代码服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsAuditDefectPositionServiceImpl extends AbstractCrudServiceImpl<IPqsAuditDefectPositionMapper, PqsAuditDefectPositionEntity> implements IPqsAuditDefectPositionService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_AUDIT_DEFECT_POSITION";

    /**
     * 获取所有的位置信息
     *
     * @return
     */
    @Override
    public List<PqsAuditDefectPositionEntity> getAllDatas() {
        Function<Object, ? extends List<PqsAuditDefectPositionEntity>> getDataFunc = (obj) -> {
            List<PqsAuditDefectPositionEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsAuditDefectPositionEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 获取位置代码配置
     *
     * @param info
     * @return
     */
    @Override
    public List<DefectShowInfo> getPositionShowList(DefectFilterlParaInfo info) {
        List<ConditionDto> conditions = new ArrayList<>();
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
        sortDto.setColumnName("description");
        sortDto.setDirection(ConditionDirection.ASC);
        sortInfos.add(sortDto);
        PageDataDto pageDataDto = new PageDataDto();
        pageDataDto.setPageIndex(info.getPageIndex());
        pageDataDto.setPageSize(info.getPageSize());
        pageDataDto.setConditions(conditions);
        pageDataDto.setSorts(sortInfos);
        PageData<PqsAuditDefectPositionEntity> pageInfo = page(pageDataDto);
        List<DefectShowInfo> exsitList = new ArrayList<DefectShowInfo>();
        if (!com.alibaba.druid.util.StringUtils.isEmpty(info.getExsitCodes())) {
            String[] exsitCodes = info.getExsitCodes().split(",");
            if (exsitCodes.length > 0) {
                exsitList = selectList(new QueryWrapper<PqsAuditDefectPositionEntity>().
                        in("CODE", exsitCodes)
                        .orderByAsc("CODE")).stream().map(t -> {
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
    public Map<String,String> getExcelColumnNames(){
        Map<String,String> columnNames=new HashMap<String,String>(4);
        columnNames.put("groupName","组");
        columnNames.put("subGroupName","子组");
        columnNames.put("code","代码");
        columnNames.put("description","描述");
        return columnNames;
    }*/
}