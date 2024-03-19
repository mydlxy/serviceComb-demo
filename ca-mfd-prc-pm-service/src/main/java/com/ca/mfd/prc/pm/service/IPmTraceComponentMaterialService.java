package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.entity.PmTraceComponentMaterialEntity;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 追溯组件物料绑定
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmTraceComponentMaterialService extends ICrudService<PmTraceComponentMaterialEntity> {

    /**
     * 获取所有的数据
     *
     * @return List<PmTraceComponentMaterialEntity>
     */
    List<PmTraceComponentMaterialEntity> getAllDatas();

    /**
     * 获取所有数据
     *
     * @param materialNo
     * @return 数据列表
     */
    PmTraceComponentMaterialEntity getFirstByMaterialNo(String materialNo);
}