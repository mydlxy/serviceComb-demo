package com.ca.mfd.prc.eps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.dto.CheyunTestDto;
import com.ca.mfd.prc.eps.communication.entity.MidCarCloudCheckEntity;

/**
 *
 * @Description: 终检完成数据中间表（车云）服务
 * @author inkelink
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
public interface IMidCarCloudCheckService extends ICrudService<MidCarCloudCheckEntity> {

    ResultVO carCloudCheckSend(String vinCode);

    ResultVO carCloudCheckSendTest(CheyunTestDto dto);
}