package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsExEntryAttchmentEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: 精致工艺附件服务
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
public interface IPqsExEntryAttchmentService extends ICrudService<PqsExEntryAttchmentEntity> {

    List<PqsExEntryAttchmentEntity> getAllDatas();
}