package com.ca.mfd.prc.pps.communication.handlers;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.mq.rabbitmq.annotation.MesRabbitListener;
import com.ca.mfd.prc.mq.rabbitmq.entity.PatternEnum;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQContext;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQQueueConstants;
import com.ca.mfd.prc.pps.communication.dto.AsKeepCarDto;
import com.ca.mfd.prc.pps.communication.dto.EntryReportPartsDto;
import com.ca.mfd.prc.pps.communication.dto.GroupCountDto;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.entity.PpsEntryPartsEntity;
import com.ca.mfd.prc.pps.entity.PpsEntryReportPartsEntity;
import com.ca.mfd.prc.pps.service.IPpsEntryPartsService;
import com.ca.mfd.prc.pps.service.IPpsEntryReportPartsService;
import com.ca.mfd.prc.pps.service.IPpsPlanPartsService;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 离散报工
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.pps.mq.partsreport.enable'))}")
@Component
public class PartsReportHandler {
    private static final Logger logger = LoggerFactory.getLogger(PartsReportHandler.class);

    @MesRabbitListener(queues = RabbitMQQueueConstants.ADD_AS_ENTRY_REPORT_PPSJ_SUBSCRIBE, pattern = PatternEnum.AutoRetry)
    public void addMessageQueue(Message message, Channel channel) throws IOException {
        String strMessage = StringUtils.toEncodedString(message.getBody(), StandardCharsets.UTF_8);
        logger.info("离散报工addMessageQueue消费信息：" + strMessage);
        RabbitMQContext rabbitMQContext = JsonUtils.parseObject(strMessage, RabbitMQContext.class);
        if (rabbitMQContext != null) {
            try { //TODO 正式会删除 try..catch
                EntryReportPartsDto entryReport = JsonUtils.parseObject(rabbitMQContext.getContent(), EntryReportPartsDto.class);
                IPpsEntryReportPartsService ppsEntryReportPartsService = SpringContextUtils.getBean(IPpsEntryReportPartsService.class);
                IPpsEntryPartsService ppsEntryPartsService = SpringContextUtils.getBean(IPpsEntryPartsService.class);
                IPpsPlanPartsService ppsPlanPartsService = SpringContextUtils.getBean(IPpsPlanPartsService.class);
                //增加容错
                PpsEntryPartsEntity entryInfo = ppsEntryPartsService.getFirstByEntryNo(entryReport.getEntryNo());
                //容错。 找不到工单直接跳过
                if (entryInfo == null) {
                    return;
                }
                Map<Integer, List<PpsEntryReportPartsEntity>> reportsMap = ppsEntryReportPartsService.getByEntryNo(entryReport.getEntryNo())
                        .stream().collect(Collectors.groupingBy(PpsEntryReportPartsEntity::getReportType));
                List<GroupCountDto> reportStatistic = new ArrayList<>();
                for (Map.Entry<Integer, List<PpsEntryReportPartsEntity>> mp : reportsMap.entrySet()) {
                    GroupCountDto et = new GroupCountDto();
                    et.setKey(mp.getKey());
                    et.setCount(mp.getValue() == null ? 0 : mp.getValue().stream().mapToInt(s -> s.getEntryReportCount()).sum());
                    reportStatistic.add(et);
                }

                //未开始的，调整为进行中
                boolean isSetStatus = false;
                if (entryInfo.getStatus() < 10) {
                    entryInfo.setStatus(10);
                    isSetStatus = true;
                }
                //报工数量+上工序不合格数 超过计划数量时，蒋计划改为已完成
                int planQuantity = reportStatistic.stream().mapToInt(GroupCountDto::getCount).sum() + entryInfo.getSubtractQuantity();
                if (entryInfo.getStatus() == 10
                        && planQuantity >= entryInfo.getPlanQuantity()) {
                    entryInfo.setStatus(20);
                    isSetStatus = true;
                }
                //1、合格 5质检合格（发送AS后的调整）
                int qualifiedQuantity = reportStatistic.stream().filter(w -> w.getKey() == 1 || w.getKey() == 5)
                        .mapToInt(GroupCountDto::getCount).sum();
                //3、报工不合格 6质检不合格 7报废
                int scrapQuantity = reportStatistic.stream().filter(w -> w.getKey() == 3 || w.getKey() == 6 || w.getKey() == 7)
                        .mapToInt(GroupCountDto::getCount).sum();

                UpdateWrapper<PpsEntryPartsEntity> upset = new UpdateWrapper<>();
                LambdaUpdateWrapper<PpsEntryPartsEntity> upsetLmp = upset.lambda();
                upsetLmp.set(PpsEntryPartsEntity::getQualifiedQuantity, qualifiedQuantity);
                upsetLmp.set(PpsEntryPartsEntity::getScrapQuantity, scrapQuantity);
                upsetLmp.set(PpsEntryPartsEntity::getStatus, entryInfo.getStatus());
                if (isSetStatus && entryInfo.getStatus() == 10) {
                    upsetLmp.set(PpsEntryPartsEntity::getActualStartDt, new Date());
                }
                if (isSetStatus && entryInfo.getStatus() == 20) {
                    upsetLmp.set(PpsEntryPartsEntity::getActualEndDt, new Date());
                }
                upsetLmp.eq(PpsEntryPartsEntity::getId, entryInfo.getId());
                ppsEntryPartsService.update(upset);
                ppsEntryPartsService.saveChange();
                //更新计划状态、时间
                if (isSetStatus && entryInfo.getStatus() == 20) {
                    ppsPlanPartsService.setPlanEnd(entryInfo.getPlanNo());
                    ppsPlanPartsService.saveChange();
                }
                if (isSetStatus && entryInfo.getStatus() == 10) {
                    ppsPlanPartsService.setPlanStart(entryInfo.getId());
                    ppsPlanPartsService.saveChange();
                }
            } catch (Exception e) {
                logger.error("离散报工addMessageQueue消费信息失败", e);
            }
        }
    }
}
