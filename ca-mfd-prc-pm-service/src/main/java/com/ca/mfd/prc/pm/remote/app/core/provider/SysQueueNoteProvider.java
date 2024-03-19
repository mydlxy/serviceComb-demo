package com.ca.mfd.prc.pm.remote.app.core.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.main.MessageContent;
import com.ca.mfd.prc.common.model.main.ReportQueue;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.remote.app.core.ISysQueueNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysQueueNoteProvider {
    @Autowired
    private ISysQueueNoteService sysQueueNoteService;

    public void addMessage(MessageContent content) {
        ResultVO result = sysQueueNoteService.addMessage(content);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-communicationpush调用失败" + result.getMessage());
        }
    }

    public void addReportQueue(ReportQueue reportQueue) {
        ResultVO result = sysQueueNoteService.addReportQueue(reportQueue);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-communicationpush调用失败" + result.getMessage());
        }
    }
}
