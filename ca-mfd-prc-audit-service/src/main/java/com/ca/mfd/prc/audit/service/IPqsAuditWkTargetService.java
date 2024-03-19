package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsAuditWkTargetEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: Audit质量周目标设置服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsAuditWkTargetService extends ICrudService<PqsAuditWkTargetEntity> {

    /**
     * 获取所有的Audit质量周目标设置信息
     *
     * @return
     */
    List<PqsAuditWkTargetEntity> getAllDatas();
}