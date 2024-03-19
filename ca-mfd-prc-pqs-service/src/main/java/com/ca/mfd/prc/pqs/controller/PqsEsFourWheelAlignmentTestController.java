package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsFourWheelAlignmentTestEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsFourWheelAlignmentTestService;
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
 * @Description: 四轮定位测试Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsesfourwheelalignmenttest")
@Tag(name = "四轮定位测试服务", description = "四轮定位测试")
public class PqsEsFourWheelAlignmentTestController extends BaseWithDefValController<PqsEsFourWheelAlignmentTestEntity> {

    private IPqsEsFourWheelAlignmentTestService pqsEsFourWheelAlignmentTestService;

    @Autowired
    public PqsEsFourWheelAlignmentTestController(IPqsEsFourWheelAlignmentTestService pqsEsFourWheelAlignmentTestService) {
        this.crudService = pqsEsFourWheelAlignmentTestService;
        this.pqsEsFourWheelAlignmentTestService = pqsEsFourWheelAlignmentTestService;
    }

}