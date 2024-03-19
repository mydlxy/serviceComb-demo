package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductEqumentWoData
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "ProductEqumentWoData", description = "")
public class ProductEqumentWoData implements Serializable {

    @Schema(description = "tableName")
    private String tableName = StringUtils.EMPTY;

    @Schema(description = "方法 upload 上报  apply 忽略  release 离开")
    private List<EqumentWoItem> items = new ArrayList<>();

}