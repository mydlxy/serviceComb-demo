package com.ca.mfd.prc.audit.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.audit.dto.AuditActiveAnomalyInfo;
import com.ca.mfd.prc.audit.dto.AuditAnomalyStatusModifyRequest;
import com.ca.mfd.prc.audit.dto.AuditDefectAnomalyReponse;
import com.ca.mfd.prc.audit.dto.AuditDefectInfo;
import com.ca.mfd.prc.audit.dto.AuditEntryDto;
import com.ca.mfd.prc.audit.dto.DefectAnomalyDto;
import com.ca.mfd.prc.audit.dto.DefectAnomalyParaInfo;
import com.ca.mfd.prc.audit.dto.EntryParaInfo;
import com.ca.mfd.prc.audit.dto.GetAuditDefectAnomalyRequest;
import com.ca.mfd.prc.audit.dto.ModifyDefectResponsibleInfo;
import com.ca.mfd.prc.audit.dto.OrderVo;
import com.ca.mfd.prc.audit.dto.QgGateConfigurationInfo;
import com.ca.mfd.prc.audit.dto.QgMatrikConfigurationInfo;
import com.ca.mfd.prc.audit.dto.ShowQgMatrikDto;
import com.ca.mfd.prc.audit.entity.AuditQualityGateAnomalyEntity;
import com.ca.mfd.prc.audit.entity.AuditQualityGateBlankEntity;
import com.ca.mfd.prc.audit.entity.AuditQualityGateEntity;
import com.ca.mfd.prc.audit.entity.AuditQualityGateTcEntity;
import com.ca.mfd.prc.audit.entity.AuditQualityGateWorkstationEntity;
import com.ca.mfd.prc.audit.entity.AuditQualityMatrikAnomalyEntity;
import com.ca.mfd.prc.audit.entity.AuditQualityMatrikEntity;
import com.ca.mfd.prc.audit.entity.AuditQualityMatrikTcEntity;
import com.ca.mfd.prc.audit.entity.AuditQualityMatrikWorkstationEntity;
import com.ca.mfd.prc.audit.entity.PqsAuditDefectAnomalyEntity;
import com.ca.mfd.prc.audit.entity.PqsAuditDeptEntity;
import com.ca.mfd.prc.audit.entity.PqsAuditEntryAttchmentEntity;
import com.ca.mfd.prc.audit.entity.PqsAuditEntryDefectAnomalyEntity;
import com.ca.mfd.prc.audit.entity.PqsAuditEntryEntity;
import com.ca.mfd.prc.audit.entity.PqsAuditGradeEntity;
import com.ca.mfd.prc.audit.entity.PqsAuditProjectEntity;
import com.ca.mfd.prc.audit.entity.PqsAuditStageEntity;
import com.ca.mfd.prc.audit.enums.PqsLogicStatusEnum;
import com.ca.mfd.prc.audit.mapper.IPqsAuditEntryDefectAnomalyMapper;
import com.ca.mfd.prc.audit.mapper.IPqsAuditLogicMapper;
import com.ca.mfd.prc.audit.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.audit.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.audit.remote.app.pm.IPmShcCalendarService;
import com.ca.mfd.prc.audit.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.audit.remote.app.pm.dto.ShiftDTO;
import com.ca.mfd.prc.audit.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.audit.remote.app.pm.entity.PmOrganizationEntity;
import com.ca.mfd.prc.audit.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.audit.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.audit.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.audit.remote.app.pps.IPpsOrderService;
import com.ca.mfd.prc.audit.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.audit.remote.app.pps.entity.PpsPlanPartsEntity;
import com.ca.mfd.prc.audit.remote.app.pps.provider.PpsPlanPartsProvider;
import com.ca.mfd.prc.audit.service.IAuditQualityGateAnomalyService;
import com.ca.mfd.prc.audit.service.IAuditQualityGateBlankService;
import com.ca.mfd.prc.audit.service.IAuditQualityGateService;
import com.ca.mfd.prc.audit.service.IAuditQualityGateTcService;
import com.ca.mfd.prc.audit.service.IAuditQualityGateWorkstationService;
import com.ca.mfd.prc.audit.service.IAuditQualityMatrikAnomalyService;
import com.ca.mfd.prc.audit.service.IAuditQualityMatrikService;
import com.ca.mfd.prc.audit.service.IAuditQualityMatrikTcService;
import com.ca.mfd.prc.audit.service.IAuditQualityMatrikWorkstationService;
import com.ca.mfd.prc.audit.service.IPqsAuditDefectAnomalyService;
import com.ca.mfd.prc.audit.service.IPqsAuditDeptService;
import com.ca.mfd.prc.audit.service.IPqsAuditEntryAttchmentService;
import com.ca.mfd.prc.audit.service.IPqsAuditEntryDefectAnomalyService;
import com.ca.mfd.prc.audit.service.IPqsAuditEntryService;
import com.ca.mfd.prc.audit.service.IPqsAuditGradeService;
import com.ca.mfd.prc.audit.service.IPqsAuditLogicService;
import com.ca.mfd.prc.audit.service.IPqsAuditProjectService;
import com.ca.mfd.prc.audit.service.IPqsAuditStageService;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboDataDTO;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.IUnitOfWorkService;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author eric.zhou
 * @date 2023/4/17
 */
@Service
public class PqsAuditLogicServiceImpl implements IPqsAuditLogicService {


    @Autowired
    private IUnitOfWorkService unitOfWorkService;

    @Autowired
    private IPqsAuditDefectAnomalyService pqsAuditDefectAnomalyService;

    @Autowired
    private IPqsAuditEntryDefectAnomalyService pqsAuditEntryDefectAnomalyService;
    @Resource
    private IPqsAuditEntryDefectAnomalyMapper pqsAuditEntryDefectAnomalyDao;

    @Autowired
    private IPqsAuditEntryAttchmentService pqsAuditEntryAttchmentService;

    @Autowired
    private IPqsAuditDeptService pqsAuditDeptService;

    @Autowired
    private IPqsAuditGradeService pqsAuditGradeService;

    @Autowired
    private PmVersionProvider pmVersionProvider;

    @Autowired
    private IPqsAuditStageService pqsAuditStageService;

    @Autowired
    private IPqsAuditProjectService pqsAuditProjectService;

    @Autowired
    private IdentityHelper identityHelper;

    @Autowired
    private IPqsAuditEntryService pqsAuditEntryService;
    @Autowired
    private IPpsOrderService ppsOrderService;
    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;
    @Autowired
    private PpsPlanPartsProvider ppsPlanPartsProvider;

    @Autowired
    private IPmShcCalendarService pmShcCalendarProvider;

    @Resource
    private IPqsAuditLogicMapper auditLogicMapper;

    @Autowired
    private IAuditQualityMatrikService auditQualityMatrikService;

