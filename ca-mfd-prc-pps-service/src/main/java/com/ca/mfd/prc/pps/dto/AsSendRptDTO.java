package com.ca.mfd.prc.pps.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * AsSendRptDTO
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2024-03-14
 */
@Data
@Schema(title = "AsSendRptDTO", description = "")
public class AsSendRptDTO {

    @Schema(description = "车辆识别码")
    private String sn;

    @Schema(title = "焊接上线计划时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date passDt1;
    @Schema(title = "焊接上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date insertDt1;
    @Schema(description = "时间差(分钟)")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer times1 = 0;

    @Schema(title = "焊接计划下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date passDt2;
    @Schema(title = "焊接下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date insertDt2;
    @Schema(description = "时间差(分钟)")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer times2 = 0;

    @Schema(title = "涂装计划上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date passDt3;
    @Schema(title = "涂装上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date insertDt3;
    @Schema(description = "时间差(分钟)")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer times3 = 0;

    @Schema(title = "涂装计划下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date passDt4;
    @Schema(title = "涂装下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date insertDt4;
    @Schema(description = "时间差(分钟)")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer times4 = 0;


    @Schema(title = "总装计划上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date passDt5;
    @Schema(title = "总装上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date insertDt5;
    @Schema(description = "时间差(分钟)")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer times5 = 0;


    @Schema(title = "总装计划下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date passDt6;
    @Schema(title = "总装下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date insertDt6;
    @Schema(description = "时间差(分钟)")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer times6 = 0;

}
