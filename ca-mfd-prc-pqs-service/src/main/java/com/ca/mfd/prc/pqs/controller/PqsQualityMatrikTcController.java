package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikTcEntity;
import com.ca.mfd.prc.pqs.service.IPqsQualityMatrikTcService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 百格图-车型Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsqualitymatriktc")
@Tag(name = "百格图-车型服务", description = "百格图-车型")
public class PqsQualityMatrikTcController extends BaseController<PqsQualityMatrikTcEntity> {

    private final IPqsQualityMatrikTcService pqsQualityMatrikTcService;

    @Autowired
    public PqsQualityMatrikTcController(IPqsQualityMatrikTcService pqsQualityMatrikTcService) {
        this.crudService = pqsQualityMatrikTcService;
        this.pqsQualityMatrikTcService = pqsQualityMatrikTcService;
    }

}