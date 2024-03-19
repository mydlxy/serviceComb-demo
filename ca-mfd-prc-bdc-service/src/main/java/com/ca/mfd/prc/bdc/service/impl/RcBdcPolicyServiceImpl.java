package com.ca.mfd.prc.bdc.service.impl;

import com.ca.mfd.prc.bdc.entity.RcBdcPolicyEntity;
import com.ca.mfd.prc.bdc.mapper.IRcBdcPolicyMapper;
import com.ca.mfd.prc.bdc.service.IRcBdcPolicyService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 路由策略服务实现
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Service
public class RcBdcPolicyServiceImpl extends AbstractCrudServiceImpl<IRcBdcPolicyMapper, RcBdcPolicyEntity> implements IRcBdcPolicyService {

    @Override
    public void beforeUpdate(RcBdcPolicyEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcBdcPolicyEntity model) {
        validData(model);
    }

    private void validData(RcBdcPolicyEntity model) {
        if (StringUtils.isBlank(model.getPolicyCode())) {
            throw new InkelinkException("代码不能为空");
        }
        if (StringUtils.isBlank(model.getPolicyName())) {
            throw new InkelinkException("名称");
        }
        validDataUnique(model.getId(), "PolicyCode", model.getPolicyCode(), "已经存在代码为%s的数据", "", "");
        validDataUnique(model.getId(), "PolicyName", model.getPolicyName(), "已经存在名称为%s的数据", "", "");
    }
}