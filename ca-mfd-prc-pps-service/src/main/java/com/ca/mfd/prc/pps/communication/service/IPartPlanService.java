package com.ca.mfd.prc.pps.communication.service;

import com.ca.mfd.prc.pps.dto.LmsPartPlanDTO;

/**
 * @author inkelink
 * @Description: 自动锁定计划服务实现
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
public interface IPartPlanService {

    /**
     * 零件下发计划实现
     */
    Boolean sendPartPlan(LmsPartPlanDTO lmsPartPlanDTO);

}