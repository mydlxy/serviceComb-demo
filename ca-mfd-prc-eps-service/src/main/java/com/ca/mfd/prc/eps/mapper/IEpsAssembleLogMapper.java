package com.ca.mfd.prc.eps.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.eps.dto.GetAssembleDataInfo;
import com.ca.mfd.prc.eps.entity.EpsAssembleLogEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 装配单日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface IEpsAssembleLogMapper extends IBaseMapper<EpsAssembleLogEntity> {

    /**
     * 装配单日志
     *
     * @param maps
     * @return
     */
    List<GetAssembleDataInfo> getAssemble(Map<String, Object> maps);
}