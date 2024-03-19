package com.ca.mfd.prc.pps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.communication.dto.CarInfoDto;
import com.ca.mfd.prc.pps.communication.dto.EcuCarInfoDto;
import com.ca.mfd.prc.pps.communication.dto.SiteInfoDto;
import com.ca.mfd.prc.pps.communication.entity.MidDjTestSendEntity;
import com.ca.mfd.prc.pps.communication.service.IMidElectricalInspectionSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("communication/midelectricalinspection")
@Tag(name = "电检下发服务", description = "电检下发")
public class MidElectricalInspectionSendController extends BaseApiController {

    @Autowired
    private IMidElectricalInspectionSendService electricalInspectionSendService;

    @GetMapping(value = "/provider/getecucarinfobyvin")
    @Operation(summary = "软件信息数据")
    public ResultVO<EcuCarInfoDto> getEcuCarInfoByVin(String vin) {
        return new ResultVO<EcuCarInfoDto>().ok(electricalInspectionSendService.getEcuCarInfoByVin(vin));
    }

    @GetMapping(value = "/provider/getcarinfobyvin")
    @Operation(summary = "整车信息数据")
    public ResultVO<CarInfoDto> getCarInfoByVin(String vin) {
        return new ResultVO<CarInfoDto>().ok(electricalInspectionSendService.getCarInfoByVin(vin));
    }

    @GetMapping(value = "/provider/getsiteinfobyvin")
    @Operation(summary = "过点信息数据")
    public ResultVO<SiteInfoDto> getSiteInfoByVin(String vin) {
        return new ResultVO<SiteInfoDto>().ok(electricalInspectionSendService.getSiteInfoByVin(vin));
    }



    @GetMapping(value = "/provider/getecucarinfobyvintest")
    @Operation(summary = "软件信息数据-手动")
    public ResultVO<EcuCarInfoDto> getEcuCarInfoByVinTest(@RequestBody MidDjTestSendEntity dto) {
        return new ResultVO<EcuCarInfoDto>().ok(electricalInspectionSendService.getEcuCarInfoByVinTest(dto));
    }

    @GetMapping(value = "/provider/getcarinfobyvintest")
    @Operation(summary = "整车信息数据-手动")
    public ResultVO<CarInfoDto> getCarInfoByVinTest(@RequestBody MidDjTestSendEntity dto) {
        return new ResultVO<CarInfoDto>().ok(electricalInspectionSendService.getCarInfoByVinTest(dto));
    }


}
