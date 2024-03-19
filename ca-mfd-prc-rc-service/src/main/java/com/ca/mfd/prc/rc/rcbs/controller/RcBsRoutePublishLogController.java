package com.ca.mfd.prc.rc.rcbs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRoutePublishLogEntity;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRoutePublishLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由发布缓存记录Controller
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcbsroutepublishlog")
@Tag(name = "路由发布缓存记录服务", description = "路由发布缓存记录")
public class RcBsRoutePublishLogController extends BaseController<RcBsRoutePublishLogEntity> {

    private final IRcBsRoutePublishLogService rcRoutePublishLogService;

    @Autowired
    public RcBsRoutePublishLogController(IRcBsRoutePublishLogService rcRoutePublishLogService) {
        this.crudService = rcRoutePublishLogService;
        this.rcRoutePublishLogService = rcRoutePublishLogService;
    }

}