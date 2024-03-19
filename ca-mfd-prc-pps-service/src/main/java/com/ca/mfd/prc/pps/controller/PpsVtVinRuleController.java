package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.entity.PpsVtVinRuleEntity;
import com.ca.mfd.prc.pps.service.IPpsOrderService;
import com.ca.mfd.prc.pps.service.IPpsVtVinRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * VIN配置,前7位
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@RestController
@RequestMapping("ppsvtvinrule")
@Tag(name = "VIN配置,前7位")
public class PpsVtVinRuleController extends BaseController<PpsVtVinRuleEntity> {

    private final IPpsVtVinRuleService ppsVtVinRuleService;

    @Autowired
    public PpsVtVinRuleController(IPpsVtVinRuleService ppsVtVinRuleService) {
        this.crudService = ppsVtVinRuleService;
        this.ppsVtVinRuleService = ppsVtVinRuleService;
    }

    @Autowired
    private IPpsOrderService ppsOrderService;

    @Operation(summary = "获取所有")
    @GetMapping("getppsvtvinrulealls")
    public ResultVO getPpsVtVinRuleAlls() {
        return new ResultVO<List<PpsVtVinRuleEntity>>().ok(ppsVtVinRuleService.getAllDatas(), "获取列表成功！");
    }

    @Operation(summary = "获取所有")
    @GetMapping("getppsvtvinrulecoms")
    public ResultVO getPpsVtVinRuleComs() {
        return new ResultVO<List<ComboInfoDTO>>().ok(ppsVtVinRuleService.getVinList(), "获取列表成功！");
    }

    @Operation(summary = "获取VIN值")
    @GetMapping("getvincode")
    public ResultVO getvincode(String sn) {
        return new ResultVO<String>().ok(ppsVtVinRuleService.createVin(ppsOrderService.getPpsOrderBySn(sn)), "获取成功！");
    }

}