package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsPlanLockConfigEntity;

import java.util.List;

/**
 * @author eric.zhou
 * @Description: 生产计划锁定配置服务
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
public interface IPpsPlanLockConfigService extends ICrudService<PpsPlanLockConfigEntity> {
    /**
     * 获取所有的数据
     *
     * @return List<PpsPlanLockConfigEntity>
     */
    List<PpsPlanLockConfigEntity> getAllDatas();
}