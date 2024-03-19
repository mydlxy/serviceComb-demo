package com.ca.mfd.prc.pps.communication.service.impl;

import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.service.IPartPlanService;

import com.ca.mfd.prc.pps.dto.LmsPartPlanDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author inkelink
 * @Description: 零件下发计划实现
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Service
public class PartPlanServiceImpl extends MidLmsBaseServiceImpl<LmsPartPlanDTO> implements IPartPlanService {

    @Override
    public Boolean sendPartPlan(LmsPartPlanDTO lmsPartPlanDTO) {
        if(StringUtils.isBlank(lmsPartPlanDTO.getUniqueCode())
            || "0".equals(lmsPartPlanDTO.getUniqueCode())){
            Date curDate = new Date();
            lmsPartPlanDTO.setUniqueCode(String.valueOf(curDate.getTime()));
        }
       return super.fetchDataFromApi("lms_part_plan_send","零件计划下发", ApiTypeConst.LMS_PART_PLAN,lmsPartPlanDTO);
    }


    @Override
    protected Object getParams(LmsPartPlanDTO lmsPartPlanDTO) {
        return Arrays.asList(lmsPartPlanDTO);
    }

}