package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.communication.entity.MidMaterialMasterEntity;
import com.ca.mfd.prc.pm.entity.PmProductMaterialMasterEntity;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 物料主数据
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmProductMaterialMasterService extends ICrudService<PmProductMaterialMasterEntity> {
    /**
     * 获取所有的数据
     *
     * @return List<PmProductMaterialMasterEntity>
     */
    List<PmProductMaterialMasterEntity> getAllDatas();

    /**
     * 把BOM数据同步过来
     * @param datas
     */
    void syncFromBom(List<MidMaterialMasterEntity> datas);
}