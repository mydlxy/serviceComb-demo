package com.ca.mfd.prc.avi.communication.controller;

import com.ca.mfd.prc.avi.communication.dto.CarInfoDto;
import com.ca.mfd.prc.avi.communication.dto.DJResultVo;
import com.ca.mfd.prc.avi.communication.entity.MidDjCarSendEntity;
import com.ca.mfd.prc.avi.communication.service.IMidDjCarSendService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @Description: 整车信息下发记录Controller
 * @author inkelink
 * @date 2023年12月25日
 * @变更说明 BY inkelink At 2023年12月25日
 */
@RestController
@RequestMapping("communication/middjcarsend")
@Tag(name = "整车信息下发记录服务", description = "整车信息下发记录")
public class MidDjCarSendController extends BaseController<MidDjCarSendEntity> {

    private IMidDjCarSendService midDjCarSendService;

    @Autowired
    public MidDjCarSendController(IMidDjCarSendService midDjCarSendService) {
        this.crudService = midDjCarSendService;
        this.midDjCarSendService = midDjCarSendService;
    }

    @GetMapping(value = "querycarinfobyvin")
    @Operation(summary = "整车信息下发")
    public ResultVO<List<CarInfoDto>> queryCarInfoByVin(String vin) {
        return midDjCarSendService.queryCarInfoByVin(vin);
    }

}