package com.ca.mfd.prc.pps.communication.controller;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.service.IYiDianTongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 伊点通接口
 */
@RestController
@RequestMapping("communication/yidiantong")
@Tag(name = "伊点通接口")
public class YiDianTongController {

    @Autowired
    private IYiDianTongService yiDianTongService;
    @GetMapping("/ordercodeissued")
    @Operation(summary = "工单号下发接口")
    public ResultVO<Boolean> orderCodeIssued(String processCode) {
        return yiDianTongService.sendOrderNos(processCode);
    }
}
