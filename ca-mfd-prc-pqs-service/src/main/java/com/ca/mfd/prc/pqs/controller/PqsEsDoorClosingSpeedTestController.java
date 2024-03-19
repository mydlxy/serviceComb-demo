package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsDoorClosingSpeedTestEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsDoorClosingSpeedTestService;
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
 * @Description: 四车门关闭速度测试Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsesdoorclosingspeedtest")
@Tag(name = "四车门关闭速度测试服务", description = "四车门关闭速度测试")
public class PqsEsDoorClosingSpeedTestController extends BaseWithDefValController<PqsEsDoorClosingSpeedTestEntity> {

    private IPqsEsDoorClosingSpeedTestService pqsEsDoorClosingSpeedTestService;

    @Autowired
    public PqsEsDoorClosingSpeedTestController(IPqsEsDoorClosingSpeedTestService pqsEsDoorClosingSpeedTestService) {
        this.crudService = pqsEsDoorClosingSpeedTestService;
        this.pqsEsDoorClosingSpeedTestService = pqsEsDoorClosingSpeedTestService;
    }

}