package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsEntryStockInEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 入库质检工单服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsEntryStockInService extends ICrudService<PqsEntryStockInEntity> {

    /**
     * 获取所有入库质检工单记录
     *
     * @return
     */
    List<PqsEntryStockInEntity> getAllDatas();
}