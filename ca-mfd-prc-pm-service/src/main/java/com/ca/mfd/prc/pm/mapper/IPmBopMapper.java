package com.ca.mfd.prc.pm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.entity.PmBopEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * @author inkelink
 * @Description: 超级BOPMapper
 * @date 2023年08月30日
 * @变更说明 BY inkelink At 2023年08月30日
 */
@Mapper
public interface IPmBopMapper extends IBaseMapper<PmBopEntity> {

    List<HashMap<String,Object>> getBomwd7(String tbname);
}