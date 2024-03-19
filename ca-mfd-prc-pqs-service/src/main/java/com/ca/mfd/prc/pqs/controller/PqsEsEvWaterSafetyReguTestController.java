package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsEvWaterSafetyReguTestEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsEvWaterSafetyReguTestService;
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
 * @Description: EV涉水安规测试Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsesevwatersafetyregutest")
@Tag(name = "EV涉水安规测试服务", description = "EV涉水安规测试")
public class PqsEsEvWaterSafetyReguTestController extends BaseWithDefValController<PqsEsEvWaterSafetyReguTestEntity> {

    private IPqsEsEvWaterSafetyReguTestService pqsEsEvWaterSafetyReguTestService;

    @Autowired
    public PqsEsEvWaterSafetyReguTestController(IPqsEsEvWaterSafetyReguTestService pqsEsEvWaterSafetyReguTestService) {
        this.crudService = pqsEsEvWaterSafetyReguTestService;
        this.pqsEsEvWaterSafetyReguTestService = pqsEsEvWaterSafetyReguTestService;
    }

}