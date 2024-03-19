package com.ca.mfd.prc.pps.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pps.dto.QcyjyVehicleInfoDTO;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-08-29
 */
@Mapper
public interface IPpsOrderMapper extends IBaseMapper<PpsOrderEntity> {

    /**
     * 删除历史工艺记录
     *
     * @param maps（vsn，vdatetime,vshopcode）
     * @return AVI_CODE （PRC_AVI_TRACKING_RECORD表SN匹配的首条记录）
     */
    List<String> spResetVehicleDel(Map<String, Object> maps);
}