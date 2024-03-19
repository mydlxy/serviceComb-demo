package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pps.entity.PpsPackStartWorkConfigEntity;
import com.ca.mfd.prc.pps.service.IPpsPackStartWorkConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 电池开工配置Controller
 * @author inkelink
 * @date 2024年01月22日
 * @变更说明 BY inkelink At 2024年01月22日
 */
@RestController
@RequestMapping("ppspackstartworkconfig")
@Tag(name = "电池开工配置服务", description = "电池开工配置")
public class PpsPackStartWorkConfigController extends BaseController<PpsPackStartWorkConfigEntity> {

    private IPpsPackStartWorkConfigService ppsPackStartWorkConfigService;

    @Autowired
    public PpsPackStartWorkConfigController(IPpsPackStartWorkConfigService ppsPackStartWorkConfigService) {
        this.crudService = ppsPackStartWorkConfigService;
        this.ppsPackStartWorkConfigService = ppsPackStartWorkConfigService;
    }

}