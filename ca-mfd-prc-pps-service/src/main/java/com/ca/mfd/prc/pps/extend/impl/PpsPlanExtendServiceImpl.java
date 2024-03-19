package com.ca.mfd.prc.pps.extend.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.extend.IPpsPlanExtendService;
import com.ca.mfd.prc.pps.mapper.IPpsPlanMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PpsPlanExtendServiceImpl extends AbstractCrudServiceImpl<IPpsPlanMapper, PpsPlanEntity> implements IPpsPlanExtendService {

    @Override
    public PpsPlanEntity getFirstByPlanNo(String planNo) {
        QueryWrapper<PpsPlanEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsPlanEntity::getPlanNo, planNo);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 获取所有，还没有拆分的订单
     */
    @Override
    public List<PpsPlanEntity> getByCategoryStatus(String category) {
        //   //获取所有，还没有拆分的订单
        //            var plans = _ppsPlanBll.Table.Where(o => o.OrderCategory == category
        //            && o.PlanStatus == 1 && o.IsFreeze == false)
        //            .OrderBy(o => o.CreateDt).ToList();//获取待拆分的数据
        QueryWrapper<PpsPlanEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsPlanEntity::getOrderCategory, category)
                .eq(PpsPlanEntity::getPlanStatus, 1)
                .eq(PpsPlanEntity::getIsFreeze, false)
                .orderByAsc(PpsPlanEntity::getCreationDate);
        return selectList(qry);
    }


    @Override
    public List<PpsPlanEntity> getGroupByPlanNo(List<String> planNos) {
        QueryWrapper<PpsPlanEntity> qry = new QueryWrapper<>();
        qry.lambda().select(PpsPlanEntity::getProductCode, PpsPlanEntity::getCharacteristicVersion)
                .in(PpsPlanEntity::getPlanNo, planNos)
                .groupBy(PpsPlanEntity::getProductCode, PpsPlanEntity::getCharacteristicVersion);
        return selectList(qry);
    }

    /**
     * 根据计划编码
     *
     * @param planNo 计划编号
     */
    @Override
    public void updateEntityByPlanNo(String planNo) {
        UpdateWrapper<PpsPlanEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PpsPlanEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.eq(PpsPlanEntity::getPlanNo, planNo);
        lambdaUpdateWrapper.set(PpsPlanEntity::getPlanStatus, 30);
        this.update(updateWrapper);
    }
}
