package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.prm.entity.PrmAuthorizeEntity;
import com.ca.mfd.prc.core.prm.service.IPrmAuthorizeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用户授权记录
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prmauthorize")
@Tag(name = "用户授权记录")
public class PrmAuthorizeController extends BaseController<PrmAuthorizeEntity> {

    private final IPrmAuthorizeService prmAuthorizeService;

    @Autowired
    public PrmAuthorizeController(IPrmAuthorizeService prmAuthorizeService) {
        this.crudService = prmAuthorizeService;
        this.prmAuthorizeService = prmAuthorizeService;
    }

}