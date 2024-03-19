package com.ca.mfd.prc.pmc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.entity.PmcAlarmEquipmntModelEntity;
import com.ca.mfd.prc.pmc.service.IPmcAlarmEquipmntModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 设备建模
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("pmcalarmequipmntmodel")
@Tag(name = "设备建模")
public class PmcAlarmEquipmntModelController extends BaseController<PmcAlarmEquipmntModelEntity> {

    private final IPmcAlarmEquipmntModelService pmcAlarmEquipmntModelService;

    @Autowired
    public PmcAlarmEquipmntModelController(IPmcAlarmEquipmntModelService pmcAlarmEquipmntModelService) {
        this.crudService = pmcAlarmEquipmntModelService;
        this.pmcAlarmEquipmntModelService = pmcAlarmEquipmntModelService;
    }

    @Operation(summary = "获取一级报警设备")
    @GetMapping("getalarmequipmntlevelone")
    public ResultVO<List<ComboInfoDTO>> getAlarmEquipmntLevelOne(String shopCode) {
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        return result.ok(pmcAlarmEquipmntModelService.getAlarmEquipmntLevelOne(shopCode));
    }

    @Operation(summary = "根据搜索条件获取设备建模列表")
    @GetMapping("getalarmequipmntmodelbyshop")
    public ResultVO<List<PmcAlarmEquipmntModelEntity>> getAlarmEquipmntModelByShop(String shop, String key) {
        ResultVO<List<PmcAlarmEquipmntModelEntity>> result = new ResultVO<>();
        return result.ok(pmcAlarmEquipmntModelService.getAlarmEquipmntModelByShop(shop, key));
    }

    @Operation(summary = "获取所有数据")
    @GetMapping(value = "getalldatas")
    public ResultVO<List<PmcAlarmEquipmntModelEntity>> getAllDatas() {
        ResultVO<List<PmcAlarmEquipmntModelEntity>> result = new ResultVO<>();
        List<PmcAlarmEquipmntModelEntity> data = pmcAlarmEquipmntModelService.getAllDatas();
        return result.ok(data);
    }
}