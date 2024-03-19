package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsRinTimeConfigEntity;

import java.util.List;

/**
 * @author eric.zhou
 * @Description: 电池RIN码时间配置服务
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
public interface IPpsRinTimeConfigService extends ICrudService<PpsRinTimeConfigEntity> {
    /**
     * 获取所有的数据
     *
     * @return
     */
    List<PpsRinTimeConfigEntity> getAllDatas();
}