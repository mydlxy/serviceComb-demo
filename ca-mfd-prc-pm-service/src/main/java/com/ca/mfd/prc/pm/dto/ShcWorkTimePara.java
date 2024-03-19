package com.ca.mfd.prc.pm.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
@Schema(description = "工厂日历查询参数")
public class ShcWorkTimePara {

    @Schema(title = "工作日")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date workDay;

    @Schema(title = "班次ID")
    private String shopCode = StringUtils.EMPTY;

    @Schema(title = "班次ID")
    private Integer dayas = 0;
}
