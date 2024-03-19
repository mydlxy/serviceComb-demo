package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsMmFreezeRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsMmFreezeRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 物料质量冻结记录Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsmmfreezerecord")
@Tag(name = "物料质量冻结记录服务", description = "物料质量冻结记录")
public class PqsMmFreezeRecordController extends BaseController<PqsMmFreezeRecordEntity> {

    private final IPqsMmFreezeRecordService pqsMmFreezeRecordService;

    @Autowired
    public PqsMmFreezeRecordController(IPqsMmFreezeRecordService pqsMmFreezeRecordService) {
        this.crudService = pqsMmFreezeRecordService;
        this.pqsMmFreezeRecordService = pqsMmFreezeRecordService;
    }

}