package com.ca.mfd.prc.pqs.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.ActiveAnomalyInfo;
import com.ca.mfd.prc.pqs.dto.AnomalyDto;
import com.ca.mfd.prc.pqs.dto.AnomalyStatusModifyInfo;
import com.ca.mfd.prc.pqs.dto.AuditDetailListDto;
import com.ca.mfd.prc.pqs.dto.CreateMMScrapInfo;
import com.ca.mfd.prc.pqs.dto.CreateQgAuditInfo;
import com.ca.mfd.prc.pqs.dto.DefectAnomalyDto;
import com.ca.mfd.prc.pqs.dto.GetDefectAnomalyRequest;
import com.ca.mfd.prc.pqs.dto.GetMmScrapDataFilterInfo;
import com.ca.mfd.prc.pqs.dto.ModifyDefectResponsibleInfo;
import com.ca.mfd.prc.pqs.dto.ProductAnomalyLogDto;
import com.ca.mfd.prc.pqs.dto.ProductDefectAnomalyReponse;
import com.ca.mfd.prc.pqs.dto.QgPartAuditDetailFilterInfo;
import com.ca.mfd.prc.pqs.dto.QgRiskDto;
import com.ca.mfd.prc.pqs.dto.QgRiskOperInfo;
import com.ca.mfd.prc.pqs.dto.ShowQgMatrikDto;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyEntity;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyRiskDetailEntity;
import com.ca.mfd.prc.pqs.entity.PqsDeptEntity;
import com.ca.mfd.prc.pqs.entity.PqsGradeEntity;
import com.ca.mfd.prc.pqs.entity.PqsMmDefectAnomalyEntity;
import com.ca.mfd.prc.pqs.entity.PqsMmDefectAnomalyLogEntity;
import com.ca.mfd.prc.pqs.entity.PqsMmScrapRecordEntity;
import com.ca.mfd.prc.pqs.entity.PqsPartsScrapEntity;
import com.ca.mfd.prc.pqs.entity.PqsProductDefectAnomalyEntity;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikEntity;
import com.ca.mfd.prc.pqs.enums.PqsLogicStatusEnum;
import com.ca.mfd.prc.pqs.mapper.IPqsLogicMapper;
import com.ca.mfd.prc.pqs.mapper.IPqsMmDefectAnomalyMapper;
import com.ca.mfd.prc.pqs.mapper.IPqsPartsLogicMapper;
import com.ca.mfd.prc.pqs.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.pqs.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pqs.remote.app.pm.dto.ShiftDTO;
import com.ca.mfd.prc.pqs.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pqs.remote.app.pm.entity.PmOrganizationEntity;
import com.ca.mfd.prc.pqs.remote.app.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.pqs.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pqs.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pqs.remote.app.pm.provider.PmProductMaterialMasterProvider;
import com.ca.mfd.prc.pqs.remote.app.pm.provider.PmShcCalendarProvider;
import com.ca.mfd.prc.pqs.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyRiskDetailService;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyService;
import com.ca.mfd.prc.pqs.service.IPqsDeptService;
import com.ca.mfd.prc.pqs.service.IPqsGradeService;
import com.ca.mfd.prc.pqs.service.IPqsMmDefectAnomalyLogService;
import com.ca.mfd.prc.pqs.service.IPqsMmDefectAnomalyService;
import com.ca.mfd.prc.pqs.service.IPqsMmScrapRecordService;
import com.ca.mfd.prc.pqs.service.IPqsPartsLogicService;
import com.ca.mfd.prc.pqs.service.IPqsPartsScrapService;
import com.ca.mfd.prc.pqs.service.IPqsProductDefectAnomalyService;
import com.ca.mfd.prc.pqs.service.IPqsQualityMatrikService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author edwards.qu
 * @date 2023/9/17
 */
@Service
public class PqsPartsLogicServiceImpl implements IPqsPartsLogicService {

