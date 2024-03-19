package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.model.base.dto.ComboDataDTO;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsInspectionCheckItemTypeEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 检验类型技术配置服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsInspectionCheckItemTypeService extends ICrudService<PqsInspectionCheckItemTypeEntity> {

    List<PqsInspectionCheckItemTypeEntity> getAllDatas();

    List<ComboDataDTO> getCombo();
}