package com.ca.mfd.prc.pps.remote.app.pm;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.pm.dto.MaintainBomDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomVersionsEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author eric.zhou
 * @Description: BOM详细
 * @date 2023年6月7日
 * @变更说明 BY eric.zhou At 2023年6月7日
 */
@FeignClient(name = "ca-mfd-prc-pm-service", path = "pmproductbomversions", contextId = "inkelink-pm-pmproductbomversions")
public interface IPmProductBomVersionsService {

    /**
     * 获取信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "getbyid")
    ResultVO<PmProductBomVersionsEntity> getById(@RequestParam("id") String id);

    /**
     * 获取BOM详细数据
     *
     * @param productMaterialNo 物料编号
     * @param bomVersions       物料版本
     * @return 物料集合
     */
    @GetMapping(value = "/provider/getbomdata")
    ResultVO<List<PmProductBomEntity>> getBomData(@RequestParam("productMaterialNo") String productMaterialNo, @RequestParam("bomVersions") String bomVersions);

    /**
     * 获取BOM最新的版本
     *
     * @param productMaterialNo 物料编号
     * @return 获取bom版本信息
     */
    @GetMapping(value = "/provider/getbomversions")
    ResultVO<String> getBomVersions(@RequestParam("productMaterialNo") String productMaterialNo);

    /**
     * 根据零件号获取整车物料版本信息
     *
     * @param materialNo
     * @return
     * */
    @GetMapping(value = "/provider/getversionbymaterialno")
    ResultVO<PmProductBomVersionsEntity> getVersionByMaterialNo(@RequestParam("materialNo") String materialNo,@RequestParam("orderCategory") String orderCategory);

    /**
     * 获取BOM详细数据
     *
     * @param productMaterialNo 物料编号
     * @param bomVersions       物料版本
     * @return 物料集合
     */
    @GetMapping(value = "/provider/getbyproductmaterialnobomverson")
    ResultVO<PmProductBomVersionsEntity> getByProductMaterialNoBomVerson(@RequestParam("productMaterialNo") String productMaterialNo, @RequestParam("bomVersions") String bomVersions);

    /**
     * 维护BOM（主要针对于一计划一BOM）
     *
     * @param bomData
     * @return String
     */
    @PostMapping(value = "/provider/maintainbom")
    ResultVO<String> maintainBom(@RequestBody MaintainBomDTO bomData);

    @GetMapping(value = "/provider/copybom")
    ResultVO<String> copyBom(@RequestParam("productMaterialNo") String productMaterialNo,@RequestParam("version")  String version);

}
