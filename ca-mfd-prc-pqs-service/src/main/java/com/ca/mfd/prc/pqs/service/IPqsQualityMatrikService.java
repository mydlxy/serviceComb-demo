package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 百格图服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsQualityMatrikService extends ICrudService<PqsQualityMatrikEntity> {

    /**
     * 获取所有数据
     *
     * @return
     */
    List<PqsQualityMatrikEntity> getAllDatas();
}