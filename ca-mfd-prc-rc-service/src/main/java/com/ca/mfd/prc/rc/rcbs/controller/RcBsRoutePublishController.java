package com.ca.mfd.prc.rc.rcbs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRoutePublishEntity;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRoutePublishService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由发布缓存Controller
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcbsroutepublish")
@Tag(name = "路由发布缓存服务", description = "路由发布缓存")
public class RcBsRoutePublishController extends BaseController<RcBsRoutePublishEntity> {

    private final IRcBsRoutePublishService rcRoutePublishService;

    @Autowired
    public RcBsRoutePublishController(IRcBsRoutePublishService rcRoutePublishService) {
        this.crudService = rcRoutePublishService;
        this.rcRoutePublishService = rcRoutePublishService;
    }

}