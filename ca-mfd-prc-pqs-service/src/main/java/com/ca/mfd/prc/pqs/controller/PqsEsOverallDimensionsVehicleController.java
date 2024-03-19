package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsOverallDimensionsVehicleEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsOverallDimensionsVehicleService;
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
 * @Description: 整车外廓尺寸Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsesoveralldimensionsvehicle")
@Tag(name = "整车外廓尺寸服务", description = "整车外廓尺寸")
public class PqsEsOverallDimensionsVehicleController extends BaseWithDefValController<PqsEsOverallDimensionsVehicleEntity> {

    private IPqsEsOverallDimensionsVehicleService pqsEsOverallDimensionsVehicleService;

    @Autowired
    public PqsEsOverallDimensionsVehicleController(IPqsEsOverallDimensionsVehicleService pqsEsOverallDimensionsVehicleService) {
        this.crudService = pqsEsOverallDimensionsVehicleService;
        this.pqsEsOverallDimensionsVehicleService = pqsEsOverallDimensionsVehicleService;
    }

}