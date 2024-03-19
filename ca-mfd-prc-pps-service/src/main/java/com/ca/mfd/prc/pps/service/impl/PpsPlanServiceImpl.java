package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ca.mfd.prc.common.constant.BodyShopCode;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.model.base.dto.TopDataDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.InkelinkExcelUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQConstants;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.SysQueueNoteEntity;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.provider.RabbitMqSysQueueNoteProvider;
import com.ca.mfd.prc.pps.communication.dto.LmsLockPlanDto;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.dto.UpdatePlanVersionsPara;
import com.ca.mfd.prc.pps.entity.PpsEntryConfigEntity;
import com.ca.mfd.prc.pps.entity.PpsEntryEntity;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.entity.PpsPackBranchingPlanConfigEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanAviEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.entity.PpsProductProcessEntity;
import com.ca.mfd.prc.pps.enums.EntryStatusEnum;
import com.ca.mfd.prc.pps.enums.EntryTypeEnum;
import com.ca.mfd.prc.pps.enums.OrderCategoryEnum;
import com.ca.mfd.prc.pps.enums.OrderStatusEnum;
import com.ca.mfd.prc.pps.enums.PlanStatusEnum;
import com.ca.mfd.prc.pps.extend.IPpsOrderExtendService;
import com.ca.mfd.prc.pps.mapper.IPpsPlanMapper;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomVersionsEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductCharacteristicsVersionsEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductBomProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductBomVersionsProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductCharacteristicsVersionsProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductMaterialMasterProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsEntryConfigService;
import com.ca.mfd.prc.pps.service.IPpsEntryService;
import com.ca.mfd.prc.pps.service.IPpsPackBranchingPlanConfigService;
import com.ca.mfd.prc.pps.service.IPpsPlanAviService;
import com.ca.mfd.prc.pps.service.IPpsPlanService;
import com.ca.mfd.prc.pps.service.IPpsProductProcessService;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 生产计划
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PpsPlanServiceImpl extends AbstractCrudServiceImpl<IPpsPlanMapper, PpsPlanEntity> implements IPpsPlanService {

    private static final Logger logger = LoggerFactory.getLogger(PpsPlanServiceImpl.class);

    private static final Object SPLIT_PLAN_LOCK = new Object();
    /***********导入导出*************/

    private final Map<String, Map<String, String>> orderDic = Maps.newHashMapWithExpectedSize(3);
    @Autowired
    private IPpsOrderExtendService ppsOrderExtendService;
    @Autowired
    private IPpsEntryService ppsEntryService;
    @Autowired
    private IPpsEntryConfigService ppsEntryConfigService;
    @Autowired
    private IPpsPlanAviService ppsPlanAviService;
    @Autowired
    private IPpsProductProcessService ppsProductProcessService;
    @Autowired
    private PmProductCharacteristicsVersionsProvider pmProductCharacteristicsVersionsService;
    @Autowired
    private PmProductBomVersionsProvider pmProductBomVersionsProvider;
    @Autowired
    private PmProductBomProvider pmProductBomProvider;
    @Autowired
    private PmProductMaterialMasterProvider pmProductMaterialMasterProvider;
    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;
    @Autowired
    private IMidApiLogService midApiLogService;

    @Autowired
    private IdentityHelper identityHelper;
    @Autowired
    private RabbitMqSysQueueNoteProvider sysQueueNoteService;

    {
        Map<String, String> mpVehicle = new LinkedHashMap<>();
        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getPlanNo), "计划编号");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getProductCode), "整车编码");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getOrderSign), "定编码");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getModel), "车系");

        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getCharacteristic6), "颜色标识");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getCharacteristic2), "车身颜色");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getCharacteristic3), "内饰颜色");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getAttribute1), "选装包");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getCharacteristic7), "车辆状态码");


        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getAttribute6), "VIN号");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getAttribute7), "吊牌号");

        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEstimatedStartDt), "预计上线时间");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEstimatedEndDt), "预计下线时间");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getStartAvi), "开始站点");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEndAvi), "结束站点");

        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getBomVersion), "BOM版本号");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getCharacteristicVersion), "特征版本");

        mpVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getRemark), "备注");
        orderDic.put(OrderSheetTableName.Vehicle, mpVehicle);

        //导入VIN和吊牌号
        Map<String, String> vinVehicle = new LinkedHashMap<>();
        vinVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getPlanNo), "VRN");

        vinVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getAttribute6), "VIN");
        vinVehicle.put(MpSqlUtils.getColumnName(PpsPlanEntity::getAttribute7), "吊牌");
        orderDic.put(OrderSheetTableName.VehicleVin, vinVehicle);

        Map<String, String> mpBattery = new LinkedHashMap<>();
        mpBattery.put(MpSqlUtils.getColumnName(PpsPlanEntity::getPlanNo), "计划编号");
        mpBattery.put(MpSqlUtils.getColumnName(PpsPlanEntity::getProductCode), "电池型号");
        mpBattery.put(MpSqlUtils.getColumnName(PpsPlanEntity::getModel), "电池系列");
        mpBattery.put(MpSqlUtils.getColumnName(PpsPlanEntity::getPlanQty), "数量");
        mpBattery.put(MpSqlUtils.getColumnName(PpsPlanEntity::getDisplayNo), "生产顺序号");
        mpBattery.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEstimatedStartDt), "预计上线时间");
        mpBattery.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEstimatedEndDt), "预计下线时间");
        mpBattery.put(MpSqlUtils.getColumnName(PpsPlanEntity::getStartAvi), "开始站点");
        mpBattery.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEndAvi), "结束站点");
        mpBattery.put(MpSqlUtils.getColumnName(PpsPlanEntity::getRemark), "备注");
        mpBattery.put(MpSqlUtils.getColumnName(PpsPlanEntity::getBomVersion), "BOM版本号");
        orderDic.put(OrderSheetTableName.Battery, mpBattery);

        Map<String, String> mpPressureCasting = new LinkedHashMap<>();
        mpPressureCasting.put(MpSqlUtils.getColumnName(PpsPlanEntity::getPlanNo), "计划编号");
        mpPressureCasting.put(MpSqlUtils.getColumnName(PpsPlanEntity::getProductCode), "产品编码");
        mpPressureCasting.put(MpSqlUtils.getColumnName(PpsPlanEntity::getModel), "产品类型");
        mpPressureCasting.put(MpSqlUtils.getColumnName(PpsPlanEntity::getPlanQty), "数量");
        mpPressureCasting.put(MpSqlUtils.getColumnName(PpsPlanEntity::getDisplayNo), "生产顺序号");
        mpPressureCasting.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEstimatedStartDt), "预计上线时间");
        mpPressureCasting.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEstimatedEndDt), "预计下线时间");
        mpPressureCasting.put(MpSqlUtils.getColumnName(PpsPlanEntity::getStartAvi), "开始站点");
        mpPressureCasting.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEndAvi), "结束站点");
        mpPressureCasting.put(MpSqlUtils.getColumnName(PpsPlanEntity::getRemark), "备注");
        orderDic.put(OrderSheetTableName.PressureCasting, mpPressureCasting);

        Map<String, String> mpMachining = new LinkedHashMap<>();
        mpMachining.put(MpSqlUtils.getColumnName(PpsPlanEntity::getPlanNo), "计划编号");
        mpMachining.put(MpSqlUtils.getColumnName(PpsPlanEntity::getProductCode), "产品编码");
        mpMachining.put(MpSqlUtils.getColumnName(PpsPlanEntity::getModel), "产品类型");
        mpMachining.put(MpSqlUtils.getColumnName(PpsPlanEntity::getPlanQty), "数量");
        mpMachining.put(MpSqlUtils.getColumnName(PpsPlanEntity::getDisplayNo), "生产顺序号");
        mpMachining.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEstimatedStartDt), "预计上线时间");
        mpMachining.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEstimatedEndDt), "预计下线时间");
        mpMachining.put(MpSqlUtils.getColumnName(PpsPlanEntity::getStartAvi), "开始站点");
        mpMachining.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEndAvi), "结束站点");
        mpMachining.put(MpSqlUtils.getColumnName(PpsPlanEntity::getRemark), "备注");
        orderDic.put(OrderSheetTableName.Machining, mpMachining);

        Map<String, String> mpStamping = new LinkedHashMap<>();
        mpStamping.put(MpSqlUtils.getColumnName(PpsPlanEntity::getPlanNo), "计划编号");
        mpStamping.put(MpSqlUtils.getColumnName(PpsPlanEntity::getProductCode), "产品编码");
        mpStamping.put(MpSqlUtils.getColumnName(PpsPlanEntity::getModel), "产品类型");
        mpStamping.put(MpSqlUtils.getColumnName(PpsPlanEntity::getPlanQty), "数量");
        mpStamping.put(MpSqlUtils.getColumnName(PpsPlanEntity::getDisplayNo), "生产顺序号");
        mpStamping.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEstimatedStartDt), "预计上线时间");
        mpStamping.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEstimatedEndDt), "预计下线时间");
        mpStamping.put(MpSqlUtils.getColumnName(PpsPlanEntity::getStartAvi), "开始站点");
        mpStamping.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEndAvi), "结束站点");
        mpStamping.put(MpSqlUtils.getColumnName(PpsPlanEntity::getRemark), "备注");
        orderDic.put(OrderSheetTableName.Stamping, mpStamping);

        Map<String, String> mpCoverPlate = new LinkedHashMap<>();
        mpCoverPlate.put(MpSqlUtils.getColumnName(PpsPlanEntity::getPlanNo), "计划编号");
        mpCoverPlate.put(MpSqlUtils.getColumnName(PpsPlanEntity::getProductCode), "产品编码");
        mpCoverPlate.put(MpSqlUtils.getColumnName(PpsPlanEntity::getModel), "产品类型");
        mpCoverPlate.put(MpSqlUtils.getColumnName(PpsPlanEntity::getPlanQty), "数量");
        mpCoverPlate.put(MpSqlUtils.getColumnName(PpsPlanEntity::getDisplayNo), "生产顺序号");
        mpCoverPlate.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEstimatedStartDt), "预计上线时间");
        mpCoverPlate.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEstimatedEndDt), "预计下线时间");
        mpCoverPlate.put(MpSqlUtils.getColumnName(PpsPlanEntity::getStartAvi), "开始站点");
        mpCoverPlate.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEndAvi), "结束站点");
        mpCoverPlate.put(MpSqlUtils.getColumnName(PpsPlanEntity::getRemark), "备注");
        orderDic.put(OrderSheetTableName.CoverPlate, mpCoverPlate);

        Map<String, String> mpSparePart = new LinkedHashMap<>();
        mpSparePart.put(MpSqlUtils.getColumnName(PpsPlanEntity::getPlanNo), "计划编号");
        mpSparePart.put(MpSqlUtils.getColumnName(PpsPlanEntity::getProductCode), "整车编码");
        mpSparePart.put(MpSqlUtils.getColumnName(PpsPlanEntity::getModel), "车系");
        mpSparePart.put(MpSqlUtils.getColumnName(PpsPlanEntity::getCharacteristic5), "备件物料号");
        mpSparePart.put(MpSqlUtils.getColumnName(PpsPlanEntity::getPlanQty), "数量");
        mpSparePart.put(MpSqlUtils.getColumnName(PpsPlanEntity::getDisplayNo), "生产顺序号");
        mpSparePart.put(MpSqlUtils.getColumnName(PpsPlanEntity::getCharacteristic10), "生产区域");
        mpSparePart.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEstimatedStartDt), "预计上线时间");
        mpSparePart.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEstimatedEndDt), "预计下线时间");
        mpSparePart.put(MpSqlUtils.getColumnName(PpsPlanEntity::getStartAvi), "开始站点");
        mpSparePart.put(MpSqlUtils.getColumnName(PpsPlanEntity::getEndAvi), "结束站点");
        mpSparePart.put(MpSqlUtils.getColumnName(PpsPlanEntity::getRemark), "备注");
        orderDic.put(OrderSheetTableName.SparePart, mpSparePart);
    }


    @Override
    public PpsPlanEntity getFirstByPlanNo(String planNo) {
        QueryWrapper<PpsPlanEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsPlanEntity::getPlanNo, planNo);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 没有锁定的计划(排除预计上线时间为null)
     *
     * @param pageIndex
     * @param pageSize
     * @param orderCategory
     * @return
     */
    @Override
    public IPage<PpsPlanEntity> getNoLockWith(int pageIndex, int pageSize, String orderCategory) {
        QueryWrapper<PpsPlanEntity> qry = new QueryWrapper<>();
        qry.lambda().lt(PpsPlanEntity::getPlanStatus, PlanStatusEnum.CreatePub.code())
                .eq(PpsPlanEntity::getOrderCategory, orderCategory)
                .isNotNull(PpsPlanEntity::getEstimatedStartDt)
                .orderByAsc(PpsPlanEntity::getEstimatedStartDt);
        return getDataByPage(qry, pageIndex, pageSize);
    }

    /**
     * 发送Lms批次锁定计划
     *
     * @param item
     */
    private void sendLmsBatchLockPlan(PpsPlanEntity item) {
        //除开整车的锁定
        if (!"1".equals(item.getOrderCategory())) {
            String aviCode = item.getStartAvi();
            LmsLockPlanDto info = new LmsLockPlanDto();
            //info.setOrgCode(orgCode);
            //info.setShopCode(shopCode);
            //info.setLineCode(llineCode);
            info.setAviCode(aviCode);
            info.setVin("");
            info.setProductType("1".equals(item.getOrderCategory()) ? "1" : "2");
            if ("7".equals(item.getOrderCategory())) {
                info.setProductCode(item.getCharacteristic5());
            } else {
                info.setProductCode(item.getProductCode());
            }
            info.setPlanNo(item.getPlanNo());
            info.setOneId("");
            info.setManager("");
            info.setPassTime(new Date());
            info.setUniqueCode(item.getId());

            SysQueueNoteEntity sysQueueNoteEntity = new SysQueueNoteEntity();
            sysQueueNoteEntity.setGroupName(RabbitMQConstants.GROUP_NAME_LMS_LOCKPLAN_QUEUE);
            sysQueueNoteEntity.setContent(JsonUtils.toJsonString(info));
            sysQueueNoteService.addSimpleMessage(sysQueueNoteEntity);
        }
    }

    /**
     * 计划锁定
     *
     * @param planNos  计划
     * @param lockType 1 自动 2 手动
     */
    @Override
    public Boolean planLock(List<String> planNos, int lockType) {
        return planLock(planNos, lockType, true);
    }

    /**
     * 计划锁定
     *
     * @param planNos  计划
     * @param lockType 1 自动 2 手动
     */
    private Boolean planLock(List<String> planNos, int lockType, boolean isCreate) {
        if (planNos == null || planNos.size() == 0) {
            return true;
        }
        //查询出所有的计划根据预计上线时间进行排序
        QueryWrapper<PpsPlanEntity> qryPlan = new QueryWrapper<>();
        qryPlan.lambda().in(PpsPlanEntity::getPlanNo, planNos)
                .orderByAsc(PpsPlanEntity::getDisplayNo)
                .orderByAsc(PpsPlanEntity::getEstimatedStartDt);
        List<PpsPlanEntity> planInfos = selectList(qryPlan);
        //10：G-电池盖板分装 11：B-BDU（电池高压盒） 12：D-DMS（电池管理器）13:S-BSM 电池状态管理器
        String packBrachPlanCategory = sysConfigurationProvider.getConfiguration("OrderCategory", "PackBrachPlan");
        Boolean flag = true;
        synchronized (SPLIT_PLAN_LOCK) {
            for (PpsPlanEntity item : planInfos) {
                if (item.getPlanStatus() != 1) {
                    throw new InkelinkException("计划" + item.getPlanNo() + "已被锁定，无法进行锁定");
                }
                UpdateWrapper<PpsPlanEntity> upPlanLock = new UpdateWrapper<>();
                upPlanLock.lambda().set(PpsPlanEntity::getLockType, lockType).eq(PpsPlanEntity::getId, item.getId());
                update(upPlanLock);
                try {
                    if (item.getEstimatedStartDt() == null) {
                        throw new InkelinkException("计划没有预计上线时间，无法进行锁定");
                    }
                    if (item.getEstimatedEndDt() == null) {
                        throw new InkelinkException("计划没有预计下线时间，无法进行锁定");
                    }
                    if (item.getIsFreeze()) {
                        throw new InkelinkException("计划已冻结无法锁定");
                    }
                    List<String> newPlan = new ArrayList<>();
                    //4、根据计划类型调用不同拆分方法
                    switch (item.getOrderCategory()) {
                        case "1":
                            //拆分整车计划
                            splitVehiclePlan(item);
                            break;
                        case "2":
                            //拆分电池计划
                            newPlan = splitPackPlan(item, isCreate);
                            break;
                        case "7":
                            //拆分备件计划
                            splitSparePartPlan(item);
                            break;
                        default:
                            if (packBrachPlanCategory != null &&
                                    Arrays.asList(packBrachPlanCategory.split(",")).contains(item.getOrderCategory())
                            ) {
                                //拆分电池分总成计划
                                splitPackBrachPlan(item);
                            } else {
                                throw new InkelinkException("计划类型中未找到[" + item.getOrderCategory() + "]类型");
                            }
                    }
                    UpdateWrapper<PpsPlanEntity> upPlanStatus = new UpdateWrapper<>();
                    upPlanStatus.lambda().set(PpsPlanEntity::getPlanStatus, PlanStatusEnum.CreatePub.code())
                            .set(PpsPlanEntity::getErrorMes, StringUtils.EMPTY)
                            .eq(PpsPlanEntity::getId, item.getId());
                    update(upPlanStatus);
                    saveChange();
                    if (!newPlan.isEmpty()) {
                        planLock(newPlan, 3, false);
                    }
                } catch (Exception ex) {
                    flag = false;
                    clearChange();
                    log.error(ex.getMessage(), ex);
                    UpdateWrapper<PpsPlanEntity> upPlanError = new UpdateWrapper<>();
                    upPlanError.lambda().set(PpsPlanEntity::getErrorMes, ex.getMessage())
                            .eq(PpsPlanEntity::getId, item.getId());
                    update(upPlanError);
                    saveChange();
                }
            }
        }
        //发送lms计划
        for (PpsPlanEntity item : planInfos) {
            sendLmsBatchLockPlan(item);
        }
        return flag;
    }

    /**
     * 拆分整车计划
     *
     * @param planInfo 计划
     */
    private void splitVehiclePlan(PpsPlanEntity planInfo) {
        if (StringUtils.isBlank(planInfo.getBomVersion())) {
            throw new InkelinkException("未匹配到对应的BOM");
        }
        if (StringUtils.isBlank(planInfo.getCharacteristicVersion())) {
            throw new InkelinkException("未匹配到对应的特征");
        }
        if (IdGenerator.isEmpty(planInfo.getPrcPpsProductProcessId())) {
            throw new InkelinkException("未找到计划对应的生产区域配置");
        }

        //获取计划过点列表
        List<PpsPlanAviEntity> planAviInfos = ppsPlanAviService.getListByPlanNo(planInfo.getPlanNo());
        if (planAviInfos.stream().filter(c -> c.getPassDt() == null).count() > 0) {
            PpsPlanAviEntity paviInfo = planAviInfos.stream().filter(c -> c.getPassDt() == null).findFirst().orElse(null);
            throw new InkelinkException("计划履历中站点" + paviInfo.getAviCode() + "还未设置计划过点时间");
        }
        if (planAviInfos.size() == 0) {
            throw new InkelinkException("计划履历中未配置计划过点时间");
        }
        //TODO  先筛选车间
        List<String> shops = Arrays.asList("WE", "PA", "GA", "VI");
        planAviInfos = planAviInfos.stream().filter(c -> shops.contains(c.getWorkshopCode())).collect(Collectors.toList());

        //获取需要生产的车间
        List<String> shopCodes = planAviInfos.stream().map(c -> c.getWorkshopCode()).distinct().collect(Collectors.toList());

        // 创建订单 -start
        String orderNo = sysSnConfigProvider.createSn("OrderNoSeq_" + planInfo.getOrderCategory());

        PpsOrderEntity orderInfo = new PpsOrderEntity();
        orderInfo.setCharacteristic1(planInfo.getCharacteristic1());
        orderInfo.setCharacteristic2(planInfo.getCharacteristic2());
        orderInfo.setCharacteristic3(planInfo.getCharacteristic3());
        orderInfo.setCharacteristic4(planInfo.getCharacteristic4());
        orderInfo.setCharacteristic5(planInfo.getCharacteristic5());
        orderInfo.setCharacteristic6(planInfo.getCharacteristic6());
        orderInfo.setCharacteristic7(planInfo.getCharacteristic7());
        orderInfo.setCharacteristic8(planInfo.getCharacteristic8());
        orderInfo.setCharacteristic9(planInfo.getCharacteristic9());
        orderInfo.setCharacteristic10(planInfo.getCharacteristic10());
        orderInfo.setAttribute1(planInfo.getAttribute1());
        orderInfo.setAttribute2(planInfo.getAttribute2());
        orderInfo.setAttribute3(planInfo.getAttribute3());

        orderInfo.setEstimatedStartDt(planInfo.getEstimatedStartDt());
        orderInfo.setEstimatedEndDt(planInfo.getEstimatedEndDt());

        orderInfo.setActualStartDt(null);
        orderInfo.setActualEndDt(null);
        //从excel导入
        if (!StringUtils.isBlank(planInfo.getAttribute6())) {
            orderInfo.setBarcode(planInfo.getAttribute6());
        } else {
            orderInfo.setBarcode(StringUtils.EMPTY);
        }
        orderInfo.setDisplayNo(sysSnConfigProvider.createSn("OrderDisplayNo_" + planInfo.getOrderCategory()));
        orderInfo.setSn(StringUtils.EMPTY);
        orderInfo.setIsFreeze(false);
        orderInfo.setOrderNo(orderNo);
        orderInfo.setOrderSource(1);
        orderInfo.setOrderStatus(3);
        orderInfo.setRemark(StringUtils.EMPTY);
        orderInfo.setOrderQuantity(1);
        //orderInfo.setOrderQuantity(planInfo.getPlanQty());
        orderInfo.setCompleteQuantity(0);

        orderInfo.setPlanNo(planInfo.getPlanNo());
        orderInfo.setProductCode(planInfo.getProductCode());
        orderInfo.setModel(planInfo.getModel());
        orderInfo.setOrderCategory(planInfo.getOrderCategory());
        orderInfo.setOrderSign(planInfo.getOrderSign());
        orderInfo.setPrcPpsProductProcessId(planInfo.getPrcPpsProductProcessId());

        orderInfo.setBomVersion(planInfo.getBomVersion());
        orderInfo.setCharacteristicVersion(planInfo.getCharacteristicVersion());

        orderInfo.setProductionNo(StringUtils.EMPTY);
        orderInfo.setBodyNo(StringUtils.EMPTY);
        orderInfo.setPaintNo(StringUtils.EMPTY);
        orderInfo.setAssemblyNo(StringUtils.EMPTY);
        orderInfo.setStartAvi(planInfo.getStartAvi());
        orderInfo.setEndAvi(planInfo.getEndAvi());
        ppsOrderExtendService.insert(orderInfo);
// 创建订单 -end

        // 创建车间工单
        for (String code : shopCodes) {

            List<PpsPlanAviEntity> shopPlanAviInfos = planAviInfos.stream().filter(c -> StringUtils.equals(c.getWorkshopCode(), code))
                    .sorted(Comparator.comparing(PpsPlanAviEntity::getPassDt)).collect(Collectors.toList());

            PpsPlanAviEntity shopPlanAviFirst = shopPlanAviInfos.get(0);
            PpsPlanAviEntity shopPlanAviLast = shopPlanAviInfos.get(shopPlanAviInfos.size() - 1);


            String entryNo = code + DateUtils.format(new Date(), DateUtils.DATE_PATTERN_C)
                    + sysSnConfigProvider.createSn("PpsEntry_" + code + "_Code");
            PpsEntryEntity entryInfo = new PpsEntryEntity();
            entryInfo.setPrcPpsOrderId(orderInfo.getId());
            entryInfo.setOrderNo(orderInfo.getOrderNo());
            entryInfo.setParentId(Constant.DEFAULT_ID);
            entryInfo.setParentNo(StringUtils.EMPTY);
            entryInfo.setPlanNo(orderInfo.getPlanNo());
            entryInfo.setModel(orderInfo.getModel());
            entryInfo.setPrcPpsProductProcessId(orderInfo.getPrcPpsProductProcessId());
            entryInfo.setActualStartDt(null);
            entryInfo.setActualEndDt(null);
            entryInfo.setSn(StringUtils.EMPTY);
            entryInfo.setLineCode(StringUtils.EMPTY);
            //entryInfo.setDisplayNo(shopPlanAviFirst.getSequenceNo());

            int displayno = Integer.parseInt(sysSnConfigProvider.createSn("EntryDisplayNo_" + planInfo.getOrderCategory()));
            entryInfo.setDisplayNo(displayno);

            entryInfo.setIsCreateWo(false);
            entryInfo.setEntryNo(entryNo);
            entryInfo.setEntrySource(1);
            entryInfo.setEntryType(EntryTypeEnum.ShopEntry.code());
            entryInfo.setEstimatedStartDt(shopPlanAviFirst.getPassDt());
            entryInfo.setEstimatedEndDt(shopPlanAviLast.getPassDt());
            entryInfo.setWorkshopCode(code);
            entryInfo.setStatus(EntryStatusEnum.Lock.code());
            entryInfo.setOrderSign(orderInfo.getOrderSign());
            entryInfo.setOrderCategory(orderInfo.getOrderCategory());
            entryInfo.setProductCode(orderInfo.getProductCode());
            entryInfo.setMaterialCn(orderInfo.getCharacteristic4());

            entryInfo.setAttribute1(planInfo.getAttribute1());
            entryInfo.setAttribute2(planInfo.getAttribute2());
            entryInfo.setAttribute3(planInfo.getAttribute3());

            entryInfo.setSubscriubeCode(StringUtils.EMPTY);

            /**   未赋值entryInfo.setPlanQuantity(); */
            /**   未赋值entryInfo.setIsDispose();  */
            ppsEntryService.insert(entryInfo);

            // 创建分线工单  ---- 开工点生成分线工单

            //创建焊装队列（可以是任意车间）
            /*List<PpsMainUpLineConfigEntity> mainupConfgs = ppsMainUpLineConfigService.getAllDatas()
                    .stream().filter(c -> StringUtils.equals(c.getWorkshopCode(), code)).collect(Collectors.toList());
            if (!mainupConfgs.isEmpty()) {
                List<PpsMainUpLineQueueEntity> mainDatas = new ArrayList<>();
                for (PpsMainUpLineConfigEntity mainup : mainupConfgs) {
                    PpsMainUpLineQueueEntity mup = new PpsMainUpLineQueueEntity();
                    //获取
                    List<PmProductBomEntity> boms = getBomData(planInfo.getProductCode(), planInfo.getBomVersion());
                    if (boms == null || boms.isEmpty()) {
                        continue;
                    }
                    PmProductBomEntity bomEnt = boms.stream().filter(c -> c.getMaterialNo().startsWith(mainup.getComponentCode()))
                            .findFirst().orElse(null);
                    if (bomEnt == null) {
                        continue;
                    }
                    mup.setMaterialNo(bomEnt.getMaterialNo());
                    mup.setStatus(0);
                    mup.setDisplayNo(entryInfo.getDisplayNo());
                    mup.setPlanNo(planInfo.getPlanNo());
                    mup.setConfigCode(mainup.getConfigCode());
                    mup.setModel(planInfo.getModel());
                    mup.setCarStatus(planInfo.getCharacteristic7());
                    mainDatas.add(mup);
                }
                ppsMainUpLineQueueService.insertBatch(mainDatas);
            }*/
        }
        // 创建车间工单-end entryInfo

    }

    /*@Autowired
    private IPpsMainUpLineQueueService ppsMainUpLineQueueService;
    @Autowired
    private IPpsMainUpLineConfigService ppsMainUpLineConfigService;*/
    @Autowired
    private IPpsPackBranchingPlanConfigService ppsPackBranchingPlanConfigService;

    /**
     * 拆分电池计划
     *
     * @param planInfo 计划
     */
    private List<String> splitPackPlan(PpsPlanEntity planInfo, boolean isCreate) {
        if (StringUtils.isBlank(planInfo.getBomVersion())) {
            throw new InkelinkException("未匹配到对应的BOM");
        }
        if (IdGenerator.isEmpty(planInfo.getPrcPpsProductProcessId())) {
            throw new InkelinkException("未找到计划对应的生产区域配置");
        }
        String shopCode = BodyShopCode.BA;
        PpsEntryEntity fristEntity = new PpsEntryEntity();

        for (int i = 0; i < planInfo.getPlanQty(); i++) {
            // 创建订单 --start
            String orderNo = sysSnConfigProvider.createSn("OrderNoSeq_" + planInfo.getOrderCategory());
            PpsOrderEntity orderInfo = new PpsOrderEntity();
            orderInfo.setActualEndDt(null);
            orderInfo.setActualStartDt(null);
            orderInfo.setEstimatedEndDt(planInfo.getEstimatedEndDt());
            orderInfo.setEstimatedStartDt(planInfo.getEstimatedStartDt());
            orderInfo.setBarcode(StringUtils.EMPTY);
            orderInfo.setCharacteristic1(planInfo.getCharacteristic1());
            orderInfo.setCharacteristic2(planInfo.getCharacteristic2());
            orderInfo.setCharacteristic3(planInfo.getCharacteristic3());
            orderInfo.setCharacteristic4(planInfo.getCharacteristic4());
            orderInfo.setCharacteristic5(planInfo.getCharacteristic5());
            orderInfo.setCharacteristic6(planInfo.getCharacteristic6());
            orderInfo.setCharacteristic7(planInfo.getCharacteristic7());
            orderInfo.setCharacteristic8(planInfo.getCharacteristic8());
            orderInfo.setCharacteristic9(planInfo.getCharacteristic9());
            orderInfo.setCharacteristic10(planInfo.getCharacteristic10());
            orderInfo.setAttribute1(planInfo.getAttribute1());
            orderInfo.setAttribute2(planInfo.getAttribute2());
            orderInfo.setAttribute3(planInfo.getAttribute3());
            orderInfo.setDisplayNo(sysSnConfigProvider.createSn("OrderDisplayNo_" + planInfo.getOrderCategory()));
            orderInfo.setSn(StringUtils.EMPTY);
            orderInfo.setProductCode(planInfo.getProductCode());
            orderInfo.setIsFreeze(false);
            orderInfo.setModel(planInfo.getModel());
            orderInfo.setOrderCategory(planInfo.getOrderCategory());
            orderInfo.setOrderNo(orderNo);
            orderInfo.setOrderSource(1);
            orderInfo.setOrderStatus(OrderStatusEnum.CreatePub.code());
            orderInfo.setOrderSign(planInfo.getOrderSign());
            orderInfo.setPlanNo(planInfo.getPlanNo());
            orderInfo.setPrcPpsProductProcessId(planInfo.getPrcPpsProductProcessId());
            orderInfo.setOrderQuantity(1);
            orderInfo.setCompleteQuantity(0);
            orderInfo.setRemark(StringUtils.EMPTY);
            orderInfo.setBomVersion(planInfo.getBomVersion());
            orderInfo.setCharacteristicVersion(planInfo.getCharacteristicVersion());
            orderInfo.setStartAvi(planInfo.getStartAvi());
            orderInfo.setEndAvi(planInfo.getEndAvi());
            ppsOrderExtendService.insert(orderInfo);
            //创建订单 --end

            //创建车间工单 --start
            String entryNo = shopCode + DateUtils.format(new Date(), DateUtils.DATE_PATTERN_C)
                    + sysSnConfigProvider.createSn("PpsEntry_" + shopCode + "_Code");
            PpsEntryEntity entryInfo = new PpsEntryEntity();
            entryInfo.setPrcPpsOrderId(orderInfo.getId());
            entryInfo.setOrderNo(orderInfo.getOrderNo());
            entryInfo.setParentId(Constant.DEFAULT_ID);
            entryInfo.setParentNo(StringUtils.EMPTY);
            entryInfo.setPlanNo(orderInfo.getPlanNo());
            entryInfo.setModel(orderInfo.getModel());
            entryInfo.setPrcPpsProductProcessId(orderInfo.getPrcPpsProductProcessId());
            entryInfo.setActualEndDt(null);
            entryInfo.setActualStartDt(null);
            entryInfo.setLineCode(StringUtils.EMPTY);
            entryInfo.setSn(StringUtils.EMPTY);
            entryInfo.setDisplayNo(Integer.parseInt(sysSnConfigProvider.createSn("PpsEntry_" + shopCode + "_Seq")));
            entryInfo.setIsCreateWo(false);
            entryInfo.setEntryNo(entryNo);
            entryInfo.setEntrySource(1);
            entryInfo.setEntryType(EntryTypeEnum.ShopEntry.code());
            entryInfo.setEstimatedEndDt(orderInfo.getEstimatedEndDt());
            entryInfo.setEstimatedStartDt(orderInfo.getEstimatedStartDt());
            entryInfo.setWorkshopCode(shopCode);
            entryInfo.setStatus(EntryStatusEnum.Lock.code());
            entryInfo.setOrderSign(orderInfo.getOrderSign());
            entryInfo.setOrderCategory(orderInfo.getOrderCategory());
            entryInfo.setProductCode(orderInfo.getProductCode());
            entryInfo.setMaterialCn(orderInfo.getCharacteristic4());

            entryInfo.setAttribute1(planInfo.getAttribute1());
            entryInfo.setAttribute2(planInfo.getAttribute2());
            entryInfo.setAttribute3(planInfo.getAttribute3());
            ppsEntryService.insert(entryInfo);
            if (i == 0) {
                fristEntity = JsonUtils.parseObject(JsonUtils.toJsonString(entryInfo), PpsEntryEntity.class);
            }
            //创建车间工单 --end
        }

        // 创建分线工单(生产批量工单)---在电池包上线处生成。
//        List<PpsEntryConfigEntity> lineEntityConfigs = ppsEntryConfigService.getPpsEntryConfigListByShopCode(shopCode, 1, planInfo.getModel());
//        fristEntity.setPlanQuantity(planInfo.getPlanQty());
//        for (PpsEntryConfigEntity lineEntityConfig : lineEntityConfigs) {
//            ppsEntryService.createBranchEntry(fristEntity, lineEntityConfig);
//        }
        // 创建分线工单(生产批量工单)----end

        //创建分线计划
        if (isCreate) {
            return createPackBranchPlan(planInfo);
        }
        return new ArrayList<>();
    }

    /**
     * 拆分电池分线计划
     *
     * @param planInfo 计划
     */
    private void splitPackBrachPlan(PpsPlanEntity planInfo) {
        if (StringUtils.isBlank(planInfo.getBomVersion())) {
            throw new InkelinkException("未匹配到对应的BOM");
        }
        if (IdGenerator.isEmpty(planInfo.getPrcPpsProductProcessId())) {
            throw new InkelinkException("未找到计划对应的生产区域配置");
        }
        String shopCode = BodyShopCode.BA;
        PpsEntryEntity fristEntity = new PpsEntryEntity();

        for (int i = 0; i < planInfo.getPlanQty(); i++) {
            // 创建订单 --start
            String orderNo = sysSnConfigProvider.createSn("OrderNoSeq_" + planInfo.getOrderCategory());
            PpsOrderEntity orderInfo = new PpsOrderEntity();
            orderInfo.setActualEndDt(null);
            orderInfo.setActualStartDt(null);
            orderInfo.setEstimatedEndDt(planInfo.getEstimatedEndDt());
            orderInfo.setEstimatedStartDt(planInfo.getEstimatedStartDt());
            orderInfo.setBarcode(StringUtils.EMPTY);
            orderInfo.setCharacteristic1(planInfo.getCharacteristic1());
            orderInfo.setCharacteristic2(planInfo.getCharacteristic2());
            orderInfo.setCharacteristic3(planInfo.getCharacteristic3());
            orderInfo.setCharacteristic4(planInfo.getCharacteristic4());
            orderInfo.setCharacteristic5(planInfo.getCharacteristic5());
            orderInfo.setCharacteristic6(planInfo.getCharacteristic6());
            orderInfo.setCharacteristic7(planInfo.getCharacteristic7());
            orderInfo.setCharacteristic8(planInfo.getCharacteristic8());
            orderInfo.setCharacteristic9(planInfo.getCharacteristic9());
            orderInfo.setCharacteristic10(planInfo.getCharacteristic10());
            orderInfo.setAttribute1(planInfo.getAttribute1());
            orderInfo.setAttribute2(planInfo.getAttribute2());
            orderInfo.setAttribute3(planInfo.getAttribute3());
            orderInfo.setDisplayNo(sysSnConfigProvider.createSn("OrderDisplayNo_" + planInfo.getOrderCategory()));
            orderInfo.setSn(StringUtils.EMPTY);
            orderInfo.setProductCode(planInfo.getProductCode());
            orderInfo.setIsFreeze(false);
            orderInfo.setModel(planInfo.getModel());
            orderInfo.setOrderCategory(planInfo.getOrderCategory());
            orderInfo.setOrderNo(orderNo);
            orderInfo.setOrderSource(1);
            orderInfo.setOrderStatus(OrderStatusEnum.CreatePub.code());
            orderInfo.setOrderSign(planInfo.getOrderSign());
            orderInfo.setPlanNo(planInfo.getPlanNo());
            orderInfo.setPrcPpsProductProcessId(planInfo.getPrcPpsProductProcessId());
            orderInfo.setOrderQuantity(1);
            orderInfo.setCompleteQuantity(0);
            orderInfo.setRemark(StringUtils.EMPTY);
            orderInfo.setBomVersion(planInfo.getBomVersion());
            orderInfo.setCharacteristicVersion(planInfo.getCharacteristicVersion());
            orderInfo.setStartAvi(planInfo.getStartAvi());
            orderInfo.setEndAvi(planInfo.getEndAvi());
            ppsOrderExtendService.insert(orderInfo);
            //创建订单 --end

            //创建车间工单 --start
            String entryNo = shopCode + DateUtils.format(new Date(), DateUtils.DATE_PATTERN_C)
                    + sysSnConfigProvider.createSn("PpsEntry_" + planInfo.getCharacteristic10() + "_Code");
            PpsEntryEntity entryInfo = new PpsEntryEntity();
            entryInfo.setPrcPpsOrderId(orderInfo.getId());
            entryInfo.setOrderNo(orderInfo.getOrderNo());
            entryInfo.setParentId(Constant.DEFAULT_ID);
            entryInfo.setParentNo(StringUtils.EMPTY);
            entryInfo.setPlanNo(orderInfo.getPlanNo());
            entryInfo.setModel(orderInfo.getModel());
            entryInfo.setPrcPpsProductProcessId(orderInfo.getPrcPpsProductProcessId());
            entryInfo.setActualEndDt(null);
            entryInfo.setActualStartDt(null);
            entryInfo.setLineCode(planInfo.getCharacteristic10()); //配置的线体
            entryInfo.setSn(StringUtils.EMPTY);
            entryInfo.setDisplayNo(Integer.parseInt(sysSnConfigProvider.createSn("PpsEntry_" + planInfo.getCharacteristic10() + "_Seq")));
            entryInfo.setIsCreateWo(false);
            entryInfo.setEntryNo(entryNo);
            entryInfo.setEntrySource(1);
            entryInfo.setEntryType(EntryTypeEnum.ShopEntry.code());
            entryInfo.setEstimatedEndDt(orderInfo.getEstimatedEndDt());
            entryInfo.setEstimatedStartDt(orderInfo.getEstimatedStartDt());
            entryInfo.setWorkshopCode(shopCode);
            entryInfo.setStatus(EntryStatusEnum.Lock.code());
            entryInfo.setOrderSign(orderInfo.getOrderSign());
            entryInfo.setOrderCategory(orderInfo.getOrderCategory());
            entryInfo.setProductCode(orderInfo.getProductCode());
            entryInfo.setMaterialCn(orderInfo.getCharacteristic4());

            entryInfo.setAttribute1(planInfo.getAttribute1());
            entryInfo.setAttribute2(planInfo.getAttribute2());
            entryInfo.setAttribute3(planInfo.getAttribute3());
            ppsEntryService.insert(entryInfo);
            if (i == 0) {
                fristEntity = JsonUtils.parseObject(JsonUtils.toJsonString(entryInfo), PpsEntryEntity.class);
            }
            //创建车间工单 --end
        }

        // 创建分线工单(生产批量工单)---在电池包上线处生成。
//        List<PpsEntryConfigEntity> lineEntityConfigs = ppsEntryConfigService.getPpsEntryConfigListByShopCode(shopCode, 1, planInfo.getModel());
//        fristEntity.setPlanQuantity(planInfo.getPlanQty());
//        for (PpsEntryConfigEntity lineEntityConfig : lineEntityConfigs) {
//            ppsEntryService.createBranchEntry(fristEntity, lineEntityConfig);
//        }
        // 创建分线工单(生产批量工单)----end

    }

    /**
     * 设置计划完成
     */
    public void setPlanEnd(PpsOrderEntity orderInfo, Date passDt, PpsPlanEntity plan) {

        //代表计划生产完成
        //更新生产计划的状态(完成）
        if (orderInfo.getOrderStatus() == 99) {
            //更新订单
            if (orderInfo.getActualEndDt() == null && passDt != null) {
                UpdateWrapper<PpsOrderEntity> orderUp = new UpdateWrapper<>();
                orderUp.lambda().set(PpsOrderEntity::getActualEndDt, passDt)
                        .eq(PpsOrderEntity::getId, orderInfo.getId());
                ppsOrderExtendService.update(orderUp);
            }

            //获取该计划下面所有没有完成的订单
            QueryWrapper<PpsOrderEntity> qryOrder = new QueryWrapper<>();
            qryOrder.lambda().eq(PpsOrderEntity::getPlanNo, orderInfo.getPlanNo())
                    .ne(PpsOrderEntity::getOrderStatus, 99)
                    .ne(PpsOrderEntity::getOrderStatus, 98)
                    .ne(PpsOrderEntity::getOrderNo, orderInfo.getOrderNo());
            if (ppsOrderExtendService.selectCount(qryOrder) == 0) {
                //var noCompleteOrders = _ppsOrderDal.Table.Where(o => o.PlanNo == plan.PlanNo
                // && o.OrderStatus !=99).ToList();
                //if (noCompleteOrders.Where(o => o.OrderNo != order.OrderNo && o.OrderStatus!=98).Count() == 0)
                UpdateWrapper<PpsPlanEntity> planUp = new UpdateWrapper<>();
                planUp.lambda().set(PpsPlanEntity::getPlanStatus, 30)
                        .eq(PpsPlanEntity::getId, plan.getId());
                this.update(planUp);
            }
        }

        if (orderInfo.getOrderStatus() == 5) {
            if (orderInfo.getActualEndDt() == null && passDt != null) {
                UpdateWrapper<PpsOrderEntity> orderUp = new UpdateWrapper<>();
                orderUp.lambda().set(PpsOrderEntity::getActualEndDt, passDt)
                        .eq(PpsOrderEntity::getId, orderInfo.getId());
                ppsOrderExtendService.update(orderUp);
            }
            //获取该计划下面所有没有完成的订单
            QueryWrapper<PpsOrderEntity> qryOrder = new QueryWrapper<>();
            qryOrder.lambda().eq(PpsOrderEntity::getPlanNo, orderInfo.getPlanNo())
                    .ne(PpsOrderEntity::getOrderStatus, 5)
                    .ne(PpsOrderEntity::getOrderStatus, 98)
                    .ne(PpsOrderEntity::getOrderNo, orderInfo.getOrderNo());
            if (ppsOrderExtendService.selectCount(qryOrder) == 0) {
                // var noCompleteOrders = _ppsOrderDal.Table.Where(o => o.PlanNo == plan.PlanNo && o.OrderStatus != 5).ToList();
                // if (noCompleteOrders.Where(o => o.OrderNo != order.OrderNo && o.OrderStatus != 98).Count() == 0)
                UpdateWrapper<PpsPlanEntity> planUp = new UpdateWrapper<>();
                planUp.lambda().set(PpsPlanEntity::getPlanStatus, 30)
                        .eq(PpsPlanEntity::getId, plan.getId());
                this.update(planUp);
            }
        }
    }

    /**
     * 创建电池分线计划
     */
    public void createPackBranchPlan(String planNo, Integer planQty) {
        PpsPlanEntity planInfo = getFirstByPlanNo(planNo);
        if (planInfo == null) {
            throw new InkelinkException("没有找到计划编号:" + planNo + "对应的数据.");
        }
        if (planQty <= 0) {
            throw new InkelinkException("计划数量必须大于0");
        }
        planInfo.setPlanQty(planQty);
        List<String> plans = createPackBranchPlan(planInfo);
        saveChange();
        if (plans == null || plans.isEmpty()) {
            return;
        }
        planLock(plans, 2, false);
    }

    /**
     * 创建电池分线计划
     */
    private List<String> createPackBranchPlan(PpsPlanEntity planInfo) {
        List<String> planNos = new ArrayList<>();
        //获取配置
        List<PpsPackBranchingPlanConfigEntity> planConfigs = ppsPackBranchingPlanConfigService.getAllDatas();
        if (planConfigs != null && !planConfigs.isEmpty()) {
            PmAllDTO pmall = pmVersionProvider.getObjectedPm();
            String errMsg = "";
            List<PmProductBomEntity> boms = getBomData(planInfo.getProductCode(), planInfo.getBomVersion());
            if (boms == null || boms.isEmpty()) {
                boms = new ArrayList<>();
            }
            for (PpsPackBranchingPlanConfigEntity pcfg : planConfigs) {
                PmProductBomEntity bomEnt = boms.stream().filter(c -> c.getMaterialNo().startsWith(pcfg.getComponentCode()))
                        .findFirst().orElse(null);
                if (bomEnt == null) {
                    errMsg = "没有找到组件" + pcfg.getComponentCode() + "的BOM信息";
                    pcfg.setErrorMes(errMsg);
                    ppsPackBranchingPlanConfigService.updateById(pcfg);
                    continue;
                }
                PpsPlanEntity et = new PpsPlanEntity();

                et.setOrderCategory(pcfg.getOrderCategory());
                String planNo = sysSnConfigProvider.createSn("PlanNoSeq_" + et.getOrderCategory());
                et.setPlanNo(planNo);
                planNos.add(planNo);

                et.setCharacteristic1("");
                et.setAttribute1("");
                et.setAttribute2("0");
                et.setAttribute3(planInfo.getPlanNo());
                et.setModel(planInfo.getModel());
                et.setProductCode(planInfo.getProductCode());
                et.setPlanQty(planInfo.getPlanQty());
                PpsProductProcessEntity vprocessInfo = null;
                if(pcfg.getPrcPpsProductProcessId()>0) {
                    vprocessInfo = ppsProductProcessService.get(pcfg.getPrcPpsProductProcessId());
                } else {
                    vprocessInfo = getProcess(et.getOrderCategory());
                }
                if (vprocessInfo != null) {
                    et.setPrcPpsProductProcessId(vprocessInfo.getId());
                } else {
                    et.setPrcPpsProductProcessId(0L);
                }
                et.setOrderSign(planInfo.getOrderSign());
                et.setPlanStatus(1);
                et.setPlanSource(planInfo.getPlanSource());
                et.setEstimatedStartDt(planInfo.getEstimatedStartDt());
                et.setEstimatedEndDt(planInfo.getEstimatedEndDt());
                et.setDisplayNo(planInfo.getDisplayNo());

                //bom
                et.setBomVersion(planInfo.getBomVersion());
                //特征
                et.setCharacteristicVersion(planInfo.getCharacteristicVersion());

                et.setCharacteristic4(bomEnt.getMaterialCn());
                et.setCharacteristic5(bomEnt.getMaterialNo());
                et.setCharacteristic10(pcfg.getLineCode());

                //开工点、结束点
                et.setStartAvi("");
                et.setEndAvi("");
                PmLineEntity line = pmall.getLines().stream().filter(c -> StringUtils.equals(c.getLineCode(), pcfg.getLineCode()))
                        .findFirst().orElse(null);
                if (line != null) {
                    //2.上线;3. 下线
                    PmAviEntity startAvi = pmall.getAvis().stream().filter(c -> c.getAviType() != null && c.getAviType().contains("2"))
                            .findFirst().orElse(null);
                    if (startAvi != null) {
                        et.setStartAvi(startAvi.getAviCode());
                    }

                    PmAviEntity endAvi = pmall.getAvis().stream().filter(c -> c.getAviType() != null && c.getAviType().contains("3"))
                            .findFirst().orElse(null);
                    if (endAvi != null) {
                        et.setEndAvi(endAvi.getAviCode());
                    }
                }
                et.setRemark("分线计划");

                insert(et);

                pcfg.setErrorMes(errMsg);
                ppsPackBranchingPlanConfigService.updateById(pcfg);
            }
        }
        return planNos;
    }

    /**
     * 拆分备件计划
     *
     * @param planInfo 计划
     */
    private void splitSparePartPlan(PpsPlanEntity planInfo) {
        if (StringUtils.isBlank(planInfo.getBomVersion())) {
            throw new InkelinkException("未匹配到对应的BOM");
        }

        if (StringUtils.isBlank(planInfo.getCharacteristicVersion())) {
            throw new InkelinkException("未匹配到对应的特征");
        }
        PmAllDTO allpm = pmVersionProvider.getObjectedPm();
        PmLineEntity lineInfo = allpm.getLines().stream().filter(c ->
                StringUtils.equals(c.getLineCode(), planInfo.getCharacteristic10())).findFirst().orElse(null);

        if (lineInfo == null) {
            throw new InkelinkException("无效的线体编码" + planInfo.getCharacteristic10());
        }
        PmWorkShopEntity shopInfo = allpm.getShops().stream().filter(c ->
                Objects.equals(c.getId(), lineInfo.getPrcPmWorkshopId())).findFirst().orElse(null);
        for (int i = 0; i < planInfo.getPlanQty(); i++) {
            String bjVin = sysSnConfigProvider.createSn("BJVin");
            //创建订单--start
            String orderNo = sysSnConfigProvider.createSn("OrderNoSeq_" + planInfo.getOrderCategory());
            PpsOrderEntity orderInfo = new PpsOrderEntity();

            orderInfo.setActualEndDt(null);
            orderInfo.setActualStartDt(null);
            orderInfo.setEstimatedEndDt(planInfo.getEstimatedEndDt());
            orderInfo.setEstimatedStartDt(planInfo.getEstimatedStartDt());
            orderInfo.setBarcode(bjVin);
            orderInfo.setCharacteristic1(planInfo.getCharacteristic1());
            orderInfo.setCharacteristic2(planInfo.getCharacteristic2());
            orderInfo.setCharacteristic3(planInfo.getCharacteristic3());
            orderInfo.setCharacteristic4(planInfo.getCharacteristic4());
            orderInfo.setCharacteristic5(planInfo.getCharacteristic5());
            orderInfo.setCharacteristic6(planInfo.getCharacteristic6());
            orderInfo.setCharacteristic7(planInfo.getCharacteristic7());
            orderInfo.setCharacteristic8(planInfo.getCharacteristic8());
            orderInfo.setCharacteristic9(planInfo.getCharacteristic9());
            orderInfo.setCharacteristic10(planInfo.getCharacteristic10());
            orderInfo.setAttribute1(planInfo.getAttribute1());
            orderInfo.setAttribute2(planInfo.getAttribute2());
            orderInfo.setAttribute3(planInfo.getAttribute3());
            orderInfo.setDisplayNo(sysSnConfigProvider.createSn("OrderDisplayNo_" + planInfo.getOrderCategory()));
            orderInfo.setSn(bjVin);
            orderInfo.setProductCode(planInfo.getProductCode());
            orderInfo.setIsFreeze(false);
            orderInfo.setModel(planInfo.getModel());
            orderInfo.setOrderCategory(planInfo.getOrderCategory());
            orderInfo.setOrderNo(orderNo);
            orderInfo.setOrderSource(1);
            orderInfo.setOrderStatus(OrderStatusEnum.CreatePub.code());
            orderInfo.setOrderSign(planInfo.getOrderSign());
            orderInfo.setPlanNo(planInfo.getPlanNo());
            orderInfo.setPrcPpsProductProcessId(planInfo.getPrcPpsProductProcessId());
            orderInfo.setOrderQuantity(1);
            orderInfo.setCompleteQuantity(0);
            orderInfo.setRemark(StringUtils.EMPTY);
            orderInfo.setBomVersion(planInfo.getBomVersion());
            orderInfo.setCharacteristicVersion(planInfo.getCharacteristicVersion());
            orderInfo.setStartAvi(planInfo.getStartAvi());
            orderInfo.setEndAvi(planInfo.getEndAvi());
            ppsOrderExtendService.insert(orderInfo);
            //创建订单--end

            //虚拟的父级工单--start
            PpsEntryConfigEntity lineEntityConfig = ppsEntryConfigService.getFirstByLineCode(lineInfo.getLineCode(), planInfo.getModel());
            if (lineEntityConfig == null) {
                throw new InkelinkException("在工单订阅配置中未找到线体" + lineInfo.getLineCode() + "的工单配置");
            }

            PpsEntryEntity entryInfo = new PpsEntryEntity();
            entryInfo.setId(1L);
            entryInfo.setPrcPpsOrderId(orderInfo.getId());
            entryInfo.setOrderNo(orderInfo.getOrderNo());
            entryInfo.setParentId(Constant.DEFAULT_ID);
            entryInfo.setParentNo(StringUtils.EMPTY);
            entryInfo.setPlanNo(orderInfo.getPlanNo());
            entryInfo.setModel(orderInfo.getModel());
            entryInfo.setPrcPpsProductProcessId(orderInfo.getPrcPpsProductProcessId());
            entryInfo.setActualEndDt(null);
            entryInfo.setActualStartDt(null);
            entryInfo.setLineCode(StringUtils.EMPTY);
            entryInfo.setSn(bjVin);
            entryInfo.setDisplayNo(1000000);
            entryInfo.setIsCreateWo(false);
            entryInfo.setEntryNo("000000000001");
            entryInfo.setEntrySource(1);
            entryInfo.setEntryType(EntryTypeEnum.ShopEntry.code());
            entryInfo.setEstimatedEndDt(orderInfo.getEstimatedEndDt());
            entryInfo.setEstimatedStartDt(orderInfo.getEstimatedStartDt());
            entryInfo.setWorkshopCode(shopInfo.getWorkshopCode());
            entryInfo.setStatus(EntryStatusEnum.NoProduce.code());
            entryInfo.setOrderSign(orderInfo.getOrderSign());
            entryInfo.setOrderCategory(orderInfo.getOrderCategory());
            entryInfo.setProductCode(orderInfo.getProductCode());
            entryInfo.setMaterialCn(orderInfo.getCharacteristic4());

            entryInfo.setAttribute1(planInfo.getAttribute1());
            entryInfo.setAttribute2(planInfo.getAttribute2());
            entryInfo.setAttribute3(planInfo.getAttribute3());
            ppsEntryService.createBranchEntry(entryInfo, lineEntityConfig);
            //虚拟的父级工单--end
        }
    }


    /**
     * 获取计划的bom数据
     *
     * @param planNo 计划号
     * @return BOM数据集合
     */
    @Override
    public List<PmProductBomEntity> getPlanBom(String planNo) {
        PpsPlanEntity planInfo = getFirstByPlanNo(planNo);
        if (planInfo == null) {
            throw new InkelinkException("无效的计划");
        }
        String materialNo = planInfo.getProductCode();
        return getBomData(materialNo, planInfo.getBomVersion());
    }

    /**
     * 修改计划BOM
     *
     * @param para BOM版本更新对象
     */
    @Override
    public void updatePlanBom(UpdatePlanVersionsPara para) {
        PpsPlanEntity planInfo = get(para.getPlanId());
        if (planInfo == null || planInfo.getIsDelete()) {
            throw new InkelinkException("计划不存在");
        }
        String bomVersion = para.getVersion();
        if (!StringUtils.equals(planInfo.getBomVersion(), bomVersion)) {
            UpdateWrapper<PpsPlanEntity> upPlan = new UpdateWrapper<>();
            upPlan.lambda().set(PpsPlanEntity::getBomVersion, bomVersion).eq(PpsPlanEntity::getId, planInfo.getId());
            update(upPlan);
            //未锁定
            List<ConditionDto> ordercons = new ArrayList<>();
            ordercons.add(new ConditionDto("PlanNo", planInfo.getPlanNo(), ConditionOper.Equal));
            ordercons.add(new ConditionDto("OrderStatus", "3", ConditionOper.LessThan));
            List<PpsOrderEntity> orders = ppsOrderExtendService.getData(ordercons);
            for (PpsOrderEntity item : orders) {
                UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
                upOrder.lambda()
                        .set(PpsOrderEntity::getBomVersion, bomVersion)
                        .eq(PpsOrderEntity::getId, item.getId());
                ppsOrderExtendService.update(upOrder);
            }
        }
    }

    /**
     * 修改计划特征
     *
     * @param para 版本更新对象
     */
    @Override
    public void updatePlanCharacteristics(UpdatePlanVersionsPara para) {
        PpsPlanEntity planInfo = get(para.getPlanId());
        PmProductCharacteristicsVersionsEntity versionsInfo = pmProductCharacteristicsVersionsService.getByMaterialNoVersions(planInfo.getProductCode(), para.getVersion());
        if (versionsInfo == null) {
            throw new InkelinkException("特征信息与计划信息不匹配");
        }
        UpdateWrapper<PpsPlanEntity> upPlan = new UpdateWrapper<>();
        upPlan.lambda()
                .set(PpsPlanEntity::getCharacteristicVersion, versionsInfo.getVersions())
                .eq(PpsPlanEntity::getId, para.getPlanId());
        update(upPlan);
        //未锁定
        List<ConditionDto> ordercons = new ArrayList<>();
        ordercons.add(new ConditionDto("PlanNo", planInfo.getPlanNo(), ConditionOper.Equal));
        ordercons.add(new ConditionDto("OrderStatus", "3", ConditionOper.LessThan));
        List<PpsOrderEntity> orders = ppsOrderExtendService.getData(ordercons);
        for (PpsOrderEntity item : orders) {
            UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
            upOrder.lambda()
                    .set(PpsOrderEntity::getCharacteristicVersion, versionsInfo.getVersions())
                    .eq(PpsOrderEntity::getId, item.getId());
            ppsOrderExtendService.update(upOrder);
        }
    }

    /**
     * 获取计划的特征数据
     *
     * @param planNo 计划号
     * @return 特征数据集合
     */
    @Override
    public List<PmProductCharacteristicsEntity> getPlanCharacteristic(String planNo) {

        PpsPlanEntity planInfo = getFirstByPlanNo(planNo);
        if (planInfo == null) {
            throw new InkelinkException("无效的计划");
        }
        String materialNo = planInfo.getProductCode();
        return pmProductCharacteristicsVersionsService.getCharacteristicsData(materialNo, planInfo.getCharacteristicVersion());
    }

    /**
     * 计划冻结
     *
     * @param ppsPlanIds 生产计划ID
     */
    @Override
    public void freeze(List<Long> ppsPlanIds) {
        for (Long planId : ppsPlanIds) {
            PpsPlanEntity ppsPlanEntity = get(planId);
            if (ppsPlanEntity == null) {
                return;
            }
            if (ppsPlanEntity.getPlanStatus() >= 3) {
                throw new InkelinkException(ppsPlanEntity.getPlanNo() + "该计划状态不正确，不能冻结！");
            }
            if (ppsPlanEntity.getIsFreeze()) {
                throw new InkelinkException(ppsPlanEntity.getPlanNo() + "该计划编号已冻结,不需冻结！");
            }
            UpdateWrapper<PpsPlanEntity> upPlan = new UpdateWrapper<>();
            upPlan.lambda().set(PpsPlanEntity::getIsFreeze, true)
                    .eq(PpsPlanEntity::getId, planId);
            update(upPlan);
        }
    }

    /**
     * 取消计划冻结
     *
     * @param ppsPlanIds 生产计划ID
     */
    @Override
    public void unFreeze(List<Long> ppsPlanIds) {
        for (Long planId : ppsPlanIds) {
            PpsPlanEntity ppsPlanEntity = get(planId);
            if (ppsPlanEntity == null) {
                return;
            }
            if (ppsPlanEntity.getPlanStatus() >= 3) {
                throw new InkelinkException(ppsPlanEntity.getPlanNo() + "该计划编号已发布冻结无效.");
            }
            if (!ppsPlanEntity.getIsFreeze()) {
                throw new InkelinkException("'" + ppsPlanEntity.getPlanNo() + "'该计划编号未冻结,不需解冻.");
            }
            UpdateWrapper<PpsPlanEntity> upPlan = new UpdateWrapper<>();
            upPlan.lambda()
                    .set(PpsPlanEntity::getIsFreeze, false)
                    .eq(PpsPlanEntity::getId, planId);
            update(upPlan);
        }
    }

    /**
     * 设置工艺路径
     *
     * @param planIds   生产计划ID
     * @param processId 工艺路径ID
     */
    @Override
    public void setProcess(List<Long> planIds, Long processId) {
        for (Long planId : planIds) {
            PpsPlanEntity ppsPlanEntity = get(planId);
            //if (PpsPlanEntity.PlanStatus >= 2)
            //    throw new InkelinkException("'" + PpsPlanEntity.PlanNo + "'该计划编号状态不正确，不能设置工艺路径。");

            UpdateWrapper<PpsPlanEntity> upPlan = new UpdateWrapper<>();
            upPlan.lambda()
                    .set(PpsPlanEntity::getPrcPpsProductProcessId, processId)
                    .eq(PpsPlanEntity::getId, planId);
            update(upPlan);
            UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
            upOrder.lambda().set(PpsOrderEntity::getPrcPpsProductProcessId, processId)
                    .eq(PpsOrderEntity::getPlanNo, ppsPlanEntity.getPlanNo())
                    .lt(PpsOrderEntity::getOrderStatus, OrderStatusEnum.ProduceStart.code());
            ppsOrderExtendService.update(upOrder);
        }
    }

    /**
     * 查询工艺路径设置
     */
    @Override
    public PpsProductProcessEntity getProcess(String orderCategory) {
        if (StringUtils.isBlank(orderCategory)) {
            return null;
        }
        return ppsProductProcessService.getProcess(orderCategory);
    }

    /**
     * 获取BOM详细数据
     */
    private List<PmProductBomEntity> getBomData(String productMaterialNo, String bomVersions) {
        return pmProductBomVersionsProvider.getBomData(productMaterialNo, bomVersions);
    }

    /**
     * 获取BOM最新的版本
     */
    @Override
    public String getBomVersions(String productMaterialNo) {
        if (StringUtils.isBlank(productMaterialNo)) {
            return StringUtils.EMPTY;
        }
        return pmProductBomVersionsProvider.getBomVersions(productMaterialNo);
    }

    /**
     * 获取特征最新的版本
     *
     * @param productMaterialNo
     * @return
     */
    @Override
    public String getCharacteristicsVersions(String productMaterialNo) {
        return pmProductCharacteristicsVersionsService.getCharacteristicsVersions(productMaterialNo);
    }

    /*********** core 完成*************/

    /**
     * 创建备件计划（现场需要创建备件工单）
     *
     * @param materialNo   备件物料名称
     * @param materialName 备件物料号
     * @param lineCode     生产线体编码
     * @param endAvi
     * @param planQty      备件生产数量
     */
    @Override
    public void createSparePartsPlan(String materialNo, String materialName, String lineCode, String endAvi, int planQty) {

        List<ConditionDto> conBoms = new ArrayList<>();
        conBoms.add(new ConditionDto("materialNo", materialNo, ConditionOper.Equal));
        List<SortDto> sortBoms = new ArrayList<>();
        sortBoms.add(new SortDto("creationDate", ConditionDirection.DESC));
        TopDataDto topPmPro = new TopDataDto();
        topPmPro.setTop(3);
        topPmPro.setConditions(conBoms);
        topPmPro.setSorts(sortBoms);
        List<PmProductBomEntity> materialList = pmProductBomProvider.getTopDatas(topPmPro);
        if (materialList.size() == 0) {
            throw new InkelinkException("在整车BOM中未找到对应的备件物料号，无法创建备件计划");
        }
        PpsOrderEntity orderInfo = null;
        for (PmProductBomEntity model : materialList) {
            PmProductBomVersionsEntity bomVersion = pmProductBomVersionsProvider.getById(model.getBomVersionsId().toString());
            if (bomVersion != null && !bomVersion.getIsDelete()) {
                orderInfo = ppsOrderExtendService.getByBomVersion(bomVersion.getBomVersions());
                if (orderInfo != null) {
                    break;
                }
            }
        }
        if (orderInfo == null) {
            throw new InkelinkException("生产车辆中未找到该备件对应的车辆，无法获取更多的备件所需的生产信息");
        }

        /*int type = 1;
        if (identityHelper.getUserId() == null || identityHelper.getUserId() == 0) {
            type = 2;
        }*/
        int type = 2;
        PmAllDTO pmAll = pmVersionProvider.getObjectedPm();
        PmLineEntity lineInfo = pmAll.getLines().stream().filter(c -> StringUtils.equals(c.getLineCode(), lineCode)).findFirst().orElse(null);

        PmAviEntity aviInfo = pmAll.getAvis().stream().filter(c -> Objects.equals(c.getPrcPmLineId(), lineInfo.getId()))
                .sorted(Comparator.comparingInt(PmAviEntity::getAviDisplayNo)).findFirst().orElse(null);
        if (aviInfo == null) {
            throw new InkelinkException("未找到线体" + lineCode + "配置AVI站点");
        }
        PmAviEntity endAviInfo = null;
        if (StringUtils.isBlank(endAvi)) {
            endAviInfo = pmAll.getAvis().stream().filter(c -> Objects.equals(c.getPrcPmLineId(), lineInfo.getId()))
                    .sorted(Comparator.comparing(PmAviEntity::getAviDisplayNo).reversed())
                    .findFirst().orElse(null);
            if (endAviInfo == null) {
                throw new InkelinkException("未找到线体" + lineCode + "配置AVI站点");
            }
        } else {
            endAviInfo = pmAll.getAvis().stream().filter(c -> StringUtils.equals(c.getAviCode(), endAvi)).findFirst().orElse(null);
            if (endAviInfo == null) {
                throw new InkelinkException("请选择有效的完成站点");
            }
        }

        PpsPlanEntity planInfo = getFirstByPlanNo(orderInfo.getPlanNo());

        planInfo.setId(IdGenerator.getId());
        planInfo.setPlanNo(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_L));
        planInfo.setEstimatedStartDt(new Date());
        planInfo.setEstimatedEndDt(DateUtils.addDateHours(new Date(), 1));
        planInfo.setCharacteristic5(materialNo);
        planInfo.setCharacteristic4(materialName);
        //planInfo.setProductCode(materialNo);
        planInfo.setPlanQty(planQty);
        planInfo.setPlanSource(type);
        planInfo.setCharacteristic10(lineCode);
        planInfo.setPlanStatus(1);

        planInfo.setOrderCategory(OrderCategoryEnum.SparePart.codeString());
        planInfo.setStartAvi(aviInfo.getAviCode());
        planInfo.setEndAvi(endAviInfo.getAviCode());

        this.insert(planInfo);
        this.saveChange();

        List<String> planNos = new ArrayList<>();
        planNos.add(planInfo.getPlanNo());
        planLock(planNos, type);
    }

    /**
     * 删除备件计划（手动创建的备件）
     *
     * @param ids
     */
    @Override
    public void deleteStamping(List<Long> ids) {
        QueryWrapper<PpsPlanEntity> planQry = new QueryWrapper<>();
        planQry.lambda().in(PpsPlanEntity::getId, ids);
        List<PpsPlanEntity> datas = selectList(planQry);

        for (PpsPlanEntity item : datas) {
            if (!OrderCategoryEnum.SparePart.codeString().equals(item.getOrderCategory()) || item.getPlanSource() != 2) {
                throw new InkelinkException("计划" + item.getPlanNo() + "不能被删除，只能删除手动添加的备件计划");
            }
            QueryWrapper<PpsOrderEntity> orderQry = new QueryWrapper<>();
            orderQry.lambda().eq(PpsOrderEntity::getPlanNo, item.getPlanNo())
                    .gt(PpsOrderEntity::getOrderStatus, 3);
            if (ppsOrderExtendService.selectCount(orderQry) > 0) {
                throw new InkelinkException("计划" + item.getPlanNo() + "已经在生产，不能删除计划");
            }
            UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
            upOrder.lambda().eq(PpsOrderEntity::getPlanNo, item.getPlanNo());
            ppsOrderExtendService.delete(upOrder);

            UpdateWrapper<PpsEntryEntity> upEntry = new UpdateWrapper<>();
            upEntry.lambda().eq(PpsEntryEntity::getPlanNo, item.getPlanNo());
            ppsEntryService.delete(upEntry);
        }
        delete(ids.toArray(new Long[0]));
    }

    @Override
    public void export(List<ConditionDto> conditions, List<SortDto> sorts, String fileName, HttpServletResponse response) throws IOException {
        ConditionDto orderCategory = conditions.stream().filter(c -> "orderCategory".equals(c.getColumnName())).findFirst().orElse(null);
        if (orderCategory == null) {
            throw new InkelinkException("需要传入orderCategory参数区分计划类型   冲压计划、整车计划");
        }
        String enumOrder;

        if (String.valueOf(OrderCategoryEnum.Vehicle.code()).equals(orderCategory.getValue())) {
            enumOrder = OrderSheetTableName.Vehicle;
        } else if (String.valueOf(OrderCategoryEnum.Battery.code()).equals(orderCategory.getValue())) {
            enumOrder = OrderSheetTableName.Battery;
        } else if (String.valueOf(OrderCategoryEnum.PressureCasting.code()).equals(orderCategory.getValue())) {
            enumOrder = OrderSheetTableName.PressureCasting;
        } else if (String.valueOf(OrderCategoryEnum.Machining.code()).equals(orderCategory.getValue())) {
            enumOrder = OrderSheetTableName.Machining;
        } else if (String.valueOf(OrderCategoryEnum.Stamping.code()).equals(orderCategory.getValue())) {
            enumOrder = OrderSheetTableName.Stamping;
        } else if (String.valueOf(OrderCategoryEnum.CoverPlate.code()).equals(orderCategory.getValue())) {
            enumOrder = OrderSheetTableName.CoverPlate;
        } else if (String.valueOf(OrderCategoryEnum.SparePart.code()).equals(orderCategory.getValue())) {
            enumOrder = OrderSheetTableName.SparePart;
        } else {
            enumOrder = OrderSheetTableName.Vehicle;
        }

        super.setExcelColumnNames(orderDic.get(enumOrder));
        super.export(conditions, sorts, enumOrder, response);
    }

    /**
     * 处理即将导出的数据
     *
     * @param datas
     */
    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            if (data.containsKey("estimatedStartDt") && data.get("estimatedStartDt") != null) {
                data.put("estimatedStartDt", DateUtils.format((Date) data.get("estimatedStartDt"), "yyyy-MM-dd HH:mm:ss"));
            }
            if (data.containsKey("estimatedEndDt") && data.get("estimatedEndDt") != null) {
                data.put("estimatedEndDt", DateUtils.format((Date) data.get("estimatedEndDt"), "yyyy-MM-dd HH:mm:ss"));
            }
        }
    }

    @Override
    public void importExcel(InputStream is) throws Exception {

        Map<String, List<Map<String, String>>> datas = InkelinkExcelUtils.importExcel(is,
                orderDic.keySet().toArray(new String[orderDic.keySet().size()]));

        List<PmProductMaterialMasterEntity> pmMateria = pmProductMaterialMasterProvider.getAllDatas();

        for (Map.Entry<String, List<Map<String, String>>> item : datas.entrySet()) {
            if (item.getValue() == null || item.getValue().size() <= 0) {
                continue;
            }
            validImportDatas(item.getValue(), orderDic.get(item.getKey()));
            List<PpsPlanEntity> dataEts = convertExcelDataToEntity(item.getValue());
            // excelPm数据导入
            switch (item.getKey()) {
                case OrderSheetTableName.Vehicle: {
                    importExcelVehicle(pmMateria, dataEts);
                }
                break;
                case OrderSheetTableName.VehicleVin: {
                    importExcelVehicleVin(pmMateria, dataEts);
                }
                break;
                case OrderSheetTableName.Battery: {
                    importExcelBattery(pmMateria, dataEts);
                }
                break;
                case OrderSheetTableName.SparePart: {
                    importExcelSparePart(pmMateria, dataEts);
                }
                break;
                default:
                    break;
            }
        }
    }

    /**
     * 批量导入计划的VIN号（产品上线前）
     *
     * */
    private void importExcelVehicleVin(List<PmProductMaterialMasterEntity> pmMateria, List<PpsPlanEntity> dataEts) {
        List<PpsPlanEntity> planInfos = new ArrayList<>();
        for (PpsPlanEntity data : dataEts) {
            //导入选装包  外饰色 颜色
            //midApiLogService.setVeExtendInfo(data);
            boolean isData = false;
            PpsPlanEntity plan = getFirstByPlanNo(data.getPlanNo());

            if (plan != null) {
                if (StringUtils.isNotBlank(data.getAttribute6())) {
                    String vin = data.getAttribute6().trim();
                    plan.setAttribute6(vin); //VIN
                    //修改订单VIN（产品上线前）
                    PpsOrderEntity order = ppsOrderExtendService.getByPlanNo(data.getPlanNo());
                    if (order != null && order.getOrderStatus() < OrderStatusEnum.ProduceStart.code()
                         && StringUtils.isBlank(order.getBarcode())
                    ) {
                        order.setBarcode(vin);
                        ppsOrderExtendService.updateById(order);
                    }
                    isData = true;
                }
                if (StringUtils.isNotBlank(data.getAttribute7())) {
                    plan.setAttribute7(data.getAttribute7().trim()); //吊牌
                    isData = true;
                }
                if (isData) {
                        planInfos.add(plan);
                }
            }
        }
        if (!planInfos.isEmpty()) {
            this.updateBatchById(planInfos);
        }
    }

    private void importExcelVehicle(List<PmProductMaterialMasterEntity> pmMateria, List<PpsPlanEntity> dataEts) {
        List<PpsPlanEntity> planInfos = new ArrayList<>();
        PpsProductProcessEntity vprocessInfo = getProcess(OrderCategoryEnum.Vehicle.codeString());
        for (PpsPlanEntity data : dataEts) {
            data.setOrderCategory(OrderCategoryEnum.Vehicle.codeString());
            data.setOrderSign(StringUtils.EMPTY);
            data.setPlanStatus(1);
            data.setPlanSource(2);
            String chara4 = pmMateria.stream().filter(s -> StringUtils.equals(s.getMaterialNo(), data.getProductCode()))
                    .findFirst().orElse(new PmProductMaterialMasterEntity()).getMaterialCn();
            data.setCharacteristic4(chara4 == null ? StringUtils.EMPTY : chara4);
            if (!StringUtils.isBlank(data.getBomVersion())) {
              /*  List<PmProductBomEntity> pmbomversion = getBomData(data.getProductCode(), data.getBomVersion());
                if (pmbomversion == null) {
                    throw new InkelinkException(data.getProductCode() + "-" + data.getBomVersion() + " 不存在");
                }*/
                String ver = pmProductBomVersionsProvider.copyBom(data.getProductCode(), data.getBomVersion());
                if (StringUtils.isBlank(ver)) {
                    throw new InkelinkException("BOM:" + data.getProductCode() + "-" + data.getBomVersion() + " 不存在");
                }
                data.setBomVersion(ver);
            } else {
                data.setBomVersion(getBomVersions(data.getProductCode()));
            }
            if (!StringUtils.isBlank(data.getCharacteristicVersion())) {
                String ver = pmProductCharacteristicsVersionsService.copyCharacteristics(data.getProductCode(), data.getCharacteristicVersion());
                if (StringUtils.isBlank(ver)) {
                    throw new InkelinkException("特征:" + data.getProductCode() + "-" + data.getCharacteristicVersion() + " 不存在");
                }
                data.setCharacteristicVersion(ver);
            } else {
                data.setCharacteristicVersion(getCharacteristicsVersions(data.getProductCode()));
            }
            if (vprocessInfo != null) {
                data.setPrcPpsProductProcessId(vprocessInfo.getId());
            }
            //导入选装包  外饰色 颜色
            midApiLogService.setVeExtendInfo(data);
            planInfos.add(data);
        }
        saveExcelData(planInfos);
    }

    @Override
    public void setplaninfo(String planNo) {
        PpsPlanEntity planInfo = getFirstByPlanNo(planNo);
        //导入选装包  外饰色 颜色
        midApiLogService.setVeExtendInfo(planInfo);
        updateById(planInfo);

        PpsOrderEntity orderInfo = ppsOrderExtendService.getByPlanNo(planNo);
        if (orderInfo != null) {
            orderInfo.setCharacteristic1(planInfo.getCharacteristic1());
            orderInfo.setCharacteristic2(planInfo.getCharacteristic2());
            orderInfo.setCharacteristic3(planInfo.getCharacteristic3());
            orderInfo.setCharacteristic4(planInfo.getCharacteristic4());
            orderInfo.setCharacteristic5(planInfo.getCharacteristic5());
            orderInfo.setCharacteristic6(planInfo.getCharacteristic6());
            orderInfo.setCharacteristic7(planInfo.getCharacteristic7());
            orderInfo.setCharacteristic8(planInfo.getCharacteristic8());
            orderInfo.setCharacteristic9(planInfo.getCharacteristic9());
            orderInfo.setCharacteristic10(planInfo.getCharacteristic10());
            orderInfo.setAttribute1(planInfo.getAttribute1());
            orderInfo.setAttribute2(planInfo.getAttribute2());
            orderInfo.setAttribute3(planInfo.getAttribute3());
            ppsOrderExtendService.updateById(orderInfo);

            List<PpsEntryEntity> entrys = ppsEntryService.getPpsEntrysByOrderNo(orderInfo.getOrderNo(), 1);
            if (!entrys.isEmpty()) {
                for (PpsEntryEntity entryInfo : entrys) {
                    entryInfo.setAttribute1(planInfo.getAttribute1());
                    entryInfo.setAttribute2(planInfo.getAttribute2());
                    entryInfo.setAttribute3(planInfo.getAttribute3());
                }
                ppsEntryService.updateBatchById(entrys);
            }
        }
    }

    private void importExcelBattery(List<PmProductMaterialMasterEntity> pmMateria, List<PpsPlanEntity> dataEts) {
        List<PpsPlanEntity> planInfos = new ArrayList<>();
        PpsProductProcessEntity vprocessInfo = getProcess(OrderCategoryEnum.Battery.codeString());
        for (PpsPlanEntity data : dataEts) {
            data.setOrderCategory(OrderCategoryEnum.Battery.codeString());
            data.setOrderSign(StringUtils.EMPTY);
            data.setPlanStatus(1);
            data.setPlanSource(2);
            String chara4 = pmMateria.stream().filter(s -> StringUtils.equals(s.getMaterialNo(), data.getProductCode()))
                    .findFirst().orElse(new PmProductMaterialMasterEntity()).getMaterialCn();
            data.setCharacteristic4(chara4 == null ? StringUtils.EMPTY : chara4);

            if (!StringUtils.isBlank(data.getBomVersion())) {
                List<PmProductBomEntity> pmbomversion = getBomData(data.getProductCode(), data.getBomVersion());
                if (pmbomversion == null) {
                    throw new InkelinkException(data.getProductCode() + "-" + data.getBomVersion() + " 不存在");
                }
            } else {
                data.setBomVersion(getBomVersions(data.getProductCode()));
            }
            if (vprocessInfo != null) {
                data.setPrcPpsProductProcessId(vprocessInfo.getId());
            }
            planInfos.add(data);
        }
        saveExcelData(planInfos);
    }

    private void importExcelSparePart(List<PmProductMaterialMasterEntity> pmMateria, List<PpsPlanEntity> dataEts) {
        List<PpsPlanEntity> planInfos = new ArrayList<>();
        for (PpsPlanEntity data : dataEts) {
            data.setOrderCategory(OrderCategoryEnum.SparePart.codeString());
            data.setOrderSign(StringUtils.EMPTY);
            data.setPlanStatus(1);
            String chara4 = pmMateria.stream().filter(s -> StringUtils.equals(s.getMaterialNo(), data.getCharacteristic5()))
                    .findFirst().orElse(new PmProductMaterialMasterEntity()).getMaterialCn();
            data.setCharacteristic4(chara4 == null ? StringUtils.EMPTY : chara4);
            data.setPlanSource(2);
            if (!StringUtils.isBlank(data.getBomVersion())) {
                List<PmProductBomEntity> pmbomversion = getBomData(data.getProductCode(), data.getBomVersion());
                if (pmbomversion == null) {
                    throw new InkelinkException(data.getProductCode() + "-" + data.getBomVersion() + " 不存在");
                }
            } else {
                data.setBomVersion(getBomVersions(data.getProductCode()));
            }
            data.setCharacteristicVersion(getCharacteristicsVersions(data.getProductCode()));
            planInfos.add(data);
        }
        saveExcelData(planInfos);
    }

    /**
     * 获取导入模板
     *
     * @param fileName
     * @param response
     * @throws IOException
     */
    @Override
    public void getImportTemplate(String fileName, HttpServletResponse response) throws IOException {
        super.setExcelColumnNames(orderDic.get(fileName));
        super.getImportTemplate(fileName, response);
    }

    @Override
    public List<PpsPlanEntity> convertExcelDataToEntity(List<Map<String, String>> datas) throws Exception {
        List<Map<String, String>> lstMp = new ArrayList<>();
        for (Map<String, String> map : datas) {
            lstMp.add(convertExcelDataBySysConfig(currentModelClass(), map, this::getSysConfigurationMaps));
        }
        return super.convertExcelDataToEntity(lstMp);
    }

    /**
     * 获取系统配置
     */
    private Map<String, String> getSysConfigurationMaps(String category) {
        return sysConfigurationProvider.getSysConfigurationMaps(category);
    }

    /**
     * 保存excel数据
     */
    @Override
    public void saveExcelData(List<PpsPlanEntity> entities) {
        String planNos = StringUtils.EMPTY;
        //跳过存在的计划
        List<PpsPlanEntity> tmps = new ArrayList<>();
        for (PpsPlanEntity data : entities) {
            PpsPlanEntity plan = getFirstByPlanNo(data.getPlanNo());
            /*if (plan != null && !StringUtils.isBlank(plan.getPlanNo())) {
                planNos = String.join("|", data.getPlanNo());
            }*/
            if (plan == null) {
                tmps.add(data);
            }
        }
        if (StringUtils.isNotBlank(planNos)) {
            // planNos.Any()
            throw new InkelinkException("计划编号【" + planNos + "】已经存在，不允许导入！");
        }

        validMaterNo(tmps); //entities
        super.saveExcelData(tmps);//entities
    }

    void validMaterNo(List<PpsPlanEntity> entities) {
        for (PpsPlanEntity data : entities) {
            switch (data.getOrderCategory()) {
                case "3": //压铸
                case "4"://机加
                case "5"://冲压
                case "6"://盖板
                    //以上不关联BOM
                    return;
            }
            String bomVersion = pmProductBomVersionsProvider.getBomVersions(data.getProductCode());
            if (StringUtils.isBlank(bomVersion)) {
                throw new InkelinkException("计划" + data.getPlanNo() + "对应的产品编码" + data.getProductCode() + "未找到对应的BOM信息");
            }
            data.setBomVersion(bomVersion);
        }
    }

    protected class OrderSheetTableName {
        public static final String Vehicle = "整车计划";
        public static final String Battery = "电池计划";
        public static final String PressureCasting = "压铸计划";
        public static final String Machining = "机加计划";
        public static final String Stamping = "冲压计划";
        public static final String CoverPlate = "盖板计划";
        public static final String SparePart = "备件计划";
        public static final String VehicleVin = "整车计划VIN";
    }

    @Data
    protected class Bom {
        private String materialNo;
        private String version;
    }

    /***********导入导出*************/
}