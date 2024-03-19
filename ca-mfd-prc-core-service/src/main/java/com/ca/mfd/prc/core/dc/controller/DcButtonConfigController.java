package com.ca.mfd.prc.core.dc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.prm.dto.ButtonBatchSetPara;
import com.ca.mfd.prc.core.prm.entity.DcButtonConfigEntity;
import com.ca.mfd.prc.core.dc.service.IDcButtonConfigService;
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
@RequestMapping("dc/dcbuttonconfig")
@Tag(name = "按钮配置")
public class DcButtonConfigController extends BaseController<DcButtonConfigEntity> {
    private final IDcButtonConfigService dcButtonConfigService;

    @Autowired
    public DcButtonConfigController(IDcButtonConfigService dcButtonConfigService) {
        this.crudService = dcButtonConfigService;
        this.dcButtonConfigService = dcButtonConfigService;
    }

    @PostMapping(value = "/batchset")
    @Operation(summary = "批量设置按钮配置")
    public ResultVO batchset(@RequestBody ButtonBatchSetPara para) {
        dcButtonConfigService.batchSet(para);
        return new ResultVO<String>().ok("", "保存成功");
    }

    @GetMapping(value = "/getpagebuttonlist")
    @Operation(summary = "获取页面按钮列表")
    public ResultVO getPageButtonList(String pageId) {
        ResultVO<List<DcButtonConfigEntity>> result = new ResultVO<>();
        List<DcButtonConfigEntity> data = dcButtonConfigService.getPageButtonList(ConvertUtils.stringToLong(pageId));
        return result.ok(data, "获取数据成功");
    }
}
