package com.ca.mfd.prc.pps.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pps.entity.PpsVtVinRuleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * VIN配置,前7位
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-08-29
 */
@Mapper
public interface IPpsVtVinRuleMapper extends IBaseMapper<PpsVtVinRuleEntity> {

}