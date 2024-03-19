package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author eric.zhou
 * @Description: SubModelInfo
 * @date 2023年6月1日
 * @变更说明 BY eric.zhou At 2023年6月1日
 */
@Data
@Schema(title = "SubModelInfo", description = "")
public class SubModelInfo {
    @Schema(description = "subCode")
    private String subCode;
    @Schema(description = "areaCode")
    private String areaCode;

    public SubModelInfo(String subCode, String areaCode) {
        this.subCode = subCode;
        this.areaCode = areaCode;
    }
}
