package com.ca.mfd.prc.pqs.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboDataDTO;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.IUnitOfWorkService;
import com.ca.mfd.prc.common.utils.*;
import com.ca.mfd.prc.pqs.communication.entity.MidIccApiEntity;
import com.ca.mfd.prc.pqs.communication.entity.MidIccCategoryApiEntity;
import com.ca.mfd.prc.pqs.communication.service.IMidIccApiService;
import com.ca.mfd.prc.pqs.communication.service.IMidIccCategoryApiService;
import com.ca.mfd.prc.pqs.dto.*;
import com.ca.mfd.prc.pqs.entity.*;
import com.ca.mfd.prc.pqs.enums.PqsLogicStatusEnum;
import com.ca.mfd.prc.pqs.mapper.IPqsLogicMapper;
import com.ca.mfd.prc.pqs.mapper.IPqsProductDefectAnomalyMapper;
import com.ca.mfd.prc.pqs.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pqs.remote.app.eps.entity.EpsVehicleWoEntity;
import com.ca.mfd.prc.pqs.remote.app.eps.provider.EpsVehicleWoProvider;
import com.ca.mfd.prc.pqs.remote.app.otweb.provider.OtWebProvider;
import com.ca.mfd.prc.pqs.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pqs.remote.app.pm.dto.ShiftDTO;
import com.ca.mfd.prc.pqs.remote.app.pm.dto.VcurrentWorkStationInfo;
import com.ca.mfd.prc.pqs.remote.app.pm.entity.*;
import com.ca.mfd.prc.pqs.remote.app.pm.provider.PmShcCalendarProvider;
import com.ca.mfd.prc.pqs.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pqs.remote.app.pm.provider.PmWorkStationProvider;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsEntryEntity;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pqs.remote.app.pps.provider.PpsEntryProvider;
import com.ca.mfd.prc.pqs.remote.app.pps.provider.PpsLogicProvider;
import com.ca.mfd.prc.pqs.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.pqs.service.*;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author eric.zhou
 * @date 2023/4/17
 */
@Service
public class PqsLogicServiceImpl implements IPqsLogicService {

    private static final Logger logger = LoggerFactory.getLogger(PqsLogicServiceImpl.class);

    private static final String pqsQgFunctionKey = "PqsQgFunctionKey";

    @Autowired
    private IPqsQualityGateService pqsQualityGateService;
    @Autowired
    private IPqsQualityGateBlankService pqsQualityGateBlankService;
    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private PpsLogicProvider ppsLogicProvider;
    @Autowired
    private OtWebProvider otWebProvider;
    @Autowired
    private IPqsProductDefectAnomalyService pqsProductDefectAnomalyService;
    @Autowired
    private IPqsDefectAnomalyService pqsDefectAnomalyService;
    @Autowired
    private IPqsProductDefectAnomalyLogService pqsProductDefectAnomalyLogService;
    @Autowired
    private IPqsQualityGateAnomalyService pqsQualityGateAnomalyService;
    @Autowired
    private IdentityHelper identityHelper;
    @Autowired
    private IPqsLogicMapper pqsLogicDao;
    @Autowired
    private IPqsProductDefectAnomalyMapper pqsProductDefectAnomalyDao;
    @Autowired
    private PmWorkStationProvider pmWorkStationProvider;
    @Autowired
    private IPqsQualityGateWorkstationService pqsQualityGateWorkplaceService;
    @Autowired
    private IPqsDefectComponentService pqsDefectComponentService;
    @Autowired
    private IPqsDefectPositionService pqsDefectPositionService;
    @Autowired
    private IPqsDefectCodeService pqsDefectCodeService;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private PpsEntryProvider ppsEntryProvider;
    @Autowired
    private IPqsProductDefectAnomalyLogService pqsVehicleDefectAnomalyLogService;
    @Autowired
    private IPqsQualityMatrikService pqsQualityMatrikService;
    @Autowired
    private IPqsQualityMatrikAnomalyService pqsQualityMatrikAnomalyService;
    @Autowired
    private IPqsQualityMatrikWorkstationService pqsQualityMatrikWorkstationService;
    @Autowired
    private IPqsQualityMatrikTcService pqsQualityMatrikTcService;
    @Autowired
    private IPqsQualityGateWorkstationService pqsQualityGateWorkstationService;
    @Autowired
    private IPqsQualityGateTcService pqsQualityGateTcService;
    @Autowired
    private IPqsDefectAnomalyWpService pqsDefectAnomalyWpService;
    @Autowired
    private IPqsGradeService pqsGradeService;
    @Autowired
    private IPqsDeptService pqsDeptService;
    @Autowired
    private IPqsDefectAnomalyRiskDetailService pqsDefectAnomalyRiskDetailService;
    @Autowired
    private PpsOrderProvider ppsOrderProvider;
    @Autowired
    private IPqsProductQgCheckListRecordService pqsProductQgCheckListRecordService;
    @Autowired
    private IPqsQgCheckListService pqsQgCheckListService;
    @Autowired
    private PmShcCalendarProvider pmShcCalendarProvider;
    @Autowired
    private IUnitOfWorkService unitOfWorkService;
    @Autowired
    private IPqsQualityRouteRecordService pqsQualityRouteRecordService;
    @Autowired
    private IPqsQualityRoutePointService pqsQualityRoutePointService;
    @Autowired
    private IPqsQgWorkstationConfigService pqsQgWorkstationConfigService;
    @Autowired
    private IMidIccApiService iccApiService;
    @Autowired
    private IMidIccCategoryApiService iccCategoryApiService;
    @Autowired
    private EpsVehicleWoProvider epsVehicleWoProvider;

    /**
     * QG岗查看质量门检查图片数据
     *
     * @param qualityGateId 质量门ID
     * @return 图片列表
     */
    @Override
    public ShowQgWorkplaceAomalyDataInfo showQgWorkplaceAomalyData(String qualityGateId) {
        ShowQgWorkplaceAomalyDataInfo info = new ShowQgWorkplaceAomalyDataInfo();
        PqsQualityGateEntity pqsQualityGateInfo = pqsQualityGateService.get(qualityGateId);
        if (pqsQualityGateInfo != null) {
            info.setId(pqsQualityGateInfo.getId());
            info.setImage(pqsQualityGateInfo.getImage());
//            info.setIsLayout(pqsQualityGateInfo.getIsLayout());
        }
        //条件组合
        List<ConditionDto> conditionDtos = new ArrayList<>();
        ConditionDto conditionDto = new ConditionDto();
        conditionDto.setColumnName("pqsQualityGateId");
        conditionDto.setOperator(ConditionOper.Equal);
        conditionDto.setValue(qualityGateId);
        conditionDtos.add(conditionDto);
        List<GateBlankInfo> blankInfoList = new ArrayList<>();
        List<PqsQualityGateBlankEntity> pqsQualityGateBlankInfos = pqsQualityGateBlankService.getData(conditionDtos);
        for (PqsQualityGateBlankEntity m : pqsQualityGateBlankInfos) {
            GateBlankInfo gateBlankInfo = new GateBlankInfo();
            gateBlankInfo.setId(m.getId());
            gateBlankInfo.setBlockType(m.getBlockType());
            gateBlankInfo.setBlockHeight(m.getBlockHeight());
            gateBlankInfo.setBlockWidth(m.getBlockWidth());
            gateBlankInfo.setBlockTop(m.getBlockTop());
            gateBlankInfo.setBlockLeft(m.getBlockLeft());
            gateBlankInfo.setBlockJson(m.getBlockJson());
            blankInfoList.add(gateBlankInfo);
        }
        info.setGateBlanks(blankInfoList);
        return info;
    }

    /**
     * 获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 节点ID
     * @param tpsCode            tps编码
     * @return 缺陷列表
     */
    @Override
    public List<GetAnomalyByQualityGateBlankIdInfo> getAnomalyByQualityGateBlankId(String qualityGateBlankId, String tpsCode) {
        return pqsQualityGateAnomalyService.getAnomalyByQualityGateBlankId(qualityGateBlankId, tpsCode);
    }

    /**
     * 获取岗位常用缺陷
     */
    @Override
    public List<GetAnomalyWpByWorkPlaceAndSnInfo> getAnomalyWpByWorkPlaceAndSn(Serializable workplaceId) {
        return pqsLogicDao.getAnomalyWpByWorkPlaceAndSn(workplaceId);
    }

    /**
     * 根据岗位编号获取QG岗检查项列表
     */
    @Override
    public List<ComboInfoDTO> getQualityGateByWorkplaceId(Serializable workplaceId, String model) {
        return pqsLogicDao.getQualityGateByWorkplaceId(workplaceId, model);
    }

    /**
     * 缺陷活动
     */
    @Override
    public void modifyDefectAnomalyStatus(AnomalyActivity anomalyActivity) {
        PqsProductDefectAnomalyEntity info = pqsProductDefectAnomalyService.get(anomalyActivity.getDataId());
        if (info == null) {
            throw new InkelinkException("未找到对应的激活缺陷");
        }
        info.setStatus(anomalyActivity.getStatus());

        // 工艺修复缺陷，调用OT 降操作置为OK
        if (!info.getPrcEpsProductWoId().equals(Constant.DEFAULT_ID)
                && info.getStatus() == PqsLogicStatusEnum.QUALIFIED.code()) {
            logger.info("===========工艺修复缺陷，调用OT 降操作置为OK=============");
            otWebProvider.systemSaveWoStatus(info.getPrcEpsProductWoId(), 1);
        }

        // 已激活
        if (anomalyActivity.getStatus() == PqsLogicStatusEnum.ACTIVATED.code()) {
            info.setActivateUser(identityHelper.getUserName());
            info.setActivateTime(new Date());
        }
        // 已修复
        if (anomalyActivity.getStatus() == PqsLogicStatusEnum.RECOVERED.code()) {
            if (anomalyActivity.getRepairActivity() == null) {
                throw new InkelinkException("请填写修复内容");
            }
            info.setRepairTime(DateUtils.parse(anomalyActivity.getRepairActivity().getRepairTime(), DateUtils.DATE_TIME_PATTERN));
            info.setRepairUser(identityHelper.getUserName());
            info.setRepairWay(anomalyActivity.getRepairActivity().getRepairWay());
            info.setRepairSpendTime(anomalyActivity.getRepairActivity().getSpendTime());
            info.setRepairRemark(anomalyActivity.getRepairActivity().getRepairRemark());

        }
        // 未发现
        if (anomalyActivity.getStatus() == PqsLogicStatusEnum.NOT_FOUND.code()) {
            info.setRepairTime(new Date());
            info.setRepairUser(identityHelper.getUserName());
            info.setRepairWay(Strings.EMPTY);
            info.setRepairSpendTime(BigDecimal.valueOf(0));
            info.setRepairRemark(Strings.EMPTY);

        }
        // 合格
        if (anomalyActivity.getStatus() == PqsLogicStatusEnum.QUALIFIED.code()) {
        }
        // 不合格
        if (anomalyActivity.getStatus() == PqsLogicStatusEnum.DISQUALIFICATION.code()) {
            info.setRecheckUser(identityHelper.getUserName());
            info.setRecheckTime(new Date());
        }

        pqsProductDefectAnomalyService.update(info);

        writeAnomalyLog(anomalyActivity.getDataId(), info.getStatus());
    }

    /**
     * 激活缺陷
     */
    @Override
    public void activeAnomaly(ActiveAnomalyInfo info) {
        //遍历传入的缺陷
        for (AnomalyDto m : info.getAnomalyInfos()) {
            PqsProductDefectAnomalyEntity vAnomaly = null;
            //如果不等于自定义缺陷
            if (!Constant.DEFAULT_ID.equals(m.getAnomalyId())) {
                //判断缺陷是否已被激活，已被激活的缺陷未关闭将不会改变他的状态
                vAnomaly = pqsProductDefectAnomalyService.getData(conditions(m, info), false).stream().findFirst().orElse(null);
                //缺陷未被激活
                if (vAnomaly == null) {
                    //获取缺陷基础信息
                    PqsDefectAnomalyEntity anomaly = pqsDefectAnomalyService.get(m.getAnomalyId());
                    if (anomaly == null) {
                        throw new InkelinkException("[" + m.getDescription() + "]基础数据中未配置该缺陷");
                    }
                    List<String> defectDescriptionArr = getDefectArray(anomaly.getDefectAnomalyDescription());
                    //封装激活缺陷对象
                    vAnomaly = assemblyDataFirst(anomaly, defectDescriptionArr, m);
                } else {
                    //缺陷已被激活，未关闭
                    throw new InkelinkException("[" + m.getDescription() + "]缺陷已激活,请确认");
                }
            } else if ("11111111-1111-1111-1111-111111111111".equals(m.getAnomalyId()) && !Constant.DEFAULT_ID.equals(m.getWoId())) {
                vAnomaly = pqsProductDefectAnomalyService.getData(conditions(m, info), false).stream().findFirst().orElse(null);
                //缺陷未被激活
                if (vAnomaly == null) {
                    //封装激活缺陷对象
                    vAnomaly = assemblyDataSecond(m);
                } else {
                    //缺陷已被激活，未关闭
                    return;
                }
            } else {
                // 添加自定义缺陷
                List<String> defectCodeArr = getDefectArray(m.getCode());
                List<String> defectDescriptionArr = getDefectArray(m.getDescription());
                vAnomaly = assemblyDataThird(m, defectCodeArr, defectDescriptionArr);
                if (!StringUtils.isBlank(vAnomaly.getDefectAnomalyCode())) {
                    PqsProductDefectAnomalyEntity vvAomaly = pqsProductDefectAnomalyService.getData(conditions(m, info), false).stream().findFirst().orElse(null);
                    if (vvAomaly != null) {
                        throw new InkelinkException("[" + m.getDescription() + "]缺陷已激活,请确认");
                    }
                }
            }
            ResultVO<PmAllDTO> rspPmall = new ResultVO().ok(pmVersionProvider.getObjectedPm());
            if (rspPmall == null || !rspPmall.getSuccess()) {
                throw new InkelinkException("PM模块主信息失败。" + (rspPmall == null ? "" : rspPmall.getMessage()));
            }
            PmAllDTO pmAll = rspPmall.getData();
            //从PM中获取工厂结构数据
            PmWorkStationEntity pmWorkplaceInfo = pmAll.getAllStations().stream().filter(c -> c.getId().equals(info.getWorkplaceId())).findFirst().orElse(null);
            if (pmWorkplaceInfo == null) {
                throw new InkelinkException("在启用的工厂模型中未找到该岗位");
            }
            PmWorkStationEntity pmStationInfo = pmAll.getStations().stream().filter(c -> c.getId().equals(pmWorkplaceInfo.getId())).findFirst().orElse(null);
            PmWorkShopEntity shopInfo = pmAll.getShops().stream().filter(c -> c.getId().equals(pmWorkplaceInfo.getPrcPmWorkshopId())).findFirst().orElse(null);
            vAnomaly = assemblyDataFourth(vAnomaly, info, m, pmStationInfo, shopInfo, pmWorkplaceInfo);
            //从PM中获取工位对应的关联的QG岗信息
            ResultVO<List<String>> rspWorkplaceInfos = new ResultVO().ok(pmVersionProvider.getRelevanceQgWorkplaceByStationId(String.valueOf(pmWorkplaceInfo.getId())));
            if (rspWorkplaceInfos == null || !rspWorkplaceInfos.getSuccess()) {
                throw new InkelinkException("PM模块根据工位编号获取关联的QG岗位信息失败。" + (rspWorkplaceInfos == null ? "" : rspWorkplaceInfos.getMessage()));
            }
            List<String> workplaceInfos = rspWorkplaceInfos.getData();
            String qgWorkplaceId = workplaceInfos.get(0);
            String qgWorkplaceName = workplaceInfos.get(1);
            //如果是质量岗激活缺陷，关联的质量岗位是否包含自己  如果不包含就把自己岗位加进去
            if (pmWorkplaceInfo.getWorkstationType() == 2 && !qgWorkplaceName.contains(info.getWorkplaceName())) {
                qgWorkplaceName = StringUtils.isBlank(qgWorkplaceName) ? info.getWorkplaceName() : "," + info.getWorkplaceName();
            }
            vAnomaly.setQgWorkstationCode(qgWorkplaceId);
            vAnomaly.setQgWorkstationName(qgWorkplaceName);
            // 激活
            if (vAnomaly.getStatus() == PqsLogicStatusEnum.ACTIVATED.code()) {
                vAnomaly.setActivateUser(identityHelper.getUserName());
//            anomalyExtendInfo.setActivateUserId(identityHelper.getUserId());
                vAnomaly.setActivateTime(new Date());
            }
            // 修复
            if (vAnomaly.getStatus() == PqsLogicStatusEnum.RECOVERED.code()
                    || vAnomaly.getStatus() == PqsLogicStatusEnum.NOT_FOUND.code()) {
                vAnomaly.setRepairTime(new Date());
                vAnomaly.setRepairUser(identityHelper.getUserName());
//            anomalyExtendInfo.setRepairUserId(identityHelper.getUserId());
            }
            // 复查
            if (vAnomaly.getStatus() == PqsLogicStatusEnum.QUALIFIED.code()
                    || vAnomaly.getStatus() == PqsLogicStatusEnum.DISQUALIFICATION.code()) {
                vAnomaly.setRecheckTime(new Date());
                vAnomaly.setRecheckUser(identityHelper.getUserName());
//            anomalyExtendInfo.setRecheckUserId(identityHelper.getUserId());
            }
            pqsProductDefectAnomalyService.insert(vAnomaly);
            AnomalyActivity anomalyActivity = AnomalyActivity.builder().dataId(vAnomaly.getId()).status(vAnomaly.getStatus()).repairActivity(null).build();
            activeAnomalyRecord(anomalyActivity);
            //记录缺陷激活次数
            /*if (!UUIDUtils.isGuidEmpty(vAnomaly.getDefectAnomalyCode())) {
                pqsVehicleDefectAnomalySortService.statisticsAnomaly(vAnomaly.getWorkShopCode(), vAnomaly.getDefectAnomalyCode());
            }*/
        }
    }

