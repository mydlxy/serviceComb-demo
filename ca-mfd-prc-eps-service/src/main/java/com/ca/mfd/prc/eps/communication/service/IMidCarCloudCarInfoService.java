package com.ca.mfd.prc.eps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.dto.CarCloudCarInfoDto;
import com.ca.mfd.prc.eps.communication.dto.CheyunTestDto;
import com.ca.mfd.prc.eps.communication.entity.MidCarCloudCarInfoEntity;

import java.util.List;

/**
 *
 * @Description: 车辆基础数据中间表（车云）服务
 * @author inkelink
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
public interface IMidCarCloudCarInfoService extends ICrudService<MidCarCloudCarInfoEntity> {

    ResultVO<CarCloudCarInfoDto> carCloudCarInfoSend(String vinCode);

    ResultVO<List<MidCarCloudCarInfoEntity>> queryByVins(List<String> vins);

    ResultVO<List<MidCarCloudCarInfoEntity>> queryByDate(String startTime, String endTime);

    ResultVO<CarCloudCarInfoDto> carCloudCarInfoSendTest(CheyunTestDto dto);

    ResultVO<CarCloudCarInfoDto> providerQueryByVin(String vin);
}