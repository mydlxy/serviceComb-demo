1、发送消息
rabbitmq发送消息，事实上是向PRC_SYS_QUEUE_NOTE表写入一条数据，跟.NET原来业务一样，具体使用：
1.1 定义
@Autowired
RabbitMqSysQueueNoteProvider sysQueueNoteService;

1.2发送
public ResultVO addSimpleMessage(SysQueueNoteEntity content) {
    sysQueueNoteService.addSimpleMessage(content);
    return new ResultVO().ok(null, "发送消息数据落库成功！");
}
1.3发送消息到消息队列
按照目前的业务逻辑，应该不需要业务开发中，调用直接发送消息到消息队列的方法，都是通过1.2写一条发消息记录到数据库即可
发送消息到消息队列，用RabbitMqProducerServiceImpl类的public void send(RabbitMQContext msg)方法
传入参数必须是RabbitMQContext类或者其子类，notesId、topic、content都是必传项，示例如下：
@Autowired
IRabbitMqProducerService producerService;

RabbitMQContext rabbitMQContext = new RabbitMQContext();
rabbitMQContext.setNotesId(mqQueuesNotesEntity.getId());
rabbitMQContext.setTopic(mqQueuesNotesEntity.getTopic());
rabbitMQContext.setContent(each.getContent());
producerService.send(rabbitMQContext);

2、消息消费
可参照com.ca.mfd.prc.modules.communication.handler.MQHandlerTest示例

消息队列的定义，需定义到com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQQueueConstants中，示例如下：
public static final String TEST1_QUEUE = "mq.mfd.queue.test1";

消费处理函数需加上MesRabbitListener注解，示例如下：
@MesRabbitListener(queues = RabbitMQQueueConstants.TEST2_QUEUE, pattern = PatternEnum.General)
public void test2Queue(Message message, Channel channel) throws IOException {
      // todo
}

MesRabbitListener注解pattern说明：
PatternEnum.General：如果消费处理异常，将再次调用处理方法，如果再次处理失败，将写入一条记录到PRC_MQ_QUEUES_EXCEPTION异常信息表
PatternEnum.AutoRetry：如果消费处理异常,将自动再发送一条消息到消息队列，一直到处理成功位置，该类型主要针对消息必须处理成功不能丢失的情况
PatternEnum.Sequence：消息处理失败，记录一条文本日志即可
