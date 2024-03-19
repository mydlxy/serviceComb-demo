package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.avi.entity.AviCutOutRelationEntity;
import com.ca.mfd.prc.avi.service.IAviCutOutRelationService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: AVI切出关联AVIController
 * @author inkelink
 * @date 2024年02月26日
 * @变更说明 BY inkelink At 2024年02月26日
 */
@RestController
@RequestMapping("avicutoutrelation")
@Tag(name = "AVI切出关联AVI服务", description = "AVI切出关联AVI")
public class AviCutOutRelationController extends BaseController<AviCutOutRelationEntity> {

    private IAviCutOutRelationService aviCutOutRelationService;

    @Autowired
    public AviCutOutRelationController(IAviCutOutRelationService aviCutOutRelationService) {
        this.crudService = aviCutOutRelationService;
        this.aviCutOutRelationService = aviCutOutRelationService;
    }

}
