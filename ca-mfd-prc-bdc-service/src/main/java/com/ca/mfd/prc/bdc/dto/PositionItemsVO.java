package com.ca.mfd.prc.bdc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * LaneCacheVO class
 *
 * @author luowenbing
 * @date 2023/09/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "PositionItemsVO")
public class PositionItemsVO implements Serializable {
    /**
     * 坐标x
     */
    private Integer positionX;

    /**
     * 坐标y
     */
    private Integer positionY;

    /**
     * 坐标z
     */
    private Integer positionZ;
}
