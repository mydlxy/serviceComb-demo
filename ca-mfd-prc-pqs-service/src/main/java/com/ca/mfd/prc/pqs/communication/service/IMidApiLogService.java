package com.ca.mfd.prc.pqs.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.communication.entity.MidApiLogEntity;

import java.util.List;

/**
 *
 * @Description: 接口记录表服务
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
public interface IMidApiLogService extends ICrudService<MidApiLogEntity> {
    /**
     * 获取需要同步的数据
     * @return
     */
    List<MidApiLogEntity> getSyncList(String apitype);
}