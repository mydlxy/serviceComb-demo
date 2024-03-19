package com.ca.mfd.prc.pmc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.domain.CameraDomain;
import com.ca.mfd.prc.pmc.dto.CameraPeriodResDTO;
import com.ca.mfd.prc.pmc.dto.DownloadCameraReplayDTO;
import com.ca.mfd.prc.pmc.entity.PmcCameraConfigEntity;
import com.ca.mfd.prc.pmc.service.IPmcCameraConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 摄像头配置;Controller
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
@RestController
@RequestMapping("equality.mes.pmc/{version}/pmccameraconfig")
@Tag(name = "摄像头配置;服务", description = "摄像头配置;")
public class PmcCameraConfigController extends BaseController<PmcCameraConfigEntity> {

    IPmcCameraConfigService service;

    @Autowired
    public PmcCameraConfigController(IPmcCameraConfigService pmcCameraConfigService) {
        this.crudService = pmcCameraConfigService;
        this.service = pmcCameraConfigService;
    }

    @Operation(summary = "查询产品在工位的生产时间段")
    @GetMapping("/cameraPeriod")
    public ResultVO<List<CameraPeriodResDTO>> cameraPeriod(String workstationCode, String sn) {
        ResultVO<List<CameraPeriodResDTO>> result = new ResultVO<>();
        CameraDomain cameraDomain = CameraDomain.initDomain();
        cameraDomain.setWorkstationCode(workstationCode);
        cameraDomain.setSn(sn);
        return result.ok(service.cameraPeriod(cameraDomain));
    }

    @Operation(summary = "下载摄像头回放")
    @GetMapping("/downloadReplay")
    public void downloadReplay(DownloadCameraReplayDTO dto){
        service.downloadReplay(dto.toDomain());
    }

}