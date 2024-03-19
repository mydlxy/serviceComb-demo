package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsAssembleSetEntity;
import com.ca.mfd.prc.eps.service.IEpsAssembleSetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 装配单设置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsassembleset")
@Tag(name = "装配单设置")
public class EpsAssembleSetController extends BaseController<EpsAssembleSetEntity> {


    @Autowired
    public EpsAssembleSetController(IEpsAssembleSetService epsAssembleSetService) {
        this.crudService = epsAssembleSetService;
    }

}