package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsGradeEntity;
import com.ca.mfd.prc.pqs.service.IPqsGradeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 缺陷等级配置Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsgrade")
@Tag(name = "缺陷等级配置服务", description = "缺陷等级配置")
public class PqsGradeController extends BaseController<PqsGradeEntity> {

    private final IPqsGradeService pqsGradeService;

    @Autowired
    public PqsGradeController(IPqsGradeService pqsGradeService) {
        this.crudService = pqsGradeService;
        this.pqsGradeService = pqsGradeService;
    }

}