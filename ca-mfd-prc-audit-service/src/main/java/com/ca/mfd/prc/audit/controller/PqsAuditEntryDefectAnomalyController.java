package com.ca.mfd.prc.audit.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.audit.entity.PqsAuditEntryDefectAnomalyEntity;
import com.ca.mfd.prc.audit.service.IPqsAuditEntryDefectAnomalyService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.EncryptionUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: AUDIT缺陷记录Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsauditentrydefectanomaly")
@Tag(name = "AUDIT缺陷记录服务", description = "AUDIT缺陷记录")
public class PqsAuditEntryDefectAnomalyController extends BaseController<PqsAuditEntryDefectAnomalyEntity> {

    private final IPqsAuditEntryDefectAnomalyService pqsAuditEntryDefectAnomalyService;

    @Autowired
    public PqsAuditEntryDefectAnomalyController(IPqsAuditEntryDefectAnomalyService pqsAuditEntryDefectAnomalyService) {
        this.crudService = pqsAuditEntryDefectAnomalyService;
        this.pqsAuditEntryDefectAnomalyService = pqsAuditEntryDefectAnomalyService;
    }

    @GetMapping("getalldatas")
    @Operation(summary = "获取所有缺陷记录")
    public ResultVO<List<PqsAuditEntryDefectAnomalyEntity>> getAllDatas() {
        return new ResultVO<List<PqsAuditEntryDefectAnomalyEntity>>().ok(pqsAuditEntryDefectAnomalyService.getAllDatas(), "获取数据成功");
    }

    /**
     * OT缺陷列表-修改备注
     * @param req
     * @return
     */
    @PostMapping(value = "updateremark")
    @Operation(summary = "OT缺陷列表-修改备注")
    public ResultVO updateRemark(@RequestBody PqsAuditEntryDefectAnomalyEntity req) {
        pqsAuditEntryDefectAnomalyService.updateRemark(req);
        return new ResultVO().ok("","操作成功");
    }




}