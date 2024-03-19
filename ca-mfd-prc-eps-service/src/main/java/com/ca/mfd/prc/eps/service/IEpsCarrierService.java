package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.dto.CarrierMaterialsResponse;
import com.ca.mfd.prc.eps.entity.EpsCarrierEntity;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsEntryReportPartsEntity;

/**
 * @author inkelink
 * @Description: 载具信息服务
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IEpsCarrierService extends ICrudService<EpsCarrierEntity> {

    /**
     * 绑定载具
     *
     * @param ppsEntryReportPartsInfo 绑定载具
     */
    void bindCarrier(PpsEntryReportPartsEntity ppsEntryReportPartsInfo);

    /**
     * 移除载具上面的物料
     *
     * @param entryReportNo 报工单号
     */
    void removeCarrierMaterial(String entryReportNo);

    /**
     * 使用载具
     *
     * @param carrierBarcode 载具条码
     */
    void emptyCarrier(String carrierBarcode);

    /**
     * 获取载具上面的货物信息
     *
     * @param carrierCode 载具条码
     * @return 载具上面货物信息
     */
    CarrierMaterialsResponse getCarrierMaterials(String carrierCode);

    /**
     * 使用载具（解绑单个货物）
     *
     * @param entryReportNo 报工单号
     */
    void useCarrierByEntryReportNo(String entryReportNo);

    /**
     * 查询载具上面的物料条码
     *
     * @param barcode 载具条码
     * @return 物料条码
     */
    String getTorrMaterialBarcode(String barcode);

    /**
     * 根据条码及物料号 查询是否存在数据
     *
     * @param carrierBarcode
     * @return
     */
    Boolean getCheckRepeat(String carrierBarcode);
}