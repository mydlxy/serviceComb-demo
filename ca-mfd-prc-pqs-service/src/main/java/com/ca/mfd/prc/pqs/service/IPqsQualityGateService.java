package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: QG检查项服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsQualityGateService extends ICrudService<PqsQualityGateEntity> {

    /**
     * 获取所有QG检查项记录
     *
     * @return
     */
    List<PqsQualityGateEntity> getAllDatas();
}