package com.ca.mfd.prc.pps.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.entity.PpsModuleProductStatusEntity;
import com.ca.mfd.prc.pps.service.IPpsModuleProductStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @Description: 模组相关产品状态Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("ppsmoduleproductstatus")
@Tag(name = "模组相关产品状态服务", description = "模组相关产品状态")
public class PpsModuleProductStatusController extends BaseController<PpsModuleProductStatusEntity> {

    private IPpsModuleProductStatusService ppsModuleProductStatusService;

    @Autowired
    public PpsModuleProductStatusController(IPpsModuleProductStatusService ppsModuleProductStatusService) {
        this.crudService = ppsModuleProductStatusService;
        this.ppsModuleProductStatusService = ppsModuleProductStatusService;
    }

    @Operation(summary = "批量查询")
    @PostMapping("/provider/getlistbybarcodes")
    public ResultVO<List<PpsModuleProductStatusEntity>> getListByBarCodes(@RequestBody IdsModel barcodes) {
        List<PpsModuleProductStatusEntity> data = ppsModuleProductStatusService.getListByBarCodes(Arrays.asList(barcodes.getIds()));
        return new ResultVO<List<PpsModuleProductStatusEntity>>().ok(data, "操作成功");
    }

    @Operation(summary = "删除")
    @PostMapping("/provider/deletebyproductbarcode")
    public ResultVO<String> deleteByProductBarcode(String barcode) {
        UpdateWrapper<PpsModuleProductStatusEntity> upset = new UpdateWrapper<>();
        upset.lambda().eq(PpsModuleProductStatusEntity::getProductBarcode, barcode);
        ppsModuleProductStatusService.delete(upset);
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "新增")
    @PostMapping("/provider/insert")
    public ResultVO<String> insertModel(@RequestBody PpsModuleProductStatusEntity model) {
        ppsModuleProductStatusService.insert(model);
        return new ResultVO<String>().ok("", "操作成功");
    }

}