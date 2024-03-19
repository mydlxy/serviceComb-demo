package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.audit.remote.app.core.provider.SysSequenceNumberProvider;
import com.ca.mfd.prc.audit.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.audit.dto.DefectShowInfo;
import com.ca.mfd.prc.audit.entity.PqsAuditDefectComponentEntity;
import com.ca.mfd.prc.audit.mapper.IPqsAuditDefectComponentMapper;
import com.ca.mfd.prc.audit.service.IPqsAuditDefectComponentService;
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
import com.ca.mfd.prc.common.utils.ResultVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: AUDIT组件代码服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsAuditDefectComponentServiceImpl extends AbstractCrudServiceImpl<IPqsAuditDefectComponentMapper, PqsAuditDefectComponentEntity> implements IPqsAuditDefectComponentService {

    @Autowired
    private LocalCache localCache;
    @Autowired
    private SysSequenceNumberProvider sysSequenceNumberProvider;
    private final String cacheName = "PRC_PQS_AUDIT_DEFECT_COMPONENT";

    @Override
    public void beforeInsert(PqsAuditDefectComponentEntity info) {
        if (!StringUtils.isNotBlank(info.getDefectComponentCode())) {
            ResultVO<String> rsp = new ResultVO().ok(sysSequenceNumberProvider.getSeqNumWithTransaction("SN_ComponentCode"));
            if (rsp == null || !rsp.getSuccess()) {
                throw new InkelinkException("获取流水号失败。" + (rsp == null ? "" : rsp.getMessage()));
            }
            info.setDefectComponentCode(rsp.getData());
        }
    }

    /**
     * 获取所有的组件信息
     *
     * @return
     */
    @Override
    public List<PqsAuditDefectComponentEntity> getAllDatas() {
        Function<Object, ? extends List<PqsAuditDefectComponentEntity>> getDataFunc = (obj) -> {
            List<PqsAuditDefectComponentEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsAuditDefectComponentEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 获取组件代码配置
     *
     * @param info
     * @return
     */
    @Override
    public List<DefectShowInfo> getComponentShowList(DefectFilterlParaInfo info) {
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
        PageData<PqsAuditDefectComponentEntity> pageInfo = page(pageDataDto);
        List<DefectShowInfo> exsitList = new ArrayList<DefectShowInfo>();
        if (!com.alibaba.druid.util.StringUtils.isEmpty(info.getExsitCodes())) {
            String[] exsitCodes = info.getExsitCodes().split(",");
            if (exsitCodes.length > 0) {
                exsitList = selectList(new QueryWrapper<PqsAuditDefectComponentEntity>().
                        in("CODE", exsitCodes)
                        .orderByAsc("CODE")).stream().map(t -> {
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
    public Map<String,String> getExcelColumnNames(){
        Map<String,String> columnNames=new HashMap<String,String>(5);
        columnNames.put("groupName","组");
        columnNames.put("subGroupName","子组");
        columnNames.put("code","代码");
        columnNames.put("description","描述");
        columnNames.put("position","位置");
        return columnNames;
    }*/
}