package com.ca.mfd.prc.pqs.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.communication.dto.CarFenceConditonDto;
import com.ca.mfd.prc.pqs.utils.CheyunHttpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("communication/cheyun")
@Tag(name = "车云寻车接口服务", description = "车云寻车接口服务")
public class CheyunController extends BaseApiController {

    @Autowired
    private CheyunHttpUtils cheyunHttpUtils;
    private static final Logger logger = LoggerFactory.getLogger(CheyunController.class);

    @GetMapping(value = "/car-fence-conditon")
    @Operation(summary = "GPS寻车接口")
    public ResultVO<CarFenceConditonDto> carFenceConditon(String vin, String fenceCode) {
        ResultVO<CarFenceConditonDto> resultVO = cheyunHttpUtils.carFenceConditon(vin, fenceCode);
        return resultVO;
    }
}
