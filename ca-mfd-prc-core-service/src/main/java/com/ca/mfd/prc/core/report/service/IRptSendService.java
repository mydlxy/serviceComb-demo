package com.ca.mfd.prc.core.report.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.report.dto.ReportQueueDTO;
import com.ca.mfd.prc.core.report.entity.RptSendEntity;

import java.util.Date;
import java.util.List;

/**
 * @author inkelink
 * @Description: 报表请求记录服务
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
public interface IRptSendService extends ICrudService<RptSendEntity> {
    /**
     * 添加打印报告接口 -- 提供各个业务模块调用
     *
     * @param model 参数
     */
    void addReportQueue(ReportQueueDTO model);

    /**
     * 打印服务的同步数量
     *
     * @return 同步数量列表
     */
    List<RptSendEntity> getListByStatus();

    /**
     * 更新打印状态和次数
     *
     * @param status   状态
     * @param sendTime 次数
     * @param id       主键
     */
    void updateDataByStatus(int status, int sendTime, String sendRemark, Long id);

    /**
     * 更新打印次数
     *
     * @param status     状态
     * @param sendDt     发送时间
     * @param sendTimes  发送次数
     * @param sendRemark 发送备注
     * @param id         主键
     */
    void updateSendTimesById(int status, Date sendDt, int sendTimes, String sendRemark, Long id);
}