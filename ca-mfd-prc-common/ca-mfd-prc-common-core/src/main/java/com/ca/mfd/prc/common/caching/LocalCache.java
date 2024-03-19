package com.ca.mfd.prc.common.caching;

import java.util.function.Function;

/**
 * 缓存接口
 *
 * @author cwy
 * @date 2023/3/31
 */
public interface LocalCache {

    /**
     * 获取缓存对象
     *
     * @param key cache的key值
     * @param <T>
     * @return
     */
    <T> T getObject(String key);

    /**
     * 获取缓存对象
     *
     * @param key       cache的key值
     * @param fun       查询数据方法
     * @param cacheTime 缓存时间
     * @return 数据
     */
    <T> T getObject(String key, Function<Object, T> fun, int cacheTime);

    /**
     * 新增缓存
     *
     * @param key       key
     * @param obj       缓存对象
     * @param cacheTime 缓存时间,单位秒（0表示不缓存,-1表示24小时内有读取，则不释放（c#中-1表示不释放））
     */
    void addObject(String key, Object obj, int cacheTime);

    /**
     * 新增缓存
     *
     * @param key key
     * @param obj 缓存对象
     *            缓存时间,默认10分钟
     */
    void addObject(String key, Object obj);

    /**
     * 删除缓存对象
     *
     * @param keys key
     */
    void removeObject(String... keys);

    /**
     * 更新远程缓存时间
     */
    void setRemoteTime();

}
