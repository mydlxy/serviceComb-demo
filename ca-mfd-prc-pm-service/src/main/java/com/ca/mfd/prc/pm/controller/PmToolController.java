package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmToolEntity;
import com.ca.mfd.prc.pm.service.IPmToolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 工具
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmtool")
@Tag(name = "工具")
public class PmToolController extends PmBaseController<PmToolEntity> {

    private final IPmToolService pmToolService;

    @Autowired
    public PmToolController(IPmToolService pmToolService) {
        this.crudService = pmToolService;
        this.pmToolService = pmToolService;
    }

    @Operation(summary = "判断当前JOB是否需要配置拧紧图片")
    @Parameter(name = "toolId", description = "")
    @GetMapping("isscrewpicture")
    public ResultVO<Boolean> isScrewPicture(String toolId) {
        ResultVO<Boolean> result = new ResultVO();
        boolean screwPicture = pmToolService.isScrewPicture(toolId);
        return result.ok(screwPicture);
    }

    @GetMapping("/getToolComboInfo")
    @Operation(summary = "根据岗位id 查询工位下没有绑定的操作")
    public ResultVO<List<ComboInfoDTO>> getToolComboInfo(Long workplaceId) {
        return pmToolService.getToolComboInfo(workplaceId);
    }

    @GetMapping("/getToolCodeAndName")
    @Operation(summary = "根据工位id查询所有工具")
    public ResultVO<List<PmToolEntity>> getToolCodeAndName(Long workplaceId) {
        return pmToolService.getToolCodeAndName(workplaceId);
    }


}