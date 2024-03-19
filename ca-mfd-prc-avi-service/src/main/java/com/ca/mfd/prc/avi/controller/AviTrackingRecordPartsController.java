package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.avi.entity.AviTrackingRecordPartsEntity;
import com.ca.mfd.prc.avi.service.IAviTrackingRecordPartsService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 离散产品过点信息Controller
 * @author inkelink
 * @date 2023年10月31日
 * @变更说明 BY inkelink At 2023年10月31日
 */
@RestController
@RequestMapping("avitrackingrecordparts")
@Tag(name = "离散产品过点信息服务", description = "离散产品过点信息")
public class AviTrackingRecordPartsController extends BaseController<AviTrackingRecordPartsEntity> {

    private IAviTrackingRecordPartsService aviTrackingRecordPartsService;

    @Autowired
    public AviTrackingRecordPartsController(IAviTrackingRecordPartsService aviTrackingRecordPartsService) {
        this.crudService = aviTrackingRecordPartsService;
        this.aviTrackingRecordPartsService = aviTrackingRecordPartsService;
    }

}