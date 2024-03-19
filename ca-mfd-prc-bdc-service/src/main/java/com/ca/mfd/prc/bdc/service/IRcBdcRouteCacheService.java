package com.ca.mfd.prc.bdc.service;

import com.ca.mfd.prc.bdc.entity.RcBdcRouteCacheEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由缓存服务
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
public interface IRcBdcRouteCacheService extends ICrudService<RcBdcRouteCacheEntity> {
    /**
     * 根据识别码 路由去查询总数
     *
     * @param sn     车辆识别码
     * @param areaId 路由区
     * @return 总数
     */
    Long getCountByCodeAndAreaId(String sn, Long areaId, Long id);

    /**
     * 根据车道号 路由去查询总数
     *
     * @param areaId   路由区
     * @param laneCode 车道号
     * @return 总数
     */
    Long getCountByLaneCodeAndAreaId(Long areaId, int laneCode);

    /**
     * 根据产品唯一标识查询
     *
     * @param sn 产品唯一标识
     * @param id 主键
     * @return 是否查询到数据
     */
    Boolean getCacheEntityBySn(String sn, Long id);

    /**
     * 根据路由区ID查询路由缓存列表
     *
     * @param areaId 路由区ID
     * @return 缓存列表
     */
    List<RcBdcRouteCacheEntity> getCacheEntityByAreaId(String areaId);
}