package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyRiskDetailEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 质量围堵-清单服务
 * @date 2023年09月07日
 * @变更说明 BY inkelink At 2023年09月07日
 */
public interface IPqsDefectAnomalyRiskDetailService extends ICrudService<PqsDefectAnomalyRiskDetailEntity> {

    /**
     * 获取所有质量围堵-清单记录
     *
     * @return
     */
    List<PqsDefectAnomalyRiskDetailEntity> getAllDatas();
}