package com.ca.mfd.prc.rc.rcbs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.rc.rcbs.dto.RcRouteAreaItemVO;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteAreaEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由区服务
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
public interface IRcBsRouteAreaService extends ICrudService<RcBsRouteAreaEntity> {

    /**
     * 根据 主键集合  查询 路由区列表
     *
     * @param areaIds 主键集合
     * @return 路由区列表
     */
    List<RcBsRouteAreaEntity> getAreaEntityByIds(Long[] areaIds);

    /**
     * 根据路由区ID查询
     *
     * @param areaId 路由区ID
     * @return 列表
     */
    RcRouteAreaItemVO getAreaEntityByAreaId(Long areaId);
}