package com.ca.mfd.prc.core.message.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.message.entity.MsgSendEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jay.he
 * @Description: 信息发送记录
 */
@Mapper
public interface IMsgSendMapper extends IBaseMapper<MsgSendEntity> {

}