    private final static String PARTS_AUDIT = "PartsAudit";
    private final static String INSPECTION_NO = "MMScrapNo";
    @Autowired
    private PmProductMaterialMasterProvider pmProductMaterialMasterProvider;
    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;
    @Autowired
    private IPqsPartsScrapService pqsPartsScrapService;
    @Autowired
    private IdentityHelper identityHelper;
    @Autowired
    private IPqsMmDefectAnomalyService pqsMmDefectAnomalyService;
    @Autowired
    private IPqsLogicMapper pqsLogicMapper;
    @Autowired
    private IPqsPartsLogicMapper pqsPartsLogicMapper;
    @Autowired
    private IPqsQualityMatrikService pqsQualityMatrikService;
    @Autowired
    private IPqsDefectAnomalyRiskDetailService pqsDefectAnomalyRiskDetailService;
    @Autowired
    private IPqsProductDefectAnomalyService pqsProductDefectAnomalyService;
    @Autowired
    private IPqsGradeService pqsGradeService;
    @Autowired
    private IPqsDeptService pqsDeptService;
    @Autowired
    private PmShcCalendarProvider pmShcCalendarProvider;
    @Autowired
    private IPqsDefectAnomalyService pqsDefectAnomalyService;
    @Autowired
    private IPqsMmDefectAnomalyLogService pqsMmDefectAnomalyLogService;
    @Autowired
    private IPqsMmDefectAnomalyMapper pqsMmDefectAnomalyMapper;
    @Autowired
    private IPqsMmScrapRecordService pqsMmScrapRecordService;

    /**
     * 根据工位代码获取常用缺陷
     *
     * @param workstationCode 工位代码
     * @param sn              产品唯一码
     * @return
     */
    @Override
    public List<DefectAnomalyDto> getAnomalyWpListBySn(String workstationCode, String sn) {

        // 查询非工艺的缺陷 且未激活的缺陷
        List<String> curretnDefect = pqsMmDefectAnomalyService.getAllDatas().stream()
                .filter(p -> StringUtils.equals(p.getSn(), sn)
                        && p.getStatus() != 4).map(PqsMmDefectAnomalyEntity::getDefectAnomalyCode)
                .collect(Collectors.toList());
        List<DefectAnomalyDto> wpDatas = pqsLogicMapper.getDefectAnomalyWpListInfo(workstationCode);
        if (CollectionUtil.isNotEmpty(wpDatas)) {
            // 匹配激活状态
            wpDatas.forEach(w -> curretnDefect.forEach(c -> w.setIsActived(StringUtils.equals(w.getDefectAnomalyCode(), c))));
        }

        return wpDatas;
    }

    /**
     * 根据工位代码+工单号获取常用缺陷
     *
     * @param workstationCode 工位代码
     * @param inspectionNo    工单号
     * @return
     */
    @Override
    public List<DefectAnomalyDto> getAnomalyWpListByInspectionNo(String workstationCode, String inspectionNo) {

        // 查询非工艺的缺陷 且未激活的缺陷
        List<String> curretnDefect = pqsMmDefectAnomalyService.getAllDatas().stream()
                .filter(p -> StringUtils.equals(p.getInspectionNo(), inspectionNo)
                        && p.getStatus() != 4).map(PqsMmDefectAnomalyEntity::getDefectAnomalyCode)
                .collect(Collectors.toList());
        List<DefectAnomalyDto> wpDatas = pqsLogicMapper.getDefectAnomalyWpListInfo(workstationCode);
        if (CollectionUtil.isNotEmpty(wpDatas)) {
            // 匹配激活状态
            wpDatas.forEach(w -> curretnDefect.forEach(c -> w.setIsActived(StringUtils.equals(w.getDefectAnomalyCode(), c))));
        }

        return wpDatas;
    }

