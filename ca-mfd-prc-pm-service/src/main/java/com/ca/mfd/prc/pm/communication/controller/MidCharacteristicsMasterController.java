package com.ca.mfd.prc.pm.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.communication.service.IMidCharacteristicsMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 特征主数据Controller
 * @author inkelink
 * @date 2023年10月24日
 * @变更说明 BY inkelink At 2023年10月24日
 */
@RestController
@RequestMapping("communication/midbomcharacteristicsmain")
@Tag(name = "特征主数据服务", description = "特征主数据")
public class MidCharacteristicsMasterController extends BaseApiController {

    @Autowired
    private IMidCharacteristicsMasterService characteristicsMasterService;

    @GetMapping(value = "receive")
    @Operation(summary = "保存")
    public void receive(String startDate,String endDate) {
        characteristicsMasterService.receive(startDate,endDate);
    }

    @GetMapping(value = "excute")
    @Operation(summary = "特征主数据执行数据处理逻辑")
    public ResultVO<String> excute(String logid) {
        characteristicsMasterService.excute(logid);
        return new ResultVO<String>().ok("", "成功");
    }
}