/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis工具类
 *
 * @author inkelink
 */
@Component
public class RedisUtils {
    /**
     * 默认过期时长为24小时，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24L;
    /**
     * 过期时长为1小时，单位：秒
     */
    public final static long HOUR_ONE_EXPIRE = 60 * 60 * 1L;
    /**
     * 过期时长为6小时，单位：秒
     */
    public final static long HOUR_SIX_EXPIRE = 60 * 60 * 6L;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1L;
    @Autowired
    @Lazy
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${inkelink.redis.prefix:CA}")
    private String prefix;
    @Value("${inkelink.redis.keySplit::}")
    private String keySplit;
    @Value("${inkelink.redis.expireTime:3600}")
    private Integer expireTime;

    public Integer getExpireTime() {
        return expireTime;
    }

    private String getRedisKey(String key) {
        return prefix + keySplit + key;
    }

    public String getRemoteSessionKey(String sessionId) {
        //{RemoteSession}:4127da36-af09-4648-aa53-b935447e2055//_sessionCacheReader.KeySplit
        return "{RemoteSession}" + keySplit + sessionId;
    }

    public void set(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(getRedisKey(key), value);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    public Object get(String key, long expire) {
        Object value = redisTemplate.opsForValue().get(getRedisKey(key));
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
        return value;
    }

    public Object get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        redisTemplate.delete(getRedisKey(key));
    }

    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys.stream().map(key -> getRedisKey(key)).collect(Collectors.toList()));
    }

    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(getRedisKey(key), field);
    }

    public Map<String, Object> hGetAll(String key) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(getRedisKey(key));
    }

    public void hmSet(String key, Map<String, Object> map) {
        hmSet(getRedisKey(key), map, DEFAULT_EXPIRE);
    }

    public void hmSet(String key, Map<String, Object> map, long expire) {
        redisTemplate.opsForHash().putAll(getRedisKey(key), map);

        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public void hSet(String key, String field, Object value) {
        hSet(key, field, value, DEFAULT_EXPIRE);
    }

    public void hSet(String key, String field, Object value, long expire) {
        redisTemplate.opsForHash().put(getRedisKey(key), field, value);

        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public void expire(String key, long expire) {
        redisTemplate.expire(getRedisKey(key), expire, TimeUnit.SECONDS);
    }

    public void hDel(String key, Object... fields) {
        redisTemplate.opsForHash().delete(getRedisKey(key), fields);
    }

    public void leftPush(String key, Object value) {
        leftPush(key, value, DEFAULT_EXPIRE);
    }

    public void leftPush(String key, Object value, long expire) {
        redisTemplate.opsForList().leftPush(getRedisKey(key), value);

        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(getRedisKey(key));
    }
}