package com.ca.mfd.prc.core.prm.utils;

import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.HttpContextUtils;
import com.ca.mfd.prc.common.utils.IpUtils;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.core.prm.dto.UserLoginLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户锁定工具类
 *
 * @author eric.zhou
 */
public class LoginLockVerificationUtils {

    private static final Logger logger = LoggerFactory.getLogger(LoginLockVerificationUtils.class);

    private static final Object lockObj = new Object();
    private static final int LockTime = 1800;

    /**
     * 判断是否已经被锁定
     *
     * @param userName 名字
     */
    public static void verificationisLock(String userName) {
        String ip = IpUtils.getIpAddr(HttpContextUtils.getHttpServletRequest());
        String cacheName = userName.toLowerCase();
        LocalCache localCache = SpringContextUtils.getBean(LocalCache.class);

        UserLoginLock datas = localCache.getObject(cacheName);
        Integer count = 3;
        if (datas != null) {
            if (datas.getLgoinCount() >= count) {
                throw new InkelinkException("账户已被锁定");
            }
        }
    }

    /**
     * 移除锁定
     *
     * @param userName 名字
     */
    public static void removeVerificationLock(String userName) {
        String ip = IpUtils.getIpAddr(HttpContextUtils.getHttpServletRequest());
        String cacheName = userName.toLowerCase();
        LocalCache localCache = SpringContextUtils.getBean(LocalCache.class);
        UserLoginLock datas = localCache.getObject(cacheName);
        if (datas != null) {
            localCache.removeObject(cacheName);
        }
    }

    /**
     * 加入锁定中
     *
     * @param userName      名字
     * @param loginLockTime 名字
     * @return 锁定结果
     */
    public static Boolean verificationLock(String userName, Integer loginLockTime) {
        if (loginLockTime == null) {
            loginLockTime = 1800;
        }
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String ip = IpUtils.getIpAddr(request);
        String userAgent = request.getHeader("User-Agent");
        if (StringUtils.isBlank(userAgent)) {
            userAgent = "httpClient请求";
        }
        String cacheName = userName.toLowerCase();
        LocalCache localCache = SpringContextUtils.getBean(LocalCache.class);
        UserLoginLock datas = localCache.getObject(cacheName);
        if (datas == null) {
            synchronized (lockObj) {
                datas = localCache.getObject(cacheName);
                if (datas == null) {
                    UserLoginLock userLoginLock = new UserLoginLock();
                    userLoginLock.setLgoinCount(1);
                    userLoginLock.setUserName(userName);
                    userLoginLock.setUserAgent(userAgent);
                    userLoginLock.setLoginIp(ip);
                    localCache.addObject(cacheName, userLoginLock, loginLockTime);
                }
            }
        } else {
            datas = localCache.getObject(cacheName);
            Integer loginCount = 3;
            if (datas.getLgoinCount() >= loginCount) {
                return true;
            }
            datas.setLgoinCount(datas.getLgoinCount() + 1);
            localCache.addObject(cacheName, datas, LockTime);
        }

        return false;
    }
}
