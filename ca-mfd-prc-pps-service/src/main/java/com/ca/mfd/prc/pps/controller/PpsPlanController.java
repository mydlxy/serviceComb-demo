package com.ca.mfd.prc.pps.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.basedto.TemplateModel;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.CreateSparePartsPlanPara;
import com.ca.mfd.prc.pps.dto.PackBranchPlanLockPara;
import com.ca.mfd.prc.pps.dto.PlanLockPara;
import com.ca.mfd.prc.pps.dto.PlanProcessInfo;
import com.ca.mfd.prc.pps.dto.PlanRemarkInfo;
import com.ca.mfd.prc.pps.dto.PpsDataIdsPara;
import com.ca.mfd.prc.pps.dto.UpdatePlanVersionsPara;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pps.service.IPpsLogicService;
import com.ca.mfd.prc.pps.service.IPpsPlanService;
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
 * 生产计划
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@RestController
@RequestMapping("ppsplan")
@Tag(name = "生产计划")
public class PpsPlanController extends BaseController<PpsPlanEntity> {

    private final IPpsPlanService ppsPlanService;
    @Autowired
    private IPpsLogicService ppsLogicService;

    @Autowired
    public PpsPlanController(IPpsPlanService ppsPlanService) {
        this.crudService = ppsPlanService;
        this.ppsPlanService = ppsPlanService;
    }

    @Operation(summary = "完善计划信息")
    @GetMapping("setplaninfo")
    public ResultVO setplaninfo(String planno) {
        ppsPlanService.setplaninfo(planno);
        ppsPlanService.saveChange();
        return new ResultVO<String>().ok("");
    }
    @Operation(summary = "完善计划信息")
    @PostMapping("setplaninfos")
    public ResultVO setplaninfos(@RequestBody PlanLockPara para) {
        ResultVO result = new ResultVO<>();
        if (para.getPlanNos() == null || para.getPlanNos().isEmpty()) {
            return result.error(-1, "请选择数据");
        }
        for (String planno : para.getPlanNos()) {
            ppsPlanService.setplaninfo(planno);
            ppsPlanService.saveChange();
        }
        return new ResultVO<String>().ok("");
    }

    @Operation(summary = "计划锁定")
    @PostMapping("planlock")
    public ResultVO planLock(@RequestBody PlanLockPara para) {
        ResultVO result = new ResultVO<>();
        result.setMessage("计划锁定成功");
        if (para.getPlanNos() == null || para.getPlanNos().isEmpty()) {
            return result.error(-1, "请选择数据");
        }
        Boolean flag = ppsPlanService.planLock(para.getPlanNos(), 2);
        if (!flag) {
            return result.ok("", "计划锁定失败，失败原因请查看错误信息");
        } else {
            return result.ok("", "计划锁定成功");
        }
    }

    @Operation(summary = "创建电池分线计划")
    @PostMapping("createpackbranchplan")
    public ResultVO createPackBranchPlan(@RequestBody PackBranchPlanLockPara para) {
        ResultVO result = new ResultVO<>();
        result.setMessage("计划锁定成功");
        if (StringUtils.isBlank(para.getPlanNo())) {
            return result.error(-1, "请输入计划号");
        }
        if (para.getPlanQty() == null) {
            return result.error(-1, "请输入计划数量");
        }
        ppsPlanService.createPackBranchPlan(para.getPlanNo(), para.getPlanQty());
        return result.ok("", "操作成功");
    }


    @Operation(summary = "删除备件计划（手动创建的备件）")
    @PostMapping("deletestamping")
    public ResultVO deleteStamping(@RequestBody IdsModel model) {
        ResultVO<String> value = new ResultVO<>();
        List<Long> ids = ConvertUtils.stringToLongs(Arrays.asList(model.getIds()));
        if (ids != null && ids.size() <= 0) {
            throw new InkelinkException("请选择要删除的数据");
        }
        ppsPlanService.deleteStamping(ids);
        ppsPlanService.saveChange();
        return value.ok("", "删除成功");
    }

    @Operation(summary = "添加备件")
    @PostMapping("createsparepartsplan")
    public ResultVO createSparePartsPlan(@RequestBody CreateSparePartsPlanPara para) {
        if (para.getPlanQty() <= 0) {
            throw new InkelinkException(para.getPlanQty() + "无效的计划数量");
        }
        ppsPlanService.createSparePartsPlan(para.getMaterialNo(), para.getMaterialName(), para.getLineCode(), para.getEndAvi(), para.getPlanQty());
        ppsPlanService.saveChange();
        return new ResultVO<String>().ok("", "添加备件成功");
    }

    @Operation(summary = "冻结选中计划")
    @PostMapping("freeze")
    public ResultVO freeze(@RequestBody PpsDataIdsPara para) {
        ResultVO result = new ResultVO<>();
        result.setMessage("冻结成功");
        if (para.getPpsDataIds() == null || para.getPpsDataIds().isEmpty()) {
            return result.error(-1, "请选择数据");
        }
        ppsPlanService.freeze(ConvertUtils.stringToLongs(para.getPpsDataIds()));
        ppsPlanService.saveChange();
        return result.ok("", "冻结成功");
    }

    @Operation(summary = "取消计划冻结")
    @PostMapping("unfreeze")
    public ResultVO unFreeze(@RequestBody PpsDataIdsPara para) {
        ResultVO result = new ResultVO<>();
        result.setMessage("解冻成功");
        if (para.getPpsDataIds() == null || para.getPpsDataIds().isEmpty()) {
            return result.error(-1, "请选择数据");
        }
        ppsPlanService.unFreeze(ConvertUtils.stringToLongs(para.getPpsDataIds()));
        ppsPlanService.saveChange();
        return result.ok("", "解冻成功");
    }

