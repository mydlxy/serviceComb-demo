package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.FeatureConfigInfo;
import com.ca.mfd.prc.pm.dto.UpdateFeatureConfigInfo;
import com.ca.mfd.prc.pm.entity.PmIssueCharacteristicsConfigEntity;
import com.ca.mfd.prc.pm.service.IPmIssueCharacteristicsConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 下发特征配置Controller
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@RestController
@RequestMapping("pmissuecharacteristicsconfig")
@Tag(name = "下发特征配置服务", description = "下发特征配置")
public class PmIssueCharacteristicsConfigController extends BaseController<PmIssueCharacteristicsConfigEntity> {

    private final IPmIssueCharacteristicsConfigService pmIssueCharacteristicsConfigService;

    @Autowired
    public PmIssueCharacteristicsConfigController(IPmIssueCharacteristicsConfigService pmIssueCharacteristicsConfigService) {
        this.crudService = pmIssueCharacteristicsConfigService;
        this.pmIssueCharacteristicsConfigService = pmIssueCharacteristicsConfigService;
    }

    @GetMapping(value = "getfeatureconfiglist")
    @Operation(summary = "获取特征项配置")
    public ResultVO getFeatureConfigList(String subkey, Integer relevanceType) {
        return new ResultVO<List<FeatureConfigInfo>>().ok(pmIssueCharacteristicsConfigService.getFeatureConfigList(subkey, relevanceType));
    }

    @GetMapping(value = "/provider/getlistbysubkeyrelevancetype")
    @Operation(summary = "获取特征项配置")
    public ResultVO getListBySubKeyRelevanceType(String subKey, Integer relevanceType) {
        return new ResultVO<List<PmIssueCharacteristicsConfigEntity>>().ok(pmIssueCharacteristicsConfigService.getListBySubKeyRelevanceType(subKey, relevanceType));
    }

    @PostMapping(value = "updatefeatureconfig")
    @Operation(summary = "更新特征项配置")
    public ResultVO updateFeatureConfig(@RequestBody UpdateFeatureConfigInfo para) {
        ResultVO result = new ResultVO<>();
        pmIssueCharacteristicsConfigService.updateFeatureConfig(para);
        pmIssueCharacteristicsConfigService.saveChange();
        return result.ok("保存成功", "保存成功");
    }
}