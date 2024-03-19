package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsAuditMmTargetEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: Audit质量月目标设置服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsAuditMmTargetService extends ICrudService<PqsAuditMmTargetEntity> {

    List<PqsAuditMmTargetEntity> getAllDatas();
}