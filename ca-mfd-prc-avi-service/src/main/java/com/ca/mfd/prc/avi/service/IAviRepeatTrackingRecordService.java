package com.ca.mfd.prc.avi.service;

import com.ca.mfd.prc.avi.entity.AviRepeatTrackingRecordEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * 关键过点配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
public interface IAviRepeatTrackingRecordService extends ICrudService<AviRepeatTrackingRecordEntity> {
    /**
     * 获取所有数据
     *
     * @return 返回所有数据
     */
    List<AviRepeatTrackingRecordEntity> getAllDatas();
}