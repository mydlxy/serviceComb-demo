package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsInspectPolicyEntity;

import java.util.List;

/**
 *
 * @Description: 检验策略服务
 * @author inkelink
 * @date 2023年11月02日
 * @变更说明 BY inkelink At 2023年11月02日
 */
public interface IPqsInspectPolicyService extends ICrudService<PqsInspectPolicyEntity> {

    /**
     * 从缓存中获取检验策略信息
     *
     * @return 检验策略列表
     */
    List<PqsInspectPolicyEntity> getAllDatas();
}