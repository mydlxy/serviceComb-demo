package com.ca.mfd.prc.avi.host.controller;

import com.ca.mfd.prc.avi.host.scheduling.IEntryReportPassAviService;
import com.ca.mfd.prc.avi.host.scheduling.IEntryReportStatisticsService;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("scheduling")
@Tag(name = "调度")
public class SchedulingController extends BaseApiController {
    @Autowired
    IEntryReportPassAviService entryReportPassAviService;

    @Autowired
    IEntryReportStatisticsService entryReportStatisticsService;

    /**
     * 报工触发过点
     *
     * @param orderCategory
     * @return
     * @throws Exception
     */
    @GetMapping("entryreportpassavijob/{orderCategory}")
    @Operation(summary = "报工触发过点")
    public ResultVO<String> entryReportPassAviJob(@PathVariable("orderCategory") int orderCategory) {
        entryReportPassAviService.start(orderCategory);
        return new ResultVO<String>().ok("触发成功");
    }

    /**
     * 报工触发过点
     *
     * @param orderCategory
     * @return
     * @throws Exception
     */
    @GetMapping("entryreportasprocessjob/{orderCategory}")
    @Operation(summary = "报工触发过点")
    public ResultVO<String> entryreportAsProcessJob(@PathVariable("orderCategory") int orderCategory) {
        entryReportStatisticsService.start(orderCategory);
        return new ResultVO<String>().ok("触发成功");
    }
}
