package com.ca.mfd.prc.core.prm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.prm.entity.PrmRedisTokenUserIdEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * token(门户集成使用)
 *
 * @author inkelink ${email}
 * @date 2023-09-23
 */
@Mapper
public interface IPrmRedisTokenUserIdMapper extends IBaseMapper<PrmRedisTokenUserIdEntity> {

}