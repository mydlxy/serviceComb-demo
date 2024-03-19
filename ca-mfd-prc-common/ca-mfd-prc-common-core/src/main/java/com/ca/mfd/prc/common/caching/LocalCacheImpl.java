package com.ca.mfd.prc.common.caching;

import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 缓存接口实现，使用guava.cache实现
 *
 * @author cwy
 * @date 2023/03/31
 */
@Service
public class LocalCacheImpl implements LocalCache {

    private static final int TIME_5000MS = 5000;
    private static final ConcurrentMap<String, Date> CACHE_KEYS = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Date> REMOTE_CACHE_KEYS = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Integer> CACHE_TIMES = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, Cache<String, Object>> CACHE_MANAGERS = new ConcurrentHashMap<>();
    private static final Object LOCK_OBJ = new Object();
    InheritableThreadLocal<ConcurrentMap<String, String>> cacheLocal = new InheritableThreadLocal<>();
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IdentityHelper identityHelper;
    @Value("${inkelink.redis.enable: false}")
    private boolean redisOpen;

    @Override
    public <T> T getObject(String key) {
        return (T) privateGetObject(key);
    }

    @Override
    public <T> T getObject(String key, Function<Object, T> fun, int cacheTime) {
        if (getObject(key) == null && fun != null) {
            synchronized (LOCK_OBJ) {
                if (getObject(key) == null) {
                    T val = fun.apply(key);
                    addObject(key, val, cacheTime);
                    return val;
                }
                return getObject(key);
            }
        }
        return getObject(key);
    }

    @Override
    public void addObject(String key, Object obj) {
        addObject(key, obj, 600);
    }

    @Override
    public void addObject(String key, Object obj, int cacheTime) {
        key = getCacheKey(key);
        if (obj != null && cacheTime != 0) {
            if (cacheTime < 0) {
                cacheTime = -1;
            }
            Cache<String, Object> cache;
            if (!CACHE_MANAGERS.containsKey(cacheTime)) {
                if (cacheTime < 0) {
                    cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(24 * 60 * 60, TimeUnit.SECONDS).build();
                    CACHE_MANAGERS.put(cacheTime, cache);
                } else if (cacheTime > 0) {
                    cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(cacheTime, TimeUnit.SECONDS).build();
                    CACHE_MANAGERS.put(cacheTime, cache);
                }

            }
            cache = CACHE_MANAGERS.get(cacheTime);
            cache.put(key, obj);

            CACHE_KEYS.put(key, new Date());
            CACHE_TIMES.put(key, cacheTime);
        }
    }

    /**
     * 获取缓存对象
     *
     * @param key
     * @return 缓存对象
     */
    private Object privateGetObject(String key) {
        key = getCacheKey(key);
        Date now = new Date();
        if (!REMOTE_CACHE_KEYS.containsKey(key) || (now.getTime() - REMOTE_CACHE_KEYS.get(key).getTime()) > TIME_5000MS) {
            if (redisOpen) {
                String redisKey = getRedisKey(key);
                Object kval = redisUtils.get(redisKey);
                String result = kval == null ? "" : kval.toString();
                if (StringUtils.isNotBlank(result)) {
                    Date dateTime = getRedisDate(result);
                    if (CACHE_KEYS.containsKey(key) && dateTime.getTime() > CACHE_KEYS.get(key).getTime()) {
                        //缓存已过期
                        return null;
                    }
                }
            }
            REMOTE_CACHE_KEYS.put(key, new Date());
        }

        if (!CACHE_TIMES.containsKey(key)) {
            //本地没有数据
            return null;
        }

        return CACHE_MANAGERS.get(CACHE_TIMES.get(key)).getIfPresent(key);
    }

    private String getRedisTimeStr() {
        Date dt = DateUtils.addDateSeconds(new Date(), 2);
        return DateUtils.format(dt, DateUtils.DATE_TIME_PATTERN_M);
    }

    private Date getRedisDate(String now) {
        return DateUtils.parse(now, DateUtils.DATE_TIME_PATTERN_M);
    }

    private String getRedisKey(String key) {
        return ("Cache_" + key).replace("-", "_");
    }

    private String getCacheKey(String txt) {
        return identityHelper.getDomain() + "-" + txt;
    }

    /**
     * 删除缓存对象.
     */
    @Override
    public void removeObject(String... keys) {
        List<String> kys = Arrays.asList(keys);
        if (kys == null || kys.size() == 0) {
            return;
        }
        for (String text : keys) {
            String removeKey = getCacheKey(text);
            CACHE_TIMES.remove(removeKey);
            CACHE_KEYS.remove(removeKey);
            REMOTE_CACHE_KEYS.remove(removeKey);
            // 更新服务端时间（原来的代码是savechange方法执行，这里执行点是afterXXX之后执行）
            if (redisOpen) {
                String redisKey = getRedisKey(removeKey);
                getCacheLoacl().put(redisKey, removeKey);
            }
        }
    }

    private ConcurrentMap<String, String> getCacheLoacl() {
        if (cacheLocal.get() == null) {
            cacheLocal.set(Maps.newConcurrentMap());
        }
        return cacheLocal.get();
    }

    @Override
    public void setRemoteTime() {
        ConcurrentMap<String, String> cacheKeys = getCacheLoacl();
        if (cacheKeys != null && cacheKeys.keySet().size() > 0) {
            String cachetime = getRedisTimeStr();
            for (String key : cacheKeys.keySet()) {
                redisUtils.set(key, cachetime);
            }
            cacheLocal.get().clear();
        }
    }

}
