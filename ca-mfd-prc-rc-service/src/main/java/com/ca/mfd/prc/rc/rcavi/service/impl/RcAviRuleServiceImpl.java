package com.ca.mfd.prc.rc.rcavi.service.impl;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRuleEntity;
import com.ca.mfd.prc.rc.rcavi.mapper.IRcAviRuleMapper;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRuleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 路由规则服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcAviRuleServiceImpl extends AbstractCrudServiceImpl<IRcAviRuleMapper, RcAviRuleEntity> implements IRcAviRuleService {
    @Override
    public void beforeUpdate(RcAviRuleEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcAviRuleEntity model) {
        validData(model);
    }

    private void validData(RcAviRuleEntity model) {
        if (StringUtils.isBlank(model.getRuleCode())) {
            throw new InkelinkException("代码不能为空");
        }
        if (StringUtils.isBlank(model.getRuleName())) {
            throw new InkelinkException("名称");
        }
        validDataUnique(model.getId(), "RuleCode", model.getRuleCode(), "已经存在代码为%s的数据", "", "");
        validDataUnique(model.getId(), "RuleName", model.getRuleName(), "已经存在代码为%s的数据", "", "");
    }
}