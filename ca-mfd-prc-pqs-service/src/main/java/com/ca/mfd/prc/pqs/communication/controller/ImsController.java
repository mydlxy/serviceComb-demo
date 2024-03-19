package com.ca.mfd.prc.pqs.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.communication.dto.LmsSupplierDto;
import com.ca.mfd.prc.pqs.communication.service.ILmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("communication/lms")
@Tag(name = "调用lms服务", description = "调用lms服务")
public class ImsController extends BaseApiController {

    @Autowired
    private ILmsService lmsService;
    private static final Logger logger = LoggerFactory.getLogger(ImsController.class);

    @GetMapping(value = "/querysupplrelbymaterialcode")
    @Operation(summary = "根据物料编码查询供应商信息")
    public ResultVO<List<LmsSupplierDto>> querysupplrelbymaterialcode(String materialCode) {
        ResultVO<List<LmsSupplierDto>> result = new ResultVO<>();
        List<LmsSupplierDto> list = lmsService.querysupplrelbymaterialcode(materialCode);
        return result.ok(list);
    }
}
