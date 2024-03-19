package com.ca.mfd.prc.eps.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsCarrierLogEntity;

import java.util.List;

/**
 *
 * @Description: 载具日志服务
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IEpsCarrierLogService extends ICrudService<EpsCarrierLogEntity> {

    /**
     * 获取
     *
     * @param batchNumber
     * @param carrierCode
     * @return
     */
    List<EpsCarrierLogEntity> getByBatchNumberCarriCode(String batchNumber, String carrierCode);

    /**
     * 获取
     *
     * @param entryReportNo
     * @return
     */
    EpsCarrierLogEntity getFirstByEntryReportNo(String entryReportNo);

    /**
     * 获取
     *
     * @param batchNumber
     * @param carrierCode
     * @return
     */
    EpsCarrierLogEntity getFirstByBatchNumberCarriCode(String batchNumber, String carrierCode);

    /**
     * 获取
     *
     * @param entryReportNo
     * @return
     */
    String getCarrierBarcodeByEntryReportNo(String entryReportNo);

    /**
     * 获取
     *
     * @param batchNumber
     * @param carrierCode
     * @param entryReportNo
     * @return
     */
    Long getCountByBatchNumberCarriCode(String batchNumber, String carrierCode, String entryReportNo);

}