package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * StampingEntryDTO
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "StampingEntryDTO", description = "")
public class StampingEntryDTO {

    @Schema(description = "工单编号")
    private String entryId = StringUtils.EMPTY;

    @Schema(description = "计划单号")
    private String planNo = StringUtils.EMPTY;

    @Schema(description = "订单号")
    private String orderNo = StringUtils.EMPTY;

    @Schema(description = "工单号")
    private String entryNo = StringUtils.EMPTY;

    @Schema(description = "物料号")
    private String materialNo = StringUtils.EMPTY;

    @Schema(description = "物料描述")
    private String materialDes = StringUtils.EMPTY;

    @Schema(description = "预计上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date estimatedStartDt;

    @Schema(description = "预计下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date estimatedEndDt;

    @Schema(description = "实际上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date actualStartDt;

    @Schema(description = "实际下下时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date actualEndDt;

    @Schema(description = "工单计划数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer entryQuantity = 1;

    @Schema(description = "工单生产数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer orderQuantity = 0;

    @Schema(description = "合格数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer qualifiedQuanitiy = 0;

    @Schema(description = "不良数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer unqualifiedQuanitiy = 0;

    @Schema(description = "报废数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer scrapQuanitiy = 0;

    @Schema(description = "状态 冲压： 1 未生产  2 待报工 3 正在生产 4 已经完成")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;

}