package com.ca.mfd.prc.bdc.service;

import com.ca.mfd.prc.bdc.dto.RouteLaneItems;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteLaneEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由车道服务
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
public interface IRcBdcRouteLaneService extends ICrudService<RcBdcRouteLaneEntity> {
    /**
     * 根据ID 或者坐标点查询
     *
     * @param id        主键
     * @param positionX 坐标X
     * @param positionY 坐标Y
     * @param positionZ 坐标Z
     * @return 返回列表
     */
    List<RcBdcRouteLaneEntity> getDataByPosition(Long id, Integer positionX, Integer positionY, Integer positionZ);

    /**
     * 根据路由区和编码查询路由缓存
     *
     * @return 路由缓存实体
     */
    RcBdcRouteLaneEntity getLaneEntityByAreaIdAndCode(Long areaId, int code);

    /**
     * 根据路由区查询列表
     *
     * @param areaId 路由区
     * @return 路由车道列表
     */
    List<RcBdcRouteLaneEntity> getLaneEntityByAreaId(String areaId);

    /**
     * 立体库车道自动添加
     *
     * @return 列表
     */
    List<RouteLaneItems> getRouteLaneItems(Long areaId);
}