package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.entity.PmProductBomEntity;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: BOM
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmProductBomService extends ICrudService<PmProductBomEntity> {
    /**
     * 更具版本号获取数据
     *
     * @param bomVersionId BOM版本id
     * @return 返回Bom列表
     */
    List<PmProductBomEntity> getByBomVersionId(Long bomVersionId);
}