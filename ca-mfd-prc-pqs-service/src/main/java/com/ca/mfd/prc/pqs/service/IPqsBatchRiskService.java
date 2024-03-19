package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.EntryAndPlanParasDto;
import com.ca.mfd.prc.pqs.dto.RiskRepairInfo;
import com.ca.mfd.prc.pqs.dto.SubmitBatchRiskInfo;
import com.ca.mfd.prc.pqs.entity.PqsBatchRiskEntity;

import java.util.List;

/**
 *
 * @Description: 批次件问题排查（质量围堵）服务
 * @author inkelink
 * @date 2023年11月08日
 * @变更说明 BY inkelink At 2023年11月08日
 */
public interface IPqsBatchRiskService extends ICrudService<PqsBatchRiskEntity> {

    /**
     * 获取所有批次件问题排查（质量围堵）记录
     *
     * @return
     */
    List<PqsBatchRiskEntity> getAllDatas();

    /**
     * 获取计划单号和工单号
     *
     * @param planNoOrEntryNo
     * @return
     */
    EntryAndPlanParasDto getEntryNoAndPlanNo(String planNoOrEntryNo);

    /**
     * 批次件问题立项
     *
     * @param info
     * @return
     */
    void submitBatchRiskInfo(SubmitBatchRiskInfo info);

    /**
     * 关闭问题
     *
     * @param info
     */
    void closeRisk(RiskRepairInfo info);
}