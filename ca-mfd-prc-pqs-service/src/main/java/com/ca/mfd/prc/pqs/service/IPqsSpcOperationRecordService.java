package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsSpcOperationRecordEntity;

import java.util.List;

/**
 *
 * @Description: SPC模块_操作记录表服务
 * @author inkelink
 * @date 2023年11月30日
 * @变更说明 BY inkelink At 2023年11月30日
 */
public interface IPqsSpcOperationRecordService extends ICrudService<PqsSpcOperationRecordEntity> {

    PqsSpcOperationRecordEntity queryByFileAndOperationMd5(Long id, String operationMd5);

    List<PqsSpcOperationRecordEntity> getResultByFileId(Long id);

    PqsSpcOperationRecordEntity queryByFileAndResultName(Long id, String resultName);
}