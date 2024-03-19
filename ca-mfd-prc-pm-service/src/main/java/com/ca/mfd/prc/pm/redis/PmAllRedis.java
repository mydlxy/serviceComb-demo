/**
 * @Description: Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.pm.redis;

import com.ca.mfd.prc.common.redis.RedisKeys;
import com.ca.mfd.prc.common.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author inkelink
 * @Description: 参数管理
 * @since 1.0.0
 */
@Component
public class PmAllRedis {
    private final String PMALLCACHENAME = "EQuality.MES.PM.Bll.PmVersionBll.PmAllInfo";
    @Autowired
    private RedisUtils redisUtils;

    public void delete(Object[] paramCodes) {
        String key = RedisKeys.getSysParamsKey();
        redisUtils.hDel(key, paramCodes);
    }

    public void set(String paramCode, String paramValue) {
        if (paramValue == null) {
            return;
        }
        String key = RedisKeys.getSysParamsKey();
        redisUtils.hSet(key, paramCode, paramValue);
    }

    public String get(String paramCode) {
        String key = RedisKeys.getSysParamsKey();
        return (String) redisUtils.hGet(key, paramCode);
    }

}
