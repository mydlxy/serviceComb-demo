package com.ca.mfd.prc.pm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.dto.PmBopBomDto;
import com.ca.mfd.prc.pm.entity.PmBopBomEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 * @Description: MBOM详情Mapper
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
@Mapper
public interface IPmBopBomMapper extends IBaseMapper<PmBopBomEntity> {

    List<PmBopBomDto> getList();
	
}