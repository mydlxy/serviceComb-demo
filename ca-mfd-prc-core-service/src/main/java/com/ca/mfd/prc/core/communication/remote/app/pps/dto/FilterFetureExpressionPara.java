package com.ca.mfd.prc.core.communication.remote.app.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * FilterFetureExpressionPara
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "FilterFetureExpressionPara", description = "")
public class FilterFetureExpressionPara {
    @Schema(description = "产品条码")
    private String barcode = StringUtils.EMPTY;
    @Schema(description = "特征表达式集合")
    private List<String> fetureExpressions = new ArrayList<>();
}
