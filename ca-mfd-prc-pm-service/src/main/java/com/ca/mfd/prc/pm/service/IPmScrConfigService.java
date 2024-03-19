package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.entity.PmScrConfigEntity;

import java.util.List;

/**
 *
 * @Description: 拧紧指示配置服务
 * @author inkelink
 * @date 2024年01月24日
 * @变更说明 BY inkelink At 2024年01月24日
 */
public interface IPmScrConfigService extends ICrudService<PmScrConfigEntity> {
    /**
     * 获取所有的数据
     *
     * @return List<PmScrConfigEntity>
     */
    List<PmScrConfigEntity> getAllDatas();
}