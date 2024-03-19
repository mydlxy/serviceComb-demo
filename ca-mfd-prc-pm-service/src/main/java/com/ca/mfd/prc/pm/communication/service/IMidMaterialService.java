package com.ca.mfd.prc.pm.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.communication.dto.MidBomInfo;
import com.ca.mfd.prc.pm.communication.entity.MidMaterialEntity;

import java.util.List;

/**
 *
 * @Description: 单车bom中间表服务
 * @author inkelink
 * @date 2023年12月08日
 * @变更说明 BY inkelink At 2023年12月08日
 */
public interface IMidMaterialService extends ICrudService<MidMaterialEntity> {

    /**
     * 获取物料主数据
     * @param plantCode 工厂代码
     * @param materialNo 整车物料号
     * @param specifyDate 计划上线日期
     */
    String getBomVersions(String plantCode,String materialNo,String specifyDate);

    /**
     * 获取单车bom
     * @param plantCode
     * @param materialNo
     * @param specifyDate
     * @return
     */
    List<MidBomInfo> getSingleBom(String plantCode, String materialNo, String specifyDate);


}