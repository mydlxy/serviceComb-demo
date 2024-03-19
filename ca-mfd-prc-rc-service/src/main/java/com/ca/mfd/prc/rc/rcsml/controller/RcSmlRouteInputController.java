package com.ca.mfd.prc.rc.rcsml.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcsml.entity.RcSmlRouteInputEntity;
import com.ca.mfd.prc.rc.rcsml.service.IRcSmlRouteInputService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由区模拟车辆输入Controller
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@RestController
@RequestMapping("rcsmlrouteinput")
@Tag(name = "路由区模拟车辆输入服务", description = "路由区模拟车辆输入")
public class RcSmlRouteInputController extends BaseController<RcSmlRouteInputEntity> {

    private final IRcSmlRouteInputService rcSmlRouteInputService;

    @Autowired
    public RcSmlRouteInputController(IRcSmlRouteInputService rcSmlRouteInputService) {
        this.crudService = rcSmlRouteInputService;
        this.rcSmlRouteInputService = rcSmlRouteInputService;
    }

}