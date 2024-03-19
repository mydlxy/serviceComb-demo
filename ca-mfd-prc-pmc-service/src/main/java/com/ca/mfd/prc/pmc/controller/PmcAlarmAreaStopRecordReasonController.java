package com.ca.mfd.prc.pmc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.dto.AuditMeasureInfoDTO;
import com.ca.mfd.prc.pmc.entity.PmcAlarmAreaStopRecordReasonEntity;
import com.ca.mfd.prc.pmc.service.IPmcAlarmAreaStopRecordReasonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 停线记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("pmcalarmareastoprecordreason")
@Tag(name = "停线记录")
public class PmcAlarmAreaStopRecordReasonController extends BaseController<PmcAlarmAreaStopRecordReasonEntity> {

    private final IPmcAlarmAreaStopRecordReasonService pmcAlarmAreaStopRecordReasonService;

    @Autowired
    public PmcAlarmAreaStopRecordReasonController(IPmcAlarmAreaStopRecordReasonService pmcAlarmAreaStopRecordReasonService) {
        this.crudService = pmcAlarmAreaStopRecordReasonService;
        this.pmcAlarmAreaStopRecordReasonService = pmcAlarmAreaStopRecordReasonService;
    }

    @Operation(summary = "措施审核")
    @PostMapping("auditmeasure")
    public ResultVO<String> auditMeasure(@RequestBody AuditMeasureInfoDTO auditMeasureInfo) {
        pmcAlarmAreaStopRecordReasonService.auditMeasure(auditMeasureInfo);
        return new ResultVO<String>().ok("操作成功");
    }


}