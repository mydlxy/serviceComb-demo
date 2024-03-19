package com.ca.mfd.prc.pm.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 从AS班次中间表获得的日历信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "从AS班次中间表获得的日历信息")
public class CalendarFromASDTO {
    @Schema(title = "车间编码")
    private String shopCode;

    /**
     * 线体编码，如果是车间日历，该字段不传
     */
    @Schema(title = "线体编码")
    private String lineCode;

    @Schema(title = "班次编码")
    private String shiftCode;

    @Schema(title = "自然日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date date;


    /**
     * 车间名称 调用方不用传，业务处理时中转用
     */
    @Schema(title = "车间名称")
    private String shopName;

    /**
     * 线体名称，调用方不用传，业务处理时中转用
     */
    @Schema(title = "线体名称")
    private String lineName;


    /**
     * 一个类型，跟mom没关系，不用管
     */
    /*@Schema(title = "工作日")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private String workDate;*/


}
