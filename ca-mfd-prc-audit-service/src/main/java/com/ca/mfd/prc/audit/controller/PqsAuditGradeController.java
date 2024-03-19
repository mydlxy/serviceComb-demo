package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsAuditGradeEntity;
import com.ca.mfd.prc.audit.service.IPqsAuditGradeService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: AUDIT缺陷等级配置Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsauditgrade")
@Tag(name = "AUDIT缺陷等级配置服务", description = "AUDIT缺陷等级配置")
public class PqsAuditGradeController extends BaseController<PqsAuditGradeEntity> {

    private final IPqsAuditGradeService pqsAuditGradeService;

    @Autowired
    public PqsAuditGradeController(IPqsAuditGradeService pqsAuditGradeService) {
        this.crudService = pqsAuditGradeService;
        this.pqsAuditGradeService = pqsAuditGradeService;
    }

    /**
     * 获取扣分等级
     *
     * @return
     */
    @PostMapping("getcombolist")
    @Operation(summary = "获取扣分等级")
    public ResultVO<List<ComboInfoDTO>> getComboList() {
        return new ResultVO<List<ComboInfoDTO>>().ok(pqsAuditGradeService.getComboList(), "获取数据成功");
    }
}