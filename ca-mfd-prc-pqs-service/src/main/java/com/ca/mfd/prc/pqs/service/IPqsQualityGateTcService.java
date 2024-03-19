package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateTcEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: QG检查项-车型服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsQualityGateTcService extends ICrudService<PqsQualityGateTcEntity> {

    /**
     * 获取所有QG检查项-车型记录
     *
     * @return
     */
    List<PqsQualityGateTcEntity> getAllDatas();
}