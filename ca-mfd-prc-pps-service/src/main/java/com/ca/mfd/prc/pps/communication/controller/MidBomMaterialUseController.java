package com.ca.mfd.prc.pps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.communication.dto.BomMaterialUseDto;
import com.ca.mfd.prc.pps.communication.entity.MidMaterialUseEntity;
import com.ca.mfd.prc.pps.communication.entity.MidVehicleMasterEntity;
import com.ca.mfd.prc.pps.communication.service.IMidBomMaterialUseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: BOM车型数据Controller
 * @date 2023年10月24日
 * @变更说明 BY inkelink At 2023年10月24日
 */
@RestController
@RequestMapping("communication/midbommaterialuse")
@Tag(name = "整车车型服务", description = "整车车型数据")
public class MidBomMaterialUseController extends BaseController<MidMaterialUseEntity> {
    @Autowired
    private IMidBomMaterialUseService bomMaterialUseService;

    @GetMapping(value = "getbommaterialusedata")
    @Operation(summary = "获取物料使用信息")
    public ResultVO<List<BomMaterialUseDto>> getVehicleModelData(String materialNo,String plantCode,String specifyDate) {
        return new ResultVO<List<BomMaterialUseDto>>().ok(bomMaterialUseService.getBomMaterialUseData(materialNo,plantCode,specifyDate));
    }

}