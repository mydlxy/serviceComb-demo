package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlRoutePublishLogEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlRoutePublishLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由发布缓存记录Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlroutepublishlog")
@Tag(name = "路由发布缓存记录服务", description = "路由发布缓存记录")
public class RcSmlRoutePublishLogController extends BaseController<RcSmlRoutePublishLogEntity> {

    private final IRcSmlRoutePublishLogService rcSmlRoutePublishLogService;

    @Autowired
    public RcSmlRoutePublishLogController(IRcSmlRoutePublishLogService rcSmlRoutePublishLogService) {
        this.crudService = rcSmlRoutePublishLogService;
        this.rcSmlRoutePublishLogService = rcSmlRoutePublishLogService;
    }

}