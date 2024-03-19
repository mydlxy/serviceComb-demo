package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.dto.ProcessRelationInfo;
import com.ca.mfd.prc.pps.entity.PpsProcessRelationEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 工序关联配置服务
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IPpsProcessRelationService extends ICrudService<PpsProcessRelationEntity> {
    /**
     * 获取列表
     *
     * @param orderCategory
     * @param processCode
     * @return
     */
    List<PpsProcessRelationEntity> getListByOrderCategory(Integer orderCategory, String processCode);

    /**
     * 查询工序关联配置
     *
     * @param category 订单大类
     * @return 工序关联配置
     */
    List<PpsProcessRelationEntity> getListByOrderCategory(int category);

    /**
     * 获取
     *
     * @param lineCode
     * @return
     */
    PpsProcessRelationEntity getFirstByLineCode(String lineCode,Integer processType);

    /**
     * 查询工序关联配置
     *
     * @param category 订单大类
     * @return 工序关联配置
     */
    List<ProcessRelationInfo> getRecordByOrderCategory(int category);
}