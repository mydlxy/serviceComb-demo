package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * ProcessArea
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-10-24
 */
@Data
@Schema(title = "ProcessArea", description = "工序下面工作区域")
public class ProcessArea {
    /**
     * 线体代码
     */
    @Schema(description = "线体代码")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 线体名称
     */
    @Schema(description = "线体名称")
    private String lineName = StringUtils.EMPTY;

    /**
     * 预计开始时间
     */
    @Schema(description = "预计开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date startTime;

    /**
     * 预计结束时间
     */
    @Schema(description = "预计结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date endTime;

    /**
     * 0、过程工艺 1、首道工艺 2末道工艺
     */
    @Schema(description = "工艺类型：0、过程工艺 1、首道工艺 2末道工艺")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int processType;
    /**
     * 生产数量
     */
    @Schema(description = "生产数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int count = 0;
}
