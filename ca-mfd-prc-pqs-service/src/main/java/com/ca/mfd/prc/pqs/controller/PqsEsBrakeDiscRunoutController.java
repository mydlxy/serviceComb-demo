package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsBrakeDiscRunoutEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsBrakeDiscRunoutService;
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
 * @Description: 制动盘跳动量记录Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsesbrakediscrunout")
@Tag(name = "制动盘跳动量记录服务", description = "制动盘跳动量记录")
public class PqsEsBrakeDiscRunoutController extends BaseWithDefValController<PqsEsBrakeDiscRunoutEntity> {

    private IPqsEsBrakeDiscRunoutService pqsEsBrakeDiscRunoutService;

    @Autowired
    public PqsEsBrakeDiscRunoutController(IPqsEsBrakeDiscRunoutService pqsEsBrakeDiscRunoutService) {
        this.crudService = pqsEsBrakeDiscRunoutService;
        this.pqsEsBrakeDiscRunoutService = pqsEsBrakeDiscRunoutService;
    }

}