    /**
     * 根据产品唯一码获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 色块ID
     * @param sn                 产品唯一码
     * @return
     */
    @Override
    public List<DefectAnomalyDto> getGateAnomalyByGateBlankIdAndSn(Long qualityGateBlankId, String sn) {

        return pqsPartsLogicMapper.getGateAnomalyByGateBlankIdAndSn(qualityGateBlankId, sn);
    }

    /**
     * 根据评审工单号获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 色块ID
     * @param inspectionNo       评审工单号
     * @return
     */
    @Override
    public List<DefectAnomalyDto> getGateAnomalyByGateBlankIdAndInspectionNo(Long qualityGateBlankId, String inspectionNo) {

        return pqsPartsLogicMapper.getGateAnomalyByGateBlankIdAndInspectionNo(qualityGateBlankId, inspectionNo);
    }

    /**
     * QG岗根据产品唯一码查看质量门检查图片数据
     *
     * @param qualityMatrikId 百格图ID
     * @param sn              产品唯一码
     * @return
     */
    @Override
    public ShowQgMatrikDto showQGMatrikDataBySn(Long qualityMatrikId, String sn) {

        // 获取QG基础信息
        PqsQualityMatrikEntity pqsQualityMatrikEntity = pqsQualityMatrikService.get(qualityMatrikId);
        // todo验证配置
        ShowQgMatrikDto result = new ShowQgMatrikDto();
        BeanUtils.copyProperties(pqsQualityMatrikEntity, result);
        List<DefectAnomalyDto> dtoLists = pqsPartsLogicMapper.showQGMatrikDataBySn(qualityMatrikId, sn);
        result.setDefectAnomalyList(dtoLists);

        return result;
    }

    /**
     * QG岗根据评审工单号查看质量门检查图片数据
     *
     * @param qualityMatrikId 百格图ID
     * @param inspectionNo    评审工单号
     * @return
     */
    @Override
    public ShowQgMatrikDto showQGMatrikDataByInspectionNo(Long qualityMatrikId, String inspectionNo) {

        // 获取QG基础信息
        PqsQualityMatrikEntity pqsQualityMatrikEntity = pqsQualityMatrikService.get(qualityMatrikId);
        // todo验证配置
        ShowQgMatrikDto result = new ShowQgMatrikDto();
        BeanUtils.copyProperties(pqsQualityMatrikEntity, result);
        List<DefectAnomalyDto> dtoLists = pqsPartsLogicMapper.showQGMatrikDataByInspectionNo(qualityMatrikId, inspectionNo);
        result.setDefectAnomalyList(dtoLists);

        return result;
    }

