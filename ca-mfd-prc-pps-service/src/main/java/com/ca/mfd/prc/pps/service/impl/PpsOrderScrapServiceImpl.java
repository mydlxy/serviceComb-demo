package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQConstants;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.SysQueueNoteEntity;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.provider.RabbitMqSysQueueNoteProvider;
import com.ca.mfd.prc.pps.communication.dto.AsOrderScrapDto;
import com.ca.mfd.prc.pps.dto.ScrapAffirmPara;
import com.ca.mfd.prc.pps.entity.PpsEntryEntity;
import com.ca.mfd.prc.pps.entity.PpsEntryPartsEntity;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.entity.PpsOrderScrapEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanPartsEntity;
import com.ca.mfd.prc.pps.enums.OrderStatusEnum;
import com.ca.mfd.prc.pps.mapper.IPpsOrderScrapMapper;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmOrgProvider;
import com.ca.mfd.prc.pps.service.IPpsEntryService;
import com.ca.mfd.prc.pps.service.IPpsOrderScrapService;
import com.ca.mfd.prc.pps.service.IPpsOrderService;
import com.ca.mfd.prc.pps.service.IPpsPlanService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 生产报废订单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PpsOrderScrapServiceImpl extends AbstractCrudServiceImpl<IPpsOrderScrapMapper, PpsOrderScrapEntity> implements IPpsOrderScrapService {

    @Autowired
    private IPpsOrderScrapMapper ppsOrderScrapMapper;
    @Autowired
    private IPpsOrderService ppsOrderService;
    @Autowired
    private IPpsPlanService ppsPlanService;
    @Autowired
    private IPpsEntryService ppsEntryService;

    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;
    @Autowired
    private PmOrgProvider pmOrgProvider;

    @Autowired
    RabbitMqSysQueueNoteProvider sysQueueNoteService;

    /**
     * 发送AS报废
     *
     * @param para
     */
    @Override
    public void sendAsScrapMessage(ScrapAffirmPara para) {
        PpsOrderScrapEntity ppsOrderScrapInfo = get(para.getDataId());

        PpsOrderEntity order = null;
        if (StringUtils.isNotBlank(ppsOrderScrapInfo.getBarcode())) {
            order = ppsOrderService.getPpsOrderBySn(ppsOrderScrapInfo.getBarcode());
        }
        if (StringUtils.isNotBlank(ppsOrderScrapInfo.getOrderNo())) {
            order = ppsOrderService.getPpsOrderInfoByorderNo(ppsOrderScrapInfo.getOrderNo());
        }
        if (order == null) {
            return;
        }
        AsOrderScrapDto send = new AsOrderScrapDto();
        send.setVrn(order.getPlanNo());
        send.setVin(ppsOrderScrapInfo.getBarcode());
        //send.setOrgCode(pmOrgProvider.getCurrentOrgCode());
        send.setScrapTime(new Date());
        SysQueueNoteEntity sysQueueNoteEntity = new SysQueueNoteEntity();
        sysQueueNoteEntity.setGroupName(RabbitMQConstants.GROUP_NAME_AS_ORDERSCRAP_QUEUE);
        sysQueueNoteEntity.setContent(JsonUtils.toJsonString(send));
        sysQueueNoteService.addSimpleMessage(sysQueueNoteEntity);
    }

    @Override
    public void beforeInsert(PpsOrderScrapEntity model) {
        checkOrdeNo(model);
    }

    @Override
    public void beforeUpdate(PpsOrderScrapEntity model) {
        checkOrdeNo(model);
    }

    @Override
    public void beforeDelete(Collection<? extends Serializable> idList) {
        if (idList == null || idList.size() == 0) {
            super.beforeDelete(idList);
            return;
        }
        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("ID", String.join("|", idList.stream().map(c -> c.toString()).collect(Collectors.toList())), ConditionOper.In));
        List<PpsOrderScrapEntity> datas = getData(conditionInfos);
        if (datas.stream().anyMatch(o -> o.getIsConfirm())) {
            throw new InkelinkException("已经确认的数据不允许删除");
        }
    }


    private void checkOrdeNo(PpsOrderScrapEntity ppsOrderScrapInfo) {
        if (StringUtils.isBlank(ppsOrderScrapInfo.getOrderNo())) {
            throw new InkelinkException("请输入需要报废的生产订单号");
        }
        QueryWrapper<PpsOrderScrapEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsOrderScrapEntity::getOrderNo, ppsOrderScrapInfo.getOrderNo())
                .eq(PpsOrderScrapEntity::getIsConfirm, false);
        if (selectCount(qry) > 0) {
            throw new InkelinkException("该产品已被报废请等待确认");
        }
        PpsOrderEntity data = ppsOrderService.getPpsOrderInfoByorderNo(ppsOrderScrapInfo.getOrderNo());
        if (data == null) {
            throw new InkelinkException("生产单号" + ppsOrderScrapInfo.getOrderNo() + "在订单中不存在");
        }
        if (data.getOrderStatus() == OrderStatusEnum.Scrap.code()) {
            throw new InkelinkException("生产单号" + ppsOrderScrapInfo.getOrderNo() + "已经报废，不能重复处理");
        }
        ppsOrderScrapInfo.setQty(data.getOrderQuantity());
        if (StringUtils.isBlank(ppsOrderScrapInfo.getScrapNo())) {
            ppsOrderScrapInfo.setScrapNo(sysSnConfigProvider.createSn("PpsOrderScrap"));
        }
    }

    /**
     * 报废确认
     *
     * @param para
     */
    @Override
    public void scrapAffirm(ScrapAffirmPara para) {
        PpsOrderScrapEntity ppsOrderScrapInfo = get(para.getDataId());
        updateIsConfirmById(ppsOrderScrapInfo.getId());
        PpsOrderEntity ppsOrderInfo = ppsOrderService.getPpsOrderInfoByorderNo(ppsOrderScrapInfo.getOrderNo());
        if (ppsOrderInfo != null) {
            ppsOrderInfo.setOrderStatus(OrderStatusEnum.Scrap.code());
            ppsEntryService.updateStatusByOrderNo(ppsOrderScrapInfo.getOrderNo(), OrderStatusEnum.Scrap.code());
            ppsOrderService.updateOrderStatusByOrderNo(ppsOrderScrapInfo.getOrderNo(), OrderStatusEnum.Scrap.code());
            //解绑所有关重件
            if (StringUtils.isNotBlank(ppsOrderInfo.getSn())) {
                spScrapAffirmDel(ppsOrderInfo.getSn());
            }
            //计划完成
            PpsPlanEntity plan = ppsPlanService.getFirstByPlanNo(ppsOrderInfo.getOrderNo());
            setVePlanEnd(ppsOrderInfo, plan);
        }
    }

    /**
     * 整车、电池订单报废
     *
     * */
    public ScrapAffirmPara orderVeScrap(String orderNo) {
        PpsOrderScrapEntity ppsOrderScrapInfo = new PpsOrderScrapEntity();
        ppsOrderScrapInfo.setOrderNo(orderNo);
        checkOrdeNo(ppsOrderScrapInfo);
        ppsOrderScrapInfo.setIsConfirm(true);

        PpsOrderEntity ppsOrderInfo = ppsOrderService.getPpsOrderInfoByorderNo(orderNo);
        if (ppsOrderInfo != null) {
            if(ppsOrderInfo.getOrderStatus()>=4) {
                throw new InkelinkException("订单状态不允许此操作!");
            }
            ppsOrderScrapInfo.setBarcode(ppsOrderInfo.getSn());
            ppsOrderScrapInfo.setOrderCategory(ppsOrderInfo.getOrderCategory());

            //更新订单和工单状态
            ppsOrderInfo.setOrderStatus(OrderStatusEnum.Scrap.code());
            ppsEntryService.updateStatusByOrderNo(ppsOrderInfo.getOrderNo(), OrderStatusEnum.Scrap.code());
            ppsOrderService.updateOrderStatusByOrderNo(ppsOrderInfo.getOrderNo(), OrderStatusEnum.Scrap.code());

            //计划完成
            PpsPlanEntity plan = ppsPlanService.getFirstByPlanNo(orderNo);
            setVePlanEnd(ppsOrderInfo, plan);

            //写入报废记录
            insert(ppsOrderScrapInfo);
            //saveChange();

            if (StringUtils.isNotBlank(ppsOrderInfo.getSn())) {
                //解绑所有关重件
                spScrapAffirmDel(ppsOrderInfo.getSn());
            }

            ScrapAffirmPara res = new ScrapAffirmPara();
            res.setRemark("取消生产");
            res.setDataId(ppsOrderScrapInfo.getId());
            return res;
        }
        return null;

    }

    /**
     * 设置计划完成(整车、电池、备件)
     */
    public void setVePlanEnd(PpsOrderEntity orderInfo, PpsPlanEntity plan) {

        //代表计划生产完成
        //更新生产计划的状态(完成）
        if ("1".equals(orderInfo.getOrderCategory())
                || "7".equals(orderInfo.getOrderCategory())) {
            //获取该计划下面所有没有完成的订单
            QueryWrapper<PpsOrderEntity> qryOrder = new QueryWrapper<>();
            qryOrder.lambda().eq(PpsOrderEntity::getPlanNo, orderInfo.getPlanNo())
                    .ne(PpsOrderEntity::getOrderStatus, 99)
                    .ne(PpsOrderEntity::getOrderStatus, 98)
                    .ne(PpsOrderEntity::getOrderNo, orderInfo.getOrderNo());
            if (ppsOrderService.selectCount(qryOrder) == 0) {
                //var noCompleteOrders = _ppsOrderDal.Table.Where(o => o.PlanNo == plan.PlanNo
                // && o.OrderStatus !=99).ToList();
                //if (noCompleteOrders.Where(o => o.OrderNo != order.OrderNo && o.OrderStatus!=98).Count() == 0)
                UpdateWrapper<PpsPlanEntity> planUp = new UpdateWrapper<>();
                planUp.lambda().set(PpsPlanEntity::getPlanStatus, 30)
                        .eq(PpsPlanEntity::getId, plan.getId());
                ppsPlanService.update(planUp);
            }
        } else if ("2".equals(orderInfo.getOrderCategory())) {
            //获取该计划下面所有没有完成的订单
            QueryWrapper<PpsOrderEntity> qryOrder = new QueryWrapper<>();
            qryOrder.lambda().eq(PpsOrderEntity::getPlanNo, orderInfo.getPlanNo())
                    .ne(PpsOrderEntity::getOrderStatus, 5)
                    .ne(PpsOrderEntity::getOrderStatus, 98)
                    .ne(PpsOrderEntity::getOrderNo, orderInfo.getOrderNo());
            if (ppsOrderService.selectCount(qryOrder) == 0) {
                // var noCompleteOrders = _ppsOrderDal.Table.Where(o => o.PlanNo == plan.PlanNo && o.OrderStatus != 5).ToList();
                // if (noCompleteOrders.Where(o => o.OrderNo != order.OrderNo && o.OrderStatus != 98).Count() == 0)
                UpdateWrapper<PpsPlanEntity> planUp = new UpdateWrapper<>();
                planUp.lambda().set(PpsPlanEntity::getPlanStatus, 30)
                        .eq(PpsPlanEntity::getId, plan.getId());
                ppsPlanService.update(planUp);
            }
        }
    }


    /**
     * 报废确认刪除
     *
     * @param sn
     */
    public void spScrapAffirmDel(String sn) {
        //@Transactional(propagation= Propagation.REQUIRES_NEW)
        Map<String, Object> maps = Maps.newHashMapWithExpectedSize(1);
        maps.put("vsn", sn);
        ppsOrderScrapMapper.spScrapAffirmDel(maps);
    }


    /**
     * 订单直接报废(离散报废)
     *
     * @param ppsOrderScrapInfo
     */
    @Override
    public void productScrap(PpsOrderScrapEntity ppsOrderScrapInfo) {
        checkOrdeNo(ppsOrderScrapInfo);
        ppsOrderScrapInfo.setIsConfirm(true);
        PpsOrderEntity ppsOrderInfo = ppsOrderService.getPpsOrderInfoByorderNo(ppsOrderScrapInfo.getOrderNo());

        //订单对应所有未完成的工单全做报废
        UpdateWrapper<PpsEntryEntity> upEntry = new UpdateWrapper<>();
        upEntry.lambda().set(PpsEntryEntity::getStatus, 98)
                .set(PpsEntryEntity::getActualEndDt, new Date())
                .eq(PpsEntryEntity::getOrderNo, ppsOrderInfo.getOrderNo())
                .le(PpsEntryEntity::getStatus, 20);
        ppsEntryService.update(upEntry);

        UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
        upOrder.lambda().set(PpsOrderEntity::getOrderStatus, 98)
                .eq(PpsOrderEntity::getOrderNo, ppsOrderInfo.getOrderNo());
        ppsOrderService.update(upOrder);

        //获取该计划下面所有没有完成的订单
        QueryWrapper<PpsOrderEntity> qryOrder = new QueryWrapper<>();
        qryOrder.lambda().eq(PpsOrderEntity::getPlanNo, ppsOrderInfo.getPlanNo())
                .lt(PpsOrderEntity::getOrderStatus, 30)
                .ne(PpsOrderEntity::getId, ppsOrderInfo.getId());
        if (ppsOrderService.selectCount(qryOrder) == 0) {
            UpdateWrapper<PpsPlanEntity> upPlan = new UpdateWrapper<>();
            upPlan.lambda().set(PpsPlanEntity::getPlanStatus, 30)
                    .eq(PpsPlanEntity::getPlanNo, ppsOrderInfo.getPlanNo());
            ppsPlanService.update(upPlan);
        }
        insert(ppsOrderScrapInfo);
        saveChange();
    }

    public void updateIsConfirmById(Long id) {
        UpdateWrapper<PpsOrderScrapEntity> wrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PpsOrderScrapEntity> lambdaUpdateWrapper = wrapper.lambda();
        lambdaUpdateWrapper.eq(PpsOrderScrapEntity::getId, id);
        lambdaUpdateWrapper.set(PpsOrderScrapEntity::getIsConfirm, true);
        this.update(wrapper);
    }

}