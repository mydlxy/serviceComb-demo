package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.audit.dto.DefectShowInfo;
import com.ca.mfd.prc.audit.entity.PqsAuditDefectComponentEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: AUDIT组件代码服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsAuditDefectComponentService extends ICrudService<PqsAuditDefectComponentEntity> {
    /**
     * 获取所有的组件信息
     *
     * @return
     */
    List<PqsAuditDefectComponentEntity> getAllDatas();

    /**
     * 获取组件代码配置
     *
     * @param info
     * @return
     */
    List<DefectShowInfo> getComponentShowList(DefectFilterlParaInfo info);
}