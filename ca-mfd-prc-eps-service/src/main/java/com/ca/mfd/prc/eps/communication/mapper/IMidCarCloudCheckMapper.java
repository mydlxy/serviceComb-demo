package com.ca.mfd.prc.eps.communication.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.eps.communication.entity.MidCarCloudCheckEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @Description: 终检完成数据中间表（车云）Mapper
 * @author inkelink
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
@Mapper
public interface IMidCarCloudCheckMapper extends IBaseMapper<MidCarCloudCheckEntity> {
	
}