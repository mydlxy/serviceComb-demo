package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsFunctionEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 质检功能配置服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsFunctionService extends ICrudService<PqsFunctionEntity> {

    /**
     * 获取所有质检功能配置记录
     *
     * @return
     */
    List<PqsFunctionEntity> getAllDatas();
}