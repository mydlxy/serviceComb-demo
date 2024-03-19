package com.ca.mfd.prc.core.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.message.entity.MqQueuesTopicEntity;
import com.ca.mfd.prc.core.message.service.IMqQueuesExceptionService;
import com.ca.mfd.prc.core.message.service.IMqQueuesNotesService;
import com.ca.mfd.prc.core.message.service.IMqQueuesTopicService;
import com.ca.mfd.prc.mq.kafka.entity.KafkaMessageContext;
import com.ca.mfd.prc.mq.kafka.utils.KafkaProducerHelper;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.SysQueueNoteEntity;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.provider.RabbitMqSysQueueNoteProvider;
import com.ca.mfd.prc.mq.rocketmq.entity.RocketMQMessageContent;
import com.ca.mfd.prc.mq.rocketmq.entity.RocketMQMessageContext;
import com.ca.mfd.prc.mq.rocketmq.utils.RocketMqProducerHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * MQ任务调度
 *
 * @author jay.he
 * @date 2023-09-13
 */
@RestController
@RequestMapping("message/scheduling")
@Tag(name = "MQ任务调度")
public class MQSchedulingController extends BaseApiController {
    @Autowired
    IMqQueuesNotesService mqQueuesNotesService;
    @Autowired
    IMqQueuesTopicService mqQueuesTopicService;
    @Autowired
    IMqQueuesExceptionService mqQueuesExceptionService;
    @Autowired
    RabbitMqSysQueueNoteProvider sysQueueNoteService;
    @Lazy
    @Autowired
    RocketMqProducerHelper producer;
    @Lazy
    @Autowired
    KafkaProducerHelper kafkaProducerHelper;
    @Resource
    private TaskExecutor task;

   /* @Autowired
    IMqQueuesExceptionService mqQueuesExceptionService;*/

    /*@Autowired
    public MQSchedulingController(IMqQueuesExceptionService mqQueuesExceptionService) {
        this.crudService = mqQueuesExceptionService;
        //   this.mqQueuesExceptionService = mqQueuesExceptionService;
    }*/
    @Value("${inkelink.core.mq.kafka.enable:}")
    private String ttt;

    /*@GetMapping(value = "removeNoUseQueue/{queueNoteId}/{totalMinute}")
    @Operation(summary = "删除totalMinute分钟以前的队列")
    public ResultVO removeNoUseQueue(@PathVariable(value = "queueNoteId") Long queueNoteId,
                                     @PathVariable(value = "totalMinute") Integer totalMinute) {

        mqQueuesNotesService.removeNoUseQueue(queueNoteId, totalMinute);

        return new ResultVO().ok(null, "删除totalMinute分钟以前的队列成功！");
    }*/

    @GetMapping(value = "autoretry/{queueNoteId}")
    @Operation(summary = "自动重试错误队列")
    public ResultVO autoRetry(@PathVariable(value = "queueNoteId") Long queueNoteId) throws JsonProcessingException {

        mqQueuesExceptionService.autoRetry(queueNoteId);

        return new ResultVO().ok(null, "自动重试成功！");
    }

    /**
     * 轮询队列 将数据库表中待发送的MQ消息，发送到消息队列中
     *
     * @return
     */
    @PostMapping(value = "pollqueue", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "轮询队列")
    public ResultVO pollQueue() {
        QueryWrapper<MqQueuesTopicEntity> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().select(MqQueuesTopicEntity::getGroupName).groupBy(MqQueuesTopicEntity::getGroupName);
        List<MqQueuesTopicEntity> mqQueuesTopicEntityList = mqQueuesTopicService.getData(queryWrapper, false);
        if (!CollectionUtils.isEmpty(mqQueuesTopicEntityList)) {
            for (MqQueuesTopicEntity entity : mqQueuesTopicEntityList) {
                task.execute(() -> mqQueuesNotesService.pollQueue(entity.getGroupName()));
                //  mqQueuesNotesService.pollQueue(entity.getGroupName());
            }
        }
        return new ResultVO().ok(null, "轮询队列成功！");
    }

