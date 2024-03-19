package com.ca.mfd.prc.pmc.service.impl;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.pmc.ability.hc.FMSGCallBackV31;
import com.ca.mfd.prc.pmc.entity.PmcIpcConfigEntity;
import com.ca.mfd.prc.pmc.service.IPmcCameraSdkService;
import open.hikvision.com.hcnetsdk.HCNetSDK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class HcNetSdkServiceImpl implements IPmcCameraSdkService {
    @Autowired
    @Lazy
    HCNetSDK hcNetSDK;

    @Autowired
    FMSGCallBackV31 alarmCallBack;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public int login(PmcIpcConfigEntity ipc) {
        String loginCacheUserId = "ipc:login:" + ipc.getName();
        logoutAndDisAlarm(loginCacheUserId);
        HCNetSDK.NET_DVR_DEVICEINFO_V30 deviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        int userID = hcNetSDK.NET_DVR_Login_V30(ipc.getIpAddress(), Short.parseShort(ipc.getPort()), ipc.getUsername(), ipc.getPassword(), deviceInfo);
        if (userID == -1) {
            throw new InkelinkException("ipc注册失败，错误码" + hcNetSDK.NET_DVR_GetLastError());
        }
        redisUtils.set(loginCacheUserId, userID);
        return userID;
    }

    @Override
    public void initAlarmCallback() {
        hcNetSDK.NET_DVR_SetDVRMessageCallBack_V31(alarmCallBack, null);
    }

    @Override
    public void setupAlarm(int userId) {
        String setupAlarmCache = "ipc:setupAlarm:" + userId;
        int handler = hcNetSDK.NET_DVR_SetupAlarmChan_V30(userId);
        if (handler == -1) {
            throw new InkelinkException("ipc布防失败，错误码" + hcNetSDK.NET_DVR_GetLastError());
        }
        redisUtils.set(setupAlarmCache, handler);
    }

    @Override
    public void downloadReplay(PmcIpcConfigEntity ipc, String name, String queryReplayTimeStart, String queryReplayTimeEnd) {
        int userId = getUserId(ipc);

    }

    @Override
    public void initSdk() {
        hcNetSDK.NET_DVR_Init();
    }

    private int getUserId(PmcIpcConfigEntity ipc) {
        String loginCacheUserId = "ipc:login:" + ipc.getName();
        Boolean isLogin = redisUtils.hasKey(loginCacheUserId);
        if (Boolean.TRUE.equals(isLogin)) {
            return (int) redisUtils.get(loginCacheUserId);
        }
        return login(ipc);
    }


    private void logoutAndDisAlarm(String loginCacheUserId) {
        Boolean isLogin = redisUtils.hasKey(loginCacheUserId);
        if (Boolean.TRUE.equals(isLogin)) {
            int loginUserId = (int) redisUtils.get(loginCacheUserId);
            disAlarm(loginUserId);
            if (!hcNetSDK.NET_DVR_Logout_V30(loginUserId)) {
                throw new InkelinkException("ipc登出失败，错误码" + hcNetSDK.NET_DVR_GetLastError());
            }
            redisUtils.delete(loginCacheUserId);
        }
    }

    private void disAlarm(int loginUserId) {
        String setupAlarmCache = "ipc:setupAlarm:" + loginUserId;
        Boolean isSetupAlarm = redisUtils.hasKey(setupAlarmCache);
        if (Boolean.TRUE.equals(isSetupAlarm)) {
            int alarmHandle = (int) redisUtils.get(setupAlarmCache);
            if (!hcNetSDK.NET_DVR_CloseAlarmChan_V30(alarmHandle)) {
                throw new InkelinkException("ipc撤防失败，错误码" + hcNetSDK.NET_DVR_GetLastError());
            }
            redisUtils.delete(setupAlarmCache);
        }
    }
}
