package com.ca.mfd.prc.gateway.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ca.mfd.prc.gateway.service.IApiPlatformService;
import com.ca.mfd.prc.gateway.utils.CaLoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * <p>功能描述: API平台相关</p>
 *
 * @author
 * @version 2023年7月4日
 */
@Service("apiPlatformService")
public class ApiPlatformImpl implements IApiPlatformService {
    private static final Logger logger = LoggerFactory.getLogger(ApiPlatformImpl.class);

    /**
     * <p>功能描述: ApiPlatformImpl.java</p>
     *
     * @param apiAppKey
     * @param apiPlatformKey
     * @return
     * @author wujc
     * @version 2023年7月4日
     * @see IApiPlatformService#chkApiPlatform(String, String)
     */
    @Override
    public Boolean chkApiPlatform(String apiAppKey, String apiPlatformKey) {
        boolean bool = false;
        int scount = 60;
        int mcount = 1000;

        try {
            String curappkey = CaLoggerUtil.decryptByPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNAxmwY49PTotkSTPrShIvOJOYaEVBJfo+YyjkG9PWwtzIos1sphtA6jVwBBT3HdIL0Gs7xNXBaDd/GvqVve4TLT33MNJwdVd1IWkPnPldL4foqeP6iK2g5uSDGzlY/iN06ChWM8WSQz7DhoZg50L/weneKzhlC9hQXebPJzClawIDAQAB", apiPlatformKey);
            //请确认格式 是"bom_1688444182447"时间戳
            if (StrUtil.isNotBlank(curappkey)) {
                int lastUnderscoreIndex = curappkey.lastIndexOf('_');
                String appkey = curappkey.substring(0, lastUnderscoreIndex);
                String timestr = curappkey.substring(lastUnderscoreIndex + 1);
                appkey = StrUtil.isNotBlank(appkey) ? appkey.trim() : "error";
                timestr = StrUtil.isNotBlank(timestr) ? timestr.trim() : "0";
                //判断时间是否有效
                int vmin = 2;
                if (Instant.now().toEpochMilli() <= (Long.parseLong(timestr.trim()) + (vmin * scount * mcount))) {
                    //判断API项目名是否有效
                    if (appkey.equalsIgnoreCase(apiAppKey)) {
                        bool = true;
                    } else {
                        if (logger.isWarnEnabled()) {
                            logger.warn("API平台转发请求过期，请求项目号：" + apiAppKey + "，实际号：" + appkey);
                        }
                    }
                } else {
                    //过期
                    if (logger.isWarnEnabled()) {
                        logger.warn("API平台转发请求过期，项目号：" + apiAppKey);
                    }
                }
            }
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("API平台转发请求码解码失败，密文：" + apiPlatformKey);
            }
        }
        return bool;
    }
}
