package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsTraceCodeRuleEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 条码追溯规则服务
 * @date 2023年09月12日
 * @变更说明 BY inkelink At 2023年09月12日
 */
public interface IEpsTraceCodeRuleService extends ICrudService<EpsTraceCodeRuleEntity> {
    /**
     * 获取所有的数据
     *
     * @return
     */
    List<EpsTraceCodeRuleEntity> getAllDatas();
}