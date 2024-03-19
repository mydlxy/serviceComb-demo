package com.ca.mfd.prc.pps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;

import com.ca.mfd.prc.pps.communication.service.IPartPlanService;
import com.ca.mfd.prc.pps.dto.LmsPartPlanDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("communication/partplan")
@Tag(name = "lms零件计划下发接口", description = "lms零件计划下发接口")
public class LmsPartPlanController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(LmsPartPlanController.class);

    @Autowired
    private IPartPlanService partPlanService;

    @PostMapping(value = "sendpartplan")
    @Operation(summary = "发送零部件过点数据")
    public ResultVO<Boolean> sendPartPlan(@RequestBody LmsPartPlanDTO lmsPartPlanDTO) {
        return new ResultVO().ok(partPlanService.sendPartPlan(lmsPartPlanDTO),"下发成功");
    }
}
