package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsAssemblyPrizeControlDownLogEntity;
import com.ca.mfd.prc.eps.service.IEpsAssemblyPrizeControlDownLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 总装车间放撬下发日志Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epsassemblyprizecontroldownlog")
@Tag(name = "总装车间放撬下发日志服务", description = "总装车间放撬下发日志")
public class EpsAssemblyPrizeControlDownLogController extends BaseController<EpsAssemblyPrizeControlDownLogEntity> {


    @Autowired
    public EpsAssemblyPrizeControlDownLogController(IEpsAssemblyPrizeControlDownLogService epsAssemblyPrizeControlDownLogService) {
        this.crudService = epsAssemblyPrizeControlDownLogService;
    }

}