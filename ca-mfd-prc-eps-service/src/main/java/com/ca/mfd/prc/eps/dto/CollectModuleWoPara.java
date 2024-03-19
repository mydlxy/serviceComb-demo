package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CollectModuleWoPara
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "CollectModuleWoPara", description = "")
public class CollectModuleWoPara implements Serializable {

    @Schema(description = "唯一码")
    private String sn = StringUtils.EMPTY;

    @Schema(description = "工号")
    private String workstationCode = StringUtils.EMPTY;

    @Schema(description = "产品类型")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer productType = 0;

    @Schema(description = "操作名称")
    private String woName = StringUtils.EMPTY;

    @Schema(description = "结果：0 未知 1 OK 2 NG")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer result = 0;

    @Schema(description = "明细")
    private List<CollectModuleWoItem> items = new ArrayList<>();
}