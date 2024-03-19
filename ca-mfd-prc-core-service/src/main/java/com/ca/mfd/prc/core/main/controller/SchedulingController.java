package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.main.service.ISysQueueNoteService;
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
@RequestMapping("main/scheduling")
@Tag(name = "PRC模块调度接口")
public class SchedulingController extends BaseApiController {
    @Autowired
    private ISysQueueNoteService sysQueueNoteService;

    /**
     * 根据参数删除历史数据
     *
     * @param minute 小时
     * @return
     */
    @GetMapping(value = "/deletehistorynotes")
    @Operation(summary = "删除历史数据")
    public ResultVO deleteHistoryNotes(Integer minute) {
        sysQueueNoteService.deleteHistoryNotes(minute);
        return new ResultVO<String>().ok("", "操作成功");
    }
}