    /**
     * QG岗查围堵缺陷信息
     *
     * @param sn 产品唯一码
     * @return
     */
    @Override
    public List<QgRiskDto> showRiskDataBySn(String sn) {

        List<QgRiskDto> resultList = Lists.newArrayList();

        List<PqsDefectAnomalyRiskDetailEntity> defectAnomalyRiskDetailEntityList = pqsDefectAnomalyRiskDetailService.getAllDatas()
                .stream().filter(p -> StringUtils.equals(p.getSn(), sn)).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(defectAnomalyRiskDetailEntityList)) {
            defectAnomalyRiskDetailEntityList.forEach(d -> {
                QgRiskDto qgRiskDto = new QgRiskDto();
                BeanUtils.copyProperties(d, qgRiskDto);
                resultList.add(qgRiskDto);
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

                    activeAnomaly(activeAnomalyInfo);
                }
                if (riskInfo.getIsActived()) {
                    // 如果已激活，跳过
                    return;
                }
                PqsDefectAnomalyRiskDetailEntity updEntity = new PqsDefectAnomalyRiskDetailEntity();
                updEntity.setIsActived(true);
                updEntity.setActiveDt(new Date());
                updEntity.setActiveBy(identityHelper.getUserName());
                updEntity.setStatus(20);
                updEntity.setId(info.getId());
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
                        AnomalyStatusModifyInfo statusModifyInfo = new AnomalyStatusModifyInfo();
                        statusModifyInfo.setDataId(anomaly.getId());
                        statusModifyInfo.setStatus(2);
                        statusModifyInfo.setRepairActivity(info.getRepairActivity());
                        modifyDefectAnomalyStatus(statusModifyInfo);
                    }
                }
                break;
            case 90:
                // 释放
                PqsDefectAnomalyRiskDetailEntity detailEntity = new PqsDefectAnomalyRiskDetailEntity();
                detailEntity.setCloseDt(new Date());
                detailEntity.setCloseBy(identityHelper.getUserName());
                detailEntity.setCloseRemark(info.getCloseRemark());
                detailEntity.setStatus(90);
                detailEntity.setId(info.getId());
                pqsDefectAnomalyRiskDetailService.updateById(detailEntity);
                break;
            default:
                throw new InkelinkException("未支持的参数，请检查！");
        }
    }

    /**
     * 激活缺陷
     *
     * @param info
     */
    @Override
    public void activeAnomaly(ActiveAnomalyInfo info) {
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

        // 工艺自定义缺陷
        if (StringUtils.isNotEmpty(info.getInspectionNo())) {
            // TODO 工单验证
        }

        // 班次
        ResultVO<ShiftDTO> shiftDTOResultVO = new ResultVO().ok(pmShcCalendarProvider.getCurrentShiftInfo(lineInfo.getLineCode()));
        if (shiftDTOResultVO == null || !shiftDTOResultVO.getSuccess()) {
            throw new InkelinkException("PM模块获取班次信息失败。" + (shiftDTOResultVO == null ? "" : shiftDTOResultVO.getMessage()));
        }
        ShiftDTO shcShift = shiftDTOResultVO.getData();

        // 遍历传入的缺陷
        for (AnomalyDto a : info.getAnomalyInfos()) {

            PqsMmDefectAnomalyEntity vAnomaly = null;

            // 空缺陷 跳过验证
            if (StringUtils.isEmpty(a.getDefectAnomalyCode())
                    && StringUtils.isEmpty(a.getDefectAnomalyDescription())) {
                continue;
            }

            if (StringUtils.isNotEmpty(a.getDefectAnomalyCode())) {
                // 评审单为空，验证单件条码
                if (StringUtils.isEmpty(info.getInspectionNo())) {
                    // 判断缺陷是否已被激活，已被激活的缺陷未关闭将不会改变他的状态
                    PqsMmDefectAnomalyEntity query = pqsMmDefectAnomalyService.getAllDatas()
                            .stream().filter(p -> StringUtils.equals(p.getDefectAnomalyCode(), a.getDefectAnomalyCode())
                                    && StringUtils.equals(p.getSn(), info.getSn())
                                    && p.getStatus() != 4).findFirst().orElse(null);
                    vAnomaly = pqsMmDefectAnomalyService.getAllDatas()
                            .stream().filter(p -> StringUtils.equals(p.getDefectAnomalyCode(), a.getDefectAnomalyCode())
                                    && StringUtils.equals(p.getSn(), info.getSn())
                                    && p.getStatus() != 4).findFirst().orElse(null);
                    // 缺陷已被激活
                    if (vAnomaly != null) {
                        continue;
                    }
                } else {
                    // 判断缺陷是否已被激活，已被激活的缺陷未关闭将不会改变他的状态
                    PqsMmDefectAnomalyEntity query = pqsMmDefectAnomalyService.getAllDatas()
                            .stream().filter(p -> StringUtils.equals(p.getDefectAnomalyCode(), a.getDefectAnomalyCode())
                                    && StringUtils.equals(p.getInspectionNo(), info.getInspectionNo())
                                    && p.getStatus() != 4).findFirst().orElse(null);
                    vAnomaly = pqsMmDefectAnomalyService.getAllDatas()
                            .stream().filter(p -> StringUtils.equals(p.getDefectAnomalyCode(), a.getDefectAnomalyCode())
                                    && StringUtils.equals(p.getInspectionNo(), info.getInspectionNo())
                                    && p.getStatus() != 4).findFirst().orElse(null);
                    // 缺陷已被激活
                    if (vAnomaly != null) {
                        continue;
                    }
                }
            }

            if (vAnomaly == null) {
                vAnomaly = new PqsMmDefectAnomalyEntity();
            }
            // 获取配置缺陷配置
            PqsDefectAnomalyEntity anomalyConfig = pqsDefectAnomalyService.getAllDatas()
                    .stream().filter(p -> StringUtils.equals(p.getDefectAnomalyCode(), a.getDefectAnomalyCode()))
                    .findFirst().orElse(null);
            // 获取PM数据

            vAnomaly.setJsonData(a.getJsonData());
            vAnomaly.setInspectionNo(info.getInspectionNo());
            vAnomaly.setId(IdGenerator.getId());
            vAnomaly.setImg(a.getImg());
            vAnomaly.setDefectAnomalyCode(a.getDefectAnomalyCode());
            vAnomaly.setDefectAnomalyDescription(anomalyConfig != null ? anomalyConfig.getDefectAnomalyDescription() : a.getDefectAnomalyDescription());
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
            vAnomaly.setWorkstationCode(workstationInfo.getWorkstationCode());
            vAnomaly.setWorkstationName(workstationInfo.getWorkstationName());
            vAnomaly.setOrganizationCode(organization.getOrganizationCode());
            vAnomaly.setSn(info.getSn());
            vAnomaly.setStatus(PqsLogicStatusEnum.ACTIVATED.code());
            vAnomaly.setWorkshopCode(shopInfo == null ? "" : shopInfo.getWorkshopCode());
            vAnomaly.setWorkshopName(shopInfo == null ? "" : shopInfo.getWorkshopName());
            vAnomaly.setSource(a.getSource());

            // 插入数据
            pqsMmDefectAnomalyService.insert(vAnomaly);
            // 写日志
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
        PqsMmDefectAnomalyLogEntity logEntity = new PqsMmDefectAnomalyLogEntity();
        logEntity.setPrcPqsMmDefectAnomalyId(dataId);
        logEntity.setRemark(identityHelper.getUserName() + "将状态更改为" + PqsLogicStatusEnum.getValue(statusCode));
        logEntity.setStatus(statusCode);
        pqsMmDefectAnomalyLogService.insert(logEntity);
    }

    /**
     * 后台更新缺陷责任区域
     *
     * @param info
     */
    @Override
    public void modifyDefectAnomalyRepsponsibelInfo(ModifyDefectResponsibleInfo info) {

        pqsMmDefectAnomalyService.modifyDefectAnomalyRepsponsibelInfo(info);
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
        List<PqsMmDefectAnomalyLogEntity> lists = pqsMmDefectAnomalyLogService.getAllDatas()
                .stream().filter(p -> p.getPrcPqsMmDefectAnomalyId().equals(anomalyId))
                .sorted(Comparator.comparing(PqsMmDefectAnomalyLogEntity::getCreationDate)).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(lists)) {
            lists.forEach(l -> {
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
     * 缺陷活动--返修复检
     *
     * @param statusModifyInfo
     */
    @Override
    public void modifyDefectAnomalyStatus(AnomalyStatusModifyInfo statusModifyInfo) {
        PqsMmDefectAnomalyEntity info = pqsMmDefectAnomalyService.get(statusModifyInfo.getDataId());
        if (info == null) {
            throw new InkelinkException("未找到对应的激活缺陷");
        }
        info.setStatus(statusModifyInfo.getStatus());


        // 已激活
        if (statusModifyInfo.getStatus() == PqsLogicStatusEnum.ACTIVATED.code()) {
            info.setActivateUser(identityHelper.getUserName());
            info.setActivateTime(new Date());
        }
        // 已修复
        if (statusModifyInfo.getStatus() == PqsLogicStatusEnum.RECOVERED.code()) {
            if (statusModifyInfo.getRepairActivity() == null) {
                throw new InkelinkException("请填写修复内容");
            }
            info.setRepairUser(identityHelper.getUserName());
            info.setRepairTime(DateUtils.parse(statusModifyInfo.getRepairActivity().getRepairTime(), DateUtils.DATE_TIME_PATTERN));
            info.setRepairWay(statusModifyInfo.getRepairActivity().getRepairWay());
            info.setRepairRemark(statusModifyInfo.getRepairActivity().getRepairRemark());
            info.setRepairSpendTime(statusModifyInfo.getRepairActivity().getSpendTime());

        }
        // 未发现
        if (statusModifyInfo.getStatus() == PqsLogicStatusEnum.NOT_FOUND.code()) {
            info.setRepairUser(identityHelper.getUserName());
            info.setRepairTime(new Date());
            info.setRepairWay(Strings.EMPTY);
            info.setRepairRemark(Strings.EMPTY);
            info.setRepairSpendTime(BigDecimal.valueOf(0));

        }
        // 合格
        if (statusModifyInfo.getStatus() == PqsLogicStatusEnum.QUALIFIED.code()) {
        }
        // 不合格
        if (statusModifyInfo.getStatus() == PqsLogicStatusEnum.DISQUALIFICATION.code()) {
            info.setRepairUser(identityHelper.getUserName());
            info.setRecheckTime(new Date());
        }

        pqsMmDefectAnomalyService.update(info);
        // 记录日志
        writeAnomalyLog(statusModifyInfo.getDataId(), info.getStatus());
    }

    /**
     * 添加激活缺陷备注
     *
     * @param dataId
     * @param remark
     */
    @Override
    public void appendDefectAnomalyRemark(Long dataId, String remark) {
        PqsMmDefectAnomalyEntity anomalyEntity = pqsMmDefectAnomalyService.get(dataId);
        if (anomalyEntity == null) {
            throw new InkelinkException("未找到对应的激活缺陷");
        }

        PqsMmDefectAnomalyEntity updateEntity = new PqsMmDefectAnomalyEntity();
        updateEntity.setId(dataId);
        updateEntity.setRemark(remark);
        pqsMmDefectAnomalyService.update(updateEntity);

        PqsMmDefectAnomalyLogEntity logEntity = new PqsMmDefectAnomalyLogEntity();
        logEntity.setPrcPqsMmDefectAnomalyId(dataId);
        logEntity.setRemark(identityHelper.getUserName() + "添加备注" + "【" + remark + "】");
        logEntity.setStatus(anomalyEntity.getStatus());
        pqsMmDefectAnomalyLogService.insert(logEntity);
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
        Page<ProductDefectAnomalyReponse> query = pqsMmDefectAnomalyMapper.getVehicleMmDefectAnomalyList(page, para);
        pageData.setDatas(query.getRecords());

        pageData.setTotal((int) query.getTotal());
        return pageData;
    }

    /**
     * 创建评审单
     *
     * @param info
     */
    @Override
    public void createAuditByQgWorkstation(CreateQgAuditInfo info) {

        PmProductMaterialMasterEntity materialInfo = pmProductMaterialMasterProvider.getAllDatas()
                .stream().filter(p -> p.getMaterialNo().equals(info.getMaterialNo())).findFirst().orElse(null);
        if (materialInfo == null) {
            throw new InkelinkException("未找到" + info.getMaterialNo() + "对应的物料信息！");
        }

        PmWorkStationEntity workstationInfo = pmVersionProvider.getObjectedPm().getStations()
                .stream().filter(p -> p.getWorkstationCode().equals(info.getWorkstationCode())).findFirst().orElse(null);
        if (workstationInfo == null) {
            throw new InkelinkException("未找到对应工位！");
        }

        String partsAudit = sysSnConfigProvider.createSn(PARTS_AUDIT);

        PqsPartsScrapEntity entity = new PqsPartsScrapEntity();
        entity.setId(IdGenerator.getId());
        entity.setInspectionNo(partsAudit);
        entity.setWorkstationCode(workstationInfo.getWorkstationCode());
        entity.setWorkstationName(workstationInfo.getWorkstationName());
        entity.setMaterialNo(materialInfo.getMaterialNo());
        entity.setMaterialName(materialInfo.getMaterialCn());
        entity.setUnit(materialInfo.getUnit());
        entity.setScrapQty(info.getQty());
        entity.setQcDt(new Date());
        entity.setQcUser(identityHelper.getUserName());
        entity.setStatus(1);
        entity.setBarcode(info.getBarcode());
        entity.setLotNo(StringUtils.EMPTY);
        entity.setHandleWay(info.getHandleWay());
        entity.setRemark(info.getRemark());

        pqsPartsScrapService.insert(entity);
    }

    /**
     * 获取评审明细
     *
     * @param info
     * @return
     */
    @Override
    public PageData<AuditDetailListDto> getQgPartAuditDetail(QgPartAuditDetailFilterInfo info) {

        List<PqsPartsScrapEntity> query = pqsPartsScrapService.getAllDatas()
                .stream().filter(p -> {
                    Boolean stationCode = p.getWorkstationCode().equals(info.getWorkstationCode());
                    Boolean keyFlag = info.getKey().equals(StringUtils.EMPTY) || p.getMaterialNo().contains(info.getKey()) || p.getMaterialName().contains(info.getKey()) || p.getInspectionNo().startsWith(info.getKey());
                    return stationCode && keyFlag;
                }).sorted(Comparator.comparing(PqsPartsScrapEntity::getQcDt)).collect(Collectors.toList());
        PageData<AuditDetailListDto> resultPage = new PageData<>();
        List<AuditDetailListDto> dtoList = Lists.newArrayList();
        query.forEach(q -> {
            AuditDetailListDto dto = new AuditDetailListDto();
            BeanUtils.copyProperties(q, dto);
            dto.setMaterialCn(q.getMaterialName());
            dto.setQty(q.getScrapQty());
            dtoList.add(dto);
        });

        resultPage.setPageSize(info.getPageSize());
        resultPage.setPageIndex(info.getPageIndex());
        resultPage.setDatas(dtoList);
        resultPage.setTotal(dtoList.size());

        return resultPage;
    }

    /**
     * 创建废料工单
     *
     * @param info
     */
    @Override
    public void createMMScrapEntry(CreateMMScrapInfo info) {

        String inspectionNo = sysSnConfigProvider.createSn(INSPECTION_NO);

        PqsMmScrapRecordEntity entity = new PqsMmScrapRecordEntity();
        BeanUtils.copyProperties(info, entity);
        entity.setInspectionNo(inspectionNo);

        pqsMmScrapRecordService.insert(entity);
    }

    /**
     * 获取料废录入信息
     *
     * @param info
     * @return
     */
    @Override
    public PageData<PqsMmScrapRecordEntity> getMMScrapDetail(GetMmScrapDataFilterInfo info) {

        PageData<PqsMmScrapRecordEntity> resultPage = new PageData<>();

        List<PqsMmScrapRecordEntity> resultLists = pqsMmScrapRecordService.getAllDatas().stream().filter(p -> p.getPlateNo().contains(info.getKey())
                || p.getSendUser().contains(info.getKey()) || p.getRecieveUser().contains(info.getKey())
        ).sorted(Comparator.comparing(PqsMmScrapRecordEntity::getRecordDt)).collect(Collectors.toList());

        resultPage.setPageSize(info.getPageSize());
        resultPage.setPageIndex(info.getPageIndex());
        resultPage.setDatas(resultLists);
        resultPage.setTotal(resultLists.size());

        return resultPage;
    }
}