    @PostMapping(value = "createsubscribe", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "创建订阅（创建消息队列并绑定交换器）")
    public ResultVO createSubscribe() {

        mqQueuesNotesService.createSubscribe();

        return new ResultVO().ok(null, "创建订阅成功！");
    }

    @PostMapping(value = "updatechannelqueuecount", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "更新队列消息数量")
    public ResultVO updateChannelQueueCount() {

        mqQueuesNotesService.updateQueueMessageCount();

        return new ResultVO().ok(null, "更新队列数量成功！");
    }

    @PostMapping(value = "addsimplemessage", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "测试发送消息数据落库")
    public ResultVO addSimpleMessage(@RequestBody SysQueueNoteEntity content) {

        sysQueueNoteService.addSimpleMessage(content);

        return new ResultVO().ok(null, "发送消息数据落库成功！");
    }

    @ApiOperation(value = "RocketMQ测试发送信息")
    @Operation(summary = "RocketMQ测试发送信息")
    @RequestMapping(value = "/sendrocketmqmesage", method = RequestMethod.GET)
    public ResultVO sendRocketMqMesage(@RequestParam(value = "topic") String topic,
                                       @RequestParam(value = "msg") String msg,
                                       @RequestParam(value = "tag", required = false) String tag) {


        //发送
        RocketMQMessageContext rocketMQContext = new RocketMQMessageContext();
        rocketMQContext.setTopic(topic);
        RocketMQMessageContent rocketMQMessageContent = new RocketMQMessageContent();
        rocketMQMessageContent.setContent(msg);
        rocketMQContext.setContent(JsonUtils.toJsonString(rocketMQMessageContent));
        rocketMQContext.setTag(tag);

        producer.convertAndSend(rocketMQContext);//发送同步消息

       /* //发送迟延消息
        rocketMQContext.setTimeout(5 * 1000);
        rocketMQContext.setDelayLevel(1);
        producer.syncSendDelay(rocketMQContext);*/

        /*//发送异步消息
        producer.asyncSend(rocketMQContext);*/

        /*//发送单向消息
        producer.sendOneWay(rocketMQContext);*/

        //同步发送顺序消息
        String sn = "L1NSPGHB1PB080534";
        rocketMQContext.setOrderlyHashKey(sn);
        producer.syncSendOrderly(rocketMQContext);

        return new ResultVO().ok("", "发送消息成功！");
        // LOGGER.info("输出生产者信息=" + sendResult);
        // return Result.sendSuccess("发送信息成功", sendResult);
    }

    @ApiOperation(value = "kafka测试发送信息")
    @Operation(summary = "kafka测试发送信息")
    @RequestMapping(value = "/sendkafkamesagestring", method = RequestMethod.GET)
    public ResultVO sendKafkaMesageString(@RequestParam(value = "topic") String topic,
                                          @RequestParam(value = "msg") String msg,
                                          @RequestParam(value = "key", required = false) String key) {
        //发送
        KafkaMessageContext kafkaMessageContext = new KafkaMessageContext();
        kafkaMessageContext.setTopic(topic);
        kafkaMessageContext.setKey(key);
        kafkaMessageContext.setContent(msg);
        kafkaProducerHelper.convertAndSend(kafkaMessageContext);
        return new ResultVO().ok("", "发送消息成功！");
        // LOGGER.info("输出生产者信息=" + sendResult);
        // return Result.sendSuccess("发送信息成功", sendResult);
    }

   /* @Autowired
    KafkaAdmin kafkaAdmin;*/

    @ApiOperation(value = "kafka手动创建topic")
    @Operation(summary = "kafka手动创建topic")
    @RequestMapping(value = "/kafkacreatetopic", method = RequestMethod.GET)
    public ResultVO kafkacreatetopic(@RequestParam(value = "topic") String topic) {
      /*  Map<String, Object> configs = kafkaAdmin.getConfigurationProperties();
        AdminClient adminClient = KafkaAdminClient.create(configs);

        NewTopic newTopic = new NewTopic(topic, 1, (short) 1);
        Collection<NewTopic> newTopics = Lists.newArrayList(newTopic);
        adminClient.createTopics(newTopics);*/
        return new ResultVO().ok("", "创建topic成功！");
    }
}
