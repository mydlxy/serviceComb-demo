package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsEntryReportPartsEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 报工单-零部件服务
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IPpsEntryReportPartsService extends ICrudService<PpsEntryReportPartsEntity> {

    /**
     * 获取
     *
     * @param entryNo
     * @return
     */
    List<PpsEntryReportPartsEntity> getByEntryNo(String entryNo);

    /**
     * 获取
     *
     * @param entryNo
     * @return
     */
    PpsEntryReportPartsEntity getFirstByEntryNo(String entryNo);

    /**
     * 获取
     *
     * @param barCode
     * @param orderCategory
     * @return
     */
    PpsEntryReportPartsEntity getByBarcodeAndCatory(String barCode, Integer orderCategory);

    /**
     * 获取
     *
     * @param barCode
     * @return
     */
    PpsEntryReportPartsEntity getFirstByBarcode(String barCode);

    /**
     * 根据分类获取前20条
     *
     * @param orderCategory
     * @return 列表
     */
    List<PpsEntryReportPartsEntity> getTopDataByOrderCategory(int orderCategory);

    /**
     * 更新状态
     *
     * @param id 主键
     */
    void updateIsPassAviById(Long id);

    /**
     *
     * @param orderCategory
     * @param rprtNos
     * @return
     */
    List<PpsEntryReportPartsEntity> getRecordByOrderCategory(int orderCategory, List<String> rprtNos);
}