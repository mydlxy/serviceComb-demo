package com.ca.mfd.prc.pqs.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdModel;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.ActiveAnomalyInfo;
import com.ca.mfd.prc.pqs.dto.AnomalyStatusModifyInfo;
import com.ca.mfd.prc.pqs.dto.AttchmentDto;
import com.ca.mfd.prc.pqs.dto.AuditDetailListDto;
import com.ca.mfd.prc.pqs.dto.AviListInfo;
import com.ca.mfd.prc.pqs.dto.CreateMMScrapInfo;
import com.ca.mfd.prc.pqs.dto.CreateQgAuditInfo;
import com.ca.mfd.prc.pqs.dto.DefectAnomalyDto;
import com.ca.mfd.prc.pqs.dto.DefectAnomalyParaInfo;
import com.ca.mfd.prc.pqs.dto.GetDefectAnomalyRequest;
import com.ca.mfd.prc.pqs.dto.GetMmScrapDataFilterInfo;
import com.ca.mfd.prc.pqs.dto.InitCheckItemModel;
import com.ca.mfd.prc.pqs.dto.MaterialInfo;
import com.ca.mfd.prc.pqs.dto.ModifyDefectResponsibleInfo;
import com.ca.mfd.prc.pqs.dto.PqsEntryCheckItemDto;
import com.ca.mfd.prc.pqs.dto.ProductAnomalyLogDto;
import com.ca.mfd.prc.pqs.dto.ProductDefectAnomalyReponse;
import com.ca.mfd.prc.pqs.dto.QgAnomalyAppendRemarkInfo;
import com.ca.mfd.prc.pqs.dto.QgCheckItemInfo;
import com.ca.mfd.prc.pqs.dto.QgGateConfigurationInfo;
import com.ca.mfd.prc.pqs.dto.QgMatrikConfigurationInfo;
import com.ca.mfd.prc.pqs.dto.QgPartAuditDetailFilterInfo;
import com.ca.mfd.prc.pqs.dto.QgRiskDto;
import com.ca.mfd.prc.pqs.dto.QgRiskOperInfo;
import com.ca.mfd.prc.pqs.dto.QgSetProductRouteInfo;
import com.ca.mfd.prc.pqs.dto.RestCheckItemModel;
import com.ca.mfd.prc.pqs.dto.RiskRepairInfo;
import com.ca.mfd.prc.pqs.dto.ShowQgMatrikDto;
import com.ca.mfd.prc.pqs.dto.TemplateFilter;
import com.ca.mfd.prc.pqs.entity.PqsMmScrapRecordEntity;
import com.ca.mfd.prc.pqs.remote.app.pm.entity.PmWorkstationMaterialEntity;
import com.ca.mfd.prc.pqs.remote.app.pm.provider.PmWorkStationMaterialProvider;
import com.ca.mfd.prc.pqs.service.IPqsEntryAttchmentService;
import com.ca.mfd.prc.pqs.service.IPqsEntryCheckItemService;
import com.ca.mfd.prc.pqs.service.IPqsInspectionTemplateService;
import com.ca.mfd.prc.pqs.service.IPqsLogicService;
import com.ca.mfd.prc.pqs.service.IPqsPartsLogicService;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 业务方法
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-08-13
 */
@RestController
@RequestMapping("pqspartslogic")
@Tag(name = "业务方法")
public class PqsPartsLogicController extends BaseController {

    @Autowired
    private IPqsPartsLogicService pqsPartsLogicService;
    @Autowired
    private PmWorkStationMaterialProvider pmWorkStationMaterialProvider;
    @Autowired
    private IPqsLogicService pqsLogicService;
    @Autowired
    private IPqsInspectionTemplateService pqsInspectionTemplateService;
    @Autowired
    private IPqsEntryCheckItemService pqsEntryCheckItemService;
    @Autowired
    private IPqsEntryAttchmentService pqsEntryAttchmentService;

    // ========================================缺陷基础数据 不含缺陷库 开始==================================================

