package com.ca.mfd.prc.rc.rcavi.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteAreaEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由区服务
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
public interface IRcAviRouteAreaService extends ICrudService<RcAviRouteAreaEntity> {

    /**
     * 根据 主键集合  查询 路由区列表
     *
     * @param areaIds 主键集合
     * @return 路由区列表
     */
    List<RcAviRouteAreaEntity> getAreaEntityByIds(Long[] areaIds);
}