package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;

import com.ca.mfd.prc.pm.entity.PmBopBomDetailEntity;
import com.ca.mfd.prc.pm.service.IPmBopBomDetailService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 *
 * @Description: MBOM日志Controller
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
@RestController
@RequestMapping("pmbopbomdetail")
@Tag(name = "MBOM日志服务", description = "MBOM日志")
public class PmBopBomDetailController extends PmBaseController<PmBopBomDetailEntity> {

    private IPmBopBomDetailService pmBopBomDetailService;

    @Autowired
    public PmBopBomDetailController(IPmBopBomDetailService pmBopBomDetailService) {
        this.crudService = pmBopBomDetailService;
        this.pmBopBomDetailService = pmBopBomDetailService;
    }

}