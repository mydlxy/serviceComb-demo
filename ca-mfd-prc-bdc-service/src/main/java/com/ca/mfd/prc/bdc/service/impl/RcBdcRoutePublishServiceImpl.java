package com.ca.mfd.prc.bdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.bdc.entity.RcBdcRoutePublishEntity;
import com.ca.mfd.prc.bdc.mapper.IRcBdcRoutePublishMapper;
import com.ca.mfd.prc.bdc.service.IRcBdcRoutePublishService;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 路由发布缓存服务实现
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Service
public class RcBdcRoutePublishServiceImpl extends AbstractCrudServiceImpl<IRcBdcRoutePublishMapper, RcBdcRoutePublishEntity> implements IRcBdcRoutePublishService {

    /**
     * 根据产品唯一码查询路由发布缓存
     *
     * @param sn 产品唯一码
     * @return 返回路由发布缓存
     */
    public RcBdcRoutePublishEntity getEntityBySn(String sn) {
        QueryWrapper<RcBdcRoutePublishEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRoutePublishEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBdcRoutePublishEntity::getSn, sn);
        return getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }
}