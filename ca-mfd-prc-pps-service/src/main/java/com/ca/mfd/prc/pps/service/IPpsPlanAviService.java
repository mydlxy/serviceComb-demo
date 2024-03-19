package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsPlanAviEntity;

import java.util.List;

/**
 * @author eric.zhou
 * @Description: 计划履历;服务
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
public interface IPpsPlanAviService extends ICrudService<PpsPlanAviEntity> {


    /**
     * 获取列表
     *
     * @param planNo
     * @return
     */
    List<PpsPlanAviEntity> getListByPlanNo(String planNo);
}