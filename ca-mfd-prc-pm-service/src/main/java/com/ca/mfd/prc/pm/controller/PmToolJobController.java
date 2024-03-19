package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmToolEntity;
import com.ca.mfd.prc.pm.entity.PmToolJobEntity;
import com.ca.mfd.prc.pm.entity.PmWoEntity;
import com.ca.mfd.prc.pm.service.IPmToolJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: 作业
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmtooljob")
@Tag(name = "作业")
public class PmToolJobController extends PmBaseController<PmToolJobEntity> {

    private final IPmToolJobService pmToolJobService;

    @Autowired
    public PmToolJobController(IPmToolJobService pmToolJobService) {
        this.crudService = pmToolJobService;
        this.pmToolJobService = pmToolJobService;
    }

    @Operation(summary = "获取操作数据")
    @Parameter(name = "toolId", description = "作业ID")
    @GetMapping("getwoinfo")
    public ResultVO getWoInfo(String toolId) {
        ResultVO result = new ResultVO();
        List<PmWoEntity> woInfo = pmToolJobService.getWoInfo(toolId);
        return result.ok(woInfo, "获取数据成功");
    }

    @Operation(summary = "获取工艺信息")
    @Parameter(name = "woId", description = "工艺ID")
    @GetMapping("gettoolsbywoid")
    public ResultVO getToolsBywoId(Long woId) {
        ResultVO result = new ResultVO();
        List<PmToolEntity> tools = pmToolJobService.getToolByWoId(woId);
        return result.ok(tools, "获取数据成功");
    }

    @Operation(summary = "根据工艺编码获取job和工具")
    @Parameter(name = "woCode", description = "工艺编码")
    @Parameter(name = "workstationId", description = "工位id")
    @GetMapping("gettoolsandtooljobsbywoid")
    public ResultVO getToolsAndToolJobsByWoCode(String woCode,Long workstationId) {
        ResultVO result = new ResultVO();
        Map<String,Object> target = pmToolJobService.getToolAndToolJobByWoCode(woCode,workstationId);
        return result.ok(target, "获取数据成功");
    }
}
