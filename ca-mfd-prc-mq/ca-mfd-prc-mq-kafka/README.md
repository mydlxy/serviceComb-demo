1、发送消息
发送消息使用com.ca.mfd.prc.mq.kafka.utils.KafkaProducerHelper类中的方法，
消息发送内容必须用KafkaMessageContext类，content、topic为必传项，如有消息消费顺序需求，就传入key，示例如下：
@Autowired
KafkaProducerHelper kafkaProducerHelper;


KafkaMessageContext kafkaMessageContext = new KafkaMessageContext();
kafkaMessageContext.setTopic(topic);
kafkaMessageContext.setKey(key);
kafkaMessageContext.setContent(new KafkaMessageContent(msg));
kafkaProducerHelper.convertAndSend(kafkaMessageContext);

2、消息消费
在消息消费的方法上加上KafkaListener注解，接受参数包含ConsumerRecord<String, String> record, Acknowledgment ack
其中ack在业务处理中不会用到，但是必须加上，示例如下：

@KafkaListener(topics = {KafkaTopicConstants.TOPIC_TEST2, KafkaTopicConstants.TOPIC_TEST3})
public void topicTest2Handler(ConsumerRecord<String, String> record, Acknowledgment ack) {
Optional<String> kafkaMessage = Optional.ofNullable(record.value());
if (kafkaMessage.isPresent()) {
String message = kafkaMessage.get();
KafkaMessageContext context = JSONUtil.toBean(message, KafkaMessageContext.class);
log.info("收到kafka消息：" + message);
}

}





