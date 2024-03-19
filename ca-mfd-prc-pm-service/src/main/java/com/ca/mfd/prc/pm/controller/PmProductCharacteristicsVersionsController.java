package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.MaintainCharacteristicsInfo;
import com.ca.mfd.prc.pm.dto.SetCharacteristicsEnablePara;
import com.ca.mfd.prc.pm.entity.PmProductCharacteristicsVersionsEntity;
import com.ca.mfd.prc.pm.service.IPmProductCharacteristicsVersionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 特征版本
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmproductcharacteristicsversions")
@Tag(name = "特征版本")
public class PmProductCharacteristicsVersionsController extends BaseController<PmProductCharacteristicsVersionsEntity> {

    private final IPmProductCharacteristicsVersionsService pmProductCharacteristicsVersionsService;

    @Autowired
    public PmProductCharacteristicsVersionsController(IPmProductCharacteristicsVersionsService pmProductCharacteristicsVersionsService) {
        this.crudService = pmProductCharacteristicsVersionsService;
        this.pmProductCharacteristicsVersionsService = pmProductCharacteristicsVersionsService;
    }


    /**
     * 获取特征详细数据
     *
     * @param productMaterialNo 物料编号
     * @param versions          物料版本
     * @return 特征
     */
    @GetMapping(value = "/provider/getbymaterialnoversions")
    @Operation(summary = "获取特征详细数据")
    public ResultVO getByMaterialNoVersions(String productMaterialNo, String versions) {
        return new ResultVO().ok(pmProductCharacteristicsVersionsService.getByMaterialNoVersions(productMaterialNo, versions));
    }


    /**
     * 获取特征详细数据
     *
     * @param productMaterialNo 物料编号
     * @param versions          物料版本
     * @return 特征集合
     */
    @GetMapping(value = "/provider/getcharacteristicsdata")
    @Operation(summary = "获取特征详细数据")
    public ResultVO getCharacteristicsData(String productMaterialNo, String versions) {
        return new ResultVO().ok(pmProductCharacteristicsVersionsService.getCharacteristicsData(productMaterialNo, versions));
    }

    /**
     * 维护特征（主要针对于一计划一特征）
     *
     * @param data 特征数据
     * @return bom 版本号
     */
    @PostMapping(value = "/provider/maintaincharacteristics")
    @Operation(summary = "维护特征（主要针对于一计划一特征）")
    public ResultVO maintainCharacteristics(@RequestBody MaintainCharacteristicsInfo data) {
        String res = pmProductCharacteristicsVersionsService.maintainCharacteristics(data,"");
        pmProductCharacteristicsVersionsService.saveChange();
        return new ResultVO().ok(res);
    }

    /**
     * 获取特征详细数据
     *
     * @param characteristicsVersions
     * @return 特征实体
     */
    @GetMapping(value = "/provider/getbycharacteristicsversions")
    @Operation(summary = "获取特征详细数据")
    public ResultVO getByCharacteristicsVersions(String characteristicsVersions) {
        return new ResultVO().ok(pmProductCharacteristicsVersionsService.getByCharacteristicsVersions(characteristicsVersions));
    }

    /**
     * 获取特征最新的版本
     *
     * @param productMaterialNo 物料编号
     * @return 获取特征版本信息
     */
    @GetMapping(value = "/provider/getcharacteristicsversions")
    @Operation(summary = "获取特征最新的版本")
    public ResultVO<String> getCharacteristicsVersions(String productMaterialNo) {
        return new ResultVO().ok(pmProductCharacteristicsVersionsService.getCharacteristicsVersions(productMaterialNo));
    }

    /**
     * 获取特征版本
     *
     * @param productMaterialNo 物料编号
     * @return 获取特征版本信息
     */
    @GetMapping(value = "/getcharacteristicsversionslist")
    @Operation(summary = "获取特征版本")
    public ResultVO<String> getCharacteristicsVersionsList(String productMaterialNo) {
        return new ResultVO().ok(pmProductCharacteristicsVersionsService.getCharacteristicsVersionsList(productMaterialNo));
    }

    /**
     * 获取特征主数据
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/setcharacteristicsenable")
    @Operation(summary = "获取特征主数据")
    public ResultVO<String> setCharacteristicsEnable(@RequestBody SetCharacteristicsEnablePara param) {
        pmProductCharacteristicsVersionsService.setCharacteristicsEnable(param);
        return new ResultVO().ok(null, "启用成功");
    }

    @GetMapping(value = "/provider/copycharacteristics")
    @Operation(summary = "复制")
    public ResultVO<String> copyCharacteristics(String productMaterialNo, String version) {
        String versionStr = pmProductCharacteristicsVersionsService.copyCharacteristics(productMaterialNo, version);
        pmProductCharacteristicsVersionsService.saveChange();
        return new ResultVO<String>().ok(versionStr);
    }

}