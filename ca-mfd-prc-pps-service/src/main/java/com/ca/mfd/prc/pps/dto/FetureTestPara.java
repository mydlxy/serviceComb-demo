package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * FetureTestPara
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "FetureTestPara", description = "")
public class FetureTestPara {
    @Schema(description = "输入模板")
    private String pattern = StringUtils.EMPTY;
    @Schema(description = "特征，逗号分割")
    private String feature = StringUtils.EMPTY;
    @Schema(description = "车型。不输入默认所有车型")
    private String model = "ALLMODELS";
}
