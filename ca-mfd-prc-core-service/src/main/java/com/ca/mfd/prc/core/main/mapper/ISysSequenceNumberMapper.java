package com.ca.mfd.prc.core.main.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.main.entity.SysSequenceNumberEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 业务编码配规则配置
 *
 * @author inkelink ${email}
 * @date 2023-04-03
 */
@Mapper
public interface ISysSequenceNumberMapper extends IBaseMapper<SysSequenceNumberEntity> {

    /**
     * 获取序列号
     *
     * @param maps
     * @return
     */
    List<String> getSeqNumNewByMysql(Map<String, Object> maps);

    /**
     * 获取序列号服务
     *
     * @param maps
     * @return
     */
    List<String> getSeqNumNewBySqlServer(Map<String, Object> maps);
}