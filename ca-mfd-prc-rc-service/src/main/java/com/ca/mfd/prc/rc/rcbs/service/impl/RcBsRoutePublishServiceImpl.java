package com.ca.mfd.prc.rc.rcbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRoutePublishEntity;
import com.ca.mfd.prc.rc.rcbs.mapper.IRcBsRoutePublishMapper;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRoutePublishService;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 路由发布缓存服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcBsRoutePublishServiceImpl extends AbstractCrudServiceImpl<IRcBsRoutePublishMapper, RcBsRoutePublishEntity> implements IRcBsRoutePublishService {

    @Override
    public RcBsRoutePublishEntity getEntityBySn(String sn) {
        QueryWrapper<RcBsRoutePublishEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRoutePublishEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBsRoutePublishEntity::getSn, sn);
        return getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }
}