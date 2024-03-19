package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.avi.entity.AviCutLogEntity;
import com.ca.mfd.prc.avi.entity.AviQueueReleaseSetEntity;
import com.ca.mfd.prc.avi.service.IAviQueueReleaseSetService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
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
 * 队列发布配置表
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@RestController
@RequestMapping("aviqueuereleaseset")
@Tag(name = "队列发布配置表")
public class AviQueueReleaseSetController extends BaseController<AviQueueReleaseSetEntity> {

    private final IAviQueueReleaseSetService aviQueueReleaseSetService;

    @Autowired
    public AviQueueReleaseSetController(IAviQueueReleaseSetService aviQueueReleaseSetService) {
        this.crudService = aviQueueReleaseSetService;
        this.aviQueueReleaseSetService = aviQueueReleaseSetService;
    }

    @Operation(summary = "查询重置列表数据")
    @GetMapping(value = "/getresetqueuelist")
    public ResultVO<List<ComboInfoDTO>> getReSetQueueList() {
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        List<ComboInfoDTO> data = aviQueueReleaseSetService.getReSetQueueList();
        return result.ok(data);
    }

    @PostMapping("getpagedata")
    @Operation(summary = "获取分页数据")
    @Override
    public ResultVO<PageData<AviQueueReleaseSetEntity>> getPageData(@RequestBody PageDataDto model) {
        PageData<AviQueueReleaseSetEntity> pageData = crudService.page(model);
        pageData.getDatas().stream().forEach(s -> {
            s.setAttribute1(s.getAviCode());
        });
        PageData<AviQueueReleaseSetEntity> page = pageData;
        return new ResultVO<PageData<AviQueueReleaseSetEntity>>().ok(page, "获取数据成功");
    }


}