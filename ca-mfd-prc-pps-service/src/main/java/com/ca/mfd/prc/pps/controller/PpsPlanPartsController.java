package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.dto.LongIdsListModel;
import com.ca.mfd.prc.common.model.basedto.TemplateModel;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.constant.Constant;
import com.ca.mfd.prc.pps.dto.ModuleOrderListInfo;
import com.ca.mfd.prc.pps.dto.ModuleSplitDataInfo;
import com.ca.mfd.prc.pps.dto.PlanPartsSplitEntryReckonInfo;
import com.ca.mfd.prc.pps.dto.PlanPartsSplitEntryReckonPara;
import com.ca.mfd.prc.pps.entity.PpsPlanPartsEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pps.service.IPpsPlanPartsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author inkelink
 * @Description: 生产计划-零部件Controller
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("ppsplanparts")
@Tag(name = "生产计划-零部件服务", description = "生产计划-零部件")
public class PpsPlanPartsController extends BaseController<PpsPlanPartsEntity> {

    private IPpsPlanPartsService ppsPlanPartsService;

    @Autowired
    public PpsPlanPartsController(IPpsPlanPartsService ppsPlanPartsService) {
        this.crudService = ppsPlanPartsService;
        this.ppsPlanPartsService = ppsPlanPartsService;
    }

    @Operation(summary = "冻结选中计划")
    @PostMapping("freeze")
    public ResultVO<String> freeze(@RequestBody LongIdsListModel request) {
        if(request.getIds() == null || request.getIds().isEmpty()) {
            return new ResultVO<String>().error(-1, "请选择数据");
        }
        ppsPlanPartsService.freeze(request.getIds());
        ppsPlanPartsService.saveChange();
        return new ResultVO<String>().ok("", "冻结成功");
    }


    @Operation(summary = "取消计划冻结")
    @PostMapping("unfreeze")
    public ResultVO<String> unFreeze(@RequestBody LongIdsListModel request) {
        if(request.getIds() == null || request.getIds().isEmpty()) {
            return new ResultVO<String>().error(-1, "请选择数据");
        }
        ppsPlanPartsService.unFreeze(request.getIds());
        ppsPlanPartsService.saveChange();
        return new ResultVO<String>().ok("", "解冻成功");
    }

    @Operation(summary = "获取计划的bom数据")
    @GetMapping("getplanbom")
    public ResultVO<List<PmProductBomEntity>> getPlanBom(String planNo) {
        List<PmProductBomEntity> data = ppsPlanPartsService.getPlanBom(planNo);
        return new ResultVO<List<PmProductBomEntity>>().ok(data, "操作成功");
    }

    @Operation(summary = "获取预成组生产订单列表")
    @GetMapping("getmoduleorderlist")
    public ResultVO<List<ModuleOrderListInfo>> getModuleOrderList(String workstationCode) {
        List<ModuleOrderListInfo> data = ppsPlanPartsService.getModuleOrderList(workstationCode);
        return new ResultVO<List<ModuleOrderListInfo>>().ok(data, "操作成功");
    }

    @Operation(summary = "自动锁定")
    @GetMapping("autolockplan")
    public ResultVO autoLockPlan(String planno) {
        PpsPlanPartsEntity plan = ppsPlanPartsService.getFirstByPlanNo(planno);
        synchronized (Constant.planLockEntryReckon) {
            ppsPlanPartsService.planPartsAutoLock(plan);
            //保存时间 ppsPlanService.updateBatchById(plans);
            ppsPlanPartsService.saveChange();
        }
        return new ResultVO().ok("", "操作成功");
    }


    @Operation(summary = "计划锁定工单初始化")
    @PostMapping("plansplitentryreckon")
    public ResultVO<List<PlanPartsSplitEntryReckonInfo>> PlanSplitEntryReckon(@RequestBody PlanPartsSplitEntryReckonPara para) {
        List<PlanPartsSplitEntryReckonInfo> data = ppsPlanPartsService.planPartsSplitEntryReckon(para);
        return new ResultVO<List<PlanPartsSplitEntryReckonInfo>>().ok(data, "操作成功");
    }

    @Operation(summary = "获取导入模板")
    @PostMapping("downloadplantemplate")
    public void DownloadPlanTemplate(@RequestBody TemplateModel templateModel, HttpServletResponse response) throws Exception {
        ppsPlanPartsService.getImportTemplate(templateModel.getFileName(), response);
    }


    @Operation(summary = "计划拆分工单")
    @PostMapping("planlockentryreckon")
    public ResultVO<String> planLockEntryReckon(@RequestBody(required = false) String reqJson) {
        if (StringUtils.isBlank(reqJson) || "{}".equals(reqJson.trim())) {
            return new ResultVO<String>().ok("", "请先提交生产数量");
        }
        List<PlanPartsSplitEntryReckonInfo> para = JsonUtils.parseArray(reqJson, PlanPartsSplitEntryReckonInfo.class);
        synchronized (Constant.planLockEntryReckon) {
            ppsPlanPartsService.planPartsLockEntryReckon(para);
            ppsPlanPartsService.saveChange();
            ppsPlanPartsService.sendLmsPartsLockPlan(para);
        }
        return new ResultVO<String>().ok("", "拆分工单成功！");
    }

    @Operation(summary = "锁定模组计划")
    @PostMapping("lockmoduleplan")
    public ResultVO<String> lockModulePlan(@RequestBody IdsModel request) {
        if(request.getIds() == null || request.getIds().length == 0) {
            return new ResultVO<String>().error(-1, "请选择数据");
        }
        ppsPlanPartsService.lockModulePlan(ConvertUtils.stringToLongs(Arrays.asList(request.getIds())));
        ppsPlanPartsService.saveChange();
        ppsPlanPartsService.sendLmsModuleLockPlan(ConvertUtils.stringToLongs(Arrays.asList(request.getIds())));
        return new ResultVO<String>().ok("","锁定成功！");
    }

    @Operation(summary = "获取拆分数据描述")
    @GetMapping("getmodulesplitdata")
    public ResultVO<ModuleSplitDataInfo> GetModuleSplitData(String planNo) {
        ModuleSplitDataInfo data = ppsPlanPartsService.getModuleSplitData(planNo);
        return new ResultVO<ModuleSplitDataInfo>().ok(data);
    }

    /**
     * 获取生产计划-零部件信息
     *
     * @param planNo
     * @return
     */
    @Operation(summary = "获取生产计划-零部件信息")
    @GetMapping("/provider/getplanpastsbyplanno")
    public ResultVO<PpsPlanPartsEntity> getEntryPartsInfoByEntryNo(String planNo) {
        PpsPlanPartsEntity data = ppsPlanPartsService.getFirstByPlanNo(planNo);
        return new ResultVO<PpsPlanPartsEntity>().ok(data, "获取数据成功");
    }
}