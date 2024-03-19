package com.ca.mfd.prc.rc.rcavi.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteSelectOptionEntity;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRouteSelectOptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 路由点Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcavirouteselectoption")
@Tag(name = "路由选择选项配置", description = "路由选择选项配置")
public class RcAviRouteSelectOptionController extends BaseController<RcAviRouteSelectOptionEntity> {

    private final IRcAviRouteSelectOptionService rcAviRouteSelectOptionService;

    @Autowired
    public RcAviRouteSelectOptionController(IRcAviRouteSelectOptionService rcAviRouteSelectOptionService) {
        this.crudService = rcAviRouteSelectOptionService;
        this.rcAviRouteSelectOptionService = rcAviRouteSelectOptionService;
    }

}