    @Autowired
    private IAuditQualityGateService auditQualityGateService;
    @Autowired
    private IAuditQualityGateWorkstationService auditQualityGateWorkstationService;
    @Autowired
    private IAuditQualityGateBlankService auditQualityGateBlankService;
    @Autowired
    private IAuditQualityGateTcService auditQualityGateTcService;
    @Autowired
    private IAuditQualityGateAnomalyService auditQualityGateAnomalyService;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private IAuditQualityMatrikTcService auditQualityMatrikTcService;
    @Autowired
    private IAuditQualityMatrikAnomalyService auditQualityMatrikAnomalyService;
    @Autowired
    private IAuditQualityMatrikWorkstationService auditQualityMatrikWorkstationService;


    @Override
    public void saveChange() {
        unitOfWorkService.saveChange();
    }

    @Override
    public PageData<DefectAnomalyDto> getAuditAnomalyShowList(DefectAnomalyParaInfo info) {
        PageDataDto model = new PageDataDto();
        model.setPageIndex(info.getPageIndex());
        model.setPageSize(info.getPageSize());
        if (!StringUtils.isEmpty(info.getKey())) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(new ConditionDto("DEFECT_ANOMALY_CODE", info.getKey(), ConditionOper.Equal, ConditionRelation.Or));
            conditionInfos.add(new ConditionDto("DEFECT_ANOMALY_DESCRIPTION", info.getKey(), ConditionOper.AllLike, ConditionRelation.Or));
            model.setConditions(conditionInfos);
        }
        List<SortDto> sortInfos = new ArrayList<>();
        sortInfos.add(new SortDto("DEFECT_ANOMALY_DESCRIPTION", ConditionDirection.ASC));
        model.setSorts(sortInfos);

        IPage<PqsAuditDefectAnomalyEntity> page = pqsAuditDefectAnomalyService.getDataByPage(model);

        PageData<DefectAnomalyDto> result_ = new PageData<>();
        List<DefectAnomalyDto> anomalyInfoList = Lists.newArrayList();
        page.getRecords().forEach(p -> {
            DefectAnomalyDto anomalyInfo = new DefectAnomalyDto();
            BeanUtils.copyProperties(p, anomalyInfo);
            PqsAuditGradeEntity pqsAuditGradeEntity = pqsAuditGradeService.getAllDatas().stream()
                    .filter(g -> StringUtils.equals(g.getGradeCode(), p.getGradeCode()))
                    .findFirst().orElse(null);
            anomalyInfo.setScore(pqsAuditGradeEntity != null ? pqsAuditGradeEntity.getScore() : 0);
            anomalyInfoList.add(anomalyInfo);
        });
        result_.setDatas(anomalyInfoList);
        result_.setTotal((int) page.getTotal());
        result_.setPageIndex(info.getPageIndex());
        result_.setPageSize(info.getPageSize());

