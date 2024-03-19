package com.ca.mfd.prc.bdc.controller;

import com.ca.mfd.prc.bdc.entity.RcBdcRoutePublishLogEntity;
import com.ca.mfd.prc.bdc.service.IRcBdcRoutePublishLogService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由发布缓存记录Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcroutepublishlog")
@Tag(name = "路由发布缓存记录服务", description = "路由发布缓存记录")
public class RcBdcRoutePublishLogController extends BaseController<RcBdcRoutePublishLogEntity> {

    private final IRcBdcRoutePublishLogService rcBdcRoutePublishLogService;

    @Autowired
    public RcBdcRoutePublishLogController(IRcBdcRoutePublishLogService rcBdcRoutePublishLogService) {
        this.crudService = rcBdcRoutePublishLogService;
        this.rcBdcRoutePublishLogService = rcBdcRoutePublishLogService;
    }

}