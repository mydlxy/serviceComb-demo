package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.CopyStrategyPara;
import com.ca.mfd.prc.pps.dto.IdDto;
import com.ca.mfd.prc.pps.entity.PpsModuleSplitStrategyEntity;
import com.ca.mfd.prc.pps.service.IPpsModuleSplitStrategyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 电池结构配置Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("ppsmodulesplitstrategy")
@Tag(name = "电池结构配置服务", description = "电池结构配置")
public class PpsModuleSplitStrategyController extends BaseController<PpsModuleSplitStrategyEntity> {

    private IPpsModuleSplitStrategyService ppsModuleSplitStrategyService;

    @Autowired
    public PpsModuleSplitStrategyController(IPpsModuleSplitStrategyService ppsModuleSplitStrategyService) {
        this.crudService = ppsModuleSplitStrategyService;
        this.ppsModuleSplitStrategyService = ppsModuleSplitStrategyService;
    }

    @Operation(summary = "启用策略")
    @PostMapping("enablestrategy")
    public ResultVO enableStrategy(@RequestBody IdDto para) {
        ppsModuleSplitStrategyService.enableStrategy(ConvertUtils.stringToLong(para.getId()));
        ppsModuleSplitStrategyService.saveChange();
        return new ResultVO<String>().ok("","操作成功");
    }

}