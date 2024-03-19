package com.ca.mfd.prc.avi.remote.app.pm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author banny.luo
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY banny.luo At 2023年4月28日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "AndonDownInfoDTO")
public class AndonDownInfoDTO {

    private Boolean enable;

    private short stopType;

    private short delayTime;
}
