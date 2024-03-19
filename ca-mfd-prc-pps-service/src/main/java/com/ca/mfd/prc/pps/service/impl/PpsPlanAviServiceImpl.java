package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.entity.PpsPlanAviEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanAviEntity;
import com.ca.mfd.prc.pps.mapper.IPpsPlanAviMapper;
import com.ca.mfd.prc.pps.service.IPpsPlanAviService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author eric.zhou
 * @Description: 计划履历;服务实现
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@Service
public class PpsPlanAviServiceImpl extends AbstractCrudServiceImpl<IPpsPlanAviMapper, PpsPlanAviEntity> implements IPpsPlanAviService {

    @Override
    public void afterInsert(PpsPlanAviEntity model) {
        if (model.getEndDt() != null && model.getBeginDt() != null
                && model.getBeginDt().getTime() > model.getEndDt().getTime()) {
            throw new InkelinkException("计划下线时间必须大于计划上线时间");
        }
    }

    @Override
    public void afterUpdate(PpsPlanAviEntity model) {
        if (model.getEndDt() != null && model.getBeginDt() != null
                && model.getBeginDt().getTime() > model.getEndDt().getTime()) {
            throw new InkelinkException("计划下线时间必须大于计划上线时间");
        }
    }

    @Override
    public List<PpsPlanAviEntity> getListByPlanNo(String planNo) {
        QueryWrapper<PpsPlanAviEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsPlanAviEntity::getPlanNo, planNo);
        return selectList(qry);
    }
}