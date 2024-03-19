package com.ca.mfd.prc.pps.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pps.entity.PpsOrderScrapEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 生产报废订单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-08-29
 */
@Mapper
public interface IPpsOrderScrapMapper extends IBaseMapper<PpsOrderScrapEntity> {

    /**
     * 删除历史工艺记录
     *
     * @param maps（vsn）
     * @return AVI_CODE （PRC_AVI_TRACKING_RECORD表SN匹配的首条记录）
     */
    List<String> spScrapAffirmDel(Map<String, Object> maps);
}