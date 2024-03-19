package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsPartsScrapEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 质检工单-评审工单服务
 * @date 2023年09月17日
 * @变更说明 BY inkelink At 2023年09月17日
 */
public interface IPqsPartsScrapService extends ICrudService<PqsPartsScrapEntity> {

    /**
     * 获取所有车辆去向指定记录
     *
     * @return
     */
    List<PqsPartsScrapEntity> getAllDatas();
}