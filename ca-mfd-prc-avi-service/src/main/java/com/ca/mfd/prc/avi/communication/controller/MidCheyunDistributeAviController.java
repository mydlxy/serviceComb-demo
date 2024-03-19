package com.ca.mfd.prc.avi.communication.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.communication.remote.app.eps.IEpsCarCloudCommunicationService;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.CarCloudCarInfoDto;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.CarCloudCheckDto;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.CarCloudDeviceDto;
import com.ca.mfd.prc.avi.communication.service.IMidApiLogService;
import com.ca.mfd.prc.avi.entity.AviQueueReleaseEntity;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("communication/cheyundistributeavi")
@Tag(name = "车云下发接口", description = "车云下发接口")
public class MidCheyunDistributeAviController {
    private static final Logger logger = LoggerFactory.getLogger(MidCheyunDistributeAviController.class);
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

    @GetMapping(value = "carcloudcarinfosend")
    @Operation(summary = "整车信息下发")
    public ResultVO carcloudcarinfosend() {
        ResultVO resultVO = new ResultVO();
        String queueStr = sysConfigurationProvider.getConfiguration("CheyunCarinfoQueueReleaseSet", "CheyunCarinfoAviQueue");
        if (StringUtils.isBlank(queueStr)) {
            throw new InkelinkException("没有配置队列代码[CheyunCarinfoQueueReleaseSet]");
        }
        try {
            //查询队列数据
            QueryWrapper<AviQueueReleaseEntity> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<AviQueueReleaseEntity> lambdaQueryWrapper = queryWrapper.lambda();
            lambdaQueryWrapper.eq(AviQueueReleaseEntity::getIsSend, false);
            lambdaQueryWrapper.in(AviQueueReleaseEntity::getQueueCode, queueStr);
            List<AviQueueReleaseEntity> datas = aviQueueReleaseService.getData(queryWrapper, false).stream().sorted(Comparator.comparing(AviQueueReleaseEntity::getCreationDate)).collect(Collectors.toList());;
            //处理过点数据触发下发
            if (CollectionUtils.isNotEmpty(datas)) {
                for (AviQueueReleaseEntity entity : datas) {
                    ResultVO<CarCloudCarInfoDto> carInfo = epsCarCloudCommunicationService.carCloudCarInfoSend(entity.getSn());
                    if (carInfo == null || !carInfo.getSuccess()) {
                        throw new InkelinkException("获取整车信息数据失败。" + (carInfo == null ? "" : carInfo.getMessage()));
                    }

                    logger.info("整车信息组装JSON:" + JsonUtils.toJsonString(carInfo.getData()));
                    //组装发送消息
                    KafkaMessageContext context = new KafkaMessageContext();
                    context.setContent(JsonUtils.toJsonString(carInfo.getData()));
                    context.setTopic("mom-queuing-carcloud-carinfo");
                    context.setKey(entity.getId().toString());
                    //kafka发送消息
                    kafkaProducerHelper.convertAndSend(context);
                    //更新队列数据状态为已发送
                    entity.setIsSend(true);
                    aviQueueReleaseService.update(entity);
                    aviQueueReleaseService.saveChange();
                }
            }
        } catch (Exception e) {
            return resultVO.error(-1, "车云：整车信息发送kafka失败" + e.getMessage());
        }
        return resultVO.ok("", "处理成功");
    }

    @PostMapping(value = "/provider/certificateno")
    @Operation(summary = "推送合格证书编号")
    public ResultVO certificateNoSend(@RequestBody CarCloudCarInfoDto dto) {
        ResultVO resultVO = new ResultVO();
        try {
            //组装发送消息
            KafkaMessageContext context = new KafkaMessageContext();
            context.setContent(JsonUtils.toJsonString(dto));
            context.setTopic("mom-queuing-carcloud-carinfo");
            context.setKey(UUIDUtils.getGuid());
            //kafka发送消息
            kafkaProducerHelper.convertAndSend(context);
            resultVO.ok("", "车云:推送合格证编号成功!");
            return resultVO;
        } catch (Exception e) {
            resultVO.setMessage(e.getMessage());
            return resultVO;
        }
    }


