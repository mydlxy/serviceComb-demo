package com.ca.mfd.prc.pm.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.communication.service.IMidMaterialMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 物料主数据Controller
 * @author inkelink
 * @date 2023年10月24日
 * @变更说明 BY inkelink At 2023年10月24日
 */
@RestController
@RequestMapping("communication/midmaterialmain")
@Tag(name = "物料主数据服务", description = "物料主数据")
public class MidMaterialMasterController extends BaseApiController {
    @Autowired
    private IMidMaterialMasterService materialMasterService;

    @PostMapping(value = "receive")
    @Operation(summary = "保存")
    public void receive() {
        materialMasterService.receive();
    }


    @GetMapping(value = "excute")
    @Operation(summary = "特征主数据执行数据处理逻辑")
    public ResultVO<String> excute(String logid) {
        materialMasterService.excute(logid);
        return new ResultVO<String>().ok("", "成功");
    }

}