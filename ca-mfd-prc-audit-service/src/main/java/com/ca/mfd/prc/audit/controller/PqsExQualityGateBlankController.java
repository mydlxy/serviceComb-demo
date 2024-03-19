package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExQualityGateBlankEntity;
import com.ca.mfd.prc.audit.service.IPqsExQualityGateBlankService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author inkelink
 * @Description: 精致工艺 QG检验项-色块Controller
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@RestController
@RequestMapping("pqsexqualitygateblank")
@Tag(name = "精致工艺 QG检验项-色块服务", description = "精致工艺 QG检验项-色块")
public class PqsExQualityGateBlankController extends BaseController<PqsExQualityGateBlankEntity> {

    private IPqsExQualityGateBlankService pqsExQualityGateBlankService;

    @Autowired
    public PqsExQualityGateBlankController(IPqsExQualityGateBlankService pqsExQualityGateBlankService) {
        this.crudService = pqsExQualityGateBlankService;
        this.pqsExQualityGateBlankService = pqsExQualityGateBlankService;
    }

}