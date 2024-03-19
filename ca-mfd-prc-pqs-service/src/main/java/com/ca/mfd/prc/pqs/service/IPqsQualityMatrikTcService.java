package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikTcEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 百格图-车型服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsQualityMatrikTcService extends ICrudService<PqsQualityMatrikTcEntity> {

    /**
     * 获取所有数据
     *
     * @return
     */
    List<PqsQualityMatrikTcEntity> getAllDatas();
}