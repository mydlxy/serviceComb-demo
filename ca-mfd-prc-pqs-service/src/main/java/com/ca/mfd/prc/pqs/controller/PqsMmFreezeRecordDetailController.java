package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsMmFreezeRecordDetailEntity;
import com.ca.mfd.prc.pqs.service.IPqsMmFreezeRecordDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 物料质量冻结明细Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsmmfreezerecorddetail")
@Tag(name = "物料质量冻结明细服务", description = "物料质量冻结明细")
public class PqsMmFreezeRecordDetailController extends BaseController<PqsMmFreezeRecordDetailEntity> {

    private final IPqsMmFreezeRecordDetailService pqsMmFreezeRecordDetailService;

    @Autowired
    public PqsMmFreezeRecordDetailController(IPqsMmFreezeRecordDetailService pqsMmFreezeRecordDetailService) {
        this.crudService = pqsMmFreezeRecordDetailService;
        this.pqsMmFreezeRecordDetailService = pqsMmFreezeRecordDetailService;
    }

}