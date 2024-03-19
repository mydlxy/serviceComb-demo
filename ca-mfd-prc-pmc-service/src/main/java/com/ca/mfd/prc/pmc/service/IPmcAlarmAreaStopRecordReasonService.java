package com.ca.mfd.prc.pmc.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pmc.dto.AuditMeasureInfoDTO;
import com.ca.mfd.prc.pmc.entity.PmcAlarmAreaStopRecordReasonEntity;

/**
 * 停线记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
public interface IPmcAlarmAreaStopRecordReasonService extends ICrudService<PmcAlarmAreaStopRecordReasonEntity> {

    /**
     * 措施审核
     *
     * @param auditMeasureInfo
     */
    void auditMeasure(AuditMeasureInfoDTO auditMeasureInfo);

}