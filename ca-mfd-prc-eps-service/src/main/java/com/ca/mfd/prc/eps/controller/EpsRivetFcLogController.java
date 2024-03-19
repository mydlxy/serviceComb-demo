package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsRivetFcLogEntity;
import com.ca.mfd.prc.eps.service.IEpsRivetFcLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 铆钉防错日志Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epsrivetfclog")
@Tag(name = "铆钉防错日志服务", description = "铆钉防错日志")
public class EpsRivetFcLogController extends BaseController<EpsRivetFcLogEntity> {

    private final IEpsRivetFcLogService epsRivetFcLogService;

    @Autowired
    public EpsRivetFcLogController(IEpsRivetFcLogService epsRivetFcLogService) {
        this.crudService = epsRivetFcLogService;
        this.epsRivetFcLogService = epsRivetFcLogService;
    }

}