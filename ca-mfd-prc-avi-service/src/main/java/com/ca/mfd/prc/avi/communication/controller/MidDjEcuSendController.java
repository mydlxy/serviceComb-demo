package com.ca.mfd.prc.avi.communication.controller;

import com.ca.mfd.prc.avi.communication.dto.EcuCarInfoDto;
import com.ca.mfd.prc.avi.communication.dto.SiteInfoDto;
import com.ca.mfd.prc.avi.communication.entity.MidDjEcuSendEntity;
import com.ca.mfd.prc.avi.communication.service.IMidDjEcuSendService;
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
 * @Description: 配置字下发记录Controller
 * @author inkelink
 * @date 2023年12月25日
 * @变更说明 BY inkelink At 2023年12月25日
 */
@RestController
@RequestMapping("communication/middjecusend")
@Tag(name = "配置字下发记录服务", description = "配置字下发记录")
public class MidDjEcuSendController extends BaseController<MidDjEcuSendEntity> {

    private IMidDjEcuSendService midDjEcuSendService;

    @Autowired
    public MidDjEcuSendController(IMidDjEcuSendService midDjEcuSendService) {
        this.crudService = midDjEcuSendService;
        this.midDjEcuSendService = midDjEcuSendService;
    }

    @GetMapping(value = "queryecuinfobyvin")
    @Operation(summary = "过点信息下发")
    public ResultVO<List<EcuCarInfoDto>> queryecuinfobyvin(String vin) {
        return midDjEcuSendService.queryEcuInfoByVin(vin);
    }
}