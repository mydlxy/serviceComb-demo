package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * OperWosCacheInfo
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "OperWosCacheInfo", description = "")
public class OperWosCacheInfo implements Serializable {

    @Schema(description = "产品编号集合")
    private List<String> sns;

    @Schema(description = "车间编号")
    private String shopCode = StringUtils.EMPTY;

}