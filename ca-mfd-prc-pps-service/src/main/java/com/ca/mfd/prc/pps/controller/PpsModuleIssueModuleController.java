package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.ModuleDetailInfo;
import com.ca.mfd.prc.pps.entity.PpsModuleIssueModuleEntity;
import com.ca.mfd.prc.pps.service.IPpsModuleIssueModuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 电池预成组下发模组Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("ppsmoduleissuemodule")
@Tag(name = "电池预成组下发模组服务", description = "电池预成组下发模组")
public class PpsModuleIssueModuleController extends BaseController<PpsModuleIssueModuleEntity> {

    private IPpsModuleIssueModuleService ppsModuleIssueModuleService;

    @Autowired
    public PpsModuleIssueModuleController(IPpsModuleIssueModuleService ppsModuleIssueModuleService) {
        this.crudService = ppsModuleIssueModuleService;
        this.ppsModuleIssueModuleService = ppsModuleIssueModuleService;
    }

    @Operation(summary = "获取详情")
    @GetMapping("moduledetail")
    public ResultVO moduleDetail(String id) {
        ModuleDetailInfo data = ppsModuleIssueModuleService.moduleDetail(ConvertUtils.stringToLong(id));
        return new ResultVO<ModuleDetailInfo>().ok(data,"操作成功");
    }

}