package com.ca.mfd.prc.pqs.remote.app.core;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.core.entity.SysQueueNoteEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author qu
 */
@FeignClient(
        name = "ca-mfd-prc-core-service",
        path = "main/sysqueuenote",
        contextId = "inkelink-core-sysqueuenote")
public interface ISysQueueNoteService {

    /**
     * 添加简单消息到队列
     *
     * @param content
     * @return
     */
    @PostMapping("/provider/addSimpleMessage")
    ResultVO addSimpleMessage(@RequestBody SysQueueNoteEntity content);
}
