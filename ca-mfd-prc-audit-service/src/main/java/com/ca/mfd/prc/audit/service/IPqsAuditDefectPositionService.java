package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.audit.dto.DefectShowInfo;
import com.ca.mfd.prc.audit.entity.PqsAuditDefectPositionEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: AUDIT缺陷位置代码服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsAuditDefectPositionService extends ICrudService<PqsAuditDefectPositionEntity> {
    /**
     * 获取所有的位置信息
     *
     * @return
     */
    List<PqsAuditDefectPositionEntity> getAllDatas();

    /**
     * 获取位置代码配置
     *
     * @param info
     * @return
     */
    List<DefectShowInfo> getPositionShowList(DefectFilterlParaInfo info);
}