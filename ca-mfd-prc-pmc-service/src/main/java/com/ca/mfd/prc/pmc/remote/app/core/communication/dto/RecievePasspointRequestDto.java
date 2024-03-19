package com.ca.mfd.prc.pmc.remote.app.core.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author inkelink
 * @Description: RecievePasspointRequest
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@Schema(description = "AVI站点")
public class RecievePasspointRequestDto {

    /**
     *
     */
    @Schema(title = "过点信息")
    @NotNull(message = "过点信息不能为空")
    @Size(message = "过点信息不能为空", min = 1)
    @Valid
    private List<RecievePasspointRequest> datas;


}