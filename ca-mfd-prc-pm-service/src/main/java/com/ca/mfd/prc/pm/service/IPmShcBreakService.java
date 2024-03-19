package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.entity.PmShcBreakEntity;

import java.util.List;

/**
 * 休息时间
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPmShcBreakService extends ICrudService<PmShcBreakEntity> {
    /**
     * 获得最近更新的记录
     *
     * @return
     */
    PmShcBreakEntity getLastUpdatePmShcBreak();

    /**
     * 获取班次下休息时间
     *
     * @param pmShcShiftId 班次ID
     * @return 获取一个列表
     */
    List<PmShcBreakEntity> getPmShcBreakInfos(Long pmShcShiftId);
}