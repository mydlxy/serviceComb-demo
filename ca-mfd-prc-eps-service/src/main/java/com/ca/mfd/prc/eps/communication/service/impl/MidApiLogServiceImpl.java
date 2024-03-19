package com.ca.mfd.prc.eps.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.eps.communication.mapper.IMidApiLogMapper;
import com.ca.mfd.prc.eps.communication.service.IMidApiLogService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author inkelink
 * @Description: 接口记录表服务实现
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Service
public class MidApiLogServiceImpl extends AbstractCrudServiceImpl<IMidApiLogMapper, MidApiLogEntity> implements IMidApiLogService {


    @Override
    public List<MidApiLogEntity> getDoList(String apitype) {

        QueryWrapper<MidApiLogEntity> qry = new QueryWrapper<>();
        qry.lambda().in(MidApiLogEntity::getStatus, Arrays.asList(1, 6))
                .eq(MidApiLogEntity::getApiType, apitype);
        return selectList(qry);
    }
}