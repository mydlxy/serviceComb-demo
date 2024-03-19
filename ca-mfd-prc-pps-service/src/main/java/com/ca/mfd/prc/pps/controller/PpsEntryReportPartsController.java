package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.basedto.TemplateModel;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.PpsEntryReportPartsDto;
import com.ca.mfd.prc.pps.entity.PpsEntryReportPartsEntity;
import com.ca.mfd.prc.pps.service.IPpsEntryReportPartsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author inkelink
 * @Description: 报工单-零部件Controller
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("ppsentryreportparts")
@Tag(name = "报工单-零部件服务", description = "报工单-零部件")
public class PpsEntryReportPartsController extends BaseController<PpsEntryReportPartsEntity> {

    private IPpsEntryReportPartsService ppsEntryReportPartsService;

    @Autowired
    public PpsEntryReportPartsController(IPpsEntryReportPartsService ppsEntryReportPartsService) {
        this.crudService = ppsEntryReportPartsService;
        this.ppsEntryReportPartsService = ppsEntryReportPartsService;
    }

    @Operation(summary = "获取")
    @GetMapping("/provider/getfirstbybarcode")
    public ResultVO<PpsEntryReportPartsEntity> getFirstByBarcode(String barcode) {
        PpsEntryReportPartsEntity data = ppsEntryReportPartsService.getFirstByBarcode(barcode);
        return new ResultVO<PpsEntryReportPartsEntity>().ok(data);
    }

    @Operation(summary = "根据分类获取前20条")
    @GetMapping("/provider/gettopdatabyordercategory")
    public ResultVO<List<PpsEntryReportPartsEntity>> getTopDataByOrderCategory(Integer orderCategory) {
        List<PpsEntryReportPartsEntity> data = ppsEntryReportPartsService.getTopDataByOrderCategory(orderCategory);
        return new ResultVO<List<PpsEntryReportPartsEntity>>().ok(data);
    }


    @Operation(summary = "根据分类获取")
    @PostMapping("/provider/getrecordbyordercategory")
    public ResultVO<List<PpsEntryReportPartsEntity>> getRecordByOrderCategory(@RequestBody PpsEntryReportPartsDto datas) {
        List<PpsEntryReportPartsEntity> data = ppsEntryReportPartsService.getRecordByOrderCategory(datas.getOrderCategory(), datas.getRprtNos());
        return new ResultVO<List<PpsEntryReportPartsEntity>>().ok(data);
    }

    @Operation(summary = "更新状态")
    @GetMapping("/provider/updateispassavibyid")
    public ResultVO<String> updateIsPassAviById(Long id) {
        ppsEntryReportPartsService.updateIsPassAviById(id);
        ppsEntryReportPartsService.saveChange();
        return new ResultVO<String>().ok("更新成功");
    }

    @Operation(summary = "获取导入模板")
    @PostMapping("downloadplantemplate")
    public void downloadTemplate(@RequestBody TemplateModel template, HttpServletResponse response) throws Exception {
        ppsEntryReportPartsService.getImportTemplate(template.getFileName(), response);
    }

}