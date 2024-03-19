package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.audit.entity.AuditQualityMatrikEntity;

import java.util.List;

/**
 *
 * @Description: AUDIT百格图服务
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
public interface IAuditQualityMatrikService extends ICrudService<AuditQualityMatrikEntity> {

    /**
     * 获取全部数据
     * @return
     */
    List<AuditQualityMatrikEntity> getAllDatas();
}