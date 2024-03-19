package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.audit.entity.AuditQualityMatrikAnomalyEntity;

import java.util.List;

/**
 *
 * @Description: AUDIT百格图-缺陷服务
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
public interface IAuditQualityMatrikAnomalyService extends ICrudService<AuditQualityMatrikAnomalyEntity> {

    /**
     * 获取所有数据
     * @return
     */
    List<AuditQualityMatrikAnomalyEntity> getAllDatas();
}