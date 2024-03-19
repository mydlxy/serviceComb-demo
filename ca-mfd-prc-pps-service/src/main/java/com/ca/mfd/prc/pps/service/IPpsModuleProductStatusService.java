package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsModuleProductStatusEntity;

import java.util.List;

/**
 *
 * @Description: 模组相关产品状态服务
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IPpsModuleProductStatusService extends ICrudService<PpsModuleProductStatusEntity> {

    /**
     * 获取
     *
     * @param barcodes
     * @return
     */
    List<PpsModuleProductStatusEntity> getListByBarCodes(List<String> barcodes);
}