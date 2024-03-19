package com.ca.mfd.prc.eps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.dto.CheyunTestDto;
import com.ca.mfd.prc.eps.communication.entity.MidCarCloudCheckEntity;
import com.ca.mfd.prc.eps.communication.service.IMidCarCloudCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author inkelink
 * @Description: 终检完成数据中间表（车云）Controller
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
@RestController
@RequestMapping("communication/midcarcloudcheck")
@Tag(name = "终检完成数据中间表（车云）服务", description = "终检完成数据中间表（车云）")
public class MidCarCloudCheckController extends BaseController<MidCarCloudCheckEntity> {

    @Autowired
    private IMidCarCloudCheckService midCarCloudCheckService;

    @GetMapping(value = "/provider/carcloudchecksend")
    @Operation(summary = "终检数据")
    public ResultVO carCloudCheckSend(String vinCode ) {
        return midCarCloudCheckService.carCloudCheckSend(vinCode);
    }

    @PostMapping(value = "/provider/carcloudchecksendtest")
    @Operation(summary = "终检数据-手动触发")
    public ResultVO carCloudCheckSendTest(@RequestBody CheyunTestDto dto) {
        return midCarCloudCheckService.carCloudCheckSendTest(dto);
    }
}