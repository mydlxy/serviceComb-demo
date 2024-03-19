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
 * @Description: AS班次信息
 * @date 2023年10月18日
 * @变更说明 BY inkelink At 2023年10月18日
 */
@Data
@Schema(description = "AS班次信息")
public class MidAsShcShiftDto implements Serializable {

    /**
     * 跨天标识 0：不夸天 1跨天
     */
    @Schema(title = "跨天标识 0：不夸天 1跨天")
    private String isCross = StringUtils.EMPTY;

    /**
     * 日工作模式代码
     */
    @Schema(title = "日工作模式代码")
    private String shiftModeCode = StringUtils.EMPTY;

    /**
     * 班次代码
     */
    @Schema(title = "班次代码")
    private String shiftCode = StringUtils.EMPTY;

    /**
     * 开始时间
     */
    @Schema(title = "开始时间")
    private String frameBegin = StringUtils.EMPTY;
    /**
     * 结束时间
     */
    @Schema(title = "结束时间")
    private String frameEnd = StringUtils.EMPTY;

}