package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsExQualityGateAnomalyEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 *
 * @Description: 精致工艺 QG检验项-缺陷服务
 * @author inkelink
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
public interface IPqsExQualityGateAnomalyService extends ICrudService<PqsExQualityGateAnomalyEntity> {

    List<PqsExQualityGateAnomalyEntity> getAllDatas();
}