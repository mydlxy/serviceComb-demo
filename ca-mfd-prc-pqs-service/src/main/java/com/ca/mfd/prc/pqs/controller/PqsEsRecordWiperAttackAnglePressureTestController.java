package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsRecordWiperAttackAnglePressureTestEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsRecordWiperAttackAnglePressureTestService;
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
 * @Description: 雨刮攻击角和压力测试记录Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsesrecordwiperattackanglepressuretest")
@Tag(name = "雨刮攻击角和压力测试记录服务", description = "雨刮攻击角和压力测试记录")
public class PqsEsRecordWiperAttackAnglePressureTestController extends BaseWithDefValController<PqsEsRecordWiperAttackAnglePressureTestEntity> {

    private IPqsEsRecordWiperAttackAnglePressureTestService pqsEsRecordWiperAttackAnglePressureTestService;

    @Autowired
    public PqsEsRecordWiperAttackAnglePressureTestController(IPqsEsRecordWiperAttackAnglePressureTestService pqsEsRecordWiperAttackAnglePressureTestService) {
        this.crudService = pqsEsRecordWiperAttackAnglePressureTestService;
        this.pqsEsRecordWiperAttackAnglePressureTestService = pqsEsRecordWiperAttackAnglePressureTestService;
    }

}