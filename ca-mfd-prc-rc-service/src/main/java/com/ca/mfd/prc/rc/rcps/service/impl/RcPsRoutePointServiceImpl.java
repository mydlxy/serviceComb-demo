package com.ca.mfd.prc.rc.rcps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRoutePointEntity;
import com.ca.mfd.prc.rc.rcps.mapper.IRcPsRoutePointMapper;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRoutePointService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由点服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcPsRoutePointServiceImpl extends AbstractCrudServiceImpl<IRcPsRoutePointMapper, RcPsRoutePointEntity> implements IRcPsRoutePointService {

    @Override
    public void beforeUpdate(RcPsRoutePointEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcPsRoutePointEntity model) {
        validData(model);
    }

    private void validData(RcPsRoutePointEntity model) {
        if (StringUtils.isBlank(model.getPointCode())) {
            throw new InkelinkException("代码不能为空");
        }
        if (StringUtils.isBlank(model.getPointName())) {
            throw new InkelinkException("名称");
        }
        validDataUnique(model.getId(), "PointCode", model.getPointCode(), "已经存在代码为%s的数据", "", "");
        validDataUnique(model.getId(), "PointName", model.getPointName(), "已经存在名称为%s的数据", "", "");
    }


    /**
     * 根据 路由区ID 获取路由点列表
     *
     * @param areaId 路由区ID
     * @return 路由点列表
     */
    @Override
    public List<RcPsRoutePointEntity> getEntityByAreaId(Long areaId) {
        QueryWrapper<RcPsRoutePointEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcPsRoutePointEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcPsRoutePointEntity::getPrcRcPsRouteAreaId, areaId);
        return selectList(queryWrapper);
    }

}