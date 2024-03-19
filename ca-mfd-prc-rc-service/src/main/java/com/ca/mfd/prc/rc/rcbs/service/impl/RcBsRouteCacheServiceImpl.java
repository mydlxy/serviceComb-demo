package com.ca.mfd.prc.rc.rcbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteCacheEntity;
import com.ca.mfd.prc.rc.rcbs.mapper.IRcBsRouteCacheMapper;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteCacheService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由缓存服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcBsRouteCacheServiceImpl extends AbstractCrudServiceImpl<IRcBsRouteCacheMapper, RcBsRouteCacheEntity> implements IRcBsRouteCacheService {

    /**
     * 根据识别码 路由去查询总数
     *
     * @param tpsCode 车辆识别码
     * @param areaId  路由区
     * @return 总数
     */
    @Override
    public Long getCountByCodeAndAreaId(String tpsCode, Long areaId, Long id) {
        QueryWrapper<RcBsRouteCacheEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRouteCacheEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBsRouteCacheEntity::getSn, tpsCode);
        //lambdaQueryWrapper.eq(RcBsRouteCacheEntity::getRcRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcBsRouteCacheEntity::getPrcRcBsRouteAreaId, areaId);
        lambdaQueryWrapper.ne(RcBsRouteCacheEntity::getId, id);
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
        QueryWrapper<RcBsRouteCacheEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRouteCacheEntity> lambdaQueryWrapper = queryWrapper.lambda();
        //lambdaQueryWrapper.eq(RcBsRouteCacheEntity::getRcRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcBsRouteCacheEntity::getPrcRcBsRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcBsRouteCacheEntity::getLaneCode, laneCode);
        return selectCount(queryWrapper);
    }

    /**
     * 根据路由区ID查询路由缓存列表
     *
     * @param areaId 路由区ID
     * @return 缓存列表
     */
    @Override
    public List<RcBsRouteCacheEntity> getCacheEntityByAreaId(String areaId) {
        QueryWrapper<RcBsRouteCacheEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRouteCacheEntity> lambdaQueryWrapper = queryWrapper.lambda();
        //lambdaQueryWrapper.eq(RcBsRouteCacheEntity::getRcRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcBsRouteCacheEntity::getPrcRcBsRouteAreaId, areaId);
        lambdaQueryWrapper.orderByAsc(RcBsRouteCacheEntity::getDisplayNo);
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
        QueryWrapper<RcBsRouteCacheEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRouteCacheEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBsRouteCacheEntity::getSn, sn);
        lambdaQueryWrapper.ne(RcBsRouteCacheEntity::getId, id);
        return selectCount(queryWrapper) > 0;
    }
}