package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.PqsEntryCheckItemDto;
import com.ca.mfd.prc.pqs.entity.PqsEntryCheckItemEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 工单检验项服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsEntryCheckItemService extends ICrudService<PqsEntryCheckItemEntity> {
    /**
     * 获取所有的工单检验项信息
     *
     * @return
     */
    List<PqsEntryCheckItemEntity> getAllDatas();

    /**
     * 重置检验项目 （删除所有检验项）
     *
     * @param inspectionNo 质检单号
     * @return 空检验项列表
     */
    List<PqsEntryCheckItemDto> restCheckItem(String inspectionNo);

    /**
     * 删除检验项
     *
     * @param id 检验项ID
     */
    void deleteCheckItem(String id);

    /**
     * 初始化检验项目
     *
     * @param inspectionNo 检验工单号
     * @param templateId   模板ID
     * @return 是否需要复检，检验项列表
     */
    List<PqsEntryCheckItemDto> initCheckItem(String inspectionNo, Long templateId);

    /**
     * 获取检验项列表
     *
     * @param inspectionNo 检验工单号
     * @return 检验项列表
     */
    List<PqsEntryCheckItemDto> getCheckItem(String inspectionNo);

    /**
     * 保存检验结果
     *
     * @param checkItem
     * @return 检验项列表
     */
    List<PqsEntryCheckItemDto> saveCheckItem(List<PqsEntryCheckItemDto> checkItem);
}