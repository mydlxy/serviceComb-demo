package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateBlankEntity;
import com.ca.mfd.prc.pqs.service.IPqsQualityGateBlankService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: QG检验项-色块Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsqualitygateblank")
@Tag(name = "QG检验项-色块服务", description = "QG检验项-色块")
public class PqsQualityGateBlankController extends BaseController<PqsQualityGateBlankEntity> {

    private final IPqsQualityGateBlankService pqsQualityGateBlankService;

    @Autowired
    public PqsQualityGateBlankController(IPqsQualityGateBlankService pqsQualityGateBlankService) {
        this.crudService = pqsQualityGateBlankService;
        this.pqsQualityGateBlankService = pqsQualityGateBlankService;
    }

}