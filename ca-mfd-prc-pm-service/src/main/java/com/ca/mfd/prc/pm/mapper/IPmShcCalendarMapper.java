package com.ca.mfd.prc.pm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.dto.PmCalendarSource;
import com.ca.mfd.prc.pm.dto.ShcCalTempInfo;
import com.ca.mfd.prc.pm.dto.ShiftDTO;
import com.ca.mfd.prc.pm.entity.PmShcCalendarEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工厂日历
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-08-29
 */
@Mapper
public interface IPmShcCalendarMapper extends IBaseMapper<PmShcCalendarEntity> {

    /**
     * 报工打印
     *
     * @param startDay 参数集合
     * @param endDay
     * @param shopCode
     * @return
     */
    List<PmCalendarSource> getcalendarsource(String startDay, String endDay, String shopCode);

    /**
     * 报工打印
     *
     * @param pmShopCode 车间编码
     * @param lineCode   线体编码
     * @return
     */
    List<ShiftDTO> getShiftInfo(@Param("pmShopCode") String pmShopCode, @Param("pmLineCode") String lineCode, @Param("minWorkDay") String minWorkDay);

    /**
     * 获得排班的日历数量
     *
     * @param shiftIds 排班表主键id
     * @return
     */
    int getCalendarCountByShiftIds(List<Long> shiftIds);

    /**
     * 报工打印
     *
     * @param req 参数集合
     * @return
     */
    List<PmShcCalendarEntity> getPmShcCalendarInfos(Map req);

    /**
     * 报工打印
     *
     * @param shopCode 参数集合
     * @param beginDt
     * @param endDt
     * @return
     */
    List<ShcCalTempInfo> getShcCalTempInfo(String shopCode, Date beginDt, Date endDt);
}