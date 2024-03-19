package com.ca.mfd.prc.core.report.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.report.dto.ReportQueueDTO;
import com.ca.mfd.prc.core.report.entity.RptSendEntity;
import com.ca.mfd.prc.core.report.service.IRptSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 报表请求记录Controller
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
@RestController
@RequestMapping("report/rptsend")
@Tag(name = "报表请求记录服务", description = "报表请求记录")
public class RptSendController extends BaseController<RptSendEntity> {

    private final IRptSendService rptSendService;

    @Autowired
    public RptSendController(IRptSendService rptSendService) {
        this.crudService = rptSendService;
        this.rptSendService = rptSendService;
    }


    /*@Operation(summary = "打印测试")
    @PostMapping("test")
    public ResultVO<String> store(@RequestBody ReportQueueDTO model) {
        rptSendService.addReportQueue(model);
        rptSendService.saveChange();
        return new ResultVO<String>().ok("保存成功", "保存成功");
    }*/
}