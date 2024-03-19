package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsFrontHeadlightLightTestRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsFrontHeadlightLightTestRecordService;
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
 * @Description: 前大灯灯光测试记录Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsesfrontheadlightlighttestrecord")
@Tag(name = "前大灯灯光测试记录服务", description = "前大灯灯光测试记录")
public class PqsEsFrontHeadlightLightTestRecordController extends BaseWithDefValController<PqsEsFrontHeadlightLightTestRecordEntity> {

    private IPqsEsFrontHeadlightLightTestRecordService pqsEsFrontHeadlightLightTestRecordService;

    @Autowired
    public PqsEsFrontHeadlightLightTestRecordController(IPqsEsFrontHeadlightLightTestRecordService pqsEsFrontHeadlightLightTestRecordService) {
        this.crudService = pqsEsFrontHeadlightLightTestRecordService;
        this.pqsEsFrontHeadlightLightTestRecordService = pqsEsFrontHeadlightLightTestRecordService;
    }

}