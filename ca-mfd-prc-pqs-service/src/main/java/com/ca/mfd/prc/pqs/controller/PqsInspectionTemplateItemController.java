package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsInspectionTemplateItemEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectionTemplateItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 检验模板-项目Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsinspectiontemplateitem")
@Tag(name = "检验模板-项目服务", description = "检验模板-项目")
public class PqsInspectionTemplateItemController extends BaseController<PqsInspectionTemplateItemEntity> {

    private final IPqsInspectionTemplateItemService pqsInspectionTemplateItemService;

    @Autowired
    public PqsInspectionTemplateItemController(IPqsInspectionTemplateItemService pqsInspectionTemplateItemService) {
        this.crudService = pqsInspectionTemplateItemService;
        this.pqsInspectionTemplateItemService = pqsInspectionTemplateItemService;
    }

}