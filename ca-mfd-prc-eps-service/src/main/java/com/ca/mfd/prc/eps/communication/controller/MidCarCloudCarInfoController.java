package com.ca.mfd.prc.eps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.dto.CarCloudCarInfoDto;
import com.ca.mfd.prc.eps.communication.dto.CheyunTestDto;
import com.ca.mfd.prc.eps.communication.entity.MidCarCloudCarInfoEntity;
import com.ca.mfd.prc.eps.communication.service.IMidCarCloudCarInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 *
 * @Description: 车辆基础数据中间表（车云）Controller
 * @author inkelink
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
@RestController
@RequestMapping("communication/midcarcloudcarinfo")
@Tag(name = "车辆基础数据中间表（车云）服务", description = "车辆基础数据中间表（车云）")
public class MidCarCloudCarInfoController extends BaseController<MidCarCloudCarInfoEntity> {

    @Autowired
    private IMidCarCloudCarInfoService midCarCloudCarInfoService;

    @GetMapping(value = "/provider/carcloudcarinfosend")
    @Operation(summary = "整车数据")
    public ResultVO carCloudCarInfoSend(String vinCode) {
        return midCarCloudCarInfoService.carCloudCarInfoSend(vinCode);
    }

    @PostMapping(value = "querybyvins")
    @Operation(summary = "根据vin码查询")
    public ResultVO<List<MidCarCloudCarInfoEntity>> queryByVins(@RequestBody List<String> vins) {
        return midCarCloudCarInfoService.queryByVins(vins);
    }
    @GetMapping(value = "querybydate")
    @Operation(summary = "根据时间范围查询")
    public ResultVO<List<MidCarCloudCarInfoEntity>> queryByDate(String startTime,String endTime) {
        return midCarCloudCarInfoService.queryByDate(startTime,endTime);
    }
    @PostMapping(value = "/provider/carcloudcarinfosendtest")
    @Operation(summary = "整车数据-手动触发")
    public ResultVO carCloudCarInfoSendTest(@RequestBody CheyunTestDto dto) {
        return midCarCloudCarInfoService.carCloudCarInfoSendTest(dto);
    }
    @GetMapping(value = "/provider/querybyvin")
    @Operation(summary = "根据vin码查询整车数据-法规调用")
    public ResultVO<CarCloudCarInfoDto> providerQueryByVin(String vin) {
        return midCarCloudCarInfoService.providerQueryByVin(vin);
    }
}