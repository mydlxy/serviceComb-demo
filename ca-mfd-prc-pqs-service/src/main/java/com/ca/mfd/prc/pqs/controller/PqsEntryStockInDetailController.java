package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsEntryStockInDetailEntity;
import com.ca.mfd.prc.pqs.service.IPqsEntryStockInDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 入库质检工单Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsentrystockindetail")
@Tag(name = "入库质检工单服务", description = "入库质检工单")
public class PqsEntryStockInDetailController extends BaseController<PqsEntryStockInDetailEntity> {

    private final IPqsEntryStockInDetailService pqsEntryStockInDetailService;

    @Autowired
    public PqsEntryStockInDetailController(IPqsEntryStockInDetailService pqsEntryStockInDetailService) {
        this.crudService = pqsEntryStockInDetailService;
        this.pqsEntryStockInDetailService = pqsEntryStockInDetailService;
    }

}