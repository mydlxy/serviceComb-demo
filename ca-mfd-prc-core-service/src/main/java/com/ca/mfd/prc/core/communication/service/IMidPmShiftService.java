package com.ca.mfd.prc.core.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.communication.entity.MidPmShiftEntity;

import java.util.List;

/**
 *
 * @Description: 班次中间表服务
 * @author inkelink
 * @date 2023年10月16日
 * @变更说明 BY inkelink At 2023年10月16日
 */
public interface IMidPmShiftService extends ICrudService<MidPmShiftEntity> {

    /**
     * 获取全部数据
     * @return
     */
    List<MidPmShiftEntity> getAllDatas();
}