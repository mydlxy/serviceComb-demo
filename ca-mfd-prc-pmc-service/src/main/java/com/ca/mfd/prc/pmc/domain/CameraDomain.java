package com.ca.mfd.prc.pmc.domain;

import com.ca.mfd.prc.pmc.entity.PmcCameraConfigEntity;
import lombok.Getter;
import lombok.Setter;

public class CameraDomain {
    private CameraDomain() {
    }

    @Setter
    @Getter
    private String workstationCode;

    @Setter
    @Getter
    private String sn;
    @Getter
    @Setter
    private PmcCameraConfigEntity entity;
    @Getter
    @Setter
    private String queryReplayTimeStart;
    @Getter
    @Setter
    private String queryReplayTimeEnd;

    public static CameraDomain initDomain() {
        return new CameraDomain();
    }


    public void cameraLinkIpc(Long prcPmcIpcConfigId) {
        this.entity.setPrcPmcIpcConfigId(prcPmcIpcConfigId);
    }
}
