package com.ca.mfd.prc.pps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.communication.dto.AsKeepCarDto;
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
 * @Description: AS保留车接口Controller
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@RestController
@RequestMapping("communication/keepcar")
@Tag(name = "AS保留车接口", description = "AS报废车辆接口")
public class MidAsKeepCarController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(MidAsKeepCarController.class);
    @Autowired
    private IMidApiLogService midApiLogService;


    @GetMapping(value = "send")
    @Operation(summary = "AS保留车记录发送")
    public ResultVO<String> send(@RequestBody List<AsKeepCarDto> fbacks) {
        return midApiLogService.sendKeepCar(fbacks);
    }

    @GetMapping(value = "test")
    @Operation(summary = "AS保留车记录发送test")
    public ResultVO<String> test() {
        List<AsKeepCarDto> dtos = new ArrayList<>();
        AsKeepCarDto et = new AsKeepCarDto();
        et.setOrgCode("CA");
        et.setHoldType("1");
        et.setHoldTime(new Date());
        et.setUlocNo("");
        et.setVin("L1NSPGHB1PB080534");
        et.setVrn("1000009");
        dtos.add(et);
        return midApiLogService.sendKeepCar(dtos);
    }

}