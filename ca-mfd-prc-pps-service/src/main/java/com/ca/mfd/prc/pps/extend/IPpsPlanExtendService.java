package com.ca.mfd.prc.pps.extend;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;

import java.util.List;

/**
 * 工单扩展
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPpsPlanExtendService extends ICrudService<PpsPlanEntity> {

    /**
     * 获取
     *
     * @param planNo
     * @return
     */
    PpsPlanEntity getFirstByPlanNo(String planNo);

    /**
     * 获取所有，还没有拆分的订单
     *
     * @param category
     * @return
     */
    List<PpsPlanEntity> getByCategoryStatus(String category);

    /**
     * 获取分组数据
     *
     * @param planNos
     * @return
     */
    List<PpsPlanEntity> getGroupByPlanNo(List<String> planNos);

    /**
     * 根据计划编码
     *
     * @param planNo 计划编号
     */
    void updateEntityByPlanNo(String planNo);
}