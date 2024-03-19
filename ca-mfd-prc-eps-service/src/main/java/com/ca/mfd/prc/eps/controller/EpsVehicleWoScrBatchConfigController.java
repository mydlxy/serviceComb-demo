package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.dto.UpdateCountPara;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoScrBatchConfigEntity;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoScrBatchConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 批次件自动追溯配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsvehiclewoscrbatchconfig")
@Tag(name = "批次件自动追溯配置")
public class EpsVehicleWoScrBatchConfigController extends BaseController<EpsVehicleWoScrBatchConfigEntity> {

    private final IEpsVehicleWoScrBatchConfigService epsVehicleWoScrBatchConfigService;

    @Autowired
    public EpsVehicleWoScrBatchConfigController(IEpsVehicleWoScrBatchConfigService epsVehicleWoScrBatchConfigService) {
        this.crudService = epsVehicleWoScrBatchConfigService;
        this.epsVehicleWoScrBatchConfigService = epsVehicleWoScrBatchConfigService;
    }

    @Operation(summary = "获取批次追溯配置")
    @GetMapping("getscrbatchconfig")
    public ResultVO getScrBatchConfig(String woCode, String workstationCode) {
        EpsVehicleWoScrBatchConfigEntity data = epsVehicleWoScrBatchConfigService.getScrBatchConfig(woCode, workstationCode);
        if (data == null) {
            data = new EpsVehicleWoScrBatchConfigEntity();
        }
        return new ResultVO<>().ok(data, "");
    }

    @Operation(summary = "更新数量")
    @PostMapping("updatecount")
    public ResultVO updateCount(@RequestBody UpdateCountPara para) {
        epsVehicleWoScrBatchConfigService.updateCount(para.getCount(), ConvertUtils.stringToLong(para.getId()));
        epsVehicleWoScrBatchConfigService.saveChange();
        return new ResultVO<>().ok("", "操作成功");
    }

}