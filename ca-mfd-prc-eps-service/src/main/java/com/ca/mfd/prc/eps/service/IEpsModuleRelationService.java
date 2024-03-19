package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.dto.BindingUnitPara;
import com.ca.mfd.prc.eps.dto.GetModuleRelationInfo;
import com.ca.mfd.prc.eps.entity.EpsModuleRelationEntity;

/**
 *
 * @Description: 电池模组关系服务
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IEpsModuleRelationService extends ICrudService<EpsModuleRelationEntity> {

    /**
     * 获取模组关系结构
     *
     * @param barcode
     * @return
     */
    GetModuleRelationInfo getModuleRelation(String barcode);

    /**
     * 绑定小单元
     *
     * @param para
     * @return
     */
    void bindingUnit(BindingUnitPara para);

    /**
     * 解除小单元关系绑定
     *
     * @param barcode
     * @return
     */
    void deleteUnit(String barcode);

    /**
     * 绑定元素
     *
     * @param para
     * @return
     */
    void bindingCell(EpsModuleRelationEntity para);

    /**
     * 删除关系元素
     *
     * @param barcode
     * @return
     */
    void deleteCell(String barcode);
}