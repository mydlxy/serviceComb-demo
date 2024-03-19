package com.ca.mfd.prc.rc.rcps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRoutePublishEntity;
import com.ca.mfd.prc.rc.rcps.mapper.IRcPsRoutePublishMapper;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRoutePublishService;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 路由发布缓存服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcPsRoutePublishServiceImpl extends AbstractCrudServiceImpl<IRcPsRoutePublishMapper, RcPsRoutePublishEntity> implements IRcPsRoutePublishService {


    @Override
    public RcPsRoutePublishEntity getEntityBySn(String sn) {
        QueryWrapper<RcPsRoutePublishEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcPsRoutePublishEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcPsRoutePublishEntity::getSn, sn);
        return getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }
}