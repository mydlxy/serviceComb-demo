package com.ca.mfd.prc.pmc.dto;

import com.ca.mfd.prc.pmc.domain.CameraDomain;
import com.ca.mfd.prc.pmc.entity.PmcCameraConfigEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "下载视频回放DTO")
@Data
public class DownloadCameraReplayDTO {
    @Schema(title = "摄像头名称")
    private String cameraName;
    @Schema(title = "开始时间")
    private String startTime;
    @Schema(title = "结束时间")
    private String endTime;

    public CameraDomain toDomain() {
        CameraDomain cameraDomain = CameraDomain.initDomain();
        PmcCameraConfigEntity entity = new PmcCameraConfigEntity();
        entity.setName(cameraName);
        entity.setWorkstationCode(cameraName);
        cameraDomain.setEntity(entity);
        cameraDomain.setQueryReplayTimeStart(startTime);
        cameraDomain.setQueryReplayTimeEnd(endTime);
        return cameraDomain;
    }
}
