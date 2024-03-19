package com.ca.mfd.prc.mq.rabbitmq.remote.app.core.provider;

import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.IRabbitSysQueueNoteService;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.SysQueueNoteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: jay.he
 * @Date: 2023-09-14
 * @Description:
 */
@Service
public class RabbitMqSysQueueNoteProvider {


    @Autowired
    private IRabbitSysQueueNoteService sysQueueNoteService;

    public void addSimpleMessage(SysQueueNoteEntity content) {
        ResultVO result = sysQueueNoteService.addSimpleMessage(content);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-rabbitmq-sysqueuenote调用失败" + result.getMessage());
        }
    }

    public List<SysQueueNoteEntity> getdata(DataDto model) {
        ResultVO<List<SysQueueNoteEntity>> result = sysQueueNoteService.getdata(model);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-rabbitmq-sysqueuenote调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /*public SysQueueNoteEntity getById(String id) {
        ResultVO<SysQueueNoteEntity> result = sysQueueNoteService.getById(id);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-rabbitmq-sysqueuenote调用失败" + result.getMessage());
        }
        return result.getData();
    }*/

    public void delete(IdsModel model) {
        ResultVO result = sysQueueNoteService.delete(model);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-rabbitmq-sysqueuenote调用失败" + result.getMessage());
        }
    }

}