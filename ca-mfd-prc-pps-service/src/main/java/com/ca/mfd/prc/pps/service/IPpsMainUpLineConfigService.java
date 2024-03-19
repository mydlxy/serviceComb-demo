package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.dto.LmsWeOnlineQueueDTO;
import com.ca.mfd.prc.pps.entity.PpsMainUpLineConfigEntity;

import java.util.List;

/**
 *
 * @Description: 焊装主体上线队列服务
 * @author inkelink
 * @date 2024年01月18日
 * @变更说明 BY inkelink At 2024年01月18日
 */
public interface IPpsMainUpLineConfigService extends ICrudService<PpsMainUpLineConfigEntity> {

    /**
     * 获取所有的数据
     *
     * @return List<PpsMainUpLineConfigEntity>
     */
    List<PpsMainUpLineConfigEntity> getAllDatas();

    /**
     * 获取lms焊装工单上线队列
     */
    List<LmsWeOnlineQueueDTO> getWeOnlineQueue(List<String> subCodes, Boolean isSendLms, Boolean isChangeStatus);
}