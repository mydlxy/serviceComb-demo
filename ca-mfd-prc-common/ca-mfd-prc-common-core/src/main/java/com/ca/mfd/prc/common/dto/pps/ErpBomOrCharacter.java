package com.ca.mfd.prc.common.dto.pps;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ErpBomOrCharacter
 *
 * @author inkelink eric.zhou
 * @date 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "erp请求")
public class ErpBomOrCharacter {

    @Schema(title = "整车物料号")
    private String productMaterialNo;

}
