package com.ca.mfd.prc.eps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * VinModel
 *
 * @author inkelink
 * @date 2023/09/05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "VinModel")
public class VinModel {
    private String vin;
}
