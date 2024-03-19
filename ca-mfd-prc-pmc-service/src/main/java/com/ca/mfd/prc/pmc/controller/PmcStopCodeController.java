package com.ca.mfd.prc.pmc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.entity.PmcStopCodeEntity;
import com.ca.mfd.prc.pmc.service.IPmcStopCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 停线代码
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("pmcstopcode")
@Tag(name = "停线代码")
public class PmcStopCodeController extends BaseController<PmcStopCodeEntity> {

    @Autowired
    private IPmcStopCodeService pmcStopCodeService;

    @Autowired
    public PmcStopCodeController(IPmcStopCodeService pmcStopCodeService) {
        this.crudService = pmcStopCodeService;
        this.pmcStopCodeService = pmcStopCodeService;
    }

    @Operation(summary = "获取所有数据")
    @GetMapping(value = "getalldatas")
    public ResultVO<List<PmcStopCodeEntity>> getAllDatas() {
        ResultVO<List<PmcStopCodeEntity>> result = new ResultVO<>();
        List<PmcStopCodeEntity> data = pmcStopCodeService.getAllDatas();
        return result.ok(data);
    }
}