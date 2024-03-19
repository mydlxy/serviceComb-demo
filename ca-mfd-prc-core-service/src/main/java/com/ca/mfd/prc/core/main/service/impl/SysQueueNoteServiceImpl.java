package com.ca.mfd.prc.core.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.model.main.MessageContent;
import com.ca.mfd.prc.common.model.main.ReportQueue;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.core.main.entity.SysQueueNoteEntity;
import com.ca.mfd.prc.core.main.mapper.ISysQueueNoteMapper;
import com.ca.mfd.prc.core.main.service.ISysQueueNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 队列笔记
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class SysQueueNoteServiceImpl extends AbstractCrudServiceImpl<ISysQueueNoteMapper, SysQueueNoteEntity> implements ISysQueueNoteService {

    @Autowired
    IdentityHelper identityHelper;

    public <T> void addQueue(String groupName, T content) {
        SysQueueNoteEntity entity = new SysQueueNoteEntity();
        entity.setGroupName(groupName);
        entity.setContent(JsonUtils.toJsonString(content));
        Map<String, Object> maps = new HashMap<>(5);
        maps.put("UserName", identityHelper.getLoginUser().getUserName());
        maps.put("UserId", identityHelper.getLoginUser().getId());
        maps.put("NickName", identityHelper.getLoginUser().getNickName());
        entity.setOnlineUser(JsonUtils.toJsonString(maps));
        save(entity);
    }

    /**
     * 删除历史的队列消息
     *
     * @param minute
     */
    @Override
    public void deleteHistoryNotes(Integer minute) {
        Date time = ((minute == null || minute == 0) ? DateUtils.addDateHours(new Date(), -3) : DateUtils.addDateMinutes(new Date(), -minute));
        QueryWrapper<SysQueueNoteEntity> qry = new QueryWrapper<>();
        qry.lambda().le(SysQueueNoteEntity::getCreationDate, time).eq(SysQueueNoteEntity::getIsDelete, true).select(SysQueueNoteEntity::getId);
        List<Serializable> ids = selectList(qry, true).stream().map(SysQueueNoteEntity::getId).collect(Collectors.toList());
        delete(ids.toArray(new Serializable[ids.size()]), false);
    }

    /**
     * @param reportQueue 参数集合
     */
    @Override
    public void addReportQueue(ReportQueue reportQueue) {
        addQueue("Report_AddQueue", reportQueue);
    }

    /**
     * 添加消息到队列
     *
     * @param content
     */
    @Override
    public void addMessage(MessageContent content) {
        addQueue("MSG_AddMessage", content);
    }

}