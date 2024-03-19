package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsStrengthenRainDetectionRecordsEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsStrengthenRainDetectionRecordsService;
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
 * @Description: 强化淋雨检测记录Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsesstrengthenraindetectionrecords")
@Tag(name = "强化淋雨检测记录服务", description = "强化淋雨检测记录")
public class PqsEsStrengthenRainDetectionRecordsController extends BaseWithDefValController<PqsEsStrengthenRainDetectionRecordsEntity> {

    private IPqsEsStrengthenRainDetectionRecordsService pqsEsStrengthenRainDetectionRecordsService;

    @Autowired
    public PqsEsStrengthenRainDetectionRecordsController(IPqsEsStrengthenRainDetectionRecordsService pqsEsStrengthenRainDetectionRecordsService) {
        this.crudService = pqsEsStrengthenRainDetectionRecordsService;
        this.pqsEsStrengthenRainDetectionRecordsService = pqsEsStrengthenRainDetectionRecordsService;
    }

}