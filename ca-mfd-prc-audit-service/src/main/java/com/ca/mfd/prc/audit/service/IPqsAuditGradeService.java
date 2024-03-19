package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsAuditGradeEntity;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: AUDIT缺陷等级配置服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsAuditGradeService extends ICrudService<PqsAuditGradeEntity> {
    /**
     * getComboList
     *
     * @return
     */
    List<ComboInfoDTO> getComboList();

    List<PqsAuditGradeEntity> getAllDatas();
}