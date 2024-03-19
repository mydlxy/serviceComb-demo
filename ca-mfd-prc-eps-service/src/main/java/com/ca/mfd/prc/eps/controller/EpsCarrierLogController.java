package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsCarrierLogEntity;
import com.ca.mfd.prc.eps.service.IEpsCarrierLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 载具日志Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("epscarrierlog")
@Tag(name = "载具日志服务", description = "载具日志")
public class EpsCarrierLogController extends BaseController<EpsCarrierLogEntity> {

    private IEpsCarrierLogService epsCarrierLogService;

    @Autowired
    public EpsCarrierLogController(IEpsCarrierLogService epsCarrierLogService) {
        this.crudService = epsCarrierLogService;
        this.epsCarrierLogService = epsCarrierLogService;
    }

}