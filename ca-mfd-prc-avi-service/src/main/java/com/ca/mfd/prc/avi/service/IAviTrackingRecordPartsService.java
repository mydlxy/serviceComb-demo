package com.ca.mfd.prc.avi.service;

import com.ca.mfd.prc.avi.entity.AviTrackingRecordPartsEntity;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: 离散产品过点信息服务
 * @date 2023年10月31日
 * @变更说明 BY inkelink At 2023年10月31日
 */
public interface IAviTrackingRecordPartsService extends ICrudService<AviTrackingRecordPartsEntity> {
    /**
     * 写入产品过点信息
     *
     * @param pm            pm
     * @param sn            车辆识别码
     * @param lineCode      线体Code
     * @param orderCategory 分类
     */
    void virtualTrassAreaPoint(PmAllDTO pm, String sn, String lineCode, Integer orderCategory);

    /**
     * 报工统计
     *
     * @param orderCategory 分类
     * @return
     */
    List<AviTrackingRecordPartsEntity> getRecordByOrderCategory(int orderCategory);

    /**
     * 更新处理状态
     *
     * @param ids 主键
     */
    void updateProcessByIds(List<Long> ids);
}