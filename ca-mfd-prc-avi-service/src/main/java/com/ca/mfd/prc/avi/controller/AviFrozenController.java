package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.avi.entity.AviFrozenEntity;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmAviServiceProvider;
import com.ca.mfd.prc.avi.service.IAviFrozenService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pm.dto.AviInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 冻结产品
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@RestController
@RequestMapping("avifrozen")
@Tag(name = "冻结产品")
public class AviFrozenController extends BaseController<AviFrozenEntity> {

    private final IAviFrozenService aviFrozenService;

    @Autowired
    private PmAviServiceProvider pmAviServiceProvider;


    @Autowired
    public AviFrozenController(IAviFrozenService aviFrozenService) {
        this.crudService = aviFrozenService;
        this.aviFrozenService = aviFrozenService;
    }

    /**
     * 冻结确认
     *
     * @param guids 冻结ids
     * @return 操作结果
     */
    @Operation(summary = "冻结确认")
    @PostMapping("confirm")
    public ResultVO<String> confirm(@RequestBody List<String> guids) {
        if (guids == null || guids.size() == 0) {
            return new ResultVO<String>().ok("冻结确认成功");
        }
        for (String data : guids) {
            aviFrozenService.confirm(data);
        }
        aviFrozenService.saveChange();
        return new ResultVO<String>().ok("冻结确认成功");
    }

    /**
     * 获取AVI站点
     *
     * @return AVI站点列表
     */
    @Operation(summary = "获取AVI站点")
    @PostMapping("getaviinfos")
    public ResultVO<String> getAviInfos() {
        List<AviInfoDTO> avisData = pmAviServiceProvider.getAviInfos();
        return new ResultVO<String>().ok("获取AVI站点成功");
    }

    @Operation(summary = "取消冻结")
    @PostMapping("unfrozen")
    public ResultVO<String> unFrozen(@RequestBody List<String> guids) {
        if (guids == null || guids.size() == 0) {
            return new ResultVO<String>().ok("取消冻结成功");
        }
        for (String data : guids) {
            aviFrozenService.unFrozen(data);
        }
        aviFrozenService.saveChange();
        return new ResultVO<String>().ok("取消冻结成功");
    }

    @Operation(summary = "冻结产品分页")
    @PostMapping("getpagedata")
    @Override
    public ResultVO<PageData<AviFrozenEntity>> getPageData(@RequestBody PageDataDto model) {
        return super.getPageData(model);
    }
}