package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsReceiveManagementEntity;
import com.ca.mfd.prc.eps.service.IEpsReceiveManagementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 领用车管理Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epsreceivemanagement")
@Tag(name = "领用车管理服务", description = "领用车管理")
public class EpsReceiveManagementController extends BaseController<EpsReceiveManagementEntity> {

    private final IEpsReceiveManagementService epsReceiveManagementService;

    @Autowired
    public EpsReceiveManagementController(IEpsReceiveManagementService epsReceiveManagementService) {
        this.crudService = epsReceiveManagementService;
        this.epsReceiveManagementService = epsReceiveManagementService;
    }

}