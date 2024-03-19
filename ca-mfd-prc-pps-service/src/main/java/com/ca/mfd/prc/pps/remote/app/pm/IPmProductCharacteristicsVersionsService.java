package com.ca.mfd.prc.pps.remote.app.pm;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.pm.dto.MaintainCharacteristicsInfo;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductCharacteristicsVersionsEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: eric
 * @Date: 2023-08-01-11:27
 * @Description:
 */
@FeignClient(name = "ca-mfd-prc-pm-service", path = "pmproductcharacteristicsversions", contextId = "inkelink-pm-pmproductcharacteristicsversions")
public interface IPmProductCharacteristicsVersionsService {

    /**
     * 获取特征详细数据
     *
     * @param productMaterialNo 物料编号
     * @param versions          物料版本
     * @return 特征
     */
    @GetMapping("/provider/getbymaterialnoversions")
    ResultVO<PmProductCharacteristicsVersionsEntity> getByMaterialNoVersions(@RequestParam("productMaterialNo") String productMaterialNo, @RequestParam("versions") String versions);

    /**
     * 获取特征详细数据
     *
     * @param productMaterialNo 物料编号
     * @param versions          物料版本
     * @return 特征集合
     */
    @GetMapping("/provider/getcharacteristicsdata")
    ResultVO<List<PmProductCharacteristicsEntity>> getCharacteristicsData(@RequestParam("productMaterialNo") String productMaterialNo, @RequestParam("versions") String versions);

    /**
     * 维护特征（主要针对于一计划一特征）
     *
     * @param data 特征数据
     * @return bom 版本号
     */
    @PostMapping(value = "/provider/maintaincharacteristics")
    ResultVO<String> maintainCharacteristics(@RequestBody MaintainCharacteristicsInfo data);

    /**
     * 获取特征详细数据
     *
     * @param characteristicsVersions
     * @return 特征实体
     */
    @GetMapping(value = "/provider/getbycharacteristicsversions")
    ResultVO<PmProductCharacteristicsVersionsEntity> getByCharacteristicsVersions(@RequestParam("characteristicsVersions") String characteristicsVersions);

    /**
     * 获取特征最新的版本
     *
     * @param productMaterialNo 物料编号
     * @return 获取特征版本信息
     */
    @GetMapping("/provider/getcharacteristicsversions")
    ResultVO<String> getCharacteristicsVersions(@RequestParam("productMaterialNo") String productMaterialNo);

    @GetMapping(value = "/provider/copycharacteristics")
    ResultVO<String> copyCharacteristics(@RequestParam("productMaterialNo") String productMaterialNo,@RequestParam("version")  String version);

}
