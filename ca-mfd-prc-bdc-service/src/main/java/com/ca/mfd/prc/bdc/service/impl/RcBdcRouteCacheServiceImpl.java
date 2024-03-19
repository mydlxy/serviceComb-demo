package com.ca.mfd.prc.bdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteCacheEntity;
import com.ca.mfd.prc.bdc.mapper.IRcBdcRouteCacheMapper;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteCacheService;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由缓存服务实现
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Service
public class RcBdcRouteCacheServiceImpl extends AbstractCrudServiceImpl<IRcBdcRouteCacheMapper, RcBdcRouteCacheEntity> implements IRcBdcRouteCacheService {

    /**
     * 根据识别码 路由去查询总数
     *
     * @param sn     车辆识别码
     * @param areaId 路由区
     * @return 总数
     */
    @Override
    public Long getCountByCodeAndAreaId(String sn, Long areaId, Long id) {
        QueryWrapper<RcBdcRouteCacheEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRouteCacheEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBdcRouteCacheEntity::getSn, sn);
        lambdaQueryWrapper.eq(RcBdcRouteCacheEntity::getPrcBdcRouteAreaId, areaId);
        lambdaQueryWrapper.ne(RcBdcRouteCacheEntity::getId, id);
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
        QueryWrapper<RcBdcRouteCacheEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRouteCacheEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBdcRouteCacheEntity::getPrcBdcRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcBdcRouteCacheEntity::getLaneCode, laneCode);
        return selectCount(queryWrapper);
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
        QueryWrapper<RcBdcRouteCacheEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRouteCacheEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBdcRouteCacheEntity::getSn, sn);
        lambdaQueryWrapper.ne(RcBdcRouteCacheEntity::getId, id);
        return selectCount(queryWrapper) > 0;
    }


    /**
     * 根据路由区ID查询路由缓存列表
     *
     * @param areaId 路由区ID
     * @return 缓存列表
     */

    @Override
    public List<RcBdcRouteCacheEntity> getCacheEntityByAreaId(String areaId) {
        QueryWrapper<RcBdcRouteCacheEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRouteCacheEntity> lambdaQueryWrapper = queryWrapper.lambda();
        //lambdaQueryWrapper.eq(RcBsRouteCacheEntity::getRcRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcBdcRouteCacheEntity::getPrcBdcRouteAreaId, areaId);
        lambdaQueryWrapper.orderByAsc(RcBdcRouteCacheEntity::getDisplayNo);
        return selectList(queryWrapper);
    }
}