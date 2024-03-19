package com.ca.mfd.prc.avi.communication.service;

import com.ca.mfd.prc.avi.communication.entity.MidDjTestSendEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;


/**
 *
 * @Description: 手动下发数据
 * @author inkelink
 * @date 2023年12月25日
 * @变更说明 BY inkelink At 2023年12月25日
 */
public interface IMidDjTestSendService extends ICrudService<MidDjTestSendEntity> {
    List<MidDjTestSendEntity> getDataByIds(List<String> ids);
}