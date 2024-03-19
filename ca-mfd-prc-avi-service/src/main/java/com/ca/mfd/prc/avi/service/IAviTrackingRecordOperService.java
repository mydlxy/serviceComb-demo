package com.ca.mfd.prc.avi.service;

import com.ca.mfd.prc.avi.entity.AviTrackingRecordOperEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * 产品过点信息行为记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
public interface IAviTrackingRecordOperService extends ICrudService<AviTrackingRecordOperEntity> {

    /**
     * 过点生产行为
     */
    /**
     * 过点生产行为
     *
     * @param id ID
     */
    void createAviPointOperDetailData(String id);


    /**
     * 根据配置行为获取未处理队列
     *
     * @param action action
     * @return 未处理队列
     */
    List<AviTrackingRecordOperEntity> getTopNoProcessData(String action);

    List<AviTrackingRecordOperEntity> getData(String sn, String action);

}