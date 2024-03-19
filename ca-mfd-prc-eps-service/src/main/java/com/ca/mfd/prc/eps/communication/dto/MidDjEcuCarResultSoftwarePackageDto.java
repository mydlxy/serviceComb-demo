package com.ca.mfd.prc.eps.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 电检软件结果信息softwarePackage
 * @date 2024年2月19日
 */
@Data
@Schema(description = "电检软件结果信息softwarePackage")
public class MidDjEcuCarResultSoftwarePackageDto {

    @Schema(title = "软件类型")
    private String packType = StringUtils.EMPTY;

    @Schema(title = "校验码")
    private String verifyValue = StringUtils.EMPTY;

    @Schema(title = "校验类型")
    private String verifyType = StringUtils.EMPTY;

    @Schema(title = "包地址")
    private String url = StringUtils.EMPTY;

    @Schema(title = "软件包大小")
    private String packSize = StringUtils.EMPTY;
}
