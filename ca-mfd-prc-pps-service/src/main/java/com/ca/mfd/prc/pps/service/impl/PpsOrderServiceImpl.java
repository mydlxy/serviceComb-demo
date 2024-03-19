package com.ca.mfd.prc.pps.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.constant.BodyShopCode;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQConstants;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.SysQueueNoteEntity;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.provider.RabbitMqSysQueueNoteProvider;
import com.ca.mfd.prc.pps.communication.dto.AsKeepCarDto;
import com.ca.mfd.prc.pps.dto.*;
import com.ca.mfd.prc.pps.entity.PpsEntryEntity;
import com.ca.mfd.prc.pps.entity.PpsOrderChangeLogEntity;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.entity.PpsResetVehicleLogEntity;
import com.ca.mfd.prc.pps.enums.OrderCategoryEnum;
import com.ca.mfd.prc.pps.enums.OrderStatusEnum;
import com.ca.mfd.prc.pps.extend.IPpsEntryExtendService;
import com.ca.mfd.prc.pps.extend.impl.PpsOrderExtendServiceImpl;
import com.ca.mfd.prc.pps.mapper.IPpsOrderMapper;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysSequenceNumberProvider;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.pps.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmIssueCharacteristicsConfigProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmOrgProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductBomVersionsProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductCharacteristicsVersionsProvider;
import com.ca.mfd.prc.pps.service.IPpsOrderChangeLogService;
import com.ca.mfd.prc.pps.service.IPpsOrderService;
import com.ca.mfd.prc.pps.service.IPpsPlanService;
import com.ca.mfd.prc.pps.service.IPpsResetVehicleLogService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PpsOrderServiceImpl extends PpsOrderExtendServiceImpl implements IPpsOrderService {

    private static final String FREEZE_CAHCE_KEY = "Iot.OT.Bll.WorkPlaceLogic_";
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private SysSequenceNumberProvider sysSequenceNumberProvider;
    @Autowired
    private PmProductCharacteristicsVersionsProvider pmProductCharacteristicsVersionsProvider;
    @Autowired
    private PmProductBomVersionsProvider pmProductBomVersionsProvider;
    @Autowired
    private PmIssueCharacteristicsConfigProvider pmIssueCharacteristicsConfigProvider;
    @Autowired
    private IPpsEntryExtendService ppsEntryExtendService;
    @Autowired
    private IPpsPlanService ppsPlanService;
    @Autowired
    private IPpsOrderChangeLogService ppsOrderChangeLogService;
    @Autowired
    private IPpsResetVehicleLogService ppsResetVehicleLogService;
    @Autowired
    private IPpsOrderMapper ppsOrderMapper;
    @Autowired
    private LocalCache localCache;
    @Autowired
    private PmOrgProvider pmOrgProvider;
    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;

    @Autowired
    private RabbitMqSysQueueNoteProvider sysQueueNoteService;

    /**
     * 发送AS保留車
     *
     * @param ids
     * @param isFreeze
     */
    @Override
    public void sendAsKeepCarMessage(List<Long> ids, Boolean isFreeze) {
        List<AsKeepCarDto> sends = new ArrayList<>();
        for (Long id : ids) {
            PpsOrderEntity order = get(id);
            AsKeepCarDto send = new AsKeepCarDto();
            send.setVrn(order.getPlanNo());
            send.setVin(order.getBarcode());
            //send.setOrgCode(pmOrgProvider.getCurrentOrgCode());
            send.setUlocNo("");//没有
            send.setHoldTime(new Date());
            send.setHoldType(isFreeze ? "1" : "2");
            sends.add(send);
        }
        SysQueueNoteEntity sysQueueNoteEntity = new SysQueueNoteEntity();
        sysQueueNoteEntity.setGroupName(RabbitMQConstants.GROUP_NAME_AS_KEEPCAR_QUEUE);
        sysQueueNoteEntity.setContent(JsonUtils.toJsonString(sends));
        sysQueueNoteService.addSimpleMessage(sysQueueNoteEntity);
    }


    @Override
    public PpsOrderEntity getPpsOrderBySn(String sn) {
        QueryWrapper<PpsOrderEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsOrderEntity::getSn, sn);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    @Override
    public List<PpsOrderEntity> getListBySnCodes(List<String> snCodes) {
        QueryWrapper<PpsOrderEntity> qry = new QueryWrapper<>();
        qry.lambda().in(PpsOrderEntity::getSn, snCodes);
        return selectList(qry);
    }

    @Override
    public List<PpsOrderEntity> getListByBarcodes(List<String> barcodes) {
        QueryWrapper<PpsOrderEntity> qry = new QueryWrapper<>();
        qry.lambda().in(PpsOrderEntity::getBarcode, barcodes);
        return selectList(qry);
    }

    /**
     * 根据订单号获取订单实体
     *
     * @param orderNo 订单号
     * @return 订单实体
     */
    @Override
    public PpsOrderEntity getPpsOrderInfoByorderNo(String orderNo) {
        QueryWrapper<PpsOrderEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsOrderEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsOrderEntity::getOrderNo, orderNo);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 根据计划号&订单号&订单状态查询
     *
     * @param planNo      计划号
     * @param orderNo     订单号
     * @param orderStatus 订单状态
     * @return 订单实体
     */
    public PpsOrderEntity getPpsOrderInfoByPlanNo(String planNo, String orderNo, Integer orderStatus) {
        QueryWrapper<PpsOrderEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsOrderEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsOrderEntity::getPlanNo, planNo);
        lambdaQueryWrapper.eq(PpsOrderEntity::getOrderNo, orderNo);
        lambdaQueryWrapper.lt(PpsOrderEntity::getOrderStatus, orderStatus);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 校验特征10
     *
     * @param characteristics
     * @return
     */
    @Override
    public Boolean checkCharacteristic10(List<String> characteristics) {
        QueryWrapper<PpsOrderEntity> qryOrder = new QueryWrapper<>();
        qryOrder.lambda().in(PpsOrderEntity::getCharacteristic10, characteristics);
        return selectCount(qryOrder) > 0;
    }

    /**
     * 根据订单状态获取订单
     *
     * @param barcode
     * @param orderCategory
     * @return 订单
     */
    @Override
    public PpsOrderEntity getFirstByBarCodeOrderCategory(String barcode, String orderCategory) {
        QueryWrapper<PpsOrderEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsOrderEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsOrderEntity::getBarcode, barcode);
        lambdaQueryWrapper.eq(PpsOrderEntity::getOrderCategory, orderCategory);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }


    /**
     * 根据订单号获取BOM信息
     *
     * @param orderId
     * @return
     */
    @Override
    public List<PmProductBomEntity> getOrderBomByOrderId(Long orderId) {
        PpsOrderEntity orderInfo = get(orderId);
        if (orderInfo == null) {
            throw new InkelinkException("无效的订单");
        }
        return pmProductBomVersionsProvider.getBomData(orderInfo.getProductCode(), orderInfo.getBomVersion());
    }


    /**
     * 获取计划的bom数据
     *
     * @param ppsOrderNo 订单号
     * @return BOM数据集合
     */
    @Override
    public List<PmProductBomEntity> getOrderBom(String ppsOrderNo) {
        PpsOrderEntity orderInfo = getPpsOrderInfoByorderNo(ppsOrderNo);
        if (orderInfo == null) {
            throw new InkelinkException("无效的订单");
        }
        return pmProductBomVersionsProvider.getBomData(orderInfo.getProductCode(), orderInfo.getBomVersion());
    }

    /**
     * 根据id获取特征信息
     *
     * @param orderId
     * @return 特征详细数据
     */
    @Override
    public List<PmProductCharacteristicsEntity> getOrderCharacteristicByOrderId(Long orderId) {
        PpsOrderEntity orderInfo = get(orderId);
        if (orderInfo == null) {
            return new ArrayList<>();
        }
        return pmProductCharacteristicsVersionsProvider.getCharacteristicsData(orderInfo.getProductCode(), orderInfo.getCharacteristicVersion());
    }


    /**
     * 根据订单号获取特征信息
     *
     * @param orderNo 订单号
     * @return 特征详细数据
     */
    @Override
    public List<PmProductCharacteristicsEntity> getOrderCharacteristicByOrderNo(String orderNo) {
        PpsOrderEntity orderInfo = getPpsOrderInfoByorderNo(orderNo);
        if (orderInfo == null) {
            return new ArrayList<>();
        }
        return pmProductCharacteristicsVersionsProvider.getCharacteristicsData(orderInfo.getProductCode(), orderInfo.getCharacteristicVersion());
    }

    /**
     * 根据订单号更新状态
     *
     * @param orderNo     订单号
     * @param orderStatus 状态
     */
    @Override
    public void updateOrderStatusByOrderNo(String orderNo, Integer orderStatus) {
        UpdateWrapper<PpsOrderEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PpsOrderEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.eq(PpsOrderEntity::getOrderNo, orderNo);
        lambdaUpdateWrapper.set(PpsOrderEntity::getOrderStatus, orderStatus);
        lambdaUpdateWrapper.set(PpsOrderEntity::getIsFreeze, true);
        this.update(updateWrapper);
    }


    /**
     * 根据订单号更新 sn,barcode,orderStatus
     *
     * @param orderNo     订单号
     * @param sn          唯一码
     * @param barcode     条码
     * @param orderStatus 订单状态
     */
    public void updateEntityByOrderNo(String orderNo, String sn, String barcode, Integer orderStatus) {
        UpdateWrapper<PpsOrderEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PpsOrderEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.eq(PpsOrderEntity::getOrderNo, orderNo);
        lambdaUpdateWrapper.set(PpsOrderEntity::getSn, sn);
        lambdaUpdateWrapper.set(PpsOrderEntity::getBarcode, barcode);
        lambdaUpdateWrapper.set(PpsOrderEntity::getOrderStatus, orderStatus);
        this.update(updateWrapper);
    }

    /**
     * 批量更新工艺路径
     *
     * @param ids       主键集合
     * @param processId 工艺路径ID
     */
    public void updateEntityProcessIdByIds(List<Long> ids, Long processId) {
        UpdateWrapper<PpsOrderEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PpsOrderEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.in(PpsOrderEntity::getId, ids);
        lambdaUpdateWrapper.set(PpsOrderEntity::getPrcPpsProductProcessId, processId);
        this.update(updateWrapper);
    }

    /**
     * 根据ID 更新 是否冻结
     *
     * @param id       主键ID
     * @param isFreeze 是否冻结
     */
    public void updateEntityIsFreezeById(Serializable id, Boolean isFreeze) {
        UpdateWrapper<PpsOrderEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PpsOrderEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.eq(PpsOrderEntity::getId, id);
        lambdaUpdateWrapper.set(PpsOrderEntity::getIsFreeze, isFreeze);
        this.update(updateWrapper);
    }


    /**
     * 订单匹配切换验证
     *
     * @param vehicleChangeTpsInfo 参数列表
     * @return 验证结果
     */
    @Override
    public String validVehicleChangeTps(VehicleChangeTpsInfo vehicleChangeTpsInfo) {
        List<SysConfigurationEntity> orderStatus = sysConfigurationProvider.getSysConfigurations("OrderStatus");
        String message = "";
        Map<String, Object> order = validVehicleChangeTpsData(vehicleChangeTpsInfo);
        PpsOrderEntity changeOrder = (PpsOrderEntity) order.get("ChangeOrder");
        PpsOrderEntity desOrder = (PpsOrderEntity) order.get("DesOrder");
        if (!StringUtils.equals(changeOrder.getProductCode(), desOrder.getProductCode())) {
            message += "两个订单的整车物料号不一致，替换以后，需要使用移动RFID，重新写入TAG信息，替换订单" +
                    "（" + changeOrder.getProductCode() + ",目标订单（" + desOrder.getProductCode() + "))";
        }
        if (!changeOrder.getOrderStatus().equals(desOrder.getOrderStatus())) {
            List<SysConfigurationEntity> orderStatusList = orderStatus.stream()
                    .filter(o -> StringUtils.equals(o.getValue(), String.valueOf(changeOrder.getOrderStatus()))).collect(Collectors.toList());
            String changeOrderStatus = orderStatusList.stream().map(SysConfigurationEntity::getText).findFirst().orElse("");
            List<SysConfigurationEntity> desOrderList = orderStatus.stream()
                    .filter(s -> StringUtils.equals(s.getValue(), String.valueOf(desOrder.getOrderStatus()))).collect(Collectors.toList());
            String desOrderStatus = desOrderList.stream().map(SysConfigurationEntity::getText).findFirst().orElse("");
            message += "两个订单的状态不一致，替换以后，需要人工处理ERP的报工信息，替换订单（" + changeOrderStatus + "）,目标订单（" + desOrderStatus + "）；";
        }
        if (StringUtils.isBlank(message)) {
            message += "替换订单以后，请人工跟踪车辆的生产情况，再次校验；";
        }
        return message;
    }

    /**
     * 订单匹配切换
     *
     * @param vehicleChangeTpsInfo 参数列表
     */
    @Override
    public void vehicleChangeTps(VehicleChangeTpsInfo vehicleChangeTpsInfo) {
        Map<String, Object> order = validVehicleChangeTpsData(vehicleChangeTpsInfo);
        PpsOrderEntity changeOrder = (PpsOrderEntity) order.get("ChangeOrder");
        PpsOrderEntity desOrder = (PpsOrderEntity) order.get("DesOrder");
        //替换订单数据
        String tpsCode = changeOrder.getSn();
        Integer orderStatus = changeOrder.getOrderStatus();
        changeOrder.setSn(desOrder.getSn());
        changeOrder.setOrderStatus(desOrder.getOrderStatus());
        desOrder.setSn(tpsCode);
        desOrder.setOrderStatus(orderStatus);
        updateEntityByOrderNo(desOrder.getOrderNo(), desOrder.getSn(), desOrder.getBarcode(), desOrder.getOrderStatus());
        updateEntityByOrderNo(changeOrder.getOrderNo(), changeOrder.getSn(), changeOrder.getBarcode(), changeOrder.getOrderStatus());
        //将原订单和目标订单下面的工单替换
        List<Long> changeEntriesIds = ppsEntryExtendService.getEntityListByOrderNo(changeOrder.getOrderNo())
                .stream().map(PpsEntryEntity::getId).collect(Collectors.toList());
        List<Long> desEntriesIds = ppsEntryExtendService.getEntityListByOrderNo(desOrder.getOrderNo())
                .stream().map(PpsEntryEntity::getId).collect(Collectors.toList());
        if (changeEntriesIds.size() > 0) {
            ppsEntryExtendService.updateEntityByIds(changeEntriesIds, desOrder.getSn(), desOrder.getId(), desOrder.getOrderNo(), desOrder.getPlanNo());
        }
        if (desEntriesIds.size() > 0) {
            ppsEntryExtendService.updateEntityByIds(desEntriesIds, changeOrder.getSn(), changeOrder.getId(), changeOrder.getOrderNo(), changeOrder.getPlanNo());
        }

        //记录日志
        PpsOrderChangeLogEntity addInfo = new PpsOrderChangeLogEntity();
        addInfo.setDesOrderNo(vehicleChangeTpsInfo.getDesOrderNo());
        addInfo.setDesSn(vehicleChangeTpsInfo.getDesTpsCode());
        addInfo.setSourceOrderNo(vehicleChangeTpsInfo.getChangeOrderNo());
        addInfo.setSourceSn(vehicleChangeTpsInfo.getChangTpsCode());
        addInfo.setOperDt(new Date());
        ppsOrderChangeLogService.insert(addInfo);
    }

    /**
     * 订单切换验证
     *
     * @param vehicleChangeTpsInfo 切换参数列表
     * @return 返回验证后实体
     */
    private Map<String, Object> validVehicleChangeTpsData(VehicleChangeTpsInfo vehicleChangeTpsInfo) {
        if (StringUtils.equals(vehicleChangeTpsInfo.getChangeOrderNo(), vehicleChangeTpsInfo.getDesOrderNo())) {
            throw new InkelinkException("目标订单与替换订单一致，不能替换！");
        }
        PpsOrderEntity changeOrder = getPpsOrderInfoByorderNo(vehicleChangeTpsInfo.getChangeOrderNo());
        if (changeOrder == null) {
            throw new InkelinkException("没有找到订单号" + vehicleChangeTpsInfo.getChangeOrderNo() + "对应的订单");
        }
        if (!StringUtils.equals(changeOrder.getSn(), vehicleChangeTpsInfo.getChangTpsCode())) {
            throw new InkelinkException("订单号" + vehicleChangeTpsInfo.getChangeOrderNo() + "对应SN码不一致");
        }
        PpsOrderEntity desOrder = getPpsOrderInfoByorderNo(vehicleChangeTpsInfo.getDesOrderNo());
        if (desOrder == null) {
            throw new InkelinkException("没有找到订单号" + vehicleChangeTpsInfo.getDesOrderNo() + "对应的订单");
        }
        if (!StringUtils.equals(desOrder.getSn(), vehicleChangeTpsInfo.getDesTpsCode())) {
            throw new InkelinkException("订单号" + vehicleChangeTpsInfo.getDesOrderNo() + "对应SN码不一致");
        }
        if (!Objects.equals(desOrder.getPrcPpsProductProcessId(), changeOrder.getPrcPpsProductProcessId())) {
            throw new InkelinkException("目标订单与替换订单工艺路径不一致，不能替换！");
        }
        if (!StringUtils.equals(desOrder.getModel(), changeOrder.getModel())) {
            throw new InkelinkException("目标订单与替换订单车型不一致，不能替换！");
        }
        Map<String, Object> dynamicInfo = Maps.newHashMapWithExpectedSize(2);
        dynamicInfo.put("ChangeOrder", changeOrder);
        dynamicInfo.put("DesOrder", desOrder);
        return dynamicInfo;
    }

    /**
     * 设置工艺路径
     *
     * @param orderIds  生产订单ID
     * @param processId 工艺路径ID
     */
    @Override
    public void setProcess(List<Long> orderIds, Long processId) {
        for (Long orderId : orderIds) {
            PpsOrderEntity data = this.get(orderId);
            if (data != null && data.getOrderStatus() > 3) {
                throw new InkelinkException("订单" + data.getOrderNo() + "已拆分,不能设置工艺路径");
            }
        }
        updateEntityProcessIdByIds(orderIds, processId);
    }

    /**
     * 删除备件订单
     *
     * @param ids
     */
    @Override
    public void deleteStamping(List<Long> ids) {
        QueryWrapper<PpsOrderEntity> orderQry = new QueryWrapper<>();
        orderQry.lambda().in(PpsOrderEntity::getId, ids);
        List<PpsOrderEntity> datas = selectList(orderQry);

        for (PpsOrderEntity item : datas) {
            if (!OrderCategoryEnum.SparePart.codeString().equals(item.getOrderCategory())) {
                throw new InkelinkException("订单" + item.getOrderNo() + "不能被删除，只能删除备件订单");
            }
            if (item.getOrderStatus() > 3) {
                throw new InkelinkException("订单" + item.getOrderNo() + "已经在生产，不能删除");
            }
            UpdateWrapper<PpsEntryEntity> upEntry = new UpdateWrapper<>();
            upEntry.lambda().eq(PpsEntryEntity::getPrcPpsOrderId, item.getId());
            ppsEntryExtendService.delete(upEntry);
        }
        delete(ids.toArray(new Long[0]));
    }

    /**
     * 取消冻结整车生成订单
     *
     * @param ppsOrderIds 订单集合
     * @param remark      备注
     */
    @Override
    public void unFreeze(List<Long> ppsOrderIds, String remark) {
        for (Long data : ppsOrderIds) {
            PpsOrderEntity ppsOrderInfo = this.get(data);
            if (ppsOrderInfo.getIsFreeze()) {
                if (ppsOrderInfo.getOrderStatus() == OrderStatusEnum.Scrap.code()) {
                    throw new InkelinkException("'" + ppsOrderInfo.getOrderNo() + "' 该订单已报废,不能解冻");
                }
                updateEntityIsFreezeById(data, false);
                String otcacheName = FREEZE_CAHCE_KEY + ppsOrderInfo.getSn();
                localCache.removeObject(otcacheName);
            } else {
                throw new InkelinkException("'" + ppsOrderInfo.getOrderNo() + "'该订单未冻结,不需解冻");
            }
        }
    }

    /**
     * 取消冻结整车生成订单
     *
     * @param ppsOrderIds 订单集合
     */
    @Override
    public void unFreeze(List<Long> ppsOrderIds) {
        this.unFreeze(ppsOrderIds, "");
    }

    /**
     * 冻结整车生成订单
     *
     * @param ppsOrderIds 订单集合
     * @param remark      备注
     */
    @Override
    public void freeze(List<Long> ppsOrderIds, String remark) {
        for (Long data : ppsOrderIds) {
            PpsOrderEntity ppsOrderInfo = this.get(data);
            if (ppsOrderInfo.getIsFreeze()) {
                throw new InkelinkException(ppsOrderInfo.getOrderNo() + "该订单状态已冻结");
            }
            updateEntityIsFreezeById(data, true);
            String otcacheName = FREEZE_CAHCE_KEY + ppsOrderInfo.getSn();
            localCache.removeObject(otcacheName);
        }
    }


    /**
     * @param sn       产品唯一标识码
     * @param isFreeze 是否冻结
     * @param remark   备注
     */
    @Override
    public void operateIsFreezeById(String sn, Boolean isFreeze, String remark) {
        if (StringUtils.isBlank(sn)) {
            throw new InkelinkException("产品唯一标识不能为空");
        }
        PpsOrderEntity ppsOrderEntity = getPpsOrderBySnOrBarcode(sn);
        if (ppsOrderEntity == null) {
            throw new InkelinkException("无效的产品标识码");
        }
        if (isFreeze && !ppsOrderEntity.getIsFreeze()) {
            updateEntityIsFreezeById(ppsOrderEntity.getId(), true);
        }
        if (!isFreeze && ppsOrderEntity.getIsFreeze()) {
            if (ppsOrderEntity.getOrderStatus() == OrderStatusEnum.Scrap.code()) {
                throw new InkelinkException("'" + ppsOrderEntity.getOrderNo() + "' 该订单已报废,不能解冻");
            }
            updateEntityIsFreezeById(ppsOrderEntity.getId(), false);
        }
        String otcacheName = FREEZE_CAHCE_KEY + ppsOrderEntity.getSn();
        localCache.removeObject(otcacheName);
    }

    /**
     * 冻结整车生成订单
     *
     * @param ppsOrderIds 订单集合
     */
    @Override
    public void freeze(List<Long> ppsOrderIds) {
        this.freeze(ppsOrderIds, "");
    }


    /**
     * 重置车辆
     *
     * @param para
     */
    @Override
    public void resetVehicle(ResetVehiclePara para) {
        if (StringUtils.isBlank(para.getVin()) || StringUtils.isBlank(para.getShopCode())) {
            throw new InkelinkException("无效的产品信息");
        }
        String vin = para.getVin();
        QueryWrapper<PpsOrderEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsOrderEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsOrderEntity::getBarcode, vin);
        PpsOrderEntity orderInfo = this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
        if (orderInfo == null) {
            throw new InkelinkException(vin + "无效的产品信息");
        }
        //焊装车间需要重新拆分
        if (BodyShopCode.WE.equals(para.getShopCode())) {
            UpdateWrapper<PpsPlanEntity> upPlan = new UpdateWrapper<>();
            upPlan.lambda().set(PpsPlanEntity::getPlanStatus, 1).eq(PpsPlanEntity::getPlanNo, orderInfo.getPlanNo());
            ppsPlanService.update(upPlan);

            UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
            upOrder.lambda().set(PpsOrderEntity::getOrderStatus, 3).eq(PpsOrderEntity::getId, orderInfo.getId());
            update(upOrder);
        }
        UpdateWrapper<PpsEntryEntity> upEntry = new UpdateWrapper<>();
        upEntry.lambda().set(PpsEntryEntity::getStatus, 2).eq(PpsEntryEntity::getEntryType, 1)
                .eq(PpsEntryEntity::getOrderNo, orderInfo.getOrderNo())
                .eq(PpsEntryEntity::getWorkshopCode, para.getShopCode());
        ppsEntryExtendService.update(upEntry);
        UpdateWrapper<PpsEntryEntity> delEntry = new UpdateWrapper<>();
        delEntry.lambda().eq(PpsEntryEntity::getEntryType, 2)
                .eq(PpsEntryEntity::getOrderNo, orderInfo.getOrderNo())
                .eq(PpsEntryEntity::getWorkshopCode, para.getShopCode());
        ppsEntryExtendService.delete(delEntry);

        //UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
        //upOrder.lambda().set(PpsOrderEntity::getOrderStatus, 3)
        //        .eq(PpsOrderEntity::getId, orderInfo.getId());
        //update(upOrder);

        String aviCode = StringUtils.EMPTY;
        // String userName = identityHelper.getUserName();
        // String datetime = DateUtils.format(new Date(),DateUtils.DATE_PATTERN);
        Map<String, Object> spMap = Maps.newHashMapWithExpectedSize(3);
        spMap.put("vsn", orderInfo.getBarcode());
        spMap.put("vdatetime", new Date());
        spMap.put("vshopcode", para.getShopCode());
        // 使用存储过程处理
        List<String> aviCodes = ppsOrderMapper.spResetVehicleDel(spMap);
        if (aviCodes != null && aviCodes.size() > 0) {
            aviCode = aviCodes.get(0);
        }
        PpsResetVehicleLogEntity resetLog = new PpsResetVehicleLogEntity();
        resetLog.setVin(orderInfo.getBarcode());
        resetLog.setEndAviCode(aviCode);
        resetLog.setWorkshopCode(para.getShopCode());
        ppsResetVehicleLogService.insert(resetLog);
        this.saveChange();
    }

    /**
     * 生成顺序号（订单）
     *
     * @param productNo
     * @param shopCode
     */
    @Override
    public void generateSequenceNoForOrder(String productNo, String shopCode) {
        PpsOrderEntity order = getPpsOrderInfoByKey(productNo);
        if (order == null)
            throw new InkelinkException("没有找到订单");

        if ("VI".equalsIgnoreCase(shopCode)) {
            return;
        }
        if ("WE".equalsIgnoreCase(shopCode)) {
            if (!StringUtils.isBlank(order.getBodyNo())) {
                return;
            }
            String seqNo = StringUtils.leftPad(sysSnConfigProvider.createSn(shopCode + "_SEQ_Order"), 10, '0');
            UpdateWrapper<PpsOrderEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PpsOrderEntity::getBodyNo, seqNo)
                    .eq(PpsOrderEntity::getId, order.getId());
            update(upset);
        } else if ("PA".equalsIgnoreCase(shopCode)) {
            if (!StringUtils.isBlank(order.getPaintNo())) {
                return;
            }
            String seqNo = StringUtils.leftPad(sysSnConfigProvider.createSn(shopCode + "_SEQ_Order"), 10, '0');

            UpdateWrapper<PpsOrderEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PpsOrderEntity::getPaintNo, seqNo)
                    .eq(PpsOrderEntity::getId, order.getId());
            update(upset);
        } else if ("GA".equalsIgnoreCase(shopCode)) {
            if (!StringUtils.isBlank(order.getAssemblyNo())) {
                return;
            }
            String seqNo = StringUtils.leftPad(sysSnConfigProvider.createSn(shopCode + "_SEQ_Order"), 10, '0');

            UpdateWrapper<PpsOrderEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PpsOrderEntity::getAssemblyNo, seqNo)
                    .eq(PpsOrderEntity::getId, order.getId());
            update(upset);
        } else {
            if (!StringUtils.isBlank(order.getProductionNo())) {
                return;
            }
            String seqNo = StringUtils.leftPad(sysSnConfigProvider.createSn(shopCode + "_SEQ_Order"), 10, '0');
            UpdateWrapper<PpsOrderEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PpsOrderEntity::getProductionNo, seqNo)
                    .eq(PpsOrderEntity::getId, order.getId());
            update(upset);
        }
    }

}