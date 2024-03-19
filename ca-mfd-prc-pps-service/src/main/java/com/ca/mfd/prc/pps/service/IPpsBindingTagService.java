package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsBindingTagEntity;

/**
 * @author eric.zhou
 * @Description: 吊牌绑定管理服务
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
public interface IPpsBindingTagService extends ICrudService<PpsBindingTagEntity> {
    /**
     * 绑定吊牌
     *
     * @param tagCode
     * @return
     */
    PpsBindingTagEntity getFirstByCode(String tagCode);

    /**
     * 绑定吊牌
     *
     * @param tagNo
     * @param vin
     * @param bindingAviCode
     * @return
     */
    void bindingTag(String tagNo, String vin, String bindingAviCode,Integer bindingMedium);

    /**
     * 解绑吊牌
     *
     * @param tagNo
     */
    void unbindTag(String tagNo);

}