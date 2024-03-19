package com.ca.mfd.prc.eps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.dto.CarCloudDeviceDto;
import com.ca.mfd.prc.eps.communication.dto.CheyunTestDto;
import com.ca.mfd.prc.eps.communication.entity.MidCarCloudDeviceEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 *
 * @Description: 车辆设备中间表（车云）服务
 * @author inkelink
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
public interface IMidCarCloudDeviceService extends ICrudService<MidCarCloudDeviceEntity> {

    ResultVO carCloudDeviceSend(String vinCode);

    ResultVO<List<CarCloudDeviceDto>> queryByVins(List<String> vins);

    ResultVO<List<CarCloudDeviceDto>> queryByDate(String startTime, String endTime);

    ResultVO carCloudDeviceSendTest(CheyunTestDto dto);
}