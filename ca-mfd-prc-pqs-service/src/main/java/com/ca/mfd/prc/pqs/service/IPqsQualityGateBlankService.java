package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateBlankEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: QG检验项-色块服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsQualityGateBlankService extends ICrudService<PqsQualityGateBlankEntity> {

    /**
     * 获取所有QG检查项-色块记录
     *
     * @return
     */
    List<PqsQualityGateBlankEntity> getAllDatas();
}