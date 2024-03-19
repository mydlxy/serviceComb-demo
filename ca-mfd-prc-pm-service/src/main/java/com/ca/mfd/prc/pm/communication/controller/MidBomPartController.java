package com.ca.mfd.prc.pm.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.communication.service.IMidBomPartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 物料主数据Controller
 * @author inkelink
 * @date 2023年10月24日
 * @变更说明 BY inkelink At 2023年10月24日
 */
@RestController
@RequestMapping("communication/midbompart")
@Tag(name = "零件BOM服务", description = "零件BOM数据")
public class MidBomPartController extends BaseApiController {
    @Autowired
    private IMidBomPartService bomPartService;

    @GetMapping(value = "getbompartversion")
    @Operation(summary = "获取零件bom版本号")
    public ResultVO<String> getBomPartVersion(String materialNo,String plantCode,String specifyDate) {
        return new ResultVO<String>().ok(bomPartService.getBomPartVersion(materialNo,plantCode,specifyDate));
    }

}