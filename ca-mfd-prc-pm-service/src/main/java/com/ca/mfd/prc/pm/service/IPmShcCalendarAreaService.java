package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.dto.CalendarCopyDTO;
import com.ca.mfd.prc.pm.entity.PmShcCalendarAreaEntity;

import java.util.List;
import java.util.Map;

/**
 * 工厂日历
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Deprecated
public interface IPmShcCalendarAreaService extends ICrudService<PmShcCalendarAreaEntity> {
    void batchSaveExcelData(List<Map<String, String>> data) throws Exception;

    /**
     * 获得源线体日历列表
     *
     * @param calendarCopyDTO
     * @return
     */
    List<PmShcCalendarAreaEntity> getSourceCalendarAreaList(CalendarCopyDTO calendarCopyDTO);
}