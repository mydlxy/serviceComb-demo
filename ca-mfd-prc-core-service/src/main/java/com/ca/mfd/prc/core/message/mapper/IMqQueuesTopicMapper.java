package com.ca.mfd.prc.core.message.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.message.entity.MqQueuesTopicEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jay.he
 * @Description: 分组（主题）
 * @date 2023年09月11日
 */
@Mapper
public interface IMqQueuesTopicMapper extends IBaseMapper<MqQueuesTopicEntity> {

}