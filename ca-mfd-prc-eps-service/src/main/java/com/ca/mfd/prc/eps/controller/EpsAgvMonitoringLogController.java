package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsAgvMonitoringLogEntity;
import com.ca.mfd.prc.eps.service.IEpsAgvMonitoringLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * AGV监听日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsagvmonitoringlog")
@Tag(name = "AGV监听日志")
public class EpsAgvMonitoringLogController extends BaseController<EpsAgvMonitoringLogEntity> {


    @Autowired
    public EpsAgvMonitoringLogController(IEpsAgvMonitoringLogService epsAgvMonitoringLogService) {
        this.crudService = epsAgvMonitoringLogService;
    }

}