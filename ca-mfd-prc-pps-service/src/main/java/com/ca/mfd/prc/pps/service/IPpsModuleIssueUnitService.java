package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsModuleIssueUnitEntity;

import java.util.List;

/**
 *
 * @Description: 电池预成组下发小单元服务
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IPpsModuleIssueUnitService extends ICrudService<PpsModuleIssueUnitEntity> {

    /**
     * 列表
     *
     * @param moduleIssueModuleId
     * @return
     */
    List<PpsModuleIssueUnitEntity> getByModuleIssueModuleId(Long moduleIssueModuleId);
}