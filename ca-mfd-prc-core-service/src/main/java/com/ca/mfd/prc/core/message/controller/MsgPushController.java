package com.ca.mfd.prc.core.message.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.message.entity.MsgPushEntity;
import com.ca.mfd.prc.core.message.service.IMsgPushService;
import com.ca.mfd.prc.core.message.service.IMsgSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


/**
 * 信息推送记录
 *
 * @author jay.he
 */
@RestController
@RequestMapping("message/messagepush")
@Tag(name = "信息推送记录")
public class MsgPushController extends BaseController<MsgPushEntity> {

    @Autowired
    IMsgPushService msgPushService;
    @Autowired
    IMsgSendService msgSendService;

    @Autowired
    public MsgPushController(IMsgPushService msgPushService) {
        this.crudService = msgPushService;
        this.msgPushService = msgPushService;
    }

    /**
     * 消息重发
     *
     * @param idsModel
     * @return
     */
    @PostMapping(value = "repush", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "消息重发")
    public ResultVO repush(@RequestBody IdsModel idsModel) {
        msgSendService.restMessagePush(Arrays.asList(idsModel.getIds()));
        return new ResultVO().ok(null, "消息重发成功！");
    }


}
