package com.ca.mfd.prc.pm.communication.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.communication.entity.MidBomPartEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @Description: 零件BOM中间表Mapper
 * @author inkelink
 * @date 2023年12月11日
 * @变更说明 BY inkelink At 2023年12月11日
 */
@Mapper
public interface IMidBomPartMapper extends IBaseMapper<MidBomPartEntity> {
	
}