package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * GetUnitRelationInfo
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "GetUnitRelationInfo", description = "")
public class GetUnitRelationInfo implements Serializable {

    @Schema(description = "moduleCode")
    private String moduleCode = StringUtils.EMPTY;

    @Schema(description = "moduleBarcode")
    private String moduleBarcode = StringUtils.EMPTY;

    @Schema(description = "UnitCode")
    private String unitCode = StringUtils.EMPTY;

    @Schema(description = "UnitBarcode")
    private String unitBarcode = StringUtils.EMPTY;

    @Schema(description = "status")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 1;

    @Schema(description = "key")
    private String key = StringUtils.EMPTY;

    @Schema(description = "cells")
    private List<GetCellRelationInfo> cells = new ArrayList<>();


}