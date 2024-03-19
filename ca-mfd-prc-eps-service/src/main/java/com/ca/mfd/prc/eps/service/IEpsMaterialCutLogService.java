package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsMaterialCutLogEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 生产物料切换记录服务
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
public interface IEpsMaterialCutLogService extends ICrudService<EpsMaterialCutLogEntity> {

    /**
     * 获取未发送的数据
     *
     * @return
     */
    List<EpsMaterialCutLogEntity> getNotSendData();

    /**
     * 获取所有的数据
     *
     * @return
     */
    List<EpsMaterialCutLogEntity> getAllDatas();
}