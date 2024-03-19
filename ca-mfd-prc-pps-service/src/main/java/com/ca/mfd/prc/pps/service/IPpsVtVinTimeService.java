package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsVtVinTimeEntity;

/**
 * @author eric.zhou
 * @Description: VIN推迟时间配置服务
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
public interface IPpsVtVinTimeService extends ICrudService<PpsVtVinTimeEntity> {
    /**
     * 获取首条
     *
     * @return
     */
    PpsVtVinTimeEntity getFirstNew();

    /**
     * 获取当前年份配置
     *
     * @param orderSign 定编码
     * @return 年份
     */
    String getYearByOrderSign(String orderSign);
}