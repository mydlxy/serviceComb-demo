package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectSheetMaterialIncomingEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectSheetMaterialIncomingService;
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
 * @Description: 板料来料检查表Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectsheetmaterialincoming")
@Tag(name = "板料来料检查表服务", description = "板料来料检查表")
public class PqsInspectSheetMaterialIncomingController extends BaseWithDefValController<PqsInspectSheetMaterialIncomingEntity> {

    private IPqsInspectSheetMaterialIncomingService pqsInspectSheetMaterialIncomingService;

    @Autowired
    public PqsInspectSheetMaterialIncomingController(IPqsInspectSheetMaterialIncomingService pqsInspectSheetMaterialIncomingService) {
        this.crudService = pqsInspectSheetMaterialIncomingService;
        this.pqsInspectSheetMaterialIncomingService = pqsInspectSheetMaterialIncomingService;
    }

}