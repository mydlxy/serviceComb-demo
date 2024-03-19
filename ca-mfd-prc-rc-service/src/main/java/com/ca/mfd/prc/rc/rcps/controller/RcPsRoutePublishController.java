package com.ca.mfd.prc.rc.rcps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRoutePublishEntity;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRoutePublishService;
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
@RequestMapping("rcpsroutepublish")
@Tag(name = "路由发布缓存服务", description = "路由发布缓存")
public class RcPsRoutePublishController extends BaseController<RcPsRoutePublishEntity> {

    private final IRcPsRoutePublishService rcRoutePublishService;

    @Autowired
    public RcPsRoutePublishController(IRcPsRoutePublishService rcRoutePublishService) {
        this.crudService = rcRoutePublishService;
        this.rcRoutePublishService = rcRoutePublishService;
    }

}