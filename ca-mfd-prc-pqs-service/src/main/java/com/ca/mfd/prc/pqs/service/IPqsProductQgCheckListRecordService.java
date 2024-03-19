package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsProductQgCheckListRecordEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 产品-QG必检项服务
 * @date 2023年09月07日
 * @变更说明 BY inkelink At 2023年09月07日
 */
public interface IPqsProductQgCheckListRecordService extends ICrudService<PqsProductQgCheckListRecordEntity> {

    /**
     * 获取所有产品-QG必检项信息
     *
     * @return
     */
    List<PqsProductQgCheckListRecordEntity> getAllDatas();
}