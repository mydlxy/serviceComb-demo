package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsAuditEntryEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: AUDIT评审单服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsAuditEntryService extends ICrudService<PqsAuditEntryEntity> {


    /**
     * 获取所有数据
     *
     * @return
     */
    List<PqsAuditEntryEntity> getAllDatas();

}