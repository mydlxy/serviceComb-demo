package com.ca.mfd.prc.pqs.remote.app.core.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.core.ISysQueueNoteService;
import com.ca.mfd.prc.pqs.remote.app.core.entity.SysQueueNoteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author edwards.qu
 */
@Service
public class SysQueueNoteProvider {

    @Autowired
    private ISysQueueNoteService sysQueueNoteService;

    /**
     * 添加简单消息到队列
     *
     * @param content
     * @return
     */
    public ResultVO addSimpleMessage(SysQueueNoteEntity content) {
        ResultVO result = sysQueueNoteService.addSimpleMessage(content);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-rabbitmq-sysqueuenote调用失败" + result.getMessage());
        }
        return result;
    }
}