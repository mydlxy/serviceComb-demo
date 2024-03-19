package com.ca.mfd.prc.core.prm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.prm.entity.PrmRedisUserIdUserInfoEntity;
import com.ca.mfd.prc.core.prm.mapper.IPrmRedisUserIdUserInfoMapper;
import com.ca.mfd.prc.core.prm.service.IPrmRedisUserIdUserInfoService;
import org.springframework.stereotype.Service;

/**
 * @author inkelink ${email}
 * @Description: token关联用户信息(门户集成使用)
 * @date 2023-09-23
 */
@Service
public class PrmRedisUserIdUserInfoServiceImpl extends AbstractCrudServiceImpl<IPrmRedisUserIdUserInfoMapper, PrmRedisUserIdUserInfoEntity> implements IPrmRedisUserIdUserInfoService {

    @Override
    public PrmRedisUserIdUserInfoEntity getUserInfoByUserId(Long caUserId) {
        QueryWrapper<PrmRedisUserIdUserInfoEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PrmRedisUserIdUserInfoEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PrmRedisUserIdUserInfoEntity::getCaUserId, caUserId);
        return this.selectList(queryWrapper).stream().findFirst().orElse(null);
    }
}
