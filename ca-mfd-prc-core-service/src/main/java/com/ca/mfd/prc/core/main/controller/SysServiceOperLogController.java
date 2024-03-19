package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.main.entity.SysServiceOperLogEntity;
import com.ca.mfd.prc.core.main.service.ISysServiceOperLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 服务操作日志
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("servicemanager/sysserviceoperlog")
@Tag(name = "服务操作日志")
public class SysServiceOperLogController extends BaseController<SysServiceOperLogEntity> {

    private final ISysServiceOperLogService sysServiceOperLogService;

    @Autowired
    public SysServiceOperLogController(ISysServiceOperLogService sysServiceOperLogService) {
        this.crudService = sysServiceOperLogService;
        this.sysServiceOperLogService = sysServiceOperLogService;
    }

}