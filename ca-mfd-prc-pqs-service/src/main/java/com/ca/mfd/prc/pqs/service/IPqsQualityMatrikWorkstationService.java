package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikWorkstationEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 百格图关联的岗位服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsQualityMatrikWorkstationService extends ICrudService<PqsQualityMatrikWorkstationEntity> {

    /**
     * 获取所有的百格图关联的岗位
     *
     * @return
     */
    List<PqsQualityMatrikWorkstationEntity> getAllDatas();
}