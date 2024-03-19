package com.ca.mfd.prc.pps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.communication.dto.BomConfigDto;
import com.ca.mfd.prc.pps.communication.entity.MidSoftwareBomConfigEntity;
import com.ca.mfd.prc.pps.communication.service.IMidSoftwareBomConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @Description: 单车配置字Controller
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
@RestController
@RequestMapping("communication/midsoftwarebomconfig")
@Tag(name = "单车配置字服务", description = "单车配置字")
public class MidSoftwareBomConfigController extends BaseController<MidSoftwareBomConfigEntity> {

    @Autowired
    private IMidSoftwareBomConfigService midSoftwareBomConfigService;


    @GetMapping(value = "getbomconfig")
    @Operation(summary = "获取单车配置字")
    public List<BomConfigDto> getBomConfig(String materialNo,String effectiveDate) {
        return midSoftwareBomConfigService.getBomConfig(materialNo,effectiveDate);
    }

    @GetMapping(value = "/provider/getbomconfig")
    @Operation(summary = "获取单车配置字-远程调用")
    public ResultVO<List<BomConfigDto>> providerGetBomConfig(String materialNo,String effectiveDate) {
        return new ResultVO<List<BomConfigDto>>().ok(midSoftwareBomConfigService.getBomConfig(materialNo,effectiveDate));
    }
}