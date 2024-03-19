package com.ca.mfd.prc.pps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.communication.entity.MidSoftwareBomVersionEntity;

import java.util.List;

/**
 *
 * @Description: 单车软件清单版本服务
 * @author inkelink
 * @date 2023年11月23日
 * @变更说明 BY inkelink At 2023年11月23日
 */
public interface IMidSoftwareBomVersionService extends ICrudService<MidSoftwareBomVersionEntity> {

    /**
     * 获取全部数据
     * @return
     */
    List<MidSoftwareBomVersionEntity> getAllDatas();
}