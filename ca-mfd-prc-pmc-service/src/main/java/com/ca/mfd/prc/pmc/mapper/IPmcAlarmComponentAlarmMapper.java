package com.ca.mfd.prc.pmc.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pmc.dto.AndonSourceDataDTO;
import com.ca.mfd.prc.pmc.entity.PmcAlarmComponentAlarmEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组件报警记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface IPmcAlarmComponentAlarmMapper extends IBaseMapper<PmcAlarmComponentAlarmEntity> {

    /**
     * 获取Andon数据
     *
     * @param startTime
     * @param endTime
     * @param andonTypes
     * @return
     */
    List<AndonSourceDataDTO> getAndonData(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("andonTypes") String andonTypes);
}