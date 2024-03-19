package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectStAudiTrackingEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectStAudiTrackingService;
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
 * @Description: 冲压车间内部奥迪特评审问题跟踪表Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectstauditracking")
@Tag(name = "冲压车间内部奥迪特评审问题跟踪表服务", description = "冲压车间内部奥迪特评审问题跟踪表")
public class PqsInspectStAudiTrackingController extends BaseWithDefValController<PqsInspectStAudiTrackingEntity> {

    private IPqsInspectStAudiTrackingService pqsInspectStAudiTrackingService;

    @Autowired
    public PqsInspectStAudiTrackingController(IPqsInspectStAudiTrackingService pqsInspectStAudiTrackingService) {
        this.crudService = pqsInspectStAudiTrackingService;
        this.pqsInspectStAudiTrackingService = pqsInspectStAudiTrackingService;
    }

}