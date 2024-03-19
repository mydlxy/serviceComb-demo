package com.ca.mfd.prc.audit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.audit.dto.AuditActiveAnomalyInfo;
import com.ca.mfd.prc.audit.dto.AuditAnomalyStatusModifyRequest;
import com.ca.mfd.prc.audit.dto.AuditEntryDto;
import com.ca.mfd.prc.audit.dto.BatchAuditAnomalyStatusModifyRequest;
import com.ca.mfd.prc.audit.dto.DefectAnomalyDto;
import com.ca.mfd.prc.audit.dto.DefectAnomalyParaInfo;
import com.ca.mfd.prc.audit.dto.EntryParaInfo;
import com.ca.mfd.prc.audit.dto.GetAuditDefectAnomalyRequest;
import com.ca.mfd.prc.audit.dto.ModifyDefectResponsibleInfo;
import com.ca.mfd.prc.audit.dto.QgGateConfigurationInfo;
import com.ca.mfd.prc.audit.dto.QgMatrikConfigurationInfo;
import com.ca.mfd.prc.audit.entity.PqsAuditEntryAttchmentEntity;
import com.ca.mfd.prc.audit.entity.PqsAuditEntryEntity;
import com.ca.mfd.prc.audit.service.IPqsAuditEntryService;
import com.ca.mfd.prc.audit.service.IPqsAuditLogicService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 业务方法
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-08-13
 */
@RestController
@RequestMapping("pqsauditlogic")
@Tag(name = "AUDIT业务方法")
public class PqsAuditLogicController extends BaseController {


    @Autowired
    private IPqsAuditEntryService pqsAuditEntryService;

    @Autowired
    private IPqsAuditLogicService pqsAuditLogicService;


    @Operation(summary = "保存工单，status值1是创建、2是使用中、90是完成")
    @PostMapping("createentry")
    public ResultVO createEntry(@RequestBody AuditEntryDto dto) {
        return pqsAuditLogicService.createEntry(dto);
    }


    @Operation(summary = "根据key、状态、工位查询工单号，key可以是vin码、barcode、工单号")
    @PostMapping("getentrybystatus")
    public ResultVO getEntry(@RequestBody EntryParaInfo key) {
        return pqsAuditLogicService.getEntryByStatus(key);
    }

    @Operation(summary = "根据key查询工单号，key可以是vin码、barcode、工单号")
    @GetMapping("getentrybykey")
    public ResultVO getEntry(String key) {
        return pqsAuditLogicService.getEntry(key);
    }


