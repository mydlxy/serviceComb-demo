package com.ca.mfd.prc.rc.rcbs.service.impl;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRuleEntity;
import com.ca.mfd.prc.rc.rcbs.mapper.IRcBsRuleMapper;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRuleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 路由规则服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcBsRuleServiceImpl extends AbstractCrudServiceImpl<IRcBsRuleMapper, RcBsRuleEntity> implements IRcBsRuleService {
    @Override
    public void beforeUpdate(RcBsRuleEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcBsRuleEntity model) {
        validData(model);
    }

    private void validData(RcBsRuleEntity model) {
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