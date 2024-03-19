package com.ca.mfd.prc.pmc.ability.hc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.pmc.entity.PmcCameraAlarmEntity;
import com.ca.mfd.prc.pmc.entity.PmcCameraConfigEntity;
import com.ca.mfd.prc.pmc.service.IPmcCameraAlarmService;
import com.ca.mfd.prc.pmc.service.IPmcCameraConfigExtendService;
import com.sun.jna.Pointer;
import open.hikvision.com.hcnetsdk.HCNetSDK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FMSGCallBackV31 implements HCNetSDK.FMSGCallBack_V31 {

    @Autowired
    IPmcCameraConfigExtendService pmcCameraConfigExtendService;
    @Autowired
    IPmcCameraAlarmService cameraAlarmService;

    //报警信息回调函数
    @Override
    public boolean invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        Long cameraId = getCamaraMap().getOrDefault(new String(pAlarmer.sDeviceName, StandardCharsets.UTF_8), 0L);
        if (HCNetSDK.COMM_IPCCFG == lCommand && cameraId > 0) {
            saveAlarm(cameraId, readAlarmInfo(pAlarmInfo).dwAlarmType);
        }
        return true;
    }

    private Map<String, Long> getCamaraMap() {
        List<PmcCameraConfigEntity> cameraConfigEntityList = pmcCameraConfigExtendService.getData(new QueryWrapper<>(), false);
        return cameraConfigEntityList.stream().collect(Collectors.toMap(PmcCameraConfigEntity::getName, PmcCameraConfigEntity::getId));
    }

    private void saveAlarm(Long cameraId, int dwAlarmType) {
        PmcCameraAlarmEntity alarm = new PmcCameraAlarmEntity();
        alarm.setPrcPmcCameraConfigId(cameraId);
        //报警时间
        Date nowTime = new Date();
        alarm.setAlarmTime(nowTime);
        alarm.setType(dwAlarmType);
        String alarmContent = convertAlarmContent(dwAlarmType);
        alarm.setContent(alarmContent);
        cameraAlarmService.insert(alarm);
    }

    private String convertAlarmContent(int dwAlarmType) {
        String sAlarmType = "";
        switch (dwAlarmType) {
            case 0:
                sAlarmType = "信号量报警";
                break;
            case 1:
                sAlarmType = "硬盘满";
                break;
            case 2:
                sAlarmType = "信号丢失";
                break;
            case 3:
                sAlarmType = "移动侦测";
                break;
            case 4:
                sAlarmType = "硬盘未格式化";
                break;
            case 5:
                sAlarmType = "读写硬盘出错";
                break;
            case 6:
                sAlarmType = "遮挡报警";
                break;
            case 7:
                sAlarmType = "制式不匹配";
                break;
            case 8:
                sAlarmType = "非法访问";
                break;
            default:
                break;
        }
        return sAlarmType;
    }

    private HCNetSDK.NET_DVR_ALARMINFO_V30 readAlarmInfo(Pointer pAlarmInfo) {
        HCNetSDK.NET_DVR_ALARMINFO_V30 strAlarmInfoV30 = new HCNetSDK.NET_DVR_ALARMINFO_V30();
        strAlarmInfoV30.write();
        Pointer pInfoV30 = strAlarmInfoV30.getPointer();
        pInfoV30.write(0, pAlarmInfo.getByteArray(0, strAlarmInfoV30.size()), 0, strAlarmInfoV30.size());
        strAlarmInfoV30.read();
        return strAlarmInfoV30;
    }
}
