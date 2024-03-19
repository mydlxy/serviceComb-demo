package com.ca.mfd.prc.pps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.communication.dto.AsQueueStartDto;
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
import java.util.List;

/**
 * @author inkelink
 * @Description: AS待开工队列Controller
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@RestController
@RequestMapping("communication/queuestart")
@Tag(name = "AS待开工队列", description = "AS待开工队列")
public class MidAsQueueStartController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(MidAsQueueStartController.class);
    @Autowired
    private IMidApiLogService midApiLogService;


    @GetMapping(value = "send")
    @Operation(summary = "AS待开工队列发送")
    public ResultVO<String> send(@RequestBody List<AsQueueStartDto> fbacks) {
        return midApiLogService.sendQueueStart(fbacks);
    }

    @GetMapping(value = "test")
    @Operation(summary = "AS待开工队列发送test")
    public ResultVO<String> test() {
        List<AsQueueStartDto> dtos = new ArrayList<>();
        AsQueueStartDto et = new AsQueueStartDto();
        et.setOrgCode("CA");
        et.setBeforeOnlineSeq("1");
        et.setVin("L1NSPGHB1PB080534");
        et.setVrn("1000009");
        dtos.add(et);
        return midApiLogService.sendQueueStart(dtos);
    }

}