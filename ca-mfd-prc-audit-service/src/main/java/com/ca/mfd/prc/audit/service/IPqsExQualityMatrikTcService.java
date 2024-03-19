package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikTcEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: 精致工艺百格图-车型服务
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
public interface IPqsExQualityMatrikTcService extends ICrudService<PqsExQualityMatrikTcEntity> {
    /**
     * 获取全部数据
     *
     * @return
     */
    List<PqsExQualityMatrikTcEntity> getAllDatas();
}