package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.entity.PqsInspectionCheckItemTypeEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectionCheckItemTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 检验类型技术配置Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsinspectioncheckitemtype")
@Tag(name = "检验类型技术配置服务", description = "检验类型技术配置")
public class PqsInspectionCheckItemTypeController extends BaseController<PqsInspectionCheckItemTypeEntity> {

    private final IPqsInspectionCheckItemTypeService pqsInspectionCheckItemTypeService;

    @Autowired
    public PqsInspectionCheckItemTypeController(IPqsInspectionCheckItemTypeService pqsInspectionCheckItemTypeService) {
        this.crudService = pqsInspectionCheckItemTypeService;
        this.pqsInspectionCheckItemTypeService = pqsInspectionCheckItemTypeService;
    }

    @GetMapping("getcheckitemtypecombo")
    @Operation(summary = "获取检查项下拉")
    public ResultVO getCheckItemtypeCombo() {
        return new ResultVO<>().ok(pqsInspectionCheckItemTypeService.getCombo(), "获取数据成功");
    }

}