package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectDimeRequEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectDimeRequService;
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
 * @Description: 监控-尺寸监控要求Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectdimerequ")
@Tag(name = "监控-尺寸监控要求服务", description = "监控-尺寸监控要求")
public class PqsInspectDimeRequController extends BaseWithDefValController<PqsInspectDimeRequEntity> {

    private IPqsInspectDimeRequService pqsInspectDimeRequService;

    @Autowired
    public PqsInspectDimeRequController(IPqsInspectDimeRequService pqsInspectDimeRequService) {
        this.crudService = pqsInspectDimeRequService;
        this.pqsInspectDimeRequService = pqsInspectDimeRequService;
    }

}