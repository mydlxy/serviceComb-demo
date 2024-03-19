package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.prm.entity.PrmSessionEntity;
import com.ca.mfd.prc.core.prm.service.IPrmSessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 登录日志
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prmsession")
@Tag(name = "登录日志")
public class PrmSessionController extends BaseController<PrmSessionEntity> {

    private final IPrmSessionService prmSessionService;

    @Autowired
    public PrmSessionController(IPrmSessionService prmSessionService) {
        this.crudService = prmSessionService;
        this.prmSessionService = prmSessionService;
    }

}