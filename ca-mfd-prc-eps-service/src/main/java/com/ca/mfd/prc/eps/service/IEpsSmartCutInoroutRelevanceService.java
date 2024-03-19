package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsSmartCutInoroutRelevanceEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: PACK切入或切出关联服务
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
public interface IEpsSmartCutInoroutRelevanceService extends ICrudService<EpsSmartCutInoroutRelevanceEntity> {
    /**
     * 获取所有的数据
     *
     * @return
     */
    List<EpsSmartCutInoroutRelevanceEntity> getAllDatas();

    /**
     * 获取工位列表
     *
     * @return
     */
    List<ComboInfoDTO> getCutInWorkstations();

    /**
     * 获取工位最近的下线站点
     *
     * @param workstationCode
     * @return
     */
    String getNextAviCode(String workstationCode);

}