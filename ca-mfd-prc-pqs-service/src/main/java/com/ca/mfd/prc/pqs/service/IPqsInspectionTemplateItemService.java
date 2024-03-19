package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsInspectionTemplateItemEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 检验模板-项目服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsInspectionTemplateItemService extends ICrudService<PqsInspectionTemplateItemEntity> {

    List<PqsInspectionTemplateItemEntity> getAllDatas();

    void saveExcelData(List<PqsInspectionTemplateItemEntity> entities);
}