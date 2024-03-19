package com.ca.mfd.prc.rc.rcbs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteCacheEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由缓存服务
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
public interface IRcBsRouteCacheService extends ICrudService<RcBsRouteCacheEntity> {
    /**
     * 根据识别码 路由去查询总数
     *
     * @param tpsCode 车辆识别码
     * @param areaId  路由区
     * @param id      id
     * @return 总数
     */
    Long getCountByCodeAndAreaId(String tpsCode, Long areaId, Long id);

    /**
     * 根据车道号 路由去查询总数
     *
     * @param areaId   路由区
     * @param laneCode 车道号
     * @return 总数
     */
    Long getCountByLaneCodeAndAreaId(Long areaId, int laneCode);

    /**
     * 根据路由区ID查询路由缓存列表
     *
     * @param areaId 路由区ID
     * @return 缓存列表
     */
    List<RcBsRouteCacheEntity> getCacheEntityByAreaId(String areaId);

    /**
     * 根据产品唯一标识查询
     *
     * @param sn 产品唯一标识
     * @param id 主键
     * @return 是否查询到数据
     */
    Boolean getCacheEntityBySn(String sn, Long id);
}