package com.ca.mfd.prc.core.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.core.report.dto.ReportQueueDTO;
import com.ca.mfd.prc.core.report.entity.RptSendEntity;
import com.ca.mfd.prc.core.report.mapper.IRptSendMapper;
import com.ca.mfd.prc.core.report.service.IRptSendService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author inkelink
 * @Description: 报表请求记录服务实现
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
@Service
public class RptSendServiceImpl extends AbstractCrudServiceImpl<IRptSendMapper, RptSendEntity> implements IRptSendService {

    /**
     * 添加打印报告接口 -- 提供各个业务模块调用
     *
     * @param reportQueue 参数
     */
    @Override
    public void addReportQueue(ReportQueueDTO reportQueue) {
        RptSendEntity model = new RptSendEntity();
        model.setBizCode(reportQueue.getPrintCode());
        model.setTargetId(reportQueue.getTargetId());
        model.setTargetType(reportQueue.getTargetType().code());
        model.setPrintDt(reportQueue.getPrintDt());
        String parameters = JsonUtils.toJsonString(reportQueue.getParameters());
        if (StringUtils.isBlank(parameters)) {
            parameters = "{}";
        }
        model.setParameters(parameters);
        if(StringUtils.isNotBlank(reportQueue.getRemark())) {
            model.setRemark(reportQueue.getRemark());
        } else {
            model.setRemark("");
        }
        model.setPrintNumber(reportQueue.getPrintNumber());
        this.insert(model);
        this.saveChange();
    }

    /**
     * 打印服务的同步数量
     *
     * @return 同步数量列表
     */
    @Override
    public List<RptSendEntity> getListByStatus() {
        QueryWrapper<RptSendEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RptSendEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RptSendEntity::getStatus, 0);
        lambdaQueryWrapper.or();
        lambdaQueryWrapper.eq(RptSendEntity::getStatus, 3);
        lambdaQueryWrapper.orderByAsc(RptSendEntity::getCreationDate);
        return this.getTopDatas(30, queryWrapper);
    }

    /**
     * 更新打印状态和次数
     *
     * @param status   状态
     * @param sendTime 次数
     * @param id       主键
     */
    @Override
    public void updateDataByStatus(int status, int sendTime, String sendRemark, Long id) {
        UpdateWrapper<RptSendEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<RptSendEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.set(RptSendEntity::getStatus, status);
        lambdaUpdateWrapper.set(RptSendEntity::getSendTimes, sendTime);
        lambdaUpdateWrapper.set(RptSendEntity::getSendRemark, sendRemark);
        lambdaUpdateWrapper.set(RptSendEntity::getLastUpdateDate, new Date());
        lambdaUpdateWrapper.eq(RptSendEntity::getId, id);
        this.update(updateWrapper);
    }

    /**
     * 更新打印次数
     *
     * @param status     状态
     * @param sendDt     发送时间
     * @param sendTimes  发送次数
     * @param sendRemark 发送备注
     * @param id         主键
     */
    @Override
    public void updateSendTimesById(int status, Date sendDt, int sendTimes, String sendRemark, Long id) {
        UpdateWrapper<RptSendEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<RptSendEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.set(RptSendEntity::getStatus, status);
        lambdaUpdateWrapper.set(RptSendEntity::getSendDt, sendDt);
        lambdaUpdateWrapper.set(RptSendEntity::getSendTimes, sendTimes);
        lambdaUpdateWrapper.set(RptSendEntity::getLastUpdateDate, new Date());
        lambdaUpdateWrapper.set(RptSendEntity::getSendRemark, sendRemark);
        lambdaUpdateWrapper.eq(RptSendEntity::getId, id);
        this.update(updateWrapper);
    }

}