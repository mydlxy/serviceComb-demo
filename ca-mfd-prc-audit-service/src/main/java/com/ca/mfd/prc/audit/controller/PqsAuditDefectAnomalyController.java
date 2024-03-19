package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsAuditDefectAnomalyEntity;
import com.ca.mfd.prc.audit.service.IPqsAuditDefectAnomalyService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 组合缺陷库Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsauditdefectanomaly")
@Tag(name = "组合缺陷库服务", description = "组合缺陷库")
public class PqsAuditDefectAnomalyController extends BaseController<PqsAuditDefectAnomalyEntity> {

    private final IPqsAuditDefectAnomalyService pqsAuditDefectAnomalyService;

    @Autowired
    public PqsAuditDefectAnomalyController(IPqsAuditDefectAnomalyService pqsAuditDefectAnomalyService) {
        this.crudService = pqsAuditDefectAnomalyService;
        this.pqsAuditDefectAnomalyService = pqsAuditDefectAnomalyService;
    }

    @GetMapping("getalldatas")
    @Operation(summary = "获取所有缺陷库")
    public ResultVO<List<PqsAuditDefectAnomalyEntity>> getAllDatas() {
        return new ResultVO<List<PqsAuditDefectAnomalyEntity>>().ok(pqsAuditDefectAnomalyService.getAllDatas(), "获取数据成功");
    }


}