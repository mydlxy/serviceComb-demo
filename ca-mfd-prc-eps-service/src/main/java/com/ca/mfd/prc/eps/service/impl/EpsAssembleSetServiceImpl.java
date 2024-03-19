package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsAssembleDetailMapper;
import com.ca.mfd.prc.eps.mapper.IEpsAssembleSetMapper;
import com.ca.mfd.prc.eps.service.IEpsAssembleDetailService;
import com.ca.mfd.prc.eps.service.IEpsAssembleSetService;
import com.ca.mfd.prc.eps.entity.EpsAssembleDetailEntity;
import com.ca.mfd.prc.eps.entity.EpsAssembleSetEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 装配单设置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class EpsAssembleSetServiceImpl extends AbstractCrudServiceImpl<IEpsAssembleSetMapper, EpsAssembleSetEntity> implements IEpsAssembleSetService {

    @Autowired
    private IEpsAssembleDetailService epsAssembleDetailService;

    @Override
    public void beforeInsert(EpsAssembleSetEntity model) {
        validData(model);
    }


    @Override
    public void beforeUpdate(EpsAssembleSetEntity model) {
        validData(model);
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        UpdateWrapper<EpsAssembleDetailEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().in(EpsAssembleDetailEntity::getPrcReportAssembleSetId,idList);
        //wrapper.in("PRC_EPS_ASSEMBLE_SET_ID", idList);
        epsAssembleDetailService.delete(wrapper);
    }

    private void validData(EpsAssembleSetEntity model) {
        ConditionDto idNotEqCondition = new ConditionDto("ID", String.valueOf(model.getId())
                , ConditionOper.Unequal);
        String tplCode = model.getTplCode();
        ConditionDto tplCodeEqCondition = new ConditionDto("TPL_CODE", tplCode, ConditionOper.Equal);
        List<ConditionDto> conditionList = new ArrayList<>();
        conditionList.add(idNotEqCondition);
        conditionList.add(tplCodeEqCondition);
        Integer count = this.count(conditionList);
        if (count > 0) {
            throw new InkelinkException(String.format("【%s】已经配置过模板", tplCode));
        }
    }
}