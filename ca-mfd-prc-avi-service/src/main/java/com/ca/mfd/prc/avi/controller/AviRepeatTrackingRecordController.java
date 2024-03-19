package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.avi.entity.AviRepeatTrackingRecordEntity;
import com.ca.mfd.prc.avi.service.IAviRepeatTrackingRecordService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 关键过点配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@RestController
@RequestMapping("avirepeattrackingrecord")
@Tag(name = "关键过点配置")
public class AviRepeatTrackingRecordController extends BaseController<AviRepeatTrackingRecordEntity> {

    private final IAviRepeatTrackingRecordService aviRepeatTrackingRecordService;

    @Autowired
    public AviRepeatTrackingRecordController(IAviRepeatTrackingRecordService aviRepeatTrackingRecordService) {
        this.crudService = aviRepeatTrackingRecordService;
        this.aviRepeatTrackingRecordService = aviRepeatTrackingRecordService;
    }

}