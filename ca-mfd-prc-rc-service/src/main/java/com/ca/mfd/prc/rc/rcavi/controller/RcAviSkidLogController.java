package com.ca.mfd.prc.rc.rcavi.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviSkidLogEntity;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviSkidLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 滑橇使用日志Controller
 * @date 2023年09月01日
 * @变更说明 BY inkelink At 2023年09月01日
 */
@RestController
@RequestMapping("rcaviskidlog")
@Tag(name = "滑橇使用日志服务", description = "滑橇使用日志")
public class RcAviSkidLogController extends BaseController<RcAviSkidLogEntity> {

    private final IRcAviSkidLogService rcAviSkidLogService;

    @Autowired
    public RcAviSkidLogController(IRcAviSkidLogService rcAviSkidLogService) {
        this.crudService = rcAviSkidLogService;
        this.rcAviSkidLogService = rcAviSkidLogService;
    }

}