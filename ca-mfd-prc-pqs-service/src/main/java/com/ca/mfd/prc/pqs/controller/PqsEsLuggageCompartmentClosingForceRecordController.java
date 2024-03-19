package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsLuggageCompartmentClosingForceRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsLuggageCompartmentClosingForceRecordService;
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
 * @Description: 行李箱关闭力记录Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsesluggagecompartmentclosingforcerecord")
@Tag(name = "行李箱关闭力记录服务", description = "行李箱关闭力记录")
public class PqsEsLuggageCompartmentClosingForceRecordController extends BaseWithDefValController<PqsEsLuggageCompartmentClosingForceRecordEntity> {

    private IPqsEsLuggageCompartmentClosingForceRecordService pqsEsLuggageCompartmentClosingForceRecordService;

    @Autowired
    public PqsEsLuggageCompartmentClosingForceRecordController(IPqsEsLuggageCompartmentClosingForceRecordService pqsEsLuggageCompartmentClosingForceRecordService) {
        this.crudService = pqsEsLuggageCompartmentClosingForceRecordService;
        this.pqsEsLuggageCompartmentClosingForceRecordService = pqsEsLuggageCompartmentClosingForceRecordService;
    }

}