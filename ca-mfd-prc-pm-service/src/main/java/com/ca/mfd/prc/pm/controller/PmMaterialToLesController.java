package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmMaterialToLesEntity;
import com.ca.mfd.prc.pm.service.IPmMaterialToLesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: LES拉取工位物料清单Controller
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
@RestController
@RequestMapping("pmmaterialtoles")
@Tag(name = "LES拉取工位物料清单服务", description = "LES拉取工位物料清单")
public class PmMaterialToLesController extends BaseController<PmMaterialToLesEntity> {

    private IPmMaterialToLesService pmMaterialToLesService;

    @Autowired
    public PmMaterialToLesController(IPmMaterialToLesService pmMaterialToLesService) {
        this.crudService = pmMaterialToLesService;
        this.pmMaterialToLesService = pmMaterialToLesService;
    }

    @GetMapping(value = "creatematerial")
    @Operation(summary = "构建工位物料数据")
    public ResultVO<String> creatematerial(Long id) {
        //pmMaterialToLesService.createPmMaterialToLes();
        pmMaterialToLesService.getPmMaterialBak(id);
        pmMaterialToLesService.saveChange();
        return new ResultVO<String>().ok("生成成功");
    }
}