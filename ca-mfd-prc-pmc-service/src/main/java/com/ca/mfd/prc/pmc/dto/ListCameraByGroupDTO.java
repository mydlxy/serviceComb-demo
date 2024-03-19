package com.ca.mfd.prc.pmc.dto;

import com.ca.mfd.prc.pmc.domain.IpcDomain;
import lombok.Setter;

public class ListCameraByGroupDTO {

    @Setter
    private String workstationCode;

    public IpcDomain toDomain() {
        IpcDomain ipcDomain = IpcDomain.initDomain();
        ipcDomain.setWorkstationCode(workstationCode);
        return ipcDomain;
    }
}
