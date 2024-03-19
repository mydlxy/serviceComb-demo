package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectFdsDataStatisticsEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectFdsDataStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @Description: FDS数据统计表Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectfdsdatastatistics")
@Tag(name = "FDS数据统计表服务", description = "FDS数据统计表")
public class PqsInspectFdsDataStatisticsController extends BaseWithDefValController<PqsInspectFdsDataStatisticsEntity> {

    private IPqsInspectFdsDataStatisticsService pqsInspectFdsDataStatisticsService;

    @Autowired
    public PqsInspectFdsDataStatisticsController(IPqsInspectFdsDataStatisticsService pqsInspectFdsDataStatisticsService) {
        this.crudService = pqsInspectFdsDataStatisticsService;
        this.pqsInspectFdsDataStatisticsService = pqsInspectFdsDataStatisticsService;
    }

}