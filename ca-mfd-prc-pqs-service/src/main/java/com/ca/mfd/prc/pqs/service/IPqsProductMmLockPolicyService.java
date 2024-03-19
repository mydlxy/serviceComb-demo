package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsProductMmLockPolicyEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 追溯件阻塞服务
 * @date 2023年09月08日
 * @变更说明 BY inkelink At 2023年09月08日
 */
public interface IPqsProductMmLockPolicyService extends ICrudService<PqsProductMmLockPolicyEntity> {

    /**
     * 获取所有追溯件阻塞记录
     *
     * @return
     */
    List<PqsProductMmLockPolicyEntity> getAllDatas();
}