package com.ca.mfd.prc.core.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.communication.entity.MidPmCountryEntity;

import java.util.List;

/**
 *
 * @Description: 国家代码中间表服务
 * @author inkelink
 * @date 2023年10月16日
 * @变更说明 BY inkelink At 2023年10月16日
 */
public interface IMidPmCountryService extends ICrudService<MidPmCountryEntity> {

    /**
     * 获取全部数据
     * @return
     */
    List<MidPmCountryEntity> getAllDatas();
}