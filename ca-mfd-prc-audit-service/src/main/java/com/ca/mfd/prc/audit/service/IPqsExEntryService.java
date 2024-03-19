package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsExEntryEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 *
 * @Description: 精致工艺评审单服务
 * @author inkelink
 * @date 2024年01月30日
 * @变更说明 BY inkelink At 2024年01月30日
 */
public interface IPqsExEntryService extends ICrudService<PqsExEntryEntity> {

    List<PqsExEntryEntity> getAllDatas();
}