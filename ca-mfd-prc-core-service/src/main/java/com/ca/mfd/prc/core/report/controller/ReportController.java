package com.ca.mfd.prc.core.report.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.report.dto.ReportDTO;
import com.ca.mfd.prc.core.report.dto.ReportPrintQueueDycVO;
import com.ca.mfd.prc.core.report.dto.ReportPrinterVO;
import com.ca.mfd.prc.core.report.entity.RptPrintQueueEntity;
import com.ca.mfd.prc.core.report.entity.RptPrinterEntity;
import com.ca.mfd.prc.core.report.service.IRptPrintQueueService;
import com.ca.mfd.prc.core.report.service.IRptPrinterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 打印外部开放接口
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
@RestController
@RequestMapping("report/report")
@Tag(name = "打印外部开放接口", description = "打印外部开放接口")
public class ReportController extends BaseApiController {
    @Autowired
    IRptPrinterService rptPrinterService;

    @Autowired
    IRptPrintQueueService rptPrintQueueService;
    //ReportPrinterVO

    @Operation(summary = "获取列表")
    @GetMapping("printer/list")
    public ResultVO<List<ReportPrinterVO>> printerList() {
        List<RptPrinterEntity> list = rptPrinterService.getListAll();
        List<ReportPrinterVO> itemsList = list.stream().map(s -> {
            ReportPrinterVO info = new ReportPrinterVO();
            info.setId(s.getId());
            info.setPrintName(s.getPrintName());
            info.setIp(s.getIp());
            info.setModel(s.getModel());
            return info;
        }).collect(Collectors.toList());
        return new ResultVO<List<ReportPrinterVO>>().ok(itemsList, "保存成功");
    }

    @Operation(summary = "打印队列数量查询")
    @GetMapping("queue/qty")
    public ResultVO<Long> queueQty() {
        Long printQueueQty = rptPrintQueueService.getQueueQty();
        return new ResultVO<Long>().ok(printQueueQty, "查询成功");
    }

    @Operation(summary = "打印队列")
    @PostMapping("queue/list")
    public ResultVO<List<ReportPrintQueueDycVO>> queueList(@RequestBody ReportDTO.IpsModel model) {
        List<ReportPrintQueueDycVO> printQueueQty = rptPrintQueueService.queueList(model.getIps());
        return new ResultVO<List<ReportPrintQueueDycVO>>().ok(printQueueQty, "查询成功");
    }


    @Operation(summary = "根据ID完成打印更新")
    @PostMapping("queue/printed")
    public ResultVO<String> queuePrinted(@RequestBody ReportDTO.IdModel model) {
        RptPrintQueueEntity queue = rptPrintQueueService.get(model.getId());
        int count = queue.getPrintQty() + 1;
        rptPrintQueueService.updatePrintQtyById(2, count, model.getId());
        rptPrintQueueService.saveChange();
        return new ResultVO<String>().ok("完成打印更新", "完成打印更新");
    }

    @Operation(summary = "完成打印更新")
    @PostMapping("queue/printstatus")
    public ResultVO<String> queuePrintStatus(@RequestBody ReportDTO.PrintStatusModel model) {
        rptPrinterService.updateQueuePrintStatus(model.getText(), model.getId());
        rptPrintQueueService.saveChange();
        return new ResultVO<String>().ok("完成打印更新", "完成打印更新");
    }


}
