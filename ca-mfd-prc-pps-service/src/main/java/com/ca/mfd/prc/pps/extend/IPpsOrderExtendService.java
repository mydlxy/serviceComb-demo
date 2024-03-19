package com.ca.mfd.prc.pps.extend;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;

import java.util.List;

/**
 * PpsOrder
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPpsOrderExtendService extends ICrudService<PpsOrderEntity> {


    /**
     * 根据订单号查询列表
     *
     * @param sn 订单编号
     * @return 工单列表
     */
    PpsOrderEntity getPpsOrderBySn(String sn);

    /**
     * 获取
     *
     * @param planNo
     * @return
     */
    Long getCountByPlanNo(String planNo);

    /**
     * 获取
     *
     * @param bomVersions
     * @return
     */
    PpsOrderEntity getByBomVersion(String bomVersions);

    /**
     * 获取
     *
     * @param planNo
     * @return
     */
    PpsOrderEntity getByPlanNo(String planNo);

    /**
     * 根据sn查询实体
     *
     * @param sn sn编码
     * @return 实体
     */
    PpsOrderEntity getPpsOrderBySnOrBarcode(String sn);

    /**
     * 获取订单数量（模糊匹配）
     *
     * @param top  关键字
     * @param code 关键字
     * @return 列表
     */
    List<PpsOrderEntity> getTopOrderByCodeLike(Integer top, String code);

    /**
     * 获取订单信息
     *
     * @param key 关键字
     * @return 订单信息
     */
    PpsOrderEntity getPpsOrderInfoByKeyAvi(String key);

    /**
     * 获取订单信息
     *
     * @param key
     * @return PpsOrderEntity
     */
    PpsOrderEntity getPpsOrderInfoByKey(String key);

    /**
     * 根据车间订单ID 更新状态
     *
     * @param completeQuantity 完成数量
     * @param orderId          车间订单ID
     */
    void updateEntityByCompleteQuantity(Integer completeQuantity, Long orderId);

    /**
     * 根据sn或barcode查询
     *
     * @param codes
     * @return
     */
    List<PpsOrderEntity> getPpsOrderBySnsOrBarcodes(List<String> codes);
}
