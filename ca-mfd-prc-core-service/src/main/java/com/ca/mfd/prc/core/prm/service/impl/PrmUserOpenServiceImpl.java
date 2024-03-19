package com.ca.mfd.prc.core.prm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.prm.entity.PrmUserOpenEntity;
import com.ca.mfd.prc.core.prm.mapper.IPrmUserOpenMapper;
import com.ca.mfd.prc.core.prm.service.IPrmUserOpenService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 用户第三方登录信息
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class PrmUserOpenServiceImpl extends AbstractCrudServiceImpl<IPrmUserOpenMapper, PrmUserOpenEntity> implements IPrmUserOpenService {

    @Override
    public List<PrmUserOpenEntity> getTokens(Serializable userId) {
        QueryWrapper<PrmUserOpenEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PrmUserOpenEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PrmUserOpenEntity::getPrcPrmUserId, userId);
        return this.selectList(queryWrapper);
    }

}