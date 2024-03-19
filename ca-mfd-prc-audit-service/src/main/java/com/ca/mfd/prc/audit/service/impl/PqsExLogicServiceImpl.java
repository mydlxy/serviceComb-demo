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
import com.ca.mfd.prc.audit.dto.ExQgGateConfigurationInfo;
import com.ca.mfd.prc.audit.dto.GetAuditDefectAnomalyRequest;
import com.ca.mfd.prc.audit.dto.ModifyDefectResponsibleInfo;
import com.ca.mfd.prc.audit.dto.OrderVo;
import com.ca.mfd.prc.audit.dto.QgMatrikConfigurationInfo;
import com.ca.mfd.prc.audit.dto.ShowQgMatrikDto;
import com.ca.mfd.prc.audit.entity.PqsExEntryAttchmentEntity;
import com.ca.mfd.prc.audit.entity.PqsExEntryDefectAnomalyEntity;
import com.ca.mfd.prc.audit.entity.PqsExDefectAnomalyEntity;
import com.ca.mfd.prc.audit.entity.PqsExDeptEntity;
import com.ca.mfd.prc.audit.entity.PqsExEntryEntity;
import com.ca.mfd.prc.audit.entity.PqsExGradeEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityGateAnomalyEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityGateBlankEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityGateEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityGateTcEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityGateWorkstationEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikAnomalyEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikTcEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikWorkstationEntity;
import com.ca.mfd.prc.audit.enums.PqsLogicStatusEnum;
import com.ca.mfd.prc.audit.mapper.IPqsExEntryDefectAnomalyMapper;
import com.ca.mfd.prc.audit.mapper.IPqsExLogicMapper;
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
import com.ca.mfd.prc.audit.service.IPqsExDefectAnomalyService;
import com.ca.mfd.prc.audit.service.IPqsExDeptService;
import com.ca.mfd.prc.audit.service.IPqsExEntryAttchmentService;
import com.ca.mfd.prc.audit.service.IPqsExEntryDefectAnomalyService;
import com.ca.mfd.prc.audit.service.IPqsExEntryService;
import com.ca.mfd.prc.audit.service.IPqsExGradeService;
import com.ca.mfd.prc.audit.service.IPqsExLogicService;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateAnomalyService;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateBlankService;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateService;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateTcService;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateWorkstationService;
import com.ca.mfd.prc.audit.service.IPqsExQualityMatrikAnomalyService;
import com.ca.mfd.prc.audit.service.IPqsExQualityMatrikService;
import com.ca.mfd.prc.audit.service.IPqsExQualityMatrikTcService;
import com.ca.mfd.prc.audit.service.IPqsExQualityMatrikWorkstationService;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.exception.InkelinkException;
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
public class PqsExLogicServiceImpl implements IPqsExLogicService {


    @Autowired
    private IUnitOfWorkService unitOfWorkService;

    @Autowired
    private IPqsExDefectAnomalyService pqsExDefectAnomalyService;

    @Autowired
    private IPqsExEntryDefectAnomalyService pqsExEntryDefectAnomalyService;
    @Resource
    private IPqsExEntryDefectAnomalyMapper pqsExEntryDefectAnomalyMapper;

    @Autowired
    private IPqsExEntryAttchmentService pqsExEntryAttchmentService;

    @Autowired
    private IPqsExDeptService pqsExDeptService;

    @Autowired
    private IPqsExGradeService pqsExGradeService;

    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private IdentityHelper identityHelper;

    @Autowired
    private IPqsExEntryService pqsExEntryService;
    @Autowired
    private IPpsOrderService ppsOrderService;
    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;
    @Autowired
    private PpsPlanPartsProvider ppsPlanPartsProvider;

    @Autowired
    private IPmShcCalendarService pmShcCalendarProvider;

    @Resource
    private IPqsExLogicMapper exLogicMapper;

    @Autowired
    private IPqsExQualityMatrikService pqsExQualityMatrikService;

    @Autowired
    private IPqsExQualityGateService pqsExQualityGateService;
    @Autowired
    private IPqsExQualityGateWorkstationService pqsExQualityGateWorkstationService;
    @Autowired
    private IPqsExQualityGateBlankService pqsExQualityGateBlankService;
    @Autowired
    private IPqsExQualityGateTcService pqsExQualityGateTcService;
    @Autowired
    private IPqsExQualityGateAnomalyService pqsExQualityGateAnomalyService;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private IPqsExQualityMatrikTcService pqsExQualityMatrikTcService;
    @Autowired
    private IPqsExQualityMatrikAnomalyService pqsExQualityMatrikAnomalyService;
    @Autowired
    private IPqsExQualityMatrikWorkstationService pqsExQualityMatrikWorkstationService;


