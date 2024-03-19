package com.ca.mfd.prc.rc.rcps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRoutePublishEntity;

/**
 * @author inkelink
 * @Description: 路由发布缓存服务
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
public interface IRcPsRoutePublishService extends ICrudService<RcPsRoutePublishEntity> {
    /**
     * 根据产品唯一码查询 路由发布缓存
     *
     * @param sn 产品唯一标识
     * @return 路由发布缓存实体
     */
    RcPsRoutePublishEntity getEntityBySn(String sn);
}