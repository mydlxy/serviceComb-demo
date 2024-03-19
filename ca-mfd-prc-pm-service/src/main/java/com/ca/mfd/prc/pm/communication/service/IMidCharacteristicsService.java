package com.ca.mfd.prc.pm.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.communication.entity.MidCharacteristicsEntity;

/**
 *
 * @Description: 单车BOM数据服务
 * @author inkelink
 * @date 2023年10月24日
 * @变更说明 BY inkelink At 2023年10月24日
 */
public interface IMidCharacteristicsService extends ICrudService<MidCharacteristicsEntity> {

    /**
     * 获取物料主数据
     * @param materialNo
     */
    String getCharacteristicsVersions(String materialNo);
}