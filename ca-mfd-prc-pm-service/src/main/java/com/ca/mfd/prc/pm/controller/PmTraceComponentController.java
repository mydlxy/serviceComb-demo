package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmTraceComponentEntity;
import com.ca.mfd.prc.pm.service.IPmTraceComponentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 追溯组件配置
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmtracecomponent")
@Tag(name = "追溯组件配置")
public class PmTraceComponentController extends BaseController<PmTraceComponentEntity> {

    private final IPmTraceComponentService pmTraceComponentService;

    @Autowired
    public PmTraceComponentController(IPmTraceComponentService pmTraceComponentService) {
        this.crudService = pmTraceComponentService;
        this.pmTraceComponentService = pmTraceComponentService;
    }

    /**
     * 获取所有的组件
     *
     * @return 组件列表
     */
    @GetMapping("/provider/getdatacache")
    @Operation(summary = "获取所有的组件")
    public ResultVO<List<PmTraceComponentEntity>> getDataCache() {
        ResultVO<List<PmTraceComponentEntity>> result = new ResultVO<>();
        List<PmTraceComponentEntity> data = pmTraceComponentService.getAllDatas();
        return result.ok(data);
    }

    /**
     * 获取所有的组件
     *
     * @return 组件列表
     */
    @PostMapping("/provider/savebybom")
    @Operation(summary = "获取所有的组件")
    public ResultVO<String> saveByBom(String materialNo, String materialCn) {
        ResultVO<String> result = new ResultVO<>();
        pmTraceComponentService.saveByBom(materialNo, materialCn);
        pmTraceComponentService.saveChange();
        return result.ok("", "操作成功");
    }

}