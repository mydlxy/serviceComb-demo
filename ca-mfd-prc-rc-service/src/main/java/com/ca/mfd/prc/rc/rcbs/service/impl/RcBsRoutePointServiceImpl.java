package com.ca.mfd.prc.rc.rcbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRoutePointEntity;
import com.ca.mfd.prc.rc.rcbs.mapper.IRcBsRoutePointMapper;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRoutePointService;
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
public class RcBsRoutePointServiceImpl extends AbstractCrudServiceImpl<IRcBsRoutePointMapper, RcBsRoutePointEntity> implements IRcBsRoutePointService {

    @Override
    public void beforeUpdate(RcBsRoutePointEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcBsRoutePointEntity model) {
        validData(model);
    }

    private void validData(RcBsRoutePointEntity model) {
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
    public List<RcBsRoutePointEntity> getEntityByAreaId(Long areaId) {
        QueryWrapper<RcBsRoutePointEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRoutePointEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBsRoutePointEntity::getPrcRcBsRouteAreaId, areaId);
        return selectList(queryWrapper);
    }

}