package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.MaintainBomDTO;
import com.ca.mfd.prc.pm.dto.SetBomEnablePara;
import com.ca.mfd.prc.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pm.entity.PmProductBomVersionsEntity;
import com.ca.mfd.prc.pm.service.IPmProductBomVersionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author inkelink
 * @Description: BOM版本
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmproductbomversions")
@Tag(name = "BOM版本")
public class PmProductBomVersionsController extends BaseController<PmProductBomVersionsEntity> {

    private final IPmProductBomVersionsService pmProductBomVersionsService;

    @Autowired
    public PmProductBomVersionsController(IPmProductBomVersionsService pmProductBomVersionsService) {
        this.crudService = pmProductBomVersionsService;
        this.pmProductBomVersionsService = pmProductBomVersionsService;
    }

    /**
     * 获取BOM详细数据
     *
     * @return 物料结合
     */
    @GetMapping(value = "/provider/getbomdata")
    @Operation(summary = "获取BOM详细数据")
    public ResultVO<List<PmProductBomEntity>> getBomData(String productMaterialNo, String bomVersions) {
        return new ResultVO<List<PmProductBomEntity>>().ok(pmProductBomVersionsService.getBomData(productMaterialNo, bomVersions));
    }
    @GetMapping(value = "/provider/inputbom")
    @Operation(summary = "导入bom数据")
    public ResultVO<String> inputbom(String tbname,String productNo) {
        pmProductBomVersionsService.inputCD701Bom(tbname,productNo);
        return new ResultVO<String>().ok("");
    }

    @GetMapping(value = "/provider/savebomtocom")
    @Operation(summary = "导入bom数据")
    public ResultVO<String> saveBomToCom(String productMaterialNo, String bomVersions) {
        pmProductBomVersionsService.saveBomToCom(productMaterialNo, bomVersions);
        pmProductBomVersionsService.saveChange();
        return new ResultVO<String>().ok("");
    }

    @GetMapping(value = "/provider/copybom")
    @Operation(summary = "复制")
    public ResultVO<String> copyBom(String productMaterialNo, String version) {
        String versionStr = pmProductBomVersionsService.copyBom(productMaterialNo, version);
        pmProductBomVersionsService.saveChange();
        return new ResultVO<String>().ok(versionStr);
    }


    /**
     * 获取BOM最新的版本
     *
     * @return 获取bom版本信息
     */
    @Operation(summary = "获取BOM最新的版本")
    @GetMapping(value = "/provider/getbomversions")
    public ResultVO getBomVersions(String productMaterialNo) {
        return new ResultVO().ok(pmProductBomVersionsService.getBomVersions(productMaterialNo));
    }

    /**
     * 获取BOM详细数据
     *
     * @param productMaterialNo 物料编号
     * @param bomVersions       物料版本
     * @return 物料结合
     */
    @GetMapping(value = "/provider/getbyproductmaterialnobomverson")
    @Operation(summary = "获取BOM详细数据")
    public ResultVO getByProductMaterialNoBomVerson(String productMaterialNo, String bomVersions) {
        return new ResultVO().ok(pmProductBomVersionsService.getByProductMaterialNoBomVerson(productMaterialNo, bomVersions));
    }

    /**
     * 根据零件号获取整车物料版本信息
     *
     * @param materialNo
     * @return
     * */
    @GetMapping(value = "/provider/getversionbymaterialno")
    @Operation(summary = "根据零件号获取整车物料版本信息")
    public ResultVO getVersionByMaterialNo(String materialNo,String orderCategory) {
        return new ResultVO<PmProductBomVersionsEntity>().ok(pmProductBomVersionsService.getVersionByMaterialNo(materialNo,orderCategory));
    }

    /**
     * 维护BOM（主要针对于一计划一BOM）
     *
     * @param bomData
     * @return String
     */
    @PostMapping(value = "/provider/getbyproductmaterialnobomverson")
    @Operation(summary = "维护BOM（主要针对于一计划一BOM）")
    public ResultVO<String> getByProductMaterialNoBomVerson(@RequestBody MaintainBomDTO bomData) {
        String res = pmProductBomVersionsService.maintainBom(bomData);
        pmProductBomVersionsService.saveChange();
        return new ResultVO().ok(res);
    }

    /**
     * 维护BOM（主要针对于一计划一BOM）
     *
     * @param bomData
     * @return String
     */
    @PostMapping(value = "/provider/maintainbom")
    @Operation(summary = "维护BOM（主要针对于一计划一BOM）")
    public ResultVO<String> maintainBom(@RequestBody MaintainBomDTO bomData) {
        String res = pmProductBomVersionsService.maintainBom(bomData);
        pmProductBomVersionsService.saveChange();
        return new ResultVO().ok(res);
    }

    /**
     * 获取BOM版本
     *
     * @return 获取BOM版本
     */
    @Operation(summary = "获取BOM版本")
    @Parameters({
            @Parameter(name = "productMaterialNo", description = "物料号")})
    @GetMapping(value = "/getbomversionscombox")
    public ResultVO getBomVersionsCombox(String productMaterialNo) {
        List<String> bomVersions = pmProductBomVersionsService.getBomVersionsList(productMaterialNo);
        if (bomVersions.isEmpty()) {
            return new ResultVO().ok(Collections.emptyList());
        }
        List<ComboInfoDTO> resultDataList = new ArrayList<>(bomVersions.size());
        for (String bomVersion : bomVersions) {
            resultDataList.add(new ComboInfoDTO(bomVersion, bomVersion));
        }
        return new ResultVO().ok(resultDataList);
    }

    /**
     * 获取BOM版本
     *
     * @return 获取BOM版本
     */
    @Operation(summary = "获取BOM版本")
    @Parameters({
            @Parameter(name = "productMaterialNo", description = "物料号")})
    @GetMapping(value = "/getbomversionslist")
    public ResultVO getBomVersionsList(String productMaterialNo) {
        return new ResultVO().ok(pmProductBomVersionsService.getBomVersionsList(productMaterialNo));
    }

    /**
     * 设置BOM启用版本
     *
     * @return 获取BOM版本
     */
    @Operation(summary = "获取BOM版本")
    @Parameters({
            @Parameter(name = "para", description = "ID")})
    @PostMapping(value = "/setbomenable")
    public ResultVO setBomEnable(@RequestBody SetBomEnablePara para) {
        pmProductBomVersionsService.setBomEnable(para);
        return new ResultVO().ok("启用成功", "启用成功");
    }


}