    @GetMapping(value = "carclouddevicesend")
    @Operation(summary = "设备数据下发")
    public ResultVO carCloudDeviceSend() {
        ResultVO resultVO = new ResultVO();
        String queueStr = sysConfigurationProvider.getConfiguration("CheyunDeviceQueueReleaseSet", "CheyunDeviceAviQueue");
        if (StringUtils.isBlank(queueStr)) {
            throw new InkelinkException("没有配置队列代码[CheyunDeviceQueueReleaseSet]");
        }
        try {
            //查询队列数据
            QueryWrapper<AviQueueReleaseEntity> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<AviQueueReleaseEntity> lambdaQueryWrapper = queryWrapper.lambda();
            lambdaQueryWrapper.eq(AviQueueReleaseEntity::getIsSend, false);
            lambdaQueryWrapper.in(AviQueueReleaseEntity::getQueueCode, queueStr);
            List<AviQueueReleaseEntity> datas = aviQueueReleaseService.getData(queryWrapper, false).stream().sorted(Comparator.comparing(AviQueueReleaseEntity::getCreationDate)).collect(Collectors.toList());;
            //处理过点数据触发下发
            if (CollectionUtils.isNotEmpty(datas)) {
                for (AviQueueReleaseEntity entity : datas) {
                    ResultVO<CarCloudDeviceDto> carInfo = epsCarCloudCommunicationService.carCloudDeviceSend(entity.getSn());
                    if (carInfo == null || !carInfo.getSuccess()) {
                        throw new InkelinkException("获取设备数据失败。" + (carInfo == null ? "" : carInfo.getMessage()));
                    }

                    logger.info("设备数据组装JSON:" + JsonUtils.toJsonString(carInfo.getData()));

                    //组装发送消息
                    KafkaMessageContext context = new KafkaMessageContext();
                    context.setContent(JsonUtils.toJsonString(carInfo.getData()));
                    context.setTopic("mom-queuing-carcloud-deviceinfo");
                    context.setKey(entity.getId().toString());
                    //kafka发送消息
                    kafkaProducerHelper.convertAndSend(context);
                    //更新队列数据状态为已发送
                    entity.setIsSend(true);
                    aviQueueReleaseService.update(entity);
                    aviQueueReleaseService.saveChange();
                }
            }
        } catch (Exception e) {
            return resultVO.error(-1, "车云：设备信息数据发送kafka失败" + e.getMessage());
        }
        return resultVO.ok("", "处理成功");
    }

    @GetMapping(value = "carcloudchecksend")
    @Operation(summary = "终检数据下发")
    public ResultVO carCloudCheckSend() {
        ResultVO resultVO = new ResultVO();
        String queueStr = sysConfigurationProvider.getConfiguration("CheyunCheckQueueReleaseSet", "CheyunCheckAviQueue");
        if (StringUtils.isBlank(queueStr)) {
            throw new InkelinkException("没有配置队列代码[CheyunCheckQueueReleaseSet]");
        }
        try {
            //查询队列数据
            QueryWrapper<AviQueueReleaseEntity> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<AviQueueReleaseEntity> lambdaQueryWrapper = queryWrapper.lambda();
            lambdaQueryWrapper.eq(AviQueueReleaseEntity::getIsSend, false);
            lambdaQueryWrapper.in(AviQueueReleaseEntity::getQueueCode, queueStr);
            List<AviQueueReleaseEntity> datas = aviQueueReleaseService.getData(queryWrapper, false).stream().sorted(Comparator.comparing(AviQueueReleaseEntity::getCreationDate)).collect(Collectors.toList());
            //处理过点数据触发下发
            if (CollectionUtils.isNotEmpty(datas)) {
                for (AviQueueReleaseEntity entity : datas) {
                    ResultVO<CarCloudCheckDto> carInfo = epsCarCloudCommunicationService.carCloudCheckSend(entity.getSn());
                    if (carInfo == null || !carInfo.getSuccess()) {
                        throw new InkelinkException("获取终检数据失败。" + (carInfo == null ? "" : carInfo.getMessage()));
                    }

                    logger.info("终检数据组装JSON:" + JsonUtils.toJsonString(carInfo.getData()));

                    //组装发送消息
                    KafkaMessageContext context = new KafkaMessageContext();
                    context.setContent(JsonUtils.toJsonString(carInfo.getData()));
                    context.setTopic("mom-queuing-carcloud-finalcheck");
                    context.setKey(entity.getId().toString());
                    //kafka发送消息
                    kafkaProducerHelper.convertAndSend(context);
                    //更新队列数据状态为已发送
                    entity.setIsSend(true);
                    aviQueueReleaseService.update(entity);
                    aviQueueReleaseService.saveChange();
                }
            }
        } catch (Exception e) {
            return resultVO.error(-1, "车云：终检数据发送kafka失败" + e.getMessage());
        }
        return resultVO.ok("", "处理成功");
    }
}
