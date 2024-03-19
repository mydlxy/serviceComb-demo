1、发送消息
发送消息使用com.ca.mfd.prc.mq.rocketmq.utils.RocketMqProducerHelper类中的方法，
此类提供了发送同步消息、异步消息、单向消息等工具方法，大多数情况用convertAndSend（同步消息）即可
消息发送内容必须用RocketMQMessageContext类，content、topic为必传项，示例如下：
@Autowired
RocketMqProducerHelper producer;


RocketMQMessageContext rocketMQContext = new RocketMQMessageContext();
rocketMQContext.setTopic(topic);
rocketMQContext.setContent(new RocketMQMessageContent(msg));
rocketMQContext.setTag(tag);
producer.convertAndSend(rocketMQContext);
return new ResultVO().ok("", "发送消息成功！");

2、消息消费
2.1消费者组处理定义
针对一个消费者组，只能定义一个消费者组的处理类，参照com.ca.mfd.prc.mq.rocketmq.handler.RocketMqConsumerGroupHandler1类所示
@Component
@RocketMQMessageListener(topic = "", consumerGroup = "${rocketmq.consumer.groupDefault}")
public class RocketMqConsumerGroupHandler1 extends BaseRocketMqConsumerGroupHandler implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {
    public RocketMqConsumerGroupHandler1(List<IRocketMqConsumer> actions) {
        this.consumers.addAll(actions);
    }
    @Override
    public void onMessage(MessageExt message) {
    }
    @SneakyThrows
    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        consumer.subscribe(RocketMqTopicConstants.TOPIC_TEST1, "*");
        consumer.subscribe(RocketMqTopicConstants.TOPIC_TEST2, "*");
        this.basePrepareStart(consumer);
    }
}
一个消费者组处理类，需使用RocketMQMessageListener注解，注解中topic可设空字符，consumerGroup设置为消费者组的名称
该类需要继承BaseRocketMqConsumerGroupHandler，并实现RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener接口
消费者组需要在prepareStart方法中将其需要处理的topic都注册进去，最后在prepareStart中调用this.basePrepareStart(consumer);
其他的基本上复制之后改名字即可

2.2topic消费处理类
针对每一个topic，都会有具体的处理逻辑，需要定义相应的处理类，可惨参照com.ca.mfd.prc.mq.rocketmq.handler.RocketMqConsumer1：

@MesRocketMqConsumer(topic = RocketMqTopicConstants.TOPIC_TEST1)
@Component
public class RocketMqConsumer1 implements IRocketMqConsumer {
    @Override
    public void onMessage(MessageExt message) { 
        // todo
    }
}
该类需实现IRocketMqConsumer接口，并加上MesRocketMqConsumer注解，注解中设置该消费者要处理的topic，然后在onMessage进行具体业务处理即可





