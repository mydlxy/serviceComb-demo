package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.avi.entity.AviOperationLogEntity;
import com.ca.mfd.prc.avi.service.IAviOperationLogService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 产品跟踪站点终端操作日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@RestController
@RequestMapping("avioperationlog")
@Tag(name = "产品跟踪站点终端操作日志")
public class AviOperationLogController extends BaseController<AviOperationLogEntity> {

    private final IAviOperationLogService aviOperationLogService;

    @Autowired
    public AviOperationLogController(IAviOperationLogService aviOperationLogService) {
        this.crudService = aviOperationLogService;
        this.aviOperationLogService = aviOperationLogService;
    }

}