package com.ca.mfd.prc.common.controller;

import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.model.base.dto.ConditionsDto;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.basedto.ExportByDcModel;
import com.ca.mfd.prc.common.model.basedto.ExportModel;
import com.ca.mfd.prc.common.model.basedto.TemplateModel;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.validator.AssertUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <T> 继承了BaseEntity的数据传输对象
 * @author inkelink
 * @Description: 基础Controller类，提供数据库表操作的接口
 * @date 2023年4月4日
 * @变更说明 BY eric.zhou At 2023年4月4日
 */
public class BaseController<T extends BaseEntity> {

    protected ICrudService crudService;

    @PostMapping(value = "getcount", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取数据条数")
    public ResultVO<Integer> getCount(@RequestBody ConditionsDto model) {
        Integer counts = crudService.count(model);
        return new ResultVO<Integer>().ok(counts, "获取数据成功");
    }

    @PostMapping(value = "getpagedata", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取分页数据")
    public ResultVO<PageData<T>> getPageData(@RequestBody PageDataDto model) {
        PageData<T> page = crudService.page(model);
        return new ResultVO<PageData<T>>().ok(page, "获取数据成功");
    }

    @PostMapping(value = "getdata", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取所有数据")
    public ResultVO<List<T>> page(@RequestBody DataDto model) {
        List<T> list = crudService.list(model);

        return new ResultVO<List<T>>().ok(list, "获取数据成功");
    }

    @GetMapping(value = "getbyid", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "信息")
    public ResultVO<T> getById(@RequestParam(value = "id") String id) {
        T data = (T) crudService.get(ConvertUtils.stringToLong(id));
        return new ResultVO<T>().ok(data);
    }

    @PostMapping(value = "edit", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "更新")
    public ResultVO<?> edit(@RequestBody @Valid T dto) {
        Long id = crudService.currentModelGetKey(dto);
        if (id == null || id <= 0) {
            crudService.save(dto);
        } else {
            crudService.update(dto);
        }
        crudService.saveChange();
        return new ResultVO<>().ok(dto, "保存成功");
    }

    @PostMapping(value = "del", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "删除")
    public ResultVO<?> delete(@RequestBody IdsModel model) {
        //效验数据
        AssertUtils.isArrayEmpty(model.getIds(), "id");
        crudService.delete(ConvertUtils.stringToLongs(Arrays.asList(model.getIds())).toArray(new Long[0]));
        crudService.saveChange();
        return new ResultVO<String>().ok("", "删除成功");
    }

    @PostMapping(value = "delbycondtions", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "根据条件删除数据")
    public ResultVO<?> delByCondtions(@RequestBody ConditionsDto model) {
        //效验数据
        AssertUtils.isListEmpty(model.getConditions());

        crudService.delete(model.getConditions());
        crudService.saveChange();
        return new ResultVO<String>().ok("", "删除成功");
    }

    @GetMapping(value = "export", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "导出")
    public void export(ExportModel model, HttpServletResponse response) throws Exception {
        crudService.setExcelColumnNames(new HashMap(0));
        crudService.export(model.getConditions(), model.getSorts(), model.getFileName(), response);
    }

    @PostMapping(value = "exportbydc", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "dc-导出")
    public void exportbydc(@RequestBody ExportByDcModel model, HttpServletResponse response) throws Exception {
        crudService.setExcelColumnNames(model.getField());
        crudService.export(model.getConditions(), model.getSorts(), model.getFileName(), response);
    }

    @PostMapping(value = "exportbydcsimpledate", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "dc-导出（中文时间导出）")
    public void exportbydcSimpleDate(@RequestBody ExportByDcModel model, HttpServletResponse response) throws Exception {
        crudService.setExcelColumnNames(model.getField());
        crudService.export(model.getConditions(), model.getSorts(), model.getFileName(), true, response);
    }

    @PostMapping(value = "import", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "导入")
    public ResultVO importExcel(MultipartHttpServletRequest req) throws Exception {
        MultipartFile file = req.getFile(req.getFileNames().next());
        crudService.setExcelColumnNames(new HashMap(0));
        crudService.importExcel(file.getInputStream());
        crudService.saveChange();
        return new ResultVO<String>().ok("", "导入数据成功");
    }

    @PostMapping(value = "importbydc", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "导入-dc")
    @Parameters({@Parameter(name = "file", description = "文件")})
    public ResultVO importByDc(@RequestParam MultipartFile file, @RequestParam Map<String, String> field) throws Exception {
        crudService.setExcelColumnNames(field);
        crudService.importExcel(file.getInputStream());
        crudService.saveChange();
        return new ResultVO<String>().ok("", "导入数据成功");
    }

    @GetMapping(value = "downloadtemplate", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取导入模板")
    public void downloadtemplate(String fileName, HttpServletResponse response) throws Exception {
        crudService.setExcelColumnNames(new HashMap<>(0));
        crudService.getImportTemplate(fileName, response);
    }

    @PostMapping(value = "downloadbydctemplate", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取导入模板-dc")
    public void downloadTemplateByDc(@RequestBody TemplateModel template, HttpServletResponse response) throws Exception {
        crudService.setExcelColumnNames(template.getField());
        crudService.getImportTemplate(template.getFileName(), response);
    }
}
