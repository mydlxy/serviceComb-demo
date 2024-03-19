package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsExGradeEntity;
import com.ca.mfd.prc.common.model.base.dto.ComboDataDTO;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: 精致工艺扣分等级配置服务
 * @date 2024年01月29日
 * @变更说明 BY inkelink At 2024年01月29日
 */
public interface IPqsExGradeService extends ICrudService<PqsExGradeEntity> {
    /**
     * getComboList
     *
     * @return
     */
    List<ComboDataDTO> getComboList();

    List<PqsExGradeEntity> getAllDatas();
}