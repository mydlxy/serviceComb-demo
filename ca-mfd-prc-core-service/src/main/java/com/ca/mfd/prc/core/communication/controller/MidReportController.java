package com.ca.mfd.prc.core.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.core.report.dto.EditStatusDto;
import com.ca.mfd.prc.core.report.entity.RptPrinterEntity;
import com.ca.mfd.prc.core.report.service.IRptPrinterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("communication/report")
@Tag(name = "打印对外服务", description = "打印对外服务")
public class MidReportController extends BaseApiController {

    @Autowired
    private IRptPrinterService rptPrinterService;

    @Operation(summary = "根据打印机ID修改打印状态")
    @PostMapping("editstatus")
    public void editStatus(@RequestBody EditStatusDto dto) {
        rptPrinterService.editStatus(dto);
    }


    @Operation(summary = "根据打印代码获取打印机配置列表")
    @PostMapping("getdata/{bizCode}")
    public List<RptPrinterEntity> getdatas(@PathVariable("bizCode") String bizCode) {
        return rptPrinterService.getData(bizCode);
    }
}
