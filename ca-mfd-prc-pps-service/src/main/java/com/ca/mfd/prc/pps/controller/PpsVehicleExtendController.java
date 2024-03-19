package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pps.entity.PpsVehicleExtendEntity;
import com.ca.mfd.prc.pps.service.IPpsVehicleExtendService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eric.zhou
 * @Description: 车辆关键扩展信息Controller
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@RestController
@RequestMapping("ppsvehicleextend")
@Tag(name = "车辆关键扩展信息服务", description = "车辆关键扩展信息")
public class PpsVehicleExtendController extends BaseController<PpsVehicleExtendEntity> {

    private final IPpsVehicleExtendService prcPpsVehicleExtendService;

    @Autowired
    public PpsVehicleExtendController(IPpsVehicleExtendService prcPpsVehicleExtendService) {
        this.crudService = prcPpsVehicleExtendService;
        this.prcPpsVehicleExtendService = prcPpsVehicleExtendService;
    }

}