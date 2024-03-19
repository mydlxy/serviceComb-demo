package com.ca.mfd.prc.pmc.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pmc.dto.TeamleaderFillInfoDTO;
import com.ca.mfd.prc.pmc.entity.PmcAlarmEquipmntDetailEntity;

import java.util.Date;
import java.util.List;

/**
 * 单个设备报警数据沉淀
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
public interface IPmcAlarmEquipmntDetailService extends ICrudService<PmcAlarmEquipmntDetailEntity> {

    /**
     * 获取一个工段内并且一段时间内所有的报警信息
     *
     * @param shopCode
     * @param startTime
     * @param endTime
     * @param postion
     * @return
     */
    List<PmcAlarmEquipmntDetailEntity> getAlarmDetailList(String shopCode, String startTime, String endTime, String postion);

    /**
     * 获取班组长需要处理的问题
     *
     * @param userId
     * @return
     */
    List<PmcAlarmEquipmntDetailEntity> getTeamleaderData(String userId);

    /**
     * 班组长处理报警问题
     *
     * @param para
     */
    void teamleaderFill(TeamleaderFillInfoDTO para);

    /**
     * 判断是否可以关闭停线原因
     *
     * @param shopCode
     * @param postion
     * @param startTime
     * @param endTime
     * @return
     */
    int getNotFinishCount(String shopCode, String postion, Date startTime, Date endTime);
}