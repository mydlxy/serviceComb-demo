package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * SetLinePara
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "SetLinePara", description = "")
public class SetLinePara implements Serializable {

    @Schema(description = "LineCode")
    private String lineCode = StringUtils.EMPTY;

    @Schema(description = "订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7:备件")
    private String orderCategory = StringUtils.EMPTY;

}