package com.ca.mfd.prc.audit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.audit.dto.AuditActiveAnomalyInfo;
import com.ca.mfd.prc.audit.dto.AuditAnomalyStatusModifyRequest;
import com.ca.mfd.prc.audit.dto.AuditEntryDto;
import com.ca.mfd.prc.audit.dto.BatchAuditAnomalyStatusModifyRequest;
import com.ca.mfd.prc.audit.dto.DefectAnomalyDto;
import com.ca.mfd.prc.audit.dto.DefectAnomalyParaInfo;
import com.ca.mfd.prc.audit.dto.EntryParaInfo;
import com.ca.mfd.prc.audit.dto.ExQgGateConfigurationInfo;
import com.ca.mfd.prc.audit.dto.GetAuditDefectAnomalyRequest;
import com.ca.mfd.prc.audit.dto.ModifyDefectResponsibleInfo;
import com.ca.mfd.prc.audit.dto.QgMatrikConfigurationInfo;
import com.ca.mfd.prc.audit.entity.PqsExEntryAttchmentEntity;
import com.ca.mfd.prc.audit.entity.PqsExEntryEntity;
import com.ca.mfd.prc.audit.service.IPqsExEntryService;
import com.ca.mfd.prc.audit.service.IPqsExLogicService;
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
 * @author inkelink
 * @Description: 精致工艺业务方法Controller
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@RestController
@RequestMapping("pqsexlogic")
@Tag(name = "精致工艺业务方法")
public class PqsExLogicController extends BaseController {


    @Autowired
    private IPqsExEntryService pqsExEntryService;

    @Autowired
    private IPqsExLogicService pqsExLogicService;


    @Operation(summary = "保存工单，status值1是创建、2是使用中、90是完成")
    @PostMapping("createentry")
    public ResultVO createEntry(@RequestBody AuditEntryDto dto) {
        return pqsExLogicService.createEntry(dto);
    }


    @Operation(summary = "根据key、状态、工位查询工单号，key可以是vin码、barcode、工单号")
    @PostMapping("getentrybystatus")
    public ResultVO getEntry(@RequestBody EntryParaInfo key) {
        return pqsExLogicService.getEntryByStatus(key);
    }

    @Operation(summary = "根据key查询工单号，key可以是vin码、barcode、工单号")
    @GetMapping("getentrybykey")
    public ResultVO getEntry(String key) {
        return pqsExLogicService.getEntry(key);
    }


