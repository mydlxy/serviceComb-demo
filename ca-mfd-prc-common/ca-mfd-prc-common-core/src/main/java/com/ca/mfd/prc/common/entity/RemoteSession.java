package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * RemoteSession class
 *
 * @author inkelink
 * @date 2023/04/03
 */
@Schema(title = "RemoteSession", description = "RemoteSession")
@Data
public class RemoteSession implements Serializable {

    @Schema(title = "id")
    private String id;

    @Schema(title = "key")
    private String key;

    @Schema(title = "time")
    private String time;

    @Schema(title = "locked")
    private Boolean locked;

    @Schema(title = "localMd5")
    private String localMd5;
}

