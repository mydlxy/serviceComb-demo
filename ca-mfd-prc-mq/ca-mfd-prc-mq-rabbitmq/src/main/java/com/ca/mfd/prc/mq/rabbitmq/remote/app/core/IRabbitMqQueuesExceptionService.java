package com.ca.mfd.prc.mq.rabbitmq.remote.app.core;


import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.MqQueuesExceptionEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: jay.he
 * @Date: 2023-09-14
 * @Description:
 */
@FeignClient(name = "ca-mfd-prc-core-service", path = "message/mqqueuesexception", contextId = "inkelink-core-rabbitmq-mqqueuesexception")
public interface IRabbitMqQueuesExceptionService {

    /**
     * 添加消息到队列
     *
     * @param model
     * @return
     */
    // @PostMapping("/provider/getdata")
    @PostMapping(value = "/provider/getdata", produces = {MediaType.APPLICATION_JSON_VALUE})
    ResultVO<List<MqQueuesExceptionEntity>> getdata(@RequestBody DataDto model);

    @GetMapping(value = "/provider/getbyid", produces = {MediaType.APPLICATION_JSON_VALUE})
    ResultVO<MqQueuesExceptionEntity> getById(@RequestParam(value = "id") String id);

    @PostMapping(value = "/provider/del", produces = {MediaType.APPLICATION_JSON_VALUE})
    ResultVO delete(@RequestBody IdsModel model);

    @PostMapping(value = "edit", produces = {MediaType.APPLICATION_JSON_VALUE})
    ResultVO edit(@RequestBody MqQueuesExceptionEntity dto) ;



}
