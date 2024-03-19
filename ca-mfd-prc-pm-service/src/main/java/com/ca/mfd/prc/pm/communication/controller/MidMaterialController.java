package com.ca.mfd.prc.pm.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.communication.service.IMidMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("communication/midmaterial")
@Tag(name = "单车BOM服务", description = "单车BOM数据")
public class MidMaterialController extends BaseApiController {
    @Autowired
    private IMidMaterialService materialService;

    @GetMapping(value = "getbomversion")
    @Operation(summary = "获取bom版本号")
    public ResultVO<String> getBomVersion(String plantCode,String materialNo,String specifyDate) {
        return new ResultVO<String>().ok(materialService.getBomVersions(plantCode,materialNo,specifyDate));
    }
}