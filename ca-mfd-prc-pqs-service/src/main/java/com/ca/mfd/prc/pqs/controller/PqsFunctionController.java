package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsFunctionEntity;
import com.ca.mfd.prc.pqs.service.IPqsFunctionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 质检功能配置Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsfunction")
@Tag(name = "质检功能配置服务", description = "质检功能配置")
public class PqsFunctionController extends BaseController<PqsFunctionEntity> {

    private final IPqsFunctionService pqsFunctionService;

    @Autowired
    public PqsFunctionController(IPqsFunctionService pqsFunctionService) {
        this.crudService = pqsFunctionService;
        this.pqsFunctionService = pqsFunctionService;
    }

}