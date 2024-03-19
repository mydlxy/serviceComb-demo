package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsQualityRouteRecordEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 车辆去向指定记录服务
 * @date 2023年09月09日
 * @变更说明 BY inkelink At 2023年09月09日
 */
public interface IPqsQualityRouteRecordService extends ICrudService<PqsQualityRouteRecordEntity> {

    /**
     * 获取所有车辆去向指定记录
     *
     * @return
     */
    List<PqsQualityRouteRecordEntity> getAllDatas();
}