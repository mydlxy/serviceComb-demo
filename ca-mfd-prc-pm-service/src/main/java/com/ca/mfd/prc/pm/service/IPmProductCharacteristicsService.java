package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.dto.ProductCharacteristicsDTO;
import com.ca.mfd.prc.pm.entity.PmProductCharacteristicsEntity;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 特征主数据
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmProductCharacteristicsService extends ICrudService<PmProductCharacteristicsEntity> {

    /**
     * 特征主数据
     *
     * @param characteristicsVersionsId id
     * @return 特征主数据列表
     */
    List<PmProductCharacteristicsEntity> getByCharacteristicsVersionsId(Long characteristicsVersionsId);

    /**
     * 特征主数据
     *
     * @param versionsid 特征版本外键
     * @param name       特征项
     * @param materialNo 产品编码
     * @param isdelete   是否删除
     * @return 特征主数据
     */
    PmProductCharacteristicsEntity getByVersionsAndName(String versionsid, String name, String materialNo, String isdelete);

    /**
     * 获取特征主数据
     *
     * @return 特征主数据列表
     */
    List<ProductCharacteristicsDTO> getCharacteristicsMaster();
}