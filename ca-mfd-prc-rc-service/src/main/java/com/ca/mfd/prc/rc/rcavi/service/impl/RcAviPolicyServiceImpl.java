package com.ca.mfd.prc.rc.rcavi.service.impl;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviPolicyEntity;
import com.ca.mfd.prc.rc.rcavi.mapper.IRcAviPolicyMapper;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviPolicyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 路由策略服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcAviPolicyServiceImpl extends AbstractCrudServiceImpl<IRcAviPolicyMapper, RcAviPolicyEntity> implements IRcAviPolicyService {

    @Override
    public void beforeUpdate(RcAviPolicyEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcAviPolicyEntity model) {
        validData(model);
    }

    private void validData(RcAviPolicyEntity model) {
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