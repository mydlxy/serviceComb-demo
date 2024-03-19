package com.ca.mfd.prc.core.prm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.prm.entity.PrmSessionEntity;
import com.ca.mfd.prc.core.prm.mapper.IPrmSessionMapper;
import com.ca.mfd.prc.core.prm.service.IPrmSessionService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 登录日志
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class PrmSessionServiceImpl extends AbstractCrudServiceImpl<IPrmSessionMapper, PrmSessionEntity> implements IPrmSessionService {
    @Override
    public PrmSessionEntity getPrmSessionEntityById(String id) {
        QueryWrapper<PrmSessionEntity> qryCode = new QueryWrapper<>();
        LambdaQueryWrapper<PrmSessionEntity> lambdaQueryWrapper = qryCode.lambda();
        lambdaQueryWrapper.eq(PrmSessionEntity::getId, id);

        lambdaQueryWrapper.and(s -> s.eq(PrmSessionEntity::getStatus, 1).or().eq(PrmSessionEntity::getStatus, 0)
                .or().eq(PrmSessionEntity::getStatus, 3)); //TODO  
        return this.getTopDatas(1, qryCode).stream().findFirst().orElse(null);
    }

    @Override
    public void updateStatusById(String id, int status) {
        UpdateWrapper<PrmSessionEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PrmSessionEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.eq(PrmSessionEntity::getId, id);
        lambdaUpdateWrapper.set(PrmSessionEntity::getExpireDt, new Date());
        lambdaUpdateWrapper.set(PrmSessionEntity::getStatus, status);
        update(updateWrapper);
    }

    @Override
    public int updateStatusByExpireDt(String id, int status) {
        UpdateWrapper<PrmSessionEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PrmSessionEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        if (status == 0) {
            lambdaUpdateWrapper.set(PrmSessionEntity::getStatus, 0);
        } else {
            lambdaUpdateWrapper.set(PrmSessionEntity::getStatus, 1);
        }
        lambdaUpdateWrapper.eq(PrmSessionEntity::getId, id);
        lambdaUpdateWrapper.ge(PrmSessionEntity::getExpireDt, new Date());
        if (status == 0) {
            lambdaUpdateWrapper.eq(PrmSessionEntity::getStatus, 1);
        } else {
            lambdaUpdateWrapper.eq(PrmSessionEntity::getStatus, 0);
        }
        return update(updateWrapper) ? 1 : 0;
    }

    /**
     * 获取单条数据
     */
    @Override
    public PrmSessionEntity getPrmSessionEntityByIdStatus(String id, Boolean isExpire) {
        QueryWrapper<PrmSessionEntity> qryCode = new QueryWrapper<>();
        LambdaQueryWrapper<PrmSessionEntity> sessionQuery = qryCode.lambda();
        sessionQuery.eq(PrmSessionEntity::getId, id);
        sessionQuery.and(c -> c.eq(PrmSessionEntity::getStatus, 0)
                .or().eq(PrmSessionEntity::getStatus, 1));
        if (isExpire) {
            sessionQuery.gt(PrmSessionEntity::getExpireDt, new Date());
        }
        return getTopDatas(1, qryCode).stream().findFirst().orElse(null);
    }

    @Override
    public PrmSessionEntity getPrmSessionEntityByExpireDt(String id) {
        QueryWrapper<PrmSessionEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PrmSessionEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PrmSessionEntity::getId, id);
        lambdaQueryWrapper.eq(PrmSessionEntity::getStatus, 0);
        lambdaQueryWrapper.ge(PrmSessionEntity::getExpireDt, new Date());
        return getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }
}