package com.ca.mfd.prc.avi.mapper;

import com.ca.mfd.prc.avi.entity.AviOperationLogEntity;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 产品跟踪站点终端操作日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@Mapper
public interface IAviOperationLogMapper extends IBaseMapper<AviOperationLogEntity> {

}