package com.ca.mfd.prc.avi.communication.controller;

import com.ca.mfd.prc.avi.communication.entity.MidDjEpSendEntity;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.EpInfoDto;
import com.ca.mfd.prc.avi.communication.service.IMidDjEpSendService;
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
 * @author inkelink
 * @Description: EP信息下发记录Controller
 * @date 2023年12月29日
 * @变更说明 BY inkelink At 2023年12月29日
 */
@RestController
@RequestMapping("communication/middjepsend")
@Tag(name = "EP下发记录服务", description = "EP下发记录服务")
public class MidDjEpSendController extends BaseController<MidDjEpSendEntity> {

    private IMidDjEpSendService midDjEpSendService;

    @Autowired
    public MidDjEpSendController(IMidDjEpSendService midDjEpSendService) {
        this.crudService = midDjEpSendService;
        this.midDjEpSendService = midDjEpSendService;
    }

    @GetMapping(value = "queryepinfobyvin")
    @Operation(summary = "EP信息下发")
    public ResultVO<List<EpInfoDto>> queryEpInfoByVin(String vin) {
        return midDjEpSendService.queryEpInfoByVin(vin);
    }

}