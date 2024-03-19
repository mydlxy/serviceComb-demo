package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.EntryAndPlanParasDto;
import com.ca.mfd.prc.pqs.dto.RiskRepairInfo;
import com.ca.mfd.prc.pqs.dto.SubmitBatchRiskInfo;
import com.ca.mfd.prc.pqs.entity.PqsBatchRiskEntity;
import com.ca.mfd.prc.pqs.service.IPqsBatchRiskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 批次件问题排查（质量围堵）Controller
 * @author inkelink
 * @date 2023年11月08日
 * @变更说明 BY inkelink At 2023年11月08日
 */
@RestController
@RequestMapping("pqsbatchrisk")
@Tag(name = "批次件问题排查（质量围堵）服务", description = "批次件问题排查（质量围堵）")
public class PqsBatchRiskController extends BaseController<PqsBatchRiskEntity> {

    private IPqsBatchRiskService pqsBatchRiskService;

    @Autowired
    public PqsBatchRiskController(IPqsBatchRiskService pqsBatchRiskService) {
        this.crudService = pqsBatchRiskService;
        this.pqsBatchRiskService = pqsBatchRiskService;
    }

    /**
     * 获取计划单号和工单号
     *
     * @param planNoOrEntryNo
     * @return
     */
    @GetMapping("getentrynoandplanno")
    @Operation(summary = "获取计划单号和工单号")
    public ResultVO<EntryAndPlanParasDto> getEntryNoAndPlanNo(String planNoOrEntryNo) {
        return new ResultVO<EntryAndPlanParasDto>().ok(pqsBatchRiskService.getEntryNoAndPlanNo(planNoOrEntryNo), "获取数据成功");
    }

    /**
     * 批次件发起问题排查
     *
     * @param info 问题信息
     * @return
     */
    @PostMapping("submitbatchriskinfo")
    @Operation(summary = "批次件发起问题排查")
    public ResultVO submitBatchRiskInfo(@RequestBody SubmitBatchRiskInfo info) {
        pqsBatchRiskService.submitBatchRiskInfo(info);
        pqsBatchRiskService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 根据问题ID 关闭问题
     *
     * @param model 问题ID
     * @return
     */
    @PostMapping("closerisk")
    @Operation(summary = "根据问题ID 关闭问题")
    public ResultVO closeRisk(@RequestBody RiskRepairInfo model) {
        pqsBatchRiskService.closeRisk(model);
        pqsBatchRiskService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }
}