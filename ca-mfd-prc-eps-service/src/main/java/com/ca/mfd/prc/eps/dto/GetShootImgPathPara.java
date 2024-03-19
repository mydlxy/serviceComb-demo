package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * GetShootImgPathPara
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "GetShootImgPathPara", description = "")
public class GetShootImgPathPara implements Serializable {

    @Schema(description = "woId")
    private String woId = StringUtils.EMPTY;

}