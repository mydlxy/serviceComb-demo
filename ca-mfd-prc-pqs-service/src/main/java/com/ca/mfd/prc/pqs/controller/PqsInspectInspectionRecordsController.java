package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectInspectionRecordsEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectInspectionRecordsService;
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
 * @Description: 抽检记录表(离线/在线)Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectinspectionrecords")
@Tag(name = "抽检记录表(离线/在线)服务", description = "抽检记录表(离线/在线)")
public class PqsInspectInspectionRecordsController extends BaseWithDefValController<PqsInspectInspectionRecordsEntity> {

    private IPqsInspectInspectionRecordsService pqsInspectInspectionRecordsService;

    @Autowired
    public PqsInspectInspectionRecordsController(IPqsInspectInspectionRecordsService pqsInspectInspectionRecordsService) {
        this.crudService = pqsInspectInspectionRecordsService;
        this.pqsInspectInspectionRecordsService = pqsInspectInspectionRecordsService;
    }

}