    @Override
    public void saveChange() {
        unitOfWorkService.saveChange();
    }

    @Override
    public PageData<DefectAnomalyDto> getExAnomalyShowList(DefectAnomalyParaInfo info) {
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

        IPage<PqsExDefectAnomalyEntity> page = pqsExDefectAnomalyService.getDataByPage(model);

        PageData<DefectAnomalyDto> result_ = new PageData<>();
        List<DefectAnomalyDto> anomalyInfoList = Lists.newArrayList();
        page.getRecords().forEach(p -> {
            DefectAnomalyDto anomalyInfo = new DefectAnomalyDto();
            BeanUtils.copyProperties(p, anomalyInfo);
            PqsExGradeEntity pqsExGradeEntity = pqsExGradeService.getAllDatas().stream()
                    .filter(g -> StringUtils.equals(g.getGradeCode(), p.getGradeCode()))
                    .findFirst().orElse(null);
            anomalyInfo.setScore(pqsExGradeEntity != null ? pqsExGradeEntity.getScore() : 0);
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
        Page<AuditDefectAnomalyReponse> list = pqsExEntryDefectAnomalyMapper.getVehicleDefectAnomalyList(page, request);
        pageData.setDatas(list.getRecords());
        pageData.setTotal((int) list.getTotal());
        return new ResultVO().ok(pageData, "获取成功");

    }

    @Override
    public ResultVO<List<PqsExEntryAttchmentEntity>> getAttchmentbyRecordNo(String recordNo) {
        if (StringUtils.isEmpty(recordNo)) {
            throw new InkelinkException("工单号不能为空");
        }
        QueryWrapper<PqsExEntryAttchmentEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PqsExEntryAttchmentEntity::getAuditRecordNo, recordNo);
        return new ResultVO<List<PqsExEntryAttchmentEntity>>().ok(pqsExEntryAttchmentService.getData(queryWrapper, false), "获取成功");
    }

    @Override
    public void saveAttachMent(PqsExEntryAttchmentEntity attchmentEntity) {
        Integer recordNo = pqsExEntryService.getAllDatas().stream().filter(c -> c.getRecordNo().equals(attchmentEntity.getAuditRecordNo())).map(PqsExEntryEntity::getStatus).findFirst().orElse(null);
        if (recordNo != null && recordNo == 90) {
            throw new InkelinkException("已完成的工单不允许操作");
        }
        if (attchmentEntity.getId() == null || attchmentEntity.getId() <= 0) {
            pqsExEntryAttchmentService.save(attchmentEntity);
        } else {
            pqsExEntryAttchmentService.updateById(attchmentEntity);
        }
        pqsExEntryAttchmentService.saveChange();
    }


    @Override
    public void del(Long id) {
        PqsExEntryAttchmentEntity pqsAuditEntryAttchmentEntity = pqsExEntryAttchmentService.get(id);
        Integer recordNo = pqsExEntryService.getAllDatas().stream().filter(c -> c.getRecordNo().equals(pqsAuditEntryAttchmentEntity.getAuditRecordNo())).map(PqsExEntryEntity::getStatus).findFirst().orElse(null);
        if (recordNo != null && recordNo == 90) {
            throw new InkelinkException("已完成的工单不允许操作");
        }
        pqsExEntryAttchmentService.delete(id);
        pqsExEntryAttchmentService.saveChange();

    }

    @Override
    public void activeAuditAnomaly(AuditActiveAnomalyInfo info) {
        // 获取配置数据
        List<PqsExGradeEntity> gradeEntityList = pqsExGradeService.getAllDatas();
        List<PqsExDeptEntity> deptEntityList = pqsExDeptService.getAllDatas();

        String vin = "";
        String workstationCode = "";
        PqsExEntryEntity exEntryEntity = pqsExEntryService.getAllDatas().stream().filter(x -> x.getRecordNo().equals(info.getRecordNo())).findFirst().orElse(null);
        if (exEntryEntity != null) {
            if (90 == exEntryEntity.getStatus()) {
                throw new InkelinkException("已完成的工单不能操作");
            }
            vin = exEntryEntity.getVin();
            workstationCode = exEntryEntity.getWorkstationCode();
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
            PqsExEntryDefectAnomalyEntity vAnomaly = new PqsExEntryDefectAnomalyEntity();

            if (StringUtils.isNotEmpty(a.getDefectAnomalyCode())) {
                // 如果不等于自定义缺陷
                // 判断缺陷是否已被激活，已被激活的缺陷未关闭将不会改变他的状态
                QueryWrapper<PqsExEntryDefectAnomalyEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(PqsExEntryDefectAnomalyEntity::getDefectAnomalyCode, a.getDefectAnomalyCode())
                        .eq(PqsExEntryDefectAnomalyEntity::getSn, vin)
                        .eq(PqsExEntryDefectAnomalyEntity::getAuditRecordNo, exEntryEntity.getRecordNo())
                        .ne(PqsExEntryDefectAnomalyEntity::getStatus, 4);
                vAnomaly = pqsExEntryDefectAnomalyService.getData(queryWrapper, false)
                        .stream().findFirst().orElse(null);
                // 缺陷已被激活
                if (vAnomaly != null) {
                    continue;
                }
            }
            if (vAnomaly == null) {
                vAnomaly = new PqsExEntryDefectAnomalyEntity();
            }

            QueryWrapper<PqsExEntryDefectAnomalyEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsExEntryDefectAnomalyEntity::getDefectAnomalyCode, a.getDefectAnomalyCode());
            PqsExEntryDefectAnomalyEntity anomalyConfig = pqsExEntryDefectAnomalyService.getData(queryWrapper, false)
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
            // 插入数据
            pqsExEntryDefectAnomalyService.insert(vAnomaly);
            pqsExEntryDefectAnomalyService.saveChange();
        }
    }

