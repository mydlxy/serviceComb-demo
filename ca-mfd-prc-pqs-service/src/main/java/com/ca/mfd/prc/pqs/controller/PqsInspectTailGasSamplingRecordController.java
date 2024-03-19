package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectTailGasSamplingRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectTailGasSamplingRecordService;
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
 * @Description: 检测线尾气抽检记录Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspecttailgassamplingrecord")
@Tag(name = "检测线尾气抽检记录服务", description = "检测线尾气抽检记录")
public class PqsInspectTailGasSamplingRecordController extends BaseWithDefValController<PqsInspectTailGasSamplingRecordEntity> {

    private IPqsInspectTailGasSamplingRecordService pqsInspectTailGasSamplingRecordService;

    @Autowired
    public PqsInspectTailGasSamplingRecordController(IPqsInspectTailGasSamplingRecordService pqsInspectTailGasSamplingRecordService) {
        this.crudService = pqsInspectTailGasSamplingRecordService;
        this.pqsInspectTailGasSamplingRecordService = pqsInspectTailGasSamplingRecordService;
    }

}