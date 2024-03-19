package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.communication.entity.MidAsShiftEntity;
import com.ca.mfd.prc.pm.dto.ShiftFromASDTO;
import com.ca.mfd.prc.pm.entity.PmShcShiftEntity;

import java.util.List;

/**
 * 工厂排班
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPmShcShiftService extends ICrudService<PmShcShiftEntity> {

    /**
     * 获得最近更新的记录
     *
     * @param codes
     * @return
     */
    List<PmShcShiftEntity> getListByCodes(List<Integer> codes);
    /**
     * 获得最近更新的记录
     *
     * @return
     */
    PmShcShiftEntity getLastUpdatePmShcShift();

    /**
     * 从as中间表导入班次信息
     */
    void syncShiftFromAS(List<MidAsShiftEntity> shiftFromASDTOS);

}