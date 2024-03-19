package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmBopOperEntity;
import com.ca.mfd.prc.pm.service.IPmBopOperService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @Description: BOP操作Controller
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
@RestController
@RequestMapping("pmbopoper")
@Tag(name = "BOP操作服务", description = "BOP操作")
public class PmBopOperController extends PmBaseController<PmBopOperEntity> {

    private final IPmBopOperService pmBopOperService;

    @Autowired
    public PmBopOperController(IPmBopOperService pmBopOperService) {
        this.crudService = pmBopOperService;
        this.pmBopOperService = pmBopOperService;
    }

    @GetMapping("getcombo")
    public ResultVO getCombo(String workshopCode,String groupCode) {
        return new ResultVO().ok(pmBopOperService.getPmCombo(workshopCode,groupCode));
    }



}