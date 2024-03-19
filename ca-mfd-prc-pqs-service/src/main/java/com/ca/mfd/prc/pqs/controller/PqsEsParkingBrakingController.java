package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsParkingBrakingEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsParkingBrakingService;
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
 * @Description: 驻车制动Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsesparkingbraking")
@Tag(name = "驻车制动服务", description = "驻车制动")
public class PqsEsParkingBrakingController extends BaseWithDefValController<PqsEsParkingBrakingEntity> {

    private IPqsEsParkingBrakingService pqsEsParkingBrakingService;

    @Autowired
    public PqsEsParkingBrakingController(IPqsEsParkingBrakingService pqsEsParkingBrakingService) {
        this.crudService = pqsEsParkingBrakingService;
        this.pqsEsParkingBrakingService = pqsEsParkingBrakingService;
    }

}