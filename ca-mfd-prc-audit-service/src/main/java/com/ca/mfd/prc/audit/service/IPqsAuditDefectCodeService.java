package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.dto.BatchAddCodeInfo;
import com.ca.mfd.prc.audit.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.audit.dto.DefectShowInfo;
import com.ca.mfd.prc.audit.entity.PqsAuditDefectCodeEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: AUDIT缺陷代码服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsAuditDefectCodeService extends ICrudService<PqsAuditDefectCodeEntity> {
    /**
     * 获取所有的缺陷信息
     *
     * @return
     */
    List<PqsAuditDefectCodeEntity> getAllDatas();

    /**
     * 批量添加缺陷分类
     *
     * @param list
     */
    void batchAddCode(List<BatchAddCodeInfo> list);

    /**
     * 获取缺陷分类数据展示
     *
     * @param info
     * @return
     */
    List<DefectShowInfo> getCodeShowList(DefectFilterlParaInfo info);
}