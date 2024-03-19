package com.ca.mfd.prc.pqs.communication.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pqs.communication.entity.MidApiLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @Description: 接口记录表Mapper
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Mapper
public interface IMidApiLogMapper extends IBaseMapper<MidApiLogEntity> {
	
}