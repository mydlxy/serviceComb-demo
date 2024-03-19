package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsWorkplaceConfigLogEntity;
import com.ca.mfd.prc.eps.service.IEpsWorkplaceConfigLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 开工检查项记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsworkplaceconfiglog")
@Tag(name = "开工检查项记录")
public class EpsWorkplaceConfigLogController extends BaseController<EpsWorkplaceConfigLogEntity> {

    private final IEpsWorkplaceConfigLogService epsWorkplaceConfigLogService;

    @Autowired
    public EpsWorkplaceConfigLogController(IEpsWorkplaceConfigLogService epsWorkplaceConfigLogService) {
        this.crudService = epsWorkplaceConfigLogService;
        this.epsWorkplaceConfigLogService = epsWorkplaceConfigLogService;
    }

}