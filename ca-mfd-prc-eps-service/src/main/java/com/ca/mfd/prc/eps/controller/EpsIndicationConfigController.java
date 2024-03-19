package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsIndicationConfigEntity;
import com.ca.mfd.prc.eps.service.IEpsIndicationConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 作业指示配置Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epsindicationconfig")
@Tag(name = "作业指示配置服务", description = "作业指示配置")
public class EpsIndicationConfigController extends BaseController<EpsIndicationConfigEntity> {


    @Autowired
    public EpsIndicationConfigController(IEpsIndicationConfigService epsIndicationConfigService) {
        this.crudService = epsIndicationConfigService;
    }

}