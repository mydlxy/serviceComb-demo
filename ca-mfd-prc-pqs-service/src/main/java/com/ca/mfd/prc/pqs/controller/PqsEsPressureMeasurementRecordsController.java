package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsPressureMeasurementRecordsEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsPressureMeasurementRecordsService;
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
 * @Description: 轮胎气压测量记录Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsespressuremeasurementrecords")
@Tag(name = "轮胎气压测量记录服务", description = "轮胎气压测量记录")
public class PqsEsPressureMeasurementRecordsController extends BaseWithDefValController<PqsEsPressureMeasurementRecordsEntity> {

    private IPqsEsPressureMeasurementRecordsService pqsEsPressureMeasurementRecordsService;

    @Autowired
    public PqsEsPressureMeasurementRecordsController(IPqsEsPressureMeasurementRecordsService pqsEsPressureMeasurementRecordsService) {
        this.crudService = pqsEsPressureMeasurementRecordsService;
        this.pqsEsPressureMeasurementRecordsService = pqsEsPressureMeasurementRecordsService;
    }

}