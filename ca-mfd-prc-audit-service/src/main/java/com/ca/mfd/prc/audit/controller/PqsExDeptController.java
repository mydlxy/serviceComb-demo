package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.entity.PqsExDeptEntity;
import com.ca.mfd.prc.audit.service.IPqsExDeptService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboDataDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author inkelink
 * @Description: 精致工艺责任部门配置Controller
 * @date 2024年01月29日
 * @变更说明 BY inkelink At 2024年01月29日
 */
@RestController
@RequestMapping("pqsexdept")
@Tag(name = "精致工艺责任部门配置服务", description = "精致工艺责任部门配置")
public class PqsExDeptController extends BaseController<PqsExDeptEntity> {

    private IPqsExDeptService pqsExDeptService;

    @Autowired
    public PqsExDeptController(IPqsExDeptService pqsExDeptService) {
        this.crudService = pqsExDeptService;
        this.pqsExDeptService = pqsExDeptService;
    }

    @GetMapping("getcombolist")
    @Operation(summary = "获取责任部门下拉")
    public ResultVO getAuditGradeCombo() {
        return new ResultVO<>().ok(pqsExDeptService.getComboList(), "获取数据成功");
    }
}