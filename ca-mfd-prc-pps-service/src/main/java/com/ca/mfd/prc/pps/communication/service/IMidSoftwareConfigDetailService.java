package com.ca.mfd.prc.pps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.communication.entity.MidSoftwareConfigDetailEntity;

import java.util.List;

/**
 *
 * @Description: 配置字详情服务
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
public interface IMidSoftwareConfigDetailService extends ICrudService<MidSoftwareConfigDetailEntity> {

    /**
     * 获取所有数据
     * @return
     */
    List<MidSoftwareConfigDetailEntity> getAllDatas();
}