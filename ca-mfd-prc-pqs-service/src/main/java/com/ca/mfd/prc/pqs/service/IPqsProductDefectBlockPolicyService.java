package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsProductDefectBlockPolicyEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 缺陷堵塞策略服务
 * @date 2023年09月08日
 * @变更说明 BY inkelink At 2023年09月08日
 */
public interface IPqsProductDefectBlockPolicyService extends ICrudService<PqsProductDefectBlockPolicyEntity> {

    /**
     * 获取所有缺陷堵塞策略记录
     *
     * @return
     */
    List<PqsProductDefectBlockPolicyEntity> getAllDatas();
}