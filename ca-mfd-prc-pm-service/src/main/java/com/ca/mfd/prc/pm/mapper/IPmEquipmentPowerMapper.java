package com.ca.mfd.prc.pm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.entity.PmEquipmentPowerEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 * @Description: 设备能力Mapper
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Mapper
public interface IPmEquipmentPowerMapper extends IBaseMapper<PmEquipmentPowerEntity> {
	List<PmEquipmentPowerEntity> getEquipmentPowersByShopId(Long workshopId);
}