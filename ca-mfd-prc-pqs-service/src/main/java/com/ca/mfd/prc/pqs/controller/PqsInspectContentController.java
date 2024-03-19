package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectContentEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectContentService;
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
 * @Description: 各车间数据监控内容Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectcontent")
@Tag(name = "各车间数据监控内容服务", description = "各车间数据监控内容")
public class PqsInspectContentController extends BaseWithDefValController<PqsInspectContentEntity> {

    private IPqsInspectContentService pqsInspectContentService;

    @Autowired
    public PqsInspectContentController(IPqsInspectContentService pqsInspectContentService) {
        this.crudService = pqsInspectContentService;
        this.pqsInspectContentService = pqsInspectContentService;
    }

}