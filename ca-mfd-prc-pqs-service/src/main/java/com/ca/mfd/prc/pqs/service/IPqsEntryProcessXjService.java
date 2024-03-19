package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsEntryProcessXjEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 过程检验-巡检明细服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsEntryProcessXjService extends ICrudService<PqsEntryProcessXjEntity> {

    /**
     * 获取所有过程检验-巡检明细记录
     *
     * @return
     */
    List<PqsEntryProcessXjEntity> getAllDatas();
}