package com.ca.mfd.prc.eps.service.impl;

import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsAssemblyPrizeControlMapper;
import com.ca.mfd.prc.eps.service.IEpsAssemblyPrizeControlService;
import com.ca.mfd.prc.eps.entity.EpsAssemblyPrizeControlEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author inkelink
 * @Description: 总装车间放撬配置服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class EpsAssemblyPrizeControlServiceImpl extends AbstractCrudServiceImpl<IEpsAssemblyPrizeControlMapper, EpsAssemblyPrizeControlEntity> implements IEpsAssemblyPrizeControlService {

    @Override
    public void beforeInsert(EpsAssemblyPrizeControlEntity model) {
        checkUniqueness(model, false);
    }


    @Override
    public void beforeUpdate(EpsAssemblyPrizeControlEntity model) {
        checkUniqueness(model, true);
        model.setIsDown(0);
    }


    private void checkUniqueness(EpsAssemblyPrizeControlEntity model, boolean update) {
        List<ConditionDto> conditionInfos = new ArrayList<>();
        ConditionDto cond = new ConditionDto();
        cond.setColumnName("CONTROL_POINT_NAME");
        cond.setValue(model.getControlPointName());
        cond.setOperator(ConditionOper.Equal);
        conditionInfos.add(cond);
        if (update) {
            ConditionDto condIdNotEqual = new ConditionDto();
            condIdNotEqual.setColumnName("ID");
            condIdNotEqual.setValue(String.valueOf(model.getId()));
            condIdNotEqual.setOperator(ConditionOper.Unequal);
            conditionInfos.add(condIdNotEqual);
        }
        EpsAssemblyPrizeControlEntity existModel = getData(conditionInfos).stream().findFirst().orElse(null);
        if (Objects.nonNull(existModel)) {
            throw new InkelinkException(String.format("控制点位%s已存在", model.getControlPointName()));
        }
    }
}