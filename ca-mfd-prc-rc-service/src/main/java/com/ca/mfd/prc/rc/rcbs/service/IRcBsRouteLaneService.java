package com.ca.mfd.prc.rc.rcbs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteLaneEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由车道服务
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
public interface IRcBsRouteLaneService extends ICrudService<RcBsRouteLaneEntity> {

    /**
     * 根据路由区和编码查询路由缓存
     *
     * @param areaId 路由区
     * @param code   编码
     * @return 路由缓存实体
     */
    RcBsRouteLaneEntity getLaneEntityByAreaIdAndCode(Long areaId, int code);

    /**
     * 根据路由区查询列表
     *
     * @param areaId 路由区
     * @return 路由车道列表
     */
    List<RcBsRouteLaneEntity> getLaneEntityByAreaId(String areaId);
}