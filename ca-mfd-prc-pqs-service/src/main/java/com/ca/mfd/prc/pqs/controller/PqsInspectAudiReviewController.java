package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectAudiReviewEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectAudiReviewService;
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
 * @Description: 车间内部奥迪特评审报告Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectaudireview")
@Tag(name = "车间内部奥迪特评审报告服务", description = "车间内部奥迪特评审报告")
public class PqsInspectAudiReviewController extends BaseWithDefValController<PqsInspectAudiReviewEntity> {

    private IPqsInspectAudiReviewService pqsInspectAudiReviewService;

    @Autowired
    public PqsInspectAudiReviewController(IPqsInspectAudiReviewService pqsInspectAudiReviewService) {
        this.crudService = pqsInspectAudiReviewService;
        this.pqsInspectAudiReviewService = pqsInspectAudiReviewService;
    }

}