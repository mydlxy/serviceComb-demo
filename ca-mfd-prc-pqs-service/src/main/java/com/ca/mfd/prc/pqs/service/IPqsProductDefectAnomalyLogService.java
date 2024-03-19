package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.ProductDefectAnomalyLogInfo;
import com.ca.mfd.prc.pqs.entity.PqsProductDefectAnomalyLogEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 产品缺陷日志服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsProductDefectAnomalyLogService extends ICrudService<PqsProductDefectAnomalyLogEntity> {

    /**
     * 获取所有产品缺陷日志
     *
     * @return
     */
    List<PqsProductDefectAnomalyLogEntity> getAllDatas();

    /**
     * 获取激活缺陷日志
     *
     * @param dataId 参数ID
     * @return 日志列表
     */

    List<ProductDefectAnomalyLogInfo> getVehicleDefectAnomalyLog(String dataId);
}