package com.ca.mfd.prc.pqs.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdModel;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.*;
import com.ca.mfd.prc.pqs.service.IPqsLogicService;
import com.ca.mfd.prc.pqs.service.IPqsQualityGateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("pqslogic")
@Tag(name = "业务方法")
public class PqsLogicController extends BaseController {
    @Autowired
    private IPqsLogicService pqsLogicService;
    @Autowired
    private IPqsQualityGateService pqsQualityGateService;

    @PostMapping("getworkplacelist")
    @Operation(summary = "获取所有岗位")
    public ResultVO<PageData<GetWorkStationListInfo>> getWorkPlaceList(@RequestBody GetWorkPlaceListParaInfo info) {
        return new ResultVO<PageData<GetWorkStationListInfo>>().ok(pqsLogicService.getWorkPlaceList(info), "获取数据成功");
    }

    @GetMapping("getanomalylistbyworkplaceid")
    @Operation(summary = "根据岗位编号获取岗位配置的常用缺陷")
    public ResultVO<List<DefectShowInfo>> getAnomalyListByWorkPlaceId(String workPlaceId) {
        return new ResultVO<List<DefectShowInfo>>().ok(pqsLogicService.getAnomalyListByWorkPlaceId(workPlaceId), "获取数据成功");
    }

    @GetMapping("getworkplacelistbyqg")
    @Operation(summary = "获取QG所有岗位")
    public ResultVO<List<GetWorkStationListInfo>> getWorkPlaceListByQg() {
        return new ResultVO<List<GetWorkStationListInfo>>().ok(pqsLogicService.getWorkPlaceListByQg(), "获取数据成功");
    }

    @GetMapping("getqganomalydata")
    @Operation(summary = "获取QG配置数据信息")
    public ResultVO<ShowQgAnomalyConfigurationInfo> getQgAnomalyData(String qgAnomalyId) {
        return new ResultVO<ShowQgAnomalyConfigurationInfo>().ok(pqsLogicService.getQgAnomalyData(qgAnomalyId), "获取数据成功");
    }

    @GetMapping("initconfigurationitem")
    @Operation(summary = "根据色块编号获取色块对应的缺陷列表")
    public ResultVO<InitConfigurationItemInfo> initconfigurationitem(String gateBlankId) {
        return new ResultVO<InitConfigurationItemInfo>().ok(pqsLogicService.initConfigurationItem(gateBlankId), "获取数据成功");
    }

