package com.ca.mfd.prc.core.message.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.message.entity.MsgPushEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jay.he
 * @Description: 信息推送记录
 */
@Mapper
public interface IMsgPushMapper extends IBaseMapper<MsgPushEntity> {

}