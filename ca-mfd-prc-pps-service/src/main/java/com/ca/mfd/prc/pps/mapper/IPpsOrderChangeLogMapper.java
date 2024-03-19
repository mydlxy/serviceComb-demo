package com.ca.mfd.prc.pps.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pps.entity.PpsOrderChangeLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单替换日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-08-29
 */
@Mapper
public interface IPpsOrderChangeLogMapper extends IBaseMapper<PpsOrderChangeLogEntity> {

}