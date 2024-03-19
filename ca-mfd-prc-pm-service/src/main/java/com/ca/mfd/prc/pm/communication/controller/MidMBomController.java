package com.ca.mfd.prc.pm.communication.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.communication.entity.MidMBomEntity;
import com.ca.mfd.prc.pm.communication.service.IMidMBomService;
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
 * @Description: 制造域BOMController
 * @author inkelink
 * @date 2023年12月08日
 * @变更说明 BY inkelink At 2023年12月08日
 */
@RestController
@RequestMapping("communication/midmbom")
@Tag(name = "制造域BOM服务", description = "制造域BOM")
public class MidMBomController extends BaseApiController {

    @Autowired
    private IMidMBomService midMBomService;

    @GetMapping(value = "receive")
    @Operation(summary = "MBOM保存")
    public ResultVO<String> receive(String orgCode,String specifyDate,String bomRoom) {
        int receiveNum = midMBomService.receive(orgCode,specifyDate,bomRoom);
        return new ResultVO<String>().ok(String.valueOf(receiveNum), "成功");
    }

    @GetMapping(value = "excute")
    @Operation(summary = "MBOM执行数据处理逻辑")
    public ResultVO<String> excute(String logid) {
        midMBomService.excute(logid);
        return new ResultVO<String>().ok("", "成功");
    }

}