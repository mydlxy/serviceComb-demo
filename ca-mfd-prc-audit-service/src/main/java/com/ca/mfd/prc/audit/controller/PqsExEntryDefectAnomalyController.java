package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExEntryDefectAnomalyEntity;
import com.ca.mfd.prc.audit.service.IPqsExEntryDefectAnomalyService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author inkelink
 * @Description: 精致工艺缺陷记录Controller
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@RestController
@RequestMapping("pqsexentrydefectanomaly")
@Tag(name = "精致工艺缺陷记录服务", description = "精致工艺缺陷记录")
public class PqsExEntryDefectAnomalyController extends BaseController<PqsExEntryDefectAnomalyEntity> {

    private IPqsExEntryDefectAnomalyService pqsExEntryDefectAnomalyService;

    @Autowired
    public PqsExEntryDefectAnomalyController(IPqsExEntryDefectAnomalyService pqsExEntryDefectAnomalyService) {
        this.crudService = pqsExEntryDefectAnomalyService;
        this.pqsExEntryDefectAnomalyService = pqsExEntryDefectAnomalyService;
    }

    @GetMapping("getalldatas")
    @Operation(summary = "获取所有缺陷记录")
    public ResultVO<List<PqsExEntryDefectAnomalyEntity>> getAllDatas() {
        return new ResultVO<List<PqsExEntryDefectAnomalyEntity>>().ok(pqsExEntryDefectAnomalyService.getAllDatas(), "获取数据成功");
    }
}