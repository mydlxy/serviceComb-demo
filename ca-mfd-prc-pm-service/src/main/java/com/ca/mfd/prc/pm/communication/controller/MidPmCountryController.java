package com.ca.mfd.prc.pm.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.communication.service.IMidPmCountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 国家代码中间表服务Controller
 * @date 2023年10月19日
 * @变更说明 BY inkelink At 2023年10月19日
 */
@RestController
@RequestMapping("communication/pmcountry")
@Tag(name = "国家代码中间表服务", description = "国家代码中间表服务")
public class MidPmCountryController extends BaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(MidPmCountryController.class);

    @Autowired
    private IMidPmCountryService midPmCountryService;

    @GetMapping(value = "excute")
    @Operation(summary = "执行数据处理逻辑")
    public ResultVO<String> excute(String logid) {
        midPmCountryService.excute(logid);
        return new ResultVO<String>().ok("", "成功");
    }
}