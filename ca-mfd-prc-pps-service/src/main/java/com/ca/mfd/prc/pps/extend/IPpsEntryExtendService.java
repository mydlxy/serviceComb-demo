package com.ca.mfd.prc.pps.extend;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsEntryEntity;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;

import java.util.List;

/**
 * 工单扩展
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPpsEntryExtendService extends ICrudService<PpsEntryEntity> {


    /**
     * 根据订单号查询列表
     *
     * @param orderNo 订单编号
     * @return 工单列表
     */
    List<PpsEntryEntity> getEntityListByOrderNo(String orderNo);

    /**
     * 批量更新工单
     *
     * @param ids     id集合
     * @param sn      唯一码
     * @param orderId 订单ID
     * @param orderNo 订单号
     * @param planNo  计划编码
     */
    void updateEntityByIds(List<Long> ids, String sn, Long orderId, String orderNo, String planNo);
}
