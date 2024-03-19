package com.ca.mfd.prc.pps.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pps.entity.PpsRinStandardConfigEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author eric.zhou
 * @Description: 电池RIN码前14位配置Mapper
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@Mapper
public interface IPpsRinStandardConfigMapper extends IBaseMapper<PpsRinStandardConfigEntity> {

}