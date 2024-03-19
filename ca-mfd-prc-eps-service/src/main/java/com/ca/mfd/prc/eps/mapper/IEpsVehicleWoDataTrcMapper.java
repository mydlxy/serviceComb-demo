package com.ca.mfd.prc.eps.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.eps.dto.CommunicationTrcModel;
import com.ca.mfd.prc.eps.dto.VehicleBatteryInfoDTO;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataTrcEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 追溯操作记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface IEpsVehicleWoDataTrcMapper extends IBaseMapper<EpsVehicleWoDataTrcEntity> {

    /**
     * 列表
     *
     * @param sn
     * @param woCodes
     * @return
     */
    List<CommunicationTrcModel> getCommunicationTrc2(String sn, List<String> woCodes);

    /**
     * 列表
     *
     * @param woCode
     * @param vin
     * @return
     */
    List<VehicleBatteryInfoDTO> getWoCodeTrc(List<String> woCode, List<String> vin);
}