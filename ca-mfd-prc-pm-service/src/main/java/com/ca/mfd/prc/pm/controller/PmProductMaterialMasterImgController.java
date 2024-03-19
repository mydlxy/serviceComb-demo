package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmProductMaterialMasterImgEntity;
import com.ca.mfd.prc.pm.service.IPmProductMaterialMasterImgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
 * @Description: 物料图片
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmproductmaterialmasterimg")
@Tag(name = "物料图片")
public class PmProductMaterialMasterImgController extends BaseController<PmProductMaterialMasterImgEntity> {

    private final IPmProductMaterialMasterImgService pmProductMaterialMasterImgService;

    @Autowired
    public PmProductMaterialMasterImgController(IPmProductMaterialMasterImgService pmProductMaterialMasterImgService) {
        this.crudService = pmProductMaterialMasterImgService;
        this.pmProductMaterialMasterImgService = pmProductMaterialMasterImgService;
    }


    /**
     * 获取类型为T的未逻辑删除的列表数据
     *
     * @param conditionInfos 条件表达式
     * @return List<T>
     */
    @PostMapping(value = "/provider/getdata")
    @Operation(summary = "获取类型为T的未逻辑删除的列表数据")
    public ResultVO getData(@RequestBody List<ConditionDto> conditionInfos) {
        return new ResultVO<>().ok(pmProductMaterialMasterImgService.getData(conditionInfos));
    }

    @Operation(summary = "检索物料列表")
    @Parameters({
            @Parameter(name = "searchKey", description = "搜索关键字")})
    @GetMapping(value = "/getmaterialinfos")
    public ResultVO getMaterialInfos(String searchKey) {
        return new ResultVO<>().ok(pmProductMaterialMasterImgService.getMaterialInfos(searchKey));
    }

}