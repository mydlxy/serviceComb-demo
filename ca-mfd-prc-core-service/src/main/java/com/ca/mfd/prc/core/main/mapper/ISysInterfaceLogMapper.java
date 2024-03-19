package com.ca.mfd.prc.core.main.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.main.entity.SysInterfaceLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 第三方接口交互记录
 *
 * @author inkelink ${email}
 * @date 2023-04-03
 */
@Mapper
public interface ISysInterfaceLogMapper extends IBaseMapper<SysInterfaceLogEntity> {

}