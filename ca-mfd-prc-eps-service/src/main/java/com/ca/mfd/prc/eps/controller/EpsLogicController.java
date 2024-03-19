package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.enums.WoDataEnum;
import com.ca.mfd.prc.eps.service.IEpsLogicService;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * EpsLogicController
 *
 * @author inkelink
 * @date 2023/09/05
 */
@RestController
@RequestMapping("epslogic")
@Tag(name = "车辆操作信息")
public class EpsLogicController {
    @Autowired
    private IEpsLogicService epsLogicService;
    @Autowired
    private IEpsVehicleWoService epsVehicleWoService;

    /**
     * 提交生产数据
     *
     * @param woId
     * @param workplaceId
     * @param productCode
     * @param data
     * @param decviceName
     * @param woDataEnum
     * @return 执行结果
     */
    @PostMapping("/provider/savevehicledata")
    @Operation(summary = "提交生产数据")
    public ResultVO<String> saveVehicleData(@RequestParam String woId, @RequestParam String workplaceId,
                                            @RequestParam String productCode, @RequestParam String data,
                                            @RequestParam String decviceName, @RequestParam WoDataEnum woDataEnum) {
        ResultVO<String> result = new ResultVO<>();
        epsLogicService.saveVehicleData(Long.valueOf(woId), workplaceId, productCode, data, decviceName, woDataEnum);
        epsVehicleWoService.saveChange();
        return result.ok("操作成功");
    }

}