        return result_;
    }

    @Override
    public ResultVO getAuditVehicleDefectAnomalyList(GetAuditDefectAnomalyRequest request) {
        PageData<AuditDefectAnomalyReponse> pageData = new PageData<>();
        Page<AuditDefectAnomalyReponse> page = new Page<>(request.getPageIndex(), request.getPageSize());
        pageData.setPageSize(request.getPageSize());
        pageData.setPageIndex(request.getPageIndex());
        if (request.getIsFlippingLibrary() != null) {
            request.setIsfk(Boolean.TRUE.equals(request.getIsFlippingLibrary()) ? 1 : 0);
        }
        Page<AuditDefectAnomalyReponse> list = pqsAuditEntryDefectAnomalyDao.getVehicleDefectAnomalyList(page, request);
        pageData.setDatas(list.getRecords());
        pageData.setTotal((int) list.getTotal());
        return new ResultVO().ok(pageData, "获取成功");

    }

    @Override
    public ResultVO<List<PqsAuditEntryAttchmentEntity>> getAttchmentbyRecordNo(String recordNo) {
        if (StringUtils.isEmpty(recordNo)) {
            throw new InkelinkException("工单号不能为空");
        }
        QueryWrapper<PqsAuditEntryAttchmentEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PqsAuditEntryAttchmentEntity::getAuditRecordNo, recordNo);
        return new ResultVO<List<PqsAuditEntryAttchmentEntity>>().ok(pqsAuditEntryAttchmentService.getData(queryWrapper, false), "获取成功");
    }

    @Override
    public void saveAttachMent(PqsAuditEntryAttchmentEntity attchmentEntity) {
        Integer recordNo = pqsAuditEntryService.getAllDatas().stream().filter(c -> c.getRecordNo().equals(attchmentEntity.getAuditRecordNo())).map(PqsAuditEntryEntity::getStatus).findFirst().orElse(null);
        if (recordNo != null && recordNo == 90) {
            throw new InkelinkException("已完成的工单不允许操作");
        }
        if (attchmentEntity.getId() == null || attchmentEntity.getId() <= 0) {
            pqsAuditEntryAttchmentService.save(attchmentEntity);
        } else {
            pqsAuditEntryAttchmentService.updateById(attchmentEntity);
        }
        pqsAuditEntryAttchmentService.saveChange();
    }


    @Override
    public void del(Long id) {
        PqsAuditEntryAttchmentEntity pqsAuditEntryAttchmentEntity = pqsAuditEntryAttchmentService.get(id);
        Integer recordNo = pqsAuditEntryService.getAllDatas().stream().filter(c -> c.getRecordNo().equals(pqsAuditEntryAttchmentEntity.getAuditRecordNo())).map(PqsAuditEntryEntity::getStatus).findFirst().orElse(null);
        if (recordNo != null && recordNo == 90) {
            throw new InkelinkException("已完成的工单不允许操作");
        }
        pqsAuditEntryAttchmentService.delete(id);
        pqsAuditEntryAttchmentService.saveChange();

    }

    @Override
    public void activeAuditAnomaly(AuditActiveAnomalyInfo info) {
        // 获取配置数据
        List<PqsAuditGradeEntity> gradeEntityList = pqsAuditGradeService.getAllDatas();
        List<PqsAuditDeptEntity> deptEntityList = pqsAuditDeptService.getAllDatas();

        String vin = "";
        String workstationCode = "";
        PqsAuditEntryEntity auditEntryEntity = pqsAuditEntryService.getAllDatas().stream().filter(x -> x.getRecordNo().equals(info.getRecordNo())).findFirst().orElse(null);
        if (auditEntryEntity != null) {
            if (90 == auditEntryEntity.getStatus()) {
                throw new InkelinkException("已完成的工单不能操作");
            }
            vin = auditEntryEntity.getVin();
            workstationCode = auditEntryEntity.getWorkstationCode();
        }

        // 从PM中获取工厂结构数据
        ResultVO<PmAllDTO> stationList = new ResultVO().ok(pmVersionProvider.getObjectedPm());
        if (stationList == null || !stationList.getSuccess()) {
            throw new InkelinkException("PM模块根据工位编号获取关联的QG岗位信息失败。" + (stationList == null ? "" : stationList.getMessage()));
        }

        String finalWorkstationCode = workstationCode;
        PmWorkStationEntity workstationInfo = stationList.getData().getStations()
                .stream().filter(s -> s.getWorkstationCode().equals(finalWorkstationCode))
                .findFirst().orElse(null);
        if (workstationInfo == null) {
            throw new InkelinkException("在启用的工厂模型中未找到该岗位");
        }

        // 线体
        PmLineEntity lineInfo = stationList.getData().getLines()
                .stream().filter(s -> s.getId().equals(workstationInfo.getPrcPmLineId())).findFirst().orElse(null);
        // 车间
        PmWorkShopEntity shopInfo = stationList.getData().getShops()
                .stream().filter(s -> s.getId().equals(lineInfo.getPrcPmWorkshopId())).findFirst().orElse(null);
        // 工厂
        PmOrganizationEntity organization = stationList.getData().getOrganization();


        // 班次
        ResultVO<ShiftDTO> shiftDTOResultVO = pmShcCalendarProvider.getCurrentShiftInfo(shopInfo.getWorkshopCode());
        if (shiftDTOResultVO == null || !shiftDTOResultVO.getSuccess()) {
            throw new InkelinkException("PM模块获取班次信息失败。" + (shiftDTOResultVO == null ? "" : shiftDTOResultVO.getMessage()));
        }
        ShiftDTO shcShift = shiftDTOResultVO.getData();

        // 遍历传入的缺陷
        for (AuditDefectInfo a : info.getAnomalyInfos()) {
            PqsAuditEntryDefectAnomalyEntity vAnomaly = new PqsAuditEntryDefectAnomalyEntity();

            if (StringUtils.isNotEmpty(a.getDefectAnomalyCode())) {
                // 如果不等于自定义缺陷
                // 判断缺陷是否已被激活，已被激活的缺陷未关闭将不会改变他的状态
                QueryWrapper<PqsAuditEntryDefectAnomalyEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(PqsAuditEntryDefectAnomalyEntity::getDefectAnomalyCode, a.getDefectAnomalyCode())
                        .eq(PqsAuditEntryDefectAnomalyEntity::getSn, vin)
                        .eq(PqsAuditEntryDefectAnomalyEntity::getAuditRecordNo, auditEntryEntity.getRecordNo())
                        .ne(PqsAuditEntryDefectAnomalyEntity::getStatus, 4);
                vAnomaly = pqsAuditEntryDefectAnomalyService.getData(queryWrapper, false)
                        .stream().findFirst().orElse(null);
                // 缺陷已被激活
                if (vAnomaly != null) {
                    continue;
                }
            }
            if (vAnomaly == null) {
                vAnomaly = new PqsAuditEntryDefectAnomalyEntity();
            }

            QueryWrapper<PqsAuditEntryDefectAnomalyEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsAuditEntryDefectAnomalyEntity::getDefectAnomalyCode, a.getDefectAnomalyCode());
            PqsAuditEntryDefectAnomalyEntity anomalyConfig = pqsAuditEntryDefectAnomalyService.getData(queryWrapper, false)
                    .stream().findFirst().orElse(null);

            vAnomaly.setId(IdGenerator.getId());
            vAnomaly.setCategory(info.getCategory());
            vAnomaly.setImg(a.getImg());
            vAnomaly.setDefectAnomalyCode(a.getDefectAnomalyCode());
            vAnomaly.setDefectAnomalyDescription(a.getDefectAnomalyDescription());
            vAnomaly.setActivateUser(identityHelper.getUserName());
            vAnomaly.setEvaluationModeCode(info.getEvaluationModeCode());
            vAnomaly.setEvaluationModeName(info.getEvaluationModeName());
            vAnomaly.setActivateTime(new Date());
            if (StringUtils.isEmpty(a.getGradeCode())) {
                if (anomalyConfig == null || StringUtils.isEmpty(anomalyConfig.getGradeCode())) {
                    vAnomaly.setGradeCode("");
                }
            } else {
                vAnomaly.setGradeCode(a.getGradeCode());
            }
            if (StringUtils.isEmpty(a.getRemark())) {
                if (anomalyConfig == null || StringUtils.isEmpty(anomalyConfig.getRemark())) {
                    vAnomaly.setRemark("");
                }
            } else {
                vAnomaly.setRemark(a.getRemark());
            }

            if (StringUtils.isEmpty(a.getGradeCode())) {
                if (anomalyConfig == null || StringUtils.isEmpty(anomalyConfig.getGradeName())) {
                    vAnomaly.setGradeName("");
                }
            } else {
                String gradeName = gradeEntityList.stream()
                        .filter(g -> g.getGradeCode().equals(a.getGradeCode())).findFirst().orElse(null).getGradeName();
                vAnomaly.setGradeName(StringUtils.isEmpty(gradeName) ? "" : gradeName);
            }

            if (StringUtils.isEmpty(a.getResponsibleDeptCode())) {
                if (anomalyConfig == null || StringUtils.isEmpty(anomalyConfig.getResponsibleDeptCode())) {
                    vAnomaly.setResponsibleDeptCode("");
                }
            } else {
                vAnomaly.setResponsibleDeptCode(a.getResponsibleDeptCode());
            }

            if (StringUtils.isEmpty(a.getResponsibleDeptCode())) {
                if (anomalyConfig == null || StringUtils.isEmpty(anomalyConfig.getResponsibleDeptName())) {
                    vAnomaly.setResponsibleDeptName("");
                }
            } else {
                String deptName = deptEntityList.stream()
                        .filter(g -> g.getDeptCode().equals(a.getResponsibleDeptCode())).findFirst().orElse(null).getDeptName();
                vAnomaly.setResponsibleDeptName(StringUtils.isEmpty(deptName) ? "" : deptName);
            }
            if (shcShift == null) {
                vAnomaly.setPrcPpsShcShiftId(Constant.DEFAULT_ID);
            } else {
                vAnomaly.setPrcPpsShcShiftId(ConvertUtils.stringToLong(shcShift.getShcCalenderId()));
            }
            vAnomaly.setWorkstationCode(workstationInfo.getWorkstationCode());
            vAnomaly.setWorkstationName(workstationInfo.getWorkstationName());
            vAnomaly.setOrganizationCode(organization.getOrganizationCode());
            vAnomaly.setSn(vin);
            vAnomaly.setStatus(PqsLogicStatusEnum.ACTIVATED.code());
            vAnomaly.setWorkshopCode(shopInfo.getWorkshopCode());
            vAnomaly.setWorkshopName(shopInfo.getWorkshopName());
            vAnomaly.setAuditRecordNo(info.getRecordNo());
            vAnomaly.setScore(a.getScore());
            vAnomaly.setIsFlippingLibrary(a.getIsFlippingLibrary());
            // 插入数据
            pqsAuditEntryDefectAnomalyService.insert(vAnomaly);
            pqsAuditEntryDefectAnomalyService.saveChange();
        }
    }

    @Override
    public List<ComboDataDTO> getGradeCombo() {
        List<ComboDataDTO> collect = pqsAuditGradeService.getAllDatas().stream()
                .sorted(Comparator.comparing(PqsAuditGradeEntity::getDisplayNo)).map(c -> {
                    ComboDataDTO dto = new ComboDataDTO();
                    dto.setLabel(c.getGradeName());
                    dto.setText(String.valueOf(c.getScore()));
                    dto.setValue(c.getGradeCode());
                    return dto;
                }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<ComboDataDTO> getDeptCombo() {
        List<ComboDataDTO> collect = pqsAuditDeptService.getAllDatas().stream()
                .sorted(Comparator.comparing(PqsAuditDeptEntity::getDisplayNo)).map(c -> {
                    ComboDataDTO dto = new ComboDataDTO();
                    dto.setText(c.getDeptName());
                    dto.setValue(c.getDeptCode());
                    return dto;
                }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<ComboDataDTO> getStageCombo() {
        List<ComboDataDTO> collect = pqsAuditStageService.getAllDatas().stream()
                .sorted(Comparator.comparing(PqsAuditStageEntity::getDisplayNo)).map(c -> {
                    ComboDataDTO dto = new ComboDataDTO();
                    dto.setText(c.getStageName());
                    dto.setValue(c.getStageCode());
                    return dto;
                }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<ComboDataDTO> getProjectCombo() {
        List<ComboDataDTO> collect = pqsAuditProjectService.getAllDatas().stream()
                .sorted(Comparator.comparing(PqsAuditProjectEntity::getDisplayNo)).map(c -> {
                    ComboDataDTO dto = new ComboDataDTO();
                    dto.setText(c.getProjectName());
                    dto.setValue(c.getProjectCode());
                    return dto;
                }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public ResultVO getEntryByStatus(EntryParaInfo key) {
        if (Objects.isNull(key)) {
            throw new InkelinkException("传入的数据不存在");
        }
        PageData<PqsAuditEntryEntity> pageData = new PageData<>();
        pageData.setPageSize(key.getPageSize());
        pageData.setPageIndex(key.getPageIndex());

        if (StringUtils.isEmpty(key.getKey())) {
            QueryWrapper<PqsAuditEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsAuditEntryEntity::getWorkstationCode, key.getWorkstationCode())
                    .eq(PqsAuditEntryEntity::getCategory, key.getCategory())
                    .in(PqsAuditEntryEntity::getStatus, Arrays.asList(key.getStatus().split(",")))
                    .orderBy(true, false, PqsAuditEntryEntity::getCreationDate);
            Page<PqsAuditEntryEntity> query = (Page<PqsAuditEntryEntity>) pqsAuditEntryService.getDataByPage(queryWrapper, key.getPageIndex(), key.getPageSize());
            pageData.setDatas(query.getRecords());
            pageData.setTotal((int) query.getTotal());

            return new ResultVO().ok(pageData, "获取数据成功");
        }
        PqsAuditEntryEntity data = pqsAuditEntryService.getAllDatas().stream().filter(c -> {
            boolean keyFlag = c.getBarcode().equals(key.getKey()) || c.getVin().equals(key.getKey()) || c.getRecordNo().equals(key.getKey());
            if (keyFlag && c.getWorkstationCode().equals(key.getWorkstationCode())
                    && Arrays.asList(key.getStatus().split(",")).contains(String.valueOf(c.getStatus()))
                    && c.getCategory().equals(key.getCategory())) {
                return true;
            }
            return false;
        }).sorted(Comparator.comparing(PqsAuditEntryEntity::getCreationDate, Comparator.reverseOrder())).findFirst().orElse(null);
        if (data == null) {
            return new ResultVO().ok(pageData, "获取数据成功");
        }
        UpdateWrapper<PqsAuditEntryEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(PqsAuditEntryEntity::getStatus, 2).eq(PqsAuditEntryEntity::getStatus, 1).eq(PqsAuditEntryEntity::getIsDelete, false).eq(PqsAuditEntryEntity::getRecordNo, data.getRecordNo());
        pqsAuditEntryService.update(updateWrapper);
        pqsAuditEntryService.saveChange();
        List<PqsAuditEntryEntity> dataList = new ArrayList<>();
        dataList.add(pqsAuditEntryService.get(data.getId()));
        pageData.setDatas(dataList);
        pageData.setTotal(dataList.size());
        return new ResultVO().ok(pageData, "获取数据成功");

    }

    @Override
    public ResultVO getEntry(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new InkelinkException("传入的数据不存在");
        }
        PqsAuditEntryEntity data = pqsAuditEntryService.getAllDatas().stream()
                .filter(c -> c.getBarcode().equals(key) || c.getVin().equals(key) || c.getRecordNo().equals(key))
                .sorted(Comparator.comparing(PqsAuditEntryEntity::getCreationDate, Comparator.reverseOrder()))
                .findFirst().orElse(null);
        if (data == null) {
            throw new InkelinkException("未获取到数据");
        }
        UpdateWrapper<PqsAuditEntryEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(PqsAuditEntryEntity::getStatus, 2)
                .eq(PqsAuditEntryEntity::getStatus, 1)
                .eq(PqsAuditEntryEntity::getIsDelete, false)
                .eq(PqsAuditEntryEntity::getRecordNo, data.getRecordNo());
        pqsAuditEntryService.update(updateWrapper);
        pqsAuditEntryService.saveChange();
        return new ResultVO().ok(pqsAuditEntryService.get(data.getId()), "获取数据成功");

    }

    /**
     * 创建audit工单
     *
     * @param dto
     * @return
     */
    @Override
    public ResultVO createEntry(AuditEntryDto dto) {

        // 从PM中获取工厂结构数据
        ResultVO<PmAllDTO> stationList = new ResultVO().ok(pmVersionProvider.getObjectedPm());
        if (stationList == null || !stationList.getSuccess()) {
            throw new InkelinkException("PM模块根据工位编号获取关联的QG岗位信息失败。" + (stationList == null ? "" : stationList.getMessage()));
        }

        PmWorkStationEntity workstationInfo = stationList.getData().getStations()
                .stream().filter(s -> s.getWorkstationCode().equals(dto.getWorkstationCode()))
                .findFirst().orElse(null);
        if (workstationInfo == null) {
            throw new InkelinkException("在启用的工厂模型中未找到该岗位");
        }
        dto.setWorkstationName(workstationInfo.getWorkstationName());

        String msg = "";
        PqsAuditEntryEntity entry = new PqsAuditEntryEntity();
        //查询缺陷总数
        Long count = pqsAuditEntryDefectAnomalyService.getAllDatas().stream()
                .filter(c -> c.getAuditRecordNo().equals(dto.getRecordNo())).map(PqsAuditEntryDefectAnomalyEntity::getDefectAnomalyCode).count();
        dto.setDefectCount(count.intValue());
        //查询总扣分数
        int sum = pqsAuditEntryDefectAnomalyService.getAllDatas().stream()
                .filter(c -> c.getAuditRecordNo().equals(dto.getRecordNo())).mapToInt(PqsAuditEntryDefectAnomalyEntity::getScore).sum();
        dto.setTotalScore(sum);
        BeanUtils.copyProperties(dto, entry);
        long id = IdGenerator.getId();
        if (dto.getId() != null && !dto.getId().equals(Constant.DEFAULT_ID)) {
            entry.setStatus(dto.getStatus());
            pqsAuditEntryService.update(entry);
            msg = "保存成功";
        } else {
            // 整车
            if (dto.getCategory() == 1) {
                entry.setVin(dto.getBarcode());
                entry.setCharacteristic2(dto.getCharacteristic2());
            }
            entry.setId(id);
            entry.setStatus(1);
            String recordNo = sysSnConfigProvider.createSn("", "WMS_MOVE_LOCATION_NO");
            entry.setRecordNo(recordNo);
            pqsAuditEntryService.insert(entry);
            msg = "创建成功";
        }
        pqsAuditEntryService.saveChange();
        PqsAuditEntryEntity auditEntryEntity = pqsAuditEntryService.get(id);
        return new ResultVO().ok(auditEntryEntity, msg);
    }

    /**
     * 整车-根据vin码或者条码获取订单
     *
     * @param code
     * @param category
     * @return
     */
    @Override
    public OrderVo getOrderByVin(String code, Integer category) {
        if (StringUtils.isEmpty(code)) {
            throw new InkelinkException("条码为空");
        }
        OrderVo vo;
        // 整车
        if (category == 1) {
            List<ConditionDto> dtos = new ArrayList<>();
            dtos.add(new ConditionDto("sn", code, ConditionOper.Equal, ConditionRelation.Or));
            dtos.add(new ConditionDto("barcode", code, ConditionOper.Equal, ConditionRelation.Or));
            PpsOrderEntity ppsOrderEntity = ppsOrderService.getData(dtos).getData().stream().findFirst().orElse(null);
            if (ppsOrderEntity == null) {
                throw new InkelinkException("未查到车辆订单数据");
            }
            vo = new OrderVo();
            vo.setBarcode(ppsOrderEntity.getBarcode());
            vo.setVin(ppsOrderEntity.getSn());
            vo.setModelCode(ppsOrderEntity.getModel());
            vo.setManufactureDt(ppsOrderEntity.getActualStartDt());
            vo.setCharacteristic2(ppsOrderEntity.getCharacteristic2());
        } else {
            PpsPlanPartsEntity planPartsEntity = ppsPlanPartsProvider.getPlanPastsByPlanNo(code);
            if (planPartsEntity == null) {
                throw new InkelinkException("未查到离散计划单数据");
            }
            vo = new OrderVo();
            vo.setBarcode(planPartsEntity.getPlanNo());
            vo.setModelCode(planPartsEntity.getModel());
            vo.setManufactureDt(planPartsEntity.getActualStartDt());
        }
        return vo;
    }

    @Override
    public ResultVO submitEntry(String recordNo, String remark) {
        //查询缺陷总数
        Long count = pqsAuditEntryDefectAnomalyService.getAllDatas().stream()
                .filter(c -> c.getAuditRecordNo().equals(recordNo)).map(PqsAuditEntryDefectAnomalyEntity::getDefectAnomalyCode).count();
        //查询总扣分数
        int sum = pqsAuditEntryDefectAnomalyService.getAllDatas().stream()
                .filter(c -> c.getAuditRecordNo().equals(recordNo)).mapToInt(PqsAuditEntryDefectAnomalyEntity::getScore).sum();
        UpdateWrapper<PqsAuditEntryEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(PqsAuditEntryEntity::getStatus, 90)
                .eq(PqsAuditEntryEntity::getRecordNo, recordNo).eq(PqsAuditEntryEntity::getStatus, 2)
                .set(PqsAuditEntryEntity::getDefectCount, count).set(PqsAuditEntryEntity::getTotalScore, sum)
                .set(PqsAuditEntryEntity::getRemark, remark);
        pqsAuditEntryService.update(updateWrapper);
        pqsAuditEntryService.saveChange();
        return new ResultVO().ok("", "提交成功");
    }

    @Override
    public void modifyDefectAnomalyRepsponsibelInfo(ModifyDefectResponsibleInfo info) {
        List<String> recordNos = pqsAuditEntryDefectAnomalyService.getAllDatas().stream().filter(c -> info.getDataIds().contains(c.getId())).map(PqsAuditEntryDefectAnomalyEntity::getAuditRecordNo).collect(Collectors.toList());
        List<Integer> status = pqsAuditEntryService.getAllDatas().stream().filter(x -> recordNos.contains(x.getRecordNo())).map(PqsAuditEntryEntity::getStatus).collect(Collectors.toList());
        if (status.contains(90)) {
            throw new InkelinkException("已完成的工单不允许操作");
        }
        ComboInfoDTO gradeInfo = pqsAuditGradeService.getAllDatas().stream().filter(w -> w.getGradeCode().equals(info.getGradeCode())).map(d -> {
            ComboInfoDTO dto = new ComboInfoDTO();
            dto.setText(d.getGradeName());
            dto.setValue(d.getGradeCode());
            return dto;
        }).findFirst().orElse(null);
        ComboInfoDTO deptInfo = pqsAuditDeptService.getAllDatas().stream().filter(w -> w.getDeptCode().equals(info.getResponsibleDeptCode())).map(d -> {
            ComboInfoDTO dto = new ComboInfoDTO();
            dto.setText(d.getDeptName());
            dto.setValue(d.getDeptCode());
            return dto;
        }).findFirst().orElse(null);
        if (gradeInfo != null && deptInfo != null) {
            info.getDataIds().stream().forEach(q -> {
                UpdateWrapper<PqsAuditEntryDefectAnomalyEntity> wrapper = new UpdateWrapper<>();
                wrapper.lambda().set(PqsAuditEntryDefectAnomalyEntity::getResponsibleDeptCode, info.getResponsibleDeptCode()).set(PqsAuditEntryDefectAnomalyEntity::getResponsibleDeptName, deptInfo.getText())
                        .set(PqsAuditEntryDefectAnomalyEntity::getGradeName, gradeInfo.getText()).set(PqsAuditEntryDefectAnomalyEntity::getGradeCode, info.getGradeCode()).set(PqsAuditEntryDefectAnomalyEntity::getResponsibleTeamNo, info.getResponsibleTeamNo())
                        .eq(PqsAuditEntryDefectAnomalyEntity::getId, q);
                pqsAuditEntryDefectAnomalyService.update(wrapper);
            });
            pqsAuditEntryDefectAnomalyService.saveChange();
        }
    }


    @Override
    public void modifyDefectAnomalyStatus(AuditAnomalyStatusModifyRequest anomalyStatusModifyRequest) {
        PqsAuditEntryDefectAnomalyEntity pqsAuditEntryDefectAnomalyEntity = pqsAuditEntryDefectAnomalyService.get(anomalyStatusModifyRequest.getDataId());
        if (pqsAuditEntryDefectAnomalyEntity == null) {
            throw new InkelinkException("未找到对应的激活缺陷");
        }
        Integer recordNo = pqsAuditEntryService.getAllDatas().stream().filter(c -> c.getRecordNo().equals(pqsAuditEntryDefectAnomalyEntity.getAuditRecordNo())).map(PqsAuditEntryEntity::getStatus).findFirst().orElse(null);
        if (recordNo != null && recordNo == 90) {
            throw new InkelinkException("已完成的工单不允许操作");
        }
        pqsAuditEntryDefectAnomalyEntity.setStatus(anomalyStatusModifyRequest.getStatus());
        // 激活
        if (anomalyStatusModifyRequest.getStatus() == PqsLogicStatusEnum.ACTIVATED.code()) {
            pqsAuditEntryDefectAnomalyEntity.setActivateUser(identityHelper.getUserName());
            pqsAuditEntryDefectAnomalyEntity.setActivateTime(new Date());
        }
        // 修复
        if (anomalyStatusModifyRequest.getStatus() == PqsLogicStatusEnum.RECOVERED.code()) {
            if (anomalyStatusModifyRequest.getRepairActivity() == null) {
                throw new InkelinkException("请填写修复内容");
            }
            pqsAuditEntryDefectAnomalyEntity.setRepairTime(anomalyStatusModifyRequest.getRepairActivity().getRepairTime());
            pqsAuditEntryDefectAnomalyEntity.setRepairUser(identityHelper.getUserName());
            pqsAuditEntryDefectAnomalyEntity.setRepairWay(anomalyStatusModifyRequest.getRepairActivity().getRepairWay());
            pqsAuditEntryDefectAnomalyEntity.setRepairSpendTime(anomalyStatusModifyRequest.getRepairActivity().getSpendTime());
            pqsAuditEntryDefectAnomalyEntity.setRepairRemark(anomalyStatusModifyRequest.getRepairActivity().getRepairRemark());

        }
        //未发现
        if (anomalyStatusModifyRequest.getStatus() == PqsLogicStatusEnum.NOT_FOUND.code()) {
            if (anomalyStatusModifyRequest.getRepairActivity() == null) {
                throw new InkelinkException("请填写修复内容");
            }
            pqsAuditEntryDefectAnomalyEntity.setRepairTime(new Date());
            pqsAuditEntryDefectAnomalyEntity.setRepairUser(identityHelper.getUserName());
            pqsAuditEntryDefectAnomalyEntity.setRepairWay(Strings.EMPTY);
            pqsAuditEntryDefectAnomalyEntity.setRepairSpendTime(BigDecimal.valueOf(0));
            pqsAuditEntryDefectAnomalyEntity.setRepairRemark(Strings.EMPTY);

        }
        // 复查
        if (anomalyStatusModifyRequest.getStatus() == PqsLogicStatusEnum.QUALIFIED.code()
                || anomalyStatusModifyRequest.getStatus() == PqsLogicStatusEnum.DISQUALIFICATION.code()) {
            pqsAuditEntryDefectAnomalyEntity.setRecheckTime(new Date());
            pqsAuditEntryDefectAnomalyEntity.setRecheckUser(identityHelper.getUserName());
//            info.setRecheckUserId(identityHelper.getUserId());
        }
        pqsAuditEntryDefectAnomalyService.update(pqsAuditEntryDefectAnomalyEntity);
//        pqsAuditEntryDefectAnomalyService.saveChange();
    }

    @Override
    public List<ComboInfoDTO> getQualityMatrikByWorkstationCode(String workstationCode, String model) {
        return auditLogicMapper.getQualityMatrikByWorkstationCode(workstationCode, model);
    }

    @Override
    public ShowQgMatrikDto showQgMatrikDataByRecordNo(Long qualityMatrikId, String recordNo) {
        ShowQgMatrikDto result = new ShowQgMatrikDto();
        // 获取QG基础信息
        AuditQualityMatrikEntity matrikEntity = auditQualityMatrikService.get(qualityMatrikId);
        BeanUtils.copyProperties(matrikEntity, result);

        List<DefectAnomalyDto> dataList = auditLogicMapper.getShowQGMatrikDataByRecordNo(qualityMatrikId, recordNo);
        if (CollectionUtil.isNotEmpty(dataList)) {
            result.setDefectAnomalyList(dataConversion(dataList, recordNo));
        }

        return result;
    }

    @Override
    public List<ComboInfoDTO> getQualityGateByWorkstationCode(String workstationCode, String model) {
        return auditLogicMapper.getQualityGateByWorkstationCode(workstationCode, model);
    }

    @Override
    public List<DefectAnomalyDto> getGateAnomalyByGateBlankIdAndRecordNo(Long qualityGateBlankId, String recordNo) {
        List<DefectAnomalyDto> resultList = Lists.newArrayList();
        List<DefectAnomalyDto> dataList = auditLogicMapper.getGateAnomalyByGateBlankIdAndRecordNo(qualityGateBlankId, recordNo);
        if (CollectionUtil.isNotEmpty(dataList)) {
            resultList = dataConversion(dataList, recordNo);
        }
        return resultList;
    }

    private List<DefectAnomalyDto> dataConversion(List<DefectAnomalyDto> lists, String recordNo) {
        QueryWrapper<PqsAuditEntryDefectAnomalyEntity> anomalyEntityQueryWrapper = new QueryWrapper<>();
        anomalyEntityQueryWrapper.lambda().ne(PqsAuditEntryDefectAnomalyEntity::getStatus, 4)
                .eq(PqsAuditEntryDefectAnomalyEntity::getAuditRecordNo, recordNo);
        List<PqsAuditEntryDefectAnomalyEntity> anomalyEntityList = pqsAuditEntryDefectAnomalyService.getData(anomalyEntityQueryWrapper, false);
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
     * 获取QG配置数据信息
     *
     * @param qualityGateId
     * @return
     */
    @Override
    public QgGateConfigurationInfo getQGGateData(Long qualityGateId) {

        // 获取QG基础信息
        QgGateConfigurationInfo result = new QgGateConfigurationInfo();
        AuditQualityGateEntity gateEntity = auditQualityGateService.get(qualityGateId);
        if (gateEntity != null) {
            BeanUtils.copyProperties(gateEntity, result);

            QueryWrapper<AuditQualityGateWorkstationEntity> workstationEntityQueryWrapper = new QueryWrapper();
            workstationEntityQueryWrapper.lambda().eq(AuditQualityGateWorkstationEntity::getPrcAuditQualityGateId, qualityGateId);
            String workstationCodes = auditQualityGateWorkstationService.getData(workstationEntityQueryWrapper, false)
                    .stream().map(AuditQualityGateWorkstationEntity::getWorkstationCode)
                    .collect(Collectors.joining(","));
            result.setWorkstationCodes(workstationCodes);

            QueryWrapper<AuditQualityGateBlankEntity> blankEntityQueryWrapper = new QueryWrapper();
            blankEntityQueryWrapper.lambda().eq(AuditQualityGateBlankEntity::getPrcAuditQualityGateId, qualityGateId);
            List<AuditQualityGateBlankEntity> blankEntityList = auditQualityGateBlankService.getData(blankEntityQueryWrapper, false);
            result.setGateBlanks(blankEntityList);

            QueryWrapper<AuditQualityGateTcEntity> tcEntityQueryWrapper = new QueryWrapper();
            tcEntityQueryWrapper.lambda().eq(AuditQualityGateTcEntity::getPrcAuditQualityGateId, qualityGateId);
            String modelCodes = auditQualityGateTcService.getData(tcEntityQueryWrapper, false)
                    .stream().map(AuditQualityGateTcEntity::getModel).collect(Collectors.joining(","));
            result.setModelCodes(modelCodes);

            // 同时返回缺陷数据
            blankEntityList.forEach(b -> {
                List<ConditionDto> conditions = new ArrayList<>();
                conditions.add(new ConditionDto("prcAuditQualityGateBlankId", b.getId().toString(), ConditionOper.Equal));
                List<AuditQualityGateAnomalyEntity> anomalyEntityList = auditQualityGateAnomalyService.getData(conditions);
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
        List<AuditQualityMatrikWorkstationEntity> pqmweList = Lists.newArrayList();
        workstationCodeList.forEach(w -> {
            workstationEntityList.forEach(wse -> {
                if (w.equals(wse.getWorkstationCode())) {
                    AuditQualityMatrikWorkstationEntity pqmwEntity = new AuditQualityMatrikWorkstationEntity();
                    pqmwEntity.setPrcAuditQualityMatrikId(info.getId() == null ? Constant.DEFAULT_ID : info.getId());
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
        List<AuditQualityMatrikTcEntity> tcEntityList = Lists.newArrayList();
        modelCodeList.forEach(m -> {
            defectLevels.forEach(d -> {
                if (m.equals(d.getValue())) {
                    AuditQualityMatrikTcEntity matrikTcEntity = new AuditQualityMatrikTcEntity();
                    matrikTcEntity.setModel(d.getValue());
                    matrikTcEntity.setModelName(d.getText());
                    tcEntityList.add(matrikTcEntity);
                }
            });
        });

        AuditQualityMatrikEntity matrikEntity = new AuditQualityMatrikEntity();
        BeanUtils.copyProperties(info, matrikEntity);
        matrikEntity.setModels(tcEntityList.stream().map(AuditQualityMatrikTcEntity::getModelName)
                .collect(Collectors.joining(",")));
        matrikEntity.setWorkstationNames(pqmweList.stream().map(AuditQualityMatrikWorkstationEntity::getWorkstationName)
                .collect(Collectors.joining(",")));

        // 新增新数据
        if (info.getId() == null) {
            matrikEntity.setId(IdGenerator.getId());
            auditQualityMatrikService.insert(matrikEntity);
        } else {
            // 修改原有数据
            auditQualityMatrikService.update(matrikEntity);
        }

        // 添加车型
        submitQualityMatrikTc(tcEntityList, matrikEntity.getId());
        // 添加工位
        submitQualityMatrikWorkstation(pqmweList, matrikEntity.getId());
        // 添加缺陷
        submitQualityMatrikAnomaly(info.getDefectAnomalyList(), matrikEntity.getId());
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
        AuditQualityMatrikEntity matrikEntity = auditQualityMatrikService.get(qualityMatrikId);
        if (matrikEntity != null) {
            BeanUtils.copyProperties(matrikEntity, result);

            QueryWrapper<AuditQualityMatrikWorkstationEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(AuditQualityMatrikWorkstationEntity::getPrcAuditQualityMatrikId, qualityMatrikId);
            String workstationCodes = auditQualityMatrikWorkstationService.getData(queryWrapper, false)
                    .stream().map(AuditQualityMatrikWorkstationEntity::getWorkstationCode).collect(Collectors.joining(","));

            QueryWrapper<AuditQualityMatrikTcEntity> tcEntityQueryWrapper = new QueryWrapper<>();
            tcEntityQueryWrapper.lambda().eq(AuditQualityMatrikTcEntity::getPrcAuditQualityMatrikId, qualityMatrikId);
            String models = auditQualityMatrikTcService.getData(tcEntityQueryWrapper, false)
                    .stream().map(AuditQualityMatrikTcEntity::getModel).collect(Collectors.joining(","));

            List<PqsAuditDefectAnomalyEntity> anomalyEntityList = pqsAuditDefectAnomalyService.getAllDatas();

            QueryWrapper<AuditQualityMatrikAnomalyEntity> anomalyEntityQueryWrapper = new QueryWrapper<>();
            anomalyEntityQueryWrapper.lambda().eq(AuditQualityMatrikAnomalyEntity::getPrcAuditQualityMatrikId, qualityMatrikId);
            List<AuditQualityMatrikAnomalyEntity> matrikAnomalyEntityList = auditQualityMatrikAnomalyService.getData(anomalyEntityQueryWrapper, false);

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
     * 添加百格图车型
     *
     * @param lists
     * @param pqsQualityMatrikId
     */
    private void submitQualityMatrikTc(List<AuditQualityMatrikTcEntity> lists, Long pqsQualityMatrikId) {
        UpdateWrapper<AuditQualityMatrikTcEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(AuditQualityMatrikTcEntity::getPrcAuditQualityMatrikId, pqsQualityMatrikId);
        // 删除现有车型
        auditQualityMatrikTcService.delete(wrapper);

        // 写入车型
        lists.forEach(p -> {
            p.setPrcAuditQualityMatrikId(pqsQualityMatrikId);
        });
        auditQualityMatrikTcService.insertBatch(lists);
    }

    /**
     * 添加QG配置工位
     *
     * @param lists
     * @param pqsQualityMatrikId
     */
    private void submitQualityMatrikWorkstation(List<AuditQualityMatrikWorkstationEntity> lists, Long pqsQualityMatrikId) {

        UpdateWrapper<AuditQualityMatrikWorkstationEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(AuditQualityMatrikWorkstationEntity::getPrcAuditQualityMatrikId, pqsQualityMatrikId);
        // 删除现有工位
        auditQualityMatrikWorkstationService.delete(updateWrapper);

        // 写入工位
        lists.forEach(p -> {
            p.setPrcAuditQualityMatrikId(pqsQualityMatrikId);
        });
        auditQualityMatrikWorkstationService.insertBatch(lists, lists.size());
    }

    /**
     * 添加百格图缺陷
     *
     * @param lists
     * @param pqsQualityMatrikId
     */
    private void submitQualityMatrikAnomaly(List<DefectAnomalyDto> lists, Long pqsQualityMatrikId) {

        List<PqsAuditDefectAnomalyEntity> anomalyEntityList = pqsAuditDefectAnomalyService.getAllDatas();

        // 删除所有色块
        UpdateWrapper<AuditQualityMatrikAnomalyEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(AuditQualityMatrikAnomalyEntity::getPrcAuditQualityMatrikId, pqsQualityMatrikId);
        auditQualityMatrikAnomalyService.delete(updateWrapper);

        // 写入百格图缺陷
        List<AuditQualityMatrikAnomalyEntity> insertLists = Lists.newArrayList();
        lists.forEach(l -> {
            anomalyEntityList.forEach(a -> {
                if (l.getDefectAnomalyCode().equals(a.getDefectAnomalyCode())) {
                    AuditQualityMatrikAnomalyEntity entity = new AuditQualityMatrikAnomalyEntity();
                    entity.setDefectAnomalyCode(l.getDefectAnomalyCode());
                    entity.setDefectAnomalyDescription(a.getDefectAnomalyDescription());
                    entity.setPrcAuditQualityMatrikId(pqsQualityMatrikId);
                    entity.setShortCode(a.getShortCode());
                    insertLists.add(entity);
                }
            });
        });
        auditQualityMatrikAnomalyService.insertBatch(insertLists, lists.size());
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
        List<AuditQualityGateWorkstationEntity> pqgweList = Lists.newArrayList();
        workstationCodeList.forEach(w -> {
            workstationEntityList.forEach(wse -> {
                if (w.equals(wse.getWorkstationCode())) {
                    AuditQualityGateWorkstationEntity pqgweEntity = new AuditQualityGateWorkstationEntity();
                    pqgweEntity.setPrcAuditQualityGateId(info.getId());
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
        List<AuditQualityGateTcEntity> gateTcEntityList = Lists.newArrayList();
        modelCodeList.forEach(m -> {
            defectLevels.forEach(d -> {
                if (m.equals(d.getValue())) {
                    AuditQualityGateTcEntity gateTcEntity = new AuditQualityGateTcEntity();
                    gateTcEntity.setPrcAuditQualityGateId(info.getId());
                    gateTcEntity.setModel(d.getValue());
                    gateTcEntity.setModelName(d.getText());
                    gateTcEntityList.add(gateTcEntity);
                }
            });
        });

        AuditQualityGateEntity gateEntity = new AuditQualityGateEntity();
        BeanUtils.copyProperties(info, gateEntity);
        gateEntity.setWorkstationNames(pqgweList.stream().map(AuditQualityGateWorkstationEntity::getWorkstationName)
                .collect(Collectors.joining(",")));
        gateEntity.setModels(gateTcEntityList.stream().map(AuditQualityGateTcEntity::getModelName)
                .collect(Collectors.joining(",")));

        // 新增新数据
        if (gateEntity.getId() == null) {
            gateEntity.setId(IdGenerator.getId());
            auditQualityGateService.insert(gateEntity);
        } else {
            // 修改原有数据
            auditQualityGateService.update(gateEntity);
        }

        // 车型
        submitQGAnomalyDataTc(gateTcEntityList, gateEntity.getId());
        // 工位
        submitQGAnomalyDataWorkstation(pqgweList, gateEntity.getId());
        // 添加QG配置对应的色块数据
        submitQGAnomalyDataWorkPlaceGateBlanks(info.getGateBlanks(), gateEntity.getId());
    }

    /**
     * 添加车型
     *
     * @param lists
     * @param pqsQualityGateId
     */
    private void submitQGAnomalyDataTc(List<AuditQualityGateTcEntity> lists, Long pqsQualityGateId) {

        // 删除现有车型
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("prcAuditQualityGateId", pqsQualityGateId.toString(), ConditionOper.Equal));
        auditQualityGateTcService.delete(conditions);

        // 写入车型
        lists.forEach(p -> {
            p.setPrcAuditQualityGateId(pqsQualityGateId);
        });
        auditQualityGateTcService.insertBatch(lists);
    }

    /**
     * 添加工位
     *
     * @param lists
     * @param pqsQualityGateId
     */
    private void submitQGAnomalyDataWorkstation(List<AuditQualityGateWorkstationEntity> lists, Long pqsQualityGateId) {

        // 删除现有工位
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("prcAuditQualityGateId", pqsQualityGateId.toString(), ConditionOper.Equal));
        auditQualityGateWorkstationService.delete(conditions);

        // 写入工位
        lists.forEach(p -> {
            p.setPrcAuditQualityGateId(pqsQualityGateId);
        });
        auditQualityGateWorkstationService.insertBatch(lists);
    }

    /***
     * 添加QG配置对应的色块数据
     *
     * @param lists
     * @param pqsQualityGateId
     */
    private void submitQGAnomalyDataWorkPlaceGateBlanks(List<AuditQualityGateBlankEntity> lists, Long pqsQualityGateId) {

        List<AuditQualityGateAnomalyEntity> gateAnomalyEntityList = Lists.newArrayList();
        List<PqsAuditDefectAnomalyEntity> defectAnomalyEntityList = pqsAuditDefectAnomalyService.getAllDatas();

        // 当前色块
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("prcAuditQualityGateId", pqsQualityGateId.toString(), ConditionOper.Equal));
        List<AuditQualityGateBlankEntity> blankEntityList = auditQualityGateBlankService.getData(conditions);
        if (CollectionUtil.isNotEmpty(blankEntityList)) {
            conditions.clear();
            List<String> defectAnomalyCodes = blankEntityList.stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
            conditions.clear();
            conditions.add(new ConditionDto("prcAuditQualityGateBlankId", String.join("|", defectAnomalyCodes), ConditionOper.In));
            auditQualityGateAnomalyService.delete(conditions);
        }
        // 删除色块
        conditions.clear();
        conditions.add(new ConditionDto("prcAuditQualityGateId", pqsQualityGateId.toString(), ConditionOper.Equal));
        auditQualityGateBlankService.delete(conditions);

        // 添加色块
        lists.forEach(l -> {
            l.setId(IdGenerator.getId());
            l.setPrcAuditQualityGateId(pqsQualityGateId);
            defectAnomalyEntityList.forEach(d -> {
                if (CollectionUtil.isNotEmpty(l.getAnomalyCodes())) {
                    l.getAnomalyCodes().forEach(a -> {
                        if (a.getDefectAnomalyCode().equals(d.getDefectAnomalyCode())) {
                            AuditQualityGateAnomalyEntity gateAnomalyEntity = new AuditQualityGateAnomalyEntity();
                            gateAnomalyEntity.setDefectAnomalyCode(d.getDefectAnomalyCode());
                            gateAnomalyEntity.setDefectAnomalyDescription(d.getDefectAnomalyDescription());
                            gateAnomalyEntity.setPrcAuditQualityGateBlankId(l.getId());
                            gateAnomalyEntity.setShortCode(d.getShortCode());
                            gateAnomalyEntityList.add(gateAnomalyEntity);
                        }
                    });
                }
            });
        });
        auditQualityGateBlankService.insertBatch(lists, lists.size());
        auditQualityGateAnomalyService.insertBatch(gateAnomalyEntityList, gateAnomalyEntityList.size());
    }
}
