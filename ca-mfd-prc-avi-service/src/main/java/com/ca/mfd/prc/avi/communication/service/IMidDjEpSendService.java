package com.ca.mfd.prc.avi.communication.service;

import com.ca.mfd.prc.avi.communication.entity.MidDjEpSendEntity;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.EpInfoDto;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.common.utils.ResultVO;

import java.util.List;

/**
 * @author inkelink
 * @Description: EP信息下发记录服务
 * @date 2023年12月27日
 * @变更说明 BY inkelink At 2023年12月27日
 */
public interface IMidDjEpSendService extends ICrudService<MidDjEpSendEntity> {
    ResultVO<List<EpInfoDto>> queryEpInfoByVin(String vin);
}