package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectIlluminationDetectiongEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectIlluminationDetectiongService;
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
 * @Description: 冲压车间照度检测记录Controller
 * @author bo.yang
 * @date 2024年03月14日
 * @变更说明 BY bo.yang At 2024年03月14日
 */
@RestController
@RequestMapping("pqsinspectilluminationdetectiong")
@Tag(name = "冲压车间照度检测记录服务", description = "冲压车间照度检测记录")
public class PqsInspectIlluminationDetectiongController extends BaseWithDefValController<PqsInspectIlluminationDetectiongEntity> {

    private IPqsInspectIlluminationDetectiongService pqsInspectIlluminationDetectiongService;

    @Autowired
    public PqsInspectIlluminationDetectiongController(IPqsInspectIlluminationDetectiongService pqsInspectIlluminationDetectiongService) {
        this.crudService = pqsInspectIlluminationDetectiongService;
        this.pqsInspectIlluminationDetectiongService = pqsInspectIlluminationDetectiongService;
    }

}