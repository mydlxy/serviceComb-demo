package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsAuditDefectAnomalyEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: 组合缺陷库服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsAuditDefectAnomalyService extends ICrudService<PqsAuditDefectAnomalyEntity> {

    /**
     * 获取所有缺陷库
     *
     * @return
     */
    List<PqsAuditDefectAnomalyEntity> getAllDatas();
}