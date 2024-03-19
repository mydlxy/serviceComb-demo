package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectOnlineSamplingRecordsEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectOnlineSamplingRecordsService;
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
 * @Description: 电池等离子清洗效果检测Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectonlinesamplingrecords")
@Tag(name = "电池等离子清洗效果检测服务", description = "电池等离子清洗效果检测")
public class PqsInspectOnlineSamplingRecordsController extends BaseWithDefValController<PqsInspectOnlineSamplingRecordsEntity> {

    private IPqsInspectOnlineSamplingRecordsService pqsInspectOnlineSamplingRecordsService;

    @Autowired
    public PqsInspectOnlineSamplingRecordsController(IPqsInspectOnlineSamplingRecordsService pqsInspectOnlineSamplingRecordsService) {
        this.crudService = pqsInspectOnlineSamplingRecordsService;
        this.pqsInspectOnlineSamplingRecordsService = pqsInspectOnlineSamplingRecordsService;
    }

}