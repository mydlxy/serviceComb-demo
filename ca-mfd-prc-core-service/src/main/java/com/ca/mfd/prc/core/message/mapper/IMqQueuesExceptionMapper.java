package com.ca.mfd.prc.core.message.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.message.entity.MqQueuesExceptionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jay.he
 * @Description: 通道异常日志
 * @date 2023年09月11日
 */
@Mapper
public interface IMqQueuesExceptionMapper extends IBaseMapper<MqQueuesExceptionEntity> {

}