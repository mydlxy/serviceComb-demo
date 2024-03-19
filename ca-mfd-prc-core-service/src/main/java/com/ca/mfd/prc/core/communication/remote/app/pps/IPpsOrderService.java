package com.ca.mfd.prc.core.communication.remote.app.pps;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.communication.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.core.communication.remote.app.pps.entity.PpsOrderEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * IPpsOrderService
 *
 * @author inkelink
 * @date 2023/09/05
 */
@FeignClient(name = "ca-mfd-prc-pps-service", path = "ppsorder", contextId = "inkelink-pps-ppsorder")
public interface IPpsOrderService {
    @GetMapping("/provider/getPpsOrderBySnOrBarcode")
    ResultVO<PpsOrderEntity> getPpsOrderBySnOrBarcode(@RequestParam("code") String code);

    @GetMapping("/provider/getPpsOrderInfoByKey")
    ResultVO<PpsOrderEntity> getPpsOrderInfoByKey(@RequestParam("tpsCode") String tpsCode);

    @GetMapping("getordercharacteristicbyorderid")
    ResultVO<List<PmProductCharacteristicsEntity>> getOrderCharacteristicByOrderId(@RequestParam("orderid") String orderid);

    @PostMapping("/provider/getListByBarcodes")
    ResultVO<List<PpsOrderEntity>> getListByBarcodes(@RequestBody List<String> barcodes);

    @GetMapping("/provider/getTopOrderByCodeLike")
    ResultVO<List<PpsOrderEntity>> getTopOrderByCodeLike(@RequestParam("top") Integer top, @RequestParam("code") String code);
}
