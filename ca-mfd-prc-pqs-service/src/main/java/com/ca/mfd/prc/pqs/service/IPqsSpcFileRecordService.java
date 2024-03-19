package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsSpcFileRecordEntity;

/**
 *
 * @Description: 服务
 * @author inkelink
 * @date 2023年11月30日
 * @变更说明 BY inkelink At 2023年11月30日
 */
public interface IPqsSpcFileRecordService extends ICrudService<PqsSpcFileRecordEntity> {

    PqsSpcFileRecordEntity getFileRecordByMd5(String fileMd5);

    PqsSpcFileRecordEntity getFileRecordById(Long id);

    PqsSpcFileRecordEntity getFileRecordByMd5AndName(String fileMd5, String fileName);
}