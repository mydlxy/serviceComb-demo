package com.ca.mfd.prc.core.prm.service;


import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.entity.PrmRedisUserIdUserInfoEntity;

/**
 * token(门户集成使用)
 *
 * @author inkelink ${email}
 * @date 2023-09-23
 */
public interface IPrmRedisUserIdUserInfoService extends ICrudService<PrmRedisUserIdUserInfoEntity> {
    PrmRedisUserIdUserInfoEntity getUserInfoByUserId(Long caUserId);
}