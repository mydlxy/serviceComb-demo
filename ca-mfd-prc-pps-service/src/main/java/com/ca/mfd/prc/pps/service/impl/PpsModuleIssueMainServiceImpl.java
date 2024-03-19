package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.entity.PpsEntryPartsEntity;
import com.ca.mfd.prc.pps.mapper.IPpsModuleIssueMainMapper;
import com.ca.mfd.prc.pps.entity.PpsModuleIssueMainEntity;
import com.ca.mfd.prc.pps.service.IPpsModuleIssueMainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 *
 * @Description: 电池预成组下发主体服务实现
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class PpsModuleIssueMainServiceImpl extends AbstractCrudServiceImpl<IPpsModuleIssueMainMapper, PpsModuleIssueMainEntity> implements IPpsModuleIssueMainService {

    @Override
    public PpsModuleIssueMainEntity getFirstByEntryNo(String entryNo) {
        QueryWrapper<PpsModuleIssueMainEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsModuleIssueMainEntity::getEntryNo, entryNo);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }


}