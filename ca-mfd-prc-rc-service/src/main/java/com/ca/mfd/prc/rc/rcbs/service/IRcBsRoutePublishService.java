package com.ca.mfd.prc.rc.rcbs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRoutePublishEntity;

/**
 * @author inkelink
 * @Description: 路由发布缓存服务
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
public interface IRcBsRoutePublishService extends ICrudService<RcBsRoutePublishEntity> {
    /**
     * 根据产品唯一码查询 路由发布缓存
     *
     * @param sn 产品唯一标识
     * @return 路由发布缓存实体
     */
    RcBsRoutePublishEntity getEntityBySn(String sn);
}