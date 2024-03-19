package com.ca.mfd.prc.core.message.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.message.entity.MsgTemplaterEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jay.he
 * @Description: 消息模板默认发送地址
 */
@Mapper
public interface IMsgTemplaterMapper extends IBaseMapper<MsgTemplaterEntity> {

}