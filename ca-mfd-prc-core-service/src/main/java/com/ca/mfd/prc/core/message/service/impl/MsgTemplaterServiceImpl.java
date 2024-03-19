package com.ca.mfd.prc.core.message.service.impl;

import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.message.entity.MsgTemplaterEntity;
import com.ca.mfd.prc.core.message.mapper.IMsgTemplaterMapper;
import com.ca.mfd.prc.core.message.service.IMsgTemplaterService;
import org.springframework.stereotype.Service;

/**
 * @author jay.he
 * @Description: 消息模板默认发送地址
 */
@Service
public class MsgTemplaterServiceImpl extends AbstractCrudServiceImpl<IMsgTemplaterMapper, MsgTemplaterEntity> implements IMsgTemplaterService {


    //插入和更新要验证数据
    /*private void ValidData(MsgTemplaterInfo model)
    {
        if (string.IsNullOrEmpty(model.Code))
            throw new InfoException(T("模板代码不能为空"));

        if (string.IsNullOrEmpty(model.Name))
            throw new InfoException(T("模板名称不能为空"));


        ValidDataUnique(model.Id, "CODE", model.Code, "已经存在代码为{0}的数据");
        ValidDataUnique(model.Id, "NAME", model.Name, "已经存在名称为{0}的数据");
    }*/

}