package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsAuditDeptEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: AUDIT责任部门配置服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsAuditDeptService extends ICrudService<PqsAuditDeptEntity> {

    List<PqsAuditDeptEntity> getAllDatas();
}