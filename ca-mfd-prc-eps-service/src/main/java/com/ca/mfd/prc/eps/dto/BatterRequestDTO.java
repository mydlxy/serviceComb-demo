package com.ca.mfd.prc.eps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * BatterRequestDTO
 *
 * @author inkelink
 * @since 1.0.0 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "BatterRequestDTO")
public class BatterRequestDTO {

    @Schema(description = "datas")
    private List<VinModel> datas;
}

