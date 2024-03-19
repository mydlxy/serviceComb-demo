package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsQualityRoutePointEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 车辆去向点位配置表服务
 * @date 2023年09月09日
 * @变更说明 BY inkelink At 2023年09月09日
 */
public interface IPqsQualityRoutePointService extends ICrudService<PqsQualityRoutePointEntity> {

    /**
     * 获取所有车辆去向点位配置记录
     *
     * @return
     */
    List<PqsQualityRoutePointEntity> getAllDatas();
}