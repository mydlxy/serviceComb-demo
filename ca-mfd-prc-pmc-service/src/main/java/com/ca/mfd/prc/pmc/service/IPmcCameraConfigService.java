package com.ca.mfd.prc.pmc.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pmc.domain.CameraDomain;
import com.ca.mfd.prc.pmc.dto.CameraPeriodResDTO;
import com.ca.mfd.prc.pmc.entity.PmcCameraConfigEntity;

import java.util.List;

/**
 *
 * @Description: 摄像头配置;服务
 * @author inkelink
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
public interface IPmcCameraConfigService extends ICrudService<PmcCameraConfigEntity> {

    List<CameraPeriodResDTO> cameraPeriod(CameraDomain cameraDomain);

    void downloadReplay(CameraDomain domain);
}