package com.ca.mfd.prc.avi.communication.service;

import com.ca.mfd.prc.avi.communication.dto.EcuCarInfoDto;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.avi.communication.entity.MidDjEcuSendEntity;
import com.ca.mfd.prc.common.utils.ResultVO;

import java.util.List;

/**
 *
 * @Description: 电检软件信息下发服务
 * @author inkelink
 * @date 2023年12月25日
 * @变更说明 BY inkelink At 2023年12月25日
 */
public interface IMidDjEcuSendService extends ICrudService<MidDjEcuSendEntity> {

    ResultVO<List<EcuCarInfoDto>> queryEcuInfoByVin(String vin);
}