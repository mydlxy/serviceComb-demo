package com.ca.mfd.prc.pmc.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pmc.dto.CreateAndonAlarmDTO;
import com.ca.mfd.prc.pmc.entity.PmcAlarmComponentAlarmEntity;

import java.util.List;

/**
 * 组件报警记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
public interface IPmcAlarmComponentAlarmService extends ICrudService<PmcAlarmComponentAlarmEntity> {

    /**
     * 获取一个工段内并且一段时间内所有的报警信息
     *
     * @param startTime
     * @param endTime
     * @param postion
     * @return
     */
    List<PmcAlarmComponentAlarmEntity> getAlarmComponentAlarmList(String startTime, String endTime, String postion);

    /**
     * 添加安灯报警时通知给报警模块
     *
     * @param para
     */
    void addAndonAlarm(CreateAndonAlarmDTO para);

    /**
     * 第一层沉淀
     */
    void data1();

    /**
     * 第二层沉淀
     */
    void data2();

    /**
     * 生产模板
     */
    void autoDealModel();

    /**
     * 解析设备报警
     *
     * @param jsonString
     */
    void analysisIotData(String jsonString);
}