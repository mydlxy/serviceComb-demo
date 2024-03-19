package com.ca.mfd.prc.pmc.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.service.IAlarmIntegrate;
import com.ca.mfd.prc.pmc.service.IAlarmIntegrate2;
import com.ca.mfd.prc.pmc.service.IAutoCreateModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PMC模块调度接口
 *
 * @author inkelink
 * @date 2023年4月4日
 */
@RestController
@RequestMapping("scheduling")
@Tag(name = "PMC模块调度接口")
public class SchedulingController extends BaseApiController {

    @Autowired
    private IAlarmIntegrate alarmIntegrate;
    @Autowired
    private IAlarmIntegrate2 alarmIntegrate2;
    @Autowired
    private IAutoCreateModel autoCreateModel;

    @Operation(summary = "报警数据第一层ETL")
    @GetMapping("alarmintegratejob")
    public ResultVO alarmintegratejob() {
        ResultVO result = new ResultVO<>();
        result.setMessage("触发成功");
        alarmIntegrate.start();
        return result.ok("");
    }

    @Operation(summary = "报警数据第二层ETL")
    @GetMapping("alarmintegratejob2")
    public ResultVO alarmintegratejob2() {
        ResultVO result = new ResultVO<>();
        result.setMessage("触发成功");
        alarmIntegrate2.start();
        return result.ok("");
    }

    @Operation(summary = "通过报警自动生成设备建模数据")
    @GetMapping("autocreatemodeljob")
    public ResultVO autoCreateModelJob() {
        ResultVO result = new ResultVO<>();
        result.setMessage("触发成功");
        autoCreateModel.start();
        return result.ok("");
    }
}