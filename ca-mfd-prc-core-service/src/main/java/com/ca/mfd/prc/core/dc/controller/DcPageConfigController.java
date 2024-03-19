package com.ca.mfd.prc.core.dc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.dc.dto.InitPageDataVO;
import com.ca.mfd.prc.core.prm.entity.DcPageConfigEntity;
import com.ca.mfd.prc.core.dc.service.IDcPageConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 */
@RestController
@RequestMapping("dc/dcpageconfig")
@Tag(name = "页面配置")
public class DcPageConfigController extends BaseController<DcPageConfigEntity> {

    private final IDcPageConfigService dcPageConfigService;

    @Autowired
    public DcPageConfigController(IDcPageConfigService dcPageConfigService) {
        this.crudService = dcPageConfigService;
        this.dcPageConfigService = dcPageConfigService;
    }

    @GetMapping(value = "initpagedata")
    @Operation(summary = "初始化页面元素")
    public ResultVO<InitPageDataVO> initpagedata(String pageKey) {
        InitPageDataVO data = dcPageConfigService.initPageData(pageKey);
        return new ResultVO<InitPageDataVO>().ok(data);
    }

    @GetMapping("copy")
    @Operation(summary = "复制页面")
    public ResultVO<String> copy(String pageKey, String pageName, String sourceId) {
        dcPageConfigService.copy(pageKey, pageName, ConvertUtils.stringToLong(sourceId));
        dcPageConfigService.saveChange();
        return new ResultVO<String>().ok("");
    }
}
