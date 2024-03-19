package com.ca.mfd.prc.pmc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pmc.domain.IpcDomain;
import com.ca.mfd.prc.pmc.dto.IpcCameraGroupDTO;
import com.ca.mfd.prc.pmc.dto.ListCameraByGroupDTO;
import com.ca.mfd.prc.pmc.entity.PmcCameraAuthConfigEntity;
import com.ca.mfd.prc.pmc.entity.PmcCameraConfigEntity;
import com.ca.mfd.prc.pmc.entity.PmcIpcConfigEntity;
import com.ca.mfd.prc.pmc.mapper.IPmcIpcConfigMapper;
import com.ca.mfd.prc.pmc.service.IPmcCameraAuthConfigService;
import com.ca.mfd.prc.pmc.service.IPmcCameraConfigExtendService;
import com.ca.mfd.prc.pmc.service.IPmcCameraSdkService;
import com.ca.mfd.prc.pmc.service.IPmcIpcConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 工控机配置;服务实现
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
@Service
public class PmcIpcConfigServiceImpl extends AbstractCrudServiceImpl<IPmcIpcConfigMapper, PmcIpcConfigEntity> implements IPmcIpcConfigService {

    // 业务防腐层Service

    IpcDomain domain;


    @Autowired
    IPmcCameraConfigExtendService pmcCameraConfigExtendService;

    @Autowired
    IPmcCameraAuthConfigService cameraAuthConfigService;


    @Autowired
    IPmcCameraSdkService sdkService;

    @Override
    public IpcCameraGroupDTO listCameraByGroup(ListCameraByGroupDTO dto) {
        domain = dto.toDomain();
        domain.setEntityList(getData(new QueryWrapper<>(), false));
        domain.setIpcCameraGroupDTO(new IpcCameraGroupDTO());
        this.listCameraByGroupByIpc(domain);
        return domain.getIpcCameraGroupDTO();
    }


    @Override
    public void registerIpc(boolean enable) {
        if (enable) {
            domain = IpcDomain.initDomain();
            domain.setEntityList(getData(new QueryWrapper<>(), false));
            this.registerIpc(domain);
        }
    }


    private void listCameraByGroupByIpc(IpcDomain domain) {
        if (domain.initIpcNode()) {
            List<PmcCameraConfigEntity> cameraList = pmcCameraConfigExtendService.getData(new QueryWrapper<>(), false);
            addCameraNode(cameraList, domain);
        }
    }

    /**
     * 防腐层，IPC注册到SDK,进行布防，设置报警回调
     *
     * @param domain IPC工控机 域对象
     */
    private void registerIpc(IpcDomain domain) {
        sdkService.initSdk();
        // 获取IPC应用服务 查询的IPC列表
        List<PmcIpcConfigEntity> ipcList = domain.getEntityList();
        // 没有IPC中断操作
        if (CollectionUtils.isEmpty(domain.getEntityList())) {
            return;
        }
        // Ability层 设置报警回调
        sdkService.initAlarmCallback();
        for (PmcIpcConfigEntity ipc : ipcList) {
            // Ability层Sdk注册到SDK，设置布防
            sdkService.setupAlarm(sdkService.login(ipc));
        }
    }


    private void addCameraNode(List<PmcCameraConfigEntity> cameraList, IpcDomain domain) {
        List<PmcCameraConfigEntity> filterList = filterIpcGroupCameraByAuth(cameraList, domain);
        if (CollectionUtils.isNotEmpty(filterList)) {
            Map<Long, List<IpcCameraGroupDTO.CameraNode>> cameraGroupByIpc = filterList.stream().map(cameraConfigEntity -> BeanUtil.copyProperties(cameraConfigEntity, IpcCameraGroupDTO.CameraNode.class)).collect(Collectors.groupingBy(IpcCameraGroupDTO.CameraNode::getPrcPmcIpcConfigId));
            domain.addCameraNode(cameraGroupByIpc);
        }
    }

    private List<PmcCameraConfigEntity> filterIpcGroupCameraByAuth(List<PmcCameraConfigEntity> cameraList, IpcDomain domain) {
        if (CollectionUtils.isEmpty(cameraList)) {
            return cameraList;
        }

        QueryWrapper<PmcCameraAuthConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmcCameraAuthConfigEntity::getWorkstationCode, domain.getWorkstationCode());
        List<PmcCameraAuthConfigEntity> authConfigList = cameraAuthConfigService.getData(qry, false);
        PmcCameraAuthConfigEntity authConfig = authConfigList.stream().findAny().orElse(null);
        if (Objects.nonNull(authConfig)) {
            return cameraList.stream().filter(camera -> authConfig.getAuthorities().contains(camera.getAuthority())).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}