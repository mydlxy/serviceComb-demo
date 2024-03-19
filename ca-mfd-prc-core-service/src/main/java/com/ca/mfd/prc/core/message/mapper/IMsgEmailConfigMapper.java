package com.ca.mfd.prc.core.message.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.message.entity.MsgEmailConfigEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jay.he
 * @Description: 邮箱维护账号
 */
@Mapper
public interface IMsgEmailConfigMapper extends IBaseMapper<MsgEmailConfigEntity> {

}