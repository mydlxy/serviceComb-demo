package com.ca.mfd.prc.rc.rcavi.service.impl;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteSelectEntity;
import com.ca.mfd.prc.rc.rcavi.mapper.IRcAviRouteSelectMapper;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRouteSelectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 路由点服务实现
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Service
public class RcAviRouteSelectServiceImpl extends AbstractCrudServiceImpl<IRcAviRouteSelectMapper, RcAviRouteSelectEntity> implements IRcAviRouteSelectService {

    @Override
    public void beforeUpdate(RcAviRouteSelectEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcAviRouteSelectEntity model) {
        validData(model);
    }

    private void validData(RcAviRouteSelectEntity model) {
        if (StringUtils.isBlank(model.getSelectCode())) {
            throw new InkelinkException("代码不能为空");
        }

        if (StringUtils.isBlank(model.getSelectName())) {
            throw new InkelinkException("名称");
        }
        validDataUnique(model.getId(), "SelectCode", model.getSelectCode(), "已经存在代码为%s的数据", "", "");
        validDataUnique(model.getId(), "SelectName", model.getSelectName(), "已经存在代码为%s的数据", "", "");
    }
}