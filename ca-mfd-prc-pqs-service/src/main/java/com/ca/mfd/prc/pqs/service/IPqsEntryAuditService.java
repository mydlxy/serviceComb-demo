package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.PqsEntryPageFilter;
import com.ca.mfd.prc.pqs.entity.PqsEntryAuditEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 质检工单-评审工单服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsEntryAuditService extends ICrudService<PqsEntryAuditEntity> {

    /**
     * 获取质检工单-评审工单数据
     *
     * @return
     */
    List<PqsEntryAuditEntity> getAllDatas();

    /**
     * 获取检验工单
     *
     * @param filter
     * @return
     */
    PageData getEntryList(PqsEntryPageFilter filter);
}