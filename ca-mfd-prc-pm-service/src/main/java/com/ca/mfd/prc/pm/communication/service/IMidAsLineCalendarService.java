package com.ca.mfd.prc.pm.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.communication.dto.MidAsLineCalendarReq;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.entity.MidAsLineCalendarEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: AS线体日历中间表服务
 * @date 2023年10月19日
 * @变更说明 BY inkelink At 2023年10月19日
 */
public interface IMidAsLineCalendarService extends ICrudService<MidAsLineCalendarEntity> {

    /**
     * 保存线体所有日历数据
     *
     */
     void saveAllAsLineCalendar() ;

    /**
     * 保存线体日历
     *
     */
    MidApiLogEntity saveAsLineCalendar(List<MidAsLineCalendarReq> reqLines) ;

    /**
     * 执行数据处理逻辑(包含线体、车间日历)(考虑异步)
     *
     */
    void excute(String logid);

    /**
     * 获取计划
     *
     * @param logid
     * @return
     */
    List<MidAsLineCalendarEntity> getListByLog(Long logid);
}