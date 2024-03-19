package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlRoutePublishEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlRoutePublishService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由发布缓存Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlroutepublish")
@Tag(name = "路由发布缓存服务", description = "路由发布缓存")
public class RcSmlRoutePublishController extends BaseController<RcSmlRoutePublishEntity> {

    private final IRcSmlRoutePublishService rcSmlRoutePublishService;

    @Autowired
    public RcSmlRoutePublishController(IRcSmlRoutePublishService rcSmlRoutePublishService) {
        this.crudService = rcSmlRoutePublishService;
        this.rcSmlRoutePublishService = rcSmlRoutePublishService;
    }

}