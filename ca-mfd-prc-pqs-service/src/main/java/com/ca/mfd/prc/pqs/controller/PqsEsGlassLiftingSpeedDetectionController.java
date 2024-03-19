package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsGlassLiftingSpeedDetectionEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsGlassLiftingSpeedDetectionService;
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
 * @Description: 玻璃升降速度检测Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsesglassliftingspeeddetection")
@Tag(name = "玻璃升降速度检测服务", description = "玻璃升降速度检测")
public class PqsEsGlassLiftingSpeedDetectionController extends BaseWithDefValController<PqsEsGlassLiftingSpeedDetectionEntity> {

    private IPqsEsGlassLiftingSpeedDetectionService pqsEsGlassLiftingSpeedDetectionService;

    @Autowired
    public PqsEsGlassLiftingSpeedDetectionController(IPqsEsGlassLiftingSpeedDetectionService pqsEsGlassLiftingSpeedDetectionService) {
        this.crudService = pqsEsGlassLiftingSpeedDetectionService;
        this.pqsEsGlassLiftingSpeedDetectionService = pqsEsGlassLiftingSpeedDetectionService;
    }

}