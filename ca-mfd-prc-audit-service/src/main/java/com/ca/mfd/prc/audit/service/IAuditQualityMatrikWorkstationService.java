package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.audit.entity.AuditQualityMatrikWorkstationEntity;

import java.util.List;

/**
 *
 * @Description: AUDIT百格图关联的岗位服务
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
public interface IAuditQualityMatrikWorkstationService extends ICrudService<AuditQualityMatrikWorkstationEntity> {
    /**
     * 获取全部数据
     * @return
     */
    List<AuditQualityMatrikWorkstationEntity> getAllDatas();
}