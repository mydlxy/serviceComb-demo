package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.main.entity.SysServiceProcessEntity;
import com.ca.mfd.prc.core.main.service.ISysServiceProcessService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 服务执行命令
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("servicemanager/sysserviceprocess")
@Tag(name = "服务执行命令")
public class SysServiceProcessController extends BaseController<SysServiceProcessEntity> {

    private final ISysServiceProcessService sysServiceProcessService;

    @Autowired
    public SysServiceProcessController(ISysServiceProcessService sysServiceProcessService) {
        this.crudService = sysServiceProcessService;
        this.sysServiceProcessService = sysServiceProcessService;
    }

}