    @PostMapping("sendqganomalydata")
    @Operation(summary = "提交QG配置数据信息")
    public ResultVO<String> sendQgAnomalyData(@RequestBody SendQgAnomalyConfigurationInfo info) {
        pqsLogicService.sendQgAnomalyData(info);
        pqsQualityGateService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @GetMapping("getavilist")
    @Operation(summary = "获取所有AVI列表")
    public ResultVO<List<AviListInfo>> getavilist() {
        return new ResultVO<List<AviListInfo>>().ok(pqsLogicService.getAviList(), "获取数据成功");
    }

    /**
     * QG岗查看质量门检查图片数据
     *
     * @param qualityGateId 质量门ID
     * @return 返回图片列表
     */
    @GetMapping("/provider/showqgworkplaceaomalydata")
    @Operation(summary = "QG岗查看质量门检查图片数据")
    public ResultVO<ShowQgWorkplaceAomalyDataInfo> showQgWorkplaceAomalyData(@RequestParam String qualityGateId) {
        ResultVO<ShowQgWorkplaceAomalyDataInfo> result = new ResultVO<>();
        ShowQgWorkplaceAomalyDataInfo data = pqsLogicService.showQgWorkplaceAomalyData(qualityGateId);
        return result.ok(data, "获取数据成功");
    }

    /**
     * 获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 节点ID
     * @param tpsCode            tps编码
     * @return 缺陷列表
     */
    @GetMapping("/provider/getanomalybyqualitygateblankid")
    @Operation(summary = "获取质量门色块对应的缺陷列表")
    public ResultVO<List<GetAnomalyByQualityGateBlankIdInfo>> getAnomalyByQualityGateBlankId(@RequestParam String qualityGateBlankId, @RequestParam String tpsCode) {
        ResultVO<List<GetAnomalyByQualityGateBlankIdInfo>> result = new ResultVO<>();
        List<GetAnomalyByQualityGateBlankIdInfo> data = pqsLogicService.getAnomalyByQualityGateBlankId(qualityGateBlankId, tpsCode);
        return result.ok(data, "获取数据成功");
    }

    /**
     * 激活缺陷
     *
     * @param activeAnomalyInfo 缺陷实体
     */
    @PostMapping("/provider/activeanomaly")
    @Operation(summary = "激活缺陷")
    public ResultVO<String> activeAnomaly(@RequestBody ActiveAnomalyInfo activeAnomalyInfo) {
        pqsLogicService.activeAnomaly(activeAnomalyInfo);
        pqsQualityGateService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 获取已激活的缺陷列表
     *
     * @param para 条件参数
     * @return 缺陷列表
     */
    @PostMapping("/provider/getvehicledefectanomalylist")
    @Operation(summary = "获取已激活的缺陷列表")
    public ResultVO<PageData<ProductDefectAnomalyReponse>> getProviderVehicleDefectAnomalyList(@RequestBody GetDefectAnomalyRequest para) {
        ResultVO<PageData<ProductDefectAnomalyReponse>> result = new ResultVO<>();
        PageData<ProductDefectAnomalyReponse> data = pqsLogicService.getVehicleDefectAnomalyList(para);
        return result.ok(data, "获取数据成功");
    }

    /**
     * 修改已激活缺陷状态
     *
     * @param dataId 数据ID
     * @param status 状态
     */
    @GetMapping("/provider/modifyvehicledefectanomalystatus")
    @Operation(summary = "修改已激活缺陷状态")
    public ResultVO<String> modifyVehicleDefectAnomalyStatus(@RequestParam String dataId, @RequestParam Integer status) {
        pqsLogicService.modifyVehicleDefectAnomalyStatus(dataId, status);
        pqsQualityGateService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 缺陷活动
     *
     * @param anomalyActivity 缺陷实体
     */
    @PostMapping("/provider/anomalyactivity")
    @Operation(summary = "缺陷活动")
    public ResultVO<String> providerModifyDefectAnomalyStatus(@RequestBody AnomalyActivity anomalyActivity) {
        ResultVO<String> result = new ResultVO<>();
        pqsLogicService.modifyDefectAnomalyStatus(anomalyActivity);
        pqsQualityGateService.saveChange();
        return result.ok("", "操作成功");
    }

    /**
     * 添加激活缺陷备注
     *
     * @param dataId 更新ID
     * @param remark 备注内容
     */
    @GetMapping("/provider/addvehicledefectanomalyremark")
    @Operation(summary = "添加激活缺陷备注")
    public ResultVO<String> addVehicleDefectAnomalyRemark(@RequestParam String dataId, @RequestParam String remark) {
        ResultVO<String> result = new ResultVO<>();
        pqsLogicService.addVehicleDefectAnomalyRemark(dataId, remark);
        pqsQualityGateService.saveChange();
        return result.ok("", "操作成功");
    }

    /**
     * 获取岗位常用缺陷
     *
     * @param workplaceId 工位ID
     * @return 缺陷列表
     */
    @GetMapping("/provider/getanomalywpbyworkplaceandsn")
    @Operation(summary = "获取岗位常用缺陷")
    public ResultVO<List<GetAnomalyWpByWorkPlaceAndSnInfo>> getAnomalyWpByWorkPlaceAndSn(@RequestParam String workplaceId) {
        return new ResultVO<List<GetAnomalyWpByWorkPlaceAndSnInfo>>().ok(pqsLogicService.getAnomalyWpByWorkPlaceAndSn(workplaceId), "获取数据成功");
    }

    /**
     * 根据岗位编号获取QG岗检查项列表
     *
     * @param workplaceId
     * @param model
     * @return List<GetAnomalyWpByWorkPlaceAndSnInfo>
     */
    @GetMapping("/provider/getqualitygatebyworkplaceid")
    @Operation(summary = "根据岗位编号获取QG岗检查项列表")
    public ResultVO<List<ComboInfoDTO>> getQualityGateByWorkplaceId(@RequestParam String workplaceId, @RequestParam String model) {
        return new ResultVO<List<ComboInfoDTO>>().ok(pqsLogicService.getQualityGateByWorkplaceId(workplaceId, model), "获取数据成功");
    }

    /**
     * 根据色块编号获取色块对应的缺陷列表
     *
     * @param gateBlankId
     * @return
     */
    @GetMapping("getgateanomalylist")
    @Operation(summary = "根据色块编号获取色块对应的缺陷列表")
    public ResultVO getGateAnomalyList(@RequestParam Long gateBlankId) {
        return new ResultVO<>().ok(pqsLogicService.getGateAnomalyList(gateBlankId), "获取数据成功");
    }

    /*
     * ---------------- 缺陷基础数据 不含含缺陷库 开始 ------------------------------------------
     */

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
    @Operation(summary = "获取责任等级")
    public ResultVO getDeptCombo() {
        return new ResultVO<>().ok(pqsLogicService.getDeptCombo(), "获取数据成功");
    }

    /*
     * ---------------- 缺陷组合信息 --缺陷库、常用缺陷、百格图、QG检查项 开始 -----------------------------
     */

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
        pqsQualityGateService.saveChange();
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
        pqsQualityGateService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /*
     * ---------------- 质量门功能 开始 -----------------------------
     */

    /**
     * 根据工位代码、唯一码获取常用缺陷
     *
     * @param workstationCode
     * @param sn
     * @return
     */
    @GetMapping("getanomalywplistbysn")
    @Operation(summary = "根据工位代码、唯一码获取常用缺陷")
    public ResultVO getAnomalyWpListBySn(@RequestParam String workstationCode, @RequestParam String sn) {
        return new ResultVO<>().ok(pqsLogicService.getAnomalyWpListBySn(workstationCode, sn), "获取数据成功");
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
     * 获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId
     * @param sn
     * @return
     */
    @GetMapping("getgateanomalybygateblankidandsn")
    @Operation(summary = "获取质量门色块对应的缺陷列表")
    public ResultVO getGateAnomalyByGateBlankIdAndSn(@RequestParam Long qualityGateBlankId, @RequestParam String sn) {
        return new ResultVO<>().ok(pqsLogicService.getGateAnomalyByGateBlankIdAndSn(qualityGateBlankId, sn), "获取数据成功");
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
        return new ResultVO<>().ok(pqsLogicService.getQualityMatrikByWorkstationCode(workstationCode, model), "获取数据成功");
    }

    /**
     * QG岗查看质量门检查图片数据
     *
     * @param qualityMatrikId 百格图ID
     * @param sn              产品唯一码
     * @return
     */
    @GetMapping("showqgmatrikdata")
    @Operation(summary = "QG岗查看质量门检查图片数据")
    public ResultVO showQGMatrikData(@RequestParam Long qualityMatrikId, @RequestParam String sn) {
        return new ResultVO<>().ok(pqsLogicService.showQgMatrikDataBySn(qualityMatrikId, sn), "获取数据成功");
    }

    /**
     * 查看围堵信息
     *
     * @param sn
     * @return
     */
    @GetMapping("showriskdatabysn")
    @Operation(summary = "查看围堵信息")
    public ResultVO showRiskDataBySn(@RequestParam String sn) {
        return new ResultVO<>().ok(pqsLogicService.showRiskDataBySn(sn), "获取数据成功");
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
        pqsLogicService.riskManager(info);
        pqsQualityGateService.saveChange();
        return new ResultVO<String>().ok("", "操作成功！");
    }

    /**
     * 查看QG必检清单
     *
     * @param workstationCode
     * @param sn
     * @return
     */
    @GetMapping("showqgchecklistbysn")
    @Operation(summary = "查看QG必检清单")
    public ResultVO showQgCheckListBySn(@RequestParam String workstationCode, @RequestParam String sn) {
        return new ResultVO<>().ok(pqsLogicService.showQgCheckListBySn(workstationCode, sn), "获取数据成功");
    }

    /**
     * 提交qg检查项目结果
     *
     * @param info
     * @return
     */
    @PostMapping("submitqgcheckitemresult")
    @Operation(summary = "提交qg检查项目结果")
    public ResultVO submitQgCheckItemResult(@RequestBody SubmitCheckItemInfo info) {
        pqsLogicService.submitQgCheckItemResult(info);
        pqsQualityGateService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
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
        pqsLogicService.activeAnomaly_(info);
        pqsQualityGateService.saveChange();
        return new ResultVO<String>().ok("", "激活成功");
    }

    /**
     * 手动激活缺陷
     *
     * @param info
     * @return
     */
    @PostMapping("setdescription")
    @Operation(summary = "设置备注信息")
    public ResultVO setDescription(@RequestBody DefectDescriptionDto info) {
        pqsLogicService.setDefectDescription(info);
        return new ResultVO<String>().ok("", "备注成功");
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
        pqsLogicService.modifyDefectAnomalyRepsponsibelInfo(info);
        pqsQualityGateService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 根据数据编号获取日志列表
     *
     * @param anomalyId
     * @return
     */
    @GetMapping("getvehicledefectanomalylog")
    @Operation(summary = "根据数据编号获取日志列表")
    public ResultVO getProductDefectAnomalyLog(@RequestParam Long anomalyId) {
        return new ResultVO<>().ok(pqsLogicService.getProductDefectAnomalyLog(anomalyId), "获取数据成功");
    }

    /**
     * 缺陷活动
     *
     * @param anomalyStatusModifyRequest
     * @return
     */
    @PostMapping("modifydefectanomalystatus")
    @Operation(summary = "缺陷活动")
    public ResultVO modifyDefectAnomalyStatus(@RequestBody AnomalyActivity anomalyStatusModifyRequest) {
        pqsLogicService.modifyDefectAnomalyStatus(anomalyStatusModifyRequest);
        pqsQualityGateService.saveChange();
        return new ResultVO<>().ok("", "操作成功");
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
                AnomalyActivity dto = AnomalyActivity.builder()
                        .dataId(i)
                        .status(info.getStatus())
                        .repairActivity(info.getRepairActivity())
                        .build();
                pqsLogicService.modifyDefectAnomalyStatus(dto);
            });
        }
        pqsQualityGateService.saveChange();

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
        pqsLogicService.appendDefectAnomalyRemark(info.getDataId(), info.getRemark());
        pqsQualityGateService.saveChange();
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
    public ResultVO getVehicleDefectAnomalyList(@RequestBody GetDefectAnomalyRequest para) {
        return new ResultVO<>().ok(pqsLogicService.getVehicleDefectAnomalyList(para), "获取数据成功");
    }

    /**
     * 获取qg质量门拦截记录
     *
     * @param workstationCode 工位代码
     * @param sn              唯一码
     * @return
     */
    @GetMapping("getqggatevarificationslist")
    @Operation(summary = "获取qg质量门拦截记录")
    public ResultVO getQgGateVarificationsList(@RequestParam String workstationCode, @RequestParam String sn) {
        return new ResultVO<>().ok(pqsLogicService.getQgGateVarificationsList(workstationCode, sn), "获取数据成功");
    }

    /**
     * 获取qg质量门拦截结果
     *
     * @param workstationCode 工位代码
     * @param sn              唯一码
     * @return
     */
    @GetMapping("getqggatevarificationsresult")
    @Operation(summary = "获取qg质量门拦截结果")
    public ResultVO getQgGateVarificationsResult(@RequestParam String workstationCode, @RequestParam String sn) {
        return new ResultVO<>().ok(pqsLogicService.getQgGateVarificationsResult(workstationCode, sn), "获取数据成功");
    }

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
     * 标记去向已处理
     *
     * @param model
     * @return
     */
    @PostMapping("setroutehand")
    @Operation(summary = "标记去向已处理")
    public ResultVO setRouteHand(@RequestBody IdModel model) {
        pqsLogicService.setRouteHand(model.getId());
        pqsQualityGateService.saveChange();
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
        pqsQualityGateService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
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
        pqsQualityGateService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }
}