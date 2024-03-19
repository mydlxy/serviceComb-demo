package com.ca.mfd.prc.core.message.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.message.entity.MsgTemplaterToEntity;
import com.ca.mfd.prc.core.message.service.IMsgTemplaterToService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 信息模板发送地址
 *
 * @author jay.he
 */
@RestController
@RequestMapping("message/messagetemplaterto")
@Tag(name = "信息模板发送地址")
public class MsgTemplaterToController extends BaseController<MsgTemplaterToEntity> {

    @Autowired
    IMsgTemplaterToService msgTemplaterToService;

    @Autowired
    public MsgTemplaterToController(IMsgTemplaterToService msgTemplaterToService) {
        this.crudService = msgTemplaterToService;
        this.msgTemplaterToService = msgTemplaterToService;
    }


}
