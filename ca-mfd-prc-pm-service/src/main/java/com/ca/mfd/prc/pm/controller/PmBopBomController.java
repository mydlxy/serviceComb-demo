package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.basedto.ExportByDcModel;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.ShopExcelDto;
import com.ca.mfd.prc.pm.dto.WorkshopCodeMaterialRelaDTO;
import com.ca.mfd.prc.pm.entity.PmBopBomEntity;
import com.ca.mfd.prc.pm.entity.PmBopEntity;
import com.ca.mfd.prc.pm.service.IPmBopBomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 *
 * @Description: MBOM日志Controller
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
@RestController
@RequestMapping("pmbopbom")
@Tag(name = "MBOM", description = "MBOM")
public class PmBopBomController extends PmBaseController<PmBopBomEntity> {

    private IPmBopBomService pmBopBomService;

    @Autowired
    public PmBopBomController(IPmBopBomService pmBopBomService) {
        this.crudService = pmBopBomService;
        this.pmBopBomService = pmBopBomService;
    }

    @GetMapping(value = "getfeaturefrombom", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取bom特征列表")
    public ResultVO<List<String>> getFeatureFromBom(String workShopCode,String workStationCode){
        return  new ResultVO().ok(this.pmBopBomService.getFeatureFromBom(workShopCode,workStationCode));
    }

    @PostMapping(value = "copeproductmaterialmastertombom", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "复制物料主数据到bom")
    public  ResultVO<Object> copeProductMaterialMasterToMBom(@RequestBody WorkshopCodeMaterialRelaDTO workshopCodeMaterialRelaDTO) {
        if(StringUtils.isBlank(workshopCodeMaterialRelaDTO.getShopCode())){
            throw new InkelinkException("车间不能为空");
        }
        if(workshopCodeMaterialRelaDTO.getMaterialNos() == null || workshopCodeMaterialRelaDTO.getMaterialNos().isEmpty()){
            throw new InkelinkException("物料编码不能为空");
        }
        this.pmBopBomService.copyProductMaterialMasterToMBom(workshopCodeMaterialRelaDTO);
        return new ResultVO().ok("","保存成功");
    }

    @PostMapping(value = "exportbopbom", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "dc-导出")
    public void exportbydc(@RequestBody ExportByDcModel model, HttpServletResponse response,String shopCode) throws Exception {
        crudService.setExcelColumnNames(model.getField());
        List<ConditionDto> conditions = model.getConditions();
        if(StringUtils.isNotBlank(shopCode)){
            ConditionDto conditionDto = new ConditionDto("useWorkShop",shopCode, ConditionOper.Equal);
            conditions.add(conditionDto);
        }
        crudService.export(conditions, model.getSorts(), model.getFileName(), response);
    }


}