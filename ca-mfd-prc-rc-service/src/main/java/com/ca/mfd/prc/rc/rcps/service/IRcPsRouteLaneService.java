package com.ca.mfd.prc.rc.rcps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteLaneEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由车道服务
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
public interface IRcPsRouteLaneService extends ICrudService<RcPsRouteLaneEntity> {

    /**
     * 根据路由区和编码查询路由缓存
     *
     * @param areaId 路由区
     * @param code   编码
     * @return 路由缓存实体
     */
    RcPsRouteLaneEntity getLaneEntityByAreaIdAndCode(Long areaId, int code);

    /**
     * 根据路由区查询列表
     *
     * @param areaId 路由区
     * @return 路由车道列表
     */
    List<RcPsRouteLaneEntity> getLaneEntityByAreaId(String areaId);
}