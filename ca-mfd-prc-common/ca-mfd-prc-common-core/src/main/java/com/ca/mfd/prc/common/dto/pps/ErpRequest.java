package com.ca.mfd.prc.common.dto.pps;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * erp请求
 *
 * @author inkelink eric.zhou
 * @date 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "erp请求")
public class ErpRequest implements Serializable {

    @Schema(title = "comp")
    private String comp = "108";

    public ErpRequest() {
        this.comp = "108";
    }

}
