package com.ca.mfd.prc.eps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.dto.CarCloudDeviceDto;
import com.ca.mfd.prc.eps.communication.dto.CheyunTestDto;
import com.ca.mfd.prc.eps.communication.entity.MidCarCloudDeviceEntity;
import com.ca.mfd.prc.eps.communication.service.IMidCarCloudDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 *
 * @Description: 车辆设备中间表（车云）Controller
 * @author inkelink
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
@RestController
@RequestMapping("communication/midcarclouddevice")
@Tag(name = "车辆设备中间表（车云）服务", description = "车辆设备中间表（车云）")
public class MidCarCloudDeviceController extends BaseController<MidCarCloudDeviceEntity> {

    @Autowired
    private IMidCarCloudDeviceService midCarCloudDeviceService;

    @GetMapping(value = "/provider/carclouddevicesend")
    @Operation(summary = "设备数据")
    public ResultVO carCloudDeviceSend(String vinCode) {
        return midCarCloudDeviceService.carCloudDeviceSend(vinCode);
    }

    @PostMapping(value = "querybyvins")
    @Operation(summary = "根据vin码查询")
    public ResultVO<List<CarCloudDeviceDto>> queryByVins(@RequestBody List<String> vins) {
        return midCarCloudDeviceService.queryByVins(vins);
    }
    @GetMapping(value = "querybydate")
    @Operation(summary = "根据时间范围查询")
    public ResultVO<List<CarCloudDeviceDto>> queryByDate(String startTime,String endTime) {
        return midCarCloudDeviceService.queryByDate(startTime,endTime);
    }

    @PostMapping(value = "/provider/carclouddevicesendtest")
    @Operation(summary = "设备数据-手动触发")
    public ResultVO carCloudDeviceSendTest(@RequestBody CheyunTestDto dto) {
        return midCarCloudDeviceService.carCloudDeviceSendTest(dto);
    }
}