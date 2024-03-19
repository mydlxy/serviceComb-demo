package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsPackBranchingPlanConfigEntity;

import java.util.List;

/**
 *
 * @Description: 电池分线计划配置服务
 * @author inkelink
 * @date 2024年01月22日
 * @变更说明 BY inkelink At 2024年01月22日
 */
public interface IPpsPackBranchingPlanConfigService extends ICrudService<PpsPackBranchingPlanConfigEntity> {
    /**
     * 获取所有的数据
     *
     * @return List<PpsPackBranchingPlanConfigEntity>
     */
    List<PpsPackBranchingPlanConfigEntity> getAllDatas();
}