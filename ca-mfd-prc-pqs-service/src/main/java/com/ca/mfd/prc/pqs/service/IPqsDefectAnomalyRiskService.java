package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.RiskProductFilterDto;
import com.ca.mfd.prc.pqs.dto.RiskProductListFilterInfo;
import com.ca.mfd.prc.pqs.dto.RiskRepairInfo;
import com.ca.mfd.prc.pqs.dto.SubmitRiskInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyRiskEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 质量围堵服务
 * @date 2023年09月07日
 * @变更说明 BY inkelink At 2023年09月07日
 */
public interface IPqsDefectAnomalyRiskService extends ICrudService<PqsDefectAnomalyRiskEntity> {

    /**
     * 获取所有车辆去向指定记录
     *
     * @return
     */
    List<PqsDefectAnomalyRiskEntity> getAllDatas();

    /**
     * 获取问题排查车辆列表
     *
     * @param info
     * @return
     */
    List<RiskProductFilterDto> getProductList(RiskProductListFilterInfo info);

    /**
     * 问题立项
     *
     * @param info
     * @return
     */
    String submitRiskInfo(SubmitRiskInfo info);

    /**
     * 根据问题ID 激活缺陷
     *
     * @param id              问题ID
     * @param workstationCode 工位代码
     */
    void triggerAnomalyByRiskId(Long id, String workstationCode);

    /**
     * 根据问题明细-批量激活缺陷
     *
     * @param ids             问题明细信息id
     * @param workstationCode
     */
    void triggerAnomalyByRiskDetailId(List<Long> ids, String workstationCode);

    /**
     * 根据问题ID 批量修复缺陷
     *
     * @param info 问题信息
     */
    void repairByRiskId(RiskRepairInfo info);

    /**
     * 根据问题明细ID 批量修复缺陷
     *
     * @param info 修复信息
     */
    void repairByRiskDetailId(RiskRepairInfo info);

    /**
     * 根据问题明细ID 批量修复缺陷
     *
     * @param info 修复信息
     */
    void recheck(RiskRepairInfo info);

    /**
     * 关闭问题
     *
     * @param info
     */
    void closeRisk(RiskRepairInfo info);
}