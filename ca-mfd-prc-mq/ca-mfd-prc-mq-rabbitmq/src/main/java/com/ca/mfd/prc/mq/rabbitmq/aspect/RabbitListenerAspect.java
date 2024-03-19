package com.ca.mfd.prc.mq.rabbitmq.aspect;

import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.mq.rabbitmq.annotation.MesRabbitListener;
import com.ca.mfd.prc.mq.rabbitmq.entity.MQLogInformation;
import com.ca.mfd.prc.mq.rabbitmq.entity.PatternEnum;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQConstants;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQContext;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.MqQueuesExceptionEntity;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.provider.RabbitMqQueuesExceptionProvider;
import com.ca.mfd.prc.mq.rabbitmq.service.IRabbitMqProducerService;
import com.rabbitmq.client.Channel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Aspect
@Component
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.rabbitmq.enable'))}")
public class RabbitListenerAspect {
    private static final Logger logger = LoggerFactory.getLogger(RabbitListenerAspect.class);

    @Autowired
    IRabbitMqProducerService rabbitMqProducerService;
    @Autowired
    RabbitMqQueuesExceptionProvider mqQueuesExceptionProvider;
    @Autowired
    RedisUtils redisUtils;

    @Around("@annotation(com.ca.mfd.prc.mq.rabbitmq.annotation.MesRabbitListener)")
    public Object RabbitListenerExceptionHandle(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        MesRabbitListener myRabbitListenerAnn = method.getAnnotation(MesRabbitListener.class);

        if (myRabbitListenerAnn == null) {
            return joinPoint.proceed();
        }
        final RabbitMQContext rabbitMQContext = getRabbitMQContextByArgs(joinPoint.getArgs());

        // 获取注解中定义的字段值
        PatternEnum pattern = myRabbitListenerAnn.pattern();
        List<MQLogInformation> mqLogInformations = new ArrayList<>();
        MQLogInformation mqLogInformation = new MQLogInformation();
        mqLogInformation.setContent(rabbitMQContext.getContent());
        mqLogInformation.setNoteId(rabbitMQContext.getNotesId());
        mqLogInformation.setStartTime(new Date());


        Object objLog = redisUtils.hGet(RabbitMQConstants.REDIS_KEY_MQ_QUEUE_LOG, String.valueOf(rabbitMQContext.getNotesId()));

        if (objLog != null) {
            String strLog = String.valueOf(objLog);
            List<MQLogInformation> redisLogs = new ArrayList<>();
            try {
                redisLogs = JsonUtils.parseArray(strLog, MQLogInformation.class);
            } catch (Exception ex) {

            }

            //  List<MQLogInformation> redisLogs = JSONUtil.toList(strLog, MQLogInformation.class);
            if (!CollectionUtils.isEmpty(redisLogs)) {
                if (redisLogs.size() >= RabbitMQConstants.REDIS_KEY_MQ_QUEUE_MAX_NUM) {
                    redisLogs = redisLogs.subList(0, 8);
                }
                mqLogInformations.addAll(redisLogs);
            }
        }
        mqLogInformations.add(0, mqLogInformation);
        StopWatch stopWatch = new StopWatch("MQ");
        stopWatch.start();
        try {
            //执行目标方法
            return joinPoint.proceed();
        } catch (Exception ex) {
            if (pattern.equals(PatternEnum.General)) {
                logger.info("常规模式，休眠2秒后，再调用一次，如果再不成功，就记录一条数据到异常表");
                Thread.sleep(2 * 1000L);
                try {
                    return joinPoint.proceed();
                } catch (Exception secondEx) {
                    mqLogInformation.setErrMessage(secondEx.getMessage());
                    //记录数据库
                    MqQueuesExceptionEntity mqQueuesExceptionEntity = new MqQueuesExceptionEntity();
                    mqQueuesExceptionEntity.setMessage(secondEx.getMessage());
                    mqQueuesExceptionEntity.setStackTrace(Arrays.toString(secondEx.getStackTrace()));
                    mqQueuesExceptionEntity.setNotesId(rabbitMQContext.getNotesId());
                    mqQueuesExceptionEntity.setContent(rabbitMQContext.getContent());
                    mqQueuesExceptionProvider.edit(mqQueuesExceptionEntity);
                }

            } else if (pattern.equals(PatternEnum.AutoRetry)) {
                logger.info("自动重试模式，休眠2的N次方秒后，再调用一次，如果再不成功，就直接发送一条消息到消息队列");
                try {
                    return joinPoint.proceed();
                } catch (Exception secondEx) {
                    Thread thread = new Thread(() -> {
                        try {
                            retrySendMq(rabbitMQContext);
                        } catch (InterruptedException e) {
                            logger.error(e.getMessage());
                            Thread.currentThread().interrupt();
                        }
                    });
                    thread.start();

                    mqLogInformation.setErrMessage(secondEx.getMessage());
                }
            } else {
                logger.info("rabbitmq消息处理失败,消息内容：" + JsonUtils.toJsonString(rabbitMQContext));
                //  log.info("rabbitmq消息处理失败,消息内容：" + JSONUtil.toJsonStr(rabbitMQContext));
            }
            throw ex;
        } finally {
            stopWatch.stop();
            mqLogInformation.setOverTime(new Date());
            mqLogInformation.setExpendTime(stopWatch.getTotalTimeSeconds());
            //  redisUtils.hSet(RabbitMQConstants.REDIS_KEY_MQ_QUEUE_LOG, String.valueOf(rabbitMQContext.getNotesId()), JSONUtil.toJsonStr(mqLogInformations));
            redisUtils.hSet(RabbitMQConstants.REDIS_KEY_MQ_QUEUE_LOG, String.valueOf(rabbitMQContext.getNotesId()), JsonUtils.toJsonString(mqLogInformations));
        }
    }

    private RabbitMQContext getRabbitMQContextByArgs(Object[] args) {
        RabbitMQContext rabbitMQContext = null;
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof Message) {
                    Message message = (Message) arg;
                    rabbitMQContext = JsonUtils.parseObject(org.apache.commons.lang3.StringUtils.toEncodedString(message.getBody(), StandardCharsets.UTF_8), RabbitMQContext.class);
                    //    rabbitMQContext = JSONUtil.toBean(new String(message.getBody()), RabbitMQContext.class);
                    //   rabbitMQContext = JSONUtil.toBean(Base64.getEncoder().encodeToString(message.getBody()), RabbitMQContext.class);
                    if (rabbitMQContext == null || !StringUtils.hasText(rabbitMQContext.getTopic()) || rabbitMQContext.getNotesId() == null || rabbitMQContext.getNotesId() <= 0) {
                        // throw new IllegalArgumentException("消息队列参数错误 '" + Base64.getEncoder().encodeToString(message.getBody()) + "'");
                        throw new IllegalArgumentException("消息队列参数错误 '" + org.apache.commons.lang3.StringUtils.toEncodedString(message.getBody(), StandardCharsets.UTF_8) + "'");
                    }
                } else if (arg instanceof Channel) {
                    Channel channel = (Channel) arg;

                    logger.info("通道信息：" + channel.toString());
                }
            }
        }
        return rabbitMQContext;
    }

    private void retrySendMq(RabbitMQContext rabbitMQContext) throws InterruptedException {
        rabbitMQContext.setRetryCount(rabbitMQContext.getRetryCount() + 1);

        long sleepSecond = (long) Math.pow(2, rabbitMQContext.getRetryCount()) * 1000;
        Thread.sleep(sleepSecond);
        rabbitMqProducerService.send(rabbitMQContext);
    }

}
