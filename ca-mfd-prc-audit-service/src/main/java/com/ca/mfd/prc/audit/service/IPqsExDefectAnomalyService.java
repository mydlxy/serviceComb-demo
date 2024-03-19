package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsExDefectAnomalyEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 *
 * @Description: 精艺检修缺陷库配置服务
 * @author inkelink
 * @date 2024年01月29日
 * @变更说明 BY inkelink At 2024年01月29日
 */
public interface IPqsExDefectAnomalyService extends ICrudService<PqsExDefectAnomalyEntity> {

    List<PqsExDefectAnomalyEntity> getAllDatas();
}