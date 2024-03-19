package com.ca.mfd.prc.rc.rcps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteCacheEntity;
import com.ca.mfd.prc.rc.rcps.mapper.IRcPsRouteCacheMapper;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRouteCacheService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由缓存服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcPsRouteCacheServiceImpl extends AbstractCrudServiceImpl<IRcPsRouteCacheMapper, RcPsRouteCacheEntity> implements IRcPsRouteCacheService {

    /**
     * 根据识别码 路由去查询总数
     *
     * @param sn     车辆识别码
     * @param areaId 路由区
     * @return 总数
     */
    @Override
    public Long getCountByCodeAndAreaId(String sn, Long areaId, Long id) {
        QueryWrapper<RcPsRouteCacheEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcPsRouteCacheEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcPsRouteCacheEntity::getSn, sn);
        lambdaQueryWrapper.eq(RcPsRouteCacheEntity::getPrcRcPsRouteAreaId, areaId);
        lambdaQueryWrapper.ne(RcPsRouteCacheEntity::getId, id);
        return selectCount(queryWrapper);
    }

    /**
     * 根据车道号 路由去查询总数
     *
     * @param areaId   路由区
     * @param laneCode 车道号
     * @return 总数
     */
    @Override
    public Long getCountByLaneCodeAndAreaId(Long areaId, int laneCode) {
        QueryWrapper<RcPsRouteCacheEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcPsRouteCacheEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcPsRouteCacheEntity::getPrcRcPsRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcPsRouteCacheEntity::getLaneCode, laneCode);
        return selectCount(queryWrapper);
    }

    /**
     * 根据路由区ID查询路由缓存列表
     *
     * @param areaId 路由区ID
     * @return 缓存列表
     */
    @Override
    public List<RcPsRouteCacheEntity> getCacheEntityByAreaId(String areaId) {
        QueryWrapper<RcPsRouteCacheEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcPsRouteCacheEntity> lambdaQueryWrapper = queryWrapper.lambda();
        //lambdaQueryWrapper.eq(RcBsRouteCacheEntity::getRcRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcPsRouteCacheEntity::getPrcRcPsRouteAreaId, areaId);
        lambdaQueryWrapper.orderByAsc(RcPsRouteCacheEntity::getDisplayNo);
        return selectList(queryWrapper);
    }

    /**
     * 根据产品唯一标识查询
     *
     * @param sn 产品唯一标识
     * @param id 主键
     * @return 是否查询到数据
     */
    @Override
    public Boolean getCacheEntityBySn(String sn, Long id) {
        QueryWrapper<RcPsRouteCacheEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcPsRouteCacheEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcPsRouteCacheEntity::getSn, sn);
        lambdaQueryWrapper.ne(RcPsRouteCacheEntity::getId, id);
        return selectCount(queryWrapper) > 0;
    }

}