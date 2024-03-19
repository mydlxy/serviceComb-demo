package com.ca.mfd.prc.bdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.bdc.entity.RcBdcRoutePointEntity;
import com.ca.mfd.prc.bdc.mapper.IRcBdcRoutePointMapper;
import com.ca.mfd.prc.bdc.service.IRcBdcRoutePointService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由点服务实现
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Service
public class RcBdcRoutePointServiceImpl extends AbstractCrudServiceImpl<IRcBdcRoutePointMapper, RcBdcRoutePointEntity> implements IRcBdcRoutePointService {


    @Override
    public void beforeUpdate(RcBdcRoutePointEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcBdcRoutePointEntity model) {
        validData(model);
    }

    private void validData(RcBdcRoutePointEntity model) {
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
    public List<RcBdcRoutePointEntity> getEntityByAreaId(Long areaId) {
        QueryWrapper<RcBdcRoutePointEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRoutePointEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBdcRoutePointEntity::getPrcBdcRouteAreaId, areaId);
        return selectList(queryWrapper);
    }


}