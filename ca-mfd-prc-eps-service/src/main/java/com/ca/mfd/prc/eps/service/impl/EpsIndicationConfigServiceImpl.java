package com.ca.mfd.prc.eps.service.impl;

import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsIndicationConfigMapper;
import com.ca.mfd.prc.eps.service.IEpsIndicationConfigService;
import com.ca.mfd.prc.eps.entity.EpsIndicationConfigEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author inkelink
 * @Description: 作业指示配置服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class EpsIndicationConfigServiceImpl extends AbstractCrudServiceImpl<IEpsIndicationConfigMapper, EpsIndicationConfigEntity> implements IEpsIndicationConfigService {

    @Override
    public void afterInsert(EpsIndicationConfigEntity model) {
        checkUniqueness(model, false);
    }

    @Override
    public void afterUpdate(EpsIndicationConfigEntity model) {
        checkUniqueness(model, true);
        EpsIndicationConfigEntity oldModel = selectById(model.getId());
        model.setSn(oldModel.getSn());
    }


    private void checkUniqueness(EpsIndicationConfigEntity model, boolean update) {
        List<ConditionDto> conditionInfos = new ArrayList<>();
        ConditionDto cond = new ConditionDto();
        cond.setColumnName("WORKSTATION_CODE");
        cond.setValue(model.getWorkstationCode());
        cond.setOperator(ConditionOper.Equal);
        conditionInfos.add(cond);
        if (update) {
            ConditionDto condIdNotEqual = new ConditionDto();
            condIdNotEqual.setColumnName("ID");
            condIdNotEqual.setValue(String.valueOf(model.getId()));
            condIdNotEqual.setOperator(ConditionOper.Unequal);
            conditionInfos.add(condIdNotEqual);
        }
        EpsIndicationConfigEntity existModel = getData(conditionInfos).stream().findFirst().orElse(null);
        if (Objects.nonNull(existModel)) {
            throw new InkelinkException("该工位已配置过上料指示");
        }

    }

}