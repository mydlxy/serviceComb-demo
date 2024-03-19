package com.ca.mfd.prc.avi.communication.controller;

import com.ca.mfd.prc.avi.communication.dto.CarInfoDto;
import com.ca.mfd.prc.avi.communication.dto.SiteInfoDto;
import com.ca.mfd.prc.avi.communication.entity.MidDjSiteSendEntity;
import com.ca.mfd.prc.avi.communication.service.IMidDjSiteSendService;
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
 * @Description: 过点信息下发记录Controller
 * @author inkelink
 * @date 2023年12月25日
 * @变更说明 BY inkelink At 2023年12月25日
 */
@RestController
@RequestMapping("communication/middjsitesend")
@Tag(name = "过点信息下发记录服务", description = "过点信息下发记录")
public class MidDjSiteSendController extends BaseController<MidDjSiteSendEntity> {

    private IMidDjSiteSendService midDjSiteSendService;

    @Autowired
    public MidDjSiteSendController(IMidDjSiteSendService midDjSiteSendService) {
        this.crudService = midDjSiteSendService;
        this.midDjSiteSendService = midDjSiteSendService;
    }

    @GetMapping(value = "querysiteinfobyvin")
    @Operation(summary = "过点信息下发")
    public ResultVO<List<SiteInfoDto>> querySiteInfoByVin(String vin) {
        return midDjSiteSendService.querySiteInfoByVin(vin);
    }

}