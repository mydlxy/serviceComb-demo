package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsBodyshopJobDetailsEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 焊装车间执行码详情服务
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
public interface IEpsBodyshopJobDetailsService extends ICrudService<EpsBodyshopJobDetailsEntity> {
    /**
     * 获取所有的数据
     *
     * @return
     */
    List<EpsBodyshopJobDetailsEntity> getAllDatas();
}