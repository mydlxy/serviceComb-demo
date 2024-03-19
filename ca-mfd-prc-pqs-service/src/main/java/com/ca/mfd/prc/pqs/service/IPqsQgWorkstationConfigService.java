package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsQgWorkstationConfigEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 质量门功能配置服务
 * @date 2023年09月06日
 * @变更说明 BY inkelink At 2023年09月06日
 */
public interface IPqsQgWorkstationConfigService extends ICrudService<PqsQgWorkstationConfigEntity> {

    /**
     * 获取所有质量门功能配置记录
     *
     * @return
     */
    List<PqsQgWorkstationConfigEntity> getAllDatas();
}