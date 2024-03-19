package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmBopGroupEntity;
import com.ca.mfd.prc.pm.service.IPmBopGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @Description: 分组配置Controller
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
@RestController
@RequestMapping("pmbopgroup")
@Tag(name = "分组配置服务", description = "分组配置")
public class PmBopGroupController extends PmBaseController<PmBopGroupEntity> {

    private IPmBopGroupService pmBopGroupService;

    @Autowired
    public PmBopGroupController(IPmBopGroupService pmBopGroupService) {
        this.crudService = pmBopGroupService;
        this.pmBopGroupService = pmBopGroupService;
    }

    @GetMapping("getcombo")
    @Operation(summary = "根据工位code获取分组配置服务")
    public ResultVO getCombo(String workshopCode) {
        return new ResultVO().ok(pmBopGroupService.getPmCombo(workshopCode));
    }

}