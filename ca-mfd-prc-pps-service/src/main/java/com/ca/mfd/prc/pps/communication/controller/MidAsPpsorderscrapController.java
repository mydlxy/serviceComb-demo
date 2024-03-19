package com.ca.mfd.prc.pps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.communication.dto.AsOrderScrapDto;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author inkelink
 * @Description: AS报废车辆接口Controller
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@RestController
@RequestMapping("communication/ppsorderscrap")
@Tag(name = "AS报废车辆接口", description = "AS报废车辆接口")
public class MidAsPpsorderscrapController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(MidAsPpsorderscrapController.class);
    @Autowired
    private IMidApiLogService midApiLogService;


    @GetMapping(value = "send")
    @Operation(summary = "AS报废车辆记录发送")
    public ResultVO<String> send(@RequestBody List<AsOrderScrapDto> fbacks) {
        return midApiLogService.sendOrderScrap(fbacks);
    }

    @GetMapping(value = "test")
    @Operation(summary = "AS报废车辆记录发送test")
    public ResultVO<String> test() {
        List<AsOrderScrapDto> dtos = new ArrayList<>();
        AsOrderScrapDto et = new AsOrderScrapDto();
        et.setOrgCode("CA");
        et.setScrapTime(new Date());
        et.setVin("L1NSPGHB1PB080534");
        et.setVrn("1000009");
        dtos.add(et);
        return midApiLogService.sendOrderScrap(dtos);
    }
}