package com.ca.mfd.prc.rc.rcbdc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbdc.entity.RcBdcRoutePublishEntity;
import com.ca.mfd.prc.rc.rcbdc.service.IRcBdcRoutePublishService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由发布缓存Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcroutepublish")
@Tag(name = "路由发布缓存服务", description = "路由发布缓存")
public class RcBdcRoutePublishController extends BaseController<RcBdcRoutePublishEntity> {

    private final IRcBdcRoutePublishService rcBdcRoutePublishService;

    @Autowired
    public RcBdcRoutePublishController(IRcBdcRoutePublishService rcBdcRoutePublishService) {
        this.crudService = rcBdcRoutePublishService;
        this.rcBdcRoutePublishService = rcBdcRoutePublishService;
    }

}