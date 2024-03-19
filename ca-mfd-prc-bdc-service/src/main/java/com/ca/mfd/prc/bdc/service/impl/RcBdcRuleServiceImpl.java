package com.ca.mfd.prc.bdc.service.impl;

import com.ca.mfd.prc.bdc.entity.RcBdcRuleEntity;
import com.ca.mfd.prc.bdc.mapper.IRcBdcRuleMapper;
import com.ca.mfd.prc.bdc.service.IRcBdcRuleService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 路由规则服务实现
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Service
public class RcBdcRuleServiceImpl extends AbstractCrudServiceImpl<IRcBdcRuleMapper, RcBdcRuleEntity> implements IRcBdcRuleService {

    @Override
    public void beforeUpdate(RcBdcRuleEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcBdcRuleEntity model) {
        validData(model);
    }

    private void validData(RcBdcRuleEntity model) {
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