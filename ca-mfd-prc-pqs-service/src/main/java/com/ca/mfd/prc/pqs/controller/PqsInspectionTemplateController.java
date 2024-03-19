package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.basedto.ExportByDcModel;
import com.ca.mfd.prc.common.model.basedto.TemplateModel;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.TemplateCopyDto;
import com.ca.mfd.prc.pqs.entity.PqsInspectionTemplateEntity;
import com.ca.mfd.prc.pqs.entity.PqsInspectionTemplateItemEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectionTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author inkelink
 * @Description: 检验模板管理Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsinspectiontemplate")
@Tag(name = "检验模板管理服务", description = "检验模板管理")
public class PqsInspectionTemplateController extends BaseController<PqsInspectionTemplateEntity> {

    @Autowired
    private IPqsInspectionTemplateService pqsInspectionTemplateService;

    @Autowired
    public PqsInspectionTemplateController(IPqsInspectionTemplateService pqsInspectionTemplateService) {
        this.crudService = pqsInspectionTemplateService;
        this.pqsInspectionTemplateService = pqsInspectionTemplateService;
    }

    @PostMapping("savetemplateitem")
    @Operation(summary = "保存模板检查项")
    public ResultVO<String> saveTemplateItem(@RequestBody List<PqsInspectionTemplateItemEntity> pqsInspectTemplateItemDtos) {
        if (CollectionUtils.isEmpty(pqsInspectTemplateItemDtos)) {
            throw new InkelinkException("要保存的数据不能为空");
        }
        pqsInspectionTemplateService.saveTemplateItem(pqsInspectTemplateItemDtos);
        pqsInspectionTemplateService.saveChange();
        return new ResultVO<String>().ok("", "保存成功");
    }

    @PostMapping("copytemplate")
    @Operation(summary = "复制模板")
    public ResultVO<String> copyTemplateItem(@RequestBody TemplateCopyDto dto) {
        if (Objects.isNull(dto)) {
            throw new InkelinkException("要复制的数据不能为空");
        }
        pqsInspectionTemplateService.copyTemplate(dto);
        return new ResultVO<String>().ok("", "保存成功");
    }

    @GetMapping("gettempaltedetail")
    @Operation(summary = "获取明细")
    public ResultVO<List<PqsInspectionTemplateItemEntity>> getTempalteDetail(String tempalteId) {
        if (StringUtils.isEmpty(tempalteId)) {
            throw new InkelinkException("模板主键不能为空");
        }
        return new ResultVO<List<PqsInspectionTemplateItemEntity>>()
                .ok(pqsInspectionTemplateService.getTempalteDetail(tempalteId), "获取成功");
    }

    @PostMapping("downloadinporttemplate")
    @Operation(summary = "下载模板")
    public void downloadInmportTemplate(@RequestBody TemplateModel model, HttpServletResponse response) throws IOException {
        pqsInspectionTemplateService.getImportTemplate(model.getFileName(), response);
    }

    @PostMapping("downloadinporttemplatema")
    @Operation(summary = "下载模板")
    public void downloadInmportTemplateMa(@RequestBody TemplateModel model, HttpServletResponse response) throws IOException {
        pqsInspectionTemplateService.getImportTemplateMa(model.getFileName(), response);
    }

    @PostMapping(value = "importma", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "导入")
    public ResultVO importExcelMa(MultipartHttpServletRequest req) throws Exception {
        MultipartFile file = req.getFile(req.getFileNames().next());
        crudService.setExcelColumnNames(new HashMap(0));
        pqsInspectionTemplateService.importExcelMa(file.getInputStream());
        crudService.saveChange();
        return new ResultVO<String>().ok("", "导入数据成功");
    }

    /**
     * 导出
     *
     * @param model
     * @param response
     * @throws Exception
     */
    @PostMapping(value = "exportPost", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "导出")
    public void exportPost(@RequestBody ExportByDcModel model, HttpServletResponse response) throws Exception {
        crudService.setExcelColumnNames(model.getField());
        crudService.export(model.getConditions(), model.getSorts(), model.getFileName(), response);
    }

    /**
     * 导出
     *
     * @param model
     * @param response
     * @throws Exception
     */
    @PostMapping(value = "exportPostma", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "导出")
    public void exportPostMa(@RequestBody ExportByDcModel model, HttpServletResponse response) throws Exception {
        crudService.setExcelColumnNames(model.getField());
        pqsInspectionTemplateService.export(model.getConditions(), model.getSorts(), model.getFileName(), response);
    }
}