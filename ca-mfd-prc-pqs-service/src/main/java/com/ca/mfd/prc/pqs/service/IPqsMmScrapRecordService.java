package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsMmScrapRecordEntity;

import java.util.List;

/**
 *
 * @Description: 压铸废料管理服务
 * @author inkelink
 * @date 2023年10月27日
 * @变更说明 BY inkelink At 2023年10月27日
 */
public interface IPqsMmScrapRecordService extends ICrudService<PqsMmScrapRecordEntity> {

    /**
     * 获取所有压铸废料管理记录
     *
     * @return
     */
    List<PqsMmScrapRecordEntity> getAllDatas();
}