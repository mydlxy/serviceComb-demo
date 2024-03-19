package com.ca.mfd.prc.pm.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author inkelink
 * @Description: AS产线日历
 * @date 2023年10月18日
 * @变更说明 BY inkelink At 2023年10月18日
 */
@Data
@Schema(description = "AS产线日历")
public class MidAsLineCalendarDto implements Serializable {


    /**
     * 工厂组织代码
     */
    @Schema(title = "工厂组织代码")
    private String organizationCode = StringUtils.EMPTY;

    /**
     * 计划编号
     */
    @Schema(title = "车间代码")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 生产线代码
     */
    @Schema(title = "生产线代码")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 自然日(yyyyMMdd)
     */
    @Schema(title = "自然日(yyyyMMdd)")
    private String dateCode = StringUtils.EMPTY;

    /**
     * 班次代码
     */
    @Schema(title = "班次代码")
    private String shiftCode = StringUtils.EMPTY;

    /**
     * 日工作模式代码
     */
    @Schema(title = "日工作模式代码")
    private String shiftModeCode = StringUtils.EMPTY;

    /**
     * 是否休息日 '0'：工作日，'1'：休息日
     */
    @Schema(title = "是否休息日 '0'：工作日，'1'：休息日")
    private String restFlag = StringUtils.EMPTY;


}