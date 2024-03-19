package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsMmScrapRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsMmScrapRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 压铸废料管理Controller
 * @author inkelink
 * @date 2023年10月27日
 * @变更说明 BY inkelink At 2023年10月27日
 */
@RestController
@RequestMapping("pqsmmscraprecord")
@Tag(name = "压铸废料管理服务", description = "压铸废料管理")
public class PqsMmScrapRecordController extends BaseController<PqsMmScrapRecordEntity> {

    private IPqsMmScrapRecordService pqsMmScrapRecordService;

    @Autowired
    public PqsMmScrapRecordController(IPqsMmScrapRecordService pqsMmScrapRecordService) {
        this.crudService = pqsMmScrapRecordService;
        this.pqsMmScrapRecordService = pqsMmScrapRecordService;
    }

}