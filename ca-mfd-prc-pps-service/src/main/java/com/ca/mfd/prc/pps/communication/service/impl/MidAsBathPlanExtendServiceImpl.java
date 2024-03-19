package com.ca.mfd.prc.pps.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.communication.entity.MidAsBathPlanEntity;
import com.ca.mfd.prc.pps.communication.mapper.IMidAsBathPlanMapper;
import com.ca.mfd.prc.pps.communication.service.IMidAsBathPlanExtendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author inkelink
 * @Description: 接口记录表服务实现
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Service
public class MidAsBathPlanExtendServiceImpl extends AbstractCrudServiceImpl<IMidAsBathPlanMapper, MidAsBathPlanEntity> implements IMidAsBathPlanExtendService {

    private static final Logger logger = LoggerFactory.getLogger(MidAsBathPlanExtendServiceImpl.class);

    @Override
    public List<MidAsBathPlanEntity> getByPlanNos(List<String> plannos) {
        if (plannos == null || plannos.isEmpty()) {
            return new ArrayList<>();
        }
        QueryWrapper<MidAsBathPlanEntity> qry = new QueryWrapper<>();
        qry.lambda().in(MidAsBathPlanEntity::getAttribute3, plannos)
                .orderByDesc(MidAsBathPlanEntity::getCreationDate);
        return selectList(qry);
    }

}