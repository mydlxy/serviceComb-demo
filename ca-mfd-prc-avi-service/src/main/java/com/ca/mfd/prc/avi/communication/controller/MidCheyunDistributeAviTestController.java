package com.ca.mfd.prc.avi.communication.controller;

import com.ca.mfd.prc.avi.communication.dto.CheyunTestDto;
import com.ca.mfd.prc.avi.communication.remote.app.eps.IEpsCarCloudCommunicationService;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.CarCloudCarInfoDto;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.CarCloudCheckDto;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.CarCloudDeviceDto;
import com.ca.mfd.prc.avi.communication.service.IMidApiLogService;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.service.IAviQueueReleaseService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.mq.kafka.entity.KafkaMessageContext;
import com.ca.mfd.prc.mq.kafka.utils.KafkaProducerHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("communication/cheyundistributeavitest")
@Tag(name = "车云手动触发推送", description = "车云手动触发推送")
public class MidCheyunDistributeAviTestController {
    private static final Logger logger = LoggerFactory.getLogger(MidCheyunDistributeAviTestController.class);
    @Autowired
    private IMidApiLogService midApiLogService;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private IAviQueueReleaseService aviQueueReleaseService;
    @Autowired
    private IEpsCarCloudCommunicationService epsCarCloudCommunicationService;
    @Autowired
    @Lazy
    private KafkaProducerHelper kafkaProducerHelper;
    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    @PostMapping(value = "send")
    @Operation(summary = "组装数据推送")
    public ResultVO send(@Valid @RequestBody List<CheyunTestDto> dtoList) {
        if (dtoList == null) {
            throw new InkelinkException("查询条件不能为空");
        }
        for (CheyunTestDto entity : dtoList) {
            //整车信息组装发送kafka消息
            ResultVO<CarCloudCarInfoDto> carInfo = epsCarCloudCommunicationService.carCloudCarInfoSendTest(entity);
            if (carInfo == null || !carInfo.getSuccess()) {
                throw new InkelinkException("获取整车信息数据失败。" + (carInfo == null ? "" : carInfo.getMessage()));
            }
            logger.info("整车信息组装JSON:"+JsonUtils.toJsonString(carInfo.getData()));
            KafkaMessageContext context = new KafkaMessageContext();
            context.setContent(JsonUtils.toJsonString(carInfo.getData()));
            context.setTopic("mom-queuing-carcloud-carinfo");
            context.setKey(UUIDUtils.getGuid());
            kafkaProducerHelper.convertAndSend(context);


            //设备数据组装发送kafka
            ResultVO<CarCloudDeviceDto> device = epsCarCloudCommunicationService.carCloudDeviceSendTest(entity);
            if (device == null || !device.getSuccess()) {
                throw new InkelinkException("获取设备数据失败。" + (device == null ? "" : device.getMessage()));
            }
            logger.info("设备数据组装JSON:"+JsonUtils.toJsonString(device.getData()));
            context = new KafkaMessageContext();
            context.setContent(JsonUtils.toJsonString(device.getData()));
            context.setTopic("mom-queuing-carcloud-deviceinfo");
            context.setKey(UUIDUtils.getGuid());
            kafkaProducerHelper.convertAndSend(context);


            //终检数据组装发送kafka
            ResultVO<CarCloudCheckDto> check = epsCarCloudCommunicationService.carCloudCheckSendTest(entity);
            if (check == null || !check.getSuccess()) {
                throw new InkelinkException("获取终检数据失败。" + (check == null ? "" : check.getMessage()));
            }
            logger.info("终检数据组装JSON:"+JsonUtils.toJsonString(check.getData()));
            context = new KafkaMessageContext();
            context.setContent(JsonUtils.toJsonString(check.getData()));
            context.setTopic("mom-queuing-carcloud-finalcheck");
            context.setKey(UUIDUtils.getGuid());
            kafkaProducerHelper.convertAndSend(context);
        }
        return new ResultVO<>().ok("", "操作成功");
    }
}
