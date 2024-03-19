package com.ca.mfd.prc.core.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.validator.AssertUtils;
import com.ca.mfd.prc.core.message.dto.ChannelTopicsModelDTO;
import com.ca.mfd.prc.core.message.entity.MqQueuesNotesEntity;
import com.ca.mfd.prc.core.message.entity.MqQueuesTopicEntity;
import com.ca.mfd.prc.core.message.service.IMqQueuesNotesService;
import com.ca.mfd.prc.core.message.service.IMqQueuesTopicService;
import com.ca.mfd.prc.mq.rabbitmq.entity.MQLogInformation;
import com.ca.mfd.prc.mq.rabbitmq.entity.QueueProperties;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQConstants;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQContext;
import com.ca.mfd.prc.mq.rabbitmq.service.IRabbitMqProducerService;
import com.ca.mfd.prc.mq.rabbitmq.service.IRabbitMqQueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * MQ通道
 *
 * @author jay.he
 * @date 2023-09-05
 */
@RestController
@RequestMapping("message/mqqueuesnotes")
@Tag(name = "MQ主题")
public class MqQueuesNotesController extends BaseController<MqQueuesNotesEntity> {

    @Autowired
    IMqQueuesNotesService mqQueuesNotesService;
    @Autowired
    IMqQueuesTopicService mqQueuesTopicService;
    @Lazy
    @Autowired
    IRabbitMqQueueService rabbitMqQueueService;
    @Lazy
    @Autowired
    IRabbitMqProducerService rabbitMqProducerService;
    @Autowired
    RedisUtils redisUtils;

    @Autowired
    public MqQueuesNotesController(IMqQueuesNotesService mqQueuesNotesService) {
        this.crudService = mqQueuesNotesService;
        this.mqQueuesNotesService = mqQueuesNotesService;
    }

