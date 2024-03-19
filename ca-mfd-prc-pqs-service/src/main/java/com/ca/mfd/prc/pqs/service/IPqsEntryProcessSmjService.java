package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsEntryProcessSmjEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 质检工单-过程检验_首末检验服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsEntryProcessSmjService extends ICrudService<PqsEntryProcessSmjEntity> {

    /**
     * 获取所有质检工单-过程检验_首末检验记录
     *
     * @return
     */
    List<PqsEntryProcessSmjEntity> getAllDatas();
}