    /**
     * 获取所有组件
     *
     * @param
     * @return
     */
    @GetMapping("getallcomponent")
    @Operation(summary = "获取所有组件")
    public ResultVO getAllComponent() {
        return new ResultVO<>().ok(pqsLogicService.getAllComponent(), "获取数据成功");
    }

    /**
     * 获取所有方位
     *
     * @param
     * @return
     */
    @GetMapping("getallposition")
    @Operation(summary = "获取所有方位")
    public ResultVO getAllPosition() {
        return new ResultVO<>().ok(pqsLogicService.getAllPosition(), "获取数据成功");
    }

    /**
     * 获取所有缺陷
     *
     * @param
     * @return
     */
    @GetMapping("getalldefectcode")
    @Operation(summary = "获取所有缺陷")
    public ResultVO getAllDefectCode() {
        return new ResultVO<>().ok(pqsLogicService.getAllDefectCode(), "获取数据成功");
    }

    /**
     * 获取缺陷数据中组件列表
     *
     * @param key
     * @return
     */
    @GetMapping("getanomalycomponent")
    @Operation(summary = "获取缺陷数据中组件列表")
    public ResultVO<List<ComboInfoDTO>> getAnomalyComponent(@RequestParam String key) {
        if (key == null) {
            key = "";
        }
        return new ResultVO<List<ComboInfoDTO>>().ok(pqsLogicService.getAnomalyComponent(key), "获取数据成功");
    }

    /**
     * 根据组件代码获取缺陷数据中位置列表
     *
     * @param componentCode
     * @return
     */
    @GetMapping("getanomalyposition")
    @Operation(summary = "根据组件代码获取缺陷数据中位置列表")
    public ResultVO<List<ComboInfoDTO>> getanomalyposition(@RequestParam String componentCode) {
        if (componentCode == null) {
            componentCode = "";
        }
        return new ResultVO<List<ComboInfoDTO>>().ok(pqsLogicService.getAnomalyPosition(componentCode), "获取数据成功");
    }

    /**
     * 获取责任等级
     *
     * @param
     * @return
     */
    @GetMapping("getgradecombo")
    @Operation(summary = "获取责任等级")
    public ResultVO getGradeCombo() {
        return new ResultVO<>().ok(pqsLogicService.getGradeCombo(), "获取数据成功");
    }

    /**
     * 获取责任部门
     *
     * @param
     * @return
     */
    @GetMapping("getdeptcombo")
    @Operation(summary = "获取责任部门")
    public ResultVO getDeptCombo() {
        return new ResultVO<>().ok(pqsLogicService.getDeptCombo(), "获取数据成功");
    }

    // ==================================缺陷组合信息 --缺陷库、常用缺陷、百格图、QG检查项======================================

    /**
     * 获取缺陷数据
     *
     * @param info
     * @return
     */
    @PostMapping("getanomalyshowlist")
    @Operation(summary = "获取缺陷数据")
    public ResultVO<PageData<DefectAnomalyDto>> getWorkPlaceList(@RequestBody DefectAnomalyParaInfo info) {
        return new ResultVO<PageData<DefectAnomalyDto>>().ok(pqsLogicService.getAnomalyShowList(info), "获取数据成功");
    }

    /**
     * 获取所有缺陷库数据
     *
     * @param allitem
     * @return
     */
    @GetMapping("getallanomalyshowlist")
    @Operation(summary = "获取所有缺陷库数据")
    public ResultVO getAllAnomalyShowList(@RequestParam Boolean allitem) {
        if (allitem == null) {
            allitem = false;
        }
        List<DefectAnomalyDto> allAnomalyShowList = pqsLogicService.getAllAnomalyShowList();
        if (allitem) {
            DefectAnomalyDto info = new DefectAnomalyDto();
            info.setDefectAnomalyCode("000000");
            info.setDefectAnomalyDescription("所有缺陷");
            allAnomalyShowList.add(info);
        }

        return new ResultVO<>().ok(allAnomalyShowList, "获取数据成功");
    }

