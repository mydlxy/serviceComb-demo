package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * CollectBarcodePara
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "CollectBarcodePara", description = "")
public class CollectBarcodePara implements Serializable {

    @Schema(description = "id")
    private String id = StringUtils.EMPTY;

    @Schema(description = "当前收集条码")
    private String nowBarcode = StringUtils.EMPTY;

    @Schema(description = "执行结果(1 OK 2 NOK)")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer execResult = 0;

    @Schema(description = "执行追溯失败异常信息")
    private String errorMessage = StringUtils.EMPTY;
}