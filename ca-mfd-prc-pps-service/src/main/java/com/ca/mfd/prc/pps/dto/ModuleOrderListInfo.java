package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * ModuleOrderListInfo
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "ModuleOrderListInfo", description = "")
public class ModuleOrderListInfo {

    /**
     * 计划号
     */
    @Schema(description = "计划号")
    private String planNo;

    /**
     * 电池型号
     */
    @Schema(description = "电池型号")
    private String packModel;

    /**
     * 电池描述
     */
    @Schema(description = "电池描述")
    private String packDes;

    /**
     * 计划产量
     */
    @Schema(description = "计划产量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int planQty;

    /**
     * 拆分数量
     */
    @Schema(description = "拆分数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int splitQty;

    /**
     * 完成数量
     */
    @Schema(description = "完成数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int finishQty;

    /**
     * 预计上线时间
     */
    @Schema(description = "预计上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date estimatedStartDt;

    /**
     * 预计下线时间
     */
    @Schema(description = "预计下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date estimatedEndDt;
}
