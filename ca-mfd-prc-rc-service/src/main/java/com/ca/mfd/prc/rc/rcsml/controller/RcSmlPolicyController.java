package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlPolicyEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlPolicyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由策略Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlpolicy")
@Tag(name = "路由策略服务", description = "路由策略")
public class RcSmlPolicyController extends BaseController<RcSmlPolicyEntity> {

    private final IRcSmlPolicyService rcSmlPolicyService;

    @Autowired
    public RcSmlPolicyController(IRcSmlPolicyService rcSmlPolicyService) {
        this.crudService = rcSmlPolicyService;
        this.rcSmlPolicyService = rcSmlPolicyService;
    }

}