    @Operation(summary = "根据工单号查询工单列表")
    @GetMapping("queryentrylist")
    public ResultVO queryEntryList(String recordNo) {
        if (StringUtils.isEmpty(recordNo)) {
            throw new InkelinkException("工单号为空");
        }
        QueryWrapper<PqsExEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PqsExEntryEntity::getRecordNo, recordNo);
        pqsExEntryService.getData(queryWrapper, false);
        return new ResultVO().ok(pqsExEntryService.getData(queryWrapper, false), "获取数据成功");
    }

    @Operation(summary = "工单提交")
    @GetMapping("submitentry")
    public ResultVO submitEntry(String recordNo, String remark) {
        if (StringUtils.isEmpty(recordNo)) {
            throw new InkelinkException("工单号为空");
        }
        return pqsExLogicService.submitEntry(recordNo, remark);
    }


    @GetMapping("getorderbycode")
    @Operation(summary = "根据条码获取订单信息")
    public ResultVO getOrderByVin(String code, Integer category) {
        return new ResultVO().ok(pqsExLogicService.getOrderByVin(code, category), "获取数据成功");
    }


    /**
     * 获取缺陷数据
     *
     * @param info
     * @return
     */
    @PostMapping("getanomalyshowlist")
    @Operation(summary = "精致工艺-获取缺陷数据")
    public ResultVO<PageData<DefectAnomalyDto>> getExAnomalyShowList(@RequestBody DefectAnomalyParaInfo info) {
        return new ResultVO<PageData<DefectAnomalyDto>>().ok(pqsExLogicService.getExAnomalyShowList(info), "获取数据成功");
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
        return pqsExLogicService.getAuditVehicleDefectAnomalyList(request);
    }

    @GetMapping("getattchmentbyrecordno")
    @Operation(summary = "根据工单号获取所有附件")
    public ResultVO<List<PqsExEntryAttchmentEntity>> getAttchmentbyRecordNo(String recordNo) {
        return pqsExLogicService.getAttchmentbyRecordNo(recordNo);
    }

    @Operation(summary = "audit保存附件")
    @PostMapping("saveattachment")
    public ResultVO saveAttachMent(@RequestBody PqsExEntryAttchmentEntity attchmentEntity) {
        pqsExLogicService.saveAttachMent(attchmentEntity);
        return new ResultVO().ok("", "操作成功");
    }

    @Operation(summary = "audit删除附件")
    @GetMapping("deleteattachment")
    public ResultVO deleteAttachment(Long id) {
        if (id == null) {
            throw new InkelinkException("附件主键不能为空");
        }
        pqsExLogicService.del(id);
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
        pqsExLogicService.activeAuditAnomaly(info);
        return new ResultVO<String>().ok("", "操作成功");
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
        pqsExLogicService.modifyDefectAnomalyRepsponsibelInfo(info);
        return new ResultVO<String>().ok("", "操作成功");
    }

    @PostMapping("modifydefectanomalystatus")
    @Operation(summary = "缺陷活动")
    public ResultVO modifyDefectAnomalyStatus(@RequestBody AuditAnomalyStatusModifyRequest anomalyStatusModifyRequest) {
        pqsExLogicService.modifyDefectAnomalyStatus(anomalyStatusModifyRequest);
        pqsExLogicService.saveChange();
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
            pqsExLogicService.modifyDefectAnomalyStatus(request);
        });
        pqsExLogicService.saveChange();
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
        return new ResultVO<>().ok(pqsExLogicService.getQualityMatrikByWorkstationCode(workstationCode, model), "获取数据成功");
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
        return new ResultVO<>().ok(pqsExLogicService.showQgMatrikDataByRecordNo(qualityMatrikId, recordNo), "获取数据成功");
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
        return new ResultVO<>().ok(pqsExLogicService.getQualityGateByWorkstationCode(workstationCode, model), "获取数据成功");
    }


    /**
     * 精致工艺-获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId
     * @param recordNo
     * @return
     */
    @GetMapping("getgateanomalybygateblankidandrecordno")
    @Operation(summary = "精致工艺-获取质量门色块对应的缺陷列表")
    public ResultVO getGateAnomalyByGateBlankIdAndRecordNo(@RequestParam Long qualityGateBlankId, @RequestParam String recordNo) {
        return new ResultVO<>().ok(pqsExLogicService.getGateAnomalyByGateBlankIdAndRecordNo(qualityGateBlankId, recordNo), "获取数据成功");
    }


    /**
     * 精致工艺-获取QG配置数据信息
     *
     * @param qualityGateId
     * @return
     */
    @GetMapping("getqggatedata")
    @Operation(summary = "精致工艺-获取QG配置数据信息")
    public ResultVO getQgGateData(@RequestParam Long qualityGateId) {
        return new ResultVO<>().ok(pqsExLogicService.getQGGateData(qualityGateId), "获取数据成功");
    }

    /**
     * 精致工艺-获取QG百格图配置数据信息
     *
     * @param qualityMatrikId
     * @return
     */
    @GetMapping("getqmatrikdata")
    @Operation(summary = "精致工艺-获取QG百格图配置数据信息")
    public ResultVO getQGMatrikData(@RequestParam Long qualityMatrikId) {
        return new ResultVO<>().ok(pqsExLogicService.getQGMatrikData(qualityMatrikId), "获取数据成功");
    }

    /**
     * 精致工艺-提交QG百格图数据信息
     *
     * @param info
     * @return
     */
    @PostMapping("submitqgmatrikdata")
    @Operation(summary = "精致工艺-提交QG百格图数据信息")
    public ResultVO submitQgMatrikData(@RequestBody QgMatrikConfigurationInfo info) {
        pqsExLogicService.submitQualityMatrikData(info);
        pqsExLogicService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 精致工艺-提交QG配置数据信息
     *
     * @param info
     * @return
     */
    @PostMapping("submitqggatedata")
    @Operation(summary = "提交QG配置数据信息")
    public ResultVO submitQGGateData(@RequestBody ExQgGateConfigurationInfo info) {
        pqsExLogicService.submitQGGateData(info);
        pqsExLogicService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }
}