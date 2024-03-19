package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsModuleIssueMainEntity;

/**
 *
 * @Description: 电池预成组下发主体服务
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IPpsModuleIssueMainService extends ICrudService<PpsModuleIssueMainEntity> {


    /**
     * 获取
     *
     * @param entryNo
     * @return
     */
    PpsModuleIssueMainEntity getFirstByEntryNo(String entryNo);
}