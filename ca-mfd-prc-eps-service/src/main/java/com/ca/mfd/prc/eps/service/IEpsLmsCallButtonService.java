package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsLmsCallButtonEntity;

import java.util.List;

/**
 *
 * @Description: 物流拉动按钮配置服务
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
public interface IEpsLmsCallButtonService extends ICrudService<EpsLmsCallButtonEntity> {
    /**
     * 获取所有的数据
     *
     * @return List<EpsLmsCallButtonEntity>
     */
    List<EpsLmsCallButtonEntity> getAllDatas();
}