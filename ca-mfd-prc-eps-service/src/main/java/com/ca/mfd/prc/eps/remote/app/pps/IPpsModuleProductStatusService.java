package com.ca.mfd.prc.eps.remote.app.pps;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsModuleProductStatusEntity;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsOrderEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 模组相关产品接口
 *
 * @author inkelink
 * @date 2023/09/05
 */
@FeignClient(name = "ca-mfd-prc-pps-service", path = "ppsmoduleproductstatus", contextId = "inkelink-pps-ppsmoduleproductstatus")
public interface IPpsModuleProductStatusService {

    @Operation(summary = "删除")
    @PostMapping("/provider/deletebyproductbarcode")
    ResultVO<String> deleteByProductBarcode(@RequestParam("barcode") String barcode);

    @Operation(summary = "新增")
    @PostMapping("/provider/insert")
    ResultVO<String> insertModel(@RequestBody PpsModuleProductStatusEntity model);

    @Operation(summary = "获取订单数量（模糊匹配）")
    @PostMapping("/provider/getlistbybarcodes")
    ResultVO<List<PpsModuleProductStatusEntity>> getListByBarCodes(@RequestBody IdsModel barcodes);
}
