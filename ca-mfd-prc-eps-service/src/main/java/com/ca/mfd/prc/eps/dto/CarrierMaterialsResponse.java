package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.eps.entity.EpsCarrierLogEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CarrierMaterialsResponse
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "CarrierMaterialsResponse", description = "")
public class CarrierMaterialsResponse implements Serializable {

    @Schema(description = "载具条码")
    private String carrierCode = StringUtils.EMPTY;

    @Schema(description = "计划物料号")
    private String materialNo = StringUtils.EMPTY;

    @Schema(description = "容量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer capacity = 0;

    @Schema(description = "实际数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer practicalCount = 0;

    @Schema(description = "装载的物料")
    private List<EpsCarrierLogEntity> materials = new ArrayList<>();

}