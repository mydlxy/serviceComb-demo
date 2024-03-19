package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsWorkplaceMiddleEntity;
import com.ca.mfd.prc.eps.service.IEpsWorkplaceMiddleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 开工检查记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsworkplacemiddle")
@Tag(name = "开工检查记录")
public class EpsWorkplaceMiddleController extends BaseController<EpsWorkplaceMiddleEntity> {

    private final IEpsWorkplaceMiddleService epsWorkplaceMiddleService;

    @Autowired
    public EpsWorkplaceMiddleController(IEpsWorkplaceMiddleService epsWorkplaceMiddleService) {
        this.crudService = epsWorkplaceMiddleService;
        this.epsWorkplaceMiddleService = epsWorkplaceMiddleService;
    }

}