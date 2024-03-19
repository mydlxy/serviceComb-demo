package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.main.entity.SysSchedulingTaskEntity;
import com.ca.mfd.prc.core.main.service.ISysSchedulingTaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 系统定时任务明细
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/sysschedulingtask")
@Tag(name = "系统定时任务明细")
public class SysSchedulingTaskController extends BaseController<SysSchedulingTaskEntity> {

    private final ISysSchedulingTaskService sysSchedulingTaskService;

    @Autowired
    public SysSchedulingTaskController(ISysSchedulingTaskService sysSchedulingTaskService) {
        this.crudService = sysSchedulingTaskService;
        this.sysSchedulingTaskService = sysSchedulingTaskService;
    }

}