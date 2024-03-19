package com.ca.mfd.prc.core.dc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.dc.dto.FieldBatchPara;
import com.ca.mfd.prc.core.prm.entity.DcFieldConfigEntity;
import com.ca.mfd.prc.core.dc.service.IDcFieldConfigService;
import io.swagger.v3.oas.annotations.Operation;
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
 */
@RestController
@RequestMapping("dc/dcfieldconfig")
@Tag(name = "字段配置")
public class DcFieldConfigController extends BaseController<DcFieldConfigEntity> {
    private final IDcFieldConfigService dcFieldConfigService;

    @Autowired
    public DcFieldConfigController(IDcFieldConfigService dcFieldConfigService) {
        this.crudService = dcFieldConfigService;
        this.dcFieldConfigService = dcFieldConfigService;
    }

    @PostMapping(value = "/batchset")
    @Operation(summary = "批量设置字段配置")
    public ResultVO batchset(@RequestBody FieldBatchPara para) {
        dcFieldConfigService.batchSet(para);
        return new ResultVO().ok("", "保存成功");
    }

    @GetMapping(value = "/getpagefieldlist")
    @Operation(summary = "获取页面字段列表")
    public ResultVO getPageFieldList(String pageId) {
        ResultVO<List<DcFieldConfigEntity>> result = new ResultVO<>();
        List<DcFieldConfigEntity> data = dcFieldConfigService.getPageFieldList(ConvertUtils.stringToLong(pageId));
        return result.ok(data, "获取数据成功");
    }
}
