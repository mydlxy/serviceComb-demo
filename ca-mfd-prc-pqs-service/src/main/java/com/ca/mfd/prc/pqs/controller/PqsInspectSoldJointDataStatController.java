package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectSoldJointDataStatEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectSoldJointDataStatService;
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
 * @Description: 焊点数据统计Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectsoldjointdatastat")
@Tag(name = "焊点数据统计服务", description = "焊点数据统计")
public class PqsInspectSoldJointDataStatController extends BaseWithDefValController<PqsInspectSoldJointDataStatEntity> {

    private IPqsInspectSoldJointDataStatService pqsInspectSoldJointDataStatService;

    @Autowired
    public PqsInspectSoldJointDataStatController(IPqsInspectSoldJointDataStatService pqsInspectSoldJointDataStatService) {
        this.crudService = pqsInspectSoldJointDataStatService;
        this.pqsInspectSoldJointDataStatService = pqsInspectSoldJointDataStatService;
    }

}