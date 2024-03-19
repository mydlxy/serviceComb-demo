package com.ca.mfd.prc.pm.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author inkelink
 * @Description: AS产线日历请求
 * @date 2023年10月18日
 * @变更说明 BY inkelink At 2023年10月18日
 */
@Data
@Schema(description = "AS产线日历请求")
public class MidAsLineCalendarReq implements Serializable {


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
     * 更新时间
     */
    @Schema(title = "更新时间")
    private String updateTime = StringUtils.EMPTY;
}