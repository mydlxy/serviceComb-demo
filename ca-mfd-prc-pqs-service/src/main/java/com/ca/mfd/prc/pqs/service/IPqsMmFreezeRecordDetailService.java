package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsMmFreezeRecordDetailEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 物料质量冻结明细服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsMmFreezeRecordDetailService extends ICrudService<PqsMmFreezeRecordDetailEntity> {

    /**
     * 获取所有物料质量冻结明细记录
     *
     * @return
     */
    List<PqsMmFreezeRecordDetailEntity> getAllDatas();
}