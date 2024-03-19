package com.ca.mfd.prc.pps.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.mapper.IMidApiLogMapper;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class MidApiLogBaseServiceImpl extends AbstractCrudServiceImpl<IMidApiLogMapper, MidApiLogEntity> implements IMidApiLogBaseService {

    private static final Logger logger = LoggerFactory.getLogger(MidApiLogBaseServiceImpl.class);

    @Override
    public List<MidApiLogEntity> getDoList(String apitype, Long logid) {

        QueryWrapper<MidApiLogEntity> qry = new QueryWrapper<>();
        LambdaQueryWrapper<MidApiLogEntity> qryLmp = qry.lambda();
        qryLmp.eq(MidApiLogEntity::getApiType, apitype);
        if (logid > 0) {
            qryLmp.in(MidApiLogEntity::getStatus, Arrays.asList(1, 6))
                    .eq(MidApiLogEntity::getId, logid);
        } else {
            qryLmp.in(MidApiLogEntity::getStatus, Arrays.asList(1));
        }
        return selectList(qry);
    }

}