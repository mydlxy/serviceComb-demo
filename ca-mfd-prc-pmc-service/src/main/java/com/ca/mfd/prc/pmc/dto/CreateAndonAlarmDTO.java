package com.ca.mfd.prc.pmc.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;

/**
 * @author inkelink
 * @date 2023年4月4日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "")
public class CreateAndonAlarmDTO {
    private String description = StringUtils.EMPTY;
    private String postion = StringUtils.EMPTY;
    private Integer type = 0;
    private Boolean isStart = false;
}
