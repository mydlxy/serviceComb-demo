package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExGradeEntity;
import com.ca.mfd.prc.audit.service.IPqsExGradeService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author inkelink
 * @Description: 精致工艺扣分等级配置Controller
 * @date 2024年01月29日
 * @变更说明 BY inkelink At 2024年01月29日
 */
@RestController
@RequestMapping("pqsexgrade")
@Tag(name = "精致工艺扣分等级配置服务", description = "精致工艺扣分等级配置")
public class PqsExGradeController extends BaseController<PqsExGradeEntity> {

    private IPqsExGradeService pqsExGradeService;

    @Autowired
    public PqsExGradeController(IPqsExGradeService pqsExGradeService) {
        this.crudService = pqsExGradeService;
        this.pqsExGradeService = pqsExGradeService;
    }

    @GetMapping("getcombolist")
    @Operation(summary = "获取扣分等级下拉")
    public ResultVO getAuditGradeCombo() {
        return new ResultVO<>().ok(pqsExGradeService.getComboList(), "获取数据成功");
    }
}