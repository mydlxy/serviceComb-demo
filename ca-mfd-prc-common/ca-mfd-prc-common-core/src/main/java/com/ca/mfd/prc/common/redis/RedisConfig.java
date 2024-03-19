/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.redis;

import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * Redis配置
 *
 * @author inkelink
 */
@Configuration
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.redis.enable'))}")
public class RedisConfig {

    @Value("${inkelink.redis.host:}")
    private String redisHost;

    @Value("${inkelink.redis.port:6379}")
    private int redisPort;

    @Value("${inkelink.redis.timeout:30000}")
    private int redisTimeout;

    @Value("${inkelink.redis.password:}")
    private String redisAuth;

    @Value("${inkelink.redis.database:0}")
    private int redisDb;

    @Value("${inkelink.redis.jedis.pool.max-active:200}")
    private int maxActive;

    @Value("${inkelink.redis.jedis.pool.max-wait:-1}")
    private int maxWait;

    @Value("${inkelink.redis.jedis.pool.max-idle:10}")
    private int maxIdle;

    @Value("${inkelink.redis.jedis.pool.min-idle:0}")
    private int minIdle;

    @Value("${inkelink.redis.jedis.pool.time-between-eviction-runs:-1}")
    private int timeEviRuns;

    @Value("${inkelink.redis.name:}")
    private String name;

//    @Resource
//    private RedisConnectionFactory factory;

    @Bean
    public RedisConnectionFactory connectionFactory() {
        if (StringUtils.isBlank(this.name)) {
            this.name = "ca-" + UUIDUtils.getGuid();
        }
        //redis.clients:jedis:2.4.2 (jedis-2.4.2.jar)
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxWaitMillis(maxWait);
        poolConfig.setMinIdle(minIdle);
        // time-between-eviction-runs
        // poolConfig.setTimeBetweenEvictionRunsMillis(-1);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestWhileIdle(true);
        // 单点redis
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        // 哨兵redis
        // RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration();
        // 集群redis
        // RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
        redisConfig.setHostName(redisHost);
        redisConfig.setPassword(RedisPassword.of(redisAuth));
        redisConfig.setPort(redisPort);
        redisConfig.setDatabase(redisDb);

        JedisClientConfiguration clientConfig =
                JedisClientConfiguration.builder()
                        .usePooling()
                        .poolConfig(poolConfig)
                        .and()
                        .readTimeout(Duration.ofMillis(redisTimeout))
                        .clientName(name)
                        .build();
        JedisConnectionFactory factory = new JedisConnectionFactory(redisConfig, clientConfig);
        return factory;
    }

    @Bean
    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper =
        JsonMapper.builder().visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) //忽略未知字段
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true).build();// 忽略字段大小写;
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        //redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer());

        RedisConnectionFactory redisConnectionFactory = connectionFactory();
        redisTemplate.setConnectionFactory(connectionFactory());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}