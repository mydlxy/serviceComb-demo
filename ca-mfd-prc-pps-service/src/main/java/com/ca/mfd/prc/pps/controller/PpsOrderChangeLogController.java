package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pps.entity.PpsOrderChangeLogEntity;
import com.ca.mfd.prc.pps.service.IPpsOrderChangeLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 订单替换日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@RestController
@RequestMapping("ppsorderchangelog")
@Tag(name = "订单替换日志")
public class PpsOrderChangeLogController extends BaseController<PpsOrderChangeLogEntity> {

    private final IPpsOrderChangeLogService ppsOrderChangeLogService;

    @Autowired
    public PpsOrderChangeLogController(IPpsOrderChangeLogService ppsOrderChangeLogService) {
        this.crudService = ppsOrderChangeLogService;
        this.ppsOrderChangeLogService = ppsOrderChangeLogService;
    }

}