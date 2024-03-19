package com.ca.mfd.prc.pqs.remote.app.pps;

import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsOrderEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author edwards.qu
 */
@FeignClient(
        name = "ca-mfd-prc-pps-service",
        path = "ppsorder",
        contextId = "inkelink-pps-ppsorder")
public interface IPpsOrderService {

    @PostMapping(value = "/provider/getdata")
    ResultVO<List<PpsOrderEntity>> getData(@RequestBody List<ConditionDto> conditions);

    @GetMapping(value = "/provider/getPpsOrderInfoByKey")
    ResultVO<PpsOrderEntity> getPpsOrderInfoByKey(@RequestParam String tpsCode);

    /**
     * 根据唯一码 或者 条码查询订单
     *
     * @param code
     * @return
     */
    @GetMapping(value = "/provider/getppsorderbysnorbarcode")
    ResultVO<PpsOrderEntity> getPpsOrderBySnOrBarcode(@RequestParam String code);

    /**
     * 根据唯一码集合 或者 条码集合查询订单
     *
     * @param codes
     * @return
     */
    @GetMapping(value = "/provider/getppsorderbysnsorbarcodes")
    ResultVO<List<PpsOrderEntity>> getPpsOrderBySnsOrBarcodes(@RequestBody List<String> codes);

    /**
     * 通过唯一码列表查询 车辆订单信息列表
     *
     * @param snCodes 唯一码列表
     * @return 车辆订单信息列表
     */
    @PostMapping("/provider/getlistbysncodes")
    ResultVO<List<PpsOrderEntity>> getListBySnCodes(@RequestBody List<String> snCodes);

}
