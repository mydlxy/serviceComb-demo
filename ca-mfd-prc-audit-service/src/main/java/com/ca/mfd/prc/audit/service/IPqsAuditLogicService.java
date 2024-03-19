package com.ca.mfd.prc.audit.service;

import com.ca.mfd.prc.audit.dto.AuditActiveAnomalyInfo;
import com.ca.mfd.prc.audit.dto.AuditAnomalyStatusModifyRequest;
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
import com.ca.mfd.prc.audit.entity.PqsAuditEntryAttchmentEntity;
import com.ca.mfd.prc.common.model.base.dto.ComboDataDTO;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;

import java.util.List;

/**
 * @author eric.zhou
 * @date 2023/4/17
 */
public interface IPqsAuditLogicService {


    void saveChange();

    PageData<DefectAnomalyDto> getAuditAnomalyShowList(DefectAnomalyParaInfo info);

    ResultVO getAuditVehicleDefectAnomalyList(GetAuditDefectAnomalyRequest request);

    ResultVO<List<PqsAuditEntryAttchmentEntity>> getAttchmentbyRecordNo(String recordNo);

    void saveAttachMent(PqsAuditEntryAttchmentEntity attchmentEntity);

    void del(Long id);

    void activeAuditAnomaly(AuditActiveAnomalyInfo info);

    List<ComboDataDTO> getGradeCombo();

    List<ComboDataDTO> getDeptCombo();

    List<ComboDataDTO> getStageCombo();

    List<ComboDataDTO> getProjectCombo();

    ResultVO getEntryByStatus(EntryParaInfo key);

    ResultVO getEntry(String key);

    /**
     * 创建audit工单
     *
     * @param dto
     * @return
     */
    ResultVO createEntry(AuditEntryDto dto);

    /**
     * 整车-根据vin码或者条码获取订单
     *
     * @param code
     * @param category
     * @return
     */
    OrderVo getOrderByVin(String code, Integer category);

    ResultVO submitEntry(String recordNo, String remark);

    void modifyDefectAnomalyRepsponsibelInfo(ModifyDefectResponsibleInfo info);

    void modifyDefectAnomalyStatus(AuditAnomalyStatusModifyRequest anomalyStatusModifyRequest);

    /**
     * 根据工位代码和车型获取
     *
     * @param workstationCode 工位
     * @param model           车型
     * @return
     */
    List<ComboInfoDTO> getQualityMatrikByWorkstationCode(String workstationCode, String model);

    /**
     * QG岗根据产品唯一码查看质量门检查图片数据
     *
     * @param qualityMatrikId 百格图ID
     * @param recordNo 工单号
     * @return
     */
    ShowQgMatrikDto showQgMatrikDataByRecordNo(Long qualityMatrikId, String recordNo);
    /**
     * 根据工位代码和车型获取
     *
     * @param workstationCode
     * @param model
     * @return
     */
    List<ComboInfoDTO> getQualityGateByWorkstationCode(String workstationCode, String model);

    /**
     * 获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 色块ID
     * @param recordNo          工单号
     * @return
     */
    List<DefectAnomalyDto>  getGateAnomalyByGateBlankIdAndRecordNo(Long qualityGateBlankId, String recordNo);

    /**
     * 获取QG配置数据信息
     *
     * @param qualityGateId
     * @return
     */
    QgGateConfigurationInfo getQGGateData(Long qualityGateId);

    /**
     * 提交百格图配置数据信息
     *
     * @param info
     */
    void submitQualityMatrikData(QgMatrikConfigurationInfo info);

    /**
     * 获取QG百格图配置数据信息
     *
     * @param qualityMatrikId
     * @return
     */
    QgMatrikConfigurationInfo getQGMatrikData(Long qualityMatrikId);

    void submitQGGateData(QgGateConfigurationInfo info);
}
