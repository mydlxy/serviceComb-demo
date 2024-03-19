package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsModuleInboxStrategyEntity;

import java.util.List;

/**
 *
 * @Description: 预成组入箱策略服务
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IEpsModuleInboxStrategyService extends ICrudService<EpsModuleInboxStrategyEntity> {

    /**
     * 获取所有的数据
     *
     * @return List<EpsModuleInboxStrategyEntity>
     */
    List<EpsModuleInboxStrategyEntity> getAllDatas();

    /**
     * 启用策略
     *
     * @param id
     * @return
     */
    void enableStrategy(Long id);
}