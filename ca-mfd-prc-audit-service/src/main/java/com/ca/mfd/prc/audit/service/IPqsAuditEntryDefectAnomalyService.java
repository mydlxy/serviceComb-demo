package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsAuditEntryDefectAnomalyEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: AUDIT缺陷记录服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsAuditEntryDefectAnomalyService extends ICrudService<PqsAuditEntryDefectAnomalyEntity> {

    /**
     * 获取所有数据
     *
     * @return
     */
    List<PqsAuditEntryDefectAnomalyEntity> getAllDatas();

    /**
     * OT缺陷列表-修改备注
     * @param req
     */
    void updateRemark(PqsAuditEntryDefectAnomalyEntity req);
}