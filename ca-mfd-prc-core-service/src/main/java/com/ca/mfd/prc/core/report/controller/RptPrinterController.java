package com.ca.mfd.prc.core.report.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.report.dto.EditStatusDto;
import com.ca.mfd.prc.core.report.entity.RptPrinterEntity;
import com.ca.mfd.prc.core.report.service.IRptPrinterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 报表打印机Controller
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
@RestController
@RequestMapping("report/rptprinter")
@Tag(name = "报表打印机服务", description = "报表打印机")
public class RptPrinterController extends BaseController<RptPrinterEntity> {

    private final IRptPrinterService rptPrinterService;

    @Autowired
    public RptPrinterController(IRptPrinterService rptPrinterService) {
        this.crudService = rptPrinterService;
        this.rptPrinterService = rptPrinterService;
    }
        //    @PostMapping(value = "getpagedata", produces = {MediaType.APPLICATION_JSON_VALUE})
        //    @Operation(summary = "获取分页数据")
        //    @Override
        //    public ResultVO<PageData<RptPrinterEntity>> getPageData(@RequestBody PageDataDto model) {
        //
        //        return null;
        //    }
}