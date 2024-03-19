package com.ca.mfd.prc.rc.rcbdc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcbdc.entity.RcBdcPolicyEntity;
import com.ca.mfd.prc.rc.rcbdc.service.IRcBdcPolicyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由策略Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcpolicy")
@Tag(name = "路由策略服务", description = "路由策略")
public class RcBdcPolicyController extends BaseController<RcBdcPolicyEntity> {

    private final IRcBdcPolicyService rcBdcPolicyService;

    @Autowired
    public RcBdcPolicyController(IRcBdcPolicyService rcBdcPolicyService) {
        this.crudService = rcBdcPolicyService;
        this.rcBdcPolicyService = rcBdcPolicyService;
    }

}