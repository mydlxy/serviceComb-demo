package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikWorkstationEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 *
 * @Description: 精致工艺百格图-关联的岗位服务
 * @author inkelink
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
public interface IPqsExQualityMatrikWorkstationService extends ICrudService<PqsExQualityMatrikWorkstationEntity> {
    /**
     * 获取全部数据
     * @return
     */
    List<PqsExQualityMatrikWorkstationEntity> getAllDatas();
}