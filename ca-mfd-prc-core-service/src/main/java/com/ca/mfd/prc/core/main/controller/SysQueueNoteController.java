package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.model.main.MessageContent;
import com.ca.mfd.prc.common.model.main.ReportQueue;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.validator.AssertUtils;
import com.ca.mfd.prc.core.main.entity.SysQueueNoteEntity;
import com.ca.mfd.prc.core.main.service.ISysQueueNoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 队列笔记
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/sysqueuenote")
@Tag(name = "队列笔记")
public class SysQueueNoteController extends BaseController<SysQueueNoteEntity> {

    private final ISysQueueNoteService sysQueueNoteService;

    @Autowired
    public SysQueueNoteController(ISysQueueNoteService sysQueueNoteService) {
        this.crudService = sysQueueNoteService;
        this.sysQueueNoteService = sysQueueNoteService;
    }

    /**
     * 添加消息到队列
     *
     * @param content
     */
    @PostMapping("/provider/addmessage")
    @Operation(summary = "添加消息到队列")
    ResultVO pdAddMessage(@RequestBody MessageContent content) {
        sysQueueNoteService.addMessage(content);
        sysQueueNoteService.saveChange();
        return new ResultVO().ok("");
    }

    /**
     * 添加消息到打印队列
     *
     * @param reportQueue
     */
    @PostMapping("/provider/addreportqueue")
    @Operation(summary = "添加消息到打印队列")
    ResultVO addReportQueue(@RequestBody ReportQueue reportQueue) {
        sysQueueNoteService.addReportQueue(reportQueue);
        sysQueueNoteService.saveChange();
        return new ResultVO().ok("");
    }

    @Operation(summary = "添加message信息")
    @PostMapping("addmessage")
    public ResultVO addMessage(MessageContent content) {
        sysQueueNoteService.addMessage(content);
        return new ResultVO().ok("", "操作成功");
    }

    @PostMapping(value = "/provider/getdata", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "根据参数获取所有数据")
    public ResultVO<List<SysQueueNoteEntity>> providerGetdata(@RequestBody DataDto model) {
        List<SysQueueNoteEntity> list = crudService.list(model);
        return new ResultVO<List<SysQueueNoteEntity>>().ok(list, "获取数据成功");
    }

    @PostMapping(value = "/provider/del", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "根据参数删除队列数据")
    public ResultVO providerDel(@RequestBody IdsModel model) {
        //效验数据
        AssertUtils.isArrayEmpty(model.getIds(), "id");
        crudService.delete(model.getIds());
        crudService.saveChange();
        return new ResultVO<String>().ok("", "删除成功");
    }

    @PostMapping(value = "/provider/addsimplemessage", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "发送简单消息")
    public ResultVO addSimpleMessage(@RequestBody SysQueueNoteEntity model) {
        this.edit(model);
        return new ResultVO<String>().ok("", "保存成功");
    }

}