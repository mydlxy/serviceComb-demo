package com.ca.mfd.prc.pm.remote.app.core;

import com.ca.mfd.prc.common.model.main.MessageContent;
import com.ca.mfd.prc.common.model.main.ReportQueue;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ca-mfd-prc-core-service", path = "main/sysqueuenote", contextId = "inkelink-core-sysqueuenote")
public interface ISysQueueNoteService {
    /**
     * 添加消息到队列
     *
     * @param content
     * @return
     */
    @PostMapping("/provider/addmessage")
    ResultVO addMessage(@RequestBody MessageContent content);

    /**
     * 添加消息到打印队列
     *
     * @param reportQueue
     * @return
     */
    @PostMapping("/provider/addreportqueue")
    ResultVO addReportQueue(@RequestBody ReportQueue reportQueue);

    @Component
    class SysQueueNoteServiceFallbackFactory implements FallbackFactory<ISysQueueNoteService> {
        private static final Logger logger = LoggerFactory.getLogger(ISysQueueNoteService.SysQueueNoteServiceFallbackFactory.class);

        @Override
        public ISysQueueNoteService create(Throwable cause) {
            logger.error("inkelink-core-sysqueuenote feign request error:" + cause.getMessage(), cause);
            return new ISysQueueNoteService() {
                /**
                 * 添加消息到队列
                 *
                 * @param content
                 */
                @Override
                public ResultVO addMessage(MessageContent content) {
                    return new ResultVO().error("调用失败：" + cause.getMessage());
                }

                /**
                 * 添加消息到打印队列
                 *
                 * @param reportQueue
                 */
                @Override
                public ResultVO addReportQueue(ReportQueue reportQueue) {
                    return new ResultVO().error("调用失败：" + cause.getMessage());
                }
            };
        }
    }
}
