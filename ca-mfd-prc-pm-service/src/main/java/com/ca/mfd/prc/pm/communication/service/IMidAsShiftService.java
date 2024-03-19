package com.ca.mfd.prc.pm.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.entity.MidAsShiftEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: AS班次信息中间表服务
 * @date 2023年10月19日
 * @变更说明 BY inkelink At 2023年10月19日
 */
public interface IMidAsShiftService extends ICrudService<MidAsShiftEntity> {

    /**
     * 保存全部班次信息
     *
     */
    MidApiLogEntity saveAsShfShift(String mqMsg);

    /**
     * 执行数据处理逻辑(考虑异步)
     *
     */
    void excute(String logid);

    /**
     * 获取计划
     *
     * @param logid
     * @return
     */
    List<MidAsShiftEntity> getListByLog(Long logid);
}