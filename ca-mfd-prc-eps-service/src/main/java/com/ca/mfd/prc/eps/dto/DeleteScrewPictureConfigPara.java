package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * DeleteScrewPictureConfigPara
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "DeleteScrewPictureConfigPara", description = "")
public class DeleteScrewPictureConfigPara implements Serializable {

    @Schema(description = "jobNo")
    private String jobNo = StringUtils.EMPTY;

    @Schema(description = "执行结果(1 OK 2 NOK)")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long pmWoId = Constant.DEFAULT_ID;

}