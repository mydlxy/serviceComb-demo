package com.ca.mfd.prc.pps.extend;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsEntryPartsEntity;

/**
 * 工单扩展（零部件）
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPpsEntryPartsExtendService extends ICrudService<PpsEntryPartsEntity> {

    /**
     * 获取
     *
     * @param entryNo
     * @return
     */
    PpsEntryPartsEntity getByEntryNo(String entryNo);
}