package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdModel;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.AffirmModuleEntryPara;
import com.ca.mfd.prc.pps.dto.ChangeWorkOrderCountVM;
import com.ca.mfd.prc.pps.dto.DeleteModuleEntryPara;
import com.ca.mfd.prc.pps.dto.DownModuleEntryInfo;
import com.ca.mfd.prc.pps.dto.ModuleReportPara;
import com.ca.mfd.prc.pps.dto.OutsourceEntryAreaDTO;
import com.ca.mfd.prc.pps.dto.SplitEntryPara;
import com.ca.mfd.prc.pps.dto.SplitModuleEntryPara;
import com.ca.mfd.prc.pps.entity.PpsEntryPartsEntity;
import com.ca.mfd.prc.pps.service.IPpsEntryPartsService;
import com.ca.mfd.prc.pps.service.IPpsLogicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @Description: 工单-零部件Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("ppsentryparts")
@Tag(name = "工单-零部件服务", description = "工单-零部件")
public class PpsEntryPartsController extends BaseController<PpsEntryPartsEntity> {

    @Autowired
    private IPpsLogicService ppsLogicService;

    private IPpsEntryPartsService ppsEntryPartsService;

    @Autowired
    public PpsEntryPartsController(IPpsEntryPartsService ppsEntryPartsService) {
        this.crudService = ppsEntryPartsService;
        this.ppsEntryPartsService = ppsEntryPartsService;
    }

    @Operation(summary = "冻结选中工单")
    @PostMapping("freeze")
    public ResultVO freeze(@RequestBody IdsModel request) {
        if(request.getIds() == null || request.getIds().length == 0) {
            return new ResultVO<String>().error(-1, "请选择数据");
        }
        ppsEntryPartsService.freeze(ConvertUtils.stringToLongs(Arrays.asList(request.getIds())));
        ppsEntryPartsService.saveChange();
        return new ResultVO<String>().ok("", "冻结成功");
    }

    @Operation(summary = "取消工单冻结")
    @PostMapping("unfreeze")
    public ResultVO unFreeze(@RequestBody IdsModel request) {
        if(request.getIds() == null || request.getIds().length == 0) {
            return new ResultVO<String>().error(-1, "请选择数据");
        }
        ppsEntryPartsService.unFreeze(ConvertUtils.stringToLongs(Arrays.asList(request.getIds())));
        ppsEntryPartsService.saveChange();
        return new ResultVO<String>().ok("", "解冻成功");
    }

    @Operation(summary = "修改工单数量")
    @PostMapping("changeworkordercount")
    public ResultVO changeWorkOrderCount(@RequestBody ChangeWorkOrderCountVM vm) {
        ppsEntryPartsService.changeWorkOrderCount(vm.getEntryNo(), vm.getCount());
        ppsEntryPartsService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "手动关闭工单")
    @PostMapping("handcloseentry")
    public ResultVO handCloseEntry(@RequestBody IdsModel request) {
        ppsEntryPartsService.closeEntry(ConvertUtils.stringToLongs(Arrays.asList(request.getIds())));
        ppsEntryPartsService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "获取可以转单工序线体列表")
    @GetMapping("getoutsourceentryarea")
    public ResultVO getOutsourceEntryArea(String entryNo) {
        List<OutsourceEntryAreaDTO> data = ppsEntryPartsService.getOutsourceEntryArea(entryNo);
        return new ResultVO<List<OutsourceEntryAreaDTO>>().ok(data, "操作成功");
    }

    @Operation(summary = "拆分生产工单")
    @PostMapping("splitentry")
    public ResultVO splitEntry(@RequestBody SplitEntryPara request) {
        ppsEntryPartsService.splitEntry(request);
        ppsEntryPartsService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "预备生产 冲压")
    @PostMapping("prelock")
    public ResultVO preLock(@RequestBody IdModel request) {
        ppsEntryPartsService.preLock(ConvertUtils.stringToLong(request.getId()));
        ppsEntryPartsService.saveChange();
        return new ResultVO<String>().ok("", "预备生产操作成功");
    }

    @Operation(summary = "冲压立即生产")
    @PostMapping("beginproduct")
    public ResultVO beginProduct(@RequestBody IdModel request) {
        //锁定后，修改状态为开始生产
        Long entryId = ConvertUtils.stringToLong(request.getId());
        ppsEntryPartsService.beginProduct(entryId);
        ppsEntryPartsService.saveChange();
        ppsLogicService.setPlanStart(entryId);
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "获取模组工单")
    @GetMapping("getmoduleentrybyline")
    public ResultVO getModuleEntryByLine(String line) {
        //拆分工单
        ppsLogicService.splitModuleEntry("");
        DownModuleEntryInfo data = ppsLogicService.getModuleEntry(line);
        return new ResultVO<DownModuleEntryInfo>().ok(data, "操作成功");
    }

    @Operation(summary = "回执下发的模组工单")
    @PostMapping("affirmmoduleentry")
    public ResultVO affirmModuleEntry(@RequestBody AffirmModuleEntryPara para) {
        ppsLogicService.affirmModuleEntry(para);
        ppsEntryPartsService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "强制拆分订单对应模组工单")
    @PostMapping("splitmoduleentry")
    public ResultVO splitModuleEntry(@RequestBody SplitModuleEntryPara para) {
        ppsLogicService.splitModuleEntry(para.getOrderNo());
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "删除模组工单")
    @PostMapping("deletemoduleentry")
    public ResultVO deleteModuleEntry(@RequestBody DeleteModuleEntryPara para) {
        ppsLogicService.deleteModuleEntry(para.getEntryNo());
        ppsEntryPartsService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "模组报工")
    @PostMapping("modulereport")
    public ResultVO moduleReport(@RequestBody ModuleReportPara para) {
        ppsLogicService.moduleReport(para);
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 获取工单-零部件信息
     *
     * @param entryNo
     * @return
     */
    @Operation(summary = "获取工单-零部件信息")
    @GetMapping("/provider/getentrypartsinfobyentryno")
    public ResultVO<PpsEntryPartsEntity> getEntryPartsInfoByEntryNo(String entryNo) {
        PpsEntryPartsEntity data = ppsEntryPartsService.getFirstByEntryNo(entryNo);
        return new ResultVO<PpsEntryPartsEntity>().ok(data, "获取数据成功");
    }

    /**
     * 获取工单-零部件信息
     *
     * @param planNoOrEntryNo
     * @return
     */
    @Operation(summary = "获取工单-零部件信息")
    @GetMapping("/provider/getfirstbyplannoorentryno")
    public ResultVO<PpsEntryPartsEntity> getFirstByPlanNoOrEntryNo(String planNoOrEntryNo) {
        PpsEntryPartsEntity data = ppsEntryPartsService.getFirstByPlanNoOrEntryNo(planNoOrEntryNo);
        return new ResultVO<PpsEntryPartsEntity>().ok(data, "获取数据成功");
    }
}