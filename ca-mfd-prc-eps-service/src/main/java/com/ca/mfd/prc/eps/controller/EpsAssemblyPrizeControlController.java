package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsAssemblyPrizeControlEntity;
import com.ca.mfd.prc.eps.service.IEpsAssemblyPrizeControlService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 总装车间放撬配置Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epsassemblyprizecontrol")
@Tag(name = "总装车间放撬配置服务", description = "总装车间放撬配置")
public class EpsAssemblyPrizeControlController extends BaseController<EpsAssemblyPrizeControlEntity> {


    @Autowired
    public EpsAssemblyPrizeControlController(IEpsAssemblyPrizeControlService epsAssemblyPrizeControlService) {
        this.crudService = epsAssemblyPrizeControlService;
    }


}