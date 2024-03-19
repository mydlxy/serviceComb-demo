package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.dto.BodyVehicleDTO;
import com.ca.mfd.prc.pps.dto.EntryStatusPara;
import com.ca.mfd.prc.pps.dto.OrderEntryInfo;
import com.ca.mfd.prc.pps.dto.RestEntryQueuePara;
import com.ca.mfd.prc.pps.dto.RestShopEntryQueuePara;
import com.ca.mfd.prc.pps.dto.ShopPlanMonitorInfo;
import com.ca.mfd.prc.pps.entity.PpsEntryConfigEntity;
import com.ca.mfd.prc.pps.entity.PpsEntryEntity;

import java.util.Date;
import java.util.List;

/**
 * 工单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPpsEntryService extends ICrudService<PpsEntryEntity> {

    /**
     * 设置工单状态
     *
     * */
    void setStatus(EntryStatusPara para);

    /**
     * 根据订单编号获取实体
     *
     * @param orderId   订单编号
     * @return 实体
     */
    PpsEntryEntity getFirstByOrderShopCode(Long orderId,String shopCode);
    /**
     * 根据订单编号&工单类型 获取实体
     *
     * @param orderNo   订单编号
     * @param entryType 工单类型
     * @return 实体
     */
    List<PpsEntryEntity> getPpsEntrysByOrderNo(String orderNo, Integer entryType);

    /**
     * 根据订单编号获取实体
     *
     * @param orderId   订单编号
     * @return 实体
     */
    PpsEntryEntity getFirstByOrderId(Long orderId);

    /**
     * 创建分线工单
     *
     * @param ppsEntryInfo
     * @param ppsEntryConfiginfo
     */
    void createBranchEntry(PpsEntryEntity ppsEntryInfo, PpsEntryConfigEntity ppsEntryConfiginfo);

    /**
     * 获取已下发未打印焊装上上线列表
     *
     * @return 获取一个列表
     */
    List<BodyVehicleDTO> getNoPrintTpscode();

    /**
     * 获取已下发已打印焊装上线列表
     *
     * @return 获取一个列表
     */
    List<BodyVehicleDTO> getPrintTpscode();

    /**
     * 读取已经下发队列（需要报废）
     *
     * @return 获取一个列表
     */
    List<BodyVehicleDTO> getDownTpsCode();


    /**
     * 设置TPS码为已打印
     *
     * @param tpsCode  参数tps编码
     * @param shopCode 参数车间编码
     */
    void setPrintTpsCode(String tpsCode, String shopCode);

    /**
     * 设置车辆为上线
     *
     * @param tpsCode  参数tps编码
     * @param shopCode 车间code
     */
    void setBodyEntryOnline(String tpsCode, String shopCode);

    /**
     * 重新生成工艺
     *
     * @param entryId entryId
     */
    void resetWo(Long entryId);

    /**
     * 保存工单状态
     *
     * @param objectNo
     * @param aviCode
     * @param passDt
     */
    void changeEntryStatus(String objectNo, String aviCode, Date passDt);

    /**
     * 获取数据
     *
     * @param sn
     * @param entryType
     * @param shopCode
     * @return 获取一个列表
     */
    PpsEntryEntity getFirstEntryTypeShopCodeSn(String sn, Integer entryType, String shopCode);

    /**
     * 重置工单队列
     *
     * @param para 参数实体
     */
    void restEntryQueue(RestEntryQueuePara para);

    /**
     * 更改预计上线时间
     *
     * @param estimatedStartDt 时间
     * @param ids              更新的ID集合
     */
    void changeEstimatedStartDt(Date estimatedStartDt, List<Long> ids);

    /**
     * 更改预计下线时间
     *
     * @param estimatedEndDt 时间
     * @param ids            更新的ID集合
     */
    void changeEstimatedEndDt(Date estimatedEndDt, List<Long> ids);

    /**
     * 根据订单号跟新状态
     *
     * @param orderNo 订单编号
     * @param status  状态
     */
    void updateStatusByOrderNo(String orderNo, Integer status);

    /**
     * 根据订单编号&工单类型 获取实体
     *
     * @param orderNo   订单编号
     * @param entryType 工单类型
     * @return 实体
     */
    PpsEntryEntity getPpsEntryEntityByOrderNo(String orderNo, Integer entryType);

    /**
     * 车间工单分线监控查询
     *
     * @param shopPlanMonitorinfo 查询参数
     */
    void getShopPlanMonitorInfos(ShopPlanMonitorInfo shopPlanMonitorinfo);

    /**
     * 获取车间订单列表(整车)
     *
     * @param conditions 查询条件
     * @param sorts      排序条件
     * @param page       分页数据
     */
    void getShopOrders(List<ConditionDto> conditions, List<SortDto> sorts, PageData<OrderEntryInfo> page);

    /**
     * 获取订阅码工单(分线工单)已下发的工单列表
     *
     * @param subCode
     * @param take
     * @param model
     * @return
     */
    List<OrderEntryInfo> getBranchingEntryIssued(String subCode, String model, int take);

    /**
     * 获取订阅码工单(分线工单)未下发的工单列表
     *
     * @param subCode
     * @param take
     * @param model
     * @return
     */
    List<OrderEntryInfo> getBranchingEntryUnissued(String subCode, String model, int take);

    /**
     * 获取车间工单
     *
     * @param shopCode
     * @param top
     * @param model
     * @return
     */
    List<OrderEntryInfo> getShopEntrys(String shopCode, int top, String model);

    /**
     * 获取车间工单
     *
     * @param shopCode
     * @param top
     * @param model
     * @param lineCode
     * @return
     */
    List<OrderEntryInfo> getShopEntrys(String shopCode, int top, String model,String lineCode);

    /**
     * 根据tps 获取工单列表
     *
     * @param tpsCode 车辆编码
     * @return 工单列表
     */
    List<PpsEntryEntity> getPpsEntryBySn(String tpsCode);

    /**
     * 根据sn 获取工单首条
     *
     * @param sn
     * @return 工单
     */
    PpsEntryEntity getFirstBySn(String sn);
}