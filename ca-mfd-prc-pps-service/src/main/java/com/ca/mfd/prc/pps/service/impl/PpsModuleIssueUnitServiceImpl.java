package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.entity.PpsModuleIssueUnitEntity;
import com.ca.mfd.prc.pps.mapper.IPpsModuleIssueUnitMapper;
import com.ca.mfd.prc.pps.service.IPpsModuleIssueUnitService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 电池预成组下发小单元服务实现
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class PpsModuleIssueUnitServiceImpl extends AbstractCrudServiceImpl<IPpsModuleIssueUnitMapper, PpsModuleIssueUnitEntity> implements IPpsModuleIssueUnitService {

    /**
     * 列表
     *
     * @param moduleIssueModuleId
     * @return
     */
    @Override
    public List<PpsModuleIssueUnitEntity> getByModuleIssueModuleId(Long moduleIssueModuleId) {
        QueryWrapper<PpsModuleIssueUnitEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsModuleIssueUnitEntity::getPrcPpsModuleIssueModuleId, moduleIssueModuleId);
        return selectList(qry);
    }
}