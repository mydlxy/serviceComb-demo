package com.ca.mfd.prc.avi.service;

import com.ca.mfd.prc.avi.entity.AviQueueReleaseSetEntity;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * 队列发布配置表
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
public interface IAviQueueReleaseSetService extends ICrudService<AviQueueReleaseSetEntity> {
    /**
     * 获取所有的数据
     *
     * @return 数据列表
     */
    List<AviQueueReleaseSetEntity> getAllDatas();

    /**
     * 查询重置列表数据
     *
     * @return 查询重置列表数据
     */
    List<ComboInfoDTO> getReSetQueueList();
}