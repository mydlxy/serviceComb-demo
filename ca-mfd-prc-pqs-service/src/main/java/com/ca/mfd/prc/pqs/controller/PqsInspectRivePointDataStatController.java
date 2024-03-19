package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectRivePointDataStatEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectRivePointDataStatService;
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
 * @Description: SPR铆点数据统计表Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectrivepointdatastat")
@Tag(name = "SPR铆点数据统计表服务", description = "SPR铆点数据统计表")
public class PqsInspectRivePointDataStatController extends BaseWithDefValController<PqsInspectRivePointDataStatEntity> {

    private IPqsInspectRivePointDataStatService pqsInspectRivePointDataStatService;

    @Autowired
    public PqsInspectRivePointDataStatController(IPqsInspectRivePointDataStatService pqsInspectRivePointDataStatService) {
        this.crudService = pqsInspectRivePointDataStatService;
        this.pqsInspectRivePointDataStatService = pqsInspectRivePointDataStatService;
    }

}