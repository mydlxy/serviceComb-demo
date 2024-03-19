package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsTechnologyAlarmRecordEntity;

import java.util.List;

/**
 *
 * @Description: 参数预警记录服务
 * @author inkelink
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
public interface IPqsTechnologyAlarmRecordService extends ICrudService<PqsTechnologyAlarmRecordEntity> {

    /**
     * 获取所有参数预警记录
     *
     * @return
     */
    List<PqsTechnologyAlarmRecordEntity> getAllDatas();
}