package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pps.entity.PpsPartsLogEntity;
import com.ca.mfd.prc.pps.service.IPpsPartsLogService;
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
 * @Description: 零部件-变更履历Controller
 * @author inkelink
 * @date 2024年02月21日
 * @变更说明 BY inkelink At 2024年02月21日
 */
@RestController
@RequestMapping("ppspartslog")
@Tag(name = "零部件-变更履历服务", description = "零部件-变更履历")
public class PpsPartsLogController extends BaseController<PpsPartsLogEntity> {

    private IPpsPartsLogService ppsPartsLogService;

    @Autowired
    public PpsPartsLogController(IPpsPartsLogService ppsPartsLogService) {
        this.crudService = ppsPartsLogService;
        this.ppsPartsLogService = ppsPartsLogService;
    }

}