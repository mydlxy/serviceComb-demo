package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.SetLinePara;
import com.ca.mfd.prc.pps.entity.PpsLineProductionConfigEntity;
import com.ca.mfd.prc.pps.service.IPpsLineProductionConfigService;
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
 * @Description: 线体生产配置Controller
 * @date 2023年09月12日
 * @变更说明 BY inkelink At 2023年09月12日
 */
@RestController
@RequestMapping("ppslineproductionconfig")
@Tag(name = "线体生产配置服务", description = "线体生产配置")
public class PpsLineProductionConfigController extends BaseController<PpsLineProductionConfigEntity> {

    private final IPpsLineProductionConfigService ppsLineProductionConfigService;

    @Autowired
    public PpsLineProductionConfigController(IPpsLineProductionConfigService ppsLineProductionConfigService) {
        this.crudService = ppsLineProductionConfigService;
        this.ppsLineProductionConfigService = ppsLineProductionConfigService;
    }

    @Operation(summary = "设置生产区域")
    @PostMapping("setline")
    public ResultVO setLine(@RequestBody SetLinePara para) {
        ResultVO<String> result = new ResultVO<>();
        result.setMessage("设置生产区域成功");
        ppsLineProductionConfigService.setLine(para);
        ppsLineProductionConfigService.saveChange();
        return result.ok("","设置生产区域成功");
    }

    @Operation(summary = "根据获取区域产品类型")
    @GetMapping("getlinebyworkstationcode")
    public ResultVO getLineByWorkstationCode(String lineCode) {
        ResultVO<String> result = new ResultVO<>();
        result.setMessage("获取区域产品类型成功");
        return result.ok(ppsLineProductionConfigService.getLineByWorkstationCode(lineCode));
    }

}