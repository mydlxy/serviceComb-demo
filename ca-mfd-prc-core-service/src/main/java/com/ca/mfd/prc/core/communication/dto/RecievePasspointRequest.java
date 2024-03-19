package com.ca.mfd.prc.core.communication.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author inkelink
 * @Description: RecievePasspointRequest
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@Schema(description = "AVI站点")
public class RecievePasspointRequest implements Serializable {

    /**
     *
     */
    @Schema(title = "唯一标识")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @NotNull(message = "ID不能为空")
    private Long id = Constant.DEFAULT_ID;

    /**
     * 过点编码
     */
    @Schema(title = "过点编码")
    @NotBlank(message = "AVI代码不能为空")
    @Length(message = "AVI代码长度不能超过50位", max = 50)
    private String pointCode = StringUtils.EMPTY;

    /**
     * 过点类型 0：正常，1：CutIn，2：CutOut
     */
    @Schema(title = "过点类型 0：正常，1：CutIn，2：CutOut")
    @NotNull(message = "过点类型不能为空")
    @Range(message = "过点类型不正确", min = 0, max = 2)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer recordType;

    /**
     * 产品标识码Sn码
     */
    @Schema(title = "产品标识码Sn码")
    @NotBlank(message = "产品标识码不能为空")
    @Length(message = "产品标识码长度不能超过50位", max = 50)
    private String productNo = StringUtils.EMPTY;

    /**
     * 过点时间 格式：yyyy-MM-dd HH24:MI: SS
     */
    @Schema(title = "过点时间 格式：yyyy-MM-dd HH24:MI: SS")
    @NotNull(message = "过点时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date passTime;

}