    private List<ConditionDto> conditions(AnomalyDto anomalyDto, ActiveAnomalyInfo info) {
        List<ConditionDto> conditionDtoList = new ArrayList<>();
        conditionDtoList.add(new ConditionDto("PqsDefectAnomalyId", String.valueOf(anomalyDto.getAnomalyId()), ConditionOper.Equal));
        conditionDtoList.add(new ConditionDto("Sn", info.getTpsCode(), ConditionOper.Equal));
        conditionDtoList.add(new ConditionDto("Status", "4", ConditionOper.Unequal));
        return conditionDtoList;
    }

    /**
     * 第一次组装产品缺陷数数据
     *
     * @return
     */
    private PqsProductDefectAnomalyEntity assemblyDataFirst(PqsDefectAnomalyEntity anomaly, List<String> defectDescriptionArr, AnomalyDto m) {

        PqsProductDefectAnomalyEntity vAnomaly = new PqsProductDefectAnomalyEntity();
        vAnomaly.setDefectAnomalyCode(anomaly.getDefectAnomalyCode());
//        vAnomaly.setPqsDefectAnomalyCode(anomaly.getCode());
        vAnomaly.setDefectAnomalyDescription(anomaly.getDefectAnomalyDescription());
        //TODO 现在字段都没有
//        vAnomaly.setPqsDefectComponentCode(anomaly.getPqsComponentCode());
//        vAnomaly.setPqsDefectComponentDescription(defectDescriptionArr.get(0));
//        vAnomaly.setPqsDefectCode(anomaly.getPqsDefectCode());
//        vAnomaly.setPqsDefectDescription(defectDescriptionArr.get(1));
//        vAnomaly.setPqsDefectPositionCode(anomaly.getPqsDefectPositionCode());
//        vAnomaly.setPqsDefectPositionDescription(defectDescriptionArr.get(2));
        vAnomaly.setGradeCode(m.getLevel() == 0 ? anomaly.getGradeCode() : String.valueOf(m.getLevel()));
        vAnomaly.setResponsibleDeptCode(StringUtils.isBlank(m.getDutyArea()) ? anomaly.getResponsibleDeptCode() : m.getDutyArea());
        vAnomaly.setImg(m.getImg());
        vAnomaly.setSource(m.getSource());
        return vAnomaly;
    }

    /**
     * 第二次组装产品缺陷数数据
     *
     * @return
     */
    private PqsProductDefectAnomalyEntity assemblyDataSecond(AnomalyDto m) {

        PqsProductDefectAnomalyEntity vAnomaly = new PqsProductDefectAnomalyEntity();
        vAnomaly.setDefectAnomalyCode(Constant.EMPTY_ID);
//        vAnomaly.setPqsDefectAnomalyCode(m.getCode());
        vAnomaly.setDefectAnomalyDescription(m.getDescription());
        //TODO 现在字段都没有
//        vAnomaly.setPqsDefectComponentCode("");
//        vAnomaly.setPqsDefectComponentDescription("");
//        vAnomaly.setPqsDefectCode("");
//        vAnomaly.setPqsDefectDescription("");
//        vAnomaly.setPqsDefectPositionCode("");
//        vAnomaly.setPqsDefectPositionDescription("");
        vAnomaly.setGradeCode(m.getLevel() + "");
        vAnomaly.setResponsibleDeptCode(m.getDutyArea());
        vAnomaly.setImg(m.getImg());
        vAnomaly.setSource(m.getSource());
        return vAnomaly;
    }

    /**
     * 第三次组装产品缺陷数数据
     *
     * @return
     */
    private PqsProductDefectAnomalyEntity assemblyDataThird(AnomalyDto m, List<String> defectCodeArr, List<String> defectDescriptionArr) {

        PqsProductDefectAnomalyEntity vAnomaly = new PqsProductDefectAnomalyEntity();
        vAnomaly.setDefectAnomalyCode(m.getCode().replace("-", ""));
        vAnomaly.setDefectAnomalyDescription(m.getDescription().replace("-", ""));
//        vAnomaly.setPqsDefectComponentCode(defectCodeArr.get(0));
//        vAnomaly.setPqsDefectComponentDescription(defectDescriptionArr.get(0));
//        vAnomaly.setPqsDefectCode(defectCodeArr.get(2));
//        vAnomaly.setPqsDefectDescription(defectDescriptionArr.get(2));
//        vAnomaly.setPqsDefectPositionCode(defectCodeArr.get(1));
//        vAnomaly.setPqsDefectPositionDescription(defectDescriptionArr.get(1));
        vAnomaly.setGradeCode(m.getLevel() + "");
        vAnomaly.setResponsibleDeptCode(m.getDutyArea());
        vAnomaly.setImg(m.getImg());
        vAnomaly.setSource(m.getSource());
        return vAnomaly;
    }

    /**
     * 第四次组装产品缺陷数数据
     *
     * @return
     */
    private PqsProductDefectAnomalyEntity assemblyDataFourth(PqsProductDefectAnomalyEntity vAnomaly, ActiveAnomalyInfo info, AnomalyDto m,
                                                             PmWorkStationEntity pmStationInfo, PmWorkShopEntity shopInfo, PmWorkStationEntity pmWorkplaceInfo) {
        vAnomaly.setJsonData(m.getJsonData());
        vAnomaly.setId(IdGenerator.getId());
        vAnomaly.setSn(info.getTpsCode());
        vAnomaly.setImg(m.getImg());
        vAnomaly.setPrcEpsProductWoId(m.getWoId());
        vAnomaly.setStatus(1);
        vAnomaly.setResponsibleTeamNo(vAnomaly.getResponsibleTeamNo());
        vAnomaly.setPrcPpsShcShiftId(ConvertUtils.stringToLong(ppsLogicProvider.getPresentShcShiftId(String.valueOf(shopInfo.getId()))));
        vAnomaly.setOrganizationCode(String.valueOf(shopInfo.getPrcPmOrganizationId()));
        vAnomaly.setWorkshopCode(String.valueOf(pmWorkplaceInfo.getPrcPmWorkshopId()));
//        vAnomaly.setPmAreaId(pmWorkplaceInfo.getPmAreaId());
        vAnomaly.setWorkstationCode(pmWorkplaceInfo.getWorkstationCode());
        vAnomaly.setWorkstationName(pmWorkplaceInfo.getWorkstationName());
        vAnomaly.setTeamNo(pmWorkplaceInfo.getTeamNo());

        return vAnomaly;
    }

    /**
     * 根据输入字符串 返回三段式缺陷描述或缺陷代码 0：组件  1 缺陷方位 2：缺陷分类
     */
    private List<String> getDefectArray(String sourceString) {
        String[] defectSourceArr = sourceString.split("-");
        return defectSourceArr.length == 3 ? Arrays.asList(defectSourceArr) : Arrays.asList("", "", "");
    }

    /**
     * 产品缺陷记录
     */
    @Override
    public void activeAnomalyRecord(AnomalyActivity anomalyActivity) {

        //封装激活缺陷日志
        String statusDis = "";
        switch (anomalyActivity.getStatus()) {
            case 1:
                statusDis = "已激活";
                break;
            case 2:
                statusDis = "已修复";
                break;
            case 3:
                statusDis = "未发现";
                break;
            case 4:
                statusDis = "合格";
                break;
            case 5:
                statusDis = "不合格";
                break;
            default:
                break;
        }

        PqsProductDefectAnomalyLogEntity logInfo = new PqsProductDefectAnomalyLogEntity();
        logInfo.setPrcPqsProductDefectAnomalyId(anomalyActivity.getDataId());
        logInfo.setRemark(identityHelper.getUserName() + "将状态更改为" + statusDis);
        logInfo.setStatus(anomalyActivity.getStatus());

        if (anomalyActivity.getRepairActivity() != null) {
            String remark = ",修复方式：" + anomalyActivity.getRepairActivity().getRepairWay() + ",修复时间：" + anomalyActivity.getRepairActivity().getRepairTime() + ", 修复花费：" + anomalyActivity.getRepairActivity().getSpendTime() + "工时";
            logInfo.setRemark(logInfo.getRemark() + remark);
        }
        if (!StringUtils.isBlank(anomalyActivity.getRemark())) {
            logInfo.setRemark(logInfo.getRemark() + ",备注信息：" + anomalyActivity.getRemark());
        }
        logInfo.setRemark(logInfo.getRemark() + "。");
        pqsProductDefectAnomalyLogService.insert(logInfo);
        pqsProductDefectAnomalyLogService.saveChange();
    }

    /**
     * 获取已激活的缺陷列表
     *
     * @param para 条件参数
     * @return 缺陷列表
     */
    @Override
    public PageData<ProductDefectAnomalyReponse> getVehicleDefectAnomalyList(GetDefectAnomalyRequest para) {
        PageData<ProductDefectAnomalyReponse> pageData = new PageData<>();
        Page<ProductDefectAnomalyReponse> page = new Page<>(para.getPageIndex(), para.getPageSize());

        // 质量门
        if (StringUtils.isNotEmpty(para.getQgWorkstationCode())) {
            // 从PM中获取工厂结构数据
            ResultVO<PmAllDTO> stationList = new ResultVO().ok(pmVersionProvider.getObjectedPm());
            if (stationList == null || !stationList.getSuccess()) {
                throw new InkelinkException("PM模块根据工位编号获取关联的QG岗位信息失败。" + (stationList == null ? "" : stationList.getMessage()));
            }
            PmWorkStationEntity workstationInfo = stationList.getData().getStations()
                    .stream().filter(s -> s.getWorkstationCode().equals(para.getQgWorkstationCode()))
                    .findFirst().orElse(null);
            if (workstationInfo == null) {
                throw new InkelinkException("在启用的工厂模型中未找到该岗位");
            }

            if (StringUtils.isNotEmpty(workstationInfo.getStations())) {
                para.setQgWorkstationCode(para.getQgWorkstationCode() + ",");
            } else {
                para.setQgWorkstationCode(StringUtils.EMPTY);
                para.setShopCode(StringUtils.EMPTY);
            }
        }

        Page<ProductDefectAnomalyReponse> query = pqsProductDefectAnomalyDao.getVehicleDefectAnomalyList(page, para);
        pageData.setDatas(query.getRecords());

        pageData.setTotal((int) query.getTotal());
        return pageData;
    }


    /**
     * 修改已激活缺陷状态
     *
     * @param dataId 数据ID
     * @param status 状态
     */
    @Override
    public void modifyVehicleDefectAnomalyStatus(String dataId, Integer status) {
        PqsProductDefectAnomalyEntity info = pqsProductDefectAnomalyService.get(dataId);
        if (info == null) {
            throw new InkelinkException("未找到对应的激活缺陷");
        }
        info.setStatus(status);
        // 激活
        if (info.getStatus() == PqsLogicStatusEnum.ACTIVATED.code()) {
            info.setActivateUser(identityHelper.getUserName());
//            info.setActivateUserId(identityHelper.getUserId());
            info.setActivateTime(new Date());
        }
        // 修复
        if (info.getStatus() == PqsLogicStatusEnum.RECOVERED.code()
                || info.getStatus() == PqsLogicStatusEnum.NOT_FOUND.code()) {
            info.setRepairTime(new Date());
            info.setRepairUser(identityHelper.getUserName());
//            info.setRepairUserId(identityHelper.getUserId());
        }
        // 复查
        if (info.getStatus() == PqsLogicStatusEnum.QUALIFIED.code()
                || info.getStatus() == PqsLogicStatusEnum.DISQUALIFICATION.code()) {
            info.setRecheckTime(new Date());
            info.setRecheckUser(identityHelper.getUserName());
//            info.setRecheckUserId(identityHelper.getUserId());
        }
        pqsProductDefectAnomalyService.update(info);
        AnomalyActivity anomalyActivity = new AnomalyActivity();
        anomalyActivity.setDataId(info.getId());
        anomalyActivity.setStatus(info.getStatus());
        anomalyActivity.setRepairActivity(null);
        activeAnomalyRecord(anomalyActivity);
    }

    /**
     * 添加激活缺陷备注
     *
     * @param dataId dataId
     * @param remark remark
     */
    @Override
    public void addVehicleDefectAnomalyRemark(String dataId, String remark) {
        if (StringUtils.isBlank(dataId)) {
            return;
        }
        PqsProductDefectAnomalyEntity info = pqsProductDefectAnomalyService.get(dataId);
        if (info == null) {
            throw new InkelinkException("未找到对应的激活缺陷");
        }
        PqsProductDefectAnomalyLogEntity logInfo = new PqsProductDefectAnomalyLogEntity();
        logInfo.setPrcPqsProductDefectAnomalyId(ConvertUtils.stringToLong(dataId));
        logInfo.setRemark(identityHelper.getUserName() + "添加备注【" + remark + "】");
        logInfo.setStatus(info.getStatus());
        pqsProductDefectAnomalyLogService.insert(logInfo);
    }

    /**
     * 获取所有岗位
     *
     * @param info
     * @return
     */
    @Override
    public PageData<GetWorkStationListInfo> getWorkPlaceList(GetWorkPlaceListParaInfo info) {
        List<ConditionDto> conditions = new ArrayList<>(1);
        ConditionDto conditionDto = new ConditionDto();
        conditionDto.setColumnName("name");
        conditionDto.setOperator(ConditionOper.AllLike);
        conditionDto.setValue(info.getWorkPlaceName());
        conditions.add(conditionDto);
        ResultVO<PageData<VcurrentWorkStationInfo>> rspWorkPlaceList = pmWorkStationProvider.getCurrentWorkplaceList(info.getPageIndex(), info.getPageSize(), conditions);
        if (rspWorkPlaceList == null || !rspWorkPlaceList.getSuccess()) {
            throw new InkelinkException("PM模块主信息失败。" + (rspWorkPlaceList == null ? "" : rspWorkPlaceList.getMessage()));
        }
        PageData<VcurrentWorkStationInfo> workPlaceList = rspWorkPlaceList.getData();
        List<GetWorkStationListInfo> list = new ArrayList<>(workPlaceList.getDatas().size());
        for (VcurrentWorkStationInfo m : workPlaceList.getDatas()) {
            GetWorkStationListInfo getWorkPlaceListInfo = new GetWorkStationListInfo();
            getWorkPlaceListInfo.setWorkstationId(m.getId());
            getWorkPlaceListInfo.setWorkstationName(m.getWorkstationName());
            list.add(getWorkPlaceListInfo);
        }
        PageData<GetWorkStationListInfo> pageInfo = new PageData<>(new ArrayList<>(), 0);
        pageInfo.setPageIndex(workPlaceList.getPageIndex());
        pageInfo.setPageSize(workPlaceList.getPageSize());
        pageInfo.setDatas(list);
        pageInfo.setTotal(workPlaceList.getTotal());
        return pageInfo;
    }

    /**
     * 根据岗位编号获取岗位配置的常用缺陷
     *
     * @param workPlaceId
     * @return
     */
    @Override
    public List<DefectShowInfo> getAnomalyListByWorkPlaceId(String workPlaceId) {
        return pqsLogicDao.getAnomalyListByWorkPlaceId(workPlaceId);
    }

    /**
     * 获取QG所有岗位
     *
     * @return
     */
    @Override
    public List<GetWorkStationListInfo> getWorkPlaceListByQg() {
        List<ConditionDto> conditions = new ArrayList<>(1);
        ConditionDto conditionDto = new ConditionDto();
        conditionDto.setColumnName("WORKSTATION_TYPE");
        conditionDto.setOperator(ConditionOper.Equal);
        conditionDto.setValue("2");
        conditions.add(conditionDto);
        ResultVO<PageData<VcurrentWorkStationInfo>> rspWorkPlaceList = pmWorkStationProvider.getCurrentWorkplaceList(1, 1000, conditions);
        if (rspWorkPlaceList == null || !rspWorkPlaceList.getSuccess()) {
            throw new InkelinkException("PM模块主信息失败。" + (rspWorkPlaceList == null ? "" : rspWorkPlaceList.getMessage()));
        }
        PageData<VcurrentWorkStationInfo> workPlaceList = rspWorkPlaceList.getData();
        List<GetWorkStationListInfo> list = new ArrayList<>(workPlaceList.getDatas().size());
        for (VcurrentWorkStationInfo m : workPlaceList.getDatas()) {
            GetWorkStationListInfo getWorkPlaceListInfo = new GetWorkStationListInfo();
            getWorkPlaceListInfo.setWorkstationId(m.getId());
            getWorkPlaceListInfo.setWorkstationName(m.getWorkstationName());
            list.add(getWorkPlaceListInfo);
        }
        return list;
    }

    /**
     * 获取缺陷数据中组件列表
     *
     * @param key
     * @return
     */
    @Override
    public List<ComboInfoDTO> getAnomalyComponent(String key) {
        return pqsLogicDao.getAnomalyComponent(key);
    }

    /**
     * 根据组件代码获取缺陷数据中位置列表
     *
     * @param componentCode
     * @return
     */
    @Override
    public List<ComboInfoDTO> getAnomalyPosition(String componentCode) {
        return pqsLogicDao.getAnomalyPosition(componentCode);
    }

    /**
     * 根据组件代码和位置代码获取缺陷数据中分类列表
     *
     * @param componentCode
     * @param positionCode
     * @return
     */
    @Override
    public List<ComboInfoDTO> getAnomalyCode(String componentCode, String positionCode) {
        return pqsLogicDao.getAnomalyCode(componentCode, positionCode);
    }

