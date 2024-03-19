package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsExDeptEntity;
import com.ca.mfd.prc.common.model.base.dto.ComboDataDTO;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 *
 * @Description: 精致工艺责任部门配置服务
 * @author inkelink
 * @date 2024年01月29日
 * @变更说明 BY inkelink At 2024年01月29日
 */
public interface IPqsExDeptService extends ICrudService<PqsExDeptEntity> {

    List<PqsExDeptEntity> getAllDatas();

    List<ComboDataDTO> getComboList();
}