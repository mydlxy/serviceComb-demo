package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.ErrorCode;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.PqsEntryPageFilter;
import com.ca.mfd.prc.pqs.entity.PqsEntryAuditEntity;
import com.ca.mfd.prc.pqs.service.IPqsEntryAuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 质检工单-评审工单Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsentryaudit")
@Tag(name = "质检工单-评审工单服务", description = "质检工单-评审工单")
public class PqsEntryAuditController extends BaseController<PqsEntryAuditEntity> {

    private final IPqsEntryAuditService pqsEntryAuditService;

    @Autowired
    public PqsEntryAuditController(IPqsEntryAuditService pqsEntryAuditService) {
        this.crudService = pqsEntryAuditService;
        this.pqsEntryAuditService = pqsEntryAuditService;
    }

    @Operation(summary = "获取检验工单列表")
    @GetMapping("getpqsinspectentry")
    public ResultVO getPqsInspectEntry(PqsEntryPageFilter filter) {
        ResultVO result = new ResultVO();
        result.setCode(ErrorCode.SUCCESS);
        result.setMessage("获取数据成功!");
        try {

            PageData data = pqsEntryAuditService.getEntryList(filter);
            result.setData(data);
        } catch (Exception e) {
            result.setCode(-1);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}