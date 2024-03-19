package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * UpdateVerifyTypePara
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "UpdateVerifyTypePara", description = "")
public class UpdateVerifyTypePara implements Serializable {

    @Schema(description = "id")
    private String id = StringUtils.EMPTY;

    @Schema(description = "verifyType")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @JsonAlias(value = {"VerifyType","verifyType"})
    private Integer verifyType = 0;

}