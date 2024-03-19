package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.eps.entity.EpsCarrierLogEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * MaterialPutPara
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "MaterialPutPara", description = "")
public class MaterialPutPara implements Serializable {

    @Schema(description = "熔炉编号")
    private String furnaceNo = StringUtils.EMPTY;

    @Schema(description = "批次;从LMS获取")
    private String lotNo = StringUtils.EMPTY;

    @Schema(description = "投料类型;1、新料 2、废料")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer putType = 0;

    @Schema(description = "来源;1、手动 2自动")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer source = 1;

    @Schema(description = "请求标识")
    private String requsetFlag = StringUtils.EMPTY;

    @Schema(description = "重量")
    private BigDecimal weight = BigDecimal.ZERO;

}