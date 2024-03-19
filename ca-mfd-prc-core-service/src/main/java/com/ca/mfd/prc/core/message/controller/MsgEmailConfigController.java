package com.ca.mfd.prc.core.message.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.message.entity.MsgEmailConfigEntity;
import com.ca.mfd.prc.core.message.service.IMsgEmailConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 邮箱维护账号
 *
 * @author jay.he
 */
@RestController
@RequestMapping("message/messageemailconfig")
@Tag(name = "邮箱维护账号")
public class MsgEmailConfigController extends BaseController<MsgEmailConfigEntity> {

    @Autowired
    IMsgEmailConfigService msgEmailConfigService;

    @Autowired
    public MsgEmailConfigController(IMsgEmailConfigService msgEmailConfigService) {
        this.crudService = msgEmailConfigService;
        this.msgEmailConfigService = msgEmailConfigService;
    }


}
