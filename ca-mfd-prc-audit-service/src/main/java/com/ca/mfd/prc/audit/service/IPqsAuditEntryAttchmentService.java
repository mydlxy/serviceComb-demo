package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsAuditEntryAttchmentEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: AUDIT附件服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsAuditEntryAttchmentService extends ICrudService<PqsAuditEntryAttchmentEntity> {

    /**
     * 获取所有AUDIT附件列表
     *
     * @return
     */
    List<PqsAuditEntryAttchmentEntity> getAllDatas();


}