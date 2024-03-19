package com.ca.mfd.prc.mq.rabbitmq.remote.app.core.provider;

import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.IRabbitMqQueuesExceptionService;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.MqQueuesExceptionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: jay.he
 * @Date: 2023-09-14
 * @Description:
 */
@Service
public class RabbitMqQueuesExceptionProvider {


    @Autowired
    private IRabbitMqQueuesExceptionService mqQueuesExceptionService;

    public List<MqQueuesExceptionEntity> getdata(DataDto model) {
        ResultVO<List<MqQueuesExceptionEntity>> result = mqQueuesExceptionService.getdata(model);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-rabbitmq-mqqueuesexception调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public MqQueuesExceptionEntity getById(String id) {
        ResultVO<MqQueuesExceptionEntity> result = mqQueuesExceptionService.getById(id);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-rabbitmq-mqqueuesexception调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public void delete(IdsModel model) {
        ResultVO result = mqQueuesExceptionService.delete(model);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-rabbitmq-mqqueuesexception调用失败" + result.getMessage());
        }
    }

    public void edit(MqQueuesExceptionEntity dto) {
        ResultVO result = mqQueuesExceptionService.edit(dto);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-rabbitmq-mqqueuesexception调用失败" + result.getMessage());
        }
    }

}