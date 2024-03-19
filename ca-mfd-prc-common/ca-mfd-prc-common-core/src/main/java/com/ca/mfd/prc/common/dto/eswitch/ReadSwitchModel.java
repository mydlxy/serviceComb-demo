package com.ca.mfd.prc.common.dto.eswitch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 点位模型
 *
 * @author inkelink eric.zhou
 * @date 2023-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "点位模型")
public class ReadSwitchModel {


    @Schema(title = "点位地址")
    private String pointAddress;

    @Schema(title = "顺序号")
    private Integer sequenceNo = 0;

}