    /**
     * 根据工位代码获取常用缺陷
     *
     * @param workstationCode
     * @return
     */
    @GetMapping("getanomalywplist")
    @Operation(summary = "根据工位代码获取常用缺陷")
    public ResultVO getAnomalyWpList(@RequestParam String workstationCode) {
        return new ResultVO<>().ok(pqsLogicService.getAnomalyWpList(workstationCode), "获取数据成功");
    }

    /**
     * 根据组件代码和位置代码获取缺陷数据中分类列表
     *
     * @param componentCode
     * @param positionCode
     * @return
     */
    @GetMapping("getanomalycode")
    @Operation(summary = "根据组件代码和位置代码获取缺陷数据中分类列表")
    public ResultVO<List<Map<String, String>>> getAnomalyCode(@RequestParam String componentCode, @RequestParam String positionCode) {
        if (componentCode == null) {
            componentCode = "";
        }
        if (positionCode == null) {
            positionCode = "";
        }
        return new ResultVO<List<Map<String, String>>>().ok(pqsLogicService.getAnomalyCode(componentCode, positionCode)
                .stream().map(o -> {
                    Map<String, String> map = new HashMap<>(3);
                    map.put("text", o.getText().substring(o.getText().lastIndexOf("_") + 1));
                    map.put("value", o.getValue());
                    map.put("groupName", o.getText().substring(0, o.getText().lastIndexOf("_")));
                    return map;
                }).collect(Collectors.toList()), "获取数据成功");
    }

    /**
     * 获取QG配置数据信息
     *
     * @param qualityGateId
     * @return
     */
    @GetMapping("getqggatedata")
    @Operation(summary = "获取QG配置数据信息")
    public ResultVO getQgGateData(@RequestParam Long qualityGateId) {
        return new ResultVO<>().ok(pqsLogicService.getQGGateData(qualityGateId), "获取数据成功");
    }

