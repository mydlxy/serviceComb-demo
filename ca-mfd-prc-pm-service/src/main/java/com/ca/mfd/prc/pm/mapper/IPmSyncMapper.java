package com.ca.mfd.prc.pm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.entity.PmSyncEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author inkelink
 * @Description: 数据同步缓存表Mapper
 * @date 2023年09月01日
 * @变更说明 BY inkelink At 2023年09月01日
 */
@Mapper
public interface IPmSyncMapper extends IBaseMapper<PmSyncEntity> {

}