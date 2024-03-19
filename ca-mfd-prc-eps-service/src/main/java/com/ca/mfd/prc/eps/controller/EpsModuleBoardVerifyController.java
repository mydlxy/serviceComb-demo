package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsModuleBoardVerifyEntity;
import com.ca.mfd.prc.eps.service.IEpsModuleBoardVerifyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 预成组端板校验Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("epsmoduleboardverify")
@Tag(name = "预成组端板校验服务", description = "预成组端板校验")
public class EpsModuleBoardVerifyController extends BaseController<EpsModuleBoardVerifyEntity> {

    private IEpsModuleBoardVerifyService epsModuleBoardVerifyService;

    @Autowired
    public EpsModuleBoardVerifyController(IEpsModuleBoardVerifyService epsModuleBoardVerifyService) {
        this.crudService = epsModuleBoardVerifyService;
        this.epsModuleBoardVerifyService = epsModuleBoardVerifyService;
    }

}