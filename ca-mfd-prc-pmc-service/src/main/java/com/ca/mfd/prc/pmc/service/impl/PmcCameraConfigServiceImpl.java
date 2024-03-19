package com.ca.mfd.prc.pmc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pmc.domain.CameraDomain;
import com.ca.mfd.prc.pmc.dto.CameraPeriodResDTO;
import com.ca.mfd.prc.pmc.entity.PmcCameraConfigEntity;
import com.ca.mfd.prc.pmc.entity.PmcIpcConfigEntity;
import com.ca.mfd.prc.pmc.mapper.IPmcCameraConfigMapper;
import com.ca.mfd.prc.pmc.remote.app.eps.entity.EpsWorkplaceTimeLogEntity;
import com.ca.mfd.prc.pmc.remote.app.eps.provider.EpsWorkplaceTimeLogProvider;
import com.ca.mfd.prc.pmc.service.IPmcCameraConfigService;
import com.ca.mfd.prc.pmc.service.IPmcCameraSdkService;
import com.ca.mfd.prc.pmc.service.IPmcIpcConfigExtendService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author inkelink
 * @Description: 摄像头配置;服务实现
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
@Service
public class PmcCameraConfigServiceImpl extends AbstractCrudServiceImpl<IPmcCameraConfigMapper, PmcCameraConfigEntity> implements IPmcCameraConfigService {


    @Autowired
    EpsWorkplaceTimeLogProvider epsWorkplaceTimeLogProvider;
    @Autowired
    IPmcIpcConfigExtendService pmcIpcConfigExtendService;
    @Autowired
    IPmcCameraSdkService sdkService;

    @Override
    public List<CameraPeriodResDTO> cameraPeriod(CameraDomain cameraDomain) {
        return this.cameraPeriodDo(cameraDomain);
    }

    @Override
    public void downloadReplay(CameraDomain domain) {
        QueryWrapper<PmcCameraConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmcCameraConfigEntity::getName, domain.getEntity().getName());
        List<PmcCameraConfigEntity> entities = getData(qry, false);
        PmcCameraConfigEntity camera = entities.stream().findAny().orElse(null);
        if (Objects.isNull(camera)) {
            throw new InkelinkException("未找到摄像头信息");
        }
        domain.cameraLinkIpc(camera.getPrcPmcIpcConfigId());
        this.downloadReplayDo(domain);
    }


    private List<CameraPeriodResDTO> cameraPeriodDo(CameraDomain cameraDomain) {
        List<EpsWorkplaceTimeLogEntity> epsWorkplaceTimeLogs = epsWorkplaceTimeLogProvider.getBySn(cameraDomain.getWorkstationCode(), cameraDomain.getSn());
        List<CameraPeriodResDTO> dataList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(epsWorkplaceTimeLogs)) {
            epsWorkplaceTimeLogs.forEach(epsWorkplaceTimeLogEntity -> {
                CameraPeriodResDTO cameraPeriodResDTO = new CameraPeriodResDTO();
                cameraPeriodResDTO.setStartTime(epsWorkplaceTimeLogEntity.getInsertWorkplaceDt());
                cameraPeriodResDTO.setEndTime(epsWorkplaceTimeLogEntity.getLastOperDt());
                dataList.add(cameraPeriodResDTO);
            });
        }

        return dataList;
    }

    private void downloadReplayDo(CameraDomain domain) {
        PmcIpcConfigEntity ipc = pmcIpcConfigExtendService.get(domain.getEntity().getPrcPmcIpcConfigId());
        if (Objects.isNull(ipc)) {
            throw new InkelinkException("未找到摄像头所属工控机信息");
        }
        sdkService.downloadReplay(ipc, domain.getEntity().getName(), domain.getQueryReplayTimeStart(), domain.getQueryReplayTimeEnd());
    }
}