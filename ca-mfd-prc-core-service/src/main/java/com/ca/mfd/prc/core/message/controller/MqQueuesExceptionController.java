package com.ca.mfd.prc.core.message.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.message.entity.MqQueuesExceptionEntity;
import com.ca.mfd.prc.core.message.service.IMqQueuesExceptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * MQ通道异常日志
 *
 * @author jay.he
 * @date 2023-09-05
 */
@RestController
@RequestMapping("message/mqqueuesexception")
@Tag(name = "MQ主题")
public class MqQueuesExceptionController extends BaseController<MqQueuesExceptionEntity> {
    @Lazy
    @Autowired
    IMqQueuesExceptionService mqQueuesExceptionService;

    @Autowired
    public MqQueuesExceptionController(IMqQueuesExceptionService mqQueuesExceptionService) {
        this.crudService = mqQueuesExceptionService;
        this.mqQueuesExceptionService = mqQueuesExceptionService;
    }

    /*@GetMapping(value = "retryQueue/{rabbitmqNotesId}")
    @Operation(summary = "重试队列")
    public ResultVO retryQueue(@PathVariable(value = "rabbitmqNotesId") String rabbitmqNotesId) {



        return new ResultVO().ok(null, "重试成功！");
    }*/

}
