package com.ca.mfd.prc.pps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.communication.entity.MidSoftwareConfigVersionEntity;

import java.util.List;

/**
 *
 * @Description: 配置字版本服务
 * @author inkelink
 * @date 2023年11月27日
 * @变更说明 BY inkelink At 2023年11月27日
 */
public interface IMidSoftwareConfigVersionService extends ICrudService<MidSoftwareConfigVersionEntity> {

    /**
     * 获取全部数据
     * @return
     */
    List<MidSoftwareConfigVersionEntity> getAllDatas();
}