    /**
     * 获取QG配置数据信息
     *
     * @param qualityGateId
     * @return
     */
    @Override
    public ShowQgAnomalyConfigurationInfo getQgAnomalyData(String qualityGateId) {
        ShowQgAnomalyConfigurationInfo info = new ShowQgAnomalyConfigurationInfo();
        PqsQualityGateEntity pqsQualityGateInfo = pqsQualityGateService.get(qualityGateId);
        if (pqsQualityGateInfo != null) {
            info.setId(pqsQualityGateInfo.getId());
            info.setName(pqsQualityGateInfo.getItemName());
//            info.setModel(pqsQualityGateInfo.getModel());
            info.setImage(pqsQualityGateInfo.getImage());
//            info.setIsLayout(pqsQualityGateInfo.getIsLayout());
            info.setRemark(pqsQualityGateInfo.getRemark());

            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(new ConditionDto("PQS_QUALITY_GATE_ID", qualityGateId, ConditionOper.Equal));
            List<GetQgCheckWorkPlacesInfo> workplaceList = pqsQualityGateWorkplaceService.getData(conditionInfos).stream()
                    .map(t -> {
                        GetQgCheckWorkPlacesInfo o = new GetQgCheckWorkPlacesInfo();
                        o.setId(ConvertUtils.stringToLong(t.getWorkstationCode()));
                        o.setWorkPlaceName("");
                        return o;
                    }).collect(Collectors.toList());
            ResultVO<PmAllDTO> rspPmall = new ResultVO().ok(pmVersionProvider.getObjectedPm());
            if (rspPmall == null || !rspPmall.getSuccess()) {
                throw new InkelinkException("PM模块主信息失败。" + (rspPmall == null ? "" : rspPmall.getMessage()));
            }
            List<PmWorkStationEntity> workplaceInfoList = rspPmall.getData().getAllStations();
            for (GetQgCheckWorkPlacesInfo m : workplaceList) {
                info.getWorkPlaces().add(new ComboInfoDTO(workplaceInfoList.stream().filter(c -> c.getId().equals(m.getId())).findFirst().orElse(new PmWorkStationEntity()).getWorkstationName()
                        , String.valueOf(m.getId())));
            }
            List<PqsQualityGateBlankEntity> pqsQualityGateBlankInfos = pqsQualityGateBlankService.getData(conditionInfos);
            for (PqsQualityGateBlankEntity m : pqsQualityGateBlankInfos) {
                GateBlankInfo gateBlankInfo = new GateBlankInfo();
                gateBlankInfo.setId(m.getId());
                gateBlankInfo.setBlockType(m.getBlockType());
                gateBlankInfo.setBlockHeight(m.getBlockHeight());
                gateBlankInfo.setBlockWidth(m.getBlockWidth());
                gateBlankInfo.setBlockTop(m.getBlockTop());
                gateBlankInfo.setBlockLeft(m.getBlockLeft());
                gateBlankInfo.setBlockJson(m.getBlockJson());
                info.getGateBlanks().add(gateBlankInfo);
            }
        }
        return info;
    }

