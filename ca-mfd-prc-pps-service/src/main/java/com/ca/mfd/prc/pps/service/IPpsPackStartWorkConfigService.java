package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsPackStartWorkConfigEntity;

import java.util.List;

/**
 *
 * @Description: 电池开工配置服务
 * @author inkelink
 * @date 2024年01月22日
 * @变更说明 BY inkelink At 2024年01月22日
 */
public interface IPpsPackStartWorkConfigService extends ICrudService<PpsPackStartWorkConfigEntity> {
    /**
     * 获取所有的数据
     *
     * @return List<PpsPackStartWorkConfigEntity>
     */
    List<PpsPackStartWorkConfigEntity> getAllDatas();
}