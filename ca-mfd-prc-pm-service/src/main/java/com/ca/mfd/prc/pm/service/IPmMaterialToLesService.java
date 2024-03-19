package com.ca.mfd.prc.pm.service;

import cn.hutool.core.io.ValidateObjectInputStream;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.entity.PmMaterialToLesEntity;

/**
 * @author inkelink
 * @Description: LES拉取工位物料清单服务
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
public interface IPmMaterialToLesService extends ICrudService<PmMaterialToLesEntity> {

    /**
     * @param productCode 物料号
     * @param sigtrue     加密串
     */
    PmMaterialToLesEntity  savePmMaterialToLes(String productCode, String sigtrue);

    /**
     * 获取LES拉取工位物料清单
     *
     * @param sigtrue
     * @return LES拉取工位物料清单
     */
    PmMaterialToLesEntity getPmMaterialToLesBySigtrue(String sigtrue);


    void  createPmMaterialToLes();

    /**
     * 更新发送状态
     *
     * @param sigtrue 令牌
     */
    void updatePmMaterialToLesStatus(String sigtrue);

    /**
     *
     * @param id
     */
    void getPmMaterialBak(Long id);
}