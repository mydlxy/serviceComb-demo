package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.QgRiskOperInfo;
import com.ca.mfd.prc.pqs.dto.RiskDetailActiveInfo;
import com.ca.mfd.prc.pqs.dto.RiskProductFilterDto;
import com.ca.mfd.prc.pqs.dto.RiskProductListFilterInfo;
import com.ca.mfd.prc.pqs.dto.RiskRepairInfo;
import com.ca.mfd.prc.pqs.dto.SubmitRiskInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyRiskEntity;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyRiskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author inkelink
 * @Description: 质量围堵Controller
 * @date 2023年09月07日
 * @变更说明 BY inkelink At 2023年09月07日
 */
@RestController
@RequestMapping("pqsdefectanomalyrisk")
@Tag(name = "质量围堵服务", description = "质量围堵")
public class PqsDefectAnomalyRiskController extends BaseController<PqsDefectAnomalyRiskEntity> {

    private final IPqsDefectAnomalyRiskService pqsDefectAnomalyRiskService;

    @Autowired
    public PqsDefectAnomalyRiskController(IPqsDefectAnomalyRiskService pqsDefectAnomalyRiskService) {
        this.crudService = pqsDefectAnomalyRiskService;
        this.pqsDefectAnomalyRiskService = pqsDefectAnomalyRiskService;
    }

    /**
     * 查询铸造、机加、冲压的问题
     *
     * @param model
     * @return
     */
    @PostMapping(value = "getpagedatabycategoryyzjjcy", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取铸造、机加、冲压分页数据")
    public ResultVO<PageData<PqsDefectAnomalyRiskEntity>> getPageDataByCategoryYzJjCy(@RequestBody PageDataDto model) {
        List<ConditionDto> conditions = new ArrayList<>(1);
        List<String> categorys = Arrays.asList("2", "3", "4");
        conditions.add(new ConditionDto("category", String.join("|", categorys), ConditionOper.In));
        model.setConditions(conditions);
        PageData<PqsDefectAnomalyRiskEntity> page = crudService.page(model);
        return new ResultVO<PageData<PqsDefectAnomalyRiskEntity>>().ok(page, "获取数据成功");
    }



    /**
     * 获取排查产品列表
     *
     * @param riskProductListFilterRequest 问题排查筛选条件
     * @return
     */
    @PostMapping("getproductlist")
    @Operation(summary = "获取排查产品列表")
    public ResultVO<List<RiskProductFilterDto>> getProductList(@RequestBody RiskProductListFilterInfo riskProductListFilterRequest) {
        return new ResultVO<List<RiskProductFilterDto>>().ok(pqsDefectAnomalyRiskService.getProductList(riskProductListFilterRequest), "操作成功");
    }

    /**
     * 发起问题排查
     *
     * @param submitRiskRequest 问题信息
     * @return
     */
    @PostMapping("submitriskinfo")
    @Operation(summary = "发起问题排查")
    public ResultVO submitRiskInfo(@RequestBody SubmitRiskInfo submitRiskRequest) {
        String result = pqsDefectAnomalyRiskService.submitRiskInfo(submitRiskRequest);
        pqsDefectAnomalyRiskService.saveChange();
        return new ResultVO<String>().ok(result, "操作成功");
    }

    /**
     * 根据问题批量激活缺陷
     *
     * @param request 问题信息
     * @return
     */
    @PostMapping("triggeranomalybyriskid")
    @Operation(summary = "根据问题批量激活缺陷")
    public ResultVO triggerAnomalyByRiskId(@RequestBody QgRiskOperInfo request) {
        pqsDefectAnomalyRiskService.triggerAnomalyByRiskId(request.getId(), request.getWorkstationCode());
        pqsDefectAnomalyRiskService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 根据问题明细-批量激活缺陷
     *
     * @param request 问题明细信息
     * @return
     */
    @PostMapping("triggeranomalybyriskdetailid")
    @Operation(summary = "根据问题明细-批量激活缺陷")
    public ResultVO triggerAnomalyByRiskDetailId(@RequestBody RiskDetailActiveInfo request) {
        pqsDefectAnomalyRiskService.triggerAnomalyByRiskDetailId(request.getIds(), request.getWorkstationCode());
        pqsDefectAnomalyRiskService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 根据问题ID 批量修复缺陷
     *
     * @param request 问题信息
     * @return
     */
    @PostMapping("repairbyriskid")
    @Operation(summary = "根据问题ID 批量修复缺陷")
    public ResultVO repairByRiskId(@RequestBody RiskRepairInfo request) {
        pqsDefectAnomalyRiskService.repairByRiskId(request);
        pqsDefectAnomalyRiskService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 根据问题明细ID 批量修复缺陷
     *
     * @param request 修复信息
     * @return
     */
    @PostMapping("repairbyriskdetailid")
    @Operation(summary = "根据问题明细ID 激活问题")
    public ResultVO repairByRiskDetailId(@RequestBody RiskRepairInfo request) {
        pqsDefectAnomalyRiskService.repairByRiskDetailId(request);
        pqsDefectAnomalyRiskService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 根据问题明细ID 批量修复缺陷
     *
     * @param riskRecheck 修复信息
     * @return
     */
    @PostMapping("recheck")
    @Operation(summary = "根据问题明细ID 批量修复缺陷")
    public ResultVO recheck(@RequestBody RiskRepairInfo riskRecheck) {
        pqsDefectAnomalyRiskService.recheck(riskRecheck);
        pqsDefectAnomalyRiskService.saveChange();
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
        pqsDefectAnomalyRiskService.closeRisk(model);
        pqsDefectAnomalyRiskService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }
}