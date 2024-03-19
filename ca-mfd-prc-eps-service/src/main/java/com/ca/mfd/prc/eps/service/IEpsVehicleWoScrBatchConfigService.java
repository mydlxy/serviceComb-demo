package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoScrBatchConfigEntity;

/**
 * 批次件自动追溯配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
public interface IEpsVehicleWoScrBatchConfigService extends ICrudService<EpsVehicleWoScrBatchConfigEntity> {
    /**
     * 工艺某个追溯工艺的自动完成配置
     *
     * @param pmWoCode
     * @param workStationCode
     * @return
     */
    EpsVehicleWoScrBatchConfigEntity getScrBatchConfig(String pmWoCode, String workStationCode);

    /**
     * 获取批次追溯条码
     *
     * @param pmWoCode
     * @param workStationCode
     * @return
     */
    String getScrBarcode(String pmWoCode, String workStationCode);

    /**
     * 自动维护配置
     *
     * @param pmWoCode
     * @param workStationCode
     * @param barcode
     */
    void autoScrBatchConfig(String pmWoCode, String workStationCode, String barcode);

    /**
     * 更新数量
     *
     * @param count
     * @param id
     */
    void updateCount(Integer count, Long id);
}