    /**
     * 提交QG配置数据信息
     *
     * @param info
     * @return
     */
    @PostMapping("submitqggatedata")
    @Operation(summary = "提交QG配置数据信息")
    public ResultVO submitQGGateData(@RequestBody QgGateConfigurationInfo info) {
        pqsLogicService.submitQGGateData(info);
        pqsEntryCheckItemService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 获取QG百格图配置数据信息
     *
     * @param qualityMatrikId
     * @return
     */
    @GetMapping("getqmatrikdata")
    @Operation(summary = "获取QG百格图配置数据信息")
    public ResultVO getQGMatrikData(@RequestParam Long qualityMatrikId) {
        return new ResultVO<>().ok(pqsLogicService.getQGMatrikData(qualityMatrikId), "获取数据成功");
    }

    /**
     * 提交QG百格图数据信息
     *
     * @param info
     * @return
     */
    @PostMapping("submitqgmatrikdata")
    @Operation(summary = "提交QG百格图数据信息")
    public ResultVO submitQgMatrikData(@RequestBody QgMatrikConfigurationInfo info) {
        pqsLogicService.submitQualityMatrikData(info);
        pqsEntryCheckItemService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    // ================================================检验项目相关操作====================================================

    /**
     * 获取检验模板
     *
     * @param filter 检验模板筛选
     * @return
     */
    @PostMapping("gettemplatelist")
    @Operation(summary = "获取检验模板")
    public ResultVO<List<ComboInfoDTO>> getTemplateList(@RequestBody TemplateFilter filter) {
        return new ResultVO<List<ComboInfoDTO>>()
                .ok(pqsInspectionTemplateService.getTemplateList(filter.getEntryType(), filter.getMaterialNo(), filter.getProcessCode()), "获取数据成功！");
    }

    /**
     * 重置所有检验项目（删除）
     *
     * @param restCheckItemModel 工单号
     * @return
     */
    @PostMapping("restcheckitem")
    @Operation(summary = "获取检验模板")
    public ResultVO<List<PqsEntryCheckItemDto>> restCheckItem(@RequestBody RestCheckItemModel restCheckItemModel) {
        List<PqsEntryCheckItemDto> data = pqsEntryCheckItemService.restCheckItem(restCheckItemModel.getInspectNo());
        pqsEntryCheckItemService.saveChange();
        return new ResultVO<List<PqsEntryCheckItemDto>>()
                .ok(data, "获取数据成功！");
    }

    /**
     * 初始化检验项目
     *
     * @param initCheckItemModel 工单号
     * @return
     */
    @PostMapping("initcheckitem")
    @Operation(summary = "初始化检验项目")
    public ResultVO<List<PqsEntryCheckItemDto>> initCheckItem(@RequestBody InitCheckItemModel initCheckItemModel) {
        List<PqsEntryCheckItemDto> data = pqsEntryCheckItemService.initCheckItem(initCheckItemModel.getInspectionNo(), initCheckItemModel.getTemplateId());
        if (CollectionUtil.isEmpty(data)) {
            throw new InkelinkException("检验模板不包含检验项目！");
        }
        pqsEntryCheckItemService.saveChange();
        return new ResultVO<List<PqsEntryCheckItemDto>>().ok(data, "获取数据成功！");
    }

    /**
     * 删除检验项目
     *
     * @param idsModel 检验项目ID
     * @return
     */
    @PostMapping("deletecheckitem")
    @Operation(summary = "初始化检验项目")
    public ResultVO<String> deleteCheckItem(@RequestBody IdsModel idsModel) {

        List<String> ids = Arrays.stream(idsModel.getIds()).collect(Collectors.toList());
        ids.forEach(id -> {
            pqsEntryCheckItemService.deleteCheckItem(id);
        });
        pqsEntryCheckItemService.saveChange();

        return new ResultVO<String>().ok("操作成功！");
    }

    /**
     * 获取检验单对应的检验项
     *
     * @param inspectionNo 工单号
     * @return
     */
    @GetMapping("getcheckitem")
    @Operation(summary = "获取检验单对应的检验项")
    public ResultVO<List<PqsEntryCheckItemDto>> getCheckItem(String inspectionNo) {
        return new ResultVO<List<PqsEntryCheckItemDto>>()
                .ok(pqsEntryCheckItemService.getCheckItem(inspectionNo), "获取数据成功！");
    }

    /**
     * 保存检验项填报记录
     *
     * @param checkItemDtos
     * @return
     */
    @PostMapping("savecheckitem")
    @Operation(summary = "保存检验项填报记录")
    public ResultVO<List<PqsEntryCheckItemDto>> saveCheckItem(@RequestBody List<PqsEntryCheckItemDto> checkItemDtos) {
        List<PqsEntryCheckItemDto> data = pqsEntryCheckItemService.saveCheckItem(checkItemDtos);
        pqsEntryCheckItemService.saveChange();
        return new ResultVO<List<PqsEntryCheckItemDto>>().ok(data, "保存数据成功！");
    }

    // ====================================================附件管理=======================================================

    /**
     * 获取附件列表
     *
     * @param inspectionNo 附件
     * @return
     */
    @GetMapping("getattachment")
    @Operation(summary = "获取附件列表")
    public ResultVO<List<AttchmentDto>> getAttachMent(String inspectionNo) {
        return new ResultVO<List<AttchmentDto>>()
                .ok(pqsEntryAttchmentService.getAttachMent(inspectionNo), "获取数据成功！");
    }

    /**
     * 保存附件
     *
     * @param attchmentDto 附件
     * @return
     */
    @PostMapping("saveattachment")
    @Operation(summary = "保存附件")
    public ResultVO<String> saveAttachMent(@RequestBody AttchmentDto attchmentDto) {
        pqsEntryAttchmentService.saveAttachMent(attchmentDto);
        pqsEntryAttchmentService.saveChange();
        return new ResultVO<String>().ok("", "操作成功！");
    }

    /**
     * 删除附件
     *
     * @param info 附件ID
     * @return
     */
    @PostMapping("deleteattachment")
    @Operation(summary = "删除附件")
    public ResultVO<String> deleteAttachMent(@RequestBody AviListInfo info) {
        pqsEntryAttchmentService.deleteAttachMent(info.getId());
        pqsEntryAttchmentService.saveChange();
        return new ResultVO<String>().ok("", "操作成功！");
    }

    // ====================================================质量门功能=====================================================

    /**
     * 根据工位代码、唯一码获取常用缺陷
     *
     * @param workstationCode
     * @param sn
     * @return
     */
    @GetMapping("getanomalywplistbysn")
    @Operation(summary = "根据工位代码、唯一码获取常用缺陷")
    public ResultVO<List<DefectAnomalyDto>> getAnomalyWpListBySn(String workstationCode, String sn) {
        return new ResultVO<List<DefectAnomalyDto>>()
                .ok(pqsPartsLogicService.getAnomalyWpListBySn(workstationCode, sn), "获取数据成功！");
    }

    /**
     * 根据工位代码+工单号获取常用缺陷
     *
     * @param workstationCode 工位代码
     * @param inspectionNo    工单号
     * @return
     */
    @GetMapping("getanomalywplistbyinspectionno")
    @Operation(summary = "根据工位代码+工单号获取常用缺陷")
    public ResultVO<List<DefectAnomalyDto>> getAnomalyWpListByInspectionNo(String workstationCode, String inspectionNo) {
        return new ResultVO<List<DefectAnomalyDto>>()
                .ok(pqsPartsLogicService.getAnomalyWpListByInspectionNo(workstationCode, inspectionNo), "获取数据成功！");
    }

    /**
     * 根据工位代码和车型获取
     *
     * @param workstationCode
     * @param model
     * @return
     */
    @GetMapping("getqualitygatebyworkstationcode")
    @Operation(summary = "根据工位代码和车型获取")
    public ResultVO getQualityGateByWorkstationCode(@RequestParam String workstationCode, @RequestParam String model) {
        return new ResultVO<>().ok(pqsLogicService.getQualityGateByWorkstationCode(workstationCode, model), "获取数据成功");
    }

    /**
     * QG岗查看质量门检查图片数据
     *
     * @param qualityGateId
     * @return
     */
    @GetMapping("showqggatedata")
    @Operation(summary = "QG岗查看质量门检查图片数据")
    public ResultVO showQGGateData(@RequestParam Long qualityGateId) {
        return new ResultVO<>().ok(pqsLogicService.showQGGateData(qualityGateId), "获取数据成功");
    }

    /**
     * 根据产品唯一码获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 色块ID
     * @param sn                 产品唯一码
     * @return
     */
    @GetMapping("getgateanomalybygateblankidandsn")
    @Operation(summary = "根据产品唯一码获取质量门色块对应的缺陷列表")
    public ResultVO<List<DefectAnomalyDto>> getGateAnomalyByGateBlankIdAndSn(Long qualityGateBlankId, String sn) {
        return new ResultVO<List<DefectAnomalyDto>>()
                .ok(pqsPartsLogicService.getGateAnomalyByGateBlankIdAndSn(qualityGateBlankId, sn), "获取数据成功！");
    }

    /**
     * 根据评审工单号获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 色块ID
     * @param inspectionNo       评审工单号
     * @return
     */
    @GetMapping("getgateanomalybygateblankidandinspectionno")
    @Operation(summary = "根据评审工单号获取质量门色块对应的缺陷列表")
    public ResultVO<List<DefectAnomalyDto>> getGateAnomalyByGateBlankIdAndInspectionNo(Long qualityGateBlankId, String inspectionNo) {
        return new ResultVO<List<DefectAnomalyDto>>()
                .ok(pqsPartsLogicService.getGateAnomalyByGateBlankIdAndInspectionNo(qualityGateBlankId, inspectionNo), "获取数据成功！");
    }

    /**
     * 根据工位代码和车型获取百格图
     *
     * @param workstationCode 工位
     * @param model           车型
     * @return
     */
    @GetMapping("getqualitymatrikbyworkstationcode")
    @Operation(summary = "根据工位代码和车型获取百格图")
    public ResultVO getQualityMatrikByWorkstationCode(@RequestParam String workstationCode, @RequestParam String model) {
        return new ResultVO<>().ok(pqsLogicService.getQualityMatrikByWorkstationCode(workstationCode, model), "获取数据成功");
    }

    /**
     * QG岗根据产品唯一码查看质量门检查图片数据
     *
     * @param qualityMatrikId 百格图ID
     * @param sn              产品唯一码
     * @return
     */
    @GetMapping("showqgmatrikdata")
    @Operation(summary = "QG岗根据产品唯一码查看质量门检查图片数据")
    public ResultVO<ShowQgMatrikDto> showQGMatrikData(Long qualityMatrikId, String sn) {
        return new ResultVO<ShowQgMatrikDto>()
                .ok(pqsPartsLogicService.showQGMatrikDataBySn(qualityMatrikId, sn), "获取数据成功！");
    }

    /**
     * QG岗根据评审工单号查看质量门检查图片数据
     *
     * @param qualityMatrikId 百格图ID
     * @param inspectionNo    评审工单号
     * @return
     */
    @GetMapping("showqgmatrikdatabyinspectionno")
    @Operation(summary = "QG岗根据评审工单号查看质量门检查图片数据")
    public ResultVO<ShowQgMatrikDto> showQGMatrikDataByInspectionNo(Long qualityMatrikId, String inspectionNo) {
        return new ResultVO<ShowQgMatrikDto>()
                .ok(pqsPartsLogicService.showQGMatrikDataByInspectionNo(qualityMatrikId, inspectionNo), "获取数据成功！");
    }

    /**
     * 查看围堵信息
     *
     * @param sn 产品唯一码
     * @return
     */
    @GetMapping("showriskdatabysn")
    @Operation(summary = "查看围堵信息")
    public ResultVO<List<QgRiskDto>> showRiskDataBySn(String sn) {
        return new ResultVO<List<QgRiskDto>>()
                .ok(pqsPartsLogicService.showRiskDataBySn(sn), "获取数据成功！");
    }

    /**
     * 查看风险缺陷
     *
     * @param info
     * @return
     */
    @PostMapping("riskmanager")
    @Operation(summary = "查看风险缺陷")
    public ResultVO riskManager(@RequestBody QgRiskOperInfo info) {
        pqsPartsLogicService.riskManager(info);
        pqsEntryCheckItemService.saveChange();
        return new ResultVO<String>().ok("", "操作成功！");
    }

    /**
     * 手动激活缺陷
     *
     * @param info
     * @return
     */
    @PostMapping("triggeranomaly")
    @Operation(summary = "手动激活缺陷")
    public ResultVO triggerAnomaly(@RequestBody ActiveAnomalyInfo info) {
        pqsPartsLogicService.activeAnomaly(info);
        pqsEntryCheckItemService.saveChange();
        return new ResultVO<String>().ok("", "操作成功！");
    }

    /**
     * 后台更新缺陷责任区域
     *
     * @param info
     * @return
     */
    @PostMapping("modifydefectanomalyrepsponsibelinfo")
    @Operation(summary = "后台更新缺陷责任区域")
    public ResultVO modifyDefectAnomalyRepsponsibelInfo(@RequestBody ModifyDefectResponsibleInfo info) {
        pqsPartsLogicService.modifyDefectAnomalyRepsponsibelInfo(info);
        pqsEntryCheckItemService.saveChange();
        return new ResultVO<String>().ok("", "操作成功！");
    }

    /**
     * 根据数据编号获取日志列表
     *
     * @param anomalyId 缺陷ID
     * @return
     */
    @GetMapping("getvehicledefectanomalylog")
    @Operation(summary = "根据数据编号获取日志列表")
    public ResultVO<List<ProductAnomalyLogDto>> getProductDefectAnomalyLog(Long anomalyId) {
        return new ResultVO<List<ProductAnomalyLogDto>>()
                .ok(pqsPartsLogicService.getProductDefectAnomalyLog(anomalyId), "获取数据成功！");
    }

    /**
     * 缺陷活动--返修复检
     *
     * @param statusModifyInfo
     * @return
     */
    @PostMapping("modifydefectanomalystatus")
    @Operation(summary = "缺陷活动--返修复检")
    public ResultVO modifyDefectAnomalyRepsponsibelInfo(@RequestBody AnomalyStatusModifyInfo statusModifyInfo) {
        pqsPartsLogicService.modifyDefectAnomalyStatus(statusModifyInfo);
        pqsEntryCheckItemService.saveChange();
        return new ResultVO<String>().ok("", "操作成功！");
    }

    /**
     * 缺陷活动-批量
     *
     * @param info
     * @return
     */
    @PostMapping("batchmodifydefectanomalystatus")
    @Operation(summary = "缺陷活动")
    public ResultVO batchModifyDefectAnomalyStatus(@RequestBody RiskRepairInfo info) {

        if (CollectionUtil.isNotEmpty(info.getIds())) {
            info.getIds().forEach(i -> {
                AnomalyStatusModifyInfo dto = new AnomalyStatusModifyInfo();
                dto.setDataId(i);
                dto.setStatus(info.getStatus());
                dto.setRepairActivity(info.getRepairActivity());
                pqsPartsLogicService.modifyDefectAnomalyStatus(dto);
            });
        }
        pqsEntryCheckItemService.saveChange();

        return new ResultVO<>().ok("", "操作成功");
    }

    /**
     * 添加激活缺陷备注
     *
     * @param info
     * @return
     */
    @PostMapping("addvehicledefectanomalyremark")
    @Operation(summary = "添加激活缺陷备注")
    public ResultVO addVehicleDefectAnomalyRemark(@RequestBody QgAnomalyAppendRemarkInfo info) {
        pqsPartsLogicService.appendDefectAnomalyRemark(info.getDataId(), info.getRemark());
        pqsEntryCheckItemService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 获取产品缺陷列表
     *
     * @param para
     * @return
     */
    @PostMapping("getvehicledefectanomalylist")
    @Operation(summary = "获取产品缺陷列表")
    public ResultVO<PageData<ProductDefectAnomalyReponse>> getVehicleDefectAnomalyList(@RequestBody GetDefectAnomalyRequest para) {
        return new ResultVO<PageData<ProductDefectAnomalyReponse>>().ok(pqsPartsLogicService.getVehicleDefectAnomalyList(para), "获取数据成功");
    }

    // ====================================================质量门功能===================================================

    /**
     * 获取未处理车辆去向列表
     *
     * @param sn
     * @return
     */
    @GetMapping("getunhandlerouteinfo")
    @Operation(summary = "获取未处理车辆去向列表")
    public ResultVO getUnHandleRouteInfo(@RequestParam String sn) {
        return new ResultVO<>().ok(pqsLogicService.getUnHandleRouteInfo(sn), "获取数据成功");
    }

    /**
     * 根据工位获去向列表
     *
     * @param workstationCode
     * @return
     */
    @GetMapping("getproductroutepoint")
    @Operation(summary = "根据工位获去向列表")
    public ResultVO getProductRoutePoint(@RequestParam String workstationCode, @RequestParam String sn) {
        return new ResultVO<>().ok(pqsLogicService.getProductRoutePoint(workstationCode, sn), "获取数据成功");
    }

    /**
     * 执行车辆去向
     *
     * @param qgSetProductRouteInfo 车辆去向信息
     * @return
     */
    @PostMapping("setproductroute")
    @Operation(summary = "执行车辆去向")
    public ResultVO setproductroute(@RequestBody QgSetProductRouteInfo qgSetProductRouteInfo) {
        pqsLogicService.setProductRoute(qgSetProductRouteInfo);
        pqsEntryCheckItemService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 标记去向已处理
     *
     * @param model
     * @return
     */
    @PostMapping("setroutehand")
    @Operation(summary = "标记去向已处理")
    public ResultVO setRouteHand(@RequestBody IdModel model) {
        pqsLogicService.setRouteHand(model.getId());
        pqsEntryCheckItemService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 删除去向指定
     *
     * @param routeDeleteRequest 删除去向指定
     * @return
     */
    @PostMapping("deleterouterecord")
    @Operation(summary = "删除去向指定")
    public ResultVO deleteRouteRecord(@RequestBody QgCheckItemInfo routeDeleteRequest) {
        pqsLogicService.deleteRouteRecord(routeDeleteRequest);
        pqsEntryCheckItemService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * QG岗位创建工料费评审工单
     *
     * @param createQgAuditRequest QG岗位创建工料费评审工单
     * @return
     */
    @PostMapping("createauditbyqgworkstation")
    @Operation(summary = "QG岗位创建工料费评审工单")
    public ResultVO createAuditByQgWorkstation(@RequestBody CreateQgAuditInfo createQgAuditRequest) {
        pqsPartsLogicService.createAuditByQgWorkstation(createQgAuditRequest);
        pqsEntryCheckItemService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 获取评审记录明细清单
     *
     * @param qgPartAuditDetailFilterRequest
     * @return
     */
    @PostMapping("getqgpartauditdetail")
    @Operation(summary = "获取评审记录明细清单")
    public ResultVO<PageData<AuditDetailListDto>> getQgPartAuditDetail(@RequestBody QgPartAuditDetailFilterInfo qgPartAuditDetailFilterRequest) {
        return new ResultVO<PageData<AuditDetailListDto>>().ok(pqsPartsLogicService.getQgPartAuditDetail(qgPartAuditDetailFilterRequest), "获取数据成功");
    }

    /**
     * 填报料废记录
     *
     * @param info 创建评审
     * @return
     */
    @PostMapping("createmmscrapentry")
    @Operation(summary = "填报料废记录")
    public ResultVO createMMScrapEntry(@RequestBody CreateMMScrapInfo info) {
        pqsPartsLogicService.createMMScrapEntry(info);
        pqsEntryCheckItemService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 获取料废记录
     *
     * @param info 创建评审
     * @return
     */
    @PostMapping("getmmscrapdetail")
    @Operation(summary = "获取料废记录")
    public ResultVO<PageData<PqsMmScrapRecordEntity>> getMMScrapDetail(@RequestBody GetMmScrapDataFilterInfo info) {
        return new ResultVO<PageData<PqsMmScrapRecordEntity>>().ok(pqsPartsLogicService.getMMScrapDetail(info), "操作成功");
    }

    /**
     * 根据工位获取关联得物料数据--此接口等待后续PM模块提供
     *
     * @param info
     * @return
     */
    @PostMapping("getareamaterialbyworkstationcode")
    @Operation(summary = "根据工位获取关联得物料数据--此接口等待后续PM模块提供")
    public ResultVO getAreaMaterialByWorkstationCode(@RequestBody CreateQgAuditInfo info) {

        List<MaterialInfo> resultList = Lists.newArrayList();

        List<PmWorkstationMaterialEntity> materialEntityList = pmWorkStationMaterialProvider.getByWorkstationCode(info.getWorkstationCode());
        if (CollectionUtil.isNotEmpty(materialEntityList)) {
            materialEntityList.forEach(m -> {
                MaterialInfo materialInfo = MaterialInfo.builder()
                        .materialNo(m.getMaterialNo())
                        .materialCn(m.getMasterChinese())
                        .materialEn(m.getMasterEnglish())
                        .build();
                resultList.add(materialInfo);
            });
        }
        return new ResultVO<List<MaterialInfo>>().ok(resultList, "获取数据成功");
    }
}