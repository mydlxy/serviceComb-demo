package com.ca.mfd.prc.pps.extend.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.entity.PpsEntryEntity;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.extend.IPpsEntryExtendService;
import com.ca.mfd.prc.pps.mapper.IPpsEntryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工单扩展
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PpsEntryExtendServiceImpl extends AbstractCrudServiceImpl<IPpsEntryMapper, PpsEntryEntity> implements IPpsEntryExtendService {



    /**
     * 根据订单号查询列表
     *
     * @param orderNo 订单编号
     * @return 工单列表
     */
    @Override
    public List<PpsEntryEntity> getEntityListByOrderNo(String orderNo) {
        QueryWrapper<PpsEntryEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsEntryEntity::getOrderNo, orderNo);
        return this.selectList(queryWrapper);
    }

    /**
     * 批量更新工单
     *
     * @param ids     id集合
     * @param sn      唯一码
     * @param orderId 订单ID
     * @param orderNo 订单号
     * @param planNo  计划编码
     */
    @Override
    public void updateEntityByIds(List<Long> ids, String sn, Long orderId, String orderNo, String planNo) {
        UpdateWrapper<PpsEntryEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PpsEntryEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.in(PpsEntryEntity::getId, ids);
        lambdaUpdateWrapper.set(PpsEntryEntity::getSn, sn);
        lambdaUpdateWrapper.set(PpsEntryEntity::getPrcPpsOrderId, orderId);
        lambdaUpdateWrapper.set(PpsEntryEntity::getOrderNo, orderNo);
        lambdaUpdateWrapper.set(PpsEntryEntity::getParentNo, planNo);
        this.update(updateWrapper);
    }

}
