package com.ca.mfd.prc.core.prm.service.impl;

import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.entity.CookieSession;
import com.ca.mfd.prc.common.entity.LocalSession;
import com.ca.mfd.prc.common.entity.PrmLocalSession;
import com.ca.mfd.prc.common.entity.RemoteSession;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.utils.HttpContextUtils;
import com.ca.mfd.prc.common.utils.IpUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.core.prm.service.ISessionHolder;
import com.ca.mfd.prc.core.prm.service.ISessionService;
import com.ca.mfd.prc.core.prm.service.Signature;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tom.wu
 */
@Service
public class PrmSessionHolderImpl implements ISessionHolder {

    private static final Logger logger = LoggerFactory.getLogger(PrmSessionHolderImpl.class);

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ISessionService sessionService;
    @Autowired
    private LocalCache localCache;

    /**
     * 初始化本地session对象
     *
     * @param token
     * @param userHostAddress
     */
    @Override
    public LocalSession initalSession(String token, String userHostAddress) throws Exception {
        //第一步获取客户端的会话id
        //直接从redis中获取RemoteSession
        //验证LocalSession和RemoteSession的md5值是否一致，如果不一致直接通过rpc接口获取最新的LocalSession，并缓存到本地
        //如果RemoteSession获取失败，直接调用rpc自动登陆接口获取最新的LocalSession，并缓存到本地
        CookieSession cookieSession = null;
        LocalSession localSession = null;
        try {
            String ticket = Signature.decryptToken(token);
            cookieSession = getSessionCookie(ticket);
        } catch (Exception e) {
            logger.error("", e);
//            try {
//                //兼容老版本 同时要求老版本redis添加{User}
//                String ticket = Signature.decryptCookie(token);
//                cookieSession = getSessionCookie(ticket);
//            } catch (Exception e2) {
//                logger.error("", e2);
//            }
        }
        if (cookieSession == null || StringUtils.isBlank(cookieSession.getSessionGuid())) {
            return null;
        }
        String loginType = "自动登录";

        try {
            String remokeKey = redisUtils.getRemoteSessionKey(cookieSession.getSessionGuid());
            Object remoteObject = redisUtils.get(remokeKey, redisUtils.getExpireTime());
            String remoteSessionJson = remoteObject == null ? null : remoteObject.toString();
            RemoteSession remoteSession = JsonUtils.parseObject(remoteSessionJson, RemoteSession.class);
            if (remoteSession == null) {
                localSession = sessionService.memberAutoLoginAsync(cookieSession, userHostAddress);
                return localSession;
            }
            //验证LocalSession和RemoteSession的md5值是否一致，如果不一致直接通过rpc接口获取最新的LocalSession，并缓存到本地
            PrmLocalSession localData = localCache.getObject("PrmLocalSession_" + remoteSession.getKey());

            if (localData != null && StringUtils.equals(localData.getMd5(), remoteSession.getLocalMd5())) {
                localSession = localData.getLocal();
                return localSession;
            }
            localSession = sessionService.getLocalSession(cookieSession.getSessionGuid());
            return localSession;

        } catch (InkelinkException ie) {
            String ip = IpUtils.getIpAddr(HttpContextUtils.getHttpServletRequest());
            logger.info("请求来源IP" + ip + "登录方式" + loginType +
                    "用户Token=sessionToken解析失败" + token + "sessionId=" + cookieSession.getSessionGuid() +
                    "错误信息 " + ie.getMessage());
        } catch (Exception exe) {
            logger.info("登录方式" + loginType + " \n 用户Token=sessionToken解析失败" + token +
                    "sessionId=" + cookieSession.getSessionGuid() + " \n 错误信息 " + exe.getMessage());
            throw new Exception("InitalSession:" + exe.getMessage(), exe);
        }
        return localSession;
    }

    /**
     * 获得账户在浏览器保存的数据
     *
     * @param ticket
     */
    private CookieSession getSessionCookie(String ticket) {
        if (StringUtils.isBlank(ticket)) {
            return null;
        }
        try {
            CookieSession model = new CookieSession();
            String[] cookies = ticket.split("\\+");
            model.setSessionGuid(cookies[0]);
            model.setRememberType(Integer.parseInt(cookies[1]));
            return model;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}