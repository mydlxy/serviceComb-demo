package com.ca.mfd.prc.rc.rcps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRoutePublishLogEntity;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRoutePublishLogService;
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
@RequestMapping("rcpsroutepublishlog")
@Tag(name = "路由发布缓存记录服务", description = "路由发布缓存记录")
public class RcPsRoutePublishLogController extends BaseController<RcPsRoutePublishLogEntity> {

    private final IRcPsRoutePublishLogService rcRoutePublishLogService;

    @Autowired
    public RcPsRoutePublishLogController(IRcPsRoutePublishLogService rcRoutePublishLogService) {
        this.crudService = rcRoutePublishLogService;
        this.rcRoutePublishLogService = rcRoutePublishLogService;
    }

}