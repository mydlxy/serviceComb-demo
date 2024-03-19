package com.ca.mfd.prc.eps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.service.IMidDjEpInfoService;
import com.ca.mfd.prc.eps.communication.dto.EpInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 电检EP信息组装Controller
 * @date 2023年11月27日
 */
@RestController
@RequestMapping("communication/middjepdata")
@Tag(name = "电检EP信息组装服务", description = "电检EP信息组装")
public class MidDjEpInfoController extends BaseApiController {
    @Autowired
    private IMidDjEpInfoService epInfoService;

    @GetMapping(value = "/provider/getepinfobyvin")
    @Operation(summary = "EP信息组装")
    public ResultVO<List<EpInfoDto>> getEpInfoByVin(String vin) {
        return new ResultVO<List<EpInfoDto>>().ok(epInfoService.getEpInfoByVin(vin));
    }

}