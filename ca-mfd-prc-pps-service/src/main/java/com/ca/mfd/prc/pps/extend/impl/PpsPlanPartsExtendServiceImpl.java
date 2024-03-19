package com.ca.mfd.prc.pps.extend.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanPartsEntity;
import com.ca.mfd.prc.pps.extend.IPpsPlanPartsExtendService;
import com.ca.mfd.prc.pps.mapper.IPpsPlanPartsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PpsPlanPartsExtendServiceImpl extends AbstractCrudServiceImpl<IPpsPlanPartsMapper, PpsPlanPartsEntity> implements IPpsPlanPartsExtendService {

    @Override
    public PpsPlanPartsEntity getFirstByPlanNo(String planNo) {
        QueryWrapper<PpsPlanPartsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsPlanPartsEntity::getPlanNo, planNo);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }
}
