package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsIssueRecordEntity;

import java.util.List;

/**
 *
 * @Description: 问题预警记录服务
 * @author inkelink
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
public interface IPqsIssueRecordService extends ICrudService<PqsIssueRecordEntity> {

    List<PqsIssueRecordEntity> getAllDatas();
}