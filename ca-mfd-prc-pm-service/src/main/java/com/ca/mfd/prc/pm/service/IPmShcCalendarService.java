package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.dto.CalendarCopyDTO;
import com.ca.mfd.prc.pm.dto.CalendarFromASDTO;
import com.ca.mfd.prc.pm.dto.CalendarParamDTO;
import com.ca.mfd.prc.pm.dto.ShcCalendarDetailInfo;
import com.ca.mfd.prc.pm.dto.ShcWorkTimeInfo;
import com.ca.mfd.prc.pm.dto.ShiftDTO;
import com.ca.mfd.prc.pm.dto.ShopDTO;
import com.ca.mfd.prc.pm.entity.PmShcCalendarEntity;

import java.util.Date;
import java.util.List;

/**
 * 工厂日历
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPmShcCalendarService extends ICrudService<PmShcCalendarEntity> {

    /**
     * 获得最近更新的记录
     *
     * @return
     */
    PmShcCalendarEntity getLastUpdatePmShcCalendar();

    /**
     * 获取工厂日历树车间列表
     *
     * @return List<ShopDTO>
     */
    List<ShopDTO> getCalendarShopInfos();

    /**
     * 获得源车间日历列表
     *
     * @param calendarCopyDTO
     * @return
     */
    List<PmShcCalendarEntity> getSourceCalendarList(CalendarCopyDTO calendarCopyDTO);

    /**
     * 获得源线体日历列表
     *
     * @param calendarCopyDTO
     * @return
     */
    List<PmShcCalendarEntity> getSourceCalendarLineList(CalendarCopyDTO calendarCopyDTO);

    /**
     * 工厂日历
     *
     * @param startTime
     * @param endTime
     * @param shopCode
     * @return Integer
     */
    Integer calculateWorkTime(Date startTime, Date endTime, String shopCode);

    /**
     * 传开始时间获取车间当前班次
     *
     * @param pmShopCode 车间Code
     * @param dateTime   开始时间
     * @return ShiftDTO
     */
    ShiftDTO getShiftInfo(String pmShopCode, Date dateTime);

    /**
     * 获取车间当前班次
     *
     * @param lineCode 线体Code
     * @return ShiftDTO
     */
    ShiftDTO getCurrentShiftInfo(String lineCode);


    /**
     * 获取已经排班记录
     *
     * @param calendarParamInfo 查询参数
     * @return List<PmShcCalendarEntity>
     */
    List<PmShcCalendarEntity> getPmShcCalendarInfos(CalendarParamDTO calendarParamInfo);

    /**
     * 获取工作时间
     *
     * @param startWorkDay 开始时间
     * @param shopCode     车间code
     * @param getDays      天数
     * @return 工作时间列表
     */
    List<ShcWorkTimeInfo> getWorkTimes(Date startWorkDay, String shopCode, Integer getDays);

    /**
     * 从AS同步车间
     *
     * @param calendarFromASDTOList
     * @param shopFlag              车间标识 1-更新车间；2-更新线体
     */
    void syncCalendarFromAS(List<CalendarFromASDTO> calendarFromASDTOList, int shopFlag);

    /**
     * 获取排班信息
     *
     * @param lineCode 线体编码
     * @return 排班信息
     */
    ShcCalendarDetailInfo getCurrentShift(String lineCode);
}