    @Override
    public ResultVO getEntryByStatus(EntryParaInfo key) {
        if (Objects.isNull(key)) {
            throw new InkelinkException("传入的数据不存在");
        }
        PageData<PqsExEntryEntity> pageData = new PageData<>();
        pageData.setPageSize(key.getPageSize());
        pageData.setPageIndex(key.getPageIndex());

        if (StringUtils.isEmpty(key.getKey())) {
            QueryWrapper<PqsExEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsExEntryEntity::getWorkstationCode, key.getWorkstationCode())
                    .eq(PqsExEntryEntity::getCategory, key.getCategory())
                    .in(PqsExEntryEntity::getStatus, Arrays.asList(key.getStatus().split(",")))
                    .orderBy(true, false, PqsExEntryEntity::getCreationDate);
            Page<PqsExEntryEntity> query = (Page<PqsExEntryEntity>) pqsExEntryService.getDataByPage(queryWrapper, key.getPageIndex(), key.getPageSize());
            pageData.setDatas(query.getRecords());
            pageData.setTotal((int) query.getTotal());

            return new ResultVO().ok(pageData, "获取数据成功");
        }
        PqsExEntryEntity data = pqsExEntryService.getAllDatas().stream().filter(c -> {
            boolean keyFlag = c.getBarcode().equals(key.getKey()) || c.getVin().equals(key.getKey()) || c.getRecordNo().equals(key.getKey());
            if (keyFlag && c.getWorkstationCode().equals(key.getWorkstationCode())
                    && Arrays.asList(key.getStatus().split(",")).contains(String.valueOf(c.getStatus()))
                    && c.getCategory().equals(key.getCategory())) {
                return true;
            }
            return false;
        }).sorted(Comparator.comparing(PqsExEntryEntity::getCreationDate, Comparator.reverseOrder())).findFirst().orElse(null);
        if (data == null) {
            return new ResultVO().ok(pageData, "获取数据成功");
        }
        UpdateWrapper<PqsExEntryEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(PqsExEntryEntity::getStatus, 2).eq(PqsExEntryEntity::getStatus, 1).eq(PqsExEntryEntity::getIsDelete, false).eq(PqsExEntryEntity::getRecordNo, data.getRecordNo());
        pqsExEntryService.update(updateWrapper);
        pqsExEntryService.saveChange();
        List<PqsExEntryEntity> dataList = new ArrayList<>();
        dataList.add(pqsExEntryService.get(data.getId()));
        pageData.setDatas(dataList);
        pageData.setTotal(dataList.size());
        return new ResultVO().ok(pageData, "获取数据成功");

    }

    @Override
    public ResultVO getEntry(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new InkelinkException("传入的数据不存在");
        }
        PqsExEntryEntity data = pqsExEntryService.getAllDatas().stream().filter(c -> c.getBarcode().equals(key) || c.getVin().equals(key) || c.getRecordNo().equals(key)).sorted(Comparator.comparing(PqsExEntryEntity::getCreationDate, Comparator.reverseOrder())).findFirst().orElse(null);
        if (data == null) {
            throw new InkelinkException("未获取到数据");
        }
        UpdateWrapper<PqsExEntryEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(PqsExEntryEntity::getStatus, 2).eq(PqsExEntryEntity::getStatus, 1).eq(PqsExEntryEntity::getIsDelete, false).eq(PqsExEntryEntity::getRecordNo, data.getRecordNo());
        pqsExEntryService.update(updateWrapper);
        pqsExEntryService.saveChange();
        return new ResultVO().ok(pqsExEntryService.get(data.getId()), "获取数据成功");

    }

    /**
     * 创建精致工艺工单
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
        PqsExEntryEntity entry = new PqsExEntryEntity();
        //获取缺陷记录
        List<PqsExEntryDefectAnomalyEntity> pqsExEntryDefectAnomalyEntityList=pqsExEntryDefectAnomalyService.getAllDatas();
        //查询缺陷总数
        Long count = pqsExEntryDefectAnomalyEntityList.stream()
                .filter(c -> c.getAuditRecordNo().equals(dto.getRecordNo())).map(PqsExEntryDefectAnomalyEntity::getDefectAnomalyCode).count();
        dto.setDefectCount(count.intValue());
        //查询总扣分数
        int sum = pqsExEntryDefectAnomalyEntityList.stream()
                .filter(c -> c.getAuditRecordNo().equals(dto.getRecordNo())).mapToInt(PqsExEntryDefectAnomalyEntity::getScore).sum();
        dto.setTotalScore(sum);
        BeanUtils.copyProperties(dto, entry);
        long id = IdGenerator.getId();
        if (dto.getId() != null && !dto.getId().equals(Constant.DEFAULT_ID)) {
            entry.setStatus(dto.getStatus());
            pqsExEntryService.update(entry);
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
            pqsExEntryService.insert(entry);
            msg = "创建成功";
        }
        pqsExEntryService.saveChange();
        PqsExEntryEntity exEntryEntity = pqsExEntryService.get(id);
        return new ResultVO().ok(exEntryEntity, msg);
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
        //获取缺陷记录
        List<PqsExEntryDefectAnomalyEntity> pqsExEntryDefectAnomalyEntityList=pqsExEntryDefectAnomalyService.getAllDatas();
        //查询缺陷总数
        Long count = pqsExEntryDefectAnomalyEntityList.stream()
                .filter(c -> c.getAuditRecordNo().equals(recordNo)).map(PqsExEntryDefectAnomalyEntity::getDefectAnomalyCode).count();
        //查询总扣分数
        int sum = pqsExEntryDefectAnomalyEntityList.stream()
                .filter(c -> c.getAuditRecordNo().equals(recordNo)).mapToInt(PqsExEntryDefectAnomalyEntity::getScore).sum();
        UpdateWrapper<PqsExEntryEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(PqsExEntryEntity::getStatus, 90)
                .eq(PqsExEntryEntity::getRecordNo, recordNo).eq(PqsExEntryEntity::getStatus, 2)
                .set(PqsExEntryEntity::getDefectCount, count).set(PqsExEntryEntity::getTotalScore, sum)
                .set(PqsExEntryEntity::getRemark, remark);
        pqsExEntryService.update(updateWrapper);
        pqsExEntryService.saveChange();
        return new ResultVO().ok("", "提交成功");
    }

    @Override
    public void modifyDefectAnomalyRepsponsibelInfo(ModifyDefectResponsibleInfo info) {
        List<String> recordNos = pqsExEntryDefectAnomalyService.getAllDatas().stream().filter(c -> info.getDataIds().contains(c.getId())).map(PqsExEntryDefectAnomalyEntity::getAuditRecordNo).collect(Collectors.toList());
        List<Integer> status = pqsExEntryService.getAllDatas().stream().filter(x -> recordNos.contains(x.getRecordNo())).map(PqsExEntryEntity::getStatus).collect(Collectors.toList());
        if (status.contains(90)) {
            throw new InkelinkException("已完成的工单不允许操作");
        }
        ComboInfoDTO gradeInfo = pqsExGradeService.getAllDatas().stream().filter(w -> w.getGradeCode().equals(info.getGradeCode())).map(d -> {
            ComboInfoDTO dto = new ComboInfoDTO();
            dto.setText(d.getGradeName());
            dto.setValue(d.getGradeCode());
            return dto;
        }).findFirst().orElse(null);
        ComboInfoDTO deptInfo = pqsExDeptService.getAllDatas().stream().filter(w -> w.getDeptCode().equals(info.getResponsibleDeptCode())).map(d -> {
            ComboInfoDTO dto = new ComboInfoDTO();
            dto.setText(d.getDeptName());
            dto.setValue(d.getDeptCode());
            return dto;
        }).findFirst().orElse(null);
        if (gradeInfo != null && deptInfo != null) {
            info.getDataIds().stream().forEach(q -> {
                UpdateWrapper<PqsExEntryDefectAnomalyEntity> wrapper = new UpdateWrapper<>();
                wrapper.lambda().set(PqsExEntryDefectAnomalyEntity::getResponsibleDeptCode, info.getResponsibleDeptCode()).set(PqsExEntryDefectAnomalyEntity::getResponsibleDeptName, deptInfo.getText())
                        .set(PqsExEntryDefectAnomalyEntity::getGradeName, gradeInfo.getText()).set(PqsExEntryDefectAnomalyEntity::getGradeCode, info.getGradeCode()).set(PqsExEntryDefectAnomalyEntity::getResponsibleTeamNo, info.getResponsibleTeamNo())
                        .eq(PqsExEntryDefectAnomalyEntity::getId, q);
                pqsExEntryDefectAnomalyService.update(wrapper);
            });
            pqsExEntryDefectAnomalyService.saveChange();
        }
    }


    @Override
    public void modifyDefectAnomalyStatus(AuditAnomalyStatusModifyRequest anomalyStatusModifyRequest) {
        PqsExEntryDefectAnomalyEntity pqsAuditEntryDefectAnomalyEntity = pqsExEntryDefectAnomalyService.get(anomalyStatusModifyRequest.getDataId());
        if (pqsAuditEntryDefectAnomalyEntity == null) {
            throw new InkelinkException("未找到对应的激活缺陷");
        }
        Integer recordNo = pqsExEntryService.getAllDatas().stream().filter(c -> c.getRecordNo().equals(pqsAuditEntryDefectAnomalyEntity.getAuditRecordNo())).map(PqsExEntryEntity::getStatus).findFirst().orElse(null);
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
        pqsExEntryDefectAnomalyService.update(pqsAuditEntryDefectAnomalyEntity);
//        pqsAuditEntryDefectAnomalyService.saveChange();
    }

    @Override
    public List<ComboInfoDTO> getQualityMatrikByWorkstationCode(String workstationCode, String model) {
        return exLogicMapper.getQualityMatrikByWorkstationCode(workstationCode, model);
    }

    @Override
    public ShowQgMatrikDto showQgMatrikDataByRecordNo(Long qualityMatrikId, String recordNo) {
        ShowQgMatrikDto result = new ShowQgMatrikDto();
        // 获取QG基础信息
        PqsExQualityMatrikEntity matrikEntity = pqsExQualityMatrikService.get(qualityMatrikId);
        BeanUtils.copyProperties(matrikEntity, result);

        List<DefectAnomalyDto> dataList = exLogicMapper.getShowQGMatrikDataByRecordNo(qualityMatrikId, recordNo);
        if (CollectionUtil.isNotEmpty(dataList)) {
            result.setDefectAnomalyList(dataConversion(dataList, recordNo));
        }

        return result;
    }

    @Override
    public List<ComboInfoDTO> getQualityGateByWorkstationCode(String workstationCode, String model) {
        return exLogicMapper.getQualityGateByWorkstationCode(workstationCode, model);
    }

    @Override
    public List<DefectAnomalyDto> getGateAnomalyByGateBlankIdAndRecordNo(Long qualityGateBlankId, String recordNo) {
        List<DefectAnomalyDto> resultList = Lists.newArrayList();
        List<DefectAnomalyDto> dataList = exLogicMapper.getGateAnomalyByGateBlankIdAndRecordNo(qualityGateBlankId, recordNo);
        if (CollectionUtil.isNotEmpty(dataList)) {
            resultList = dataConversion(dataList, recordNo);
        }
        return resultList;
    }

    private List<DefectAnomalyDto> dataConversion(List<DefectAnomalyDto> lists, String recordNo) {
        QueryWrapper<PqsExEntryDefectAnomalyEntity> anomalyEntityQueryWrapper = new QueryWrapper<>();
        anomalyEntityQueryWrapper.lambda().ne(PqsExEntryDefectAnomalyEntity::getStatus, 4)
                .eq(PqsExEntryDefectAnomalyEntity::getAuditRecordNo, recordNo);
        List<PqsExEntryDefectAnomalyEntity> anomalyEntityList = pqsExEntryDefectAnomalyService.getData(anomalyEntityQueryWrapper, false);
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
    public ExQgGateConfigurationInfo getQGGateData(Long qualityGateId) {

        // 获取QG基础信息
        ExQgGateConfigurationInfo result = new ExQgGateConfigurationInfo();
        PqsExQualityGateEntity gateEntity = pqsExQualityGateService.get(qualityGateId);
        if (gateEntity != null) {
            BeanUtils.copyProperties(gateEntity, result);

            QueryWrapper<PqsExQualityGateWorkstationEntity> workstationEntityQueryWrapper = new QueryWrapper();
            workstationEntityQueryWrapper.lambda().eq(PqsExQualityGateWorkstationEntity::getPrcPqsExQualityGateId, qualityGateId);
            String workstationCodes = pqsExQualityGateWorkstationService.getData(workstationEntityQueryWrapper, false)
                    .stream().map(PqsExQualityGateWorkstationEntity::getWorkstationCode)
                    .collect(Collectors.joining(","));
            result.setWorkstationCodes(workstationCodes);

            QueryWrapper<PqsExQualityGateBlankEntity> blankEntityQueryWrapper = new QueryWrapper();
            blankEntityQueryWrapper.lambda().eq(PqsExQualityGateBlankEntity::getPrcPqsExQualityGateId, qualityGateId);
            List<PqsExQualityGateBlankEntity> blankEntityList = pqsExQualityGateBlankService.getData(blankEntityQueryWrapper, false);
            result.setGateBlanks(blankEntityList);

            QueryWrapper<PqsExQualityGateTcEntity> tcEntityQueryWrapper = new QueryWrapper();
            tcEntityQueryWrapper.lambda().eq(PqsExQualityGateTcEntity::getPrcPqsExQualityGateId, qualityGateId);
            String modelCodes = pqsExQualityGateTcService.getData(tcEntityQueryWrapper, false)
                    .stream().map(PqsExQualityGateTcEntity::getModel).collect(Collectors.joining(","));
            result.setModelCodes(modelCodes);

            // 同时返回缺陷数据
            blankEntityList.forEach(b -> {
                List<ConditionDto> conditions = new ArrayList<>();
                conditions.add(new ConditionDto("prcPqsExQualityGateBlankId", b.getId().toString(), ConditionOper.Equal));
                List<PqsExQualityGateAnomalyEntity> anomalyEntityList = pqsExQualityGateAnomalyService.getData(conditions);
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
        List<PqsExQualityMatrikWorkstationEntity> pqmweList = Lists.newArrayList();
        workstationCodeList.forEach(w -> {
            workstationEntityList.forEach(wse -> {
                if (w.equals(wse.getWorkstationCode())) {
                    PqsExQualityMatrikWorkstationEntity pqmwEntity = new PqsExQualityMatrikWorkstationEntity();
                    pqmwEntity.setPrcPqsExQualityMatrikId(info.getId() == null ? Constant.DEFAULT_ID : info.getId());
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
        List<PqsExQualityMatrikTcEntity> tcEntityList = Lists.newArrayList();
        modelCodeList.forEach(m -> {
            defectLevels.forEach(d -> {
                if (m.equals(d.getValue())) {
                    PqsExQualityMatrikTcEntity matrikTcEntity = new PqsExQualityMatrikTcEntity();
                    matrikTcEntity.setModel(d.getValue());
                    matrikTcEntity.setModelName(d.getText());
                    tcEntityList.add(matrikTcEntity);
                }
            });
        });

        PqsExQualityMatrikEntity matrikEntity = new PqsExQualityMatrikEntity();
        BeanUtils.copyProperties(info, matrikEntity);
        matrikEntity.setModels(tcEntityList.stream().map(PqsExQualityMatrikTcEntity::getModelName)
                .collect(Collectors.joining(",")));
        matrikEntity.setWorkstationNames(pqmweList.stream().map(PqsExQualityMatrikWorkstationEntity::getWorkstationName)
                .collect(Collectors.joining(",")));

        // 新增新数据
        if (info.getId() == null) {
            matrikEntity.setId(IdGenerator.getId());
            pqsExQualityMatrikService.insert(matrikEntity);
        } else {
            // 修改原有数据
            pqsExQualityMatrikService.update(matrikEntity);
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
        PqsExQualityMatrikEntity matrikEntity = pqsExQualityMatrikService.get(qualityMatrikId);
        if (matrikEntity != null) {
            BeanUtils.copyProperties(matrikEntity, result);

            QueryWrapper<PqsExQualityMatrikWorkstationEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsExQualityMatrikWorkstationEntity::getPrcPqsExQualityMatrikId, qualityMatrikId);
            String workstationCodes = pqsExQualityMatrikWorkstationService.getData(queryWrapper, false)
                    .stream().map(PqsExQualityMatrikWorkstationEntity::getWorkstationCode).collect(Collectors.joining(","));

            QueryWrapper<PqsExQualityMatrikTcEntity> tcEntityQueryWrapper = new QueryWrapper<>();
            tcEntityQueryWrapper.lambda().eq(PqsExQualityMatrikTcEntity::getPrcPqsExQualityMatrikId, qualityMatrikId);
            String models = pqsExQualityMatrikTcService.getData(tcEntityQueryWrapper, false)
                    .stream().map(PqsExQualityMatrikTcEntity::getModel).collect(Collectors.joining(","));

            List<PqsExDefectAnomalyEntity> anomalyEntityList = pqsExDefectAnomalyService.getAllDatas();

            QueryWrapper<PqsExQualityMatrikAnomalyEntity> anomalyEntityQueryWrapper = new QueryWrapper<>();
            anomalyEntityQueryWrapper.lambda().eq(PqsExQualityMatrikAnomalyEntity::getPrcPqsExQualityMatrikId, qualityMatrikId);
            List<PqsExQualityMatrikAnomalyEntity> matrikAnomalyEntityList = pqsExQualityMatrikAnomalyService.getData(anomalyEntityQueryWrapper, false);

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
     * @param pqsExQualityMatrikId
     */
    private void submitQualityMatrikTc(List<PqsExQualityMatrikTcEntity> lists, Long pqsExQualityMatrikId) {
        UpdateWrapper<PqsExQualityMatrikTcEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(PqsExQualityMatrikTcEntity::getPrcPqsExQualityMatrikId, pqsExQualityMatrikId);
        // 删除现有车型
        pqsExQualityMatrikTcService.delete(wrapper);

        // 写入车型
        lists.forEach(p -> {
            p.setPrcPqsExQualityMatrikId(pqsExQualityMatrikId);
        });
        pqsExQualityMatrikTcService.insertBatch(lists);
    }

    /**
     * 添加QG配置工位
     *
     * @param lists
     * @param pqsExQualityMatrikId
     */
    private void submitQualityMatrikWorkstation(List<PqsExQualityMatrikWorkstationEntity> lists, Long pqsExQualityMatrikId) {

        UpdateWrapper<PqsExQualityMatrikWorkstationEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(PqsExQualityMatrikWorkstationEntity::getPrcPqsExQualityMatrikId, pqsExQualityMatrikId);
        // 删除现有工位
        pqsExQualityMatrikWorkstationService.delete(updateWrapper);

        // 写入工位
        lists.forEach(p -> {
            p.setPrcPqsExQualityMatrikId(pqsExQualityMatrikId);
        });
        pqsExQualityMatrikWorkstationService.insertBatch(lists, lists.size());
    }

    /**
     * 添加百格图缺陷
     *
     * @param lists
     * @param pqsExQualityMatrikId
     */
    private void submitQualityMatrikAnomaly(List<DefectAnomalyDto> lists, Long pqsExQualityMatrikId) {

        List<PqsExDefectAnomalyEntity> anomalyEntityList = pqsExDefectAnomalyService.getAllDatas();

        // 删除所有色块
        UpdateWrapper<PqsExQualityMatrikAnomalyEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(PqsExQualityMatrikAnomalyEntity::getPrcPqsExQualityMatrikId, pqsExQualityMatrikId);
        pqsExQualityMatrikAnomalyService.delete(updateWrapper);

        // 写入百格图缺陷
        List<PqsExQualityMatrikAnomalyEntity> insertLists = Lists.newArrayList();
        lists.forEach(l -> {
            anomalyEntityList.forEach(a -> {
                if (l.getDefectAnomalyCode().equals(a.getDefectAnomalyCode())) {
                    PqsExQualityMatrikAnomalyEntity entity = new PqsExQualityMatrikAnomalyEntity();
                    entity.setDefectAnomalyCode(l.getDefectAnomalyCode());
                    entity.setDefectAnomalyDescription(a.getDefectAnomalyDescription());
                    entity.setPrcPqsExQualityMatrikId(pqsExQualityMatrikId);
                    entity.setShortCode(a.getShortCode());
                    insertLists.add(entity);
                }
            });
        });
        pqsExQualityMatrikAnomalyService.insertBatch(insertLists, lists.size());
    }

    /**
     * 提交QG配置数据信息
     *
     * @param info
     */
    @Override
    public void submitQGGateData(ExQgGateConfigurationInfo info) {

        // 从PM中获取工位对应的关联的QG岗信息
        ResultVO<PmAllDTO> stationList = new ResultVO().ok(pmVersionProvider.getObjectedPm());
        if (stationList == null || !stationList.getSuccess()) {
            throw new InkelinkException("PM模块根据工位编号获取关联的QG岗位信息失败。" + (stationList == null ? "" : stationList.getMessage()));
        }
        List<PmWorkStationEntity> workstationEntityList = stationList.getData().getStations();

        List<String> workstationCodeList = Arrays.stream(info.getWorkstationCodes().split(",")).collect(Collectors.toList());
        List<PqsExQualityGateWorkstationEntity> pqgweList = Lists.newArrayList();
        workstationCodeList.forEach(w -> {
            workstationEntityList.forEach(wse -> {
                if (w.equals(wse.getWorkstationCode())) {
                    PqsExQualityGateWorkstationEntity pqgweEntity = new PqsExQualityGateWorkstationEntity();
                    pqgweEntity.setPrcPqsExQualityGateId(info.getId());
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
        List<PqsExQualityGateTcEntity> gateTcEntityList = Lists.newArrayList();
        modelCodeList.forEach(m -> {
            defectLevels.forEach(d -> {
                if (m.equals(d.getValue())) {
                    PqsExQualityGateTcEntity gateTcEntity = new PqsExQualityGateTcEntity();
                    gateTcEntity.setPrcPqsExQualityGateId(info.getId());
                    gateTcEntity.setModel(d.getValue());
                    gateTcEntity.setModelName(d.getText());
                    gateTcEntityList.add(gateTcEntity);
                }
            });
        });

        PqsExQualityGateEntity gateEntity = new PqsExQualityGateEntity();
        BeanUtils.copyProperties(info, gateEntity);
        gateEntity.setWorkstationNames(pqgweList.stream().map(PqsExQualityGateWorkstationEntity::getWorkstationName)
                .collect(Collectors.joining(",")));
        gateEntity.setModels(gateTcEntityList.stream().map(PqsExQualityGateTcEntity::getModelName)
                .collect(Collectors.joining(",")));

        // 新增新数据
        if (gateEntity.getId() == null) {
            gateEntity.setId(IdGenerator.getId());
            pqsExQualityGateService.insert(gateEntity);
        } else {
            // 修改原有数据
            pqsExQualityGateService.update(gateEntity);
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
     * @param pqsExQualityGateId
     */
    private void submitQGAnomalyDataTc(List<PqsExQualityGateTcEntity> lists, Long pqsExQualityGateId) {

        // 删除现有车型
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("prcPqsExQualityGateId", pqsExQualityGateId.toString(), ConditionOper.Equal));
        pqsExQualityGateTcService.delete(conditions);

        // 写入车型
        lists.forEach(p -> {
            p.setPrcPqsExQualityGateId(pqsExQualityGateId);
        });
        pqsExQualityGateTcService.insertBatch(lists);
    }

    /**
     * 添加工位
     *
     * @param lists
     * @param pqsExQualityGateId
     */
    private void submitQGAnomalyDataWorkstation(List<PqsExQualityGateWorkstationEntity> lists, Long pqsExQualityGateId) {

        // 删除现有工位
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("prcPqsExQualityGateId", pqsExQualityGateId.toString(), ConditionOper.Equal));
        pqsExQualityGateWorkstationService.delete(conditions);

        // 写入工位
        lists.forEach(p -> {
            p.setPrcPqsExQualityGateId(pqsExQualityGateId);
        });
        pqsExQualityGateWorkstationService.insertBatch(lists);
    }

    /***
     * 添加QG配置对应的色块数据
     *
     * @param lists
     * @param pqsExQualityGateId
     */
    private void submitQGAnomalyDataWorkPlaceGateBlanks(List<PqsExQualityGateBlankEntity> lists, Long pqsExQualityGateId) {

        List<PqsExQualityGateAnomalyEntity> gateAnomalyEntityList = Lists.newArrayList();
        List<PqsExDefectAnomalyEntity> defectAnomalyEntityList = pqsExDefectAnomalyService.getAllDatas();

        // 当前色块
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("prcPqsExQualityGateId", pqsExQualityGateId.toString(), ConditionOper.Equal));
        List<PqsExQualityGateBlankEntity> blankEntityList = pqsExQualityGateBlankService.getData(conditions);
        if (CollectionUtil.isNotEmpty(blankEntityList)) {
            conditions.clear();
            List<String> defectAnomalyCodes = blankEntityList.stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
            conditions.clear();
            conditions.add(new ConditionDto("prcPqsExQualityGateBlankId", String.join("|", defectAnomalyCodes), ConditionOper.In));
            pqsExQualityGateAnomalyService.delete(conditions);
        }
        // 删除色块
        conditions.clear();
        conditions.add(new ConditionDto("prcPqsExQualityGateId", pqsExQualityGateId.toString(), ConditionOper.Equal));
        pqsExQualityGateBlankService.delete(conditions);

        // 添加色块
        lists.forEach(l -> {
            l.setId(IdGenerator.getId());
            l.setPrcPqsExQualityGateId(pqsExQualityGateId);
            defectAnomalyEntityList.forEach(d -> {
                if (CollectionUtil.isNotEmpty(l.getAnomalyCodes())) {
                    l.getAnomalyCodes().forEach(a -> {
                        if (a.getDefectAnomalyCode().equals(d.getDefectAnomalyCode())) {
                            PqsExQualityGateAnomalyEntity gateAnomalyEntity = new PqsExQualityGateAnomalyEntity();
                            gateAnomalyEntity.setDefectAnomalyCode(d.getDefectAnomalyCode());
                            gateAnomalyEntity.setDefectAnomalyDescription(d.getDefectAnomalyDescription());
                            gateAnomalyEntity.setPrcPqsExQualityGateBlankId(l.getId());
                            gateAnomalyEntity.setShortCode(d.getShortCode());
                            gateAnomalyEntityList.add(gateAnomalyEntity);
                        }
                    });
                }
            });
        });
        pqsExQualityGateBlankService.insertBatch(lists, lists.size());
        pqsExQualityGateAnomalyService.insertBatch(gateAnomalyEntityList, gateAnomalyEntityList.size());
    }
}
