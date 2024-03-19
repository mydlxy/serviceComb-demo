package com.ca.mfd.prc.pmc.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author inkelink
 * @date 2023年4月4日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "")
public class TeamleaderFillInfoDTO {
    private String id;

    private String stopCodeId;
}