    /**
     * 根据色块编号获取色块对应的缺陷列表
     *
     * @param gateBlankId
     * @return
     */
    @Override
    public InitConfigurationItemInfo initConfigurationItem(String gateBlankId) {
        InitConfigurationItemInfo info = new InitConfigurationItemInfo();
        List<GetGateAnomalyListInfo> list = pqsLogicDao.getGateAnomalyListInfo(gateBlankId);
        if (list.size() > 0) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(new ConditionDto("CODE", list.get(0).getComponentCode(), ConditionOper.Equal));
            List<PqsDefectComponentEntity> componentEntityList = pqsDefectComponentService.getData(conditionInfos);
            if (CollectionUtil.isNotEmpty(componentEntityList)) {
                info.getComponent().setValue(componentEntityList.get(0).getDefectComponentCode());
                info.getComponent().setText(componentEntityList.get(0).getDefectComponentDescription());
            }
            conditionInfos.clear();
            conditionInfos.add(new ConditionDto("CODE", list.get(0).getComponentCode(), ConditionOper.Equal));
            List<PqsDefectPositionEntity> positionEntityList = pqsDefectPositionService.getData(conditionInfos);
            if (CollectionUtil.isNotEmpty(positionEntityList)) {
                info.getPosition().setValue(positionEntityList.get(0).getDefectPositionCode());
                info.getPosition().setText(positionEntityList.get(0).getDefectPositionDescription());
            }
            info.setAnomalyIds(list.stream().map(c -> c.getId()).collect(Collectors.toList()));
        }
        return info;
    }

    /**
     * 添加QG配置相关的岗位
     *
     * @param workPlaceIds
     * @param pqsQualityGateId
     */
    private void sendQgAnomalyDataWorkPlace(List<String> workPlaceIds, Long pqsQualityGateId) {
        List<PqsQualityGateWorkstationEntity> pqsQualityGateWorkplaceInfoList = new ArrayList<>();
        for (String m : workPlaceIds) {
            PqsQualityGateWorkstationEntity entity = new PqsQualityGateWorkstationEntity();
            entity.setId(IdGenerator.getId());
            entity.setPrcPqsQualityGateId(pqsQualityGateId);
            entity.setWorkstationCode(m);
            pqsQualityGateWorkplaceInfoList.add(entity);
        }
        if (pqsQualityGateWorkplaceInfoList.size() > 0) {
            pqsQualityGateWorkplaceService.insertBatch(pqsQualityGateWorkplaceInfoList);
        }
    }

    /**
     * 添加QG配置相关的色块及缺陷数据
     *
     * @param gateBlanks
     * @param pqsQualityGateId
     */
    private void sendQgAnomalyDataWorkPlaceGateBlanks(List<GateBlank> gateBlanks, Long pqsQualityGateId) {
        List<PqsQualityGateBlankEntity> gateBlankList = new ArrayList<>();
        List<PqsQualityGateAnomalyEntity> pqsQualityGateAnomalyInfoList = new ArrayList<>();
        /**
         * 添加色块
         */
        for (GateBlank m : gateBlanks) {
            PqsQualityGateBlankEntity entity = new PqsQualityGateBlankEntity();
            Long gateBlankId = IdGenerator.getId();
            entity.setId(gateBlankId);
            entity.setPrcPqsQualityGateId(pqsQualityGateId);
            entity.setBlockType(m.getBlockType() != null ? m.getBlockType() : 0);
            entity.setBlockHeight(m.getBlockHeight() != null ? m.getBlockHeight() : BigDecimal.ZERO);
            entity.setBlockWidth(m.getBlockWidth() != null ? m.getBlockWidth() : BigDecimal.ZERO);
            entity.setBlockTop(m.getBlockTop() != null ? m.getBlockTop() : BigDecimal.ZERO);
            entity.setBlockLeft(m.getBlockLeft() != null ? m.getBlockLeft() : BigDecimal.ZERO);
            entity.setBlockJson(m.getBlockJson());
            gateBlankList.add(entity);

            /**
             * 添加色块对应的缺陷
             */
            for (String anomalyId : m.getAnomalyIds()) {
                PqsQualityGateAnomalyEntity anomalyEntity = new PqsQualityGateAnomalyEntity();
                anomalyEntity.setPrcPqsQualityGateBlankId(gateBlankId);
                anomalyEntity.setDefectAnomalyCode(anomalyId);
                pqsQualityGateAnomalyInfoList.add(anomalyEntity);
            }
        }
        pqsQualityGateBlankService.insertBatch(gateBlankList);
        pqsQualityGateAnomalyService.insertBatch(pqsQualityGateAnomalyInfoList);
    }

    /**
     * 提交QG配置数据信息
     *
     * @param info
     */
    @Override
    public void sendQgAnomalyData(SendQgAnomalyConfigurationInfo info) {
        ResultVO<PmAllDTO> rspPmall = new ResultVO().ok(pmVersionProvider.getObjectedPm());
        if (rspPmall == null || !rspPmall.getSuccess()) {
            throw new InkelinkException("PM模块主信息失败。" + (rspPmall == null ? "" : rspPmall.getMessage()));
        }
        PmAllDTO pmAll = rspPmall.getData();
        String workplaceNames = "";
        for (String m : info.getWorkPlaceIds()) {
            PmWorkStationEntity workplaceInfo = pmAll.getAllStations().stream().filter(c -> c.getId().equals(m)).findFirst().orElse(null);
            if (workplaceInfo != null) {
                if ("".equals(workplaceNames)) {
                    workplaceNames = workplaceInfo.getWorkstationName();
                } else {
                    workplaceNames = new StringBuilder().append(workplaceNames).append(",").append(workplaceInfo.getWorkstationName()).toString();
                }
            }
        }
        PqsQualityGateEntity pqsQualityGateInfo = null;
        if (Constant.DEFAULT_ID.equals(info.getId())) {
            pqsQualityGateInfo = new PqsQualityGateEntity();
            pqsQualityGateInfo.setId(IdGenerator.getId());
            pqsQualityGateInfo.setItemName(info.getName());
//            pqsQualityGateInfo.setModel(info.getModel());
            pqsQualityGateInfo.setImage(info.getImage());
            pqsQualityGateInfo.setRemark(info.getRemark());
            pqsQualityGateInfo.setWorkstationNames(workplaceNames);
            pqsQualityGateService.insert(pqsQualityGateInfo);
        } else {
            LambdaUpdateWrapper<PqsQualityGateEntity> upset = new LambdaUpdateWrapper<>();
            upset.eq(PqsQualityGateEntity::getId, info.getId());
            upset.set(PqsQualityGateEntity::getItemName, info.getName());
//            upset.set(PqsQualityGateEntity::getModel, info.getModel());
            upset.set(PqsQualityGateEntity::getImage, info.getImage());
            upset.set(PqsQualityGateEntity::getRemark, info.getRemark());
            upset.set(PqsQualityGateEntity::getWorkstationNames, workplaceNames);
            pqsQualityGateService.update(upset);

            UpdateWrapper<PqsQualityGateWorkstationEntity> delWoData = new UpdateWrapper<>();
            delWoData.lambda().eq(PqsQualityGateWorkstationEntity::getPrcPqsQualityGateId, info.getId());
            pqsQualityGateWorkplaceService.delete(delWoData);
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(new ConditionDto("PQS_QUALITY_GATE_ID", String.valueOf(info.getId()), ConditionOper.Equal));
            List<PqsQualityGateBlankEntity> pqsQualityGateBlanks = pqsQualityGateBlankService.getData(conditionInfos);
            for (PqsQualityGateBlankEntity m : pqsQualityGateBlanks) {
                UpdateWrapper<PqsQualityGateAnomalyEntity> del = new UpdateWrapper<>();
                del.lambda().eq(PqsQualityGateAnomalyEntity::getPrcPqsQualityGateBlankId, m.getId());
                pqsQualityGateAnomalyService.delete(del);
            }
            UpdateWrapper<PqsQualityGateBlankEntity> delwp = new UpdateWrapper<>();
            delwp.lambda().eq(PqsQualityGateBlankEntity::getPrcPqsQualityGateId, info.getId());
            pqsQualityGateBlankService.delete(delwp);
        }
        /**
         * QG配置关联相应的岗位
         */
        sendQgAnomalyDataWorkPlace(info.getWorkPlaceIds(), pqsQualityGateInfo != null && pqsQualityGateInfo.getId() != null ? pqsQualityGateInfo.getId() : info.getId());
        /**
         * 添加QG配置对应的色块数据
         */
        sendQgAnomalyDataWorkPlaceGateBlanks(info.getGateBlanks(), pqsQualityGateInfo != null && pqsQualityGateInfo.getId() != null ? pqsQualityGateInfo.getId() : info.getId());
    }

    /**
     * 获取所有AVI列表
     *
     * @return
     */
    @Override
    public List<AviListInfo> getAviList() {
        ResultVO<List> rspAvis = new ResultVO().ok(pmVersionProvider.getAllElements(PmAviEntity.class));
        if (rspAvis == null || !rspAvis.getSuccess()) {
            throw new InkelinkException("PM模块主信息失败。" + (rspAvis == null ? "" : rspAvis.getMessage()));
        }
        List<PmAviEntity> avis = (List<PmAviEntity>) rspAvis.getData();
        List<AviListInfo> aviLists = new ArrayList<AviListInfo>();
        for (PmAviEntity aviInfo : avis) {
            AviListInfo info = new AviListInfo();
            info.setId(aviInfo.getId());
            info.setCode(aviInfo.getAviCode());
            info.setName(aviInfo.getAviName());
            aviLists.add(info);
        }
        return aviLists;
    }

    /**
     * QMS系统激活缺陷（QMS缺陷代码0000000000）
     *
     * @param tpsCode
     * @param status
     */
    @Override
    public void qMsActiveAnomaly(String tpsCode, int status) {
        List<ConditionDto> condistions = new ArrayList<>();
        condistions.add(new ConditionDto("SN", tpsCode, ConditionOper.Equal));
        ResultVO<List<PpsEntryEntity>> rspEntryInfo = new ResultVO().ok(ppsEntryProvider.getData(condistions));
        if (rspEntryInfo == null || !rspEntryInfo.getSuccess()) {
            throw new InkelinkException("PPS模块工单获取失败。" + (rspEntryInfo == null ? "" : rspEntryInfo.getMessage()));
        }
        PpsEntryEntity entryInfo = rspEntryInfo.getData().stream().findFirst().orElse(null);
        if (entryInfo == null) {
            throw new InkelinkException("在生产订单中未找到对应的产品编号");
        }
        condistions.clear();
        condistions.add(new ConditionDto("SN", tpsCode, ConditionOper.Equal));
        condistions.add(new ConditionDto("PQS_DEFECT_ANOMALY_CODE", "0000000000", ConditionOper.Equal));
        PqsProductDefectAnomalyEntity vehicleAnomaly = pqsProductDefectAnomalyService.getData(condistions).stream().findFirst().orElse(null);
        if (vehicleAnomaly == null) {
            vehicleAnomaly = new PqsProductDefectAnomalyEntity();
            vehicleAnomaly.setDefectAnomalyCode("0000000000");
            vehicleAnomaly.setDefectAnomalyDescription("由QMS系统产生的一条缺陷");
            vehicleAnomaly.setId(IdGenerator.getId());
            vehicleAnomaly.setGradeCode("1");
            vehicleAnomaly.setSn(tpsCode);
            vehicleAnomaly.setImg("");
            vehicleAnomaly.setPrcEpsProductWoId(Constant.DEFAULT_ID);
            vehicleAnomaly.setStatus(status == 1 ? 1 : 4);
            vehicleAnomaly.setResponsibleTeamNo("0");
            vehicleAnomaly.setPrcPpsShcShiftId(Constant.DEFAULT_ID);
            vehicleAnomaly.setOrganizationCode(StringUtils.EMPTY);
            vehicleAnomaly.setWorkshopCode(StringUtils.EMPTY);
            vehicleAnomaly.setWorkstationCode(StringUtils.EMPTY);
            vehicleAnomaly.setWorkstationName(StringUtils.EMPTY);
            vehicleAnomaly.setQgWorkstationCode(StringUtils.EMPTY);
            vehicleAnomaly.setQgWorkstationName("");
            vehicleAnomaly.setActivateUser(identityHelper.getUserName());
            vehicleAnomaly.setActivateTime(new Date());

            PqsProductDefectAnomalyLogEntity logInfo = new PqsProductDefectAnomalyLogEntity();
            logInfo.setId(IdGenerator.getId());
            logInfo.setPrcPqsProductDefectAnomalyId(vehicleAnomaly.getId());
            logInfo.setRemark("QMS系统激活了缺陷");
            logInfo.setStatus(1);

            pqsProductDefectAnomalyService.insert(vehicleAnomaly);
            pqsVehicleDefectAnomalyLogService.insert(logInfo);
        }
        /**
         * 关闭缺陷
         */
        if (status == PqsLogicStatusEnum.RECOVERED.code()) {
            pqsProductDefectAnomalyService.update(new UpdateWrapper<PqsProductDefectAnomalyEntity>().lambda()
                    .eq(PqsProductDefectAnomalyEntity::getId, vehicleAnomaly.getId())
                    .set(PqsProductDefectAnomalyEntity::getActivateUser, identityHelper.getUserName())
                    .set(PqsProductDefectAnomalyEntity::getActivateTime, new Date())
                    .set(PqsProductDefectAnomalyEntity::getStatus, 4));
            PqsProductDefectAnomalyLogEntity closeLogInfo = new PqsProductDefectAnomalyLogEntity();
            closeLogInfo.setId(IdGenerator.getId());
            closeLogInfo.setPrcPqsProductDefectAnomalyId(vehicleAnomaly.getId());
            closeLogInfo.setRemark("QMS系统关闭了缺陷");
            closeLogInfo.setStatus(PqsLogicStatusEnum.QUALIFIED.code());
            pqsVehicleDefectAnomalyLogService.insert(closeLogInfo);
        }
    }

    /**
     * 获取缺陷列表
     *
     * @param info
     * @return
     */
    @Override
    public PageData<DefectAnomalyDto> getAnomalyShowList(DefectAnomalyParaInfo info) {

        PageData<DefectAnomalyDto> pageData = new PageData<>();
        Page<DefectAnomalyDto> page = new Page<>(info.getPageIndex(), info.getPageSize());
        pageData.setPageSize(info.getPageSize());
        pageData.setPageIndex(info.getPageIndex());
        List<String> defectAnomalyCodes = Arrays.asList(info.getExcludeCodes().split(","));
        info.setDefectAnomalyCodes(defectAnomalyCodes);
        Page<DefectAnomalyDto> list = pqsLogicDao.getDefectAnomalyList(page, info);
        pageData.setDatas(list.getRecords());
        pageData.setTotal((int) list.getTotal());

        return pageData;
    }

    /**
     * 获取QG百格图配置数据信息
     *
     * @param qualityMatrikId
     * @return
     */
    @Override
    public QgMatrikConfigurationInfo getQGMatrikData(Long qualityMatrikId) {

        QgMatrikConfigurationInfo result = new QgMatrikConfigurationInfo();
        PqsQualityMatrikEntity matrikEntity = pqsQualityMatrikService.get(qualityMatrikId);
        if (matrikEntity != null) {
            BeanUtils.copyProperties(matrikEntity, result);

            QueryWrapper<PqsQualityMatrikWorkstationEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsQualityMatrikWorkstationEntity::getPrcPqsQualityMatrikId, qualityMatrikId);
            String workstationCodes = pqsQualityMatrikWorkstationService.getData(queryWrapper, false)
                    .stream().map(PqsQualityMatrikWorkstationEntity::getWorkstationCode).collect(Collectors.joining(","));

            QueryWrapper<PqsQualityMatrikTcEntity> tcEntityQueryWrapper = new QueryWrapper<>();
            tcEntityQueryWrapper.lambda().eq(PqsQualityMatrikTcEntity::getPrcPqsQualityMatrikId, qualityMatrikId);
            String models = pqsQualityMatrikTcService.getData(tcEntityQueryWrapper, false)
                    .stream().map(PqsQualityMatrikTcEntity::getModel).collect(Collectors.joining(","));

            List<PqsDefectAnomalyEntity> anomalyEntityList = pqsDefectAnomalyService.getAllDatas();

            QueryWrapper<PqsQualityMatrikAnomalyEntity> anomalyEntityQueryWrapper = new QueryWrapper<>();
            anomalyEntityQueryWrapper.lambda().eq(PqsQualityMatrikAnomalyEntity::getPrcPqsQualityMatrikId, qualityMatrikId);
            List<PqsQualityMatrikAnomalyEntity> matrikAnomalyEntityList = pqsQualityMatrikAnomalyService.getData(anomalyEntityQueryWrapper, false);

            List<DefectAnomalyDto> defectAnomalyList = Lists.newArrayList();
            anomalyEntityList.forEach(a -> {
                matrikAnomalyEntityList.forEach(m -> {
                    if (a.getDefectAnomalyCode().equals(m.getDefectAnomalyCode())) {
                        DefectAnomalyDto info = new DefectAnomalyDto();
                        BeanUtils.copyProperties(a, info);
                        defectAnomalyList.add(info);
                    }
                });
            });
            result.setModelCodes(models);
            result.setWorkstationCodes(workstationCodes);
            result.setDefectAnomalyList(defectAnomalyList);
        }

        return result;
    }

    /**
     * 提交百格图配置数据信息
     *
     * @param info
     */
    @Override
    public void submitQualityMatrikData(QgMatrikConfigurationInfo info) {

        // 从PM中获取工位对应的关联的QG岗信息
        ResultVO<PmAllDTO> stationList = new ResultVO().ok(pmVersionProvider.getObjectedPm());
        if (stationList == null || !stationList.getSuccess()) {
            throw new InkelinkException("PM模块根据工位编号获取关联的QG岗位信息失败。" + (stationList == null ? "" : stationList.getMessage()));
        }
        List<PmWorkStationEntity> workstationEntityList = stationList.getData().getStations();

        List<String> workstationCodeList = Arrays.stream(info.getWorkstationCodes().split(",")).collect(Collectors.toList());
        List<PqsQualityMatrikWorkstationEntity> pqmweList = Lists.newArrayList();
        workstationCodeList.forEach(w -> {
            workstationEntityList.forEach(wse -> {
                if (w.equals(wse.getWorkstationCode())) {
                    PqsQualityMatrikWorkstationEntity pqmwEntity = new PqsQualityMatrikWorkstationEntity();
                    pqmwEntity.setPrcPqsQualityMatrikId(info.getId() == null ? Constant.DEFAULT_ID : info.getId());
                    pqmwEntity.setWorkstationCode(wse.getWorkstationCode());
                    pqmwEntity.setWorkstationName(wse.getWorkstationName());
                    pqmweList.add(pqmwEntity);
                }
            });
        });

        // 关联车型 后期考虑配置特征
        ResultVO<List<ComboInfoDTO>> rspDefectLevels = new ResultVO().ok(sysConfigurationProvider.getComboDatas("vehicleModel"));
        if (rspDefectLevels == null || !rspDefectLevels.getSuccess()) {
            throw new InkelinkException("获取关联车型列表失败。" + (rspDefectLevels == null ? "" : rspDefectLevels.getMessage()));
        }
        List<ComboInfoDTO> defectLevels = rspDefectLevels.getData();

        List<String> modelCodeList = Arrays.stream(info.getModelCodes().split(",")).collect(Collectors.toList());
        List<PqsQualityMatrikTcEntity> tcEntityList = Lists.newArrayList();
        modelCodeList.forEach(m -> {
            defectLevels.forEach(d -> {
                if (m.equals(d.getValue())) {
                    PqsQualityMatrikTcEntity matrikTcEntity = new PqsQualityMatrikTcEntity();
                    matrikTcEntity.setModel(d.getValue());
                    matrikTcEntity.setModelName(d.getText());
                    tcEntityList.add(matrikTcEntity);
                }
            });
        });

        PqsQualityMatrikEntity matrikEntity = new PqsQualityMatrikEntity();
        BeanUtils.copyProperties(info, matrikEntity);
        matrikEntity.setModels(tcEntityList.stream().map(PqsQualityMatrikTcEntity::getModelName)
                .collect(Collectors.joining(",")));
        matrikEntity.setWorkstationNames(pqmweList.stream().map(PqsQualityMatrikWorkstationEntity::getWorkstationName)
                .collect(Collectors.joining(",")));

        // 新增新数据
        if (info.getId() == null) {
            matrikEntity.setId(IdGenerator.getId());
            pqsQualityMatrikService.insert(matrikEntity);
        } else {
            // 修改原有数据
            pqsQualityMatrikService.update(matrikEntity);
        }

        // 添加车型
        submitQualityMatrikTc(tcEntityList, matrikEntity.getId());
        // 添加工位
        submitQualityMatrikWorkstation(pqmweList, matrikEntity.getId());
        // 添加缺陷
        submitQualityMatrikAnomaly(info.getDefectAnomalyList(), matrikEntity.getId());
    }

    /**
     * 获取QG配置数据信息
     *
     * @param qualityGateId
     * @return
     */
    @Override
    public QgGateConfigurationInfo getQGGateData(Long qualityGateId) {

        // 获取QG基础信息
        QgGateConfigurationInfo result = new QgGateConfigurationInfo();
        PqsQualityGateEntity gateEntity = pqsQualityGateService.get(qualityGateId);
        if (gateEntity != null) {
            BeanUtils.copyProperties(gateEntity, result);

            QueryWrapper<PqsQualityGateWorkstationEntity> workstationEntityQueryWrapper = new QueryWrapper();
            workstationEntityQueryWrapper.lambda().eq(PqsQualityGateWorkstationEntity::getPrcPqsQualityGateId, qualityGateId);
            String workstationCodes = pqsQualityGateWorkstationService.getData(workstationEntityQueryWrapper, false)
                    .stream().map(PqsQualityGateWorkstationEntity::getWorkstationCode)
                    .collect(Collectors.joining(","));
            result.setWorkstationCodes(workstationCodes);

            QueryWrapper<PqsQualityGateBlankEntity> blankEntityQueryWrapper = new QueryWrapper();
            blankEntityQueryWrapper.lambda().eq(PqsQualityGateBlankEntity::getPrcPqsQualityGateId, qualityGateId);
            List<PqsQualityGateBlankEntity> blankEntityList = pqsQualityGateBlankService.getData(blankEntityQueryWrapper, false);
            result.setGateBlanks(blankEntityList);

            QueryWrapper<PqsQualityGateTcEntity> tcEntityQueryWrapper = new QueryWrapper();
            tcEntityQueryWrapper.lambda().eq(PqsQualityGateTcEntity::getPrcPqsQualityGateId, qualityGateId);
            String modelCodes = pqsQualityGateTcService.getData(tcEntityQueryWrapper, false)
                    .stream().map(PqsQualityGateTcEntity::getModel).collect(Collectors.joining(","));
            result.setModelCodes(modelCodes);

            // 同时返回缺陷数据
            blankEntityList.forEach(b -> {
                List<ConditionDto> conditions = new ArrayList<>();
                conditions.add(new ConditionDto("prcPqsQualityGateBlankId", b.getId().toString(), ConditionOper.Equal));
                List<PqsQualityGateAnomalyEntity> anomalyEntityList = pqsQualityGateAnomalyService.getData(conditions);
                List<DefectAnomalyDto> anomalyInfoList = Lists.newArrayList();
                anomalyEntityList.forEach(a -> {
                    DefectAnomalyDto anomalyInfo = new DefectAnomalyDto();
                    BeanUtils.copyProperties(a, anomalyInfo);
                    anomalyInfoList.add(anomalyInfo);
                });
                b.setAnomalyCodes(anomalyInfoList);
            });
        }

        return result;
    }

    /**
     * 根据色块编号获取色块对应的缺陷列表
     *
     * @param gateBlankId
     * @return
     */
    @Override
    public List<DefectAnomalyDto> getGateAnomalyList(Long gateBlankId) {

        List<DefectAnomalyDto> resultList = Lists.newArrayList();

        // 根据色块编号查询QG检验项-缺陷
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("prcPqsQualityGateBlankId", gateBlankId.toString(), ConditionOper.Equal));
        List<PqsQualityGateAnomalyEntity> gateAnomalyEntityList = pqsQualityGateAnomalyService.getData(conditions);

        // 查询缺陷库
        List<String> defectAnomalyCodes = gateAnomalyEntityList.stream().map(PqsQualityGateAnomalyEntity::getDefectAnomalyCode).collect(Collectors.toList());
        conditions.clear();
        conditions.add(new ConditionDto("defectAnomalyCode", String.join("|", defectAnomalyCodes), ConditionOper.In));
        List<PqsDefectAnomalyEntity> defectAnomalyEntityList = pqsDefectAnomalyService.getData(conditions);

        gateAnomalyEntityList.forEach(g -> {
            defectAnomalyEntityList.forEach(d -> {
                if (g.getDefectAnomalyCode().equals(d.getDefectAnomalyCode())) {
                    DefectAnomalyDto anomalyInfo = new DefectAnomalyDto();
                    BeanUtils.copyProperties(d, anomalyInfo);
                    resultList.add(anomalyInfo);
                }
            });
        });

        return resultList;
    }

    /**
     * 提交QG配置数据信息
     *
     * @param info
     */
    @Override
    public void submitQGGateData(QgGateConfigurationInfo info) {

        // 从PM中获取工位对应的关联的QG岗信息
        ResultVO<PmAllDTO> stationList = new ResultVO().ok(pmVersionProvider.getObjectedPm());
        if (stationList == null || !stationList.getSuccess()) {
            throw new InkelinkException("PM模块根据工位编号获取关联的QG岗位信息失败。" + (stationList == null ? "" : stationList.getMessage()));
        }
        List<PmWorkStationEntity> workstationEntityList = stationList.getData().getStations();

        List<String> workstationCodeList = Arrays.stream(info.getWorkstationCodes().split(",")).collect(Collectors.toList());
        List<PqsQualityGateWorkstationEntity> pqgweList = Lists.newArrayList();
        workstationCodeList.forEach(w -> {
            workstationEntityList.forEach(wse -> {
                if (w.equals(wse.getWorkstationCode())) {
                    PqsQualityGateWorkstationEntity pqgweEntity = new PqsQualityGateWorkstationEntity();
                    pqgweEntity.setPrcPqsQualityGateId(info.getId());
                    pqgweEntity.setWorkstationCode(wse.getWorkstationCode());
                    pqgweEntity.setWorkstationName(wse.getWorkstationName());
                    pqgweList.add(pqgweEntity);
                }
            });
        });

        // 关联车型 后期考虑配置特征
        ResultVO<List<ComboInfoDTO>> rspDefectLevels = new ResultVO().ok(sysConfigurationProvider.getComboDatas("vehicleModel"));
        if (rspDefectLevels == null || !rspDefectLevels.getSuccess()) {
            throw new InkelinkException("获取关联车型列表失败。" + (rspDefectLevels == null ? "" : rspDefectLevels.getMessage()));
        }
        List<ComboInfoDTO> defectLevels = rspDefectLevels.getData();

        List<String> modelCodeList = Arrays.stream(info.getModelCodes().split(",")).collect(Collectors.toList());
        List<PqsQualityGateTcEntity> gateTcEntityList = Lists.newArrayList();
        modelCodeList.forEach(m -> {
            defectLevels.forEach(d -> {
                if (m.equals(d.getValue())) {
                    PqsQualityGateTcEntity gateTcEntity = new PqsQualityGateTcEntity();
                    gateTcEntity.setPrcPqsQualityGateId(info.getId());
                    gateTcEntity.setModel(d.getValue());
                    gateTcEntity.setModelName(d.getText());
                    gateTcEntityList.add(gateTcEntity);
                }
            });
        });

        PqsQualityGateEntity gateEntity = new PqsQualityGateEntity();
        BeanUtils.copyProperties(info, gateEntity);
        gateEntity.setWorkstationNames(pqgweList.stream().map(PqsQualityGateWorkstationEntity::getWorkstationName)
                .collect(Collectors.joining(",")));
        gateEntity.setModels(gateTcEntityList.stream().map(PqsQualityGateTcEntity::getModelName)
                .collect(Collectors.joining(",")));

        // 新增新数据
        if (gateEntity.getId() == null) {
            gateEntity.setId(IdGenerator.getId());
            pqsQualityGateService.insert(gateEntity);
        } else {
            // 修改原有数据
            pqsQualityGateService.update(gateEntity);
        }

        // 车型
        submitQGAnomalyDataTc(gateTcEntityList, gateEntity.getId());
        // 工位
        submitQGAnomalyDataWorkstation(pqgweList, gateEntity.getId());
        // 添加QG配置对应的色块数据
        submitQGAnomalyDataWorkPlaceGateBlanks(info.getGateBlanks(), gateEntity.getId());
    }

    /**
     * 根据工位代码获取常用缺陷
     *
     * @param workstationCode
     * @return
     */
    @Override
    public List<DefectAnomalyDto> getAnomalyWpList(String workstationCode) {

        List<DefectAnomalyDto> resultList = Lists.newArrayList();

        // 常用缺陷信息
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("workstationCode", workstationCode, ConditionOper.Equal));
        List<PqsDefectAnomalyWpEntity> wpEntityList = pqsDefectAnomalyWpService.getData(conditions);
        // 组合缺陷库信息
        List<PqsDefectAnomalyEntity> anomalyEntityList = pqsDefectAnomalyService.getAllDatas();
        wpEntityList.forEach(w -> {
            anomalyEntityList.forEach(a -> {
                if (w.getDefectAnomalyCode().equals(a.getDefectAnomalyCode())) {
                    DefectAnomalyDto info = new DefectAnomalyDto();
                    BeanUtils.copyProperties(a, info);
                    resultList.add(info);
                }
            });
        });

        return resultList;
    }

    /**
     * 添加车型
     *
     * @param lists
     * @param pqsQualityGateId
     */
    private void submitQGAnomalyDataTc(List<PqsQualityGateTcEntity> lists, Long pqsQualityGateId) {

        // 删除现有车型
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("prcPqsQualityGateId", pqsQualityGateId.toString(), ConditionOper.Equal));
        pqsQualityGateTcService.delete(conditions);

        // 写入车型
        lists.forEach(p -> {
            p.setPrcPqsQualityGateId(pqsQualityGateId);
        });
        pqsQualityGateTcService.insertBatch(lists);
    }

    /**
     * 添加工位
     *
     * @param lists
     * @param pqsQualityGateId
     */
    private void submitQGAnomalyDataWorkstation(List<PqsQualityGateWorkstationEntity> lists, Long pqsQualityGateId) {

        // 删除现有工位
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("prcPqsQualityGateId", pqsQualityGateId.toString(), ConditionOper.Equal));
        pqsQualityGateWorkstationService.delete(conditions);

        // 写入工位
        lists.forEach(p -> {
            p.setPrcPqsQualityGateId(pqsQualityGateId);
        });
        pqsQualityGateWorkstationService.insertBatch(lists);
    }

    /***
     * 添加QG配置对应的色块数据
     *
     * @param lists
     * @param pqsQualityGateId
     */
    private void submitQGAnomalyDataWorkPlaceGateBlanks(List<PqsQualityGateBlankEntity> lists, Long pqsQualityGateId) {

        List<PqsQualityGateAnomalyEntity> gateAnomalyEntityList = Lists.newArrayList();
        List<PqsDefectAnomalyEntity> defectAnomalyEntityList = pqsDefectAnomalyService.getAllDatas();

        // 当前色块
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("prcPqsQualityGateId", pqsQualityGateId.toString(), ConditionOper.Equal));
        List<PqsQualityGateBlankEntity> blankEntityList = pqsQualityGateBlankService.getData(conditions);
        if (CollectionUtil.isNotEmpty(blankEntityList)) {
            conditions.clear();
            List<String> defectAnomalyCodes = blankEntityList.stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
            conditions.clear();
            conditions.add(new ConditionDto("prcPqsQualityGateBlankId", String.join("|", defectAnomalyCodes), ConditionOper.In));
            pqsQualityGateAnomalyService.delete(conditions);
        }
        // 删除色块
        conditions.clear();
        conditions.add(new ConditionDto("prcPqsQualityGateId", pqsQualityGateId.toString(), ConditionOper.Equal));
        pqsQualityGateBlankService.delete(conditions);

        // 添加色块
        lists.forEach(l -> {
            l.setId(IdGenerator.getId());
            l.setPrcPqsQualityGateId(pqsQualityGateId);
            defectAnomalyEntityList.forEach(d -> {
                if (CollectionUtil.isNotEmpty(l.getAnomalyCodes())) {
                    l.getAnomalyCodes().forEach(a -> {
                        if (a.getDefectAnomalyCode().equals(d.getDefectAnomalyCode())) {
                            PqsQualityGateAnomalyEntity gateAnomalyEntity = new PqsQualityGateAnomalyEntity();
                            gateAnomalyEntity.setDefectAnomalyCode(d.getDefectAnomalyCode());
                            gateAnomalyEntity.setDefectAnomalyDescription(d.getDefectAnomalyDescription());
                            gateAnomalyEntity.setPrcPqsQualityGateBlankId(l.getId());
                            gateAnomalyEntity.setShortCode(d.getShortCode());
                            gateAnomalyEntityList.add(gateAnomalyEntity);
                        }
                    });
                }
            });
        });
        pqsQualityGateBlankService.insertBatch(lists, lists.size());
        pqsQualityGateAnomalyService.insertBatch(gateAnomalyEntityList, gateAnomalyEntityList.size());
    }

    /**
     * 添加百格图车型
     *
     * @param lists
     * @param pqsQualityMatrikId
     */
    private void submitQualityMatrikTc(List<PqsQualityMatrikTcEntity> lists, Long pqsQualityMatrikId) {
        UpdateWrapper<PqsQualityMatrikTcEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(PqsQualityMatrikTcEntity::getPrcPqsQualityMatrikId, pqsQualityMatrikId);
        // 删除现有车型
        pqsQualityMatrikTcService.delete(wrapper);

        // 写入车型
        lists.forEach(p -> {
            p.setPrcPqsQualityMatrikId(pqsQualityMatrikId);
        });
        pqsQualityMatrikTcService.insertBatch(lists);
    }

    /**
     * 添加QG配置工位
     *
     * @param lists
     * @param pqsQualityMatrikId
     */
    private void submitQualityMatrikWorkstation(List<PqsQualityMatrikWorkstationEntity> lists, Long pqsQualityMatrikId) {

        UpdateWrapper<PqsQualityMatrikWorkstationEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(PqsQualityMatrikWorkstationEntity::getPrcPqsQualityMatrikId, pqsQualityMatrikId);
        // 删除现有工位
        pqsQualityMatrikWorkstationService.delete(updateWrapper);

        // 写入工位
        lists.forEach(p -> {
            p.setPrcPqsQualityMatrikId(pqsQualityMatrikId);
        });
        pqsQualityMatrikWorkstationService.insertBatch(lists, lists.size());
    }

    /**
     * 添加百格图缺陷
     *
     * @param lists
     * @param pqsQualityMatrikId
     */
    private void submitQualityMatrikAnomaly(List<DefectAnomalyDto> lists, Long pqsQualityMatrikId) {

        List<PqsDefectAnomalyEntity> anomalyEntityList = pqsDefectAnomalyService.getAllDatas();

        // 删除所有色块
        UpdateWrapper<PqsQualityMatrikAnomalyEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(PqsQualityMatrikAnomalyEntity::getPrcPqsQualityMatrikId, pqsQualityMatrikId);
        pqsQualityMatrikAnomalyService.delete(updateWrapper);

        // 写入百格图缺陷
        List<PqsQualityMatrikAnomalyEntity> insertLists = Lists.newArrayList();
        lists.forEach(l -> {
            anomalyEntityList.forEach(a -> {
                if (l.getDefectAnomalyCode().equals(a.getDefectAnomalyCode())) {
                    PqsQualityMatrikAnomalyEntity entity = new PqsQualityMatrikAnomalyEntity();
                    entity.setDefectAnomalyCode(l.getDefectAnomalyCode());
                    entity.setDefectAnomalyDescription(a.getDefectAnomalyDescription());
                    entity.setPrcPqsQualityMatrikId(pqsQualityMatrikId);
                    entity.setShortCode(a.getShortCode());
                    insertLists.add(entity);
                }
            });
        });
        pqsQualityMatrikAnomalyService.insertBatch(insertLists, lists.size());
    }

    /**
     * 获取问题等级
     *
     * @return
     */
    @Override
    public List<ComboDataDTO> getGradeCombo() {

        List<ComboDataDTO> collect = pqsGradeService.getAllDatas().stream()
                .sorted(Comparator.comparing(PqsGradeEntity::getDisplayNo)).map(c -> {
                    ComboDataDTO dto = new ComboDataDTO();
                    dto.setText(c.getGradeName());
                    dto.setValue(c.getGradeCode());
                    return dto;
                }).collect(Collectors.toList());

        return collect;
    }

    /**
     * 获取责任部门
     *
     * @return
     */
    @Override
    public List<ComboDataDTO> getDeptCombo() {

        List<ComboDataDTO> collect = pqsDeptService.getAllDatas().stream()
                .sorted(Comparator.comparing(PqsDeptEntity::getDisplayNo)).map(c -> {
                    ComboDataDTO dto = new ComboDataDTO();
                    dto.setText(c.getDeptName());
                    dto.setValue(c.getDeptCode());
                    return dto;
                }).collect(Collectors.toList());

        return collect;
    }


    /**
     * 获取所有组件
     *
     * @return
     */
    @Override
    public List<ComboDataDTO> getAllComponent() {

        List<ComboDataDTO> collect = pqsDefectComponentService.getAllDatas().stream()
                .sorted(Comparator.comparing(PqsDefectComponentEntity::getDefectComponentCode)).map(c -> {
                    ComboDataDTO dto = new ComboDataDTO();
                    dto.setLabel(c.getShortCode());
                    dto.setValue(c.getDefectComponentCode());
                    dto.setText(c.getDefectComponentDescription());
                    return dto;
                }).collect(Collectors.toList());

        return collect;
    }

    /**
     * 获取所有位置
     *
     * @return
     */
    @Override
    public List<ComboDataDTO> getAllPosition() {
        List<ComboDataDTO> collect = pqsDefectPositionService.getAllDatas().stream()
                .sorted(Comparator.comparing(PqsDefectPositionEntity::getDefectPositionCode)).map(c -> {
                    ComboDataDTO dto = new ComboDataDTO();
                    dto.setLabel(c.getShortCode());
                    dto.setValue(c.getDefectPositionCode());
                    dto.setText(c.getDefectPositionDescription());
                    return dto;
                }).collect(Collectors.toList());

        return collect;
    }

    /**
     * 获取所有列表
     *
     * @return
     */
    @Override
    public List<ComboDataDTO> getAllDefectCode() {
        List<ComboDataDTO> collect = pqsDefectCodeService.getAllDatas().stream()
                .sorted(Comparator.comparing(PqsDefectCodeEntity::getDefectCodeCode)).map(c -> {
                    ComboDataDTO dto = new ComboDataDTO();
                    dto.setLabel(c.getShortCode());
                    dto.setValue(c.getDefectCodeCode());
                    dto.setText(c.getDefectCodeDescription());
                    return dto;
                }).collect(Collectors.toList());

        return collect;
    }

    /**
     * 获取所有缺陷库数据
     *
     * @return
     */
    @Override
    public List<DefectAnomalyDto> getAllAnomalyShowList() {
        List<DefectAnomalyDto> collect = pqsDefectAnomalyService.getAllDatas().stream()
                .sorted(Comparator.comparing(PqsDefectAnomalyEntity::getGradeName)).map(c -> {
                    DefectAnomalyDto info = new DefectAnomalyDto();
                    BeanUtils.copyProperties(c, info);
                    return info;
                }).collect(Collectors.toList());

        return collect;
    }

    /**
     * 根据工位代码获取常用缺陷
     *
     * @param workstationCode
     * @param sn
     * @return
     */
    @Override
    public List<DefectAnomalyDto> getAnomalyWpListBySn(String workstationCode, String sn) {
        // 查询费工艺的缺陷 且未激活的缺陷
        QueryWrapper<PqsProductDefectAnomalyEntity> pdaEntity = new QueryWrapper<>();
        pdaEntity.lambda().eq(PqsProductDefectAnomalyEntity::getSn, sn)
                .eq(PqsProductDefectAnomalyEntity::getPrcEpsProductWoId, 0)
                .ne(PqsProductDefectAnomalyEntity::getStatus, 4);
        List<String> defectAnomalyCodes = pqsProductDefectAnomalyService.getData(pdaEntity, false)
                .stream().map(PqsProductDefectAnomalyEntity::getDefectAnomalyCode).collect(Collectors.toList());

        List<DefectAnomalyDto> wpDataList = pqsLogicDao.getDefectAnomalyWpListInfo(workstationCode);
        // 匹配激活状态
        wpDataList.forEach(w -> defectAnomalyCodes.forEach(d -> {
            if (w.getDefectAnomalyCode().equals(d)) {
                w.setIsActived(true);
            }
        }));

        return wpDataList;
    }

    /**
     * 根据工位代码和车型获取
     *
     * @param workstationCode
     * @param model
     * @return
     */
    @Override
    public List<ComboInfoDTO> getQualityGateByWorkstationCode(String workstationCode, String model) {

        return pqsLogicDao.getQualityGateByWorkstationCode(workstationCode, model);
    }

    /**
     * QG岗查看质量门检查图片数据
     *
     * @param qualityGateId
     * @return
     */
    @Override
    public ShowQgGateDto showQGGateData(Long qualityGateId) {
        ShowQgGateDto result = new ShowQgGateDto();

        QueryWrapper<PqsQualityGateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PqsQualityGateEntity::getId, qualityGateId);
        PqsQualityGateEntity gateEntity = pqsQualityGateService.get(qualityGateId);
        if (gateEntity != null) {
            result.setId(gateEntity.getId());
            result.setImage(gateEntity.getImage());
            QueryWrapper<PqsQualityGateBlankEntity> blankEntityQueryWrapper = new QueryWrapper<>();
            blankEntityQueryWrapper.lambda().eq(PqsQualityGateBlankEntity::getPrcPqsQualityGateId, qualityGateId);
            List<PqsQualityGateBlankEntity> gateBlanks = pqsQualityGateBlankService.getData(blankEntityQueryWrapper, false);
            result.setGateBlanks(gateBlanks);
        }

        return result;
    }

    /**
     * 获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 色块ID
     * @param sn                 产品唯一码
     * @return
     */
    @Override
    public List<DefectAnomalyDto> getGateAnomalyByGateBlankIdAndSn(Long qualityGateBlankId, String sn) {

        List<DefectAnomalyDto> resultList = Lists.newArrayList();
        List<DefectAnomalyDto> dataList = pqsLogicDao.getGateAnomalyByGateBlankIdAndSn(qualityGateBlankId, sn);
        if (CollectionUtil.isNotEmpty(dataList)) {
            resultList = dataConversion(dataList, sn);
        }
        return resultList;
    }

    /**
     * 根据工位代码和车型获取
     *
     * @param workstationCode 工位
     * @param model           车型
     * @return
     */
    @Override
    public List<ComboInfoDTO> getQualityMatrikByWorkstationCode(String workstationCode, String model) {

        return pqsLogicDao.getQualityMatrikByWorkstationCode(workstationCode, model);
    }

    /**
     * QG岗查看质量门检查图片数据
     *
     * @param qualityMatrikId 百格图ID
     * @param sn
     * @return
     */
    @Override
    public ShowQgMatrikDto showQgMatrikDataBySn(Long qualityMatrikId, String sn) {

        ShowQgMatrikDto result = new ShowQgMatrikDto();
        // 获取QG基础信息
        PqsQualityMatrikEntity matrikEntity = pqsQualityMatrikService.get(qualityMatrikId);
        BeanUtils.copyProperties(matrikEntity, result);

        List<DefectAnomalyDto> dataList = pqsLogicDao.getShowQGMatrikDataBySn(qualityMatrikId, sn);
        if (CollectionUtil.isNotEmpty(dataList)) {
            result.setDefectAnomalyList(dataConversion(dataList, sn));
        }

        return result;
    }

    private List<DefectAnomalyDto> dataConversion(List<DefectAnomalyDto> lists, String sn) {
        QueryWrapper<PqsProductDefectAnomalyEntity> anomalyEntityQueryWrapper = new QueryWrapper<>();
        anomalyEntityQueryWrapper.lambda().ne(PqsProductDefectAnomalyEntity::getStatus, 4)
                .eq(PqsProductDefectAnomalyEntity::getSn, sn);
        List<PqsProductDefectAnomalyEntity> anomalyEntityList = pqsProductDefectAnomalyService.getData(anomalyEntityQueryWrapper, false);
        if (CollectionUtil.isNotEmpty(anomalyEntityList)) {
            lists.forEach(d -> {
                anomalyEntityList.forEach(a -> {
                    if (d.getDefectAnomalyCode().equals(a.getDefectAnomalyCode())) {
                        d.setIsActived(true);
                    }
                });
            });
        }
        return lists;
    }

    /**
     * QG岗查围堵缺陷信息
     *
     * @param sn 唯一码
     * @return
     */
    @Override
    public List<QgRiskDto> showRiskDataBySn(String sn) {

        List<QgRiskDto> resultList = Lists.newArrayList();

        QueryWrapper<PqsDefectAnomalyRiskDetailEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PqsDefectAnomalyRiskDetailEntity::getSn, sn);
        List<PqsDefectAnomalyRiskDetailEntity> dataList = pqsDefectAnomalyRiskDetailService.getData(queryWrapper, false);
        if (CollectionUtil.isNotEmpty(dataList)) {
            dataList.forEach(d -> {
                QgRiskDto dto = new QgRiskDto();
                BeanUtils.copyProperties(d, dto);
                resultList.add(dto);
            });
        }
        return resultList;
    }

    /**
     * 风险管理
     *
     * @param info
     */
    @Override
    public void riskManager(QgRiskOperInfo info) {

        PqsDefectAnomalyRiskDetailEntity riskInfo = pqsDefectAnomalyRiskDetailService.get(info.getId());
        if (riskInfo == null) {
            throw new InkelinkException("未找到风险明细");
        }

        switch (info.getOperType()) {
            // 激活
            case 20:
                // 缺陷不为空时 激活缺陷
                if (StringUtils.isNotEmpty(riskInfo.getDefectAnomalyCode())) {
                    ActiveAnomalyInfo activeAnomalyInfo = new ActiveAnomalyInfo();
                    activeAnomalyInfo.setWorkstationCode(info.getWorkstationCode());
                    activeAnomalyInfo.setSn(riskInfo.getSn());

                    List<AnomalyDto> anomalyInfos = Lists.newArrayList();
                    AnomalyDto anomalyDto = new AnomalyDto();
                    anomalyDto.setDefectAnomalyCode(riskInfo.getDefectAnomalyCode());
                    anomalyDto.setDefectAnomalyDescription(riskInfo.getDefectAnomalyDescription());
                    anomalyDto.setSource(3);
                    anomalyInfos.add(anomalyDto);
                    activeAnomalyInfo.setAnomalyInfos(anomalyInfos);

                    activeAnomaly_(activeAnomalyInfo);
                }
                if (riskInfo.getIsActived()) {
                    // 如果已激活，跳过
                    return;
                }
                PqsDefectAnomalyRiskDetailEntity updEntity = pqsDefectAnomalyRiskDetailService.get(info.getId());
                updEntity.setIsActived(true);
                updEntity.setActiveDt(new Date());
                updEntity.setActiveBy(identityHelper.getUserName());
                updEntity.setStatus(20);
                pqsDefectAnomalyRiskDetailService.updateById(updEntity);
                break;
            // 问题修复
            case 30:
                // 判断缺陷是否激活 如果已激活 关闭缺陷
                if (riskInfo.getIsActived()) {
                    PqsProductDefectAnomalyEntity anomaly = pqsProductDefectAnomalyService.getAllDatas().stream()
                            .filter(p -> p.getSn().equals(riskInfo.getSn()) && p.getSource() == 3 && (p.getStatus() == 1 || p.getStatus() == 5))
                            .findFirst().orElse(null);
                    // 存在风险缺陷，修复缺陷
                    if (anomaly != null) {
                        // 修复缺陷
                        AnomalyActivity anomalyActivity = new AnomalyActivity();
                        anomalyActivity.setDataId(anomaly.getId());
                        anomalyActivity.setStatus(2);
                        anomalyActivity.setRepairActivity(info.getRepairActivity());
                        modifyDefectAnomalyStatus(anomalyActivity);
                    }
                }
                break;
            case 90:
                // 释放
                PqsDefectAnomalyRiskDetailEntity detailEntity = pqsDefectAnomalyRiskDetailService.get(info.getId());
                detailEntity.setCloseDt(new Date());
                detailEntity.setCloseBy(identityHelper.getUserName());
                detailEntity.setCloseRemark(info.getCloseRemark());
                detailEntity.setStatus(90);
                pqsDefectAnomalyRiskDetailService.updateById(detailEntity);

                // 判断缺陷是否激活 如果已激活 关闭缺陷
                if (riskInfo.getIsActived()) {
                    PqsProductDefectAnomalyEntity anomaly = pqsProductDefectAnomalyService.getAllDatas().stream().filter(p -> {
                        boolean sn = StringUtils.equals(p.getSn(), riskInfo.getSn());
                        boolean source = p.getSource() == 3;
                        boolean status = p.getStatus() == 2 || p.getStatus() == 3;
                        return sn && source && status;
                    }).findFirst().orElse(null);
                    // 存在风险缺陷，修复缺陷
                    if (anomaly != null) {
                        // 修复缺陷
                        AnomalyActivity anomalyActivity = new AnomalyActivity();
                        anomalyActivity.setDataId(anomaly.getId());
                        anomalyActivity.setStatus(4);
                        modifyDefectAnomalyStatus(anomalyActivity);
                    }
                }
                break;
            default:
                throw new InkelinkException("未支持的参数，请检查！");
        }
    }

    /**
     * QG必检项清单
     *
     * @param workstationCode 工位代码
     * @param sn              唯一码
     * @return
     */
    @Override
    public List<ShowQgCheckListDto> showQgCheckListBySn(String workstationCode, String sn) {

        List<ShowQgCheckListDto> resultList = null;
        ResultVO<PpsOrderEntity> ppsOrderInfoByKey = new ResultVO().ok(ppsOrderProvider.getPpsOrderInfoByKey(sn));
        if (ppsOrderInfoByKey == null || !ppsOrderInfoByKey.getSuccess()) {
            throw new InkelinkException("PPS订单获取失败。" + (ppsOrderInfoByKey == null ? "" : ppsOrderInfoByKey.getMessage()));
        }
        PpsOrderEntity order = ppsOrderInfoByKey.getData();
        if (order == null) {
            throw new InkelinkException("未找到对应订单信息，请检查！");
        }
        List<ShowQgCheckListDto> showQgCheckListBySnList = pqsLogicDao.getShowQgCheckListBySn(workstationCode, sn, order.getModel());
        if (CollectionUtil.isNotEmpty(showQgCheckListBySnList)) {
            resultList = showQgCheckListBySnList;
        }

        return resultList;
    }

    /**
     * 提交qg必检项结果
     *
     * @param info 检查项清单
     */
    @Override
    public void submitQgCheckItemResult(SubmitCheckItemInfo info) {

        if (CollectionUtil.isEmpty(info.getChcekList())) {
            return;
        }

        // 匹配已保存的数据
        List<Long> chekItemIds = info.getChcekList().stream().map(QgCheckItemInfo::getId).collect(Collectors.toList());

        // 匹配工位信息
        ResultVO<PmAllDTO> stationList = new ResultVO().ok(pmVersionProvider.getObjectedPm());
        if (stationList == null || !stationList.getSuccess()) {
            throw new InkelinkException("PM模块根据工位编号获取关联的QG岗位信息失败。" + (stationList == null ? "" : stationList.getMessage()));
        }
        PmWorkStationEntity workstationInfo = stationList.getData().getStations()
                .stream().filter(s -> s.getWorkstationCode().equals(info.getWorkstationCode()))
                .findFirst().orElse(null);

        // 必检项明细
        QueryWrapper<PqsProductQgCheckListRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PqsProductQgCheckListRecordEntity::getSn, info.getSn())
                .eq(PqsProductQgCheckListRecordEntity::getWorkstationCode, info.getWorkstationCode())
                .in(PqsProductQgCheckListRecordEntity::getPrcPqsQgCheckListId, chekItemIds);
        List<PqsProductQgCheckListRecordEntity> currentList = pqsProductQgCheckListRecordService.getData(queryWrapper, false);

        QueryWrapper<PqsQgCheckListEntity> pqclEntityQueryWrapper = new QueryWrapper<>();
        pqclEntityQueryWrapper.lambda().eq(PqsQgCheckListEntity::getWorkstationCode, info.getWorkstationCode())
                .in(PqsQgCheckListEntity::getId, chekItemIds);
        List<PqsQgCheckListEntity> checkList = pqsQgCheckListService.getData(pqclEntityQueryWrapper, false);

        List<PqsProductQgCheckListRecordEntity> insertList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(checkList)) {
            // 组装新增数据
            checkList.forEach(a -> {
                if (CollectionUtil.isNotEmpty(currentList)) {
                    currentList.forEach(b -> {
                        if (!a.getId().equals(b.getPrcPqsQgCheckListId())) {
                            PqsProductQgCheckListRecordEntity entity = new PqsProductQgCheckListRecordEntity();
                            entity.setPrcPqsQgCheckListId(a.getId());
                            entity.setGroup(a.getGroup());
                            entity.setWorkstationCode(workstationInfo == null ? "" : workstationInfo.getWorkstationCode());
                            entity.setWorkstationName(workstationInfo == null ? "" : workstationInfo.getWorkstationName());
                            entity.setContent(a.getContent());
                            entity.setResult(info.getChcekList().stream().
                                    filter(i -> i.getId().equals(a.getId())).findFirst().orElse(null).getResult());
                            entity.setDisplayNo(a.getDisplayNo());
                            entity.setSn(info.getSn());
                            insertList.add(entity);
                        }
                    });
                } else {
                    PqsProductQgCheckListRecordEntity entity = new PqsProductQgCheckListRecordEntity();
                    entity.setPrcPqsQgCheckListId(a.getId());
                    entity.setGroup(a.getGroup());
                    entity.setWorkstationCode(workstationInfo == null ? "" : workstationInfo.getWorkstationCode());
                    entity.setWorkstationName(workstationInfo == null ? "" : workstationInfo.getWorkstationName());
                    entity.setContent(a.getContent());
                    entity.setResult(info.getChcekList().stream().
                            filter(i -> i.getId().equals(a.getId())).findFirst().orElse(null).getResult());
                    entity.setDisplayNo(a.getDisplayNo());
                    entity.setSn(info.getSn());
                    insertList.add(entity);
                }
            });
        }

        // 已存在清单，修改数据
        // 遍历集合修改数据
        currentList.forEach(updateInfo -> {
            int result = info.getChcekList().stream()
                    .filter(c -> c.getId().equals(updateInfo.getPrcPqsQgCheckListId())).findFirst().orElse(null).getResult();
            if (updateInfo.getResult() == result) {
                return;
            }

            UpdateWrapper<PqsProductQgCheckListRecordEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().set(PqsProductQgCheckListRecordEntity::getResult, result)
                    .eq(PqsProductQgCheckListRecordEntity::getId, updateInfo.getId());
            pqsProductQgCheckListRecordService.update(updateWrapper);
            // ng 激活缺陷
            if (result == 2) {
                PqsQgCheckListEntity checkItem = checkList.stream()
                        .filter(l -> l.getId().equals(updateInfo.getPrcPqsQgCheckListId())).findFirst().orElse(null);
                // 缺陷清单
                if (checkItem != null && StringUtils.isNotEmpty(checkItem.getDefectAnomalyCode())) {
                    ActiveAnomalyInfo infoData = new ActiveAnomalyInfo();
                    infoData.setWorkstationCode(info.getWorkstationCode());
                    infoData.setSn(info.getSn());

                    List<AnomalyDto> anomalyInfos = Lists.newArrayList();
                    AnomalyDto dto = new AnomalyDto();
                    dto.setDefectAnomalyCode(checkItem.getDefectAnomalyCode());
                    dto.setDefectAnomalyDescription(checkItem.getDefectAnomalyDescription());
                    dto.setSource(4);
                    anomalyInfos.add(dto);
                    infoData.setAnomalyInfos(anomalyInfos);

                    activeAnomaly_(infoData);
                }
            }
        });

        List<PqsProductQgCheckListRecordEntity> ngItems = insertList.stream()
                .filter(i -> i.getResult() == 2).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(ngItems)) {
            // ng项
            ngItems.forEach(n -> {
                PqsQgCheckListEntity cfgItem = checkList.stream()
                        .filter(c -> c.getId().equals(n.getPrcPqsQgCheckListId())).findFirst().orElse(null);
                ActiveAnomalyInfo infoData = new ActiveAnomalyInfo();
                infoData.setWorkstationCode(info.getWorkstationCode());
                infoData.setSn(info.getSn());

                List<AnomalyDto> anomalyInfos = Lists.newArrayList();
                AnomalyDto dto = new AnomalyDto();
                dto.setDefectAnomalyCode(cfgItem == null ? "" : cfgItem.getDefectAnomalyCode());
                dto.setDefectAnomalyDescription(cfgItem == null ? "" : cfgItem.getDefectAnomalyDescription());
                dto.setSource(4);
                anomalyInfos.add(dto);
                infoData.setAnomalyInfos(anomalyInfos);

                activeAnomaly_(infoData);
            });
        }
        if (CollectionUtil.isNotEmpty(insertList)) {
            pqsProductQgCheckListRecordService.insertBatch(insertList);
        }
    }

    /**
     * 激活缺陷
     *
     * @param info
     */
    @Override
    public void activeAnomaly_(ActiveAnomalyInfo info) {
        // 获取配置数据
        List<PqsGradeEntity> gradeEntityList = pqsGradeService.getAllDatas();
        List<PqsDeptEntity> deptEntityList = pqsDeptService.getAllDatas();

        // 从PM中获取工厂结构数据
        ResultVO<PmAllDTO> stationList = new ResultVO().ok(pmVersionProvider.getObjectedPm());
        if (stationList == null || !stationList.getSuccess()) {
            throw new InkelinkException("PM模块根据工位编号获取关联的QG岗位信息失败。" + (stationList == null ? "" : stationList.getMessage()));
        }
        PmWorkStationEntity workstationInfo = stationList.getData().getStations()
                .stream().filter(s -> s.getWorkstationCode().equals(info.getWorkstationCode()))
                .findFirst().orElse(null);
        if (workstationInfo == null) {
            throw new InkelinkException("在启用的工厂模型中未找到该岗位");
        }

        // 线体
        PmLineEntity lineInfo = stationList.getData().getLines()
                .stream().filter(s -> s.getId().equals(workstationInfo.getPrcPmLineId())).findFirst().orElse(null);
        if (lineInfo == null) {
            throw new InkelinkException("在启用的工厂模型中未找到该线体");
        }
        // 车间
        PmWorkShopEntity shopInfo = stationList.getData().getShops()
                .stream().filter(s -> s.getId().equals(lineInfo.getPrcPmWorkshopId())).findFirst().orElse(null);
        // 工厂
        PmOrganizationEntity organization = stationList.getData().getOrganization();

        // 班次
        ResultVO<ShiftDTO> shiftDTOResultVO = new ResultVO().ok(pmShcCalendarProvider.getCurrentShiftInfo(lineInfo.getLineCode()));
        if (shiftDTOResultVO == null || !shiftDTOResultVO.getSuccess()) {
            throw new InkelinkException("PM模块获取班次信息失败。" + (shiftDTOResultVO == null ? "" : shiftDTOResultVO.getMessage()));
        }
        ShiftDTO shcShift = shiftDTOResultVO.getData();

        // 遍历传入的缺陷
        for (AnomalyDto a : info.getAnomalyInfos()) {

            PqsProductDefectAnomalyEntity vAnomaly = null;

            // 空缺陷 跳过验证
            if (StringUtils.isEmpty(a.getDefectAnomalyCode())
                    && StringUtils.isEmpty(a.getDefectAnomalyDescription())) {
                continue;
            }

            // 获取配置缺陷配置
            QueryWrapper<PqsDefectAnomalyEntity> queryWrapperDa = new QueryWrapper<>();
            queryWrapperDa.lambda().eq(PqsDefectAnomalyEntity::getDefectAnomalyCode, a.getDefectAnomalyCode());
            PqsDefectAnomalyEntity anomalyConfig = pqsDefectAnomalyService.getData(queryWrapperDa, false)
                    .stream().findFirst().orElse(null);
            // 记录已有缺陷的备注
            if (null != anomalyConfig && StringUtils.isNotBlank(a.getDescription())) {
                UpdateWrapper<PqsDefectAnomalyEntity> updateWrapper = Wrappers.update();
                updateWrapper.lambda().eq(PqsDefectAnomalyEntity::getId, anomalyConfig.getId())
                        .set(PqsDefectAnomalyEntity::getAttribute1, a.getDescription());
                pqsDefectAnomalyService.update(updateWrapper);
            }

            if (a.getWoId() != 0) {
                // 工艺自定义缺陷
                QueryWrapper<PqsProductDefectAnomalyEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(PqsProductDefectAnomalyEntity::getPrcEpsProductWoId, a.getWoId())
                        .eq(PqsProductDefectAnomalyEntity::getSn, info.getSn())
                        .ne(PqsProductDefectAnomalyEntity::getStatus, 4);
                vAnomaly = pqsProductDefectAnomalyService.getData(queryWrapper, false)
                        .stream().findFirst().orElse(null);
                // 已激活 跳过
                if (vAnomaly != null) {
                    continue;
                }
            } else if (StringUtils.isNotEmpty(a.getDefectAnomalyCode())) {
                // 如果不等于自定义缺陷
                // 判断缺陷是否已被激活，已被激活的缺陷未关闭将不会改变他的状态
                QueryWrapper<PqsProductDefectAnomalyEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(PqsProductDefectAnomalyEntity::getDefectAnomalyCode, a.getDefectAnomalyCode())
                        .eq(PqsProductDefectAnomalyEntity::getSn, info.getSn())
                        .ne(PqsProductDefectAnomalyEntity::getStatus, 4);
                vAnomaly = pqsProductDefectAnomalyService.getData(queryWrapper, false)
                        .stream().findFirst().orElse(null);
                // 缺陷已被激活
                if (vAnomaly != null) {
                    continue;
                }
            }
            if (vAnomaly == null) {
                vAnomaly = new PqsProductDefectAnomalyEntity();
            }

            vAnomaly.setJsonData(a.getJsonData());
            vAnomaly.setId(IdGenerator.getId());
            vAnomaly.setImg(a.getImg());
            vAnomaly.setDefectAnomalyCode(a.getDefectAnomalyCode());
            vAnomaly.setDefectAnomalyDescription(anomalyConfig != null ? anomalyConfig.getDefectAnomalyDescription() : a.getDefectAnomalyDescription());
            // 添加备注信息
            vAnomaly.setRemark(anomalyConfig != null ? anomalyConfig.getDefectCodeDescription() : a.getDescription());
            vAnomaly.setActivateUser(identityHelper.getUserName());
            vAnomaly.setActivateTime(new Date());
            // 等级
            if (StringUtils.isEmpty(a.getGradeCode())) {
                if (anomalyConfig == null) {
                    vAnomaly.setGradeCode(StringUtils.EMPTY);
                }
            } else {
                vAnomaly.setGradeCode(a.getGradeCode());
            }
            if (StringUtils.isEmpty(a.getGradeCode())) {
                if (anomalyConfig == null) {
                    vAnomaly.setGradeName(StringUtils.EMPTY);
                }
            } else {
                PqsGradeEntity pqsGradeEntity = gradeEntityList.stream()
                        .filter(g -> g.getGradeCode().equals(a.getGradeCode())).findFirst().orElse(null);
                vAnomaly.setGradeName(pqsGradeEntity == null ? StringUtils.EMPTY : pqsGradeEntity.getGradeName());
            }

            // 责任部门
            if (StringUtils.isEmpty(a.getResponsibleDeptCode())) {
                if (anomalyConfig == null) {
                    vAnomaly.setResponsibleDeptCode(StringUtils.EMPTY);
                }
            } else {
                vAnomaly.setResponsibleDeptCode(a.getResponsibleDeptCode());
            }
            if (StringUtils.isEmpty(a.getResponsibleDeptCode())) {
                if (anomalyConfig == null) {
                    vAnomaly.setResponsibleDeptName(StringUtils.EMPTY);
                }
            } else {
                PqsDeptEntity pqsDeptEntity = deptEntityList.stream()
                        .filter(g -> g.getDeptCode().equals(a.getResponsibleDeptCode())).findFirst().orElse(null);
                vAnomaly.setResponsibleDeptName(pqsDeptEntity == null ? StringUtils.EMPTY : pqsDeptEntity.getDeptName());
            }

            if (shcShift == null) {
                vAnomaly.setPrcPpsShcShiftId(Constant.DEFAULT_ID);
            } else {
                vAnomaly.setPrcPpsShcShiftId(ConvertUtils.stringToLong(shcShift.getShcCalenderId()));
            }
            vAnomaly.setWorkstationCode(workstationInfo.getWorkstationCode());
            vAnomaly.setWorkstationName(workstationInfo.getWorkstationName());
            vAnomaly.setOrganizationCode(organization.getOrganizationCode());
            vAnomaly.setSn(info.getSn());
            vAnomaly.setStatus(PqsLogicStatusEnum.ACTIVATED.code());
            vAnomaly.setWorkshopCode(shopInfo == null ? "" : shopInfo.getWorkshopCode());
            vAnomaly.setWorkshopName(shopInfo == null ? "" : shopInfo.getWorkshopName());
            vAnomaly.setSource(a.getSource());
            vAnomaly.setPrcEpsProductWoId(a.getWoId());

            // 从PM中获取工厂结构数据
            ResultVO<List<PmWorkStationEntity>> strLists = new ResultVO().ok(pmVersionProvider.getrelevanceworkplacebystation(workstationInfo.getWorkstationCode()));
            if (strLists == null || !strLists.getSuccess()) {
                throw new InkelinkException("PM模块根据工位编号获取关联的QG岗位信息失败。" + (strLists == null ? "" : strLists.getMessage()));
            }
            List<String> collect = strLists.getData().stream().map(PmWorkStationEntity::getWorkstationCode).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                // 绑定关联工位
                String qgWorkstationCodes = String.join(",", collect);
                vAnomaly.setQgWorkstationCode(qgWorkstationCodes + ",");
            }

            // 关联QG工位；2：质量门；6：MP录入站
            if (!vAnomaly.getQgWorkstationCode().contains(workstationInfo.getWorkstationCode() + ",")
                    && (workstationInfo.getWorkstationType() == 2 || workstationInfo.getWorkstationType() == 3 || workstationInfo.getWorkstationType() == 6)) {
                vAnomaly.setQgWorkstationCode(vAnomaly.getQgWorkstationCode() + workstationInfo.getWorkstationCode() + ",");
            }

            // 插入数据
            pqsProductDefectAnomalyService.insert(vAnomaly);

            writeAnomalyLog(vAnomaly.getId(), PqsLogicStatusEnum.ACTIVATED.code());
        }
    }

    /**
     * 写入日志
     *
     * @param dataId     缺陷ID
     * @param statusCode
     */
    private void writeAnomalyLog(Long dataId, int statusCode) {
        PqsProductDefectAnomalyLogEntity logEntity = new PqsProductDefectAnomalyLogEntity();
        logEntity.setPrcPqsProductDefectAnomalyId(dataId);
        logEntity.setRemark(identityHelper.getUserName() + "将状态更改为" + PqsLogicStatusEnum.getValue(statusCode));
        logEntity.setStatus(statusCode);
        pqsProductDefectAnomalyLogService.insert(logEntity);
    }

    /**
     * 修改缺陷等级、责任班组、责任部门信息
     *
     * @param info
     */
    @Override
    public void modifyDefectAnomalyRepsponsibelInfo(ModifyDefectResponsibleInfo info) {

        pqsProductDefectAnomalyService.modifyDefectAnomalyRepsponsibelInfo(info);
    }

    @Override
    public void saveChange() {
        unitOfWorkService.saveChange();
    }

    /**
     * 获取缺陷日志
     *
     * @param anomalyId
     * @return
     */
    @Override
    public List<ProductAnomalyLogDto> getProductDefectAnomalyLog(Long anomalyId) {

        List<ProductAnomalyLogDto> resultList = Lists.newArrayList();

        QueryWrapper<PqsProductDefectAnomalyLogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PqsProductDefectAnomalyLogEntity::getPrcPqsProductDefectAnomalyId, anomalyId)
                .orderByDesc(BaseEntity::getCreationDate);
        List<PqsProductDefectAnomalyLogEntity> logEntityList = pqsProductDefectAnomalyLogService.getData(queryWrapper, false);
        if (CollectionUtil.isNotEmpty(logEntityList)) {
            logEntityList.forEach(l -> {
                ProductAnomalyLogDto dto = new ProductAnomalyLogDto();
                dto.setCreatedUser(l.getCreatedUser());
                dto.setOperDt(l.getCreationDate());
                dto.setRemark(l.getRemark());
                dto.setStatus(l.getStatus());
                resultList.add(dto);
            });
        }

        return resultList;
    }

    /**
     * 添加激活缺陷备注
     *
     * @param dataId
     * @param remark
     */
    @Override
    public void appendDefectAnomalyRemark(Long dataId, String remark) {

        PqsProductDefectAnomalyEntity anomalyEntity = pqsProductDefectAnomalyService.get(dataId);
        if (anomalyEntity == null) {
            throw new InkelinkException("未找到对应的激活缺陷");
        }
        anomalyEntity.setRemark(remark);
        pqsProductDefectAnomalyService.update(anomalyEntity);

        PqsProductDefectAnomalyLogEntity logEntity = new PqsProductDefectAnomalyLogEntity();
        logEntity.setPrcPqsProductDefectAnomalyId(dataId);
        logEntity.setRemark(identityHelper.getUserName() + "添加备注" + "【" + remark + "】");
        logEntity.setStatus(anomalyEntity.getStatus());
        pqsProductDefectAnomalyLogService.insert(logEntity);

        pqsProductDefectAnomalyService.saveChange();
    }

    /**
     * @param workstationCode 工位代码
     * @param sn              唯一码
     * @return
     */
    @Override
    public List<QgGateVarificationDto> getQgGateVarificationsList(String workstationCode, String sn) {

        List<QgGateVarificationDto> resultData = Lists.newArrayList();

        // 获取质量门拦截配置
        PqsQgWorkstationConfigEntity qgWorkstationConfigEntity = pqsQgWorkstationConfigService.getAllDatas().stream()
                .filter(p -> StringUtils.equals(p.getWorkstationCode(), workstationCode)).findFirst().orElse(null);
        if (qgWorkstationConfigEntity != null && StringUtils.isNotEmpty(qgWorkstationConfigEntity.getPqsFunction())) {
            List<String> pqsFunctions = Arrays.stream(qgWorkstationConfigEntity.getPqsFunction().split(","))
                    .collect(Collectors.toList());
            List<ComboInfoDTO> comboDatas = sysConfigurationProvider.getComboDatas(pqsQgFunctionKey);

            // 从PM中获取工厂结构数据
            ResultVO<PmAllDTO> stationList = new ResultVO().ok(pmVersionProvider.getObjectedPm());
            if (stationList == null || !stationList.getSuccess()) {
                throw new InkelinkException("PM模块根据工位编号获取关联的QG岗位信息失败。" + (stationList == null ? "" : stationList.getMessage()));
            }
            PmWorkStationEntity workstationInfo = stationList.getData().getAllStations()
                    .stream().filter(s -> StringUtils.equals(s.getWorkstationCode(), workstationCode))
                    .findFirst().orElse(null);
            if (workstationInfo == null) {
                throw new InkelinkException("在启用的工厂模型中未找到该岗位");
            }
            List<String> workstationCodes = Lists.newArrayList();
            if (StringUtils.isNotEmpty(workstationInfo.getStations())) {
                workstationCodes = Arrays.stream(workstationInfo.getStations().split(",")).collect(Collectors.toList());
                workstationCodes.add(workstationCode);
            }

            // 遍历质量门拦截配置
            for (String f : pqsFunctions) {
                QgGateVarificationDto dto = new QgGateVarificationDto();
                if (CollectionUtil.isNotEmpty(comboDatas)) {
                    ComboInfoDTO comboInfoDTO = comboDatas.stream().filter(c -> StringUtils.equals(c.getValue(), f))
                            .findFirst().orElse(null);
                    dto.setCheckItem(comboInfoDTO != null ? comboInfoDTO.getText() : StringUtils.EMPTY);
                }
                switch (f) {
                    // 缺陷阻塞
                    case "1":
                        QueryWrapper<PqsProductDefectAnomalyEntity> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(PqsProductDefectAnomalyEntity::getSn, sn);
                        queryWrapper.lambda().ne(PqsProductDefectAnomalyEntity::getStatus, 4);
                        // 存在关联工位，只找当前工位和关联工位的缺陷阻塞，反之找所有
                        if (CollectionUtils.isNotEmpty(workstationCodes)) {
                            queryWrapper.lambda().in(PqsProductDefectAnomalyEntity::getWorkstationCode, workstationCodes);
                        }
                        List<PqsProductDefectAnomalyEntity> data = pqsProductDefectAnomalyService.getData(queryWrapper, false);
                        dto.setResult(CollectionUtils.isEmpty(data));
                        resultData.add(dto);
                        break;
                    // 必检项
                    case "9":
                        List<ConditionDto> conditions = Lists.newArrayList();
                        ConditionDto conditionDto = new ConditionDto();
                        conditionDto.setColumnName("sn");
                        conditionDto.setOperator(ConditionOper.Equal);
                        conditions.add(conditionDto);
                        List<PpsOrderEntity> ppsOrderLists = ppsOrderProvider.getData(conditions);
                        if (CollectionUtil.isEmpty(ppsOrderLists)) {
                            break;
                        }
                        List<String> models = ppsOrderLists.stream().map(PpsOrderEntity::getModel).collect(Collectors.toList());
                        QueryWrapper<PqsQgCheckListEntity> checkListQuery = new QueryWrapper<>();
                        checkListQuery.lambda().in(PqsQgCheckListEntity::getModel, models);
                        // 配置项
                        List<PqsQgCheckListEntity> checkCfgList = pqsQgCheckListService.getData(checkListQuery, false);
                        // 已检项
                        List<PqsProductQgCheckListRecordEntity> listRecordEntityList = pqsProductQgCheckListRecordService.getAllDatas().stream()
                                .filter(p -> StringUtils.equals(p.getSn(), sn)).collect(Collectors.toList());
                        List<PqsQgCheckedListDto> checkSummary = Lists.newArrayList();
                        if (CollectionUtil.isNotEmpty(listRecordEntityList)) {
                            for (PqsProductQgCheckListRecordEntity c : listRecordEntityList) {
                                PqsQgCheckedListDto checkedListDto = new PqsQgCheckedListDto();
                                checkedListDto.setId(c.getPrcPqsQgCheckListId());
                                checkedListDto.setWorkstationCode(c.getWorkstationCode());
                                checkedListDto.setResult(c.getResult());
                                checkSummary.add(checkedListDto);
                            }
                        }
                        // 存在未检项
                        List<PqsQgCheckListEntity> unQgCheckList = Lists.newArrayList();
                        for (PqsQgCheckListEntity c : checkCfgList) {
                            for (PqsQgCheckedListDto s : checkSummary) {
                                if (c.getId().equals(s.getId())) {
                                    unQgCheckList.add(c);
                                }
                            }
                        }
                        checkCfgList.removeAll(unQgCheckList);
                        if (CollectionUtil.isNotEmpty(checkCfgList)) {
                            for (PqsQgCheckListEntity p : checkCfgList) {
                                PqsQgCheckedListDto checkedListDto = new PqsQgCheckedListDto();
                                checkedListDto.setId(p.getId());
                                checkedListDto.setWorkstationCode(p.getWorkstationCode());
                                checkedListDto.setResult(0);
                                checkSummary.add(checkedListDto);
                            }
                        }

                        // 过滤工位
                        if (CollectionUtil.isNotEmpty(workstationCodes)) {
                            if (CollectionUtil.isNotEmpty(checkSummary)) {
                                List<String> finalWorkstationCodes = workstationCodes;
                                checkSummary = checkSummary.stream()
                                        .filter(u -> finalWorkstationCodes.contains(u.getWorkstationCode()))
                                        .collect(Collectors.toList());
                            }
                        }

                        if (CollectionUtil.isNotEmpty(checkSummary)) {
                            List<PqsQgCheckedListDto> collect = checkSummary.stream()
                                    .filter(u -> u.getResult() != 1).collect(Collectors.toList());
                            dto.setResult(CollectionUtil.isEmpty(collect));
                            resultData.add(dto);
                            break;
                        }

                        break;
                    // 问题排查不存在关联工位
                    case "10":
                        PqsDefectAnomalyRiskDetailEntity pqsDefectAnomalyRiskDetailEntity = pqsDefectAnomalyRiskDetailService.getAllDatas()
                                .stream().filter(dar -> StringUtils.equals(dar.getSn(), sn) && dar.getStatus() < 90)
                                .findFirst().orElse(null);
                        dto.setResult(pqsDefectAnomalyRiskDetailEntity == null);
                        resultData.add(dto);
                        break;
                    // 所有工艺
                    case "20":
                        List<EpsVehicleWoEntity> epsVehicleWoDatas = epsVehicleWoProvider.getEpsVehicleWoDatas(sn);
                        if (CollectionUtil.isNotEmpty(epsVehicleWoDatas)) {
                            // 存在关联工位，只找当前工位和关联工位的所有工艺，反之找所有
                            if (CollectionUtils.isNotEmpty(workstationCodes)) {
                                epsVehicleWoDatas = epsVehicleWoDatas.stream()
                                        .filter(e -> StringUtils.equals(e.getWorkstationCode(), e.getWorkstationCode()))
                                        .collect(Collectors.toList());
                            }
                            dto.setResult(CollectionUtil.isEmpty(epsVehicleWoDatas));
                            resultData.add(dto);
                        }
                        break;
                    // 追溯工艺
                    case "21":
                        List<EpsVehicleWoEntity> epsVehicleWoDatas_ = epsVehicleWoProvider.getEpsVehicleWoDatas(sn);
                        if (CollectionUtil.isNotEmpty(epsVehicleWoDatas_)) {
                            // 存在关联工位，只找当前工位和关联工位的追溯工艺，反之找所有
                            if (CollectionUtils.isNotEmpty(workstationCodes)) {
                                epsVehicleWoDatas_ = epsVehicleWoDatas_.stream()
                                        .filter(e -> StringUtils.equals(e.getWorkstationCode(), e.getWorkstationCode())
                                                && e.getWoType() == 2)
                                        .collect(Collectors.toList());
                            }
                            dto.setResult(CollectionUtil.isEmpty(epsVehicleWoDatas_));
                            resultData.add(dto);
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        return resultData;
    }

    /**
     * 获取质量门阻塞信息
     *
     * @param workstationCode 工位代码
     * @param sn              唯一码
     * @return
     */
    @Override
    public QgGateVarificationResultDto getQgGateVarificationsResult(String workstationCode, String sn) {

        QgGateVarificationResultDto resultDto = new QgGateVarificationResultDto();

        QueryWrapper<PqsQgWorkstationConfigEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PqsQgWorkstationConfigEntity::getWorkstationCode, workstationCode);
        PqsQgWorkstationConfigEntity qgworkstationCfg = pqsQgWorkstationConfigService.getData(queryWrapper, false)
                .stream().findFirst().orElse(null);
        if (qgworkstationCfg == null) {
            resultDto.setResult(true);
            resultDto.setAllowByPass(true);
            return resultDto;
        }

        Map<String, Object> maps = new HashMap<>(2);
        maps.put("workstationCode", workstationCode);
        maps.put("sn", sn);
        List<QgGateVarificationDto> checkItemResult = pqsLogicDao.getQgGateVarificationsResult(maps);
        resultDto.setAllowByPass(qgworkstationCfg.getAllowBypass());

        List<QgGateVarificationDto> trueLists = checkItemResult.stream().filter(QgGateVarificationDto::isResult).collect(Collectors.toList());
        resultDto.setResult(trueLists.size() == checkItemResult.size());

        return resultDto;
    }

    /**
     * 获取车辆去向
     *
     * @param sn 唯一码
     * @return
     */
    @Override
    public List<PqsRouteDto> getUnHandleRouteInfo(String sn) {
        List<PqsRouteDto> resultList = Lists.newArrayList();

        QueryWrapper<PqsQualityRouteRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PqsQualityRouteRecordEntity::getSn, sn)
                .eq(PqsQualityRouteRecordEntity::getIsHandle, false)
                .orderByAsc(BaseEntity::getCreationDate);
        List<PqsQualityRouteRecordEntity> recordEntityList = pqsQualityRouteRecordService.getData(queryWrapper, false);
        if (CollectionUtil.isNotEmpty(recordEntityList)) {
            recordEntityList.forEach(r -> {
                PqsRouteDto info = new PqsRouteDto();
                BeanUtils.copyProperties(r, info);
                info.setDataId(r.getId());
                resultList.add(info);
            });
        }

        return resultList;
    }

    /**
     * 完成车辆去向处理
     *
     * @param dataId 路由记录ID
     */
    @Override
    public void setRouteHand(String dataId) {

        PqsQualityRouteRecordEntity record = pqsQualityRouteRecordService.get(dataId);
        // 找不到记录 或是记录已被处理 跳过
        if (record == null || record.getIsHandle()) {
            return;
        }

        PqsQualityRouteRecordEntity entity = new PqsQualityRouteRecordEntity();
        entity.setId(ConvertUtils.stringToLong(dataId));
        entity.setIsHandle(true);
        pqsQualityRouteRecordService.updateById(entity);
    }

    /**
     * 删除路由去向
     *
     * @param routeDeleteRequest 路由记录ID,sn
     */
    @Override
    public void deleteRouteRecord(QgCheckItemInfo routeDeleteRequest) {

        PqsQualityRouteRecordEntity record = pqsQualityRouteRecordService.getAllDatas()
                .stream().filter(p -> p.getId().equals(routeDeleteRequest.getId()) && p.getSn().equals(routeDeleteRequest.getSn()))
                .sorted(Comparator.comparing(BaseEntity::getCreationDate)).findFirst().orElse(null);
        // 找不到记录 或是记录已被处理 跳过
        if (record == null || record.getIsHandle()) {
            return;
        }

        PqsQualityRouteRecordEntity dto = new PqsQualityRouteRecordEntity();
        dto.setId(record.getId());
        dto.setIsHandle(true);
        pqsQualityRouteRecordService.update(dto);
    }

    /**
     * 获取车辆去向路由点位
     *
     * @param workstationCode
     * @param sn
     * @return
     */
    @Override
    public List<ProductRoutePointDto> getProductRoutePoint(String workstationCode, String sn) {

        List<ProductRoutePointDto> resultList = Lists.newArrayList();

        QueryWrapper<PqsQualityRouteRecordEntity> recordEntityQueryWrapper = new QueryWrapper<>();
        recordEntityQueryWrapper.lambda().eq(PqsQualityRouteRecordEntity::getSn, sn)
                .eq(PqsQualityRouteRecordEntity::getIsHandle, false);
        List<PqsQualityRouteRecordEntity> currentRouteRecord = pqsQualityRouteRecordService.getData(recordEntityQueryWrapper, false);

        QueryWrapper<PqsQualityRoutePointEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PqsQualityRoutePointEntity::getWorkstationCode, workstationCode)
                .orderByAsc(PqsQualityRoutePointEntity::getDisplayNo);
        List<PqsQualityRoutePointEntity> lists = pqsQualityRoutePointService.getData(queryWrapper, false);
        if (CollectionUtil.isNotEmpty(lists)) {
            lists.forEach(l -> {
                ProductRoutePointDto dto = new ProductRoutePointDto();
                dto.setId(l.getId());
                dto.setAreaCode(l.getAreaCode());
                dto.setAreaName(l.getAreaName());
                dto.setRcCode(l.getRcCode());
                resultList.add(dto);
            });
        }

        if (CollectionUtil.isNotEmpty(resultList)) {
            resultList.forEach(r -> {
                r.setIsActive(currentRouteRecord.contains(r.getId()));
            });
        }

        return resultList;
    }

    /**
     * 设置车辆去向
     *
     * @param qgSetProductRouteInfo 车辆去向请求
     */
    @Override
    public void setProductRoute(QgSetProductRouteInfo qgSetProductRouteInfo) {

        PqsQualityRoutePointEntity pointEntity = pqsQualityRoutePointService.get(qgSetProductRouteInfo.getPointId());
        if (pointEntity == null) {
            throw new InkelinkException("去向配置未找到！");
        }

        // 如果已存在就不保存 工位+sn+handle状态
        QueryWrapper<PqsQualityRouteRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PqsQualityRouteRecordEntity::getWorkstationCode, pointEntity.getWorkstationCode())
                .eq(PqsQualityRouteRecordEntity::getSn, qgSetProductRouteInfo.getSn())
                .eq(PqsQualityRouteRecordEntity::getIsHandle, false);
        List<PqsQualityRouteRecordEntity> recordEntityList = pqsQualityRouteRecordService.getData(queryWrapper, false);
        if (CollectionUtil.isNotEmpty(recordEntityList)) {
            return;
        }

        PqsQualityRouteRecordEntity entity = new PqsQualityRouteRecordEntity();
        entity.setId(IdGenerator.getId());
        entity.setWorkstationCode(pointEntity.getWorkstationCode());
        entity.setWorkstationName(pointEntity.getWorkstationName());
        entity.setAreaCode(pointEntity.getAreaCode());
        entity.setAreaName(pointEntity.getAreaName());
        entity.setRcCode(pointEntity.getRcCode());
        entity.setRemark(qgSetProductRouteInfo.getRemark());
        entity.setSn(qgSetProductRouteInfo.getSn());
        entity.setIsHandle(false);
        entity.setPrcPqsQualityRoutePointId(qgSetProductRouteInfo.getPointId());
        pqsQualityRouteRecordService.insert(entity);
    }

    /**
     * 工艺完成关闭缺陷
     *
     * @param woId 工艺ID
     */
    @Override
    public void closeAnomlytByWoId(Long woId) {

        PqsProductDefectAnomalyEntity anomaly = pqsProductDefectAnomalyService.getAllDatas()
                .stream().filter(p -> p.getPrcEpsProductWoId().equals(woId)).findFirst().orElse(null);
        // 找不到缺陷 或缺陷已关闭直接跳过
        if (anomaly == null || anomaly.getStatus() == 5) {
            return;
        }

        PqsProductDefectAnomalyEntity info = new PqsProductDefectAnomalyEntity();
        info.setId(anomaly.getId());
        info.setRepairTime(new Date());
        info.setRepairWay("5");
        info.setRepairUser(identityHelper.getUserName());
        info.setRecheckTime(new Date());
        info.setRecheckUser(identityHelper.getUserName());
        pqsProductDefectAnomalyService.update(info);
        pqsProductDefectAnomalyService.saveChange();

        writeAnomalyLog(anomaly.getId(), PqsLogicStatusEnum.QUALIFIED.code());
    }

    @Override
    public void receiveIccData(List<MidIccApiEntity> dtos) {
        List<MidIccApiEntity> insertDto = dtos.stream().filter(c -> c.getOpCode() == 1 && c.getExeStatus() == 0).collect(Collectors.toList());
        List<MidIccApiEntity> updateDto = dtos.stream().filter(c -> c.getOpCode() == 2 && c.getExeStatus() == 0).collect(Collectors.toList());
        List<MidIccApiEntity> deleteDto = dtos.stream().filter(c -> c.getOpCode() == 3 && c.getExeStatus() == 0).collect(Collectors.toList());
        List<MidIccApiEntity> insertOrUpdateDto = dtos.stream().filter(c -> c.getOpCode() == 4 && c.getExeStatus() == 0).collect(Collectors.toList());


        insertIccData(insertDto);
        insertOrUpdateIccData(insertOrUpdateDto);
        updateIccData(updateDto);
        deleteIccData(deleteDto);
    }

    @Override
    public void receiveIccCategaryData(List<MidIccCategoryApiEntity> dtos) {
        List<MidIccCategoryApiEntity> insertDto = dtos.stream().filter(c -> c.getOpCode() == 1 && c.getExeStatus() == 0).collect(Collectors.toList());
        List<MidIccCategoryApiEntity> updateDto = dtos.stream().filter(c -> c.getOpCode() == 2 && c.getExeStatus() == 0).collect(Collectors.toList());
        List<MidIccCategoryApiEntity> deleteDto = dtos.stream().filter(c -> c.getOpCode() == 3 && c.getExeStatus() == 0).collect(Collectors.toList());
        List<MidIccCategoryApiEntity> insertOrUpdateDto = dtos.stream().filter(c -> c.getOpCode() == 4 && c.getExeStatus() == 0).collect(Collectors.toList());

        insertIccCategoryData(insertDto);
        insertOrUpdateIccCategoryData(insertOrUpdateDto);
        updateIccCategoryData(updateDto);
        deleteIccCategoryData(deleteDto);

    }

    @Override
    public void setDefectDescription(DefectDescriptionDto defectDescriptionDto) {
        UpdateWrapper<PqsDefectAnomalyEntity> updateWrapper = Wrappers.update();
        updateWrapper.lambda().set(PqsDefectAnomalyEntity::getAttribute1, defectDescriptionDto.getDescription())
                .eq(PqsDefectAnomalyEntity::getId, defectDescriptionDto.getId());
        pqsDefectAnomalyService.update(updateWrapper);
        pqsQualityGateService.saveChange();
    }

    private void deleteIccData(List<MidIccApiEntity> deleteDto) {
        if (CollectionUtils.isNotEmpty(deleteDto)) {
            List<PqsDefectAnomalyEntity> allDatas = pqsDefectAnomalyService.getAllDatas();
            List<Long> deleteData = new ArrayList<>();
            //删除
            deleteDto.forEach(c -> {
                PqsDefectAnomalyEntity check = allDatas.stream().filter(x -> x.getDefectAnomalyCode().equals(c.getIccCode())).findFirst().orElse(null);
                String anomalyMsg = "ICC代码不存在";
                if (check != null) {
                    c.setExeStatus(1);
                    c.setExeTime(new Date());
                    deleteData.add(check.getId());
                } else {
                    c.setCheckResult(1);
                    c.setExeStatus(3);
                    c.setExeMsg(anomalyMsg);
                    c.setExeTime(new Date());
                    c.setCheckResultDesc(anomalyMsg);
                }
            });
            if (CollectionUtils.isNotEmpty(deleteData)) {
                pqsDefectAnomalyService.delete(deleteData.toArray(new Serializable[deleteData.size()]));
                pqsDefectAnomalyService.saveChange();
            }
            iccApiService.updateBatchById(deleteDto);
            iccApiService.saveChange();
        }
    }

    private void updateIccData(List<MidIccApiEntity> updateDto) {
        if (CollectionUtils.isNotEmpty(updateDto)) {
            List<PqsDefectAnomalyEntity> allDatas = pqsDefectAnomalyService.getAllDatas();
            List<PqsDefectAnomalyEntity> updateData = new ArrayList<>();
            //更新
            updateDto.forEach(c -> {
                PqsDefectAnomalyEntity check = allDatas.stream().filter(x -> x.getDefectAnomalyCode().equals(c.getIccCode())).findFirst().orElse(null);
                String anomalyMsg = "ICC代码不存在";
                if (check != null) {
                    PqsDefectAnomalyEntity anomalyEntity = new PqsDefectAnomalyEntity();
                    anomalyEntity.setId(check.getId());
                    anomalyEntity.setComponentDescription(c.getIccParts());
                    anomalyEntity.setDefectAnomalyCode(c.getIccCode());//ICC
                    anomalyEntity.setDefectAnomalyDescription(c.getIccFaultName());//ICC名称
                    anomalyEntity.setDefectCodeDescription(c.getIccModel());//缺陷名称
                    anomalyEntity.setGradeCode(c.getIccGrade());
                    anomalyEntity.setGradeName(c.getIccTaxonomyName());
                    c.setExeStatus(1);
                    c.setExeTime(new Date());
                    updateData.add(anomalyEntity);
                } else {
                    c.setCheckResult(1);
                    c.setExeStatus(3);
                    c.setExeMsg(anomalyMsg);
                    c.setExeTime(new Date());
                    c.setCheckResultDesc(anomalyMsg);
                }
            });
            if (CollectionUtils.isNotEmpty(updateData)) {
                pqsDefectAnomalyService.updateBatchById(updateData);
                pqsDefectAnomalyService.saveChange();
            }
            iccApiService.updateBatchById(updateDto);
            iccApiService.saveChange();
        }
    }

    private void insertOrUpdateIccData(List<MidIccApiEntity> insertOrUpdateDto) {
        if (CollectionUtils.isNotEmpty(insertOrUpdateDto)) {
            List<PqsDefectAnomalyEntity> insert4Data = new ArrayList<>();
            List<PqsDefectAnomalyEntity> update4Data = new ArrayList<>();
            //新增or更新
            List<PqsDefectAnomalyEntity> allDatas = pqsDefectAnomalyService.getAllDatas();
            insertOrUpdateDto.forEach(c -> {
                PqsDefectAnomalyEntity check = allDatas.stream().filter(x -> x.getDefectAnomalyCode().equals(c.getIccCode())).findFirst().orElse(null);
                PqsDefectAnomalyEntity anomalyEntity = new PqsDefectAnomalyEntity();
                anomalyEntity.setId(check.getId());
                anomalyEntity.setComponentDescription(c.getIccParts());
                anomalyEntity.setDefectAnomalyCode(c.getIccCode());//ICC
                anomalyEntity.setDefectAnomalyDescription(c.getIccFaultName());//ICC名称
                anomalyEntity.setDefectCodeDescription(c.getIccModel());//缺陷名称
                anomalyEntity.setGradeCode(c.getIccGrade());
                anomalyEntity.setGradeName(c.getIccTaxonomyName());
                c.setExeStatus(1);
                c.setExeTime(new Date());
                if (check != null) {
                    anomalyEntity.setId(check.getId());
                    update4Data.add(anomalyEntity);
                } else {
                    anomalyEntity.setId(IdGenerator.getId());
                    insert4Data.add(anomalyEntity);
                }
            });
            if (CollectionUtils.isNotEmpty(insert4Data)) {
                pqsDefectAnomalyService.insertBatch(insert4Data);
            }
            if (CollectionUtils.isNotEmpty(update4Data)) {
                pqsDefectAnomalyService.updateBatchById(update4Data);
            }
            pqsDefectAnomalyService.saveChange();
            iccApiService.updateBatchById(insertOrUpdateDto);
            iccApiService.saveChange();
        }
    }

    private void insertIccData(List<MidIccApiEntity> insertDto) {
        if (CollectionUtils.isNotEmpty(insertDto)) {
            List<PqsDefectAnomalyEntity> insertData = new ArrayList<>();
            List<PqsDefectAnomalyEntity> allDatas = pqsDefectAnomalyService.getAllDatas();
            insertDto.forEach(c -> {
                PqsDefectAnomalyEntity check = allDatas.stream().filter(x -> x.getDefectAnomalyCode().equals(c.getIccCode())).findFirst().orElse(null);
                String anomalyMsg = "ICC代码重复";
                if (check != null) {
                    c.setCheckResult(1);
                    c.setExeStatus(3);
                    c.setExeMsg(anomalyMsg);
                    c.setExeTime(new Date());
                    c.setCheckResultDesc(anomalyMsg);
                } else {
                    PqsDefectAnomalyEntity anomalyEntity = new PqsDefectAnomalyEntity();
                    anomalyEntity.setId(IdGenerator.getId());
                    anomalyEntity.setComponentDescription(c.getIccParts());
                    anomalyEntity.setDefectAnomalyCode(c.getIccCode());//ICC
                    anomalyEntity.setDefectAnomalyDescription(c.getIccFaultName());//ICC名称
                    anomalyEntity.setDefectCodeDescription(c.getIccModel());//缺陷名称
                    anomalyEntity.setGradeCode(c.getIccGrade());
                    anomalyEntity.setGradeName(c.getIccTaxonomyName());
                    c.setExeStatus(1);
                    c.setExeTime(new Date());
                    insertData.add(anomalyEntity);
                }
            });
            if (CollectionUtils.isNotEmpty(insertData)) {
                pqsDefectAnomalyService.insertBatch(insertData);
                pqsDefectAnomalyService.saveChange();
            }
            iccApiService.updateBatchById(insertDto);
            iccApiService.saveChange();
        }
    }


    private void deleteIccCategoryData(List<MidIccCategoryApiEntity> deleteDto) {
        if (CollectionUtils.isNotEmpty(deleteDto)) {
            List<PqsGradeEntity> allDatas = pqsGradeService.getAllDatas();
            List<Long> deleteData = new ArrayList<>();
            //删除
            deleteDto.forEach(c -> {
                PqsGradeEntity check = allDatas.stream().filter(x -> x.getGradeCode().equals(c.getIccCsGradeItgwWeightValue())).findFirst().orElse(null);
                String anomalyMsg = "等级代码不存在";
                if (check != null) {
                    c.setExeStatus(1);
                    c.setExeTime(new Date());
                    deleteData.add(check.getId());
                } else {
                    c.setCheckResult(1);
                    c.setExeStatus(3);
                    c.setExeMsg(anomalyMsg);
                    c.setExeTime(new Date());
                    c.setCheckResultDesc(anomalyMsg);
                }
            });
            if (CollectionUtils.isNotEmpty(deleteData)) {
                pqsGradeService.delete(deleteData.toArray(new Serializable[deleteData.size()]));
                pqsGradeService.saveChange();
            }
            iccCategoryApiService.updateBatchById(deleteDto);
            iccCategoryApiService.saveChange();
        }
    }

    private void updateIccCategoryData(List<MidIccCategoryApiEntity> updateDto) {
        if (CollectionUtils.isNotEmpty(updateDto)) {
            List<PqsGradeEntity> allDatas = pqsGradeService.getAllDatas();
            List<PqsGradeEntity> updateData = new ArrayList<>();
            //更新
            updateDto.forEach(c -> {
                PqsGradeEntity check = allDatas.stream().filter(x -> x.getGradeCode().equals(c.getIccCsGradeItgwWeightValue())).findFirst().orElse(null);
                String anomalyMsg = "等级代码不存在";
                if (check != null) {
                    PqsGradeEntity gradeEntity = new PqsGradeEntity();
                    gradeEntity.setId(check.getId());
                    gradeEntity.setGradeCode(c.getIccCsGradeItgwWeightValue());
                    gradeEntity.setGradeName(c.getIccCsTaxonomyIssueClassification());
                    gradeEntity.setRemark(c.getIccCsRemark());
                    gradeEntity.setScore(c.getIccCsScoreAuditScore());
                    c.setExeStatus(1);
                    c.setExeTime(new Date());
                    updateData.add(gradeEntity);
                } else {
                    c.setCheckResult(1);
                    c.setExeStatus(3);
                    c.setExeMsg(anomalyMsg);
                    c.setExeTime(new Date());
                    c.setCheckResultDesc(anomalyMsg);
                }
            });
            if (CollectionUtils.isNotEmpty(updateData)) {
                pqsGradeService.updateBatchById(updateData);
                pqsGradeService.saveChange();
            }
            iccCategoryApiService.updateBatchById(updateDto);
            iccCategoryApiService.saveChange();
        }
    }

    private void insertOrUpdateIccCategoryData(List<MidIccCategoryApiEntity> insertOrUpdateDto) {
        if (CollectionUtils.isNotEmpty(insertOrUpdateDto)) {
            List<PqsGradeEntity> insert4Data = new ArrayList<>();
            List<PqsGradeEntity> update4Data = new ArrayList<>();
            //新增or更新
            List<PqsGradeEntity> allDatas = pqsGradeService.getAllDatas();
            insertOrUpdateDto.forEach(c -> {
                PqsGradeEntity check = allDatas.stream().filter(x -> x.getGradeCode().equals(c.getIccCsGradeItgwWeightValue())).findFirst().orElse(null);
                PqsGradeEntity gradeEntity = new PqsGradeEntity();
                gradeEntity.setGradeCode(c.getIccCsGradeItgwWeightValue());
                gradeEntity.setGradeName(c.getIccCsTaxonomyIssueClassification());
                gradeEntity.setRemark(c.getIccCsRemark());
                gradeEntity.setScore(c.getIccCsScoreAuditScore());
                c.setExeStatus(1);
                c.setExeTime(new Date());
                if (check != null) {
                    gradeEntity.setId(check.getId());
                    update4Data.add(gradeEntity);
                } else {
                    gradeEntity.setId(IdGenerator.getId());
                    insert4Data.add(gradeEntity);
                }
            });
            if (CollectionUtils.isNotEmpty(insert4Data)) {
                pqsGradeService.insertBatch(insert4Data);
            }
            if (CollectionUtils.isNotEmpty(update4Data)) {
                pqsGradeService.updateBatchById(update4Data);
            }
            pqsGradeService.saveChange();
            iccCategoryApiService.updateBatchById(insertOrUpdateDto);
            iccCategoryApiService.saveChange();
        }
    }

    private void insertIccCategoryData(List<MidIccCategoryApiEntity> insertDto) {
        if (CollectionUtils.isNotEmpty(insertDto)) {

            List<PqsGradeEntity> insertData = new ArrayList<>();
            List<PqsGradeEntity> allDatas = pqsGradeService.getAllDatas();
            insertDto.forEach(c -> {
                PqsGradeEntity check = allDatas.stream().filter(x -> x.getGradeCode().equals(c.getIccCsGradeItgwWeightValue())).findFirst().orElse(null);
                String anomalyMsg = "等级代码重复";
                if (check != null) {
                    c.setCheckResult(1);
                    c.setExeStatus(3);
                    c.setExeMsg(anomalyMsg);
                    c.setExeTime(new Date());
                    c.setCheckResultDesc(anomalyMsg);
                } else {
                    PqsGradeEntity gradeEntity = new PqsGradeEntity();
                    gradeEntity.setId(IdGenerator.getId());
                    gradeEntity.setGradeCode(c.getIccCsGradeItgwWeightValue());
                    gradeEntity.setGradeName(c.getIccCsTaxonomyIssueClassification());
                    gradeEntity.setRemark(c.getIccCsRemark());
                    gradeEntity.setScore(c.getIccCsScoreAuditScore());
                    c.setExeStatus(1);
                    c.setExeTime(new Date());
                    insertData.add(gradeEntity);
                }
            });
            if (CollectionUtils.isNotEmpty(insertData)) {
                pqsGradeService.insertBatch(insertData);
                pqsGradeService.saveChange();
            }
            iccCategoryApiService.updateBatchById(insertDto);
            iccCategoryApiService.saveChange();
        }
    }
}
