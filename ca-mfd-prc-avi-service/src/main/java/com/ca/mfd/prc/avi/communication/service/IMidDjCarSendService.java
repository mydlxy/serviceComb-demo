package com.ca.mfd.prc.avi.communication.service;

import com.ca.mfd.prc.avi.communication.dto.CarInfoDto;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.avi.communication.entity.MidDjCarSendEntity;
import com.ca.mfd.prc.common.utils.ResultVO;

import java.util.List;

/**
 *
 * @Description: 整车信息下发记录服务
 * @author inkelink
 * @date 2023年12月25日
 * @变更说明 BY inkelink At 2023年12月25日
 */
public interface IMidDjCarSendService extends ICrudService<MidDjCarSendEntity> {

    ResultVO<List<CarInfoDto>> queryCarInfoByVin(String vin);
}