package com.ca.mfd.prc.pmc.service;


import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pmc.entity.PmcAlarmAreaStopRecordEntity;

import java.util.List;

/**
 * 停线记录(安灯使用)
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
public interface IPmcAlarmAreaStopRecordService extends ICrudService<PmcAlarmAreaStopRecordEntity> {

    /**
     * 删除
     *
     * @param guids
     */
    void deleteAct(List<String> guids);
}