    @PostMapping(value = "getpagedata", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取分页数据")
    public ResultVO<PageData<MqQueuesNotesEntity>> getPageData(@RequestBody PageDataDto model) {
        PageData<MqQueuesNotesEntity> page = crudService.page(model);
        if (!CollectionUtils.isEmpty(page.getDatas())) {
            page.getDatas().stream().forEach(c -> {
                c.setTopic(c.getClassName());
                c.setChannelName(c.getNamespaceName());
            });
        }
        return new ResultVO<PageData<MqQueuesNotesEntity>>().ok(page, "获取数据成功");
    }

    @PostMapping(value = "edit", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "更新")
    public ResultVO edit(@RequestBody MqQueuesNotesEntity dto) {
        dto.setClassName(dto.getTopic());
        dto.setNamespaceName(dto.getChannelName());
        // dto.setChannelName(dto.getNamespaceName());

        /*model.ClassName = model.Topic;
        model.NamespaceName = model.ChannelName;
        model.ChannelName = $"{model.NamespaceName}.{model.ClassName}";
        model.Topic = $"{model.NamespaceName}.{model.ClassName}.Handler";*/

        Long id = crudService.currentModelGetKey(dto);

        QueryWrapper<MqQueuesTopicEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MqQueuesTopicEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(MqQueuesTopicEntity::getGroupName, dto.getGroupName());
        lambdaQueryWrapper.last("limit 0,1");
        List<MqQueuesTopicEntity> topicEntities = mqQueuesTopicService.getData(queryWrapper, false);
        if (CollectionUtils.isEmpty(topicEntities)) {
            throw new InkelinkException("没有对应的主题！");
        }
        MqQueuesTopicEntity topicEntity = topicEntities.get(0);
        dto.setAppId(topicEntity.getAppId());
        dto.setChannelName(dto.getNamespaceName() + "." + dto.getClassName());
        dto.setTopic(dto.getNamespaceName() + "." + dto.getClassName() + ".Handler");

        if (id == null || id <= 0) {
            crudService.save(dto);
        } else {
            //要先删除原来的队列
            MqQueuesNotesEntity entity = mqQueuesNotesService.get(id);
            if (entity != null) {
                rabbitMqQueueService.delQueue(entity.getChannelName());
            }
            crudService.update(dto);
        }
        crudService.saveChange();

        //保存成功后，创建新队列
        QueueProperties queueProperties = new QueueProperties();
        queueProperties.setTopic(dto.getTopic());
        queueProperties.setChannelName(dto.getChannelName());
        List<QueueProperties> queuePropertiesList = new ArrayList<>();
        queuePropertiesList.add(queueProperties);
        rabbitMqQueueService.createRabbitMqQueue(queuePropertiesList);

        return new ResultVO<MqQueuesNotesEntity>().ok(dto, "保存成功");
    }

    @PostMapping(value = "del", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "删除")
    public ResultVO delete(@RequestBody IdsModel model) {
        //效验数据
        AssertUtils.isArrayEmpty(model.getIds(), "id");
        for (String id : model.getIds()) {
            MqQueuesNotesEntity entity = mqQueuesNotesService.get(id);
            if (entity != null) {
                rabbitMqQueueService.delQueue(entity.getChannelName());
            }

        }
        crudService.delete(model.getIds());
        crudService.saveChange();
        return new ResultVO<String>().ok("", "删除成功");
    }

    @GetMapping(value = "getqueuemessagecount")
    @Operation(summary = "获得队列消息数量")
    public ResultVO getQueueMessageCount(String queueName) {
        Integer count = mqQueuesNotesService.getQueueMessageCount(queueName);

        return new ResultVO<Integer>().ok(count, "操作成功");
    }

    @GetMapping(value = "getrabbitmqloggers")
    @Operation(summary = "查看请求日志100条")
    public ResultVO getRabbitMQLoggers(String channelName) {

        QueryWrapper<MqQueuesNotesEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MqQueuesNotesEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(MqQueuesNotesEntity::getChannelName, channelName);
        lambdaQueryWrapper.eq(MqQueuesNotesEntity::getStatus, 1);
        lambdaQueryWrapper.last("limit 0,1");
        List<MqQueuesNotesEntity> mqQueuesNotesEntities = mqQueuesNotesService.getData(queryWrapper, false);

        List<MQLogInformation> mqLogInformations = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mqQueuesNotesEntities)) {
            Object objLog = redisUtils.hGet(RabbitMQConstants.REDIS_KEY_MQ_QUEUE_LOG, String.valueOf(mqQueuesNotesEntities.get(0).getId()));
            if (objLog != null) {
                String strLog = String.valueOf(objLog);
                // mqLogInformations = JSONUtil.toList(strLog, MQLogInformation.class);
                mqLogInformations = new ArrayList<>();
                try {
                    mqLogInformations = JsonUtils.parseArray(strLog, MQLogInformation.class);
                } catch (Exception ex) {

                }
                //  mqLogInformations = JSONUtil.toList(strLog, MQLogInformation.class);
            }
            if (!CollectionUtils.isEmpty(mqLogInformations)) {
                for (MQLogInformation each : mqLogInformations) {
                    each.setChannelName(mqQueuesNotesEntities.get(0).getChannelName());
                    each.setTopic(mqQueuesNotesEntities.get(0).getTopic());
                    each.setClassName(mqQueuesNotesEntities.get(0).getClassName());
                    each.setNameSpace(mqQueuesNotesEntities.get(0).getNamespaceName());
                }
            }
        }
        return new ResultVO<List<MQLogInformation>>().ok(mqLogInformations, "查询成功");
    }

    @GetMapping(value = "gettopics")
    @Operation(summary = "根据组名获取所有的MQ的Topic")
    public ResultVO getTopics(String groupName) {
        QueryWrapper<MqQueuesNotesEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MqQueuesNotesEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(MqQueuesNotesEntity::getGroupName, groupName);
        lambdaQueryWrapper.eq(MqQueuesNotesEntity::getStatus, 1);
        // lambdaQueryWrapper.last("limit 0,1");
        List<MqQueuesNotesEntity> mqQueuesNotesEntities = mqQueuesNotesService.getData(queryWrapper, false);
        List<String> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mqQueuesNotesEntities)) {
            result = mqQueuesNotesEntities.stream().map(c -> c.getTopic()).collect(Collectors.toList());
        }
        return new ResultVO<List<String>>().ok(result, "查询成功");
    }

    @GetMapping(value = "getchannels")
    @Operation(summary = "获取所有的通道")
    public ResultVO getChannels() {
        QueryWrapper<MqQueuesNotesEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MqQueuesNotesEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(MqQueuesNotesEntity::getStatus, 1);
        List<MqQueuesNotesEntity> mqQueuesNotesEntities = mqQueuesNotesService.getData(queryWrapper, false);

        List<ChannelTopicsModelDTO> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mqQueuesNotesEntities)) {
            for (MqQueuesNotesEntity each : mqQueuesNotesEntities) {
                ChannelTopicsModelDTO data = new ChannelTopicsModelDTO();
                data.setChannelName(each.getChannelName());
                data.setTopic(each.getTopic());
                result.add(data);
            }
        }
        return new ResultVO<List<ChannelTopicsModelDTO>>().ok(result, "查询成功");
    }


    /*@GetMapping(value = "testsendmq")
    @Operation(summary = "测试发送消息队列")
    public ResultVO testSendMQ(String routingKey, String message) throws Exception {
        ResultVO result = new ResultVO<>();
        result.setMessage("测试发送消息队列成功！");
        //   scheduleProvider.pauseOrDelScheduleJobAsync(schedulerConfig.scheduler(), jobKey.getGroup(), jobKey.getName(), true);
        QueryWrapper<MqQueuesNotesEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MqQueuesNotesEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(MqQueuesNotesEntity::getTopic, routingKey);
        lambdaQueryWrapper.eq(MqQueuesNotesEntity::getStatus, 1);
        List<MqQueuesNotesEntity> mqQueuesNotesEntities = mqQueuesNotesService.getData(queryWrapper, false);
        Long nodeId = 0l;
        if (!CollectionUtils.isEmpty(mqQueuesNotesEntities)) {
            nodeId = mqQueuesNotesEntities.get(0).getId();
        }
        if (nodeId <= 0) {
            return result.error("没有对应的队列！");
        }

       *//* RabbitMQContext.MessageContent messageContent = new RabbitMQContext.MessageContent();
        messageContent.setContent(message);*//*

        RabbitMQContext rabbitMQContext = new RabbitMQContext();
        rabbitMQContext.setNotesId(nodeId);
        rabbitMQContext.setGroupName(mqQueuesNotesEntities.get(0).getGroupName());
        rabbitMQContext.setTopic(routingKey);
        rabbitMQContext.setContent(message);
        rabbitMqProducerService.send(rabbitMQContext);
        return result.ok("");
    }*/

}
