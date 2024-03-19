package com.ca.mfd.prc.avi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "AviQueueItems")
public class AviQueueItems implements Serializable {

    /**
     * 队列ID
     */
    @Schema(title = "ID")
    private Long id;

    /**
     * 队列编码
     */

    private String queueCode;

    /**
     * 队列名称
     */
    private String queueName;
}
