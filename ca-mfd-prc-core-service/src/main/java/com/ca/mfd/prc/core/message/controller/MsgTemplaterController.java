package com.ca.mfd.prc.core.message.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.message.entity.MsgTemplaterEntity;
import com.ca.mfd.prc.core.message.service.IMsgTemplaterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 信息模板
 *
 * @author jay.he
 */
@RestController
@RequestMapping("message/messagetemplater")
@Tag(name = "信息模板")
public class MsgTemplaterController extends BaseController<MsgTemplaterEntity> {

    @Autowired
    IMsgTemplaterService msgTemplaterService;

    @Autowired
    public MsgTemplaterController(IMsgTemplaterService msgTemplaterService) {
        this.crudService = msgTemplaterService;
        this.msgTemplaterService = msgTemplaterService;
    }


}
