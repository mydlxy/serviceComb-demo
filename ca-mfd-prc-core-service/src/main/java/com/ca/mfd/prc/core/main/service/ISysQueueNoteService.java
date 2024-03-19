package com.ca.mfd.prc.core.main.service;

import com.ca.mfd.prc.common.model.main.MessageContent;
import com.ca.mfd.prc.common.model.main.ReportQueue;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.main.entity.SysQueueNoteEntity;

/**
 * 队列笔记
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface ISysQueueNoteService extends ICrudService<SysQueueNoteEntity> {
    /**
     * 添加消息到打印队列
     *
     * @param reportQueue 参数集合
     */
    void addReportQueue(ReportQueue reportQueue);

    /**
     * 添加消息到队列
     *
     * @param content
     */
    void addMessage(MessageContent content);

    /**
     * 删除历史的队列消息
     *
     * @param minute
     */
    void deleteHistoryNotes(Integer minute);

}