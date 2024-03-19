package com.ca.mfd.prc.pmc.domain;

import cn.hutool.core.bean.BeanUtil;
import com.ca.mfd.prc.pmc.dto.IpcCameraGroupDTO;
import com.ca.mfd.prc.pmc.entity.PmcIpcConfigEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IpcDomain {

    private IpcDomain(){}

    @Setter
    @Getter
    private String workstationCode;
    @Setter
    @Getter
    private PmcIpcConfigEntity entity;
    @Setter
    @Getter
    private List<PmcIpcConfigEntity> entityList;
    @Setter
    @Getter
    private IpcCameraGroupDTO ipcCameraGroupDTO;

    public static IpcDomain initDomain(){
        return new IpcDomain();
    }

    public boolean initIpcNode() {
        if (CollectionUtils.isEmpty(this.entityList)) {
            return false;
        }
        this.entityList.forEach(ipcEntity -> {
            IpcCameraGroupDTO.IpcNode ipcNode = BeanUtil.copyProperties(ipcEntity, IpcCameraGroupDTO.IpcNode.class);
            ipcNode.setCaremaList(new ArrayList<>());
            this.ipcCameraGroupDTO.getIpcNodeList().add(ipcNode);
        });
        return true;
    }

    public void addCameraNode(Map<Long, List<IpcCameraGroupDTO.CameraNode>> cameraGroupByIpc){
        this.ipcCameraGroupDTO.getIpcNodeList().forEach(ipcNode -> ipcNode.getCaremaList().addAll(cameraGroupByIpc.getOrDefault(ipcNode.getId(), Lists.newArrayList())));
    }
}