    @Operation(summary = "修改计划BOM")
    @PostMapping("updateplanbom")
    public ResultVO updatePlanBom(@RequestBody UpdatePlanVersionsPara para) {
        ResultVO result = new ResultVO<>();
        ppsPlanService.updatePlanBom(para);
        ppsPlanService.saveChange();
        return result.ok("", "操作成功");
    }

    @Operation(summary = "修改计划特征")
    @PostMapping("updateplancharacteristics")
    public ResultVO updatePlanCharacteristics(@RequestBody UpdatePlanVersionsPara para) {
        ResultVO result = new ResultVO<>();
        ppsPlanService.updatePlanCharacteristics(para);
        ppsPlanService.saveChange();
        return result.ok("", "操作成功");
    }

    @Operation(summary = "设置工艺路径")
    @PostMapping("setprocess")
    public ResultVO setProcess(@RequestBody PlanProcessInfo planProcessInfo) {
        ResultVO result = new ResultVO<>();
        result.setMessage("设置工艺路径成功");
        if (planProcessInfo.getPlanIds() == null || planProcessInfo.getPlanIds().isEmpty()) {
            throw new InkelinkException("请选择需要设置的数据");
        }
        Long processId = ConvertUtils.stringToLong(planProcessInfo.getProcessId());
        if (processId <= 0) {
            throw new InkelinkException("请选择需生产区域");
        }
        ppsPlanService.setProcess(ConvertUtils.stringToLongs(planProcessInfo.getPlanIds()), processId);
        ppsPlanService.saveChange();
        return result.ok("", "设置工艺路径成功");
    }

    @Operation(summary = "更新备注信息")
    @PostMapping("setremark")
    public ResultVO setRemark(@RequestBody PlanRemarkInfo planRemarkInfo) {
        ResultVO result = new ResultVO<>();
        result.setMessage("备注更新成功");
        UpdateWrapper<PpsPlanEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(PpsPlanEntity::getRemark, planRemarkInfo.getRemark())
                .in(PpsPlanEntity::getId, planRemarkInfo.getIds());
        ppsPlanService.update(upset);
        ppsPlanService.saveChange();
        return result.ok("", "备注更新成功");
    }

    @Operation(summary = "获取计划的bom数据")
    @GetMapping("getplanbom")
    public ResultVO getPlanBom(String planNo) {
        ResultVO result = new ResultVO<>();
        List<PmProductBomEntity> data = ppsPlanService.getPlanBom(planNo);
        return result.ok(data);
    }

    @Operation(summary = "获取计划的特征数据")
    @GetMapping("getplancharacteristic")
    public ResultVO getPlanCharacteristic(String planNo) {
        ResultVO result = new ResultVO<>();
        List<PmProductCharacteristicsEntity> data = ppsPlanService.getPlanCharacteristic(planNo);
        return result.ok(data);
    }

    @Operation(summary = "获取导入模板")
    @PostMapping("downloadplantemplate")
    public void downloadTemplate(@RequestBody TemplateModel template, HttpServletResponse response) throws Exception {
        ppsPlanService.getImportTemplate(template.getFileName(), response);
    }

    @Operation(summary = "根据计划号获取计划")
    @GetMapping("/provider/getfirstbyplanno")
    public PpsPlanEntity getFirstByPlanNo(String planNo) {
        return ppsPlanService.getFirstByPlanNo(planNo);
    }


    @Operation(summary = "焊装批量开工")
    @PostMapping("tagbatch")
    public ResultVO tagbatch(@RequestBody PlanLockPara para) {
        ResultVO result = new ResultVO<>();
        result.setMessage("计划锁定成功");
        if (para.getPlanNos() == null || para.getPlanNos().isEmpty()) {
            return result.error(-1, "请选择数据");
        }
        ppsLogicService.batchBodyShopStartWork(para.getPlanNos(), para.getWorkstationCode());
        return result.ok("", "批量开工完成");
    }

    @Operation(summary = "生成分线信息")
    @PostMapping("createbln")
    public ResultVO createBln(@RequestBody PlanLockPara para) {
        ResultVO result = new ResultVO<>();
        result.setMessage("计划锁定成功");
        if (para.getPlanNos() == null || para.getPlanNos().isEmpty()) {
            return result.error(-1, "请选择数据");
        }
        ppsLogicService.createBln(para.getPlanNos());
        return result.ok("", "批量开工完成");
    }

    @Operation(summary = "批量计划履历")
    @PostMapping("planavibatch")
    public ResultVO planavibatch(@RequestBody PlanLockPara para) {
        ResultVO result = new ResultVO<>();
        result.setMessage("批量计划履历完成");
        if (para.getPlanNos() == null || para.getPlanNos().isEmpty()) {
            return result.error(-1, "请选择数据");
        }
        ppsLogicService.batchCopyPlanAvi(para.getPlanNos(), para.getWorkstationCode());
        ppsPlanService.saveChange();
        return result.ok("", "批量计划履历完成");
    }

    @Operation(summary = "线体是否在计划履历中")
    @GetMapping("/provider/hasplanline")
    public ResultVO<Integer> hasPlanLine(String lineCode) {
        return new ResultVO<Integer>().ok(ppsLogicService.hasPlanLine(lineCode));
    }

    @Operation(summary = "其他服务调用-根据计划号获取计划")
    @GetMapping("/provider/getPlanByPlanNo")
    public  ResultVO<PpsPlanEntity> getPlanByPlanNo(String planNo) {
        return  new ResultVO<PpsPlanEntity>().ok(ppsPlanService.getFirstByPlanNo(planNo));
    }
}