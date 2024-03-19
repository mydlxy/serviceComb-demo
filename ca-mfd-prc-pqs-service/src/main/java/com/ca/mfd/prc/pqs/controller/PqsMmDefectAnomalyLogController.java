package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsMmDefectAnomalyLogEntity;
import com.ca.mfd.prc.pqs.service.IPqsMmDefectAnomalyLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 零部件缺陷活动日志Controller
 * @author inkelink
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
@RestController
@RequestMapping("pqsmmdefectanomalylog")
@Tag(name = "零部件缺陷活动日志服务", description = "零部件缺陷活动日志")
public class PqsMmDefectAnomalyLogController extends BaseController<PqsMmDefectAnomalyLogEntity> {

    private IPqsMmDefectAnomalyLogService pqsMmDefectAnomalyLogService;

    @Autowired
    public PqsMmDefectAnomalyLogController(IPqsMmDefectAnomalyLogService pqsMmDefectAnomalyLogService) {
        this.crudService = pqsMmDefectAnomalyLogService;
        this.pqsMmDefectAnomalyLogService = pqsMmDefectAnomalyLogService;
    }

}