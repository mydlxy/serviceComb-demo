package com.ca.mfd.prc.pps.communication.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.communication.entity.MidSoftwareBomListEntity;
import com.ca.mfd.prc.pps.communication.entity.MidVehicleCodeEntity;
import com.ca.mfd.prc.pps.communication.service.IMidVehicleCodeService;
import com.ca.mfd.prc.pps.service.IPpsBindingTagService;
import com.ca.mfd.prc.pps.service.IPpsLogicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 车型代码中间表Controller
 * @author inkelink
 * @date 2023年12月08日
 * @变更说明 BY inkelink At 2023年12月08日
 */
@RestController
@RequestMapping("communication/midvehiclecode")
@Tag(name = "车型代码中间表服务", description = "车型代码中间表")
public class MidVehicleCodeController  extends BaseController<MidVehicleCodeEntity> {

    private IMidVehicleCodeService midVehicleCodeService;

    @Autowired
    public MidVehicleCodeController(IMidVehicleCodeService midVehicleCodeService) {
        this.crudService = midVehicleCodeService;
        this.midVehicleCodeService = midVehicleCodeService;
    }

    @GetMapping(value = "receive")
    @Operation(summary = "获取车型代码")
    public ResultVO receive() {
        midVehicleCodeService.receive();
        return new ResultVO().ok(null,"处理完成");
    }
}