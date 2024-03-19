package com.ca.mfd.prc.core.prm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.prm.entity.PrmRedisUserIdUserInfoEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author inkelink ${email}
 * @Description: token关联用户信息(门户集成使用)
 * @date 2023-09-23
 */
@Mapper
public interface IPrmRedisUserIdUserInfoMapper extends IBaseMapper<PrmRedisUserIdUserInfoEntity> {

}