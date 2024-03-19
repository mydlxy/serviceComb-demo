package com.ca.mfd.prc.pm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.entity.PmShcCalendarAreaEntity;
import com.ca.mfd.prc.pm.entity.PmShcCalendarEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 工厂日历-线体
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-08-29
 */
@Deprecated
@Mapper
public interface IPmShcCalendarAreaMapper extends IBaseMapper<PmShcCalendarAreaEntity> {
    /**
     * 获得线体的工厂日历集合
     *
     * @param req 参数集合
     * @return
     */
    List<PmShcCalendarEntity> getPmShcCalendarInfos(Map req);
}