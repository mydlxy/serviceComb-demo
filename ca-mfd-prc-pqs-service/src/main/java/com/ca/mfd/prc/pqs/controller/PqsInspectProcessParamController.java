package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectProcessParamEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectProcessParamService;
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
 * @Description: 过程参数残扭抽检表Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectprocessparam")
@Tag(name = "过程参数残扭抽检表服务", description = "过程参数残扭抽检表")
public class PqsInspectProcessParamController extends BaseWithDefValController<PqsInspectProcessParamEntity> {

    private IPqsInspectProcessParamService pqsInspectProcessParamService;

    @Autowired
    public PqsInspectProcessParamController(IPqsInspectProcessParamService pqsInspectProcessParamService) {
        this.crudService = pqsInspectProcessParamService;
        this.pqsInspectProcessParamService = pqsInspectProcessParamService;
    }

}