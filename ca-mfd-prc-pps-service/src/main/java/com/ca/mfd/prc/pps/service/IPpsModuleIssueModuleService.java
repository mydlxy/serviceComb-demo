package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.dto.ModuleDetailInfo;
import com.ca.mfd.prc.pps.entity.PpsModuleIssueModuleEntity;

import java.util.List;

/**
 *
 * @Description: 电池预成组下发模组服务
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IPpsModuleIssueModuleService extends ICrudService<PpsModuleIssueModuleEntity> {

    /**
     * 获取
     *
     * @param lineCode
     * @return
     */
    List<PpsModuleIssueModuleEntity> getListByLineEntryCode(String entryNo,String lineCode);

    /**
     * 获取详情
     *
     * @param lineCode
     * @return
     */
    String getEntryNoByLineCode(String lineCode);

    /**
     * 获取详情
     *
     * @param mainId
     * @return
     */
    List<PpsModuleIssueModuleEntity> getListByMainId(Long mainId);

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    ModuleDetailInfo moduleDetail(Long id);

    /**
     * 获取电池预成组下发模组
     *
     * @param planNo   计划编码
     * @param lineCode 生产线体编码
     * @return 电池预成组下发模组
     */
    PpsModuleIssueModuleEntity getRecordInfoByPlanNo(String planNo, String lineCode);
}