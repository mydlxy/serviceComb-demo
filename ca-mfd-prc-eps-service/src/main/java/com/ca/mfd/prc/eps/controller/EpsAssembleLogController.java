package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.entity.EpsAssembleLogEntity;
import com.ca.mfd.prc.eps.service.IEpsAssembleLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 装配单日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsassemblelog")
@Tag(name = "装配单日志")
public class EpsAssembleLogController extends BaseController<EpsAssembleLogEntity> {

    private final IEpsAssembleLogService epsAssembleLogService;

    @Autowired
    public EpsAssembleLogController(IEpsAssembleLogService epsAssembleLogService) {
        this.crudService = epsAssembleLogService;
        this.epsAssembleLogService = epsAssembleLogService;
    }

    @Operation(summary = "获取装配指示详细数据")
    @GetMapping("/provider/getassemblelogdata")
    public ResultVO<List<EpsAssembleLogEntity>> getAssembleLogData(@RequestParam String sn) {
        return new ResultVO<List<EpsAssembleLogEntity>>().ok(epsAssembleLogService.getAssembleLogData(sn));
    }

    @Operation(summary = "添加装配日志")
    @GetMapping("addinsertlog")
    public ResultVO<String> addInsertLog(String sn, String tplcode) {
        ResultVO<String> result = new ResultVO<>();
        epsAssembleLogService.addInsertLog(sn, tplcode);
        epsAssembleLogService.saveChange();
        return result.ok("", "操作成功");
    }

}