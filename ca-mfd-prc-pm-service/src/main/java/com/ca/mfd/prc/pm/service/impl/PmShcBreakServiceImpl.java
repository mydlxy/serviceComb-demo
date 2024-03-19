package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.entity.PmShcBreakEntity;
import com.ca.mfd.prc.pm.mapper.IPmShcBreakMapper;
import com.ca.mfd.prc.pm.service.IPmShcBreakService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 休息时间
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PmShcBreakServiceImpl extends AbstractCrudServiceImpl<IPmShcBreakMapper, PmShcBreakEntity> implements IPmShcBreakService {

    /**
     * 获得最近更新的一条记录
     *
     * @return
     */
    @Override
    public PmShcBreakEntity getLastUpdatePmShcBreak() {
        QueryWrapper<PmShcBreakEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmShcBreakEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.orderByDesc(PmShcBreakEntity::getLastUpdateDate);
        lambdaQueryWrapper.last("limit 0,1");

        List<PmShcBreakEntity> list = this.selectList(queryWrapper);
        PmShcBreakEntity entity = null;
        if (!CollectionUtils.isEmpty(list)) {
            entity = list.get(0);
        }
        return entity;
    }

    /**
     * 获取班次下休息时间
     *
     * @param pmShcShiftId 班次ID
     * @return 获取一个列表
     */
    @Override
    public List<PmShcBreakEntity> getPmShcBreakInfos(Long pmShcShiftId) {
        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("PRC_PM_SHC_SHIFT_ID", String.valueOf(pmShcShiftId), ConditionOper.Equal));
        return getData(conditionInfos);
    }
}