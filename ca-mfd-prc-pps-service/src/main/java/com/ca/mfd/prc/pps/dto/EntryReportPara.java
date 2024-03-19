package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * EntryReportPara
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Data
@Schema(title = "EntryReportPara", description = "")
public class EntryReportPara {

    @Schema(description = "EntryId")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long entryId;

    @Schema(description = "AreaCode")
    private String areaCode;

    @Schema(description = "WorkplaceName")
    private String workplaceName;

    @Schema(description = "1 合格 2 不良  3 报废")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer reportType;

    @Schema(description = "报工数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer reportCount;

    @Schema(description = "报工份数")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer num;

    @Schema(description = "合格数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer qualifiedQuantity;

    @Schema(description = "不合格数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer unqualifiedQuantity;

    @Schema(description = "报废数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer scrapQuanitiy;
}
