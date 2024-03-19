package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.dto.BarcodePara;
import com.ca.mfd.prc.eps.dto.BindingUnitPara;
import com.ca.mfd.prc.eps.dto.GetModuleRelationInfo;
import com.ca.mfd.prc.eps.entity.EpsModuleRelationEntity;
import com.ca.mfd.prc.eps.service.IEpsModuleRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @Description: 电池模组关系Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("epsmodulerelation")
@Tag(name = "电池模组关系服务", description = "电池模组关系")
public class EpsModuleRelationController extends BaseController<EpsModuleRelationEntity> {

    private IEpsModuleRelationService epsModuleRelationService;

    @Autowired
    public EpsModuleRelationController(IEpsModuleRelationService epsModuleRelationService) {
        this.crudService = epsModuleRelationService;
        this.epsModuleRelationService = epsModuleRelationService;
    }

    @Operation(summary = "获取模组关系结构")
    @GetMapping("getmodulerelation")
    public ResultVO<GetModuleRelationInfo> getModuleRelation(String barCode) {
        GetModuleRelationInfo data = epsModuleRelationService.getModuleRelation(barCode);
        return new ResultVO<GetModuleRelationInfo>().ok(data, "操作成功");
    }

    @Operation(summary = "绑定小单元")
    @PostMapping("bindingunit")
    public ResultVO<String> bindingUnit(@RequestBody BindingUnitPara para) {
        epsModuleRelationService.bindingUnit(para);
        epsModuleRelationService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "解除小单元关系绑定")
    @PostMapping("deleteunit")
    public ResultVO<String> deleteUnit(@RequestBody BarcodePara para) {
        epsModuleRelationService.deleteUnit(para.getBarcode());
        epsModuleRelationService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "绑定元素")
    @PostMapping("bindingcell")
    public ResultVO<String> bindingCell(@RequestBody EpsModuleRelationEntity para) {
        epsModuleRelationService.bindingCell(para);
        epsModuleRelationService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "删除关系元素")
    @PostMapping("deletecell")
    public ResultVO<String> deleteCell(@RequestBody BarcodePara para) {
        epsModuleRelationService.deleteCell(para.getBarcode());
        epsModuleRelationService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

}