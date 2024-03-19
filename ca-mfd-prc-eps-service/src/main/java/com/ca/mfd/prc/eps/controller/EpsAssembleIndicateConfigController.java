package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.entity.EpsAssembleIndicateConfigEntity;
import com.ca.mfd.prc.eps.service.IEpsAssembleIndicateConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 装配指示配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsassembleindicateconfig")
@Tag(name = "装配指示配置")
public class EpsAssembleIndicateConfigController extends BaseController<EpsAssembleIndicateConfigEntity> {

    private final IEpsAssembleIndicateConfigService epsAssembleIndicateConfigService;

    @Autowired
    public EpsAssembleIndicateConfigController(IEpsAssembleIndicateConfigService epsAssembleIndicateConfigService) {
        this.crudService = epsAssembleIndicateConfigService;
        this.epsAssembleIndicateConfigService = epsAssembleIndicateConfigService;
    }

    @PostMapping("getworkstationdata")
    @Operation(summary = "获取工位上面的装配指示列表")
    public ResultVO<List<EpsAssembleIndicateConfigEntity>> getWorkstationData(@RequestParam String sn, @RequestParam String workstationCode) {
        return new ResultVO<List<EpsAssembleIndicateConfigEntity>>().ok(epsAssembleIndicateConfigService.getWorkstationData(sn, workstationCode));
    }

}