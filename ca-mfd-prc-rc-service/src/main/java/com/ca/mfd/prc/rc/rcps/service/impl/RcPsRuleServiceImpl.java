package com.ca.mfd.prc.rc.rcps.service.impl;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRuleEntity;
import com.ca.mfd.prc.rc.rcps.mapper.IRcPsRuleMapper;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRuleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 路由规则服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcPsRuleServiceImpl extends AbstractCrudServiceImpl<IRcPsRuleMapper, RcPsRuleEntity> implements IRcPsRuleService {

    @Override
    public void beforeUpdate(RcPsRuleEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcPsRuleEntity model) {
        validData(model);
    }

    private void validData(RcPsRuleEntity model) {
        if (StringUtils.isBlank(model.getRuleCode())) {
            throw new InkelinkException("代码不能为空");
        }
        if (StringUtils.isBlank(model.getRuleName())) {
            throw new InkelinkException("名称");
        }
        validDataUnique(model.getId(), "RuleCode", model.getRuleCode(), "已经存在代码为%s的数据", "", "");
        validDataUnique(model.getId(), "RuleName", model.getRuleName(), "已经存在名称为%s的数据", "", "");
    }
}