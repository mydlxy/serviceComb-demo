package com.ca.mfd.prc.pmc.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author inkelink
 * @date 2023年4月4日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "")
public class AndonSourceDataDTO {
    private String position;

    private String areaCode;

    private String stationName;

    private String workplaceName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date startDt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date endDt;

    private Integer alarmLevel;

    private String description;

    private Integer duration;

    private Integer type;
}
