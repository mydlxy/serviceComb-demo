package com.ca.mfd.prc.pm.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.communication.entity.MidColorBaseEntity;
import com.ca.mfd.prc.pm.communication.service.IMidColorBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @Description: 颜色代码库中间表Controller
 * @author inkelink
 * @date 2023年10月31日
 * @变更说明 BY inkelink At 2023年10月31日
 */
@RestController
@RequestMapping("communication/midcolorbase")
@Tag(name = "颜色代码库中间表服务", description = "颜色代码库中间表")
public class MidColorBaseController extends BaseController<MidColorBaseEntity> {

    private IMidColorBaseService colorBaseService;

    @Autowired
    public MidColorBaseController(IMidColorBaseService colorBaseService) {
        this.crudService = colorBaseService;
        this.colorBaseService = colorBaseService;
    }

    @PostMapping(value = "receive")
    @Operation(summary = "保存")
    public void receive() {
        colorBaseService.receive();
    }

    @GetMapping(value = "getbyclorcode")
    @Operation(summary = "获取颜色")
    public ResultVO<List<MidColorBaseEntity>> getByClorCode(String colorCode) {
        return new ResultVO<List<MidColorBaseEntity>>().ok(colorBaseService.getByClorCode(colorCode));
    }
}