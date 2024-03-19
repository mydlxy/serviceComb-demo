package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsMmFreezeRecordEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 物料质量冻结记录服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsMmFreezeRecordService extends ICrudService<PqsMmFreezeRecordEntity> {

    /**
     * 获取所有物料质量冻结记录
     *
     * @return
     */
    List<PqsMmFreezeRecordEntity> getAllDatas();
}