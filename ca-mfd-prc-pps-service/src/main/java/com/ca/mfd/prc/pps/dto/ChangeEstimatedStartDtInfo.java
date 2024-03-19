package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * ChangeEstimatedStartDtInfo
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "ChangeEstimatedStartDtInfo", description = "")
public class ChangeEstimatedStartDtInfo {

    @Schema(description = "需要更新的数据ID")
    private List<String> ids;

    @Schema(description = "预计上线时间")
    @JsonAlias(value = {"EstimatedStartDt", "estimatedStartDt"})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date estimatedStartDt;
}
