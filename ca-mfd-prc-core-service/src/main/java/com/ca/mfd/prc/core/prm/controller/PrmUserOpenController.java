package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.prm.entity.PrmUserOpenEntity;
import com.ca.mfd.prc.core.prm.service.IPrmUserOpenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用户第三方登录信息
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prmuseropen")
@Tag(name = "用户第三方登录信息")
public class PrmUserOpenController extends BaseController<PrmUserOpenEntity> {

    private final IPrmUserOpenService prmUserOpenService;

    @Autowired
    public PrmUserOpenController(IPrmUserOpenService prmUserOpenService) {
        this.crudService = prmUserOpenService;
        this.prmUserOpenService = prmUserOpenService;
    }

}