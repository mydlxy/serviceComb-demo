package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsSetBypassWoEntity;
import com.ca.mfd.prc.eps.service.IEpsSetBypassWoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 设置进工位BYPASS工艺Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epssetbypasswo")
@Tag(name = "设置进工位BYPASS工艺服务", description = "设置进工位BYPASS工艺")
public class EpsSetBypassWoController extends BaseController<EpsSetBypassWoEntity> {

    private final IEpsSetBypassWoService epsSetBypassWoService;

    @Autowired
    public EpsSetBypassWoController(IEpsSetBypassWoService epsSetBypassWoService) {
        this.crudService = epsSetBypassWoService;
        this.epsSetBypassWoService = epsSetBypassWoService;
    }

}