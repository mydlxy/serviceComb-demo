package com.ca.mfd.prc.core.report.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.report.dto.ReportPrintQueueDycVO;
import com.ca.mfd.prc.core.report.entity.RptPrintQueueEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 报表打印队列服务
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
public interface IRptPrintQueueService extends ICrudService<RptPrintQueueEntity> {
    /**
     * 重新打印
     *
     * @param status 状态
     * @param ids    主键集合
     * @return 列表
     */
    List<RptPrintQueueEntity> getDataListByIds(int status, List<Long> ids);

    /**
     * 根据主键集合更新
     *
     * @param ids 主键集合
     */
    void updateDatasByIds(List<Long> ids);


    /**
     * 打印队列数量查询
     *
     * @return 打印队列数量
     */
    Long getQueueQty();

    /**
     * 基于打印机返回打印队列
     *
     * @param ips ips
     * @return 列表
     */
    List<ReportPrintQueueDycVO> queueList(List<String> ips);

    /**
     * 更新
     *
     * @param status   状态
     * @param printQty 打印次数
     * @param id       主键
     */
    void updatePrintQtyById(int status, int printQty, Long id);

    /**
     * 查询待打印、重新打印的数量
     *
     * @return 总数
     */
    Long getCountNumberByStatus();
}