package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.avi.entity.AviCutLogEntity;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordEntity;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsOrderProvider;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.avi.service.IAviCutLogService;
import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * AVICUT记录表
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@RestController
@RequestMapping("avicutlog")
@Tag(name = "AVICUT记录表")
public class AviCutLogController extends BaseController<AviCutLogEntity> {

    @Autowired
    PpsOrderProvider ppsOrderProvider;

    private final IAviCutLogService aviCutLogService;

    @Autowired
    public AviCutLogController(IAviCutLogService aviCutLogService) {
        this.crudService = aviCutLogService;
        this.aviCutLogService = aviCutLogService;
    }

    @PostMapping("getpagedata")
    @Operation(summary = "获取分页数据")
    @Override
    public ResultVO<PageData<AviCutLogEntity>> getPageData(@RequestBody PageDataDto model) {
        PageData<AviCutLogEntity> pageData = crudService.page(model);
        pageData.getDatas().stream().forEach(s -> {
            s.setAttribute1(s.getAviCode());
        });
        PageData<AviCutLogEntity> page = pageData;
        return new ResultVO<PageData<AviCutLogEntity>>().ok(page, "获取数据成功");
    }

    @PostMapping("savelog")
    @Operation(summary = "更新")
    @LogOperation("更新")
    //@Override
    public ResultVO savelog(@RequestBody List<AviCutLogEntity> data) {
        ResultVO<AviTrackingRecordEntity> result = new ResultVO<>();
        result.setMessage("保存成功");
        if (data == null || data.isEmpty()) {
            throw new InkelinkException("数据不能为空.");
        }

        for (AviCutLogEntity item : data) {
            PpsOrderEntity ppsOrder = ppsOrderProvider.getPpsOrderBySnOrBarcode(item.getSn().trim());
            if (ppsOrder == null || StringUtils.isBlank(ppsOrder.getSn())) {
                continue;
            }
            if (StringUtils.isBlank(item.getSn())) {
                continue;
            }
            if (item.getId() <= 0) {
                crudService.save(item);
            } else {
                crudService.update(item);
            }
        }
        crudService.saveChange();
        return result.ok();
    }
}