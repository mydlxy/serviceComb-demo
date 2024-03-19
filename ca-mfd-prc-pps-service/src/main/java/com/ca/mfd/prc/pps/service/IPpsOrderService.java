package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.pps.dto.QcyjyVehicleInfoDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pps.dto.IssueFeatureInfo;
import com.ca.mfd.prc.pps.dto.ResetVehiclePara;
import com.ca.mfd.prc.pps.dto.UpdateOrderBomPara;
import com.ca.mfd.prc.pps.dto.VehicleChangeTpsInfo;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.extend.IPpsOrderExtendService;

import java.io.Serializable;
import java.util.List;

/**
 * 订单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPpsOrderService extends IPpsOrderExtendService {
    /**
     * 校验特征10
     *
     * @param characteristics
     * @return
     * */
    Boolean checkCharacteristic10(List<String> characteristics);

    /**
     * 发送AS保留車
     *
     * @param ids
     * @param isFreeze
     */
    void sendAsKeepCarMessage(List<Long> ids, Boolean isFreeze);

    /**
     * 根据订单状态获取订单
     *
     * @param barcode
     * @param orderCategory
     * @return 订单
     */
    PpsOrderEntity getFirstByBarCodeOrderCategory(String barcode, String orderCategory);

    /**
     * 获取整车信息
     *
     * @param sn 车身识别码
     * @return PpsOrderEntity
     */
    @Override
    PpsOrderEntity getPpsOrderBySn(String sn);

    /**
     * 获取信息
     *
     * @param snCodes 唯一码
     * @return 车辆订单列表
     */
    List<PpsOrderEntity> getListBySnCodes(List<String> snCodes);

    /**
     * 获取信息
     *
     * @param barcodes 车身识别码
     * @return PpsOrderEntity
     */
    List<PpsOrderEntity> getListByBarcodes(List<String> barcodes);

    /**
     * 获取计划的bom数据
     *
     * @param ppsOrderNo 计划号
     * @return BOM数据集合
     */
    List<PmProductBomEntity> getOrderBom(String ppsOrderNo);

    /**
     * 根据订单号获取BOM信息
     *
     * @param orderId
     * @return List<PmProductBomEntity>
     */
    List<PmProductBomEntity> getOrderBomByOrderId(Long orderId);

    /**
     * 根据id获取特征信息
     *
     * @param orderId
     * @return 特征详细数据
     */
    List<PmProductCharacteristicsEntity> getOrderCharacteristicByOrderId(Long orderId);

    /**
     * 根据订单号获取特征信息
     *
     * @param orderNo 订单号
     * @return 特征详细数据
     */
    List<PmProductCharacteristicsEntity> getOrderCharacteristicByOrderNo(String orderNo);


    /**
     * 根据订单号获取订单实体
     *
     * @param orderNo 订单号
     * @return 订单实体
     */
    PpsOrderEntity getPpsOrderInfoByorderNo(String orderNo);

    /**
     * 根据订单号更新状态
     *
     * @param orderNo     订单号
     * @param orderStatus 状态
     */
    void updateOrderStatusByOrderNo(String orderNo, Integer orderStatus);


    /**
     * 订单匹配切换
     *
     * @param vehicleChangeTpsInfo 参数列表
     */
    void vehicleChangeTps(VehicleChangeTpsInfo vehicleChangeTpsInfo);

    /**
     * 订单匹配切换验证
     *
     * @param vehicleChangeTpsInfo 参数列表
     * @return 验证结果
     */
    String validVehicleChangeTps(VehicleChangeTpsInfo vehicleChangeTpsInfo);


    /**
     * 设置工艺路径
     *
     * @param orderIds  生产订单ID
     * @param processId 工艺路径ID
     */
    void setProcess(List<Long> orderIds, Long processId);

    /**
     * 删除备件订单
     *
     * @param ids
     */
    void deleteStamping(List<Long> ids);

    /**
     * 取消冻结整车生成订单
     *
     * @param ppsOrderIds 订单集合
     * @param remark      备注
     */
    void unFreeze(List<Long> ppsOrderIds, String remark);

    /**
     * 取消冻结整车生成订单
     *
     * @param ppsOrderIds 订单集合
     */
    void unFreeze(List<Long> ppsOrderIds);

    /**
     * 冻结整车生成订单
     *
     * @param ppsOrderIds 订单集合
     * @param remark      备注
     */
    void freeze(List<Long> ppsOrderIds, String remark);

    /**
     * 冻结整车生成订单
     *
     * @param ppsOrderIds 订单集合
     */
    void freeze(List<Long> ppsOrderIds);


    /**
     * 重置车辆
     *
     * @param para
     */
    void resetVehicle(ResetVehiclePara para);

    /**
     * 根据产品唯一码冻结/解冻 订单
     *
     * @param sn       产品唯一码
     * @param isFreeze 冻结/解冻
     * @param remark   备注
     */

    void operateIsFreezeById(String sn, Boolean isFreeze, String remark);

    /**
     * 生成顺序号（订单）
     *
     * @param productNo
     * @param shopCode
     */
    void generateSequenceNoForOrder(String productNo, String shopCode);

}