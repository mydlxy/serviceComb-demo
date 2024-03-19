package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsMmDefectAnomalyLogEntity;

import java.util.List;

/**
 *
 * @Description: 零部件缺陷活动日志服务
 * @author inkelink
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
public interface IPqsMmDefectAnomalyLogService extends ICrudService<PqsMmDefectAnomalyLogEntity> {

    /**
     * 获取所有零部件缺陷活动日志记录
     *
     * @return
     */
    List<PqsMmDefectAnomalyLogEntity> getAllDatas();
}