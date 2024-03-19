package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsAuditEntryEntity;
import com.ca.mfd.prc.audit.service.IPqsAuditEntryService;
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
 * @Description: AUDIT评审单Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsauditentry")
@Tag(name = "AUDIT评审单服务", description = "AUDIT评审单")
public class PqsAuditEntryController extends BaseController<PqsAuditEntryEntity> {

    private final IPqsAuditEntryService pqsAuditEntryService;

    @Autowired
    public PqsAuditEntryController(IPqsAuditEntryService pqsAuditEntryService) {
        this.crudService = pqsAuditEntryService;
        this.pqsAuditEntryService = pqsAuditEntryService;
    }

    /**
     * 获取所有缺陷位置
     *
     * @return
     */
    @GetMapping("getalldatas")
    @Operation(summary = "获取所有缺陷位置")
    public ResultVO<List<PqsAuditEntryEntity>> getAllDatas() {
        return new ResultVO<List<PqsAuditEntryEntity>>().ok(pqsAuditEntryService.getAllDatas(), "获取数据成功");
    }


}