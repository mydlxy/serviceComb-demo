package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.CreateProcessEntryInfo;
import com.ca.mfd.prc.pqs.dto.GetPartsProcessInfo;
import com.ca.mfd.prc.pqs.dto.PqsEntryPageFilter;
import com.ca.mfd.prc.pqs.dto.PqsEntryProcessDto;
import com.ca.mfd.prc.pqs.dto.SaveEntryProcessInfo;
import com.ca.mfd.prc.pqs.entity.PqsEntryProcessEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 过程检验服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsEntryProcessService extends ICrudService<PqsEntryProcessEntity> {

    /**
     * 获取所有的过程检验信息
     *
     * @return
     */
    List<PqsEntryProcessEntity> getAllDatas();

    /**
     * 根据工单号或条码获取零件信息
     *
     * @param key
     * @return
     */
    GetPartsProcessInfo getPartProcessInfo(String key);

    /**
     * 获取工单列表
     *
     * @param filter
     * @return
     */
    PageData<PqsEntryProcessDto> getEntryList(PqsEntryPageFilter filter);

    /**
     * 初始化工单
     *
     * @param inspectionNo
     * @return
     */
    PqsEntryProcessDto initialization(String inspectionNo);

    /**
     * 创建工单
     *
     * @param info
     * @return
     */
    String createProcessEntry(CreateProcessEntryInfo info);

    /**
     * 保存工单结果
     *
     * @param info
     */
    void saveEntryProcessResult(SaveEntryProcessInfo info);

    /**
     * 删除工单
     *
     * @param inspectionNo
     */
    void deleteEntry(String inspectionNo);
}