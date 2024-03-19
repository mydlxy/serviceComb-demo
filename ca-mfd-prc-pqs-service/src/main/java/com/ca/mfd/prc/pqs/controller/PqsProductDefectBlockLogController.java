package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsProductDefectBlockLogEntity;
import com.ca.mfd.prc.pqs.service.IPqsProductDefectBlockLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 缺陷堵塞记录Controller
 * @date 2023年09月08日
 * @变更说明 BY inkelink At 2023年09月08日
 */
@RestController
@RequestMapping("pqsproductdefectblocklog")
@Tag(name = "缺陷堵塞记录服务", description = "缺陷堵塞记录")
public class PqsProductDefectBlockLogController extends BaseController<PqsProductDefectBlockLogEntity> {

    private final IPqsProductDefectBlockLogService pqsProductDefectBlockLogService;

    @Autowired
    public PqsProductDefectBlockLogController(IPqsProductDefectBlockLogService pqsProductDefectBlockLogService) {
        this.crudService = pqsProductDefectBlockLogService;
        this.pqsProductDefectBlockLogService = pqsProductDefectBlockLogService;
    }

}