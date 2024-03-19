package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 操作缓存单个工艺
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "操作缓存单个工艺", description = "")
public class OperWosCacheStatusInfo implements Serializable {

    @Schema(description = "产品编号")
    private String sn = StringUtils.EMPTY;

    @Schema(description = "车间编号")
    private String woCode = StringUtils.EMPTY;

    @Schema(description = "结果 0 未做 1 成功 2 失败 3 Bypass")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer result = 0;

}