package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.entity.PpsBindingTagLogEntity;
import com.ca.mfd.prc.pps.service.IPpsBindingTagLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eric.zhou
 * @Description: 吊牌绑定日志Controller
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@RestController
@RequestMapping("ppsbindingtaglog")
@Tag(name = "吊牌绑定日志服务", description = "吊牌绑定日志")
public class PpsBindingTagLogController extends BaseController<PpsBindingTagLogEntity> {

    private final IPpsBindingTagLogService prcPpsBindingTagLogService;

    @Autowired
    public PpsBindingTagLogController(IPpsBindingTagLogService prcPpsBindingTagLogService) {
        this.crudService = prcPpsBindingTagLogService;
        this.prcPpsBindingTagLogService = prcPpsBindingTagLogService;
    }

    @PostMapping(value = "getpagedata", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取分页数据")
    @Override
    public ResultVO<PageData<PpsBindingTagLogEntity>> getPageData(@RequestBody PageDataDto model) {
        if (model.getPageSize() == null) {
            model.setPageSize(20);
        }
        if (model.getPageIndex() == null) {
            model.setPageIndex(1);
        }
        PageData<PpsBindingTagLogEntity> page = crudService.page(model);
        return new ResultVO<PageData<PpsBindingTagLogEntity>>().ok(page, "获取数据成功");
    }

}