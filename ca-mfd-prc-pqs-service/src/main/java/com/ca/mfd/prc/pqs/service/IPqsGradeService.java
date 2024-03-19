package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsGradeEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 缺陷等级配置服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsGradeService extends ICrudService<PqsGradeEntity> {

    /**
     * 获取缺陷等级配置数据
     *
     * @return
     */
    List<PqsGradeEntity> getAllDatas();
}