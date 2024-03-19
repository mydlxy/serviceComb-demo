package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsFourDoorTensionRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsFourDoorTensionRecordService;
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
 * @Description: 四门拉力记录Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsesfourdoortensionrecord")
@Tag(name = "四门拉力记录服务", description = "四门拉力记录")
public class PqsEsFourDoorTensionRecordController extends BaseWithDefValController<PqsEsFourDoorTensionRecordEntity> {

    private IPqsEsFourDoorTensionRecordService pqsEsFourDoorTensionRecordService;

    @Autowired
    public PqsEsFourDoorTensionRecordController(IPqsEsFourDoorTensionRecordService pqsEsFourDoorTensionRecordService) {
        this.crudService = pqsEsFourDoorTensionRecordService;
        this.pqsEsFourDoorTensionRecordService = pqsEsFourDoorTensionRecordService;
    }

}