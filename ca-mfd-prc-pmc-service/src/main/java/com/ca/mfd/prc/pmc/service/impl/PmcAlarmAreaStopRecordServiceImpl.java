package com.ca.mfd.prc.pmc.service.impl;

import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pmc.entity.PmcAlarmAreaStopRecordEntity;
import com.ca.mfd.prc.pmc.mapper.IPmcAlarmAreaStopRecordMapper;
import com.ca.mfd.prc.pmc.service.IPmcAlarmAreaStopRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 停线记录(安灯使用)
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class PmcAlarmAreaStopRecordServiceImpl extends AbstractCrudServiceImpl<IPmcAlarmAreaStopRecordMapper, PmcAlarmAreaStopRecordEntity> implements IPmcAlarmAreaStopRecordService {


    @Override
    public void deleteAct(List<String> guids) {
        this.delete(guids.toArray(new String[guids.size()]), false);
    }
}