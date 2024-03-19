package com.ca.mfd.prc.avi.communication.remote.app.pps;

import com.ca.mfd.prc.avi.communication.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.common.utils.ResultVO;
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
@FeignClient(name = "ca-mfd-prc-pps-service", path = "ppsorder", contextId = "inkelink-pps-midppsorder")
public interface IPpsOrderService {
    @GetMapping("/provider/getppsorderbysnorbarcode")
    ResultVO<PpsOrderEntity> getPpsOrderBySnOrBarcode(@RequestParam("code") String code);

    @GetMapping("/provider/getppsorderinfobykey")
    ResultVO<PpsOrderEntity> getPpsOrderInfoByKey(@RequestParam("tpsCode") String tpsCode);

    @PostMapping("/provider/getlistbybarcodes")
    ResultVO<List<PpsOrderEntity>> getListByBarcodes(@RequestBody List<String> barcodes);

    @GetMapping("/provider/gettoporderbycodelike")
    ResultVO<List<PpsOrderEntity>> getTopOrderByCodeLike(@RequestParam("top") Integer top, @RequestParam("code") String code);

    @PostMapping("/provider/checkcharacteristic10")
    ResultVO<Boolean> checkCharacteristic10(@RequestBody List<String> characteristics);
}
