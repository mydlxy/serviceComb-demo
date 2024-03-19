package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsTechnologyAlarmRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsTechnologyAlarmRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 参数预警记录Controller
 * @author inkelink
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
@RestController
@RequestMapping("pqstechnologyalarmrecord")
@Tag(name = "参数预警记录服务", description = "参数预警记录")
public class PqsTechnologyAlarmRecordController extends BaseController<PqsTechnologyAlarmRecordEntity> {

    private IPqsTechnologyAlarmRecordService pqsTechnologyAlarmRecordService;

    @Autowired
    public PqsTechnologyAlarmRecordController(IPqsTechnologyAlarmRecordService pqsTechnologyAlarmRecordService) {
        this.crudService = pqsTechnologyAlarmRecordService;
        this.pqsTechnologyAlarmRecordService = pqsTechnologyAlarmRecordService;
    }

}