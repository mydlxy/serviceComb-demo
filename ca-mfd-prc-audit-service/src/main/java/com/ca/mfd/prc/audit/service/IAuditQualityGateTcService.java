package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.AuditQualityGateTcEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 *
 * @Description: AUDIT QG检查项-车型服务
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
public interface IAuditQualityGateTcService extends ICrudService<AuditQualityGateTcEntity> {

    /**
     * 获取所有数据
     *
     * @return
     */
    List<AuditQualityGateTcEntity> getAllDatas();
}