package com.ca.mfd.prc.core.message.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.main.ReportQueue;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.message.dto.MessageContent;
import com.ca.mfd.prc.core.message.entity.MsgSendEntity;
import com.ca.mfd.prc.core.message.service.IMsgPushService;
import com.ca.mfd.prc.core.message.service.IMsgSendService;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.IRabbitSysQueueNoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 信息推送记录
 *
 * @author jay.he
 */
@RestController
@RequestMapping("message/msgsend")
@Tag(name = "信息推送记录")
public class MsgSendController extends BaseController<MsgSendEntity> {

    @Autowired
    IMsgPushService msgPushService;
    @Autowired
    IMsgSendService msgSendService;
    @Lazy
    @Autowired
    IRabbitSysQueueNoteService sysQueueNoteService;

    @Autowired
    public MsgSendController(IMsgPushService msgPushService) {
        this.crudService = msgPushService;
        this.msgPushService = msgPushService;
    }

    /**
     * 测试发送消息
     *
     * @param model
     * @return
     */
    @PostMapping(value = "provider/pushsimplemessage", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "测试发送消息")
    public ResultVO pushSimpleMessage(@RequestBody MessageContent model) {
        msgSendService.pushSimpleMessage(model);
        /*msgSendService.addMessage(model);
        msgSendService.saveChange();*/

       /* _messageSendBll.AddMessage(new MessageContent() {
            Parameters =dic,
            TplCode ="Interface_Upload_JK0020",
            Source =$"QPS系统通知",
            PushDt =DateTime.Now.AddSeconds(10),
            DistinationType =DistinationType.Address,
            Method =this.

            GetMethodString(MethodType.Email, MethodType.DingDing),

            TargetType =MessageTargetType.UnKnown,
        });*/
        return new ResultVO().ok(null, "消息发送成功！");
    }

    /**
     * 发送打印消息
     *
     * @param model
     * @return
     */
    @PostMapping(value = "/provider/sendreportmsg", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "发送打印消息")
    public ResultVO sendReportMsg(@RequestBody ReportQueue model) {
        msgSendService.pushReportMessage(model);

        return new ResultVO().ok(null, "消息发送成功！");
    }

   /* @PostMapping(value = "testSendNoTplCode", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "测试发送无TplCode的消息")
    public ResultVO testSendNoTplCode(MessageContent model) {
        _sysQueueNoteBll.AddMessage(new MessageContent() {
            TargetType =MessageTargetType.Andon,
            Subject =$"{callData.WorkplaceName}[{callData.ButtonName}]-{strState}超时",//标题
            Content =$"{callData.WorkplaceName}岗位，{callData.ParentButtonName}->{callData.ButtonName}，{strState}状态等待超时",//内容
            Source ="MES系统Andon模块",
            DistinationType =DistinationType.UserId,
            Distination =sendInfo.UserId.ToString(),
            DistinationName =sendInfo.UserName,
            Method =this.

            GetMethodString(MethodType.SiteMsg),

            TargetId =sinfo.Id,
        });
        return new ResultVO().ok(null, "消息发送成功！");
    }*/

}
