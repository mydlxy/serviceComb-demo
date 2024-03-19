package com.ca.mfd.prc.mq.kafka.aspect;

import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Component
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.kafka.enable'))}")
public class KafkaListenerAspect {
    private static final Logger logger = LoggerFactory.getLogger(KafkaListenerAspect.class);
    @Autowired
    RedisUtils redisUtils;
    private final static Integer MAX_RECONSUME_TIMES = 3;//最大重试次数（消息中传了key的才能重试）
    public final static String KAFKA_CONSUMER_RETRY_KEY = "CA:Kafka:Consumer:Retry";// "kafka_consumer_retry";

    @Around("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public Object KafkaListenerExceptionHandle(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取目标方法上的注解
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        KafkaListener kafkaListenerAnn = method.getAnnotation(KafkaListener.class);

        if (kafkaListenerAnn == null) {
            //判断是否有MyRabbitListener注解，如果没有加MyRabbitListener注解：不进行逻辑处理，直接放行；
            return joinPoint.proceed();
        }

        Object[] args = joinPoint.getArgs();
        Acknowledgment ack = null;
        ConsumerRecord consumerRecord = null;
        String key = null;
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof ConsumerRecord) {
                    consumerRecord = (ConsumerRecord) arg;
                    Optional<String> kafkaMessage = Optional.ofNullable(String.valueOf(consumerRecord.value()));
                    if (kafkaMessage.isPresent()) {
                        String context = kafkaMessage.get();
                        //   kafkaMessageContext = JSONUtil.toBean(context, KafkaMessageContext.class);
                        logger.info("收到kafka消息：" + context);
                    }
                    Object objKey = consumerRecord.key();
                    if (objKey != null) {
                        key = String.valueOf(consumerRecord.key());
                    }

                } else if (arg instanceof Acknowledgment) {
                    ack = (Acknowledgment) arg;
                }
            }
        }
        if (ack == null) {
            throw new IllegalArgumentException("消费处理没有Acknowledgment参数!");
        }

        StopWatch stopWatch = new StopWatch("MQ");
        stopWatch.start();
        boolean exceptionFlag = false;
        int retryNum = 0;
        try {
            //执行目标方法
            return joinPoint.proceed();
        } catch (Exception ex) {
            exceptionFlag = true;
            if (key != null) {
                Object redisRetryNum = redisUtils.hGet(KAFKA_CONSUMER_RETRY_KEY, key);
                if (redisRetryNum != null) {
                    retryNum = Integer.parseInt(String.valueOf(redisRetryNum));
                }
                retryNum++;
                redisUtils.hSet(KAFKA_CONSUMER_RETRY_KEY, key,String.valueOf(retryNum));
            }
            logger.error("kafka消费数据异常，数据：" + JsonUtils.toJsonString(consumerRecord) + "，异常信息：" + ex.getMessage());
            // throw ex;
        } finally {
            //没有异常提交；key没有值的话不管是否有异常都提交；key有值并且有异常但是重试次数超过最大次数也提交
            if (!exceptionFlag || !StringUtils.hasText(key) || retryNum > MAX_RECONSUME_TIMES) {
                ack.acknowledge();
            } else {
                ack.nack(2000);
            }
            stopWatch.stop();
        }
        return null;
    }

}
