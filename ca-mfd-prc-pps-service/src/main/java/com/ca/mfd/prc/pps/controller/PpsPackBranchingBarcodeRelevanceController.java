package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pps.entity.PpsPackBranchingBarcodeRelevanceEntity;
import com.ca.mfd.prc.pps.service.IPpsPackBranchingBarcodeRelevanceService;
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
 * @Description: 电池分线条码关联Controller
 * @author inkelink
 * @date 2024年02月26日
 * @变更说明 BY inkelink At 2024年02月26日
 */
@RestController
@RequestMapping("ppspackbranchingbarcoderelevance")
@Tag(name = "电池分线条码关联服务", description = "电池分线条码关联")
public class PpsPackBranchingBarcodeRelevanceController extends BaseController<PpsPackBranchingBarcodeRelevanceEntity> {

    private IPpsPackBranchingBarcodeRelevanceService ppsPackBranchingBarcodeRelevanceService;

    @Autowired
    public PpsPackBranchingBarcodeRelevanceController(IPpsPackBranchingBarcodeRelevanceService ppsPackBranchingBarcodeRelevanceService) {
        this.crudService = ppsPackBranchingBarcodeRelevanceService;
        this.ppsPackBranchingBarcodeRelevanceService = ppsPackBranchingBarcodeRelevanceService;
    }

}