package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.dto.CopyStrategyPara;
import com.ca.mfd.prc.pps.entity.PpsModuleSplitStrategyEntity;

import java.util.List;

/**
 *
 * @Description: 电池结构配置服务
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IPpsModuleSplitStrategyService extends ICrudService<PpsModuleSplitStrategyEntity> {
    /**
     * 获取所有的数据
     *
     * @return List<PpsModuleSplitStrategyEntity>
     */
    List<PpsModuleSplitStrategyEntity> getAllDatas();

    /**
     * 启用策略
     *
     * @param id
     * @return
     */
    void enableStrategy(Long id);

}