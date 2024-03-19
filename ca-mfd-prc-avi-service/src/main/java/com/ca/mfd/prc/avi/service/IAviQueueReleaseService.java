package com.ca.mfd.prc.avi.service;

import com.ca.mfd.prc.avi.communication.dto.MidLmsAviQueueDto;
import com.ca.mfd.prc.avi.dto.OrderSequenceDTO;
import com.ca.mfd.prc.avi.dto.ResetQueueParaDTO;
import com.ca.mfd.prc.avi.entity.AviQueueReleaseEntity;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * 队列发布数据表
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
public interface IAviQueueReleaseService extends ICrudService<AviQueueReleaseEntity> {
    /**
     * 修改队列数据状态
     *
     * @param isSend
     * @param id
     * @return
     */
    void updateIsSendStatus(Boolean isSend, Long id);

    /**
     * 查找指定队列的未处理数据
     *
     * @param queueName
     * @param top
     * @return
     */
    List<AviQueueReleaseEntity> getNoSendByQuee(String queueName,Integer top);

    /**
     * 查找指定队列的未处理数据
     *
     * @param queueName
     * @return
     */
    List<AviQueueReleaseEntity> getNoSendByQuee(String queueName);

    /**
     * 整车数据(原代码未实现)
     *
     * @param conditions
     * @param sorts
     * @param pageIndex
     * @param pageSize
     * @return PageData<OrderSequenceDTO>
     */
    PageData<OrderSequenceDTO> getPageVehicleDatas(List<ConditionDto> conditions, List<SortDto> sorts, int pageIndex, int pageSize);

    /**
     * 重置队列
     *
     * @param para
     */
    void resetQueue(ResetQueueParaDTO para);

    /**
     * 下发lms车辆队列
     *
     * @return 车辆过点信息
     */
    List<MidLmsAviQueueDto> getLmsAviQueueList();

    /**
     * 更新队列发送标识
     *
     * @param ids 主键
     */
    void updateLmsAviQueueStatus(List<Long> ids);

    List<MidLmsAviQueueDto> getLmsAviQueueListBak();
}