package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsEntryAuditDetailEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 评审工单明细服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsEntryAuditDetailService extends ICrudService<PqsEntryAuditDetailEntity> {

    /**
     * 获取评审工单明细数据
     *
     * @return
     */
    List<PqsEntryAuditDetailEntity> getAllDatas();
}