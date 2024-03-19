package com.ca.mfd.prc.rc.rcavi.service.impl;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteSelectOptionEntity;
import com.ca.mfd.prc.rc.rcavi.mapper.IRcAviRouteSelectOptionMapper;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRouteSelectOptionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 路由点服务实现
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Service
public class RcAviRouteSelectOptionServiceImpl extends AbstractCrudServiceImpl<IRcAviRouteSelectOptionMapper, RcAviRouteSelectOptionEntity> implements IRcAviRouteSelectOptionService {
    @Override
    public void beforeUpdate(RcAviRouteSelectOptionEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcAviRouteSelectOptionEntity model) {
        validData(model);
    }

    private void validData(RcAviRouteSelectOptionEntity model) {
        if (StringUtils.isBlank(model.getOptionValue())) {
            throw new InkelinkException("选项值不能为空");
        }
        if (StringUtils.isBlank(model.getOptionName())) {
            throw new InkelinkException("选项名称不能为空");
        }
    }
}