package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.main.entity.SysLogOperEntity;
import com.ca.mfd.prc.core.main.service.ISysLogOperService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 关键操作日志
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/syslogoper")
@Tag(name = "关键操作日志")
public class SysLogOperController extends BaseController<SysLogOperEntity> {

    private final ISysLogOperService sysLogOperService;

    @Autowired
    public SysLogOperController(ISysLogOperService sysLogOperService) {
        this.crudService = sysLogOperService;
        this.sysLogOperService = sysLogOperService;
    }

}