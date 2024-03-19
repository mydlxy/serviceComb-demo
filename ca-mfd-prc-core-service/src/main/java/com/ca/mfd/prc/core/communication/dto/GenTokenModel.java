package com.ca.mfd.prc.core.communication.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author inkelink
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "GenTokenModel")
public class GenTokenModel {

    private String uri;

    private String appId;

    private String secret;
}
