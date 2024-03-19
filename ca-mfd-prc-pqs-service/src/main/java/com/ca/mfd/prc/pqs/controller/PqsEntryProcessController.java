package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.CreateProcessEntryInfo;
import com.ca.mfd.prc.pqs.dto.GetPartsProcessInfo;
import com.ca.mfd.prc.pqs.dto.PqsEntryPageFilter;
import com.ca.mfd.prc.pqs.dto.PqsEntryProcessDto;
import com.ca.mfd.prc.pqs.dto.SaveEntryProcessInfo;
import com.ca.mfd.prc.pqs.entity.PqsEntryProcessEntity;
import com.ca.mfd.prc.pqs.service.IPqsEntryProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 过程检验Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsentryprocess")
@Tag(name = "过程检验服务", description = "过程检验")
public class PqsEntryProcessController extends BaseController<PqsEntryProcessEntity> {

    private final IPqsEntryProcessService pqsEntryProcessService;

    @Autowired
    public PqsEntryProcessController(IPqsEntryProcessService pqsEntryProcessService) {
        this.crudService = pqsEntryProcessService;
        this.pqsEntryProcessService = pqsEntryProcessService;
    }

    /**
     * 获取零部件工序信息
     *
     * @param key 单件条码或工单号
     * @return
     */
    @GetMapping("getpartprocessinfo")
    @Operation(summary = "获取零部件工序信息")
    public ResultVO<GetPartsProcessInfo> getPartProcessInfo(@RequestParam String key) {
        return new ResultVO<GetPartsProcessInfo>().ok(pqsEntryProcessService.getPartProcessInfo(key), "获取数据成功！");
    }

    /**
     * 获取检验工单列表
     *
     * @param filter
     * @return
     */
    @PostMapping("getpqsinspectentry")
    @Operation(summary = "获取检验工单列表")
    public ResultVO<PageData<PqsEntryProcessDto>> getPqsInspectEntry(@RequestBody PqsEntryPageFilter filter) {
        return new ResultVO<PageData<PqsEntryProcessDto>>().ok(pqsEntryProcessService.getEntryList(filter), "获取数据成功！");
    }

    /**
     * 初始化检验工单
     *
     * @param inspectionNo
     * @return
     */
    @GetMapping("initialization")
    @Operation(summary = "初始化检验工单")
    public ResultVO<PqsEntryProcessDto> initialization(@RequestParam String inspectionNo) {
        PqsEntryProcessDto result = pqsEntryProcessService.initialization(inspectionNo);
        pqsEntryProcessService.saveChange();
        return new ResultVO<PqsEntryProcessDto>().ok(result, "获取数据成功！");
    }

    /**
     * 创建过程检验
     *
     * @param info
     * @return
     */
    @PostMapping("createprocessentry")
    @Operation(summary = "创建过程检验")
    public ResultVO<String> createProcessEntry(@RequestBody CreateProcessEntryInfo info) {
        String result = pqsEntryProcessService.createProcessEntry(info);
        pqsEntryProcessService.saveChange();
        return new ResultVO<String>().ok(result, "操作成功！");
    }

    /**
     * 保存检验结果
     *
     * @param info
     * @return
     */
    @PostMapping("saveentryprocessresult")
    @Operation(summary = "保存检验结果")
    public ResultVO saveEntryProcessResult(@RequestBody SaveEntryProcessInfo info) {
        pqsEntryProcessService.saveEntryProcessResult(info);
        pqsEntryProcessService.saveChange();
        return new ResultVO<>().ok("", "操作成功！");
    }

    /**
     * 删除单据
     *
     * @param inspectionNo
     * @return
     */
    @GetMapping("delete")
    @Operation(summary = "删除单据")
    public ResultVO delete(@RequestParam String inspectionNo) {
        pqsEntryProcessService.deleteEntry(inspectionNo);
        pqsEntryProcessService.saveChange();
        return new ResultVO<>().ok("", "操作成功！");
    }
}