package com.ca.mfd.prc.pps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.communication.dto.BomMaterialUseDto;
import com.ca.mfd.prc.pps.communication.entity.MidMaterialUseEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 物料使用处服务
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
public interface IMidBomMaterialUseService extends ICrudService<MidMaterialUseEntity> {

    /**
     * 根据整车物料号获取物料使用信息
     *
     * @param materialNo
     * @return
     */
    List<BomMaterialUseDto> getBomMaterialUseData(String materialNo,String plantCode,String specifyDate);
}