package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQConstants;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.SysQueueNoteEntity;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.provider.RabbitMqSysQueueNoteProvider;
import com.ca.mfd.prc.pps.communication.dto.AsQueueStartDto;
import com.ca.mfd.prc.pps.communication.dto.LmsLockPlanDto;
import com.ca.mfd.prc.pps.dto.AffirmModuleEntryPara;
import com.ca.mfd.prc.pps.dto.DownModuleEntryInfo;
import com.ca.mfd.prc.pps.dto.InsertAsAviPointInfo;
import com.ca.mfd.prc.pps.dto.ModuleReportPara;
import com.ca.mfd.prc.pps.dto.ModuleSpacerDto;
import com.ca.mfd.prc.pps.dto.ModuleStructDto;
import com.ca.mfd.prc.pps.dto.ModuleUnitDto;
import com.ca.mfd.prc.pps.dto.OrderEntryInfo;
import com.ca.mfd.prc.pps.entity.PpsEntryConfigEntity;
import com.ca.mfd.prc.pps.entity.PpsEntryEntity;
import com.ca.mfd.prc.pps.entity.PpsEntryPartsEntity;
import com.ca.mfd.prc.pps.entity.PpsModuleIssueMainEntity;
import com.ca.mfd.prc.pps.entity.PpsModuleIssueModuleEntity;
import com.ca.mfd.prc.pps.entity.PpsModuleIssueUnitEntity;
import com.ca.mfd.prc.pps.entity.PpsModuleReportEntity;
import com.ca.mfd.prc.pps.entity.PpsModuleSplitStrategyEntity;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanAviEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanPartsEntity;
import com.ca.mfd.prc.pps.entity.PpsProductProcessAviEntity;
import com.ca.mfd.prc.pps.entity.PpsProductProcessEntity;
import com.ca.mfd.prc.pps.extend.IPpsPlanExtendService;
import com.ca.mfd.prc.pps.mapper.IPpsEntryMapper;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.pps.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pps.service.IPpsAsAviPointService;
import com.ca.mfd.prc.pps.service.IPpsBindingTagService;
import com.ca.mfd.prc.pps.service.IPpsEntryConfigService;
import com.ca.mfd.prc.pps.service.IPpsEntryPartsService;
import com.ca.mfd.prc.pps.service.IPpsEntryService;
import com.ca.mfd.prc.pps.service.IPpsLogicService;
import com.ca.mfd.prc.pps.service.IPpsModuleIssueMainService;
import com.ca.mfd.prc.pps.service.IPpsModuleIssueModuleService;
import com.ca.mfd.prc.pps.service.IPpsModuleIssueUnitService;
import com.ca.mfd.prc.pps.service.IPpsModuleReportService;
import com.ca.mfd.prc.pps.service.IPpsModuleSplitStrategyService;
import com.ca.mfd.prc.pps.service.IPpsOrderService;
import com.ca.mfd.prc.pps.service.IPpsPlanAviService;
import com.ca.mfd.prc.pps.service.IPpsPlanPartsService;
import com.ca.mfd.prc.pps.service.IPpsProductProcessService;
import com.ca.mfd.prc.pps.service.IPpsRinStandardConfigService;
import com.ca.mfd.prc.pps.service.IPpsVtVinRuleService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 计划逻辑处理
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PpsLogicServiceImpl implements IPpsLogicService {
    private static final Logger logger = LoggerFactory.getLogger(PpsLogicServiceImpl.class);
    private static final Object PLAN_SPLITLOCK_OBJ = new Object();
    private static final Object PLAN_SORT_RELEASELOCK_OBJ = new Object();
    @Autowired
    private RabbitMqSysQueueNoteProvider sysQueueNoteService;
    @Autowired
    private IPpsEntryService ppsEntryService;
    @Autowired
    private IPpsPlanExtendService ppsPlanExtendService;
    @Autowired
    private IPpsEntryMapper ppsEntryDao;
    @Autowired
    private IPpsOrderService ppsOrderService;
    @Autowired
    private IPpsVtVinRuleService ppsVtVinRuleService;
    @Autowired
    private IPpsRinStandardConfigService ppsRinStandardConfigService;
    @Autowired
    private IPpsAsAviPointService ppsAsAviPointService;
    @Autowired
    private IPpsProductProcessService ppsProductProcessService;
    @Autowired
    private IPpsEntryConfigService ppsEntryConfigService;
    @Autowired
    private IPpsBindingTagService ppsBindingTagService;
    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    private IPpsModuleIssueModuleService ppsModuleIssueModuleService;
    @Autowired
    private IPpsModuleIssueMainService ppsModuleIssueMainService;
//    @Autowired
//    private IPpsModuleIssueSpacerService ppsModuleIssueSpacerService;
    @Autowired
    private IPpsModuleIssueUnitService ppsModuleIssueUnitService;
    @Autowired
    private IPpsModuleSplitStrategyService ppsModuleSplitStrategyService;
    @Autowired
    private IPpsModuleReportService ppsModuleReportService;

    @Autowired
    private IPpsPlanPartsService ppsPlanPartsService;
    @Autowired
    private IPpsEntryPartsService ppsEntryPartsService;
    @Autowired
    private IPpsPlanAviService ppsPlanAviService;




    private final Object moduleEntrylock = new Object();
    private final Object splitModulelock = new Object();
    private final Object moduleReportlock = new Object();
    private final Object bodyShopStartlock = new Object();
    private final Object baShopStartlock = new Object();


//    /// <summary>
//    /// 返回工位操作指导书
//    /// </summary>
//    /// <param name="workStationCode"></param>
//    /// <param name="sn"></param>
//    /// <returns></returns>
//    public WoBopBook GetBopBookByWorkStationCode(string workStationCode, string sn) {
//
//        var order= _ppsOrderBll.GetPpsOrderInfoByKey(sn);
//        if (order != null)
//            return _pmWorkstationOperBookBll.GetBopBook(workStationCode, order.ProductCode, order.CharacteristicVersion, order.Model);
//
//        return default;
//
//    }
//
//    public List<PmWorkstationOperBookInfo> GetBookByWorkStationCode(string workStationCode, string sn, string materialNo = null)
//    {
//
//        var order = _ppsOrderBll.GetPpsOrderInfoByKey(sn);
//        if (order != null)
//            if (string.IsNullOrEmpty(materialNo))
//                return _pmWorkstationOperBookBll.GetBookByWorkStationCode(workStationCode, order.ProductCode, order.CharacteristicVersion, order.Model);
//            else
//                return _pmWorkstationOperBookBll.GetBookByWorkStationCode(workStationCode, order.ProductCode, order.CharacteristicVersion, order.Model).Where(s => s.MaterialNo == materialNo).ToList();
//
//        return default;
//
//    }
//
//
//    /// <summary>
//    /// 根据工位号和sn 获取物料清单
//    /// </summary>
//    /// <param name="workStationCode"></param>
//    /// <param name="sn"></param>
//    /// <param name="materialNo"></param>
//    /// <returns></returns>
//    public List<PmWorkstationMaterialInfo> GetMaterialByWorkStationCode(string workStationCode, string sn, string materialNo=null)
//    {
//
//        var order = _ppsOrderBll.GetPpsOrderInfoByKey(sn);
//        if (order != null)
//            if (string.IsNullOrEmpty(materialNo))
//                return _pmWorkstationMaterialBll.GeWorkStationCodeByMaterial(workStationCode, order.ProductCode, order.CharacteristicVersion, order.Model);
//            else
//                return _pmWorkstationMaterialBll.GeWorkStationCodeByMaterial(workStationCode, order.ProductCode, order.CharacteristicVersion, order.Model).Where(s => s.MaterialNo == materialNo).ToList();
//        return default;
//
//    }
//    /// <summary>
//    /// 返回该车的物料对应的操作指导书
//    /// </summary>
//    /// <param name="workStationCode"></param>
//    /// <param name="sn"></param>
//    /// <returns></returns>
//    public List<MateralInculdOperBook> GetMaterialBookByWorkStationCode(string workStationCode, string sn)
//    {
//
//        var order = _ppsOrderBll.GetPpsOrderInfoByKey(sn);
//        if (order != null) {
//
//            var data= from a in _pmWorkstationMaterialBll.GeWorkStationCodeByMaterial(workStationCode, order.ProductCode, order.CharacteristicVersion, order.Model)
//            join b in _pmWorkstationOperBookBll.GetBookByWorkStationCode(workStationCode, order.ProductCode, order.CharacteristicVersion, order.Model)
//            on a.MaterialNo equals b.MaterialNo
//            select new MateralInculdOperBook
//            {
//                WorkStationCode = a.WorkstationCode,
//                        LineCode = a.LineCode,
//                        MateralNo = a.MaterialNo,
//                        MateralNum = a.MaterialNum,
//                        MateralName = a.MasterChinese,
//                        FileName = b.WoBookName,
//                        FilePath = b.WoBookPath
//            };
//            return data.ToList();
//        }
//
//        return default;
//
//    }

    /**
     * 批量复制计划履历
     *
     * @param planNos
     */
    @Override
    public void batchCopyPlanAvi(List<String> planNos,String planNo) {
        if (planNos == null || planNos.isEmpty()) {
            return;
        }
        List<PpsPlanAviEntity> planAvis = ppsPlanAviService.getListByPlanNo(planNo);
        if (planAvis == null || planAvis.isEmpty()) {
            return;
        }
        List<PpsPlanAviEntity> planAvisAdds = new ArrayList<>();
        for (String pno : planNos) {
            List<PpsPlanAviEntity> planAvisTmp = ppsPlanAviService.getListByPlanNo(pno);
            if (planAvisTmp != null && !planAvisTmp.isEmpty()) {
                continue;
            }
            for (PpsPlanAviEntity ent : planAvis) {
                PpsPlanAviEntity item = new PpsPlanAviEntity();
                BeanUtils.copyProperties(ent, item);
                item.setPlanNo(pno);
                item.setId(IdGenerator.getId());
                planAvisAdds.add(item);
            }
        }
        ppsPlanAviService.insertBatch(planAvisAdds, 200, false, 1);
    }

    /**
     * 批量绑定吊牌
     *
     * @param planNos
     */
    @Override
    public void batchBodyShopStartWork(List<String> planNos,String workstationCode) {
        if (planNos == null || planNos.isEmpty()) {
            return;
        }
        for (String planNo : planNos) {
            PpsPlanEntity plan = ppsPlanExtendService.getFirstByPlanNo(planNo);
            if (plan == null) {
                throw new InkelinkException("没有计划号【"+planNo+"】对应的计划信息");
            }
            PpsOrderEntity order = ppsOrderService.getByPlanNo(planNo);
            if (order == null) {
                throw new InkelinkException("没有计划号【"+planNo+"】对应的订单信息");
            }
            if (!StringUtils.isBlank(plan.getAttribute6()) && StringUtils.isBlank(order.getBarcode())) {
                order.setBarcode(plan.getAttribute6());
                ppsOrderService.updateById(order);
                ppsOrderService.saveChange();
            }
            //从计划excel导入
            PpsEntryEntity entryInfo = ppsEntryService.getFirstByOrderShopCode(order.getId(), "WE");
            OrderEntryInfo shopEntity = new OrderEntryInfo();
            if (entryInfo == null) {
                //直接开工
                shopEntity.setOrderId(order.getId());
                startWorkOther(shopEntity);
            } else {
                if (StringUtils.isBlank(plan.getAttribute7())) {
                    throw new InkelinkException("没有计划号【"+planNo+"】对应的吊牌号");
                }
                shopEntity.setId(entryInfo.getId());
                shopEntity.setOrderId(order.getId());
                shopEntity.setModel(order.getModel());
                bodyShopStartWorkIn(plan.getAttribute7(), shopEntity, workstationCode, "");
            }
        }
    }

    /**
     * 焊装车间开工并下发(指定计划号)
     *
     * @param model
     * @param workstationCode
     * @param tagNo
     */
    @Override
    public OrderEntryInfo bodyShopStartWorkByPlan(String tagNo, String workstationCode, String model,String planNo) {
        if (!StringUtils.isBlank(planNo)) {
            OrderEntryInfo shopEntity = null;
            PpsOrderEntity order = ppsOrderService.getByPlanNo(planNo);
            if (order == null) {
                throw new InkelinkException("没有计划号【" + planNo + "】对应的订单信息");
            }
            PpsEntryEntity entryInfo = ppsEntryService.getFirstByOrderShopCode(order.getId(), "WE");
            if (entryInfo == null) {
                throw new InkelinkException("没有计划号【" + planNo + "】对应的焊装工单信息");
            }
            shopEntity = new OrderEntryInfo();
            shopEntity.setId(entryInfo.getId());
            shopEntity.setOrderId(order.getId());
            shopEntity.setModel(order.getModel());
            return bodyShopStartWorkIn(tagNo, shopEntity, workstationCode, model);
        } else {
            throw new InkelinkException("没有计划信息");
        }
    }

    /**
     * 焊装车间开工并下发
     *
     * @param model
     * @param workstationCode
     * @param tagNo
     */
    @Override
    public OrderEntryInfo bodyShopStartWork(String tagNo, String workstationCode, String model) {
        return bodyShopStartWorkIn(tagNo, null, workstationCode, model);
    }

    public void createBln(List<String> planos) {
        for (String planno : planos) {
            PpsOrderEntity ppsOrderInfo = ppsOrderService.getByPlanNo(planno);
            PpsEntryEntity shopEntity = ppsEntryService.getFirstByOrderShopCode(ppsOrderInfo.getId(),"WE");
            List<PpsEntryConfigEntity> lineEntityConfigs = ppsEntryConfigService.getPpsEntryConfigListByShopCode("WE", 1, shopEntity.getModel());
            PpsEntryEntity entryInfo = ppsEntryService.get(shopEntity.getId());
            // entryInfo.setSn(ppsOrderInfo.getSn());
            for (PpsEntryConfigEntity entityConfig : lineEntityConfigs) {
                ppsEntryService.createBranchEntry(entryInfo, entityConfig);
            }
        }
        ppsEntryService.saveChange();
    }

    /**
     * 焊装车间开工并下发
     *
     * @param model
     * @param workstationCode
     * @param tagNo
     */
    private OrderEntryInfo bodyShopStartWorkIn(String tagNo, OrderEntryInfo shopEntity , String workstationCode, String model) {
        //OrderEntryInfo shopEntity = null;
        String shopCode = "WE";
        PpsOrderEntity ppsOrderInfo = null;
        synchronized (bodyShopStartlock) {
            //获取车间工单第一条待生产的工单
            if(shopEntity==null) {
                List<ConditionDto> conditions = new ArrayList<>();
                conditions.add(new ConditionDto("workshopCode", shopCode, ConditionOper.Equal));
                conditions.add(new ConditionDto("entryType", "1", ConditionOper.Equal));
                conditions.add(new ConditionDto("status", "2", ConditionOper.Equal));
                conditions.add(new ConditionDto("orderCategory", "1", ConditionOper.Equal));

                List<SortDto> sorts = new ArrayList<>();
                sorts.add(new SortDto("displayNo", ConditionDirection.ASC));

                PageData<OrderEntryInfo> page = new PageData<>();
                page.setPageIndex(1);
                page.setPageSize(5);
                ppsEntryService.getShopOrders(conditions, sorts, page);
                shopEntity = page.getDatas() == null ? null : page.getDatas().stream().findFirst().orElse(null);
                //shopEntity = ppsEntryService.getShopEntrys(shopCode, 1, model).stream().findFirst().orElse(null);
            }
            if (shopEntity == null) {
                throw new InkelinkException("没有可生产的订单");
            }
            //生成VIN号
            //获取订单信息
            ppsOrderInfo = ppsOrderService.get(shopEntity.getOrderId());

            //生成vin
            if (StringUtils.isBlank(ppsOrderInfo.getBarcode())) {
                ppsOrderInfo.setBarcode(ppsVtVinRuleService.createVin(ppsOrderInfo));
                UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
                upOrder.lambda().set(PpsOrderEntity::getBarcode, ppsOrderInfo.getBarcode())
                        .eq(PpsOrderEntity::getId, shopEntity.getOrderId());
                ppsOrderService.update(upOrder);
            }
            //生成车身号（开工生成VIN，SN==VIN）
            if (StringUtils.isBlank(ppsOrderInfo.getSn())) {
                //是否重复
                QueryWrapper<PpsOrderEntity> cntOrder = new QueryWrapper<>();
                cntOrder.lambda().eq(PpsOrderEntity::getSn, ppsOrderInfo.getBarcode());
                if(ppsOrderService.selectCount(cntOrder)>0) {
                    throw new InkelinkException("VIN号重复：" + ppsOrderInfo.getBarcode());
                }
                ppsOrderInfo.setSn(ppsOrderInfo.getBarcode());
                UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
                upOrder.lambda().set(PpsOrderEntity::getSn, ppsOrderInfo.getSn())
                        .eq(PpsOrderEntity::getId, shopEntity.getOrderId());
                ppsOrderService.update(upOrder);
            }
            //绑定吊牌
            ppsBindingTagService.bindingTag(tagNo, ppsOrderInfo.getBarcode(), workstationCode, 1);
            //更新车间工单状态
            UpdateWrapper<PpsEntryEntity> upEntry = new UpdateWrapper<>();
            upEntry.lambda().set(PpsEntryEntity::getStatus, 3)
                    .eq(PpsEntryEntity::getId, shopEntity.getId());
            ppsEntryService.update(upEntry);

            UpdateWrapper<PpsEntryEntity> upEntrySn = new UpdateWrapper<>();
            upEntrySn.lambda().set(PpsEntryEntity::getSn, ppsOrderInfo.getSn())
                    .eq(PpsEntryEntity::getPrcPpsOrderId, ppsOrderInfo.getId());
            ppsEntryService.update(upEntrySn);

            // 创建分线工单
            List<PpsEntryConfigEntity> lineEntityConfigs = ppsEntryConfigService.getPpsEntryConfigListByShopCode(shopCode, 1, shopEntity.getModel());
            PpsEntryEntity entryInfo = ppsEntryService.get(shopEntity.getId());
            entryInfo.setSn(ppsOrderInfo.getSn());
            for (PpsEntryConfigEntity entityConfig : lineEntityConfigs) {
                ppsEntryService.createBranchEntry(entryInfo, entityConfig);
            }

            shopEntity.setSn(ppsOrderInfo.getSn());
            shopEntity.setBarcode(ppsOrderInfo.getBarcode());
            ppsEntryService.saveChange();

            //开工复制焊装生产顺序号（不自动生产的原因是 有多个开工点 ，只能遵照锁定工单的顺序）---需要通过过点生成
//            String bodyNo = StringUtils.leftPad(entryInfo.getDisplayNo().toString(), 10, '0');
//            UpdateWrapper<PpsOrderEntity> upOrderNo = new UpdateWrapper<>();
//            upOrderNo.lambda().set(PpsOrderEntity::getBodyNo, bodyNo)
//                    .eq(PpsOrderEntity::getId, shopEntity.getOrderId());
//            ppsOrderService.update(upOrderNo);

            ppsOrderService.generateSequenceNoForOrder(ppsOrderInfo.getSn(), shopCode);
            ppsOrderService.saveChange();
            //发送AS待开工队列
            sendAsQueueStartMessage(ppsOrderInfo.getSn());
            //lms下发整车锁定计划
            sendLmsVeLockPlan(ppsOrderInfo.getSn());

        }

        return shopEntity;
    }

    /**
     * 其他车间线体开工
     * */
    private void startWorkOther(OrderEntryInfo shopEntity ){
        PpsOrderEntity ppsOrderInfo = ppsOrderService.get(shopEntity.getOrderId());
        //生成VIN号
        if (StringUtils.isBlank(ppsOrderInfo.getBarcode())) {
            ppsOrderInfo.setBarcode(ppsVtVinRuleService.createVin(ppsOrderInfo));
            UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
            upOrder.lambda().set(PpsOrderEntity::getBarcode, ppsOrderInfo.getBarcode())
                    .eq(PpsOrderEntity::getId, shopEntity.getOrderId());
            ppsOrderService.update(upOrder);
        }
        //生成车身号（开工生成VIN，SN==VIN）
        if (StringUtils.isBlank(ppsOrderInfo.getSn())) {
            //是否重复
            QueryWrapper<PpsOrderEntity> cntOrder = new QueryWrapper<>();
            cntOrder.lambda().eq(PpsOrderEntity::getSn, ppsOrderInfo.getBarcode());
            if(ppsOrderService.selectCount(cntOrder)>0) {
                throw new InkelinkException("VIN号重复：" + ppsOrderInfo.getBarcode());
            }
            ppsOrderInfo.setSn(ppsOrderInfo.getBarcode());
            UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
            upOrder.lambda().set(PpsOrderEntity::getSn, ppsOrderInfo.getSn())
                    .eq(PpsOrderEntity::getId, shopEntity.getOrderId());
            ppsOrderService.update(upOrder);
        }
        //绑定吊牌
        // ppsBindingTagService.bindingTag(tagNo, ppsOrderInfo.getBarcode(), workstationCode, 1);
        //更新车间工单状态
        /*UpdateWrapper<PpsEntryEntity> upEntry = new UpdateWrapper<>();
        upEntry.lambda().set(PpsEntryEntity::getStatus, 3)
                .eq(PpsEntryEntity::getId, shopEntity.getId());
        ppsEntryService.update(upEntry);*/

        UpdateWrapper<PpsEntryEntity> upEntrySn = new UpdateWrapper<>();
        upEntrySn.lambda().set(PpsEntryEntity::getSn, ppsOrderInfo.getSn())
                .eq(PpsEntryEntity::getPrcPpsOrderId, ppsOrderInfo.getId());
        ppsEntryService.update(upEntrySn);

        //ppsOrderService.generateSequenceNoForOrder(ppsOrderInfo.getSn(), shopCode);
        ppsOrderService.saveChange();
        //发送AS待开工队列
        sendAsQueueStartMessage(ppsOrderInfo.getSn());
        //lms下发整车锁定计划
        sendLmsVeLockPlan(ppsOrderInfo.getSn());
    }

    /**
     * 发送AS待开工队列
     *
     * @param vin 车辆唯一标识
     */
    @Override
    public void sendAsQueueStartMessage(String vin) {
        PpsOrderEntity ppsOrderInfo = ppsOrderService.getPpsOrderBySn(vin);
        if (ppsOrderInfo == null) {
            logger.error("发送AS待开工队列异常,vin码:" + vin + ",查询订单为空");
            return;
        }
        //在队列中获取
        //String orgCode = pmOrgProvider.getCurrentOrgCode();
        AsQueueStartDto info = new AsQueueStartDto();
        info.setBeforeOnlineSeq(ppsOrderInfo.getBodyNo());
        info.setVin(vin);
        info.setOrgCode("");
        info.setVrn(ppsOrderInfo.getPlanNo());
        SysQueueNoteEntity sysQueueNoteEntity = new SysQueueNoteEntity();
        sysQueueNoteEntity.setGroupName(RabbitMQConstants.GROUP_NAME_AS_QUEUESTART_QUEUE);
        sysQueueNoteEntity.setContent(JsonUtils.toJsonString(info));
        sysQueueNoteService.addSimpleMessage(sysQueueNoteEntity);
    }

    /**
     * 发送Lms整车锁定计划
     *
     * @param vin 车辆唯一标识
     */
    @Override
    public void sendLmsVeLockPlan(String vin) {
        PpsOrderEntity ppsOrderInfo = ppsOrderService.getPpsOrderBySn(vin);
        if (ppsOrderInfo == null) {
            logger.error("发送Lms整车锁定计划异常,vin码:" + vin + ",查询订单为空");
            return;
        }
        //String orgCode = pmOrgProvider.getCurrentOrgCode();
        String aviCode = ppsOrderInfo.getStartAvi();
        if (StringUtils.isBlank(aviCode)) {
            aviCode = "EH001";
        }
        LmsLockPlanDto info = new LmsLockPlanDto();
        //info.setOrgCode(orgCode);
        //info.setShopCode(shopCode);
        //info.setLineCode(llineCode);
        info.setAviCode(aviCode);
        info.setVin(vin);
        info.setProductType("1");
        info.setProductCode(ppsOrderInfo.getProductCode());
        info.setPlanNo(ppsOrderInfo.getPlanNo());
        info.setOneId("");
        info.setManager("");
        info.setPassTime(new Date());
        info.setUniqueCode(ppsOrderInfo.getId());
        sendLmsLockPlan(info);
    }


    /**
     * 发送Lms锁定计划
     *
     * @param info
     */
    @Override
    public void sendLmsLockPlan(LmsLockPlanDto info) {
        SysQueueNoteEntity sysQueueNoteEntity = new SysQueueNoteEntity();
        sysQueueNoteEntity.setGroupName(RabbitMQConstants.GROUP_NAME_LMS_LOCKPLAN_QUEUE);
        sysQueueNoteEntity.setContent(JsonUtils.toJsonString(info));
        sysQueueNoteService.addSimpleMessage(sysQueueNoteEntity);
    }

    /**
     * 电池车间开工下发
     */
    @Override
    public OrderEntryInfo packShopStartWork(String model) {
        synchronized (baShopStartlock) {
            String shopCode = "BA";
            //获取车间工单第一条待生产的工单
            OrderEntryInfo shopEntity = ppsEntryService.getShopEntrys(shopCode, 1, model).stream().findFirst().orElse(null);
            if (shopEntity == null) {
                throw new InkelinkException("没有可生产的订单");
            }
            //生成VIN号
            //获取订单信息
            PpsOrderEntity ppsOrderInfo = ppsOrderService.get(shopEntity.getOrderId());

            //生成vin
            if (StringUtils.isBlank(ppsOrderInfo.getBarcode())) {
                ppsOrderInfo.setBarcode(ppsRinStandardConfigService.generateRin(ppsOrderInfo.getModel()));
                UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
                upOrder.lambda().set(PpsOrderEntity::getBarcode, ppsOrderInfo.getBarcode())
                        .eq(PpsOrderEntity::getId, shopEntity.getOrderId());
                ppsOrderService.update(upOrder);
            }

            //生成车身号（开工生成VIN，SN==VIN）
            if (StringUtils.isBlank(ppsOrderInfo.getSn())) {
                ppsOrderInfo.setSn(ppsOrderInfo.getBarcode());
                UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
                upOrder.lambda().set(PpsOrderEntity::getSn, ppsOrderInfo.getSn())
                        .eq(PpsOrderEntity::getId, shopEntity.getOrderId());
                ppsOrderService.update(upOrder);
            }

            //开工赋值生产顺序号（不自动生产的原因是 有多个开工点 ，只能遵照锁定工单的顺序）
            String productionNo = StringUtils.leftPad(shopEntity.getDisplayNo().toString(), 10, '0');
            //考虑可能有多个开工点，所以顺序号要已锁定顺序号
            UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
            upOrder.lambda().set(PpsOrderEntity::getProductionNo, productionNo)
                    .eq(PpsOrderEntity::getId, ppsOrderInfo.getId());
            ppsOrderService.update(upOrder);

            UpdateWrapper<PpsEntryEntity> upEntry = new UpdateWrapper<>();
            upEntry.lambda().set(PpsEntryEntity::getSn, ppsOrderInfo.getBarcode())
                    .eq(PpsEntryEntity::getPrcPpsOrderId, ppsOrderInfo.getId());
            ppsEntryService.update(upEntry);
            PpsProductProcessEntity processInfo = ppsProductProcessService.getAllDatas().stream()
                    .filter(c -> Objects.equals(c.getId(), ppsOrderInfo.getPrcPpsProductProcessId()))
                    .findFirst().orElse(null);
            if (processInfo == null) {
                throw new InkelinkException("电池订单" + ppsOrderInfo.getOrderNo() + "未关联上工艺路径");
            }
            PpsProductProcessAviEntity aviInfo = processInfo.getPpsProductProcessAviInfos().stream().filter(c -> c.getAviType() == 21)
                    .findFirst().orElse(null);
            if (aviInfo == null) {
                throw new InkelinkException("工艺路径上未配置开工站点");
            }
            ppsOrderService.saveChange();

            // 创建分线工单(生产批量工单)
            List<PpsEntryConfigEntity> lineEntityConfigs = ppsEntryConfigService.getPpsEntryConfigListByShopCode(shopCode, 1, shopEntity.getModel());
            PpsEntryEntity fristEntity = ppsEntryService.getFirstByOrderId(ppsOrderInfo.getId());
            fristEntity.setSn(ppsOrderInfo.getBarcode());
            for (PpsEntryConfigEntity entityConfig : lineEntityConfigs) {
                ppsEntryService.createBranchEntry(fristEntity, entityConfig);
            }

            ppsEntryService.changeEntryStatus(ppsOrderInfo.getBarcode(), aviInfo.getAviCode(), new Date());
            ppsOrderService.saveChange();

            shopEntity.setSn(ppsOrderInfo.getSn());
            shopEntity.setBarcode(ppsOrderInfo.getBarcode());
            return shopEntity;
        }
    }

    /**
     * 获取工艺路径关联的需要验证顺序的AVI
     *
     * @param barcode
     * @return
     */
    @Override
    public List<PpsProductProcessAviEntity> getSequenceAviList(String barcode) {
        List<PpsProductProcessAviEntity> datas = new ArrayList<>();
        PpsEntryEntity entry = ppsEntryService.getFirstBySn(barcode);
        if (entry != null) {
            Long processId = 0L;
            PpsOrderEntity order = ppsOrderService.get(entry.getPrcPpsOrderId());
            if (order != null) {
                processId = order.getPrcPpsProductProcessId();
            }
            Long finalProcessId = processId;
            PpsProductProcessEntity process = ppsProductProcessService.getAllDatas().stream().filter(o ->
                            Objects.equals(o.getId(), finalProcessId))
                    .findFirst().orElse(null);
            if (process != null && process.getPpsProductProcessAviInfos() != null) {
                datas = process.getPpsProductProcessAviInfos().stream().filter(o -> !StringUtils.isBlank(o.getAviCode())
                                && o.getIsSequence()).sorted(Comparator.comparingInt(PpsProductProcessAviEntity::getDisplayNo))
                        .collect(Collectors.toList());
            }
        }
        return datas;
    }

    /**
     * 获取工艺路径线体
     *
     * @param barcode
     * @return
     */
    @Override
    public List<PpsProductProcessAviEntity> getIsMainAviList(String barcode) {
        List<PpsProductProcessAviEntity> datas = new ArrayList<>();
        PpsEntryEntity entry = ppsEntryService.getFirstBySn(barcode);
        if (entry != null) {
            Long processId = 0L;
            if (entry.getEntrySource() == 1) {
                PpsOrderEntity order = ppsOrderService.get(entry.getPrcPpsOrderId());
                if (order != null) {
                    processId = order.getPrcPpsProductProcessId();
                }
            }
            Long finalProcessId = processId;
            PpsProductProcessEntity process = ppsProductProcessService.getAllDatas().stream().filter(o ->
                            Objects.equals(o.getId(), finalProcessId))
                    .findFirst().orElse(null);
            if (process != null && process.getPpsProductProcessAviInfos() != null) {
                datas = process.getPpsProductProcessAviInfos().stream().filter(o -> !StringUtils.isBlank(o.getAviCode()))
                        .sorted(Comparator.comparingInt(PpsProductProcessAviEntity::getDisplayNo))
                        .collect(Collectors.toList());
            }
        }
        return datas;
    }

    /**
     * 判断产品物料是否存在
     *
     * @param orderId
     * @param materialNo
     * @return Boolean
     */
    @Override
    public Boolean existProductBomMaterialNo(Long orderId, String materialNo) {
        boolean exsitsMaterialNo = false;
        PmProductBomEntity datas = ppsOrderService.getOrderBomByOrderId(orderId).stream().filter(c -> StringUtils.startsWith(c.getMaterialNo(), materialNo)).findFirst().orElse(null);
        if (datas != null) {
            exsitsMaterialNo = true;
        }
        return exsitsMaterialNo;
    }

    /** ----------- 整车 结束---------------- */

    /**
     * 预成组(start)
     */

    /**
     * 获取模组生产工单(修改wdl)
     *
     * @param lineCode
     */
    @Override
    public DownModuleEntryInfo getModuleEntry(String lineCode) {
        synchronized (moduleEntrylock) {
            //c.LineCode.Contains(lineCode)  有些下发数据两条线体都可以生产
            String entryNo = ppsModuleIssueModuleService.getEntryNoByLineCode(lineCode);
            if (StringUtils.isBlank(entryNo)) {
                //需要重新拆分工单
                return null;
            }
            //获取需要下发的模组集合
            List<PpsModuleIssueModuleEntity> modules = ppsModuleIssueModuleService.getListByLineEntryCode(entryNo, lineCode);
            //获取需要下发的主数据对象
            PpsModuleIssueMainEntity mainInfo = ppsModuleIssueMainService.getFirstByEntryNo(modules.get(0).getEntryNo());
            //封装下发对象
            DownModuleEntryInfo downModuleEntryInfo = new DownModuleEntryInfo();
            downModuleEntryInfo.setPlanNo(modules.get(0).getPlanNo());
            downModuleEntryInfo.setEntryNo(modules.get(0).getEntryNo());
            downModuleEntryInfo.setPackModel(modules.get(0).getPackModel());
            downModuleEntryInfo.setPackCount(mainInfo.getPackNum());
            downModuleEntryInfo.setAreaCode(mainInfo.getAreaCode());

            for (PpsModuleIssueModuleEntity module : modules) {
                ModuleStructDto structInfo = new ModuleStructDto();
                structInfo.setLineCode(module.getLineCode());
                structInfo.setModuleCode(module.getModuleCode());
                //structInfo.setModuleNum(module.getModuleNum());  TODO mdl
                //List<PpsModuleIssueSpacerEntity> spacesDatas = ppsModuleIssueSpacerService.getByModuleIssueModuleId(module.getId());
//                if (!spacesDatas.isEmpty()) {
//                    structInfo.setSpacers(spacesDatas.stream().map(c -> {
//                        ModuleSpacerDto dto = new ModuleSpacerDto();
//                        dto.setSpacerNum(c.getSpacerNum());
//                        dto.setSpacerCode(c.getSpacerCode());
//                        return dto;
//                    }).collect(Collectors.toList()));
//                } else {
//                    structInfo.setSpacers(new ArrayList<>());
//                }
                structInfo.setSpacers(new ArrayList<>());
                List<PpsModuleIssueUnitEntity> unitsDatas = ppsModuleIssueUnitService.getByModuleIssueModuleId(module.getId());
                if (!unitsDatas.isEmpty()) {
                    structInfo.setUnits(unitsDatas.stream().map(c -> {
                        ModuleUnitDto dto = new ModuleUnitDto();
                        dto.setUnitCode(c.getUnitCode());

                        dto.setCellCode(c.getCellCode());
                        dto.setCellNum(c.getCellNum());
                        dto.setUpBoardCode(c.getUpBoardCode());
                        dto.setDownBoardCode(c.getDownBoardCode());
                        //dto.setUnitNum(c.getUnitNum());
                        //dto.setUpBoardNum(c.getUpBoardNum()); TODO wdl
                        //dto.setDownBoardNum(c.getDownBoardNum());
                        return dto;
                    }).collect(Collectors.toList()));
                } else {
                    structInfo.setUnits(new ArrayList<>());
                }
//                UpdateWrapper<PpsModuleIssueSpacerEntity> upIssueSpacer = new UpdateWrapper<>();
//                upIssueSpacer.lambda().set(PpsModuleIssueSpacerEntity::getLineCode, lineCode)
//                        .eq(PpsModuleIssueSpacerEntity::getPrcPpsModuleIssueModuleId, module.getId());
//                ppsModuleIssueSpacerService.update(upIssueSpacer);

                UpdateWrapper<PpsModuleIssueUnitEntity> upIssueUnit = new UpdateWrapper<>();
                upIssueUnit.lambda().set(PpsModuleIssueUnitEntity::getLineCode, lineCode)
                        .eq(PpsModuleIssueUnitEntity::getPrcPpsModuleIssueModuleId, module.getId());
                ppsModuleIssueUnitService.update(upIssueUnit);

                downModuleEntryInfo.getModules().add(structInfo);
            }

            PpsEntryPartsEntity entryInfo = ppsEntryPartsService.getFirstByEntryNo(entryNo);
            if (entryInfo.getActualStartDt() == null) {
                UpdateWrapper<PpsEntryPartsEntity> upEntryPart = new UpdateWrapper<>();
                upEntryPart.lambda().set(PpsEntryPartsEntity::getActualStartDt, new Date())
                        .set(PpsEntryPartsEntity::getStatus, 10)
                        .eq(PpsEntryPartsEntity::getId, entryInfo.getId());
                ppsEntryPartsService.update(upEntryPart);
            }

            //更新模组下发状态
            List<Long> ids = modules.stream().map(c -> c.getId()).collect(Collectors.toList());
            UpdateWrapper<PpsModuleIssueModuleEntity> upIssueModule = new UpdateWrapper<>();
            upIssueModule.lambda().set(PpsModuleIssueModuleEntity::getLineCode, lineCode)
                    .set(PpsModuleIssueModuleEntity::getStatus, 1)
                    .in(PpsModuleIssueModuleEntity::getId, ids);
            ppsModuleIssueModuleService.update(upIssueModule);

            //更新主对象下发状态
            int num = ids.size();
            QueryWrapper<PpsModuleIssueModuleEntity> qryIssModule = new QueryWrapper<>();
            qryIssModule.lambda().eq(PpsModuleIssueModuleEntity::getEntryNo, entryNo)
                    .notIn(PpsModuleIssueModuleEntity::getId, ids)
                    .lt(PpsModuleIssueModuleEntity::getStatus, 2);
            if (ppsModuleIssueModuleService.selectCount(qryIssModule) == 0) {
                UpdateWrapper<PpsModuleIssueMainEntity> upIssueMain = new UpdateWrapper<>();
                upIssueMain.lambda().setSql(" FINISH_ISSUE_NUM = FINISH_ISSUE_NUM + " + num)
                        .set(PpsModuleIssueMainEntity::getStatus, 2)
                        .eq(PpsModuleIssueMainEntity::getId, mainInfo.getId());
                ppsModuleIssueMainService.update(upIssueMain);
            } else {
                UpdateWrapper<PpsModuleIssueMainEntity> upIssueMain = new UpdateWrapper<>();
                upIssueMain.lambda().setSql(" FINISH_ISSUE_NUM = FINISH_ISSUE_NUM + " + num)
                        .set(PpsModuleIssueMainEntity::getStatus, 1)
                        .eq(PpsModuleIssueMainEntity::getId, mainInfo.getId());
                ppsModuleIssueMainService.update(upIssueMain);
            }
            ppsModuleIssueMainService.saveChange();
            return downModuleEntryInfo;
        }
    }

    /**
     * 拆分模组工单(修改wdl)
     *
     * @param planNo
     */
    @Override
    public void splitModuleEntry(String planNo) {
        synchronized (splitModulelock) {
            //获取模组拆分数量
            SysConfigurationEntity numConfig = sysConfigurationProvider.getSysConfigurations("SplitModuleNum").stream().findFirst().orElse(null);
            if (numConfig == null) {
                throw new InkelinkException("未配置模组拆分数量");
            }
            int splitNum = Integer.parseInt(numConfig.getValue());
            PpsPlanPartsEntity planInfo = null;
            //是否指定订单进行强行拆分
            if (!StringUtils.isBlank(planNo)) {
                planInfo = ppsPlanPartsService.getFirstByPlanNo(planNo);

                if (planInfo.getPlanStatus() == 0) {
                    throw new InkelinkException("计划" + planNo + "未锁定无法拆分");
                }
                if (planInfo.getIsFreeze()) {
                    throw new InkelinkException("计划" + planNo + "已冻结无法拆分");
                }
                if (planInfo.getPlanQty() <= planInfo.getLockQty()) {
                    throw new InkelinkException("计划" + planNo + "数量已拆分完");
                }
                //判断拆分数量是否大于订单剩余数量
                if (splitNum > (planInfo.getPlanQty() - planInfo.getLockQty())) {
                    splitNum = planInfo.getPlanQty() - planInfo.getLockQty();
                }
                if (splitNum <= 0) {
                    throw new InkelinkException("计划" + planNo + "计划数量和已拆分数量有异常");
                }
            } else {
                //根据顺序获取还未拆分完的订单
                List<PpsPlanPartsEntity> planList = ppsPlanPartsService.getListNoCompele(8);
                for (PpsPlanPartsEntity plan : planList) {
                    if ((plan.getPlanQty() - plan.getLockQty()) >= splitNum) {
                        planInfo = plan;
                        break;
                    }
                }
            }
            if (planInfo == null) {
                return;
            }
            if (planInfo.getActualStartDt() == null) {
                UpdateWrapper<PpsPlanPartsEntity> upPlan = new UpdateWrapper<>();
                upPlan.lambda().set(PpsPlanPartsEntity::getActualStartDt, new Date())
                        .eq(PpsPlanPartsEntity::getId, planInfo.getId());
                ppsPlanPartsService.update(upPlan);
            }
            //更新订单拆分数量
            UpdateWrapper<PpsPlanPartsEntity> upPlanLockQty = new UpdateWrapper<>();
            upPlanLockQty.lambda().setSql(" LOCK_QTY = LOCK_QTY + " + splitNum)
                    .set(PpsPlanPartsEntity::getPlanStatus, 10)
                    .eq(PpsPlanPartsEntity::getId, planInfo.getId());
            ppsPlanPartsService.update(upPlanLockQty);

            //获取电池对应的拆分计划
            PpsPlanPartsEntity finalPlanInfo = planInfo;
            PpsModuleSplitStrategyEntity splitStrategyInfo = ppsModuleSplitStrategyService.getAllDatas()
                    .stream().filter(c -> StringUtils.equals(c.getPackModel(), finalPlanInfo.getModel())
                            && c.getIsEnable()).findFirst().orElse(null);
            if (splitStrategyInfo == null) {
                throw new InkelinkException("计划对应的电池型号" + planInfo.getModel() + "未找到对应的下发策略");
            }
            String key = "MODULE";
            //创建生产工单
            String entryNo = key + DateUtils.format(new Date(), "yyyyMMdd")
                    + sysSnConfigProvider.createSn("PpsEntry_" + key + "_Code");
            PpsEntryPartsEntity entryInfo = new PpsEntryPartsEntity();
            entryInfo.setEntryNo(entryNo);
            entryInfo.setEstimatedStartDt(planInfo.getEstimatedStartDt());
            entryInfo.setEstimatedEndDt(planInfo.getEstimatedEndDt());
            entryInfo.setIsSend(false);
            entryInfo.setLineCode(StringUtils.EMPTY);
            entryInfo.setLineName(StringUtils.EMPTY);
            entryInfo.setMaterialCn(planInfo.getMaterialCn());
            entryInfo.setMaterialNo(planInfo.getMaterialNo());
            entryInfo.setModel(planInfo.getModel());
            entryInfo.setOrderCategory(8);
            entryInfo.setPlanNo(planInfo.getPlanNo());
            entryInfo.setPlanQuantity(splitNum);
            entryInfo.setProcessCode(StringUtils.EMPTY);
            entryInfo.setProcessName(StringUtils.EMPTY);
            entryInfo.setQualifiedQuantity(0);
            entryInfo.setScrapQuantity(0);
            entryInfo.setIsFreeze(false);
            entryInfo.setStatus(1);
            entryInfo.setSubtractQuantity(0);
            entryInfo.setWorkshopCode("BA");
            ppsEntryPartsService.insert(entryInfo);

            //下发主数据封装
            PpsModuleIssueMainEntity mainInfo = new PpsModuleIssueMainEntity();
            mainInfo.setId(IdGenerator.getId());
            mainInfo.setPlanNo(planInfo.getPlanNo());
            mainInfo.setStrategySign(splitStrategyInfo.getStrategySign());
            mainInfo.setEntryNo(entryNo);
            mainInfo.setPackModel(planInfo.getModel());
            mainInfo.setPackNum(splitNum);
            mainInfo.setAreaCode(splitStrategyInfo.getAreaCode());
            mainInfo.setIssueNum(splitStrategyInfo.getModules().size());
            ppsModuleIssueMainService.insert(mainInfo);

            //下发的子节点封装
            List<PpsModuleIssueModuleEntity> moduleInfos = new ArrayList<>();
            //List<PpsModuleIssueSpacerEntity> spacerInfos = new ArrayList<>();  TODO mdl
            List<PpsModuleIssueUnitEntity> unitInfos = new ArrayList<>();
            List<PpsModuleReportEntity> moduleReports = new ArrayList<>();
            //拆分预报工单（模组维度）
            for (int i = 1; i <= splitNum; i++) {
                for (ModuleStructDto itemModule : splitStrategyInfo.getModules()) {
                    for (int o = 0; o < itemModule.getModuleNum(); o++) {
                        PpsModuleReportEntity et = new PpsModuleReportEntity();
                        et.setPpsPlanNo(mainInfo.getPlanNo());
                        et.setEntryNo(mainInfo.getEntryNo());
                        et.setGroupNo(i);
                        et.setAreaCode(splitStrategyInfo.getAreaCode());
                        et.setModuleCode(itemModule.getModuleCode());
                        moduleReports.add(et);
                    }
                }
            }
            ppsModuleReportService.insertBatch(moduleReports);
            //封装模组下发结构
            for (ModuleStructDto itemModule : splitStrategyInfo.getModules()) {
                //封装模组层
                PpsModuleIssueModuleEntity moduleInfo = new PpsModuleIssueModuleEntity();
                moduleInfo.setId(IdGenerator.getId());
                moduleInfo.setPrcPpsModuleIssueMainId(mainInfo.getId());
                moduleInfo.setPlanNo(mainInfo.getPlanNo());
                moduleInfo.setEntryNo(mainInfo.getEntryNo());
                moduleInfo.setPackModel(mainInfo.getPackModel());
                moduleInfo.setLineCode(itemModule.getLineCode());
                moduleInfo.setModuleCode(itemModule.getModuleCode());
                //moduleInfo.setModuleNum(itemModule.getModuleNum() * splitNum);  TODO mdl
                moduleInfos.add(moduleInfo);
                //封装隔热垫  TODO mdl
               /* for (ModuleSpacerDto itemSpacer : itemModule.getSpacers()) {
                    PpsModuleIssueSpacerEntity et = new PpsModuleIssueSpacerEntity();
                    et.setPrcPpsModuleIssueModuleId(moduleInfo.getId());
                    et.setPlanNo(mainInfo.getPlanNo());
                    et.setEntryNo(mainInfo.getEntryNo());
                    et.setSpacerCode(itemSpacer.getSpacerCode());
                    et.setSpacerNum(itemSpacer.getSpacerNum() * splitNum);
                    spacerInfos.add(et);
                }*/
                //封装小单元
                for (ModuleUnitDto itemUnit : itemModule.getUnits()) {
                    PpsModuleIssueUnitEntity et = new PpsModuleIssueUnitEntity();
                    et.setPrcPpsModuleIssueModuleId(moduleInfo.getId());
                    et.setPlanNo(mainInfo.getPlanNo());
                    et.setEntryNo(mainInfo.getEntryNo());
                    et.setUnitCode(itemUnit.getUnitCode());

                    et.setCellCode(itemUnit.getCellCode());
                    et.setCellNum(itemUnit.getCellNum() * splitNum);
                    et.setUpBoardCode(itemUnit.getUpBoardCode());

                    et.setDownBoardCode(itemUnit.getDownBoardCode());

                    //et.setUpBoardNum(itemUnit.getUpBoardNum() * splitNum);
                    //et.setUnitNum(itemUnit.getUnitNum() * splitNum); TODO wdl
                    //et.setDownBoardNum(itemUnit.getDownBoardNum() * splitNum);
                    unitInfos.add(et);
                }
            }
            ppsModuleIssueModuleService.insertBatch(moduleInfos);
            //ppsModuleIssueSpacerService.insertBatch(spacerInfos);  TODO mdl
            ppsModuleIssueUnitService.insertBatch(unitInfos);
            ppsModuleIssueUnitService.saveChange();
        }
    }

    /**
     * 回执下发的模组工单
     *
     * @param para
     */
    @Override
    public void affirmModuleEntry(AffirmModuleEntryPara para) {
        PpsEntryPartsEntity entry = ppsEntryPartsService.getFirstByEntryNo(para.getEntryNo());

        InsertAsAviPointInfo inAvi = new InsertAsAviPointInfo();
        inAvi.setPlanNo(entry.getPlanNo());
        inAvi.setScanType("S");
        inAvi.setQualifiedCount(entry.getPlanQuantity());
        ppsAsAviPointService.insertAsAviPoint(inAvi);

        UpdateWrapper<PpsModuleIssueModuleEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(PpsModuleIssueModuleEntity::getStatus, 2)
                .eq(PpsModuleIssueModuleEntity::getEntryNo, para.getEntryNo())
                .eq(PpsModuleIssueModuleEntity::getLineCode, para.getLineCode())
                .eq(PpsModuleIssueModuleEntity::getStatus, 1);
        ppsModuleIssueModuleService.update(upset);
    }

    /**
     * 删除工单业务(修改wdl)
     *
     * @param entryNo
     */
    @Override
    public void deleteModuleEntry(String entryNo) {
        //获取工单对象
        QueryWrapper<PpsEntryPartsEntity> qryParts = new QueryWrapper<>();
        qryParts.lambda().eq(PpsEntryPartsEntity::getEntryNo, entryNo)
                .lt(PpsEntryPartsEntity::getStatus, 20);
        PpsEntryPartsEntity entryInfo = ppsEntryPartsService.getTopDatas(1, qryParts).stream().findFirst().orElse(null);
        if (entryInfo == null) {
            throw new InkelinkException("工单已完成，无法删除工单");
        }
        //删除对应的工单
        ppsEntryPartsService.delete(entryInfo.getId());
        //更新订单拆分数量
        UpdateWrapper<PpsPlanPartsEntity> upPlan = new UpdateWrapper<>();
        upPlan.lambda().setSql(" LOCK_QTY = LOCK_QTY - " + entryInfo.getPlanQuantity())
                .eq(PpsPlanPartsEntity::getPlanNo, entryInfo.getPlanNo());
        ppsPlanPartsService.update(upPlan);

        //删除工单对应的模组报工单
        UpdateWrapper<PpsModuleReportEntity> upModuleReport = new UpdateWrapper<>();
        upModuleReport.lambda().eq(PpsModuleReportEntity::getEntryNo, entryInfo.getEntryNo());
        ppsModuleReportService.delete(upModuleReport);
        //删除工单对应的下发结构
        UpdateWrapper<PpsModuleIssueMainEntity> upIssueMain = new UpdateWrapper<>();
        upIssueMain.lambda().eq(PpsModuleIssueMainEntity::getEntryNo, entryNo);
        ppsModuleIssueMainService.delete(upIssueMain);
        UpdateWrapper<PpsModuleIssueModuleEntity> upIssueModule = new UpdateWrapper<>();
        upIssueModule.lambda().eq(PpsModuleIssueModuleEntity::getEntryNo, entryNo);
        ppsModuleIssueModuleService.delete(upIssueModule);
        //TODO wdl
        /*UpdateWrapper<PpsModuleIssueSpacerEntity> upIssueSpacer = new UpdateWrapper<>();
        upIssueSpacer.lambda().eq(PpsModuleIssueSpacerEntity::getEntryNo, entryNo);
        ppsModuleIssueSpacerService.delete(upIssueSpacer);*/
        UpdateWrapper<PpsModuleIssueUnitEntity> upIssueUnit = new UpdateWrapper<>();
        upIssueUnit.lambda().eq(PpsModuleIssueUnitEntity::getEntryNo, entryNo);
        ppsModuleIssueUnitService.delete(upIssueUnit);
    }

    /**
     * 模组报工
     *
     * @param para
     */
    @Override
    public void moduleReport(ModuleReportPara para) {
        synchronized (moduleReportlock) {
            //1、寻找对应的模组预报工单
            QueryWrapper<PpsModuleReportEntity> qryReport = new QueryWrapper<>();
            qryReport.lambda().eq(PpsModuleReportEntity::getModuleCode, para.getModuleCode())
                    .eq(PpsModuleReportEntity::getReportStatus, 0)
                    .orderByAsc(PpsModuleReportEntity::getCreationDate);
            PpsModuleReportEntity reportInfo = ppsModuleReportService.getTopDatas(1, qryReport)
                    .stream().findFirst().orElse(null);
            if (reportInfo == null) {
                throw new InkelinkException("未找到模组" + para.getModuleCode() + "对应的报工单");
            }
            UpdateWrapper<PpsModuleReportEntity> upReport = new UpdateWrapper<>();
            upReport.lambda().set(PpsModuleReportEntity::getLineCode, para.getLineCode())
                    .set(PpsModuleReportEntity::getModuleBarcode, para.getModuleBarcode())
                    .set(PpsModuleReportEntity::getReportStatus, para.getReoprtStatus())
                    .eq(PpsModuleReportEntity::getId, reportInfo.getId());
            ppsModuleReportService.update(upReport);

            //2、统计工单的报工数量 标识完成一个电池包
            QueryWrapper<PpsModuleReportEntity> qryCntReport = new QueryWrapper<>();
            qryCntReport.lambda().eq(PpsModuleReportEntity::getEntryNo, reportInfo.getEntryNo())
                    .eq(PpsModuleReportEntity::getGroupNo, reportInfo.getGroupNo())
                    .eq(PpsModuleReportEntity::getReportStatus, 0)
                    .ne(PpsModuleReportEntity::getId, reportInfo.getId());
            if (ppsModuleReportService.selectCount(qryCntReport) == 0) {
                PpsEntryPartsEntity entryInfo = ppsEntryPartsService.getFirstByEntryNo(reportInfo.getEntryNo());
                //合格数量累加
                if (para.getReoprtStatus() == 1) {
                    UpdateWrapper<PpsEntryPartsEntity> upEntryParts = new UpdateWrapper<>();
                    upEntryParts.lambda().setSql(" QUALIFIED_QUANTITY = QUALIFIED_QUANTITY + " + 1)
                            .eq(PpsEntryPartsEntity::getId, entryInfo.getId());
                    ppsEntryPartsService.update(upEntryParts);

                    InsertAsAviPointInfo inAvi =   new InsertAsAviPointInfo();
                    inAvi.setPlanNo(entryInfo.getPlanNo());
                    inAvi.setScanType("X");
                    ppsAsAviPointService.insertAsAviPoint(inAvi);

                } else {//不合格数量累加
                    UpdateWrapper<PpsEntryPartsEntity> upEntryParts = new UpdateWrapper<>();
                    upEntryParts.lambda().setSql(" SCRAP_QUANTITY = SCRAP_QUANTITY + " + 1)
                            .eq(PpsEntryPartsEntity::getId, entryInfo.getId());
                    ppsEntryPartsService.update(upEntryParts);

                    InsertAsAviPointInfo inAvi = new InsertAsAviPointInfo();
                    inAvi.setPlanNo(entryInfo.getPlanNo());
                    inAvi.setScanType("X");
                    ppsAsAviPointService.insertAsAviPoint(inAvi);
                }
                //判断工单是否都完成
                QueryWrapper<PpsModuleReportEntity> qryReportStats = new QueryWrapper<>();
                qryReportStats.lambda().eq(PpsModuleReportEntity::getEntryNo, reportInfo.getEntryNo())
                        .eq(PpsModuleReportEntity::getReportStatus, 0);
                if (ppsModuleReportService.selectCount(qryReportStats) == 0) {
                    UpdateWrapper<PpsEntryPartsEntity> upEntryParts = new UpdateWrapper<>();
                    upEntryParts.lambda().set(PpsEntryPartsEntity::getStatus, 20)
                            .eq(PpsEntryPartsEntity::getId, entryInfo.getId());
                    ppsEntryPartsService.update(upEntryParts);
                    PpsPlanPartsEntity planInfo = ppsPlanPartsService.getFirstByPlanNo(entryInfo.getPlanNo());
                    //代表计划生产完成
                    if (planInfo.getLockQty() >= planInfo.getPlanQty()) {
                        QueryWrapper<PpsEntryPartsEntity> qryEntryParts = new QueryWrapper<>();
                        qryEntryParts.lambda().lt(PpsEntryPartsEntity::getStatus, 20)
                                .eq(PpsEntryPartsEntity::getPlanNo,entryInfo.getPlanNo());
                        if (ppsEntryPartsService.selectCount(qryEntryParts) == 0) {
                            UpdateWrapper<PpsPlanPartsEntity> upPlanParts = new UpdateWrapper<>();
                            upPlanParts.lambda().set(PpsPlanPartsEntity::getPlanStatus, 20)
                                    .set(PpsPlanPartsEntity::getActualEndDt, new Date())
                                    .eq(PpsPlanPartsEntity::getPlanNo, planInfo.getPlanNo());
                            ppsPlanPartsService.update(upPlanParts);
                            //这里需要加入一个MQ广播
                        }
                    }
                }
            }
            ppsPlanPartsService.saveChange();
        }
    }

    /**
     *  预成组(end)
     */

    @Override
    public void setPlanStart(Long entryId) {
        ppsPlanPartsService.setPlanStart(entryId);
        ppsPlanPartsService.saveChange();
    }

    /**
     * 线体是否在计划履历中
     *
     * */
    @Override
    public Integer hasPlanLine(String lineCode) {
        QueryWrapper<PpsPlanAviEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsPlanAviEntity::getLineCode, lineCode);
        return ppsPlanAviService.selectCount(qry) > 0 ? 1 : 0;
    }
}