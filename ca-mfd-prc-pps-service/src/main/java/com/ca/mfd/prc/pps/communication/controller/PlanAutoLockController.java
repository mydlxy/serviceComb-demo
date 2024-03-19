package com.ca.mfd.prc.pps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.communication.service.IPlanAutoLockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 计划自动锁定Controller
 * @date 2023年10月24日
 * @变更说明 BY inkelink At 2023年10月24日
 */
@RestController
@RequestMapping("communication/planautolock")
@Tag(name = "计划自动锁定", description = "计划自动锁定")
public class PlanAutoLockController extends BaseApiController {
    @Autowired
    private IPlanAutoLockService planAutoLockService;

    @GetMapping(value = "excute")
    @Operation(summary = "执行数据处理逻辑")
    public ResultVO<String> excute(Integer status) {
        String msg = planAutoLockService.excute(status);
        if (StringUtils.isBlank(msg)) {
            return new ResultVO<String>().ok("", "成功");
        } else {
            return new ResultVO<String>().error(-1, msg);
        }
    }

    @GetMapping(value = "autolockpartsplan")
    @Operation(summary = "批次计划自动锁定")
    public ResultVO<String> autolockPartsPlan(Integer orderCategory) {
        String msg = planAutoLockService.autolockPartsPlan(orderCategory);
        if (StringUtils.isBlank(msg)) {
            return new ResultVO<String>().ok("", "成功");
        } else {
            return new ResultVO<String>().error(-1, msg);
        }
    }

}