package com.ca.mfd.prc.pm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eric.zhou
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY eric.zhou At 2023年4月28日
 */
@Data
public class MaintainSearchDTO {

    @Schema(title = "条件")
    private String key= StringUtils.EMPTY;

    @Schema(title = "多个物料号，")
    private String sltMaterialNo = StringUtils.EMPTY;

}
