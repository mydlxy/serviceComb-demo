package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsProductQgCheckListRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsProductQgCheckListRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 产品-QG必检项Controller
 * @date 2023年09月07日
 * @变更说明 BY inkelink At 2023年09月07日
 */
@RestController
@RequestMapping("pqsproductqgchecklistrecord")
@Tag(name = "产品-QG必检项服务", description = "产品-QG必检项")
public class PqsProductQgCheckListRecordController extends BaseController<PqsProductQgCheckListRecordEntity> {

    private final IPqsProductQgCheckListRecordService pqsProductQgCheckListRecordService;

    @Autowired
    public PqsProductQgCheckListRecordController(IPqsProductQgCheckListRecordService pqsProductQgCheckListRecordService) {
        this.crudService = pqsProductQgCheckListRecordService;
        this.pqsProductQgCheckListRecordService = pqsProductQgCheckListRecordService;
    }

}