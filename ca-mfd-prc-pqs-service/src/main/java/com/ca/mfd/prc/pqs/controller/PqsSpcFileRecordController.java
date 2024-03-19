package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsSpcFileRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsSpcFileRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: Controller
 * @author inkelink
 * @date 2023年11月30日
 * @变更说明 BY inkelink At 2023年11月30日
 */
@RestController
@RequestMapping("pqsspcfilerecord")
@Tag(name = "服务", description = "")
public class PqsSpcFileRecordController extends BaseController<PqsSpcFileRecordEntity> {

    private IPqsSpcFileRecordService pqsSpcFileRecordService;

    @Autowired
    public PqsSpcFileRecordController(IPqsSpcFileRecordService pqsSpcFileRecordService) {
        this.crudService = pqsSpcFileRecordService;
        this.pqsSpcFileRecordService = pqsSpcFileRecordService;
    }

}