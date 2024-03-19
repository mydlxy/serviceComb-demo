package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsPlanEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 检验计划配置服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsPlanService extends ICrudService<PqsPlanEntity> {

    /**
     * 获取所有检验计划配置记录
     *
     * @return
     */
    List<PqsPlanEntity> getAllDatas();
}