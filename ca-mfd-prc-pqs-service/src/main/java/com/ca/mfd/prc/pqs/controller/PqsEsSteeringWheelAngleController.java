package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsSteeringWheelAngleEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsSteeringWheelAngleService;
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
 * @Description: 方向盘倾角Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsessteeringwheelangle")
@Tag(name = "方向盘倾角服务", description = "方向盘倾角")
public class PqsEsSteeringWheelAngleController extends BaseWithDefValController<PqsEsSteeringWheelAngleEntity> {

    private IPqsEsSteeringWheelAngleService pqsEsSteeringWheelAngleService;

    @Autowired
    public PqsEsSteeringWheelAngleController(IPqsEsSteeringWheelAngleService pqsEsSteeringWheelAngleService) {
        this.crudService = pqsEsSteeringWheelAngleService;
        this.pqsEsSteeringWheelAngleService = pqsEsSteeringWheelAngleService;
    }

}