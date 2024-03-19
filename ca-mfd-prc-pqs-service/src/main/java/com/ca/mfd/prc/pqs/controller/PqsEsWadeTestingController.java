package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsWadeTestingEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsWadeTestingService;
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
 * @Description: 涉水测试Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqseswadetesting")
@Tag(name = "涉水测试服务", description = "涉水测试")
public class PqsEsWadeTestingController extends BaseWithDefValController<PqsEsWadeTestingEntity> {

    private IPqsEsWadeTestingService pqsEsWadeTestingService;

    @Autowired
    public PqsEsWadeTestingController(IPqsEsWadeTestingService pqsEsWadeTestingService) {
        this.crudService = pqsEsWadeTestingService;
        this.pqsEsWadeTestingService = pqsEsWadeTestingService;
    }

}