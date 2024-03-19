package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.pqs.dto.ActiveAnomalyInfo;
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
import com.ca.mfd.prc.pqs.entity.PqsMmScrapRecordEntity;

import java.util.List;

/**
 * @author qu
 * @date 2023/9/17
 */
public interface IPqsPartsLogicService {

    // ====================================================质量门功能=====================================================
    /**
     * 根据工位代码获取常用缺陷
     *
     * @param workstationCode 工位代码
     * @param sn 产品唯一码
     * @return
     */
    List<DefectAnomalyDto> getAnomalyWpListBySn(String workstationCode, String sn);

    /**
     * 根据工位代码+工单号获取常用缺陷
     *
     * @param workstationCode 工位代码
     * @param inspectionNo 工单号
     * @return
     */
    List<DefectAnomalyDto> getAnomalyWpListByInspectionNo(String workstationCode, String inspectionNo);

    /**
     * 根据产品唯一码获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 色块ID
     * @param sn 产品唯一码
     * @return
     */
    List<DefectAnomalyDto> getGateAnomalyByGateBlankIdAndSn(Long qualityGateBlankId, String sn);

    /**
     * 根据评审工单号获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 色块ID
     * @param inspectionNo 评审工单号
     * @return
     */
    List<DefectAnomalyDto> getGateAnomalyByGateBlankIdAndInspectionNo(Long qualityGateBlankId, String inspectionNo);

    /**
     * QG岗根据产品唯一码查看质量门检查图片数据
     *
     * @param qualityMatrikId 百格图ID
     * @param sn 产品唯一码
     * @return
     */
    ShowQgMatrikDto showQGMatrikDataBySn(Long qualityMatrikId, String sn);

    /**
     * QG岗根据评审工单号查看质量门检查图片数据
     *
     * @param qualityMatrikId 百格图ID
     * @param inspectionNo 评审工单号
     * @return
     */
    ShowQgMatrikDto showQGMatrikDataByInspectionNo(Long qualityMatrikId, String inspectionNo);

    /**
     * QG岗查围堵缺陷信息
     *
     * @param sn 产品唯一码
     * @return
     */
    List<QgRiskDto> showRiskDataBySn(String sn);

    /**
     * 风险管理
     *
     * @param info
     */
    void riskManager(QgRiskOperInfo info);

    /**
     * 激活缺陷
     *
     * @param info
     */
    void activeAnomaly(ActiveAnomalyInfo info);

    /**
     * 后台更新缺陷责任区域
     *
     * @param info
     */
    void modifyDefectAnomalyRepsponsibelInfo(ModifyDefectResponsibleInfo info);

    /**
     * 获取缺陷日志
     *
     * @param anomalyId
     * @return
     */
    List<ProductAnomalyLogDto> getProductDefectAnomalyLog(Long anomalyId);

    /**
     * 缺陷活动--返修复检
     *
     * @param statusModifyInfo
     */
    void modifyDefectAnomalyStatus(AnomalyStatusModifyInfo statusModifyInfo);

    /**
     * 添加激活缺陷备注
     *
     * @param dataId
     * @param remark
     */
    void appendDefectAnomalyRemark(Long dataId, String remark);

    /**
     * 获取已激活的缺陷列表
     *
     * @param para 条件参数
     * @return 缺陷列表
     */
    PageData<ProductDefectAnomalyReponse> getVehicleDefectAnomalyList(GetDefectAnomalyRequest para);

    /**
     * 创建评审单
     *
     * @param info
     */
    void createAuditByQgWorkstation(CreateQgAuditInfo info);

    /**
     * 获取评审明细
     *
     * @param info
     * @return
     */
    PageData<AuditDetailListDto> getQgPartAuditDetail(QgPartAuditDetailFilterInfo info);

    /**
     * 创建废料工单
     *
     * @param info
     */
    void createMMScrapEntry(CreateMMScrapInfo info);

    /**
     * 获取料废录入信息
     *
     * @param info
     * @return
     */
    PageData<PqsMmScrapRecordEntity> getMMScrapDetail(GetMmScrapDataFilterInfo info);
}
