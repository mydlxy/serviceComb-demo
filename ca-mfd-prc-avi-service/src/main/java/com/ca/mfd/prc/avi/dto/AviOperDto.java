package com.ca.mfd.prc.avi.dto;

import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 前端AVI站点对象数据
 * <p>
 * AviOperDto class
 *
 * @author luowenbing
 * @date 2023/04/06
 */
@Data
public class AviOperDto {

    /**
     * ID
     */
    @Schema(title = "ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcAviOperId;

    /**
     * avid代码
     */
    @Schema(title = "avid代码")
    private String aviCode = StringUtils.EMPTY;

    /**
     * avi 名称
     */
    @Schema(title = "avi 名称")
    private String aviName = StringUtils.EMPTY;

    /**
     * 行为代码
     */
    @Schema(title = "行为代码")
    private String action = StringUtils.EMPTY;

    /**
     * 行为代码
     */
    @Schema(title = "行为名称")
    private String actionName = StringUtils.EMPTY;

    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isEnabled;

    /**
     * 是否允许重复执行
     */
    @Schema(title = "是否允许重复执行")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isRepeat;
    /// <summary>
    /// 是否产生行为
    /// </summary>
    /**
     * ID
     */
    @Schema(title = "ID")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isProcess;

    /**
     * 备注信息
     */
    @Schema(title = "备注信息")
    private String message = StringUtils.EMPTY;
}
