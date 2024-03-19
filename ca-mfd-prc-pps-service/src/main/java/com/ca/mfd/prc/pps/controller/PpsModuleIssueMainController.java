package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.ModuleMainDetailDto;
import com.ca.mfd.prc.pps.entity.PpsModuleIssueMainEntity;
import com.ca.mfd.prc.pps.entity.PpsModuleIssueModuleEntity;
import com.ca.mfd.prc.pps.service.IPpsModuleIssueMainService;
import com.ca.mfd.prc.pps.service.IPpsModuleIssueModuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @Description: 电池预成组下发主体Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("ppsmoduleissuemain")
@Tag(name = "电池预成组下发主体服务", description = "电池预成组下发主体")
public class PpsModuleIssueMainController extends BaseController<PpsModuleIssueMainEntity> {

    @Autowired
    private IPpsModuleIssueModuleService ppsModuleIssueModuleService;

    private IPpsModuleIssueMainService ppsModuleIssueMainService;

    @Autowired
    public PpsModuleIssueMainController(IPpsModuleIssueMainService ppsModuleIssueMainService) {
        this.crudService = ppsModuleIssueMainService;
        this.ppsModuleIssueMainService = ppsModuleIssueMainService;
    }

    @Operation(summary = "获取下发主体详情")
    @GetMapping("modulemaindetail")
    public ResultVO moduleMainDetail(String entryNo) {
        PpsModuleIssueMainEntity mainInfo = ppsModuleIssueMainService.getFirstByEntryNo(entryNo);
        List<PpsModuleIssueModuleEntity> modules = ppsModuleIssueModuleService.getListByMainId(mainInfo.getId());
        ModuleMainDetailDto res = new ModuleMainDetailDto();
        res.setMain(mainInfo);
        res.setModules(modules);
        return new ResultVO<ModuleMainDetailDto>().ok(res, "操作成功");
    }

}