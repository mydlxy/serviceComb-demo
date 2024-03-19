package com.ca.mfd.prc.mq.rabbitmq.config;

import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQExchangeConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * MQ通道
 *
 * @author jay.he
 * @date 2023-09-05
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.rabbitmq.enable'))}")
public class RabbitMQConfig {

    @Value("${inkelink.core.mq.rabbitmq.host}")
    private String host;// = "10.23.1.152";
    @Value("${inkelink.core.mq.rabbitmq.port}")
    private int port;// = 5672;
    @Value("${inkelink.core.mq.rabbitmq.username}")
    private String username;// = "rabbitmqadmin";
    @Value("${inkelink.core.mq.rabbitmq.password}")
    private String password;// = "equality-rabbitmq";
    @Value("${inkelink.core.mq.rabbitmq.virtual-host:}")
    private String virtualHost;// = "mfd";

    @Value("${inkelink.core.mq.rabbitmq.listener.simple.retry.enabled:false}")
    private boolean listenerSimpleRetryEnabled;
    @Value("${inkelink.core.mq.rabbitmq.listener.simple.retry.max-attempts:}")
    private int listenerSimpleRetryMaxAttempts;
    @Value("${inkelink.core.mq.rabbitmq.listener.simple.retry.initial-interval:}")
    private int listenerSimpleRetryInitialInterval;
    @Value("${inkelink.core.mq.rabbitmq.listener.simple.default-requeue-rejected:}")
    private boolean listenerSimpleDefaultRequeueRejected;


    @Bean
    public ConnectionFactory rabbitMQConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        if (StringUtils.hasText(virtualHost)) {
            connectionFactory.setVirtualHost(virtualHost);
        }
        // 开启ConfirmCallback回调
        connectionFactory.setPublisherReturns(true);

        return connectionFactory;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // factory.setFailedDeclarationRetryInterval();
        //  factory.setMaxConcurrentConsumers();
        factory.setDefaultRequeueRejected(listenerSimpleDefaultRequeueRejected);
        return factory;
    }

    /**
     * 指定队列ack手动提交模式
     *
     * @return SimpleMessageListenerContainer
     */
    @Bean
    public SimpleMessageListenerContainer chatSimpleMessageListenerContainer() {
        SimpleMessageListenerContainer simpleMessageListenerContainer =
                new SimpleMessageListenerContainer(rabbitMQConnectionFactory());
        // 指定会话重试队列
        //   simpleMessageListenerContainer.setQueueNames(RabbitMQConstants.TEST1_RETRY_QUEUE);
        // 手动提交
        //   simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        simpleMessageListenerContainer.setFailedDeclarationRetryInterval(listenerSimpleRetryInitialInterval);
        // 重试次数
        simpleMessageListenerContainer.setDeclarationRetries(listenerSimpleRetryMaxAttempts);
        return simpleMessageListenerContainer;
    }

    /**
     * 创建 RabbitAdmin 类，这个类封装了对 RabbitMQ 管理端的操作！
     * <p>
     * 比如：Exchange 操作，Queue 操作，Binding 绑定 等
     *
     * @param
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }


    @Bean("mfdExchange")
    public DirectExchange direcExchange() {
        DirectExchange directExchange = ExchangeBuilder.directExchange(RabbitMQExchangeConstants.MFD_INFO_EXCHANGE).durable(true).build();

        return directExchange;
    }

    /**
     * 定义死信交换机
     *
     * @return
     */
   /* @Bean("dl_exchange")
    public DirectExchange deadExchange() {
   // public Exchange deadExchange() {
        return ExchangeBuilder.directExchange(RabbitMQConstants.DL_EXCHANGE).durable(true).build();
    }*/

  /*  @Bean("test1Queue")
    public Queue itemQueue() {
        Queue queue = QueueBuilder.durable(RabbitMQConstants.TEST1_QUEUE).build();
        return queue;
    }

    @Bean
    public Binding itemQueueExchange(@Qualifier("test1Queue") Queue queue,
                                     @Qualifier("mfdExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMQConstants.TEST1_ROUTE_KEY);
    }*/

   /* @Bean("test1RetryQueue")
    public Queue itemRetryQueue() {
        return QueueBuilder.durable(RabbitMQConstants.TEST1_RETRY_QUEUE)
                // 正常的队列，在消息失效后，需要将消息丢入 死信 交换机中
                // 这里只需要针对名称进行绑定
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DL_EXCHANGE)
                // 丢入 死信交换机，需要设定指定的 routingKey
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.TEST1_DATA_DEAD_LETTER_ROUTE_KEY).build();
    }

    @Bean
    public Binding itemRetryQueueExchange(@Qualifier("test1RetryQueue") Queue queue,
                                          @Qualifier("mfdExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMQConstants.TEST1_RETRY_ROUTE_KEY);
    }*/


   /* @Bean("testDeadQueue")
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(RabbitMQConstants.TEST1_DEAD_LETTER_QUEUE).build();
    }

    *//**
     * 将死信交换机和死信队列进行绑定
     *
     * @param deadQueue
     * @param directDeadExchange
     * @return
     *//*
    @Bean
    public Binding bindDeadExchangeAndQueue(@Qualifier("testDeadQueue") Queue deadQueue,
                                            @Qualifier("dl_exchange") DirectExchange directDeadExchange) {
        return BindingBuilder.bind(deadQueue).to(directDeadExchange)
                .with(RabbitMQConstants.TEST1_DATA_DEAD_LETTER_ROUTE_KEY);
    }*/

   /* @Bean("uploadRetryQueue")
    public Queue uploadRetryQueue() {
        return QueueBuilder.durable(RabbitMQConstants.CHAT_UPLOAD_FILE_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DL_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.CHAT_UPLOAD_FILE_DEAD_LETTER_ROUTE_KEY).build();
    }*/

   /* @Bean
    public Binding uploadRetryQueueExchange(@Qualifier("uploadRetryQueue") Queue queue,
                                            @Qualifier("chatExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMQConstants.CHAT_UPLOAD_FILE_ROUTE_KEY);
    }*/


   /* @Bean("uploadDeadLetterQueue")
    public Queue uploadDeadLetterQueue() {
        return QueueBuilder.durable(RabbitMQConstants.CHAT_UPLOAD_DEAD_LETTER_FILE_QUEUE).build();
    }*/

    /**
     * 将死信交换机和死信队列进行绑定
     * @param deadQueue
     * @param directDeadExchange
     * @return
     */
   /* @Bean
    public Binding bindUploadDeadExchangeAndQueue(@Qualifier("uploadDeadLetterQueue") Queue deadQueue,
                                                  @Qualifier("dl_exchange") DirectExchange directDeadExchange) {
        return BindingBuilder.bind(deadQueue).to(directDeadExchange)
                .with(RabbitMQConstants.CHAT_UPLOAD_FILE_DEAD_LETTER_ROUTE_KEY);
    }*/

    /**
     * 自动打标签消息队列
     *
     * @return
     */
    /*@Bean("autoTagQueue")
    public Queue autoTagQueue() {
        return QueueBuilder.durable(RabbitMQConstants.CHAT_DATA_AUTO_TAG_QUEUE).build();
    }*/

    /**
     * 自动打标签消息队列交换器绑定
     * @param queue
     * @param exchange
     * @return
     */
   /* @Bean
    public Binding autoTagQueueExchange(@Qualifier("autoTagQueue") Queue queue,
                                        @Qualifier("chatExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMQConstants.CHAT_DATA_AUTO_TAG_ROUTE_KEY);
    }*/


}
