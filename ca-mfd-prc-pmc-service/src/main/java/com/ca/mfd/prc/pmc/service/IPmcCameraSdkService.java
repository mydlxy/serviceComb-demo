package com.ca.mfd.prc.pmc.service;

import com.ca.mfd.prc.pmc.entity.PmcIpcConfigEntity;

public interface IPmcCameraSdkService {

    int login(PmcIpcConfigEntity ipc);

    void initAlarmCallback();

    void setupAlarm(int userId);

    void downloadReplay(PmcIpcConfigEntity ipc, String name, String queryReplayTimeStart, String queryReplayTimeEnd);

    void initSdk();
}
