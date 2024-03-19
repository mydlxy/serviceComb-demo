package com.ca.mfd.prc.pm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.dto.PmCalendarSourceDTO;
import com.ca.mfd.prc.pm.entity.PmShcCalendarAtomEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 工厂日历清洗
 *
 * @author jay.he
 */
@Mapper
public interface IPmShcCalendarAtomMapper extends IBaseMapper<PmShcCalendarAtomEntity> {
    List<PmCalendarSourceDTO> getCalendarSource(String workDay);

}