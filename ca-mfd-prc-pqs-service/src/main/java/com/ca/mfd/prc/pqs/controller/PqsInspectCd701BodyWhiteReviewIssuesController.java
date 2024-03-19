package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectCd701BodyWhiteReviewIssuesEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectCd701BodyWhiteReviewIssuesService;
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
 * @Description: CD701白车身评审问题Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectcd701bodywhitereviewissues")
@Tag(name = "CD701白车身评审问题服务", description = "CD701白车身评审问题")
public class PqsInspectCd701BodyWhiteReviewIssuesController extends BaseWithDefValController<PqsInspectCd701BodyWhiteReviewIssuesEntity> {

    private IPqsInspectCd701BodyWhiteReviewIssuesService pqsInspectCd701BodyWhiteReviewIssuesService;

    @Autowired
    public PqsInspectCd701BodyWhiteReviewIssuesController(IPqsInspectCd701BodyWhiteReviewIssuesService pqsInspectCd701BodyWhiteReviewIssuesService) {
        this.crudService = pqsInspectCd701BodyWhiteReviewIssuesService;
        this.pqsInspectCd701BodyWhiteReviewIssuesService = pqsInspectCd701BodyWhiteReviewIssuesService;
    }

}