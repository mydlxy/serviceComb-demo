package com.ca.mfd.prc.bdc.service;

import com.ca.mfd.prc.bdc.dto.RcRouteAreaItemVO;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteAreaEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由区服务
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
public interface IRcBdcRouteAreaService extends ICrudService<RcBdcRouteAreaEntity> {
    /**
     * 根据路由区ID查询
     *
     * @param areaId 路由区ID
     * @return 列表
     */
    RcRouteAreaItemVO getAreaEntityByAreaId(Long areaId);

    /**
     * 根据 主键集合  查询 路由区列表
     *
     * @param areaIds 主键集合
     * @return 路由区列表
     */
    List<RcBdcRouteAreaEntity> getAreaEntityByIds(Long[] areaIds);
}