    @Operation(summary = "根据工单号查询工单列表")
    @GetMapping("queryentrylist")
    public ResultVO queryEntryList(String recordNo) {
        if (StringUtils.isEmpty(recordNo)) {
            throw new InkelinkException("工单号为空");
        }
        QueryWrapper<PqsAuditEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PqsAuditEntryEntity::getRecordNo, recordNo);
        pqsAuditEntryService.getData(queryWrapper, false);
        return new ResultVO().ok(pqsAuditEntryService.getData(queryWrapper, false), "获取数据成功");
    }

    @Operation(summary = "工单提交")
    @GetMapping("submitentry")
    public ResultVO submitEntry(String recordNo, String remark) {
        if (StringUtils.isEmpty(recordNo)) {
            throw new InkelinkException("工单号为空");
        }
        return pqsAuditLogicService.submitEntry(recordNo, remark);
    }


    @GetMapping("getorderbycode")
    @Operation(summary = "根据条码获取订单信息")
    public ResultVO getOrderByVin(String code, Integer category) {
        return new ResultVO().ok(pqsAuditLogicService.getOrderByVin(code, category), "获取数据成功");
    }


    /**
     * 获取缺陷数据
     *
     * @param info
     * @return
     */
    @PostMapping("getanomalyshowlist")
    @Operation(summary = "audit获取缺陷数据")
    public ResultVO<PageData<DefectAnomalyDto>> getAuditAnomalyShowList(@RequestBody DefectAnomalyParaInfo info) {
        return new ResultVO<PageData<DefectAnomalyDto>>().ok(pqsAuditLogicService.getAuditAnomalyShowList(info), "获取数据成功");
    }

    /**
     * 获取产品缺陷列表
     *
     * @param request
     * @return
     */
    @PostMapping("getvehicledefectanomalylist")
    @Operation(summary = "根据工单号获取产品缺陷列表")
    public ResultVO getAuditVehicleDefectAnomalyList(@RequestBody GetAuditDefectAnomalyRequest request) {
        return pqsAuditLogicService.getAuditVehicleDefectAnomalyList(request);
    }


    @GetMapping("getattchmentbyrecordno")
    @Operation(summary = "根据工单号获取所有附件")
    public ResultVO<List<PqsAuditEntryAttchmentEntity>> getAttchmentbyRecordNo(String recordNo) {
        return pqsAuditLogicService.getAttchmentbyRecordNo(recordNo);
    }

    @Operation(summary = "audit保存附件")
    @PostMapping("saveattachment")
    public ResultVO saveAttachMent(@RequestBody PqsAuditEntryAttchmentEntity attchmentEntity) {
        pqsAuditLogicService.saveAttachMent(attchmentEntity);
        return new ResultVO().ok("", "操作成功");
    }

    @Operation(summary = "audit删除附件")
    @GetMapping("deleteattachment")
    public ResultVO deleteAttachment(Long id) {
        if (id == null) {
            throw new InkelinkException("附件主键不能为空");
        }
        pqsAuditLogicService.del(id);
        return new ResultVO().ok("", "操作成功");
    }


    /**
     * 手动激活缺陷
     *
     * @param info
     * @return
     */
    @PostMapping("audittriggeranomaly")
    @Operation(summary = "手动激活缺陷")
    public ResultVO auditTriggerAnomaly(@RequestBody AuditActiveAnomalyInfo info) {
        pqsAuditLogicService.activeAuditAnomaly(info);
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 获取缺陷等级
     *
     * @param
     * @return
     */
    @GetMapping("getgradecombo")
    @Operation(summary = "获取audit缺陷等级下拉")
    public ResultVO getAuditGradeCombo() {
        return new ResultVO<>().ok(pqsAuditLogicService.getGradeCombo(), "获取数据成功");
    }

    @GetMapping("getdeptcombo")
    @Operation(summary = "获取audit责任部门下拉")
    public ResultVO getAuditDeptCombo() {
        return new ResultVO<>().ok(pqsAuditLogicService.getDeptCombo(), "获取数据成功");
    }

    @GetMapping("getstagecombo")
    @Operation(summary = "获取audit阶段下拉")
    public ResultVO getAuditStageCombo() {
        return new ResultVO<>().ok(pqsAuditLogicService.getStageCombo(), "获取数据成功");
    }

    @GetMapping("getprojectcombo")
    @Operation(summary = "获取audit项目下拉")
    public ResultVO getAuditProjectCombo() {
        return new ResultVO<>().ok(pqsAuditLogicService.getProjectCombo(), "获取数据成功");
    }

    /**
     * OT屏后台更新缺陷责任区域
     *
     * @param info
     * @return
     */
    @PostMapping("modifydefectanomalyrepsponsibelinfo")
    @Operation(summary = "OT屏后台更新缺陷责任区域")
    public ResultVO modifyDefectAnomalyRepsponsibelInfo(@RequestBody ModifyDefectResponsibleInfo info) {
        pqsAuditLogicService.modifyDefectAnomalyRepsponsibelInfo(info);
        return new ResultVO<String>().ok("", "操作成功");
    }

    @PostMapping("modifydefectanomalystatus")
    @Operation(summary = "缺陷活动")
    public ResultVO modifyDefectAnomalyStatus(@RequestBody AuditAnomalyStatusModifyRequest anomalyStatusModifyRequest) {
        pqsAuditLogicService.modifyDefectAnomalyStatus(anomalyStatusModifyRequest);
        pqsAuditLogicService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @PostMapping("batchmodifydefectanomalystatus")
    @Operation(summary = "批量缺陷操作")
    public ResultVO batchModifyDefectAnomalyStatus(@RequestBody BatchAuditAnomalyStatusModifyRequest anomalyStatusModifyRequest) {
        if (CollectionUtils.isEmpty(anomalyStatusModifyRequest.getIds())) {
            throw new InkelinkException("主键不能为空");
        }
        anomalyStatusModifyRequest.getIds().stream().forEach(x -> {
            AuditAnomalyStatusModifyRequest request = new AuditAnomalyStatusModifyRequest();
            request.setDataId(x);
            request.setStatus(anomalyStatusModifyRequest.getStatus());
            request.setRepairActivity(anomalyStatusModifyRequest.getRepairActivity());
            pqsAuditLogicService.modifyDefectAnomalyStatus(request);
        });
        pqsAuditLogicService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }


    /**
     * 根据工位代码和车型获取百格图
     *
     * @param workstationCode
     * @param model
     * @return
     */
    @GetMapping("getqualitymatrikbyworkstationcode")
    @Operation(summary = "根据工位代码和车型获取百格图")
    public ResultVO getQualityMatrikByWorkstationCode(@RequestParam String workstationCode, @RequestParam String model) {
        return new ResultVO<>().ok(pqsAuditLogicService.getQualityMatrikByWorkstationCode(workstationCode, model), "获取数据成功");
    }

    /**
     * QG岗查看质量门检查图片数据
     *
     * @param qualityMatrikId 百格图ID
     * @param recordNo          工单号
     * @return
     */
    @GetMapping("showqgmatrikdata")
    @Operation(summary = "QG岗查看质量门检查图片数据")
    public ResultVO showQGMatrikData(@RequestParam Long qualityMatrikId, @RequestParam String recordNo) {
        return new ResultVO<>().ok(pqsAuditLogicService.showQgMatrikDataByRecordNo(qualityMatrikId, recordNo), "获取数据成功");
    }

    /**
     * audit-根据工位代码和车型获取
     *
     * @param workstationCode
     * @param model
     * @return
     */
    @GetMapping("getqualitygatebyworkstationcode")
    @Operation(summary = "audit-根据工位代码和车型获取")
    public ResultVO getQualityGateByWorkstationCode(@RequestParam String workstationCode, @RequestParam String model) {
        return new ResultVO<>().ok(pqsAuditLogicService.getQualityGateByWorkstationCode(workstationCode, model), "获取数据成功");
    }


    /**
     * audit-获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId
     * @param recordNo
     * @return
     */
    @GetMapping("getgateanomalybygateblankidandrecordno")
    @Operation(summary = "audit-获取质量门色块对应的缺陷列表")
    public ResultVO getGateAnomalyByGateBlankIdAndRecordNo(@RequestParam Long qualityGateBlankId, @RequestParam String recordNo) {
        return new ResultVO<>().ok(pqsAuditLogicService.getGateAnomalyByGateBlankIdAndRecordNo(qualityGateBlankId, recordNo), "获取数据成功");
    }


    /**
     * audit-获取QG配置数据信息
     *
     * @param qualityGateId
     * @return
     */
    @GetMapping("getqggatedata")
    @Operation(summary = "audit-获取QG配置数据信息")
    public ResultVO getQgGateData(@RequestParam Long qualityGateId) {
        return new ResultVO<>().ok(pqsAuditLogicService.getQGGateData(qualityGateId), "获取数据成功");
    }

    /**
     * audit-获取QG百格图配置数据信息
     *
     * @param qualityMatrikId
     * @return
     */
    @GetMapping("getqmatrikdata")
    @Operation(summary = "audit-获取QG百格图配置数据信息")
    public ResultVO getQGMatrikData(@RequestParam Long qualityMatrikId) {
        return new ResultVO<>().ok(pqsAuditLogicService.getQGMatrikData(qualityMatrikId), "获取数据成功");
    }

    /**
     * audit-提交QG百格图数据信息
     *
     * @param info
     * @return
     */
    @PostMapping("submitqgmatrikdata")
    @Operation(summary = "audit-提交QG百格图数据信息")
    public ResultVO submitQgMatrikData(@RequestBody QgMatrikConfigurationInfo info) {
        pqsAuditLogicService.submitQualityMatrikData(info);
        pqsAuditLogicService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * audit-提交QG配置数据信息
     *
     * @param info
     * @return
     */
    @PostMapping("submitqggatedata")
    @Operation(summary = "提交QG配置数据信息")
    public ResultVO submitQGGateData(@RequestBody QgGateConfigurationInfo info) {
        pqsAuditLogicService.submitQGGateData(info);
        pqsAuditLogicService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }
}