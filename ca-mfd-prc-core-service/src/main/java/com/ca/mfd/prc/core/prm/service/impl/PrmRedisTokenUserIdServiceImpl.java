package com.ca.mfd.prc.core.prm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.prm.entity.PrmRedisTokenUserIdEntity;
import com.ca.mfd.prc.core.prm.mapper.IPrmRedisTokenUserIdMapper;
import com.ca.mfd.prc.core.prm.service.IPrmRedisTokenUserIdService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author inkelink ${email}
 * @Description: token(门户集成使用)
 * @date 2023-09-23
 */
@Service
public class PrmRedisTokenUserIdServiceImpl extends AbstractCrudServiceImpl<IPrmRedisTokenUserIdMapper, PrmRedisTokenUserIdEntity> implements IPrmRedisTokenUserIdService {
    @Override
    public PrmRedisTokenUserIdEntity getUserIdByToken(String token) {
        QueryWrapper<PrmRedisTokenUserIdEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PrmRedisTokenUserIdEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PrmRedisTokenUserIdEntity::getToken, token);
        lambdaQueryWrapper.ge(PrmRedisTokenUserIdEntity::getExpireDt, new Date());
        return this.selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public PrmRedisTokenUserIdEntity getLocaluserByToken(String token) {
        QueryWrapper<PrmRedisTokenUserIdEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PrmRedisTokenUserIdEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PrmRedisTokenUserIdEntity::getToken, token);
        return this.selectList(queryWrapper).stream().findFirst().orElse(null);
    }
}
