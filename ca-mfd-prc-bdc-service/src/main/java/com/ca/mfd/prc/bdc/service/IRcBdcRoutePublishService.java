package com.ca.mfd.prc.bdc.service;

import com.ca.mfd.prc.bdc.entity.RcBdcRoutePublishEntity;
import com.ca.mfd.prc.common.service.ICrudService;

/**
 * @author inkelink
 * @Description: 路由发布缓存服务
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
public interface IRcBdcRoutePublishService extends ICrudService<RcBdcRoutePublishEntity> {
    /**
     * 根据产品唯一码查询路由发布缓存
     *
     * @param sn 产品唯一码
     * @return 返回路由发布缓存
     */
    RcBdcRoutePublishEntity getEntityBySn(String sn);
}